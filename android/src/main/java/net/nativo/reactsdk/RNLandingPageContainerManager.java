package net.nativo.reactsdk;

import android.app.Activity;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;
import android.widget.RelativeLayout;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.util.ReactFindViewUtil;

import net.nativo.sdk.NativoSDK;

import java.util.WeakHashMap;

import javax.annotation.Nonnull;

public class RNLandingPageContainerManager extends ViewGroupManager<RelativeLayout> {

    public static Activity currentactivity;
    private ThemedReactContext themedReactContext;
    private WeakHashMap<Integer, View> viewWeakHashMap = new WeakHashMap<>();

    @Nonnull
    @Override
    public String getName() {
        return "NativoLandingPageContainer";
    }

    @Nonnull
    @Override
    protected RelativeLayout createViewInstance(@Nonnull ThemedReactContext reactContext) {
        currentactivity = reactContext.getCurrentActivity();
        themedReactContext = reactContext;
        return new RelativeLayout(reactContext);
    }

    @ReactProp(name = "injectLandingPage")
    public void setInjectLandingPage(View view, ReadableMap map) {
        String sectionUrl = map.getString("url");
        int containerHash = map.getInt("containerHash");
        int adId = map.getInt("adId");
        Log.d(RNLandingPageContainerManager.class.getName(), " with containerHash " + containerHash);
        viewWeakHashMap.put(containerHash, view);
        makeAdRequest(containerHash, adId, sectionUrl);
    }

    private void makeAdRequest(final int containerhash, final int adId, final String sectionUrl) {
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                View webViewContainer = ReactFindViewUtil.findView((View)viewWeakHashMap.get(containerhash), "nativoAdWebViewContainer");
                Log.d(RNLandingPageContainerManager.class.getName(), "makeAdRequest: req " + containerhash);
                NativoSDK.getInstance().initLandingPage(webViewContainer, sectionUrl, containerhash, adId, NativeLandingPage.class);

            }
        });
    }
}
