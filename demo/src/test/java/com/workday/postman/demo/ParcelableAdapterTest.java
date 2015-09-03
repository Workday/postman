/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.demo;

import com.workday.postman.adapter.ArrayListParcelableAdapter;
import com.workday.postman.adapter.BigDecimalParcelableAdapter;
import com.workday.postman.adapter.BigIntegerParcelableAdapter;
import com.workday.postman.adapter.BooleanParcelableAdapter;
import com.workday.postman.adapter.ByteParcelableAdapter;
import com.workday.postman.adapter.CharParcelableAdapter;
import com.workday.postman.adapter.DoubleParcelableAdapter;
import com.workday.postman.adapter.FloatParcelableAdapter;
import com.workday.postman.adapter.HashMapParcelableAdapter;
import com.workday.postman.adapter.HashSetParcelableAdapter;
import com.workday.postman.adapter.IntParcelableAdapter;
import com.workday.postman.adapter.LinkedHashMapParcelableAdapter;
import com.workday.postman.adapter.LinkedHashSetParcelableAdapter;
import com.workday.postman.adapter.LinkedListParcelableAdapter;
import com.workday.postman.adapter.LongParcelableAdapter;
import com.workday.postman.adapter.ParcelableAdapters;
import com.workday.postman.adapter.SerializableParcelableAdapter;
import com.workday.postman.adapter.ShortParcelableAdapter;
import com.workday.postman.adapter.StringParcelableAdapter;
import com.workday.postman.adapter.TreeSetParcelableAdapter;
import com.workday.postman.util.CollectionUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.TreeSet;

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
        assertTrue(ParcelableAdapters.asParcelable(new Throwable()) instanceof
                           SerializableParcelableAdapter);
        assertTrue(ParcelableAdapters.asParcelable("r2-d2") instanceof StringParcelableAdapter);
        assertTrue(ParcelableAdapters.asParcelable(new ArrayList<String>()) instanceof
                           ArrayListParcelableAdapter);
        assertTrue(ParcelableAdapters.asParcelable(new LinkedList<String>()) instanceof
                           LinkedListParcelableAdapter);
        assertTrue(ParcelableAdapters.asParcelable(new HashSet<String>()) instanceof
                           HashSetParcelableAdapter);
        assertTrue(ParcelableAdapters.asParcelable(new LinkedHashSet<String>()) instanceof
                           LinkedHashSetParcelableAdapter);
        assertTrue(ParcelableAdapters.asParcelable(new TreeSet<String>()) instanceof
                           TreeSetParcelableAdapter);
        assertTrue(ParcelableAdapters.asParcelable(new HashMap<String, String>()) instanceof
                           HashMapParcelableAdapter);
        assertTrue(ParcelableAdapters.asParcelable(new LinkedHashMap<String, String>())
                           instanceof LinkedHashMapParcelableAdapter);
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

        MyNestedCollectionParcelable out = ParcelTestUtils.writeAndReadParcelable(in);

        assertEquals(outerList, out.myStringCollections);
    }

    @Test
    public void testListOfListOfCustomObject() {
        MyNestedCollectionParcelable in = new MyNestedCollectionParcelable();
        ArrayList<ArrayList<MyParcelable>> outerList = new ArrayList<>();
        ArrayList<MyParcelable> innerList0 = new ArrayList<>();
        MyParcelable myParcelable = new MyParcelable();
        myParcelable.myString = "Lore";
        innerList0.add(myParcelable);
        outerList.add(innerList0);

        in.myParcelableCollections = outerList;

        MyNestedCollectionParcelable out = ParcelTestUtils.writeAndReadParcelable(in);

        assertEquals("Lore", out.myParcelableCollections.get(0).get(0).myString);
    }

    @Test
    public void testMapOfObjects() {
        MyNestedCollectionParcelable in = new MyNestedCollectionParcelable();

        HashMap<String, Object> map = new HashMap<>();
        map.put("bigDecimal", new BigDecimal("36"));
        map.put("bigInteger", new BigInteger("16"));
        map.put("boolean", true);
        map.put("byte", (byte) 5);
        map.put("char", 'c');
        map.put("double", 2.0);
        map.put("float", 3.1f);
        map.put("int", 1);
        map.put("long", 49L);
        map.put("short", (short) 4);
        map.put("serializable", new Throwable("Cube 1834"));
        map.put("string", "USS Enterprise");
        map.put("myParcelable", new MyParcelable("USS Voyager"));
        map.put("arrayList", CollectionUtils.newArrayList("USS Defiant"));

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Captain", "Picard");
        map.put("hashMap", hashMap);

        in.myObjectMap = map;

        MyNestedCollectionParcelable out = ParcelTestUtils.writeAndReadParcelable(in);

        assertEquals("size", 15, out.myObjectMap.size());

        assertEquals("bigDecimal", new BigDecimal("36"), out.myObjectMap.get("bigDecimal"));
        assertEquals("bigInteger", new BigInteger("16"), out.myObjectMap.get("bigInteger"));
        assertEquals("boolean", true, out.myObjectMap.get("boolean"));
        assertEquals("byte", (byte) 5, out.myObjectMap.get("byte"));
        assertEquals("char", 'c', out.myObjectMap.get("char"));
        assertEquals("double", 2.0, out.myObjectMap.get("double"));
        assertEquals("float", 3.1f, out.myObjectMap.get("float"));
        assertEquals("int", 1, out.myObjectMap.get("int"));
        assertEquals("long", 49L, out.myObjectMap.get("long"));
        assertEquals("short", (short) 4, out.myObjectMap.get("short"));
        assertEquals("serializable",
                     "Cube 1834",
                     ((Throwable) out.myObjectMap.get("serializable")).getMessage());

        assertEquals("string", "USS Enterprise", out.myObjectMap.get("string"));
        assertEquals("myParcelable",
                     "USS Voyager",
                     ((MyParcelable) out.myObjectMap.get("myParcelable")).myString);
        assertEquals("arrayList",
                     CollectionUtils.newArrayList("USS Defiant"),
                     out.myObjectMap.get("arrayList"));
        assertEquals("hashMap", hashMap, out.myObjectMap.get("hashMap"));
    }
}
