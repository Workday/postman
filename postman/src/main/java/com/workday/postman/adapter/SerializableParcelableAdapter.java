/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.adapter;

import android.os.Parcel;

import java.io.Serializable;

/**
 * @author nathan.taylor
 * @since 2015-09-02.
 */
public class SerializableParcelableAdapter implements ParcelableAdapter<Serializable> {

    public static final Creator<SerializableParcelableAdapter> CREATOR =
            new Creator<SerializableParcelableAdapter>() {


                @Override
                public SerializableParcelableAdapter createFromParcel(Parcel source) {
                    return new SerializableParcelableAdapter(source.readSerializable());
                }

                @Override
                public SerializableParcelableAdapter[] newArray(int size) {
                    return new SerializableParcelableAdapter[size];
                }
            };

    private final Serializable value;

    public SerializableParcelableAdapter(Serializable value) {
        this.value = value;
    }

    @Override
    public Serializable getValue() {
        return value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(value);
    }
}
