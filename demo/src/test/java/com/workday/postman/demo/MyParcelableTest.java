/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.demo;

import android.os.Parcel;
import android.os.Parcelable;

import com.workday.postman.Postman;
import com.workday.postman.PostmanException;
import com.workday.postman.util.CollectionUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author nathan.taylor
 * @since 2013-9-25-15:59
 */
@RunWith(RobolectricTestRunner.class)
public class MyParcelableTest {

    @Test
    public void testParcelable() {
        MyParcelable in = new MyParcelable();
        in.myInt = 5;
        in.myCharSequence = "Andy";
        in.myString = "Bob";
        in.notParceled = BigDecimal.ONE;
        in.myChildParcelable = new MyChildParcelable();
        in.myChildParcelable.aBoolean = true;
        in.myChildParcelable.aString = "Robby";
        in.myChildParcelable.notParceled = "I shouldn't be retained.";
        in.myParcelableList = CollectionUtils.newArrayList(new MyChildParcelable("a", false),
                new MyChildParcelable("b", true));
        in.myStringMap = new HashMap<>();
        in.myStringMap.put("one key", "one value");
        in.myStringMap.put("two key", "two value");

        MyParcelable out = writeAndReadParcelable(in);

        assertEquals(5, out.myInt);
        assertEquals("Andy", out.myCharSequence);
        assertEquals("Bob", out.myString);
        assertNull(out.notParceled);
        assertTrue(out.myChildParcelable.aBoolean);
        assertEquals("Robby", out.myChildParcelable.aString);
        assertNull(out.myChildParcelable.notParceled);
        assertEquals(2, out.myParcelableList.size());
        assertEquals("a", out.myParcelableList.get(0).aString);
        assertFalse(out.myParcelableList.get(0).aBoolean);
        assertEquals("b", out.myParcelableList.get(1).aString);
        assertTrue(out.myParcelableList.get(1).aBoolean);

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("one key", "one value");
        expectedMap.put("two key", "two value");
        assertEquals(expectedMap, out.myStringMap);
    }

    @Test
    public void testStringArrayList() {
        MyParcelable in = new MyParcelable();
        ArrayList<String> stringList = CollectionUtils.newArrayList("one", "two");
        in.myStringList = stringList;

        MyParcelable out = writeAndReadParcelable(in);

        assertEquals(stringList, out.myStringList);
    }

    @Test
    public void testCharSequenceArrayList() {
        MyParcelable in = new MyParcelable();
        ArrayList<CharSequence> charSequenceList = CollectionUtils.<CharSequence> newArrayList("one", "two");
        in.myCharSequenceList = charSequenceList;

        MyParcelable out = writeAndReadParcelable(in);

        assertEquals(charSequenceList, out.myCharSequenceList);
    }

    @Test
    public void testSet() {
        MyParcelable in = new MyParcelable();
        Set<Integer> set = CollectionUtils.newHashSet(1, 2, 3);
        in.myIntegerSet = set;

        MyParcelable out = writeAndReadParcelable(in);

        assertEquals(set, out.myIntegerSet);
    }

    @Test
    public void testSerializable() {

        MyParcelable in = new MyParcelable();
        in.mySerializable = new MySerializable();

        MyParcelable out = writeAndReadParcelable(in);

        assertNotNull(out.mySerializable);
    }

    @Test
    public void testEnums() {
        MyParcelableWithEnums in = new MyParcelableWithEnums();
        in.myEnum1 = MyEnum.VALUE_1;
        in.myEnum2 = MyEnum.VALUE_2;
        in.myEnum3 = null;

        MyParcelableWithEnums out = writeAndReadParcelable(in);

        assertEquals(MyEnum.VALUE_1, out.myEnum1);
        assertEquals(MyEnum.VALUE_2, out.myEnum2);
        assertNull(out.myEnum3);
    }

    @Test
    public void testNonParceledClassThrowsPostmanException() {
        Object o = new Object();

        boolean exceptionCaught = false;
        try {
            Postman.writeToParcel(o, Parcel.obtain());
        }
        catch (PostmanException e) {
            exceptionCaught = true;
            assertTrue("expected cause to be of type ClassNotFoundException but found " +
                       e.getCause().getClass().getCanonicalName(),
                       e.getCause() instanceof ClassNotFoundException);
        }
        assertTrue(exceptionCaught);
    }

    @Test
    public void testParcelableWithPostCreateAction() {
        MyParcelableWithPostCreateAction in = new MyParcelableWithPostCreateAction();
        in.myChildParcelable = new MyChildParcelable("child", false);
        in.myChildren = CollectionUtils.newArrayList(new MyChildParcelable("list child", false));
        in.mySerializable = new MySerializable();
        in.myMap = new HashMap<>();
        in.myMap.put(new MyChildParcelable("key", false), new MyChildParcelable("value", false));
        in.string = "string";

        MyParcelableWithPostCreateAction out = writeAndReadParcelable(in);
        assertEquals("child seen", out.myChildParcelable.aString);
        assertEquals("list child seen", out.myChildren.get(0).aString);
        assertNotNull(out.mySerializable);
        Map.Entry<MyChildParcelable, MyChildParcelable> entry = out.myMap.entrySet().iterator().next();
        assertEquals("key seen", entry.getKey().aString);
        assertEquals("value seen", entry.getValue().aString);
        assertEquals("string", out.string);
    }

    private <T extends Parcelable> T writeAndReadParcelable(Parcelable in) {
        Parcel parcel = Parcel.obtain();
        parcel.writeParcelable(in, 0);

        parcel.setDataPosition(0);
        return parcel.readParcelable(RuntimeEnvironment.application.getClassLoader());
    }
}
