
package net.nativo.reactsdk;

import android.app.Activity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.util.ReactFindViewUtil;

import net.nativo.reactsdk.ntvadtemplate.NativeAd;
import net.nativo.reactsdk.ntvadtemplate.NativeLandingPage;
import net.nativo.reactsdk.ntvadtemplate.NativeVideoAd;
import net.nativo.reactsdk.ntvadtemplate.StandardDisplayAd;
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

public class RNNativoSdkModule extends ReactContextBaseJavaModule implements NtvSectionAdapter, LifecycleEventListener {

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
            NativoSDK.registerNativeAd(new NativeAd());
            NativoSDK.registerVideoAd(new NativeVideoAd());
            NativoSDK.registerLandingPage(new NativeLandingPage());
            NativoSDK.registerStandardDisplayAd(new StandardDisplayAd());
            isTemplateRegistred = true;
        }
    }

    @ReactMethod
    public void trackShareActionForAd(String adUUID){
        NativoSDK.trackShareAction(adUUID);
    }

    @ReactMethod
    public void enableGAMRequestsWithVersion(String gamVersion) {
        NativoSDK.initWithGAMVersion(getReactApplicationContext(), gamVersion);
    }

    @ReactMethod
    public void enableDevLogs() {
        NativoSDK.enableDevLogs();
    }

    @ReactMethod
    public void enableTestAdvertisementsWithType(String s) {
        if (s.equals("NATIVE")) {
            NativoSDK.enableTestAdvertisements(NtvAdData.NtvAdType.NATIVE);
        } else if (s.equals("DISPLAY")) {
            NativoSDK.enableTestAdvertisements(NtvAdData.NtvAdType.CLICK_OUT);
        } else if (s.equals("CLICK_VIDEO")) {
            NativoSDK.enableTestAdvertisements(NtvAdData.NtvAdType.IN_FEED_VIDEO);
        } else if (s.equals("SCROLL_VIDEO")) {
            NativoSDK.enableTestAdvertisements(NtvAdData.NtvAdType.IN_FEED_AUTO_PLAY_VIDEO);
        } else if (s.equals("STANDARD_DISPLAY")) {
            NativoSDK.enableTestAdvertisements(NtvAdData.NtvAdType.STANDARD_DISPLAY);
        } else if (s.equals("STORY")) {
            NativoSDK.enableTestAdvertisements(NtvAdData.NtvAdType.STORY);
        } else if (s.equals("NO_FILL")) {
            NativoSDK.enableTestAdvertisements(NtvAdData.NtvAdType.NO_FILL);
        } else {
            NativoSDK.enableTestAdvertisements();
        }
    }

    @ReactMethod
    public void enableTestAdvertisements(){
        NativoSDK.enableTestAdvertisements();
    }

    @ReactMethod
    public void placeAdInWebView(final String sectionUrl) {
        LOG.debug("placeAdInWebView called for section " + sectionUrl);
        View view = getCurrentActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        final WebView webView = (WebView) ReactFindViewUtil.findView(view, "nativoMoapAdView");
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());
                webView.setWebChromeClient(new NativoChromeClient(getCurrentActivity()));
                NativoSDK.placeAdInWebView(webView, sectionUrl);
            }
        });
    }

    @ReactMethod
    public void clearAdsInSection(String sectionUrl) {
        // noop
        //todo will implement in next native SDK update. Currently there is an API incompatibility
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
        constants.put("STORY", "STORY");
        final Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("AdTypes", constants);
        return objectMap;
    }

    @ReactMethod
    public void prefetchAdForSection(String sectionUrl, Callback cb) {
        callbacks.add(cb);
        NativoSDK.prefetchAdForSection(sectionUrl, this, null);
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
    public void onReceiveAd(String section, NtvAdData ntvAdData, Integer index) {
        Callback prefetchCallback = callbacks.poll();
        if (prefetchCallback != null) {
            prefetchCallback.invoke(defaultError, true, section);
        }
    }

    @Override
    public void onFail(String section, Integer index) {
        Callback prefetchCallback = callbacks.poll();
        if (prefetchCallback != null) {
            prefetchCallback.invoke(defaultError, false, section);
        }
    }

    private class NativoChromeClient extends WebChromeClient {

        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;
        private Activity activity;

        NativoChromeClient(Activity activity) {
            this.activity = activity;
        }

        public void onHideCustomView() {
            ((FrameLayout) activity.getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            activity.getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            activity.setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = activity.getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout) activity.getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE |
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @Override
    public void onHostResume() {
        LOG.debug("onHostResume() called");
        NativoSDK.onResume();
    }

    @Override
    public void onHostPause() {
        LOG.debug("onHostPause() called");
        NativoSDK.onPause();
    }

    @Override
    public void onHostDestroy() {

    }
}