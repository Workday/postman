/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.demo;

import android.os.Bundle;

import com.workday.postman.parceler.MapBundler;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author nathan.taylor
 * @since 2014-5-12
 */
@RunWith(RobolectricTestRunner.class)
public class MapBundlerTest {

    public static final String BUNDLE_KEY = "bundle_key";

    @Test
    public void testStringStringMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        Bundle bundle = new Bundle();
        MapBundler.writeMapToBundle(map, bundle, String.class, String.class, BUNDLE_KEY);

        map.clear();
        MapBundler.readMapFromBundle(map, bundle, String.class, String.class, BUNDLE_KEY);
        assertEquals("value1", map.get("key1"));
        assertEquals("value2", map.get("key2"));
    }

    @Test
    public void testEmptyMapDoesNotThrowException() {
        Map<String, String> map = new HashMap<>();

        Bundle bundle = new Bundle();
        MapBundler.writeMapToBundle(map, bundle, String.class, String.class, BUNDLE_KEY);

        map.clear();
        MapBundler.readMapFromBundle(map, bundle, String.class, String.class, BUNDLE_KEY);

        assertTrue("map.isEmpty()", map.isEmpty());
    }

    @Test
    public void testUnhandledTypeThrowsIllegalArgumentException() {
        Map<Object, String> map = new HashMap<>();
        map.put(new Object(), "value1");

        boolean writeThrewException = false;
        Bundle bundle = new Bundle();
        try {
            MapBundler.writeMapToBundle(map, bundle, Object.class, String.class, BUNDLE_KEY);
        } catch (IllegalArgumentException e) {
            writeThrewException = true;
        }
        assertTrue("MapBundler.writeMapToBundle throws IllegalArgumentException",
                   writeThrewException);

        map.clear();
        boolean readThrewException = false;
        try {
            MapBundler.readMapFromBundle(map, bundle, Object.class, String.class, BUNDLE_KEY);
        } catch (IllegalArgumentException e) {
            readThrewException = true;
        }
        assertTrue("MapBundler.readMapFromBundle throws IllegalArgumentException",
                   readThrewException);
    }

    @Test
    public void testIntParcelableMap() {
        Map<Integer, MyParcelable> map = new HashMap<>();
        MyParcelable value1 = new MyParcelable();
        MyParcelable value2 = new MyParcelable();
        map.put(1, value1);
        map.put(2, value2);

        Bundle bundle = new Bundle();
        MapBundler.writeMapToBundle(map, bundle, Integer.class, MyParcelable.class, BUNDLE_KEY);

        map.clear();
        MapBundler.readMapFromBundle(map, bundle, Integer.class, MyParcelable.class, BUNDLE_KEY);
        Assert.assertEquals(value1, map.get(1));
        Assert.assertEquals(value2, map.get(2));
    }

}
