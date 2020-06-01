package net.nativo.reactsdk.ntvutil;

import android.app.Activity;
import android.view.View;

import com.facebook.react.uimanager.util.ReactFindViewUtil;

public class ViewFinder {

    private static ViewFinder instance;

    private ViewFinder() {
    }

    public static ViewFinder getInstance() {
        if (instance == null) {
            instance = new ViewFinder();
        }
        return instance;
    }

    public View findPublisherAdContainer(Activity currentactivity) {
        if (currentactivity == null) {
            return null;
        }
        View nativeContainerParent = null;
        View view = currentactivity.getWindow().getDecorView().findViewById(android.R.id.content);
        nativeContainerParent = ReactFindViewUtil.findView(view, "publisherNativoAdContainer");
        return nativeContainerParent;
    }
}
