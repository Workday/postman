/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.adapter;

import java.util.HashSet;
import java.util.List;

/**
 * @author nathan.taylor
 * @since 2015-08-27.
 */
public class HashSetParcelableAdapter extends AbstractCollectionParcelableAdapter<HashSet> {

    public static final Creator<HashSetParcelableAdapter> CREATOR =
            new Creator<HashSetParcelableAdapter>() {

                @Override
                protected HashSetParcelableAdapter newParcelableAdapterInstance(List<Object>
                                                                                        items) {
                    return new HashSetParcelableAdapter(new HashSet<>(items));
                }

                @Override
                public HashSetParcelableAdapter[] newArray(int size) {
                    return new HashSetParcelableAdapter[size];
                }
            };

    public HashSetParcelableAdapter(HashSet value) {
        super(value);
    }
}
