package net.nativo.reactsdk;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;

import com.facebook.react.uimanager.util.ReactFindViewUtil;

import net.nativo.sdk.ntvadtype.display.NtvStandardDisplayInterface;

public class StandardDisplayAd implements NtvStandardDisplayInterface {

    private WebView webView;

    @Override
    public WebView getContentWebView() {
        return webView;
    }

    @Override
    public void contentWebViewOnPageFinished() {

    }

    @Override
    public void contentWebViewOnReceivedError(String s) {

    }

    @Override
    public int getLayout(Context context) {
        return 0;
    }

    @Override
    public void bindViews(View v) {
        webView = (WebView) ReactFindViewUtil.findView(v, "nativoAdWebView");
    }

    @Override
    public View getAdContainerView() {
        return null;
    }
}
