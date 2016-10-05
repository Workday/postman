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
public class ShortParcelableAdapter implements ParcelableAdapter<Short> {

    private final Short value;

    public static final Creator<ShortParcelableAdapter> CREATOR =
            new Creator<ShortParcelableAdapter>() {


                @Override
                public ShortParcelableAdapter createFromParcel(Parcel source) {
                    return new ShortParcelableAdapter((short) source.readInt());
                }

                @Override
                public ShortParcelableAdapter[] newArray(int size) {
                    return new ShortParcelableAdapter[size];
                }
            };

    public ShortParcelableAdapter(Short value) {
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
    public Short getValue() {
        return value;
    }

}
