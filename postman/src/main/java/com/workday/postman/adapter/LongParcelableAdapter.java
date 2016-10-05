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
public class LongParcelableAdapter implements ParcelableAdapter<Long> {

    private final Long value;

    public static final Creator<LongParcelableAdapter> CREATOR =
            new Creator<LongParcelableAdapter>() {


                @Override
                public LongParcelableAdapter createFromParcel(Parcel source) {
                    return new LongParcelableAdapter(source.readLong());
                }

                @Override
                public LongParcelableAdapter[] newArray(int size) {
                    return new LongParcelableAdapter[size];
                }
            };

    public LongParcelableAdapter(Long value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(value);
    }

    @Override
    public Long getValue() {
        return value;
    }

}
