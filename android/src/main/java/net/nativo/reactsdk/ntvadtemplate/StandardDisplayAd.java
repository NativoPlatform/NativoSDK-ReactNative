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
import android.view.View;
import android.webkit.WebView;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.util.ReactFindViewUtil;

import net.nativo.reactsdk.ntvsdkmanager.RNAdContainerManager;
import net.nativo.sdk.ntvadtype.display.NtvStandardDisplayInterface;

public class StandardDisplayAd implements NtvStandardDisplayInterface {

    private static final String ON_PAGE_FINISHED = "onStandardDisplayPageFinished";
    private static final String ON_RECEIVED_ERROR = "onStandardDisplayReceivedError";
    private WebView webView;

    @Override
    public WebView getContentWebView() {
        return webView;
    }

    @Override
    public void contentWebViewOnPageFinished() {
        WritableMap event = Arguments.createMap();
        event.putInt("standardDisplayHeight", webView.getContentHeight());
        sendEvent(RNAdContainerManager.containerReactContext, ON_PAGE_FINISHED, event);
    }

    @Override
    public void contentWebViewOnReceivedError(String s) {
        WritableMap event = Arguments.createMap();
        event.putString("standardDisplayError", s);
        sendEvent(RNAdContainerManager.containerReactContext, ON_RECEIVED_ERROR, event);
    }

    @Override
    public int getLayout(Context context) {
        return 0;
    }

    @Override
    public void bindViews(View v) {
        webView = (WebView) ReactFindViewUtil.findView(v, "nativoAdWebView");
    }

    @Override
    public View getAdContainerView() {
        return null;
    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @javax.annotation.Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

}
