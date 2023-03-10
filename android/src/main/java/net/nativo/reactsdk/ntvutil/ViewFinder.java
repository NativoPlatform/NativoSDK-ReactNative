package net.nativo.reactsdk.ntvutil;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.facebook.react.R;
import com.facebook.react.uimanager.util.ReactFindViewUtil;

public class ViewFinder {

    private static final String PUB_CONTAINER_TAG = "publisherNativoAdContainer";
    private static ViewFinder instance;

    public interface OnViewFoundListener {
        void onViewFound(View view);
    }

    private ViewFinder() {
    }

    public static ViewFinder getInstance() {
        if (instance == null) {
            instance = new ViewFinder();
        }
        return instance;
    }

    private View findPublisherAdContainerInUpperHierarchy(View root) {
        if (root != null) {
            Object tag = root.getTag(R.id.view_tag_native_id);
            if (tag != null && tag.equals(PUB_CONTAINER_TAG)) {
                return root;
            }
            ViewParent parent = root.getParent();
            if (parent != null) {
                return findPublisherAdContainerInUpperHierarchy((View) parent);
            }
        }
        return null;
    }

    public void findPublisherAdContainer(View root, Activity currentactivity, final OnViewFoundListener listener) {
        View pubContainer = findPublisherAdContainerInUpperHierarchy(root);
        if (pubContainer != null) {
            listener.onViewFound(pubContainer);
            return;
        }
        if (currentactivity == null) {
            Log.d("NativoRNSDK", "Failed findPublisherAdContainer. CurrentActivity is null");
            return;
        }
        root = currentactivity.getWindow().getDecorView().findViewById(android.R.id.content);
        ReactFindViewUtil.findView(root, new ReactFindViewUtil.OnViewFoundListener() {
            @Override
            public String getNativeId() {
                return PUB_CONTAINER_TAG;
            }

            @Override
            public void onViewFound(View view) {
                try {
                    listener.onViewFound(view);
                    ReactFindViewUtil.removeViewListener(this);
                } catch (Exception ex) {
                    Log.d("NativoRNSDK", "Caught ex pub container ViewFound: "+ ex);
                }
            }
        });
    }
}