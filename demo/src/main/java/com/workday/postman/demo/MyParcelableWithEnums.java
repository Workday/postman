/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.demo;

import android.os.Parcel;
import android.os.Parcelable;
import com.workday.postman.Postman;
import com.workday.postman.annotations.Parceled;

/**
 * @author nathan.taylor
 * @since 2013-12-30
 */
@Parceled
public class MyParcelableWithEnums implements Parcelable {

    MyEnum myEnum1;
    MyEnum myEnum2;
    MyEnum myEnum3;

    public static final Creator<MyParcelableWithEnums> CREATOR =
            Postman.getCreator(MyParcelableWithEnums.class);

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Postman.writeToParcel(this, dest);
    }
}
