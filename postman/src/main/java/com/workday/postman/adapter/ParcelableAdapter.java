package com.workday.postman.adapter;

import android.os.Parcelable;

/**
 * Marks a class that wraps a non-{@link Parcelable} class to make it Parcelable.
 *
 * @param <T> The type this adapter wraps
 *
 * @author Nathan Taylor
 * @since 2015-04-26
 */
public interface ParcelableAdapter<T> extends Parcelable {

    /**
     * Get the object wrapped by this ParcelableAdapter.
     */
    T getValue();
}
