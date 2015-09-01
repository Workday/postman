/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * The base class for all {@link ParcelableAdapter}s that wrap Collections. Subclasses need only
 * declare the concrete Collection type they wrap (for parameter {@link T}), declare a matching
 * constructor, and declare a public constant {@code CREATOR} of type {@link
 * com.workday.postman.adapter.AbstractCollectionParcelableAdapter.Creator}. This base class will
 * handle wrapping the Collection and writing it to the Parcel.
 *
 * @param <T> The concrete Collection type this ParcelableAdapter wraps.
 *
 * @author nathan.taylor
 * @since 2015-08-27.
 */
public abstract class AbstractCollectionParcelableAdapter<T extends Collection>
        implements ParcelableAdapter<T> {


    /**
     * The base implementation of {@link android.os.Parcelable.Creator} for Collection
     * ParcelableAdapters. This base class will handle unwrapping the Collection written to the
     * Parcel.
     */
    public abstract static class Creator<C extends AbstractCollectionParcelableAdapter>
            implements Parcelable.Creator<C> {

        @Override
        public final C createFromParcel(Parcel source) {
            final Parcelable[] wrapped =
                    source.readParcelableArray(ParcelableAdapter.class.getClassLoader());
            final List<Object> unwrapped =
                    Arrays.asList(ParcelableAdapters.unwrapParcelableArray(wrapped));
            return newParcelableAdapterInstance(unwrapped);
        }

        /**
         * Create a new instance of your ParcelableAdapter using the provided list of items.
         *
         * @param items The items extracted from the Parcel and unwrapped. You should create a
         * Collection of your desired type from the provided List rather than using the List
         * directly.
         */
        protected abstract C newParcelableAdapterInstance(List<Object> items);
    }


    private final T value;

    public AbstractCollectionParcelableAdapter(T value) {
        this.value = value;
    }

    @Override
    public final int describeContents() {
        return 0;
    }

    @Override
    public final void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelableArray(ParcelableAdapters.toParcelableArray(value), flags);
    }

    @Override
    public final T getValue() {
        return value;
    }
}
