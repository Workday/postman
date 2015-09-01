/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.adapter;

import android.os.Parcelable;

import java.util.LinkedHashMap;

/**
 * @author nathan.taylor
 * @since 2015-08-28.
 */
public class LinkedHashMapParcelableAdapter extends AbstractMapParcelableAdapter<LinkedHashMap> {

    public static final Parcelable.Creator<LinkedHashMapParcelableAdapter> CREATOR =
            new Creator<LinkedHashMap, LinkedHashMapParcelableAdapter>() {


                @Override
                protected LinkedHashMap newMapInstance() {
                    return new LinkedHashMap();
                }

                @Override
                protected LinkedHashMapParcelableAdapter newParcelableAdapterInstance(
                        LinkedHashMap map) {
                    return new LinkedHashMapParcelableAdapter(map);
                }

                @Override
                public LinkedHashMapParcelableAdapter[] newArray(int size) {
                    return new LinkedHashMapParcelableAdapter[size];
                }
            };

    public LinkedHashMapParcelableAdapter(LinkedHashMap value) {
        super(value);
    }

}
