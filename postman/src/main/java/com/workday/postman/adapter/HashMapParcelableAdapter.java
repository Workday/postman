/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.adapter;

import android.os.Parcelable;

import java.util.HashMap;

/**
 * @author nathan.taylor
 * @since 2015-08-28.
 */
public class HashMapParcelableAdapter extends AbstractMapParcelableAdapter<HashMap> {

    public static final Parcelable.Creator<HashMapParcelableAdapter> CREATOR =
            new Creator<HashMap, HashMapParcelableAdapter>() {


                @Override
                protected HashMap newMapInstance() {
                    return new HashMap();
                }

                @Override
                protected HashMapParcelableAdapter newParcelableAdapterInstance(
                        HashMap map) {
                    return new HashMapParcelableAdapter(map);
                }

                @Override
                public HashMapParcelableAdapter[] newArray(int size) {
                    return new HashMapParcelableAdapter[size];
                }
            };

    public HashMapParcelableAdapter(HashMap value) {
        super(value);
    }

}
