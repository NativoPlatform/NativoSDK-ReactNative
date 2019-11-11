package net.nativo.reactsdk;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.react.uimanager.util.ReactFindViewUtil;

import net.nativo.sdk.ntvadtype.NtvBaseInterface;
import net.nativo.sdk.ntvadtype.landing.NtvLandingPageInterface;
import net.nativo.sdk.ntvcore.NtvAdData;
import net.nativo.sdk.ntvcore.NtvSectionAdapter;

import java.util.Date;

public class NativeLandingPage implements NtvLandingPageInterface, NtvSectionAdapter {

    private WebView webView;
    private TextView titleLabel;
    private TextView authorNameLabel;
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
        return null;
    }

    @Override
    public ImageView getPreviewImageView() {
        return null;
    }

    @Override
    public TextView getPreviewTextLabel() {
        return null;
    }

    @Override
    public TextView getDateLabel() {
        return null;
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
        adContainerView = v;
        webView = (WebView) ReactFindViewUtil.findView(v, "nativoAdWebView");
    }

    @Override
    public View getAdContainerView() {
        return adContainerView;
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

    }

    @Override
    public void needsDisplayClickOutURL(String s, String s1) {

    }

    @Override
    public void hasbuiltView(View view, NtvBaseInterface ntvBaseInterface, NtvAdData ntvAdData) {

    }

    @Override
    public void onReceiveAd(String s, int i, NtvAdData ntvAdData) {

    }

    @Override
    public void onFail(String s, int i) {

    }
}
