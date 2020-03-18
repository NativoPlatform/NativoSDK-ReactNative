
package net.nativo.reactsdk;

import android.view.View;
import android.webkit.WebView;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.util.ReactFindViewUtil;

import net.nativo.sdk.NativoSDK;
import net.nativo.sdk.ntvadtype.NtvBaseInterface;
import net.nativo.sdk.ntvcore.NtvAdData;
import net.nativo.sdk.ntvcore.NtvSectionAdapter;
import net.nativo.sdk.ntvlog.Logger;
import net.nativo.sdk.ntvlog.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class RNNativoSdkModule extends ReactContextBaseJavaModule implements NtvSectionAdapter {

    private static final String TAG = RNNativoSdkModule.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger(TAG);
    private Object defaultError = null;
    private Queue<Callback> callbacks = new LinkedList<>();
    boolean isTemplateRegistred = false;

    public RNNativoSdkModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "NativoSDK";
    }

    @ReactMethod
    public void registerTemplates() {
        if (!isTemplateRegistred) {
            NativoSDK.getInstance().registerNativeAd(new NativeAd());
            NativoSDK.getInstance().registerVideoAd(new NativeVideoAd());
            NativoSDK.getInstance().registerLandingPage(new NativeLandingPage());
            NativoSDK.getInstance().registerStandardDisplayAd(new StandardDisplayAd());
            isTemplateRegistred = true;
        }
    }


    @ReactMethod
    public void enableDevLogs() {
        NativoSDK.getInstance().enableDevLogs();
    }

    @ReactMethod
    public void enableTestAdvertisements(String s) {
        if (s.equals("NATIVE")) {
            NativoSDK.getInstance().enableTestAdvertisements(NtvAdData.NtvAdType.NATIVE);
        } else if (s.equals("DISPLAY")) {
            NativoSDK.getInstance().enableTestAdvertisements(NtvAdData.NtvAdType.CLICK_OUT);
        } else if (s.equals("CLICK_VIDEO")) {
            NativoSDK.getInstance().enableTestAdvertisements(NtvAdData.NtvAdType.IN_FEED_VIDEO);
        } else if (s.equals("SCROLL_VIDEO")) {
            NativoSDK.getInstance().enableTestAdvertisements(NtvAdData.NtvAdType.IN_FEED_AUTO_PLAY_VIDEO);
        } else if (s.equals("STANDARD_DISPLAY")) {
            NativoSDK.getInstance().enableTestAdvertisements(NtvAdData.NtvAdType.STANDARD_DISPLAY);
        } else if (s.equals("NO_FILL")) {
            NativoSDK.getInstance().enableTestAdvertisements(NtvAdData.NtvAdType.NO_FILL);
        } else {
            NativoSDK.getInstance().enableTestAdvertisements();
        }
    }

    @ReactMethod
    public void placeAdInWebView(final String sectionUrl) {
        LOG.debug("placeAdInWebView called for section " + sectionUrl);
        View view = getCurrentActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        final WebView webView = (WebView) ReactFindViewUtil.findView(view, "nativoMoapAdView");
        webView.post(new Runnable() {
            @Override
            public void run() {
                NativoSDK.getInstance().placeAdInWebView(webView, sectionUrl);
            }
        });
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put("NATIVE", "NATIVE");
        constants.put("DISPLAY", "DISPLAY");
        constants.put("CLICK_VIDEO", "CLICK_VIDEO");
        constants.put("SCROLL_VIDEO", "SCROLL_VIDEO");
        constants.put("NO_FILL", "NO_FILL");
        constants.put("STANDARD_DISPLAY", "STANDARD_DISPLAY");
        final Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("AdTypes", constants);
        return objectMap;
    }

    @ReactMethod
    public void prefetchAdForSection(String sectionUrl, Callback cb) {
        callbacks.add(cb);
        NativoSDK.getInstance().prefetchAdForSection(sectionUrl, this, null);
    }

    @Override
    public boolean shouldPlaceAdAtIndex(String s, int i) {
        return false;
    }

    @Override
    public Class<?> registerLayoutClassForIndex(int i, NtvAdData.NtvAdTemplateType ntvAdTemplateType) {
        return null;
    }

    @Override
    public void needsDisplayLandingPage(String s, int i) {

    }

    @Override
    public void needsDisplayClickOutURL(String s, String s1) {

    }

    @Override
    public void hasbuiltView(View view, NtvBaseInterface ntvBaseInterface, NtvAdData ntvAdData) {

    }

    @Override
    public void onReceiveAd(String s, NtvAdData ntvAdData) {
        Callback prefetchCallback = callbacks.poll();
        if (prefetchCallback != null) {
            prefetchCallback.invoke(defaultError, true, s);
        }
    }

    @Override
    public void onFail(String s) {
        Callback prefetchCallback = callbacks.poll();
        if (prefetchCallback != null) {
            prefetchCallback.invoke(defaultError, false, s);
        }
    }
}