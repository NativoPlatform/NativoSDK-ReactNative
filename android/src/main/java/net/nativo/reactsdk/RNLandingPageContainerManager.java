package net.nativo.reactsdk;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.util.ReactFindViewUtil;

import net.nativo.sdk.NativoSDK;

import java.util.Map;

import javax.annotation.Nonnull;

public class RNLandingPageContainerManager extends ViewGroupManager<RelativeLayout> {

    public static Activity currentactivity;
    public static ReactContext landingContext;
    private ThemedReactContext themedReactContext;
    public static final int COMMAND_INJECT_AD = 3;
    String sectionUrl;
    int containerHash;
    int adId;

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
        landingContext = reactContext;
        return new RelativeLayout(reactContext);
    }

    @ReactProp(name = "injectLandingPage")
    public void setInjectLandingPage(View view, ReadableMap map) {
        sectionUrl = map.getString("url");
        containerHash = map.getInt("containerHash");
        adId = map.getInt("adId");
        Log.d(RNLandingPageContainerManager.class.getName(), " with containerHash " + containerHash);
    }

    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of(
                "injectAd",
                COMMAND_INJECT_AD
        );
    }

    @Override
    public void receiveCommand(final RelativeLayout root, int commandId, @Nullable ReadableArray args) {
        switch (commandId) {
            case COMMAND_INJECT_AD:
                View webViewContainer = ReactFindViewUtil.findView(root, "nativoAdWebViewContainer");
                int adid = args.getInt(0);
                String sectionUrl = args.getString(1);
                int containerHash = args.getInt(2);
                NativoSDK.getInstance().initLandingPage(webViewContainer, sectionUrl, containerHash, adid, NativeLandingPage.class);
                break;
        }
    }

}
