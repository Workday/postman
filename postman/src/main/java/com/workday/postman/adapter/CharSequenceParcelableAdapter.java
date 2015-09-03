/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.adapter;

import android.os.Parcel;
import android.text.TextUtils;

/**
 * @author Nathan Taylor
 * @since 2015-04-26
 */
public class CharSequenceParcelableAdapter implements ParcelableAdapter<CharSequence> {

    private final CharSequence value;

    public CharSequenceParcelableAdapter(CharSequence value) {
        this.value = value;
    }

    public static final Creator<CharSequenceParcelableAdapter> CREATOR =
            new Creator<CharSequenceParcelableAdapter>() {


                @Override
                public CharSequenceParcelableAdapter createFromParcel(Parcel source) {
                    final CharSequence value =
                            TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
                    return new CharSequenceParcelableAdapter(value);
                }

                @Override
                public CharSequenceParcelableAdapter[] newArray(int size) {
                    return new CharSequenceParcelableAdapter[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        TextUtils.writeToParcel(value, dest, flags);
    }

    @Override
    public CharSequence getValue() {
        return value;
    }

}
