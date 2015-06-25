/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.parceler;

import android.os.Parcel;

/**
 * Interface for a class that handles the parceling of another object.
 *
 * @author nathan.taylor
 * @since 2013-9-25-15:03
 */
public interface Parceler<T> {

    void writeToParcel(T object, Parcel parcel);

    T readFromParcel(Parcel parcel);

    T[] newArray(int size);

}
