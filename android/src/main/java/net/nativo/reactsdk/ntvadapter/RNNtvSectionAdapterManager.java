package net.nativo.reactsdk.ntvadapter;

import java.util.HashMap;
import java.util.Map;

public class RNNtvSectionAdapterManager {

    private static RNNtvSectionAdapterManager instance;
    private Map<String, Map<Integer, RNNtvSectionAdapter>> ntvSectionAdapterMap = new HashMap<>();

    public static RNNtvSectionAdapterManager getInstance() {
        if (instance == null) {
            instance = new RNNtvSectionAdapterManager();
        }
        return instance;
    }

    public RNNtvSectionAdapter getNtvSectionAdapter(String sectionUrl, int index) {
        if (!ntvSectionAdapterMap.containsKey(sectionUrl)) {
            ntvSectionAdapterMap.put(sectionUrl, new HashMap<Integer, RNNtvSectionAdapter>());
        }
        Map<Integer, RNNtvSectionAdapter> map = ntvSectionAdapterMap.get(sectionUrl);
        if (!map.containsKey(index)) {
            map.put(index, new RNNtvSectionAdapter());
        }
        return map.get(index);
    }

    public void removeNtvSectionAdapter(String sectionUrl, int index) {
        if (ntvSectionAdapterMap.containsKey(sectionUrl)) {
            Map<Integer, RNNtvSectionAdapter> map = ntvSectionAdapterMap.get(sectionUrl);
            map.remove(index);
        }
    }

}
