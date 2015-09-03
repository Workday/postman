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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author nathan.taylor
 * @since 2015-08-28.
 */
@Parceled
public class MyNestedCollectionParcelable implements Parcelable {

    ArrayList<ArrayList<String>> myStringCollections;
    ArrayList<ArrayList<MyParcelable>> myParcelableCollections;
    HashMap<String, Object> myObjectMap;


    // ====== Parcelable Implementation =======

    public static final Creator<MyNestedCollectionParcelable> CREATOR =
            Postman.getCreator(MyNestedCollectionParcelable.class);

    MyNestedCollectionParcelable() {
        // default constructor required by Postman
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Postman.writeToParcel(this, dest);
    }


}
