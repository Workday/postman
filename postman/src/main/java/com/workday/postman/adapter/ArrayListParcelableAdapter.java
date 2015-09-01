/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nathan.taylor
 * @since 2015-08-27.
 */
public class ArrayListParcelableAdapter extends AbstractCollectionParcelableAdapter<ArrayList> {

    public static final Creator<ArrayListParcelableAdapter> CREATOR =
            new Creator<ArrayListParcelableAdapter>() {

                @Override
                protected ArrayListParcelableAdapter newParcelableAdapterInstance(List<Object>
                                                                                          items) {
                    return new ArrayListParcelableAdapter(new ArrayList<>(items));
                }

                @Override
                public ArrayListParcelableAdapter[] newArray(int size) {
                    return new ArrayListParcelableAdapter[size];
                }
            };

    public ArrayListParcelableAdapter(ArrayList value) {
        super(value);
    }
}
