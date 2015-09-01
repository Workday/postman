/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.adapter;

import java.util.List;
import java.util.TreeSet;

/**
 * @author nathan.taylor
 * @since 2015-08-27.
 */
public class TreeSetParcelableAdapter extends AbstractCollectionParcelableAdapter<TreeSet> {

    public static final Creator<TreeSetParcelableAdapter> CREATOR =
            new Creator<TreeSetParcelableAdapter>() {

                @Override
                protected TreeSetParcelableAdapter newParcelableAdapterInstance(List<Object>
                                                                                        items) {
                    return new TreeSetParcelableAdapter(new TreeSet<>(items));
                }

                @Override
                public TreeSetParcelableAdapter[] newArray(int size) {
                    return new TreeSetParcelableAdapter[size];
                }
            };

    public TreeSetParcelableAdapter(TreeSet value) {
        super(value);
    }
}
