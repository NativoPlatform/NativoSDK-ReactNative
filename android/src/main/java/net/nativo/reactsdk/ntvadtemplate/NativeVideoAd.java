/**
 * Copyright 2024 Nativo
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.nativo.reactsdk.ntvadtemplate;

import android.content.Context;
import android.content.res.Resources;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.touch.OnInterceptTouchEventListener;
import com.facebook.react.uimanager.util.ReactFindViewUtil;
import com.facebook.react.views.view.ReactViewGroup;

import net.nativo.reactsdk.R;
import net.nativo.reactsdk.ntvsdkmanager.NativoAdView;
import net.nativo.reactsdk.ntvsdkmanager.RNAdContainerManager;
import net.nativo.sdk.ntvadtype.video.NtvVideoAdInterface;
import net.nativo.sdk.ntvadtype.video.VideoPlaybackError;
import net.nativo.sdk.ntvlog.Logger;
import net.nativo.sdk.ntvlog.LoggerFactory;

import java.util.Date;

import static net.nativo.reactsdk.ntvutil.RNConstants.EVENT_AD_FAILED_TO_LOAD;


public class NativeVideoAd implements NtvVideoAdInterface {

    private static String TAG = NativeVideoAd.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger(TAG);
    private static final String DRAWABLE = "drawable";
    private View layout;
    private TextureView textureView;
    private ImageView previewImage;
    private ImageView playButton;
    private ImageView restartButton;
    private ImageView muteIndicator;
    private TextView titleLabel;
    private TextView dateLabel;
    private TextView authorLabel;
    private ImageView sponsoredIndicator;
    private ProgressBar progressBar;
    private View adContainerView;
    private ImageView articleAuthorImage;
    private ViewGroup videoControlsGroup;
    private ImageView adChoicesIndicator;


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

        layout = (View) ReactFindViewUtil.findView(v, "nativoAdView");
        textureView = (TextureView) ReactFindViewUtil.findView(v, "videoView");
        previewImage = (ImageView) ReactFindViewUtil.findView(v, "articleImage");

        playButton = (ImageView) ReactFindViewUtil.findView(v, "videoPlay");
        playButton.setImageResource(getResourceId("ic_media_play", DRAWABLE));
        restartButton = (ImageView) ReactFindViewUtil.findView(v, "videoRestart");
        restartButton.setImageResource(getResourceId("restart", DRAWABLE));
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adContainerView.callOnClick();
            }
        });
        titleLabel = (TextView) ReactFindViewUtil.findView(v, "adTitle");
        authorLabel = (TextView) ReactFindViewUtil.findView(v, "adAuthorName");
        ViewGroup videoProgress = (ViewGroup) ReactFindViewUtil.findView(v, "videoProgress");
        progressBar = (ProgressBar) videoProgress.getChildAt(0);
        muteIndicator = (ImageView) ReactFindViewUtil.findView(v, "videoMuteIndicator");
        articleAuthorImage = (ImageView) ReactFindViewUtil.findView(v, "adAuthorImage");
        dateLabel = (TextView) ReactFindViewUtil.findView(v, "adDate");
        adChoicesIndicator = (ImageView) ReactFindViewUtil.findView(v, "adChoicesImage");
        rootViewClick((ReactViewGroup) layout);
    }

    private void rootViewClick(ReactViewGroup viewGroup) {
        viewGroup.setOnInterceptTouchEventListener(new OnInterceptTouchEventListener() {
            @Override
            public boolean onInterceptTouchEvent(ViewGroup v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        adContainerView.performClick();
                        break;
                }
                return false;
            }
        });
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
        return dateLabel;
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
        return adChoicesIndicator;
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
        LOG.debug("onVideoEnteredFullscreen: ");
    }

    @Override
    public void onVideoExitedFullscreen() {
        LOG.debug("onVideoExitedFullscreen: ");
    }

    @Override
    public void onVideoPlay() {
        LOG.debug("onVideoPlay: ");
    }

    @Override
    public void onVideoPause() {
        LOG.debug("onVideoPause: ");
    }

    @Override
    public void onVideoPlaybackCompleted() {
        LOG.debug("onVideoPlaybackCompleted: ");
    }

    @Override
    public void onVideoPlaybackError(VideoPlaybackError videoPlaybackError) {
        LOG.debug("onVideoPlaybackError: ");
        removeAdOnPlaybackError();
    }

    private void removeAdOnPlaybackError() {
        NativoAdView adView = (NativoAdView) adContainerView.getParent();
        if (adView != null) {
            WritableMap event = Arguments.createMap();
            adView.sendEvent(EVENT_AD_FAILED_TO_LOAD, event);
        }
    }

    private int getResourceId(String variableName, String resourceName) {
        Context root = RNAdContainerManager.currentactivity;
        Resources resources = root.getResources();
        final int resourceId = resources.getIdentifier(variableName, resourceName,
                root.getPackageName());
        LOG.debug("getResourceId: " + resourceId);
        return resourceId;
    }

}
