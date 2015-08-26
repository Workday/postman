/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.parceler;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author nathan.taylor
 * @since 2015-04-06
 */
public class CollectionBundler {

    private CollectionBundler() {
    }

    public static <T> void writeCollectionToBundle(Collection<T> collection,
                                                   Bundle bundle,
                                                   Class<T> itemClass,
                                                   String key) {

        ArrayList<T> arrayList = collection instanceof ArrayList
                                 ? (ArrayList<T>) collection
                                 : new ArrayList<>(collection);
        ArrayListBundler.writeArrayListToBundle(arrayList, bundle, itemClass, key);
    }

    public static <T> void readCollectionFromBundle(Collection<T> collection,
                                                    Bundle bundle,
                                                    Class<T> itemClass,
                                                    String key) {

        collection.addAll(ArrayListBundler.readArrayListFromBundle(bundle, itemClass, key));
    }
}
