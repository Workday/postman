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

import java.math.BigDecimal;
import java.math.BigInteger;
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

    @Test
    public void testStringObjectMap() {
        Map<String, Object> map = new HashMap<>();
        BigInteger value1 = new BigInteger("5");
        BigDecimal value2 = new BigDecimal("2.5");
        MyParcelable value3 = new MyParcelable();

        map.put("v1", value1);
        map.put("v2", value2);
        map.put("v3", value3);

        Bundle bundle = new Bundle();
        MapBundler.writeMapToBundle(map, bundle, String.class, Object.class, BUNDLE_KEY);

        map.clear();
        MapBundler.readMapFromBundle(map, bundle, String.class, Object.class, BUNDLE_KEY);
        assertEquals(value1, map.get("v1"));
        assertEquals(value2, map.get("v2"));
        assertEquals(value3, map.get("v3"));
    }

}
