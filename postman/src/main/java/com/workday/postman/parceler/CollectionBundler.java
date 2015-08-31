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

    @SuppressWarnings("unchecked")
    public static void writeCollectionToBundle(Collection collection,
                                                   Bundle bundle,
                                                   Class itemClass,
                                                   String key) {

        ArrayList arrayList = collection instanceof ArrayList
                                 ? (ArrayList) collection
                                 : new ArrayList(collection);
        ArrayListBundler.writeArrayListToBundle(arrayList, bundle, itemClass, key);
    }

    @SuppressWarnings("unchecked")
    public static void readCollectionFromBundle(Collection collection,
                                                    Bundle bundle,
                                                    Class itemClass,
                                                    String key) {

        collection.addAll(ArrayListBundler.readArrayListFromBundle(bundle, itemClass, key));
    }
}
