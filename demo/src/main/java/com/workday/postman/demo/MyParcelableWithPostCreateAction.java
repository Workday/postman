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
import com.workday.postman.annotations.PostCreateChild;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author nathan.taylor
 * @since 2014-12-02
 */
@Parceled
public class MyParcelableWithPostCreateAction implements Parcelable {

    MyChildParcelable myChildParcelable;
    ArrayList<MyChildParcelable> myChildren;
    MySerializable mySerializable;
    Map<MyChildParcelable, MyChildParcelable> myMap;
    String string;

    @PostCreateChild
    void onChildRead(Object child) {
        if (child instanceof MyChildParcelable) {
            ((MyChildParcelable) child).aString += " seen";
        }
    }

    public static final Creator<MyParcelableWithPostCreateAction> CREATOR = Postman.getCreator(
            MyParcelableWithPostCreateAction.class);

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Postman.writeToParcel(this, dest);
    }
}
