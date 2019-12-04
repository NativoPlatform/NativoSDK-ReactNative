package net.nativo.reactsdk;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.uimanager.util.ReactFindViewUtil;
import com.facebook.react.views.view.ReactViewGroup;

import net.nativo.sdk.NativoSDK;
import net.nativo.sdk.ntvadtype.NtvBaseInterface;
import net.nativo.sdk.ntvcore.NtvAdData;
import net.nativo.sdk.ntvcore.NtvSectionAdapter;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

class NativoAdView extends ReactViewGroup {

    public NativoAdView(Context context) {
        super(context);
    }

    public void sendEvent(String name, @Nullable WritableMap event) {
        ReactContext reactContext = (ReactContext) getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                getId(),
                name,
                event);
    }
}

public class RNAdContainerManager extends ViewGroupManager<NativoAdView> implements NtvSectionAdapter {

    public static final String EVENT_AD_LOADED = "onAdLoaded";
    public static final String EVENT_AD_FAILED_TO_LOAD = "onAdFailed";

    public static final int COMMAND_PLACE_AD_IN_VIEW = 5;
    public static final int COMMAND_PREFETCH_AD = 10;

    public static Activity currentactivity;
    public static ReactContext containerReactContext;
    private ThemedReactContext themedReactContext;
    public static final String SECTION_URL = "http://www.nativo.net/test/";
    View RNContainer;
    Map<Integer, View> viewMap = new HashMap<>();
    Map<Integer, Integer> containerAdIdmap = new HashMap<>();

    String sectionUrl;
    int index;

    @Nonnull
    @Override
    public String getName() {
        return "NativoContainer";
    }

    @Nonnull
    @Override
    protected NativoAdView createViewInstance(@Nonnull ThemedReactContext reactContext) {
        currentactivity = reactContext.getCurrentActivity();
        themedReactContext = reactContext;
        containerReactContext = reactContext;
        NativoAdView nativoAdView = new NativoAdView(reactContext);
        return nativoAdView;
    }

    @ReactProp(name = "sectionUrl")
    public void setSectionUrl(View container, ReadableMap map) {
        Log.d(RNAdContainerManager.class.getName(), "sectionURL: " + map.getString("url") + " with index " + map.getInt("index"));
        sectionUrl = map.getString("url");
        index = map.getInt("index");
        RNContainer = container;
        viewMap.put(index, container);
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
        sendEvent(themedReactContext, "needsDisplayLandingPage", params);
    }

    @Override
    public void needsDisplayClickOutURL(String s, String s1) {
        WritableMap params = Arguments.createMap();
        params.putString("url", s1);
        sendEvent(themedReactContext, "needsDisplayClickOutURL", params);

        Log.d(getClass().getName(), "needsDisplayClickOutURL: done");
    }

    @Override
    public void hasbuiltView(View view, NtvBaseInterface ntvBaseInterface, NtvAdData ntvAdData) {
        View nativeContainerParent = findPublisherAdContainer();
        containerAdIdmap.put(ntvAdData.getAdID(), nativeContainerParent.hashCode());

    }

    @Override
    public void onReceiveAd(String s, int i, NtvAdData ntvAdData) {
        Log.d(getClass().getName(), "onReceiveAd: for index " + i);
        View view = viewMap.get(i);

        // callback to js
        WritableMap event = Arguments.createMap();
        event.putString("adType", ntvAdData.getAdType().toString());
        event.putString("adDescription", ntvAdData.getPreviewText());
        event.putString("adTitle", ntvAdData.getTitle());
        event.putString("adAuthorName", ntvAdData.getAuthorName());
        event.putString("adDate", ntvAdData.getDate().toString());
        sendEvent(EVENT_AD_LOADED, (NativoAdView) view, event);

        Log.d(getClass().getName(), "onReceiveAd: done");

    }

    @Override
    public void onFail(String s, int i) {
        Log.d(getClass().getName(), "onFail: ");
        View view = viewMap.get(i);
        WritableMap event = Arguments.createMap();
        sendEvent(EVENT_AD_FAILED_TO_LOAD, (NativoAdView) view, event);
    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        MapBuilder.Builder<String, Object> builder = MapBuilder.builder();
        String[] events = {
                EVENT_AD_LOADED,
                EVENT_AD_FAILED_TO_LOAD
        };
        for (int i = 0; i < events.length; i++) {
            builder.put(events[i], MapBuilder.of("registrationName", events[i]));
        }
        return builder.build();
    }

    private void sendEvent(String name, NativoAdView adView, @Nullable WritableMap event) {
        adView.sendEvent(name, event);
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
        View nativeContainerParent = findPublisherAdContainer();

        if (nativeContainerParent == null) {
            Log.e(getClass().getName(), "publisherNativoAdContainer view not found. Looked upto 4 parents");
            nativeContainerParent = root;
        }

        switch (commandId) {
            case COMMAND_PLACE_AD_IN_VIEW:
                View adView = null;
                View nativeContainer = ReactFindViewUtil.findView(root, "nativoAdView");
                View videoContainer = ReactFindViewUtil.findView(root, "nativoVideoAdView");
                View sdContainer = ReactFindViewUtil.findView(root, "nativoSDAdView");
                if (nativeContainer != null) {
                    adView = nativeContainer;
                } else if (videoContainer != null) {
                    adView = videoContainer;
                } else if (sdContainer != null) {
                    adView = sdContainer;
                } else {
                    adView = root;
                }


                index = args.getInt(0);
                Log.d(RNAdContainerManager.class.getName(), "receiveCommand: for index " + index);
                NativoSDK.getInstance().placeAdInView(adView, (ViewGroup) nativeContainerParent, sectionUrl, index, this, null);
                forceAdTracking();
                break;
            case COMMAND_PREFETCH_AD:
                index = args.getInt(0);
                NativoSDK.getInstance().prefetchAdForSection(SECTION_URL, (ViewGroup) nativeContainerParent, index, this, null);
        }
    }

    private View findPublisherAdContainer() {
        View nativeContainerParent = null;
        View view = currentactivity.getWindow().getDecorView().findViewById(android.R.id.content);
        nativeContainerParent = ReactFindViewUtil.findView(view, "publisherNativoAdContainer");
        return nativeContainerParent;
    }

    private void forceAdTracking() {
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                View nativeContainerParent = findPublisherAdContainer();
                if (nativeContainerParent != null) {
                    if (nativeContainerParent.getOnFocusChangeListener() != null) {
                        nativeContainerParent.getOnFocusChangeListener().onFocusChange(null, true);
                    }
                }

            }
        });
    }

}
