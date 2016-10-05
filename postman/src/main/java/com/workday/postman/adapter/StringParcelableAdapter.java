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
public class StringParcelableAdapter implements ParcelableAdapter<String> {

    private final String value;

    public static final Creator<StringParcelableAdapter> CREATOR =
            new Creator<StringParcelableAdapter>() {


                @Override
                public StringParcelableAdapter createFromParcel(Parcel source) {
                    return new StringParcelableAdapter(source.readString());
                }

                @Override
                public StringParcelableAdapter[] newArray(int size) {
                    return new StringParcelableAdapter[size];
                }
            };

    public StringParcelableAdapter(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value);
    }

    @Override
    public String getValue() {
        return value;
    }

}
