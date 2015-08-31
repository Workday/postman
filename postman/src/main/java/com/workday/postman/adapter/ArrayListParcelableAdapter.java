/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author nathan.taylor
 * @since 2015-08-27.
 */
public class ArrayListParcelableAdapter implements ParcelableAdapter<ArrayList> {

    public static final Creator<ArrayListParcelableAdapter> CREATOR =
            new Creator<ArrayListParcelableAdapter>() {

                @Override
                public ArrayListParcelableAdapter createFromParcel(Parcel source) {
                    final Parcelable[] wrapped =
                            source.readParcelableArray(ParcelableAdapter.class.getClassLoader());
                    final Object[] unwrapped = ParcelableAdapters.unwrapParcelableArray(wrapped);
                    final ArrayList<Object> unwrappedList =
                            new ArrayList<>(Arrays.asList(unwrapped));
                    return new ArrayListParcelableAdapter(unwrappedList);
                }

                @Override
                public ArrayListParcelableAdapter[] newArray(int size) {
                    return new ArrayListParcelableAdapter[size];
                }
            };

    private final ArrayList value;

    public ArrayListParcelableAdapter(ArrayList value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelableArray(ParcelableAdapters.toParcelableArray(value), flags);
    }

    @Override
    public ArrayList getValue() {
        return value;
    }
}
