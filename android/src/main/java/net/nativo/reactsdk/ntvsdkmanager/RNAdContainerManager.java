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
    public void receiveCommand(@Nonnull final NativoAdView adView, int commandId, @Nullable final ReadableArray args) {

        switch (commandId) {
            case COMMAND_PLACE_AD_IN_VIEW:
                ViewFinder.getInstance().findPublisherAdContainer(adView, currentactivity, new ViewFinder.OnViewFoundListener() {
                    @Override
                    public void onViewFound(View pubContainer) {
                        if (args != null && args.size() > 2) {
                            String paivSectionUrl = args.getString(1);
                            int paivIndex = args.getInt(0);
                            int adId = args.getInt(2);
                            placeAdInView(adView, pubContainer, args, paivSectionUrl, adId, paivIndex);
                        } else {
                            LOG.debug("Invalid args for placeAdInView");
                        }
                    }
                });

                break;
            case COMMAND_PREFETCH_AD:
                if (args != null && args.size() > 1) {
                    View nativeContainerParent = ViewFinder.getInstance().findPublisherAdContainer(adView, currentactivity);
                    String prefetchSectionUrl = args.getString(1);
                    int prefetchIndex = args.getInt(0);
                    prefetch(nativeContainerParent, prefetchSectionUrl, prefetchIndex);
                } else {
                    LOG.debug("Invalid args for prefetch");
                }
        }
    }

    private void prefetch(View nativeContainerParent, String prefetchSectionUrl, int prefetchIndex) {
        RNNtvSectionAdapter ntvSectionAdapter = RNNtvSectionAdapterManager.getInstance().getNtvSectionAdapter(prefetchSectionUrl, prefetchIndex);
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
            if (adData != null && adData.isAdContentAvailable()) {
                ntvSectionAdapter.onReceiveAd(prefetchSectionUrl, adData, adData.getLocationIdentifier());
            } else {
                ntvSectionAdapter.onFail(prefetchSectionUrl, adData != null ? adData.getLocationIdentifier() : null);
            }
        }
    }

    private void placeAdInView(@Nonnull NativoAdView adView, View nativeContainerParent, @Nullable ReadableArray args, String paivSectionUrl, int adId, int paivIndex) {
        LOG.debug("placeAdInView called for section: " + paivSectionUrl + " index: " + paivIndex);
        View nativeAdView = adView;
        View nativeContainer = ReactFindViewUtil.findView(adView, "nativoAdView");
        if (nativeContainer != null) {
            nativeAdView = nativeContainer;
        }
        RNNtvSectionAdapter ntvSectionAdapter = RNNtvSectionAdapterManager.getInstance().getNtvSectionAdapter(paivSectionUrl, paivIndex);
        ntvSectionAdapter.setAdID(adId);
        NativoSDK.placeAdInView(nativeAdView, (ViewGroup) nativeContainerParent, paivSectionUrl, paivIndex, ntvSectionAdapter, null);
        forceAdTracking(nativeContainerParent);
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
