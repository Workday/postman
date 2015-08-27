package com.workday.postman.adapter;

import android.os.Parcelable;

/**
 * @author Nathan Taylor
 * @since 2015-04-26
 */
public interface ParcelableAdapter<T> extends Parcelable {

    T getValue();
}
