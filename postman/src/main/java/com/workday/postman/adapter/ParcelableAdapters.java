/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.adapter;

import android.os.Parcelable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Locale;

/**
 * @author nathan.taylor
 * @since 2015-08-27.
 */
public class ParcelableAdapters {

    private ParcelableAdapters() {
    }

    public static void toParcelableCollection(Collection<?> source, Collection<Parcelable> target) {
        for (Object o : source) {
            target.add(asParcelable(o));
        }
    }

    public static Object unwrapParcelable(Parcelable p) {
        if (p instanceof ParcelableAdapter) {
            return ((ParcelableAdapter) p).getValue();
        }
        return p;
    }

    public static Parcelable asParcelable(Object o) {

        if (o == null) {
            return null;
        }
        if (o instanceof Parcelable) {
            return (Parcelable) o;
        }

        if (o instanceof BigDecimal) {
            return new BigDecimalParcelableAdapter((BigDecimal) o);
        }
        if (o instanceof BigInteger) {
            return new BigIntegerParcelableAdapter((BigInteger) o);
        }
        if (o instanceof Boolean) {
            return new BooleanParcelableAdapter((Boolean) o);
        }
        if (o instanceof Byte) {
            return new ByteParcelableAdapter((Byte) o);
        }
        if (o instanceof Character) {
            return new CharParcelableAdapter((Character) o);
        }
        if (o instanceof Double) {
            return new DoubleParcelableAdapter((Double) o);
        }
        if (o instanceof Float) {
            return new FloatParcelableAdapter((Float) o);
        }
        if (o instanceof Integer) {
            return new IntParcelableAdapter((Integer) o);
        }
        if (o instanceof Long) {
            return new LongParcelableAdapter((Long) o);
        }
        if (o instanceof Short) {
            return new ShortParcelableAdapter((Short) o);
        }

        if (o instanceof String) {
            return new StringParcelableAdapter((String) o);
        }
        if (o instanceof CharSequence) {
            return new CharSequenceParcelableAdapter((CharSequence) o);
        }

        final String message = String.format(Locale.US,
                                             "Could not convert class of type %s to Parcelable",
                                             o.getClass());
        throw new IllegalArgumentException(message);
    }
}
