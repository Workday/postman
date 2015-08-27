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
public class FloatParcelableAdapter implements ParcelableAdapter<Float> {

    private final Float value;

    public FloatParcelableAdapter(Float value) {
        this.value = value;
    }

    public static final Creator<FloatParcelableAdapter> CREATOR =
            new Creator<FloatParcelableAdapter>() {


                @Override
                public FloatParcelableAdapter createFromParcel(Parcel source) {
                    return new FloatParcelableAdapter(source.readFloat());
                }

                @Override
                public FloatParcelableAdapter[] newArray(int size) {
                    return new FloatParcelableAdapter[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(value);
    }

    @Override
    public Float getValue() {
        return value;
    }

}
