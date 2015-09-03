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
public class BooleanParcelableAdapter implements ParcelableAdapter<Boolean> {

    private final Boolean value;

    public BooleanParcelableAdapter(Boolean value) {
        this.value = value;
    }

    public static final Creator<BooleanParcelableAdapter> CREATOR =
            new Creator<BooleanParcelableAdapter>() {


                @Override
                public BooleanParcelableAdapter createFromParcel(Parcel source) {
                    return new BooleanParcelableAdapter(source.readInt() == 1);
                }

                @Override
                public BooleanParcelableAdapter[] newArray(int size) {
                    return new BooleanParcelableAdapter[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(value ? 1 : 0);
    }

    @Override
    public Boolean getValue() {
        return value;
    }

}
