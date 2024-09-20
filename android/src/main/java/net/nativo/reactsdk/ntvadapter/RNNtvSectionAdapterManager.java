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
