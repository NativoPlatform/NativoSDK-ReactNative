package net.nativo.reactsdk.ntvadapter;

import android.view.View;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import net.nativo.reactsdk.ntvsdkmanager.NativoAdView;
import net.nativo.reactsdk.ntvsdkmanager.RNAdContainerManager;
import net.nativo.reactsdk.ntvutil.ViewFinder;
import net.nativo.sdk.ntvadtype.NtvBaseInterface;
import net.nativo.sdk.ntvcore.NtvAdData;
import net.nativo.sdk.ntvcore.NtvSectionAdapter;
import net.nativo.sdk.ntvlog.Logger;
import net.nativo.sdk.ntvlog.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.annotation.Nullable;

import static net.nativo.reactsdk.ntvutil.RNConstants.EVENT_AD_DISPLAY_LANDING_PAGE;
import static net.nativo.reactsdk.ntvutil.RNConstants.EVENT_AD_FAILED_TO_LOAD;
import static net.nativo.reactsdk.ntvutil.RNConstants.EVENT_AD_LOADED;
import static net.nativo.sdk.ntvconstant.NtvConstants.NTV_BY;

public class SharedNtvSectionAdapter implements NtvSectionAdapter {

    private static final String TAG = SharedNtvSectionAdapter.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger(TAG);
    private static SharedNtvSectionAdapter instance;
    private Map<String, Queue<View>> viewMap = new HashMap<>();
    private Map<Integer, Integer> containerAdIdmap = new HashMap<>();
    private Map<Integer, NativoAdView> adidAdViewMap = new HashMap<>();

    private SharedNtvSectionAdapter() {
    }

    public static SharedNtvSectionAdapter getInstance() {
        if (instance == null) {
            instance = new SharedNtvSectionAdapter();
        }
        return instance;
    }

    @Override
    public boolean shouldPlaceAdAtIndex(String s, int i) {
        return true;
    }

    @Override
    public Class<?> registerLayoutClassForIndex(int i, NtvAdData.NtvAdTemplateType ntvAdTemplateType) {
        return null;
    }

    @Override
    public void needsDisplayLandingPage(String s, int i) {
        WritableMap params = Arguments.createMap();
        params.putString("sectionUrl", s);
        params.putInt("adId", i);
        params.putInt("containerHash", containerAdIdmap.get(i));
        sendEvent(EVENT_AD_DISPLAY_LANDING_PAGE, adidAdViewMap.get(i), params);
    }

    @Override
    public void needsDisplayClickOutURL(String s, String s1) {
        WritableMap params = Arguments.createMap();
        params.putString("url", s1);
        sendEvent(RNAdContainerManager.containerReactContext, "needsDisplayClickOutURL", params);
    }

    @Override
    public void hasbuiltView(View view, NtvBaseInterface ntvBaseInterface, NtvAdData ntvAdData) {
        View nativeContainerParent = ViewFinder.getInstance().findPublisherAdContainer(RNAdContainerManager.currentactivity);
        containerAdIdmap.put(ntvAdData.getAdID(), nativeContainerParent.hashCode());
    }

    @Override
    public void onReceiveAd(String s, NtvAdData ntvAdData) {
        Queue<View> viewQueue = viewMap.get(s);
        View view = viewQueue != null ? viewQueue.poll() : null;

        if (view == null) {
            LOG.error("onReceiveAd: view is null");
            return;
        }

        // callback to js
        WritableMap event = Arguments.createMap();
        event.putString("adType", ntvAdData.getAdType().toString());
        event.putString("adDescription", ntvAdData.getPreviewText());
        event.putString("adTitle", ntvAdData.getTitle());
        event.putString("adAuthorName", String.format("%s %s", NTV_BY, ntvAdData.getAuthorName()));
        event.putString("adDate", ntvAdData.getDate().toString());
        event.putString("adAuthorUrl", ntvAdData.getAuthorImageURL());
        event.putString("adImgUrl", ntvAdData.getPreviewImageURL());
        event.putInt("adAdID", ntvAdData.getAdID());

        if (ntvAdData.getAdType() == NtvAdData.NtvAdType.STANDARD_DISPLAY) {
            event.putInt("adDisplayWidth", ntvAdData.getStandardDisplayWidth());
            event.putInt("adDisplayHeight", ntvAdData.getStandardDisplayHeight());
        }

        sendEvent(EVENT_AD_LOADED, (NativoAdView) view, event);

    }

    @Override
    public void onFail(String s) {
        Queue<View> viewQueue = viewMap.get(s);
        View view = viewQueue != null ? viewQueue.poll() : null;
        if (view == null) {
            return;
        }
        WritableMap event = Arguments.createMap();
        sendEvent(EVENT_AD_FAILED_TO_LOAD, (NativoAdView) view, event);
    }

    private void sendEvent(String name, NativoAdView adView, @Nullable WritableMap event) {
        adView.sendEvent(name, event);
    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    public void addNativoAdViewToQueue(String sectionUrl, NativoAdView nativoAdView) {
        LOG.debug("section url " + sectionUrl + " nativoAdView hash " + nativoAdView.hashCode());
        Queue<View> viewQueue = viewMap.get(sectionUrl);
        if (viewQueue == null) {
            viewQueue = new LinkedList<>();
        }
        viewQueue.add(nativoAdView);
        viewMap.put(sectionUrl, viewQueue);
    }

    public void addNativoAdViewToAdIdMap(Integer adId, NativoAdView nativoAdView) {
        if (adidAdViewMap == null) {
            adidAdViewMap = new HashMap<>();
        }
        adidAdViewMap.put(adId, nativoAdView);
    }
}
