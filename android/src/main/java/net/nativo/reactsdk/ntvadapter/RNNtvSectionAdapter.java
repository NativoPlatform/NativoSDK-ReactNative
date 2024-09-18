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

package net.nativo.reactsdk.ntvadapter;

import android.app.Activity;
import android.view.View;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import net.nativo.reactsdk.ntvsdkmanager.NativoAdView;
import net.nativo.reactsdk.ntvsdkmanager.RNAdContainerManager;
import net.nativo.reactsdk.ntvutil.ViewFinder;
import net.nativo.sdk.ntvadtype.NtvBaseInterface;
import net.nativo.sdk.ntvcore.NtvAdData;
import net.nativo.sdk.ntvcore.NtvSectionAdapter;
import net.nativo.sdk.ntvlog.Logger;
import net.nativo.sdk.ntvlog.LoggerFactory;
import net.nativo.sdk.ntvutils.AppUtils;

import javax.annotation.Nullable;

import static net.nativo.reactsdk.ntvutil.RNConstants.EVENT_AD_DISPLAY_LANDING_PAGE;
import static net.nativo.reactsdk.ntvutil.RNConstants.EVENT_AD_FAILED_TO_LOAD;
import static net.nativo.reactsdk.ntvutil.RNConstants.EVENT_AD_LOADED;
import static net.nativo.sdk.ntvconstant.NtvConstants.NTV_BY;

public class RNNtvSectionAdapter implements NtvSectionAdapter {

    private static final String TAG = RNNtvSectionAdapter.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger(TAG);

    private View nativoAdView;
    private int adID;
    private int containerHashCode;

    @Override
    public Class<?> registerLayoutClassForIndex(int i, NtvAdData.NtvAdTemplateType ntvAdTemplateType) {
        return null;
    }

    @Override
    public void needsDisplayLandingPage(String s, int i) {
        WritableMap params = Arguments.createMap();
        params.putString("sectionUrl", s);
        params.putInt("adId", i);
        params.putInt("containerHash", containerHashCode);
        sendEvent(EVENT_AD_DISPLAY_LANDING_PAGE, (NativoAdView) nativoAdView, params);
    }

    @Override
    public void needsDisplayClickOutURL(String s, String s1) {
        WritableMap params = Arguments.createMap();
        params.putString("url", s1);
        sendEvent(RNAdContainerManager.containerReactContext, "needsDisplayClickOutURL", params);
    }

    @Override
    public void hasbuiltView(View view, NtvBaseInterface ntvBaseInterface, NtvAdData ntvAdData) {
        ViewFinder.getInstance().findPublisherAdContainer(view, (Activity) AppUtils.getInstance().getContext(), new ViewFinder.OnViewFoundListener() {
            @Override
            public void onViewFound(View view) {
                containerHashCode = view.hashCode();
            }
        });
    }

    @Override
    public void onReceiveAd(String s, NtvAdData ntvAdData, Integer integer) {
        if (nativoAdView == null) {
            LOG.error("onReceiveAd: view is null");
            return;
        }

        // callback to js
        WritableMap event = Arguments.createMap();
        event.putString("adType", ntvAdData.getAdType().toString());
        event.putString("adDescription", ntvAdData.getPreviewText());
        event.putString("adTitle", ntvAdData.getTitle());
        event.putString("adAuthorName", String.format("%s %s", NTV_BY, ntvAdData.getAuthorName()));
        event.putString("adDate", ntvAdData.getDate().toString());
        event.putString("adAuthorUrl", ntvAdData.getAuthorImageURL());
        event.putString("adImgUrl", ntvAdData.getPreviewImageURL());
        event.putInt("adAdID", ntvAdData.getAdID());
        event.putString("adUUID", ntvAdData.getUuid());
        String shareUrl = ntvAdData.getShareUrl();
        event.putString("adShareUrl", shareUrl == null ? "" : shareUrl);

        if (ntvAdData.getAdType() == NtvAdData.NtvAdType.STANDARD_DISPLAY) {
            event.putInt("adDisplayWidth", ntvAdData.getStandardDisplayWidth());
            event.putInt("adDisplayHeight", ntvAdData.getStandardDisplayHeight());
        }

        sendEvent(EVENT_AD_LOADED, (NativoAdView) nativoAdView, event);
    }

    @Override
    public void onFail(String s, Integer integer) {
        if (nativoAdView == null) {
            return;
        }
        WritableMap event = Arguments.createMap();
        sendEvent(EVENT_AD_FAILED_TO_LOAD, (NativoAdView) nativoAdView, event);
    }

    private void sendEvent(String name, NativoAdView adView, @Nullable WritableMap event) {
        adView.sendEvent(name, event);
    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    public View getNativoAdView() {
        return nativoAdView;
    }

    public void setNativoAdView(View nativoAdView) {
        this.nativoAdView = nativoAdView;
    }

    public int getAdID() {
        return adID;
    }

    public void setAdID(int adID) {
        this.adID = adID;
    }

    public int getContainerHashCode() {
        return containerHashCode;
    }

    public void setContainerHashCode(int containerHashCode) {
        this.containerHashCode = containerHashCode;
    }
}
