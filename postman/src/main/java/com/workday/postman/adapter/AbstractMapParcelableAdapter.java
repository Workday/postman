/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;
import java.util.Map;

/**
 * The base class for all {@link ParcelableAdapter}s that wrap Maps. Subclasses need only delcare
 * the concrete Map type they wrap (for parameter {@link T}), declare a matching constructor, and
 * declare a public constant {@code CREATOR} of type {@link com.workday.postman.adapter
 * .AbstractMapParcelableAdapter.Creator}. This base class will handle wrapping the Map and writing
 * it to the Parcel.
 *
 * @param <T> The concrete Map type this ParcelableAdapter wraps.
 *
 * @author nathan.taylor
 * @since 2015-08-28.
 */
public class AbstractMapParcelableAdapter<T extends Map> implements ParcelableAdapter<T> {

    /**
     * The base implementation of {@link android.os.Parcelable.Creator} for Map ParcelableAdapters.
     * This base class will handle unwrapping the Map written to the Parcel.
     *
     * @param <M> The concrete Map type the ParcelableAdapter wraps.
     * @param <A> The concrete ParcelableAdapter this Creator will create.
     */
    public abstract static class Creator<M extends Map, A extends AbstractMapParcelableAdapter<M>>
            implements Parcelable.Creator<A> {

        @Override
        public final A createFromParcel(Parcel source) {
            final Parcelable[] keys =
                    source.readParcelableArray(ParcelableAdapter.class.getClassLoader());
            final Parcelable[] values =
                    source.readParcelableArray(ParcelableAdapter.class.getClassLoader());
            if (keys.length != values.length) {
                final String message = String.format(Locale.US,
                                                     "Length of keys array (%d) does not match "
                                                             + "length of values array (%d)",
                                                     keys.length,
                                                     values.length);
                throw new IllegalStateException(message);
            }

            final M map = newMapInstance();
            @SuppressWarnings("unchecked")
            final Map<Object, Object> castedMap = (Map<Object, Object>) map;
            for (int i = 0; i < keys.length; i++) {
                final Object key = ParcelableAdapters.unwrapParcelable(keys[i]);
                final Object val = ParcelableAdapters.unwrapParcelable(values[i]);
                castedMap.put(key, val);
            }
            return newParcelableAdapterInstance(map);
        }

        /**
         * Create a new instance of the Map type the ParcelableAdapter wraps.
         */
        protected abstract M newMapInstance();

        /**
         * Create a new instance of the ParcelableAdapter from the provided Map.
         *
         * @param map The Map that was extracted from the parcel, with all items unwrapped. This
         * will be the map that was returned in {@link #newMapInstance()}, so there is no need to
         * create a new Map from this one.
         */
        protected abstract A newParcelableAdapterInstance(M map);

    }

    private final T value;

    public AbstractMapParcelableAdapter(T value) {
        this.value = value;
    }

    @Override
    public final int describeContents() {
        return 0;
    }

    @Override
    public final void writeToParcel(Parcel dest, int flags) {
        final Parcelable[] keys = new Parcelable[value.size()];
        final Parcelable[] values = new Parcelable[value.size()];

        int i = 0;
        @SuppressWarnings("unchecked")
        final Map<Object, Object> castedMap = value;
        for (Map.Entry entry : castedMap.entrySet()) {
            keys[i] = ParcelableAdapters.asParcelable(entry.getKey());
            values[i] = ParcelableAdapters.asParcelable(entry.getValue());
        }

        dest.writeParcelableArray(keys, flags);
        dest.writeParcelableArray(values, flags);
    }

    @Override
    public final T getValue() {
        return value;
    }
}
