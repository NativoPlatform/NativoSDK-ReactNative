
package net.nativo.reactsdk;

import android.view.View;
import android.webkit.WebView;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.util.ReactFindViewUtil;

import net.nativo.sdk.NativoSDK;
import net.nativo.sdk.ntvcore.NtvAdData;

import java.util.HashMap;
import java.util.Map;

public class RNNativoSdkModule extends ReactContextBaseJavaModule {

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
    public void enableDebugLogs() {
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
        constants.put("NATIVE","NATIVE" );
        constants.put("DISPLAY", "DISPLAY");
        constants.put("CLICK_VIDEO", "CLICK_VIDEO");
        constants.put("SCROLL_VIDEO", "SCROLL_VIDEO");
        constants.put("NO_FILL", "NO_FILL");
        constants.put("STANDARD_DISPLAY", "STANDARD_DISPLAY");
        final Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("AdTypes", constants);
        return objectMap;
    }

}