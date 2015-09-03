/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.adapter;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author nathan.taylor
 * @since 2015-08-27.
 */
public class LinkedHashSetParcelableAdapter
        extends AbstractCollectionParcelableAdapter<LinkedHashSet> {

    public static final Creator<LinkedHashSetParcelableAdapter> CREATOR =
            new Creator<LinkedHashSetParcelableAdapter>() {

                @Override
                protected LinkedHashSetParcelableAdapter newParcelableAdapterInstance(
                        List<Object> items) {
                    return new LinkedHashSetParcelableAdapter(new LinkedHashSet<>(items));
                }

                @Override
                public LinkedHashSetParcelableAdapter[] newArray(int size) {
                    return new LinkedHashSetParcelableAdapter[size];
                }
            };

    public LinkedHashSetParcelableAdapter(LinkedHashSet value) {
        super(value);
    }
}
