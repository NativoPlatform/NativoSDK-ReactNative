package net.nativo.reactsdk;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.util.ReactFindViewUtil;

import net.nativo.sdk.NativoSDK;
import net.nativo.sdk.ntvadtype.NtvBaseInterface;
import net.nativo.sdk.ntvcore.NtvAdData;
import net.nativo.sdk.ntvcore.NtvSectionAdapter;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RNAdContainerManager extends ViewGroupManager<RelativeLayout> implements NtvSectionAdapter {

    private static final String CONTAINER_HASH_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";

    public static Activity currentactivity;
    private ThemedReactContext themedReactContext;
    public static final String SECTION_URL = "http://www.nativo.net/test/";
    View RNContainer;
    Map<Integer, View> viewMap = new HashMap<>();
    Map<Integer, NtvAdData> containerAdDatamap = new HashMap<>();

    @Nonnull
    @Override
    public String getName() {
        return "NativoContainer";
    }

    @Nonnull
    @Override
    protected RelativeLayout createViewInstance(@Nonnull ThemedReactContext reactContext) {
        currentactivity = reactContext.getCurrentActivity();
        themedReactContext = reactContext;
        return new RelativeLayout(reactContext);
    }

    @ReactProp(name = "sectionUrl")
    public void setSectionUrl(View container, ReadableMap map) {
        Log.d(RNAdContainerManager.class.getName(), "sectionURL: " + map.getString("url") + " with index " + map.getInt("index"));
        String sectionUrl = map.getString("url");
        int index = map.getInt("index");
        RNContainer = container;
        viewMap.put(index, container);
        NativoSDK.getInstance().prefetchAdForSection(SECTION_URL, (ViewGroup) RNContainer, index, this, null);

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
        for (Map.Entry<Integer, NtvAdData> dataEntry : containerAdDatamap.entrySet()) {
            if (dataEntry.getValue().getAdID() == i) {
                params.putInt("containerHash", dataEntry.getKey());
            }
        }
        sendEvent(themedReactContext, "EventReminder", params);
    }

    @Override
    public void needsDisplayClickOutURL(String s, String s1) {

    }

    @Override
    public void hasbuiltView(View view, NtvBaseInterface ntvBaseInterface, NtvAdData ntvAdData) {

    }

    @Override
    public void onReceiveAd(String s, int i, NtvAdData ntvAdData) {
        Log.d(getClass().getName(), "onReceiveAd: for index " + i);
        View view = viewMap.get(i);
        containerAdDatamap.put(view.hashCode(), ntvAdData);
        View adView = ReactFindViewUtil.findView(view, "nativoAdView");
        NativoSDK.getInstance().placeAdInView(adView, (ViewGroup) view, SECTION_URL, i, this, null);
        Log.d(getClass().getName(), "onReceiveAd: done");
    }

    @Override
    public void onFail(String s, int i) {
        Log.d(getClass().getName(), "onFail: ");
    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }


}
