package com.workday.postman.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


/**
 * Some stuff copied from Google's guava library.
 *
 * @author Nathan Taylor
 * @since 2015-08-04
 */
public class CollectionUtils {

    /**
     * The largest power of two that can be represented as an {@code int}.
     *
     * @since 10.0
     */
    public static final int MAX_POWER_OF_TWO = 1 << (Integer.SIZE - 2);

    public static <E> ArrayList<E> newArrayList(E... elements) {
        Preconditions.checkNotNull(elements, "elements");
        ArrayList<E> list = new ArrayList<>(elements.length);
        Collections.addAll(list, elements);
        return list;
    }

    @SafeVarargs
    public static <E> HashSet<E> newHashSet(E... elements) {
        Preconditions.checkNotNull(elements, "elements");
        HashSet<E> set = newHashSetWithExpectedSize(elements.length);
        Collections.addAll(set, elements);
        return set;
    }

    private static <E> HashSet<E> newHashSetWithExpectedSize(int expectedSize) {
        return new HashSet<>(mapCapacity(expectedSize));
    }

    /**
     * Returns a capacity that is sufficient to keep the map from being resized as
     * long as it grows no larger than expectedSize and the load factor is >= its
     * default (0.75).
     */
    static int mapCapacity(int expectedSize) {
        if (expectedSize < 3) {
            Preconditions.checkArgument(expectedSize >= 0,
                    "Size must be nonnegative but was " + expectedSize);
            return expectedSize + 1;
        }
        if (expectedSize < MAX_POWER_OF_TWO) {
            return expectedSize + expectedSize / 3;
        }
        return Integer.MAX_VALUE; // any large value
    }
}
