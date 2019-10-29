package net.nativo.reactsdk;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.react.uimanager.util.ReactFindViewUtil;

import net.nativo.sdk.ntvadtype.video.NtvVideoAdInterface;
import net.nativo.sdk.ntvadtype.video.VideoPlaybackError;

import java.util.Date;

public class NativeVideoAd implements NtvVideoAdInterface {

    private static String TAG = NativeVideoAd.class.getName();
    private static final String DRAWABLE = "drawable";
    private View layout;
    private TextureView textureView;
    private ImageView previewImage;
    private ImageView playButton;
    private ImageView restartButton;
    private ImageView muteIndicator;
    private TextView titleLabel;
    private TextView authorLabel;
    private ImageView sponsoredIndicator;
    private ProgressBar progressBar;
    private View adContainerView;
    private ImageView articleAuthorImage;


    @Override
    public int getLayout(Context context) {
        return R.layout.video_layout;
    }

    @Override
    public View getAdContainerView() {
        return adContainerView;
    }

    @Override
    public void bindViews(View v) {
        adContainerView = v;
        layout = (View) ReactFindViewUtil.findView(v, "nativoVideoAdView");
        textureView = (TextureView) ReactFindViewUtil.findView(v, "videoView");
        previewImage = (ImageView) ReactFindViewUtil.findView(v, "articleImage");
        playButton = (ImageView) ReactFindViewUtil.findView(v, "videoPlay");
        playButton.setImageResource(getResourceId("ic_media_play", DRAWABLE));
        restartButton = (ImageView) ReactFindViewUtil.findView(v, "videoRestart");
        restartButton.setImageResource(getResourceId("restart", DRAWABLE));
        titleLabel = (TextView) ReactFindViewUtil.findView(v, "articleTitle");
        authorLabel = (TextView) ReactFindViewUtil.findView(v, "authorName");
        ViewGroup videoProgress = (ViewGroup) ReactFindViewUtil.findView(v, "videoProgress");
        progressBar = (ProgressBar) videoProgress.getChildAt(0);
        muteIndicator = (ImageView) ReactFindViewUtil.findView(v, "videoMuteIndicator");
        articleAuthorImage = (ImageView) ReactFindViewUtil.findView(v, "authorImage");
    }

    @Override
    public View getRootView() {
        return layout;
    }

    @Override
    public TextureView getTextureView() {
        return textureView;
    }

    @Override
    public ImageView getPreviewImage() {
        return previewImage;
    }

    @Override
    public ImageView getPlayButton() {
        return playButton;
    }

    @Override
    public ImageView getRestartButton() {
        return restartButton;
    }

    @Override
    public TextView getTitleLabel() {
        return titleLabel;
    }

    @Override
    public TextView getAuthorLabel() {
        return authorLabel;
    }

    @Override
    public TextView getPreviewTextLabel() {
        return null;
    }

    @Override
    public ImageView getAuthorImageView() {
        return articleAuthorImage;
    }

    @Override
    public TextView getDateLabel() {
        return null;
    }

    @Override
    public void displaySponsoredIndicators(boolean isSponsored) {
    }

    @Override
    public ImageView getMuteIndicator() {
        return muteIndicator;
    }

    @Override
    public ImageView getAdChoicesImageView() {
        return null;
    }

    @Override
    public String formatDate(Date date) {
        return null;
    }

    @Override
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    public void onVideoEnteredFullscreen() {
        Log.d(TAG, "onVideoEnteredFullscreen: ");
    }

    @Override
    public void onVideoExitedFullscreen() {
        Log.d(TAG, "onVideoExitedFullscreen: ");
    }

    @Override
    public void onVideoPlay() {
        Log.d(TAG, "onVideoPlay: ");
    }

    @Override
    public void onVideoPause() {
        Log.d(TAG, "onVideoPause: ");
    }

    @Override
    public void onVideoPlaybackCompleted() {
        Log.d(TAG, "onVideoPlaybackCompleted: ");
    }

    @Override
    public void onVideoPlaybackError(VideoPlaybackError videoPlaybackError) {
        Log.d(TAG, "onVideoPlaybackError: ");
    }

    private int getResourceId(String variableName, String resourceName) {
        Context root = RNAdContainerManager.currentactivity;
        Resources resources = root.getResources();
        final int resourceId = resources.getIdentifier(variableName, resourceName,
                root.getPackageName());
        Log.d(TAG, "getResourceId: " + resourceId);
        return resourceId;
    }

}
