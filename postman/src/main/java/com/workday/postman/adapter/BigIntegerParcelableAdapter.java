/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.adapter;

import android.os.Parcel;

import java.math.BigInteger;

/**
 * @author Nathan Taylor
 * @since 2015-04-26
 */
public class BigIntegerParcelableAdapter implements ParcelableAdapter<BigInteger> {

    private final BigInteger value;

    public static final Creator<BigIntegerParcelableAdapter> CREATOR =
            new Creator<BigIntegerParcelableAdapter>() {


                @Override
                public BigIntegerParcelableAdapter createFromParcel(Parcel source) {
                    return new BigIntegerParcelableAdapter(new BigInteger(source.readString()));
                }

                @Override
                public BigIntegerParcelableAdapter[] newArray(int size) {
                    return new BigIntegerParcelableAdapter[size];
                }
            };

    public BigIntegerParcelableAdapter(BigInteger value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value.toString());
    }

    @Override
    public BigInteger getValue() {
        return value;
    }

}
