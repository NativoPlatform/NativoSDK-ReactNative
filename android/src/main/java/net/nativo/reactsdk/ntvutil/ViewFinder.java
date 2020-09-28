package net.nativo.reactsdk.ntvutil;

import android.app.Activity;
import android.view.View;

import com.facebook.react.R;
import com.facebook.react.uimanager.util.ReactFindViewUtil;

public class ViewFinder {

    private static final int MAX_PARENT_HEIGHT = 7;
    private static ViewFinder instance;

    private ViewFinder() {
    }

    public static ViewFinder getInstance() {
        if (instance == null) {
            instance = new ViewFinder();
        }
        return instance;
    }

    public View findPublisherAdContainerInUpperHierarchy(View root) {
        return findPublisherAdContainerInUpperHierarchy(root, new SearchIterationCount());
    }

    private class SearchIterationCount {
        private int currenthIteration = 0;
    }

    private View findPublisherAdContainerInUpperHierarchy(View root, SearchIterationCount searchIteration) {
        View nativeContainerParent = null;
        if (root == null) {
            return null;
        }
        Object tag = root.getTag(R.id.view_tag_native_id);
        if (tag != null && tag.equals("publisherNativoAdContainer")) {
            return root;
        }
        if (searchIteration.currenthIteration < MAX_PARENT_HEIGHT && root instanceof View) {
            searchIteration.currenthIteration++;
            return findPublisherAdContainerInUpperHierarchy((View) root.getParent(), searchIteration);
        }
        return null;
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