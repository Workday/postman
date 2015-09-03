/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.adapter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author nathan.taylor
 * @since 2015-08-27.
 */
public class LinkedListParcelableAdapter extends AbstractCollectionParcelableAdapter<LinkedList> {

    public static final Creator<LinkedListParcelableAdapter> CREATOR =
            new Creator<LinkedListParcelableAdapter>() {

                @Override
                protected LinkedListParcelableAdapter newParcelableAdapterInstance(List<Object>
                                                                                           items) {
                    return new LinkedListParcelableAdapter(new LinkedList<>(items));
                }

                @Override
                public LinkedListParcelableAdapter[] newArray(int size) {
                    return new LinkedListParcelableAdapter[size];
                }
            };

    public LinkedListParcelableAdapter(LinkedList value) {
        super(value);
    }
}

