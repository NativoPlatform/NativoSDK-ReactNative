package net.nativo.reactsdk;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Choreographer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import net.nativo.sdk.NativoSDK;
import net.nativo.sdk.ntvadtype.NtvBaseInterface;
import net.nativo.sdk.ntvconstant.NtvAdTypeConstants;
import net.nativo.sdk.ntvcore.NtvAdData;
import net.nativo.sdk.ntvcore.NtvSectionAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.WeakHashMap;

import javax.annotation.Nonnull;

public class RNAdManager extends ViewGroupManager implements NtvSectionAdapter {

    public String SECTION_URL;
    private View originalView;
    private View newView;
    private ViewGroup container;
    private ThemedReactContext themedReactContext;
    private RNAdManager instance;
    private Integer index;
    private WeakHashMap<Integer, View> viewWeakHashMap = new WeakHashMap<>();
    private List<View> listRemoveView = new ArrayList<>();
    private Queue<Integer> indexList = new LinkedList<>();

    public RNAdManager() {
        this.instance = this;
    }

    @ReactProp(name = "adIndex")
    public void setAdRequestIndex(View AdView, Integer index) {
        Log.d(RNAdManager.class.getName(), "setAdRequestIndex: " + index);
        this.index = index;
        viewWeakHashMap.put(index, AdView);
        indexList.add(index);
        makeAdRequest();
    }


    @ReactProp(name = "sectionUrl")
    public void setSectionUrl(View container, String sectionUrl) {
        Log.d(RNAdManager.class.getName(), "sectionURL: " + sectionUrl);
        SECTION_URL = sectionUrl;
    }

    @Nonnull
    @Override
    public String getName() {
        return "NativoAd";
    }

    @Nonnull
    @Override
    protected View createViewInstance(@Nonnull ThemedReactContext reactContext) {
        themedReactContext = reactContext;
        originalView = LayoutInflater.from(reactContext).inflate(R.layout.native_article, null, false);
        originalView.setVisibility(View.GONE);
        return originalView;
    }


    private void makeAdRequest() {
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                index = indexList.remove();
                Log.d(RNAdManager.class.getName(), "onReceiveAd: req " + index);
                container = (ViewGroup) viewWeakHashMap.get(index).getParent();
                NativoSDK.getInstance().prefetchAdForSection(SECTION_URL, container, index, instance, null);
            }
        });
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
        themedReactContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(s1)));
    }

    @Override
    public void hasbuiltView(View view, NtvBaseInterface ntvBaseInterface, NtvAdData ntvAdData) {

    }

    @Override
    public void onReceiveAd(String s, int i, NtvAdData ntvAdData) {
        Log.d(RNAdManager.class.getName(), "onReceiveAd: " + i);
        if (viewWeakHashMap.get(i) == null) {
            return;
        }
        s = NativoSDK.getInstance().getAdTypeForIndex(SECTION_URL, container, i);
        switch (s) {
            case NtvAdTypeConstants.AD_TYPE_STANDARD_DISPLAY:
            case NtvAdTypeConstants.AD_TYPE_VIDEO:
                newView = LayoutInflater.from(themedReactContext).inflate(R.layout.video_layout, container, true);
                break;
            case NtvAdTypeConstants.AD_TYPE_NATIVE:
            case NtvAdTypeConstants.AD_TYPE_NONE:
            default:
                newView = LayoutInflater.from(themedReactContext).inflate(R.layout.native_article, container, true);
        }

        if (viewWeakHashMap.get(i) != null && container != null) {
            container.removeView(viewWeakHashMap.get(i));
            viewWeakHashMap.remove(i);
        }

        if (!listRemoveView.isEmpty()) {
            for (View view : listRemoveView) {
                if (container != null) {
                    container.removeView(view);
                }
            }
            listRemoveView.clear();
        }

        Log.d(RNAdManager.class.getName(), "onReceiveAd: place ad called" + i);

        if (container != null) {
            NativoSDK.getInstance().placeAdInView(newView, container, SECTION_URL, i, this, null);
        }

        listRemoveView.add(newView);

        setupLayoutHack();
    }

    void setupLayoutHack() {

        final Choreographer.FrameCallback frameCallback = new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                manuallyLayoutChildren();
                newView.getViewTreeObserver().dispatchOnGlobalLayout();
            }
        };
        Choreographer.getInstance().postFrameCallback(frameCallback);
    }

    void manuallyLayoutChildren() {
        for (int i = 0; i < getChildCount((ViewGroup) newView); i++) {
            View child = getChildAt((ViewGroup) newView, i);
            child.measure(View.MeasureSpec.makeMeasureSpec(newView.getMeasuredWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(newView.getMeasuredHeight(), View.MeasureSpec.EXACTLY));
            child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
        }
    }

    @Override
    public void onFail(String s, int i) {
        Log.d(RNAdManager.class.getName(), "onFail: ");
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return true;
    }

}
