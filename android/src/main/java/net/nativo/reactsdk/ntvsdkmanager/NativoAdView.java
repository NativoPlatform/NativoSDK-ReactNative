package net.nativo.reactsdk.ntvsdkmanager;

import android.content.Context;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactViewGroup;

import javax.annotation.Nullable;

public class NativoAdView extends ReactViewGroup {
    public NativoAdView(Context context) {
        super(context);
    }

    public void sendEvent(String name, @Nullable WritableMap event) {
        ReactContext reactContext = (ReactContext) getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                getId(),
                name,
                event);
    }
}
