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
public class DoubleParcelableAdapter implements ParcelableAdapter<Double> {

    private final Double value;

    public static final Creator<DoubleParcelableAdapter> CREATOR =
            new Creator<DoubleParcelableAdapter>() {


                @Override
                public DoubleParcelableAdapter createFromParcel(Parcel source) {
                    return new DoubleParcelableAdapter(source.readDouble());
                }

                @Override
                public DoubleParcelableAdapter[] newArray(int size) {
                    return new DoubleParcelableAdapter[size];
                }
            };

    public DoubleParcelableAdapter(Double value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(value);
    }

    @Override
    public Double getValue() {
        return value;
    }

}
