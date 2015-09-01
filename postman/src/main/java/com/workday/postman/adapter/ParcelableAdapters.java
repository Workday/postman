/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.adapter;

import android.os.Parcelable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.TreeSet;

/**
 * A utility class to help with wrapping objects in {@link ParcelableAdapter}s.
 *
 * @author nathan.taylor
 * @since 2015-08-27.
 */
public final class ParcelableAdapters {

    private ParcelableAdapters() {
    }

    /**
     * Wraps each item in {@code source} that is not already Parcelable in a ParcelableAdapter and
     * returns an array of the new objects.
     */
    public static Parcelable[] toParcelableArray(Collection<?> source) {
        final Parcelable[] target = new Parcelable[source.size()];
        final Iterator<?> sourceIterator = source.iterator();
        for (int i = 0; i < source.size(); i++) {
            target[i] = asParcelable(sourceIterator.next());
        }
        return target;
    }

    /**
     * Take an array of Parcelable objects and unwrap any that are ParcelableAdapters.
     */
    public static Object[] unwrapParcelableArray(Parcelable[] wrapped) {
        final Object[] unwrapped = new Object[wrapped.length];
        for (int i = 0; i < wrapped.length; i++) {
            unwrapped[i] = unwrapParcelable(wrapped[i]);
        }
        return unwrapped;
    }

    /**
     * Converts a Collection into a Collection of Parcelables, wrapping each item that is not
     * Parcelable in a ParcelableAdapter.
     *
     * @param source The original Collection.
     * @param target The Collection in which to store the Parcelable objects.
     */
    public static void toParcelableCollection(Collection<?> source, Collection<Parcelable> target) {
        for (Object o : source) {
            target.add(asParcelable(o));
        }
    }

    /**
     * If the given Parcelable is a ParcelableAdapter, return the original, unwrapped value.
     * Otherwise, return the Parcelable object.
     */
    public static Object unwrapParcelable(Parcelable p) {
        if (p instanceof ParcelableAdapter) {
            return ((ParcelableAdapter) p).getValue();
        }
        return p;
    }

    /**
     * If the provided object is not Parcelable, wrap it in a ParcelableAdapter.
     *
     * @return The original object if is Parcelable, otherwise the ParcelableAdapter that wraps the
     * object.
     */
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

        if (o instanceof ArrayList) {
            return new ArrayListParcelableAdapter((ArrayList) o);
        }
        if (o instanceof LinkedList) {
            return new LinkedListParcelableAdapter((LinkedList) o);
        }
        if (o instanceof LinkedHashSet) {
            return new LinkedHashSetParcelableAdapter((LinkedHashSet) o);
        }
        if (o instanceof HashSet) {
            return new HashSetParcelableAdapter((HashSet) o);
        }
        if (o instanceof TreeSet) {
            return new TreeSetParcelableAdapter((TreeSet) o);
        }

        if (o instanceof LinkedHashMap) {
            return new LinkedHashMapParcelableAdapter((LinkedHashMap) o);
        }
        if (o instanceof HashMap) {
            return new HashMapParcelableAdapter((HashMap) o);
        }

        if (o instanceof Serializable) {
            return new SerializableParcelableAdapter((Serializable) o);
        }

        final String message = String.format(Locale.US,
                                             "Could not convert class of type %s to Parcelable",
                                             o.getClass());
        throw new IllegalArgumentException(message);
    }
}
