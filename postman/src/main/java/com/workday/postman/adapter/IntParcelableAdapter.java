/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.adapter;

import android.os.Parcel;

/**
 * @author Nathan Taylor
 * @since 2015-04-26
 */
public class IntParcelableAdapter implements ParcelableAdapter<Integer> {

    private final Integer value;

    public static final Creator<IntParcelableAdapter> CREATOR =
            new Creator<IntParcelableAdapter>() {


                @Override
                public IntParcelableAdapter createFromParcel(Parcel source) {
                    return new IntParcelableAdapter(source.readInt());
                }

                @Override
                public IntParcelableAdapter[] newArray(int size) {
                    return new IntParcelableAdapter[size];
                }
            };

    public IntParcelableAdapter(Integer value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(value);
    }

    @Override
    public Integer getValue() {
        return value;
    }

}
