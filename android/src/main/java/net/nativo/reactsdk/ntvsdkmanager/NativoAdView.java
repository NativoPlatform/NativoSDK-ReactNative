package net.nativo.reactsdk.ntvsdkmanager;

import android.content.Context;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactViewGroup;

import net.nativo.reactsdk.ntvadapter.RNNtvSectionAdapterManager;

import javax.annotation.Nullable;

public class NativoAdView extends ReactViewGroup {

    private int index;
    private String url;

    public NativoAdView(Context context) {
        super(context);
    }

    public NativoAdView(Context context, String sectionUrl, int index) {
        super(context);
        url = sectionUrl;
        this.index = index;
    }

    public void sendEvent(String name, @Nullable WritableMap event) {
        ReactContext reactContext = (ReactContext) getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                getId(),
                name,
                event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        RNNtvSectionAdapterManager.getInstance().removeNtvSectionAdapter(url, index);
    }

}
