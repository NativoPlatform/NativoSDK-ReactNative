package net.nativo.reactsdk.ntvadtemplate;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.util.ReactFindViewUtil;

import net.nativo.reactsdk.ntvsdkmanager.RNLandingPageContainerManager;
import net.nativo.sdk.ntvadtype.landing.NtvLandingPageInterface;

import java.util.Date;

public class NativeLandingPage implements NtvLandingPageInterface {

    private static final String ON_PAGE_FINISHED = "onPageFinished";
    private static final String ON_RECEIVED_ERROR = "onReceivedError";
    private WebView webView;
    private TextView titleLabel;
    private TextView authorNameLabel;
    private ImageView authorImage;
    private ImageView previewImage;
    private TextView descriptionLabel;
    private TextView dateLabel;
    private View adContainerView;

    @Override
    public WebView getContentWebView() {
        return webView;
    }

    @Override
    public TextView getTitleLabel() {
        return titleLabel;
    }

    @Override
    public TextView getAuthorNameLabel() {
        return authorNameLabel;
    }

    @Override
    public ImageView getAuthorImageView() {
        return authorImage;
    }

    @Override
    public ImageView getPreviewImageView() {
        return previewImage;
    }

    @Override
    public TextView getPreviewTextLabel() {
        return descriptionLabel;
    }

    @Override
    public TextView getDateLabel() {
        return dateLabel;
    }

    @Override
    public String formatDate(Date date) {
        return null;
    }

    @Override
    public boolean contentWebViewShouldScroll() {
        return true;
    }

    @Override
    public void contentWebViewOnPageFinished() {
        WritableMap event = Arguments.createMap();
        event.putInt("contentHeight", webView.getContentHeight());
        sendEvent(RNLandingPageContainerManager.landingContext,ON_PAGE_FINISHED, event);
    }

    @Override
    public void contentWebViewOnReceivedError(String s) {
        WritableMap event = Arguments.createMap();
        event.putString("error", s);
        sendEvent(RNLandingPageContainerManager.landingContext,ON_PAGE_FINISHED, event);
    }

    @Override
    public int getLayout(Context context) {
        return 0;
    }

    @Override
    public void bindViews(View v) {
        adContainerView = v;
        webView = (WebView) ReactFindViewUtil.findView(v, "nativoAdWebView");
        titleLabel = (TextView) ReactFindViewUtil.findView(v, "articleTitle");
        authorNameLabel = (TextView) ReactFindViewUtil.findView(v, "authorName");
        authorImage = (ImageView) ReactFindViewUtil.findView(v, "authorImage");
        previewImage = (ImageView) ReactFindViewUtil.findView(v, "articleImage");
        descriptionLabel = (TextView) ReactFindViewUtil.findView(v, "articleDescription");
        dateLabel = (TextView) ReactFindViewUtil.findView(v, "articleDate");
    }

    @Override
    public View getAdContainerView() {
        return adContainerView;
    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @javax.annotation.Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @Override
    public void setShareAndTrackingUrl(String s, String s1) {

    }
}
