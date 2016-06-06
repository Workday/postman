/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.adapter;

import android.os.Parcel;

import java.math.BigDecimal;

/**
 * @author Nathan Taylor
 * @since 2015-04-26
 */
public class BigDecimalParcelableAdapter implements ParcelableAdapter<BigDecimal> {

    private final BigDecimal value;

    public static final Creator<BigDecimalParcelableAdapter> CREATOR =
            new Creator<BigDecimalParcelableAdapter>() {


                @Override
                public BigDecimalParcelableAdapter createFromParcel(Parcel source) {
                    return new BigDecimalParcelableAdapter(new BigDecimal(source.readString()));
                }

                @Override
                public BigDecimalParcelableAdapter[] newArray(int size) {
                    return new BigDecimalParcelableAdapter[size];
                }
            };

    public BigDecimalParcelableAdapter(BigDecimal value) {
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
    public BigDecimal getValue() {
        return value;
    }

}
