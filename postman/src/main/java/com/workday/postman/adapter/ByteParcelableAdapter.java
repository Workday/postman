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
public class ByteParcelableAdapter implements ParcelableAdapter<Byte> {

    private final Byte value;

    public ByteParcelableAdapter(Byte value) {
        this.value = value;
    }

    public static final Creator<ByteParcelableAdapter> CREATOR =
            new Creator<ByteParcelableAdapter>() {


                @Override
                public ByteParcelableAdapter createFromParcel(Parcel source) {
                    return new ByteParcelableAdapter(source.readByte());
                }

                @Override
                public ByteParcelableAdapter[] newArray(int size) {
                    return new ByteParcelableAdapter[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(value);
    }

    @Override
    public Byte getValue() {
        return value;
    }

}
