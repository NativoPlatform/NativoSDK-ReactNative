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
