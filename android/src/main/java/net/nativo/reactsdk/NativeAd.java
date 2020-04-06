package net.nativo.reactsdk;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.facebook.react.uimanager.util.ReactFindViewUtil;

import net.nativo.sdk.ntvadtype.nativead.NtvNativeAdInterface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NativeAd implements NtvNativeAdInterface {

    private LinearLayout layout;
    private CardView cardView;
    private TextView titleLabel;
    private TextView authorLabel;
    private TextView articlePreviewLabel;
    private TextView articleDateLabel;
    private ImageView articleAuthorImage;
    private ImageView image;
    private ImageView sponsoredIndicator;
    private TextView sponsoredTag;
    private View view;
    private View adContainerView;
    private ImageView adChoicesIndicator;

    @Override
    public TextView getTitleLabel() {
        if (titleLabel == null) {
            titleLabel = (TextView) view.findViewById(R.id.article_title);
            return titleLabel;
        }
        return titleLabel;
    }

    @Override
    public View getAdContainerView() {
        return adContainerView;
    }

    @Override
    public TextView getAuthorLabel() {
        if (!authorLabel.getText().toString().contains("By")) {
            authorLabel.append("By ", 0, 3);
        }
        return authorLabel;
    }

    @Override
    public TextView getPreviewTextLabel() {
        return articlePreviewLabel;
    }

    @Override
    public ImageView getPreviewImageView() {
        return image;
    }

    @Override
    public ImageView getAuthorImageView() {
        return articleAuthorImage;
    }

    @Override
    public TextView getDateLabel() {
        return articleDateLabel;
    }

    @Override
    public void displaySponsoredIndicators(boolean b) {
        if (cardView != null) {
            cardView.setBackgroundColor(Color.LTGRAY);
        }
        if (sponsoredIndicator != null) {
            sponsoredIndicator.setVisibility(View.VISIBLE);
        }
        if (sponsoredTag != null) {
            sponsoredTag.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public String formatDate(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(date);
    }

    @Override
    public int getLayout(Context context) {
        return R.layout.native_article;
    }

    @Override
    public void bindViews(View v) {
        view = v;
        adContainerView = ReactFindViewUtil.findView(v, "nativoAdView");
        titleLabel = (TextView) ReactFindViewUtil.findView(v, "adTitle");
        authorLabel = (TextView) ReactFindViewUtil.findView(v, "adAuthorName");
        image = (ImageView) ReactFindViewUtil.findView(v, "adImage");
        articleDateLabel = (TextView) ReactFindViewUtil.findView(v, "adDate");
        articlePreviewLabel = (TextView) ReactFindViewUtil.findView(v, "adDescription");
        articleAuthorImage = (ImageView) ReactFindViewUtil.findView(v, "adAuthorImage");
        adChoicesIndicator = (ImageView) ReactFindViewUtil.findView(v, "adChoicesImage");
    }

    @Override
    public ImageView getAdChoicesImageView() {
        return adChoicesIndicator;
    }
}