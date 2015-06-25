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
import com.workday.postman.annotations.Parceled;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * @author nathan.taylor
 * @since 2013-9-25-15:40
 */
public class MyParcelable
        implements Parcelable {

    public static final Creator<MyParcelable> CREATOR = Postman.getCreator(MyParcelable.class);

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Postman.writeToParcel(this, dest);

    }

    @Parceled
    int myInt;

    @Parceled
    CharSequence myCharSequence;

    @Parceled
    String myString;

    @Parceled
    MyChildParcelable myChildParcelable;

    @Parceled
    ArrayList<MyChildParcelable> myParcelableList;

    @Parceled
    ArrayList<String> myStringList;

    @Parceled
    ArrayList<CharSequence> myCharSequenceList;

    @Parceled
    MySerializable mySerializable;

    @Parceled
    Map<String, String> myStringMap;

    @Parceled
    Set<Integer> myIntegerSet;

    BigDecimal notParceled;
}
