package com.workday.postman.util;

import java.util.ArrayList;

/**
 * @author kenneth.nickles
 * @since 2016-03-27.
 */
public class EnumUtils {

    private EnumUtils() {
    }

    public static <E> ArrayList<Integer> enumArrayListToOrdinalArrayList(ArrayList<E>
                                                                                 enumArrayList) {
        Preconditions.checkArgument(!enumArrayList.isEmpty(), "Enum list cannot be empty");
        final ArrayList<Integer> ordinalArrayList = new ArrayList<>(enumArrayList.size());
        for (int i = 0; i < enumArrayList.size(); i++) {
            ordinalArrayList.add(((Enum) enumArrayList.get(i)).ordinal());
        }
        return ordinalArrayList;
    }

    public static <E> ArrayList<E> ordinalArrayListToEnumArrayList(Class<E> enumClass,
                                                                   ArrayList<Integer>
                                                                           ordinalArrayList) {
        Preconditions.checkArgument(!ordinalArrayList.isEmpty(), "Ordinal list cannot be empty");
        final ArrayList<E> enumArrayList = new ArrayList<>(ordinalArrayList.size());
        for (int i = 0; i < ordinalArrayList.size(); i++) {
            enumArrayList.add(getEnumForOrdinal(enumClass, ordinalArrayList.get(i)));
        }
        return enumArrayList;
    }

    private static <E> E getEnumForOrdinal(Class<E> enumClass, int ordinal) {
        return enumClass.getEnumConstants()[ordinal];
    }
}
