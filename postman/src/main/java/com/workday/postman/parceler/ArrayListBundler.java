/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.parceler;

import android.os.Bundle;
import android.os.Parcelable;

import com.workday.postman.adapter.ParcelableAdapters;

import java.util.ArrayList;

/**
 * @author nathan.taylor
 * @since 2015-04-06
 */
class ArrayListBundler {

    private ArrayListBundler() {
    }

    private static final InnerListBundler<Integer> INTEGER_LIST_BUNDLER
            = new InnerListBundler<Integer>() {

        @Override
        public void writeToBundle(ArrayList<Integer> list, Bundle bundle, String key) {
            bundle.putIntegerArrayList(key, list);
        }

        @Override
        public ArrayList<Integer> readFromBundle(Bundle bundle, String key) {
            return bundle.getIntegerArrayList(key);
        }
    };

    private static final InnerListBundler<String> STRING_LIST_BUNDLER
            = new InnerListBundler<String>() {

        @Override
        public void writeToBundle(ArrayList<String> list, Bundle bundle, String key) {
            bundle.putStringArrayList(key, list);
        }

        @Override
        public ArrayList<String> readFromBundle(Bundle bundle, String key) {
            return bundle.getStringArrayList(key);
        }
    };

    private static final InnerListBundler<CharSequence> CHAR_SEQUENCE_LIST_BUNDLER
            = new InnerListBundler<CharSequence>() {

        @Override
        public void writeToBundle(ArrayList<CharSequence> list, Bundle bundle, String key) {
            bundle.putCharSequenceArrayList(key, list);
        }

        @Override
        public ArrayList<CharSequence> readFromBundle(Bundle bundle, String key) {
            return bundle.getCharSequenceArrayList(key);
        }
    };

    private static final InnerListBundler<Parcelable> PARCELABLE_LIST_BUNDLER
            = new InnerListBundler<Parcelable>() {

        @Override
        public void writeToBundle(ArrayList<Parcelable> list, Bundle bundle, String key) {
            bundle.putParcelableArrayList(key, list);
        }

        @Override
        public ArrayList<Parcelable> readFromBundle(Bundle bundle, String key) {
            return bundle.getParcelableArrayList(key);
        }
    };

    private static final InnerListBundler<Object> FALLBACK_LIST_BUNDLER =
            new InnerListBundler<Object>() {

                @Override
                public void writeToBundle(ArrayList<Object> list, Bundle bundle, String key) {
                    ArrayList<Parcelable> wrapped = new ArrayList<>(list.size());
                    ParcelableAdapters.toParcelableCollection(list, wrapped);
                    bundle.putParcelableArrayList(key, wrapped);
                }

                @Override
                public ArrayList<Object> readFromBundle(Bundle bundle, String key) {
                    ArrayList<Parcelable> wrapped = bundle.getParcelableArrayList(key);
                    if (wrapped == null) {
                        return null;
                    }

                    ArrayList<Object> unwrapped = new ArrayList<>(wrapped.size());
                    for (Parcelable parcelable : wrapped) {
                        unwrapped.add(ParcelableAdapters.unwrapParcelable(parcelable));
                    }
                    return unwrapped;
                }
            };

    public static <T> void writeArrayListToBundle(ArrayList<T> list, Bundle bundle,
                                                  Class<T> itemClass, String key) {
        getListBundlerForItemClass(itemClass).writeToBundle(list, bundle, key);
    }

    public static <T> ArrayList<T> readArrayListFromBundle(Bundle bundle, Class<T> itemClass,
                                                           String key) {
        return getListBundlerForItemClass(itemClass).readFromBundle(bundle, key);
    }

    @SuppressWarnings("unchecked")
    private static <T> InnerListBundler<T> getListBundlerForItemClass(Class<T> clazz) {
        InnerListBundler<T> innerListBundler;
        if (Integer.class.isAssignableFrom(clazz)) {
            innerListBundler = (InnerListBundler<T>) INTEGER_LIST_BUNDLER;
        } else if (String.class.isAssignableFrom(clazz)) {
            innerListBundler = (InnerListBundler<T>) STRING_LIST_BUNDLER;
        } else if (CharSequence.class.isAssignableFrom(clazz)) {
            innerListBundler = (InnerListBundler<T>) CHAR_SEQUENCE_LIST_BUNDLER;
        } else if (Parcelable.class.isAssignableFrom(clazz)) {
            innerListBundler = (InnerListBundler<T>) PARCELABLE_LIST_BUNDLER;
        } else {
            // This is safe because fallback can handle any type, and the arraylist returned will
            // contain the same type of objects as the one given
            innerListBundler = (InnerListBundler<T>) FALLBACK_LIST_BUNDLER;
        }

        return innerListBundler;
    }

    /**
     * @author nathan.taylor
     * @since 2015-04-06
     */
    private interface InnerListBundler<T> {

        void writeToBundle(ArrayList<T> list, Bundle bundle, String key);

        ArrayList<T> readFromBundle(Bundle bundle, String key);
    }

}
