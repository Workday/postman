/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.demo;

import android.os.Parcel;
import android.os.Parcelable;

import org.robolectric.RuntimeEnvironment;

public class ParcelTestUtils {

    private ParcelTestUtils() {
    }

    public static <T extends Parcelable> T writeAndReadParcelable(Parcelable in) {
        Parcel parcel = Parcel.obtain();
        parcel.writeParcelable(in, 0);

        parcel.setDataPosition(0);
        T value = parcel.readParcelable(RuntimeEnvironment.application.getClassLoader());

        parcel.recycle();

        return value;
    }
}