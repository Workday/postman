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
import com.workday.postman.annotations.NotParceled;
import com.workday.postman.annotations.Parceled;

/**
 * @author nathan.taylor
 * @since 2013-9-25-16:02
 */
@Parceled
public class MyChildParcelable
        implements Parcelable {

    public static final Creator<MyChildParcelable> CREATOR = Postman.getCreator(MyChildParcelable.class);

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Postman.writeToParcel(this, dest);
    }

    public MyChildParcelable() {

    }

    public MyChildParcelable(String string, Boolean aBoolean) {
        this.aString = string;
        this.aBoolean = aBoolean;
    }

    String aString;

    Boolean aBoolean;

    @NotParceled
    String notParceled;
}
