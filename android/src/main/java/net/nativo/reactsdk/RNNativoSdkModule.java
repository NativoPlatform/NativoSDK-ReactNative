
package net.nativo.reactsdk;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.util.ReactFindViewUtil;

import net.nativo.sdk.NativoSDK;

public class RNNativoSdkModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    public static Activity currentactivity;

    public RNNativoSdkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        currentactivity = getCurrentActivity();
    }

    @Override
    public String getName() {
        return "NativoRNSdk";
    }

    @ReactMethod
    public void registerTemplates() {
        NativoSDK.getInstance().registerNativeAd(new NativeAd());
        NativoSDK.getInstance().registerVideoAd(new NativeVideoAd());
        NativoSDK.getInstance().registerLandingPage(new NativeLandingPage());
        NativoSDK.getInstance().registerStandardDisplayAd(new StandardDisplayAd());
    }

    @ReactMethod
    public void enableDebugLogs() {
        NativoSDK.getInstance().enableDevLogs();
    }

    @ReactMethod
    public void enableTestAds(String s) {
        if (s.equals("native")) {
            NativoSDK.getInstance().enableTestAdvertisements(NtvAdData.NtvAdType.NATIVE);
        } else if (s.equals("click_out")) {
            NativoSDK.getInstance().enableTestAdvertisements(NtvAdData.NtvAdType.CLICK_OUT);
        } else if (s.equals("in_feed_video")) {
            NativoSDK.getInstance().enableTestAdvertisements(NtvAdData.NtvAdType.IN_FEED_VIDEO);
        } else if (s.equals("in_feed_auto_play_video")) {
            NativoSDK.getInstance().enableTestAdvertisements(NtvAdData.NtvAdType.IN_FEED_AUTO_PLAY_VIDEO);
        } else if (s.equals("standard_display")) {
            NativoSDK.getInstance().enableTestAdvertisements(NtvAdData.NtvAdType.STANDARD_DISPLAY);
        } else if (s.equals("no_fill")) {
            NativoSDK.getInstance().enableTestAdvertisements(NtvAdData.NtvAdType.NO_FILL);
        } else {
            NativoSDK.getInstance().enableTestAdvertisements();
        }
    }

    @ReactMethod
    public void placeAdInWebView(final String sectionUrl) {
        Log.d("RNNativoSdkModule", "placeAdInWebView:  for " + sectionUrl);
        View view = getCurrentActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        final WebView webView = (WebView) ReactFindViewUtil.findView(view, "nativoMoapAdView");
        webView.post(new Runnable() {
            @Override
            public void run() {
                NativoSDK.getInstance().placeAdInWebView(webView, sectionUrl);
            }
        });
    }

}