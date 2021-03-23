package net.nativo.reactsdk.ntvsdkmanager;

import android.app.Activity;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.util.ReactFindViewUtil;

import net.nativo.reactsdk.ntvadapter.RNNtvSectionAdapter;
import net.nativo.reactsdk.ntvadapter.RNNtvSectionAdapterManager;
import net.nativo.reactsdk.ntvutil.ViewFinder;
import net.nativo.sdk.NativoSDK;
import net.nativo.sdk.ntvconstant.NativoAdType;
import net.nativo.sdk.ntvcore.NtvAdData;
import net.nativo.sdk.ntvcore.NtvCache;
import net.nativo.sdk.ntvcore.NtvSectionConfig;
import net.nativo.sdk.ntvlog.Logger;
import net.nativo.sdk.ntvlog.LoggerFactory;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.nativo.reactsdk.ntvutil.RNConstants.EVENT_AD_DISPLAY_CLICKOUT_PAGE;
import static net.nativo.reactsdk.ntvutil.RNConstants.EVENT_AD_DISPLAY_LANDING_PAGE;
import static net.nativo.reactsdk.ntvutil.RNConstants.EVENT_AD_FAILED_TO_LOAD;
import static net.nativo.reactsdk.ntvutil.RNConstants.EVENT_AD_LOADED;

public class RNAdContainerManager extends ViewGroupManager<NativoAdView> {

    private static final String TAG = RNAdContainerManager.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger(TAG);

    public static final int COMMAND_PLACE_AD_IN_VIEW = 5;
    public static final int COMMAND_PREFETCH_AD = 10;

    public static Activity currentactivity;
    public static ReactContext containerReactContext;

    @Nonnull
    @Override
    public String getName() {
        return "NativoContainer";
    }

    @Nonnull
    @Override
    protected NativoAdView createViewInstance(@Nonnull ThemedReactContext reactContext) {
        currentactivity = reactContext.getCurrentActivity();
        containerReactContext = reactContext;
        return new NativoAdView(reactContext);
    }

    @ReactProp(name = "sectionUrl")
    public void setSectionUrl(View nativoAdView, ReadableMap map) {
        LOG.debug("section url " + map.getString("url") + " index " + map.getInt("index"));
        String sectionUrl = map.getString("url");
        Integer locationId = map.getInt("index");
        RNNtvSectionAdapterManager.getInstance()
                .getNtvSectionAdapter(sectionUrl, locationId)
                .setNativoAdView(nativoAdView);
    }


    @Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        MapBuilder.Builder<String, Object> builder = MapBuilder.builder();
        String[] events = {
                EVENT_AD_LOADED,
                EVENT_AD_FAILED_TO_LOAD,
                EVENT_AD_DISPLAY_LANDING_PAGE,
                EVENT_AD_DISPLAY_CLICKOUT_PAGE
        };
        for (int i = 0; i < events.length; i++) {
            builder.put(events[i], MapBuilder.of("registrationName", events[i]));
        }
        return builder.build();
    }

    @androidx.annotation.Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of(
                "placeAdInView",
                COMMAND_PLACE_AD_IN_VIEW,
                "prefetchAd",
                COMMAND_PREFETCH_AD
        );
    }

    @Override
    public void receiveCommand(@Nonnull NativoAdView root, int commandId, @Nullable ReadableArray args) {
        View nativeContainerParent = ViewFinder.getInstance().findPublisherAdContainerInUpperHierarchy(root);
        if (nativeContainerParent == null) {
            nativeContainerParent = ViewFinder.getInstance().findPublisherAdContainer(currentactivity);
        }
        RNNtvSectionAdapter ntvSectionAdapter;

        switch (commandId) {
            case COMMAND_PLACE_AD_IN_VIEW:
                if (nativeContainerParent == null) {
                    LOG.error("publisherNativoAdContainer view not found. Looked upto 7 parents");
                    return;
                }
                String paivSectionUrl = args != null ? args.getString(1) : "";
                int paivIndex = args.getInt(0);
                int adId = args.getInt(2);
                LOG.debug("placeAdInView called for section: " + paivSectionUrl + " index: " + paivIndex);
                View adView = null;
                View nativeContainer = ReactFindViewUtil.findView(root, "nativoAdView");
                if (nativeContainer != null) {
                    adView = nativeContainer;
                } else {
                    adView = root;
                }
                ntvSectionAdapter = RNNtvSectionAdapterManager.getInstance()
                        .getNtvSectionAdapter(paivSectionUrl, paivIndex);
                ntvSectionAdapter.setAdID(adId);
                NativoSDK.placeAdInView(adView, (ViewGroup) nativeContainerParent, paivSectionUrl, paivIndex, ntvSectionAdapter, null);
                forceAdTracking(nativeContainerParent);
                break;
            case COMMAND_PREFETCH_AD:
                String prefetchSectionUrl = args != null ? args.getString(1) : "";
                int prefetchIndex = args.getInt(0);
                ntvSectionAdapter = RNNtvSectionAdapterManager.getInstance().getNtvSectionAdapter(prefetchSectionUrl, prefetchIndex);
                LOG.debug("prefetch called for section: " + prefetchSectionUrl + " index: " + prefetchIndex);
                NativoAdType adType = null;
                if (nativeContainerParent != null) {
                    adType = NativoSDK.getAdTypeForIndex(prefetchSectionUrl, (ViewGroup) nativeContainerParent, prefetchIndex);
                }
                if (nativeContainerParent == null || (adType != null && adType.equals(NativoAdType.AD_TYPE_NONE))) {
                    NativoSDK.prefetchAdForSection(prefetchSectionUrl, ntvSectionAdapter, null);
                } else {
                    NtvSectionConfig ntvSectionConfig = NtvCache.getInstance().getSectionForUrl(prefetchSectionUrl);
                    NtvAdData adData = NtvCache.getInstance().getMappedAdData(ntvSectionConfig, (ViewGroup) nativeContainerParent, prefetchIndex);
                    if (adData.isAdContentAvailable()) {
                        ntvSectionAdapter.onReceiveAd(prefetchSectionUrl, adData);
                    } else {
                        ntvSectionAdapter.onFail(prefetchSectionUrl);
                    }

                }
        }
    }

    private void forceAdTracking(final View nativeContainerParent) {
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                if (nativeContainerParent != null) {
                    if (nativeContainerParent.getOnFocusChangeListener() != null) {
                        nativeContainerParent.getOnFocusChangeListener().onFocusChange(null, true);
                    }
                }

            }
        });
    }
}
