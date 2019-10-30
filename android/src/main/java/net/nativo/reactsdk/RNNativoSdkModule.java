
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
    public void show(String text) {
        Context context = getReactApplicationContext();
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();

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