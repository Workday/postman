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
public class CharParcelableAdapter implements ParcelableAdapter<Character> {

    private final Character value;

    public CharParcelableAdapter(Character value) {
        this.value = value;
    }

    public static final Creator<CharParcelableAdapter> CREATOR =
            new Creator<CharParcelableAdapter>() {


                @Override
                public CharParcelableAdapter createFromParcel(Parcel source) {
                    return new CharParcelableAdapter((char) source.readInt());
                }

                @Override
                public CharParcelableAdapter[] newArray(int size) {
                    return new CharParcelableAdapter[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(value);
    }

    @Override
    public Character getValue() {
        return value;
    }

}
