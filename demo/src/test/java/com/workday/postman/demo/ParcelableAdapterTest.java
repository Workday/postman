/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.demo;

import com.workday.postman.adapter.BigDecimalParcelableAdapter;
import com.workday.postman.adapter.BigIntegerParcelableAdapter;
import com.workday.postman.adapter.BooleanParcelableAdapter;
import com.workday.postman.adapter.ByteParcelableAdapter;
import com.workday.postman.adapter.CharParcelableAdapter;
import com.workday.postman.adapter.DoubleParcelableAdapter;
import com.workday.postman.adapter.FloatParcelableAdapter;
import com.workday.postman.adapter.IntParcelableAdapter;
import com.workday.postman.adapter.LongParcelableAdapter;
import com.workday.postman.adapter.ParcelableAdapters;
import com.workday.postman.adapter.ShortParcelableAdapter;
import com.workday.postman.adapter.StringParcelableAdapter;
import com.workday.postman.util.CollectionUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author nathan.taylor
 * @since 2015-08-26.
 */
@RunWith(RobolectricTestRunner.class)
public class ParcelableAdapterTest {

    @Test
    public void testAsParcelable() {
        assertTrue(ParcelableAdapters.asParcelable(BigDecimal.TEN) instanceof
                           BigDecimalParcelableAdapter);
        assertTrue(ParcelableAdapters.asParcelable(BigInteger.TEN) instanceof
                           BigIntegerParcelableAdapter);
        assertTrue(ParcelableAdapters.asParcelable((byte) 5) instanceof ByteParcelableAdapter);
        assertTrue(ParcelableAdapters.asParcelable(true) instanceof BooleanParcelableAdapter);
        assertTrue(ParcelableAdapters.asParcelable('c') instanceof CharParcelableAdapter);
        assertTrue(ParcelableAdapters.asParcelable(2.0) instanceof DoubleParcelableAdapter);
        assertTrue(ParcelableAdapters.asParcelable(3.0f) instanceof FloatParcelableAdapter);
        assertTrue(ParcelableAdapters.asParcelable(3) instanceof IntParcelableAdapter);
        assertTrue(ParcelableAdapters.asParcelable(3L) instanceof LongParcelableAdapter);
        assertTrue(ParcelableAdapters.asParcelable((short) 3) instanceof ShortParcelableAdapter);
        assertTrue(ParcelableAdapters.asParcelable("r2-d2") instanceof StringParcelableAdapter);
    }

    @Test
    public void testListOfList() {
        MyNestedCollectionParcelable in = new MyNestedCollectionParcelable();
        ArrayList<ArrayList<String>> outerList = new ArrayList<>();
        ArrayList<String> innerList0 = new ArrayList<>();
        innerList0.add("00");
        innerList0.add("01");
        ArrayList<String> innerList1 = new ArrayList<>();
        innerList1.add("10");
        innerList1.add("11");
        outerList.add(innerList0);
        outerList.add(innerList1);

        in.myStringCollections = outerList;

        MyNestedCollectionParcelable out = ParcelUtils.writeAndReadParcelable(in);

        assertEquals(outerList, out.myStringCollections);
    }

    @Test
    public void testListOfListCustomObject() {
        MyNestedCollectionParcelable in = new MyNestedCollectionParcelable();
        ArrayList<ArrayList<MyParcelable>> outerList = new ArrayList<>();
        ArrayList<MyParcelable> innerList0 = new ArrayList<>();
        MyParcelable myParcelable = new MyParcelable();
        myParcelable.myString = "Lore";
        innerList0.add(myParcelable);
        outerList.add(innerList0);

        in.myParcelableCollections = outerList;

        MyNestedCollectionParcelable out = ParcelUtils.writeAndReadParcelable(in);

        assertEquals("Lore", outerList.get(0).get(0).myString);
    }

    @Test
    public void testMapOfObjects() {
        MyNestedCollectionParcelable in = new MyNestedCollectionParcelable();

        HashMap<String, Object> map = new HashMap<>();
        map.put("int", 1);
        map.put("double", 2.0);
        map.put("string", "USS Enterprise");
        map.put("myParcelable", new MyParcelable("USS Voyager"));
        map.put("arrayList", CollectionUtils.newArrayList("USS Defiant"));

        in.myObjectMap = map;

        MyNestedCollectionParcelable out = ParcelUtils.writeAndReadParcelable(in);

        assertEquals("size", 5, out.myObjectMap.size());
        assertEquals("int", 1, out.myObjectMap.get("int"));
        assertEquals("double", 2.0, out.myObjectMap.get("double"));
        assertEquals("string", "USS Enterprise", out.myObjectMap.get("string"));
        assertEquals("myParcelable",
                     "USS Voyager",
                     ((MyParcelable) out.myObjectMap.get("myParcelable")).myString);
        assertEquals("arrayList",
                     CollectionUtils.newArrayList("USS Defiant"),
                     out.myObjectMap.get("arrayList"));
    }
}
