/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.parceler;

import android.os.Bundle;
import android.os.Parcelable;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Map;

/**
 * A utility class that assists with writing {@link Map}s to {@link Bundle}s.
 * <p>
 * This implementation can handle the following types of values and keys in maps:
 * <ul>
 *     <li>{@link Integer}</li>
 *     <li>{@link String}</li>
 *     <li>{@link Parcelable}</li>
 * </ul>
 *
 * @author nathan.taylor
 * @since 2014-5-12
 */
public class MapBundler {

    private static final String KEYS_SUFFIX = "$$map_keys";
    private static final String VALUES_SUFFIX = "$$map_values";

    /**
     * Write the given map to bundle, using the given {@code bundleKey} to identify it.
     *
     * @param map The Map to write.
     * @param bundle The Bundle to which the Map will be written.
     * @param keyClass The class representing the type for the map keys.
     * @param valueClass The class representing the type for the map values.
     * @param bundleKey The string used to identify the map for later retrieval (see {@link Bundle#get(String)}).
     * @param <K> The type for the map keys.
     * @param <V> The type for the map values.
     */
    public static <K, V> void writeMapToBundle(Map<K, V> map, Bundle bundle, Class<K> keyClass, Class<V> valueClass,
                                               String bundleKey) {
        ArrayList<K> keys = new ArrayList<>(map.size());
        ArrayList<V> values = new ArrayList<>(map.size());

        for (Map.Entry<K, V> entry : map.entrySet()) {
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }

        ArrayListBundler.writeArrayListToBundle(keys, bundle, keyClass, bundleKey + KEYS_SUFFIX);
        ArrayListBundler.writeArrayListToBundle(values, bundle, valueClass, bundleKey + VALUES_SUFFIX);
    }

    /**
     * Read map stored in the bundle using the given {@code bundleKey}, populating {@code map} with the result.
     *
     * @param map The Map to populate
     * @param bundle The Bundle from which the Map will be read.
     * @param keyClass The class representing the type for the map keys.
     * @param valueClass The class representing the type for the map values.
     * @param bundleKey The string used to identify the map (see {@link Bundle#get(String)}).
     * @param <K> The type for the map keys.
     * @param <V> The type for the map values.
     */
    public static <K, V> void readMapFromBundle(Map<K, V> map, Bundle bundle, Class<K> keyClass, Class<V> valueClass,
                                                String bundleKey) {
        ArrayList<K> keys = ArrayListBundler.readArrayListFromBundle(bundle, keyClass, bundleKey + KEYS_SUFFIX);
        ArrayList<V> values = ArrayListBundler.readArrayListFromBundle(bundle, valueClass, bundleKey + VALUES_SUFFIX);
        if (keys == null && values == null) {
            return;
        }

        Preconditions.checkState(keys.size() == values.size(),
                                 String.format("Expected keys.size() (%d) and values.size() (%d) to be the same.",
                                               keys.size(), values.size())
        );

        for (int i = 0; i < keys.size(); i++) {
            map.put(keys.get(i), values.get(i));
        }
    }

}
