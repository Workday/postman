/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author nathan.taylor
 * @since 2015-08-28.
 */
public class HashMapParcelableAdapter implements ParcelableAdapter<HashMap> {

    public static final Creator<HashMapParcelableAdapter> CREATOR =
            new Creator<HashMapParcelableAdapter>() {


                @Override
                public HashMapParcelableAdapter createFromParcel(Parcel source) {
                    final Parcelable[] keys =
                            source.readParcelableArray(ParcelableAdapter.class.getClassLoader());
                    final Parcelable[] values =
                            source.readParcelableArray(ParcelableAdapter.class.getClassLoader());
                    if (keys.length != values.length) {
                        final String message = String.format(Locale.US,
                                                             "Length of keys array (%d) does not "
                                                                     + "match length of values "
                                                                     + "array (%d)",
                                                             keys.length,
                                                             values.length);
                        throw new IllegalStateException(message);
                    }

                    final HashMap<Object, Object> map = new HashMap<>();
                    for (int i = 0; i < keys.length; i++) {
                        final Object key = ParcelableAdapters.unwrapParcelable(keys[i]);
                        final Object value = ParcelableAdapters.unwrapParcelable(values[i]);
                        map.put(key, value);
                    }
                    return new HashMapParcelableAdapter(map);
                }

                @Override
                public HashMapParcelableAdapter[] newArray(int size) {
                    return new HashMapParcelableAdapter[size];
                }
            };

    private HashMap<Object, Object> value;

    public HashMapParcelableAdapter(HashMap value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        final Parcelable[] keys = new Parcelable[value.size()];
        final Parcelable[] values = new Parcelable[value.size()];

        int i = 0;
        for (Map.Entry entry : value.entrySet()) {
            keys[i] = ParcelableAdapters.asParcelable(entry.getKey());
            values[i] = ParcelableAdapters.asParcelable(entry.getValue());
        }

        dest.writeParcelableArray(keys, flags);
        dest.writeParcelableArray(values, flags);
    }

    @Override
    public HashMap getValue() {
        return value;
    }
}
