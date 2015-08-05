/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.workday.meta.ConcreteTypeNames;
import com.workday.postman.codegen.Names;
import com.workday.postman.annotations.Parceled;
import com.workday.postman.parceler.Parceler;

import java.util.HashMap;
import java.util.Map;

/**
 * Entry point for classes wishing to use the Postman framework to handle the implementation details of {@link
 * Parcelable}. This service can handle Parcelable implementation details for all classes either annotated with or
 * containing fields annotated with {@literal @}{@link Parceled}.
 *
 * @author nathan.taylor
 * @since 2013-9-25-15:02
 */
public class Postman {

    private static Map<Class<?>, Parceler<?>> parcelerMap = new HashMap<>();
    private static Map<Class<?>, Parcelable.Creator<?>> creatorMap = new HashMap<>();

    /**
     * Write the specified Object to a {@link Parcel}.
     *
     * @param object The Object to be written to the Parcel.
     * @param parcel The destination Parcel.
     * @param <T>    The type of the Object.
     * @throws PostmanException if there is no {@link Parceler} associated with the given type.
     */
    public static <T> void writeToParcel(T object, Parcel parcel)
            throws PostmanException {
        @SuppressWarnings("unchecked")
        Parceler<T> parceler = getParcelerForClass((Class<T>) object.getClass());
        parceler.writeToParcel(object, parcel);
    }

    /**
     * Reads an object of the specified class from the given Parcel.
     *
     * @param clazz  The class of the object to be read.
     * @param parcel The source Parcel.
     * @param <T>    The type of the Object.
     * @return A new instance of the specified type instantiated from the Parcel.
     * @throws PostmanException if there is no {@link Parceler} associated with the given type.
     */
    public static <T> T readFromParcel(Class<T> clazz, Parcel parcel)
            throws PostmanException {
        Parceler<T> parceler = getParcelerForClass(clazz);
        return parceler.readFromParcel(parcel);
    }

    /**
     * Creates of new array of the specified type with the specified size.
     *
     * @param clazz The class associated with the type of array to create.
     * @param size  The desired size of the array.
     * @param <T>   The type of the array to create.
     * @return A new array of the specified type with the specified size.
     * @throws PostmanException if there is no {@link Parceler} associated with the given type.
     * @see Creator#newArray(int)
     */
    public static <T> T[] newArray(Class<T> clazz, int size)
            throws PostmanException {
        Parceler<T> parceler = getParcelerForClass(clazz);
        return parceler.newArray(size);
    }

    /**
     * Get a {@link Creator} that can be used for the {@code CREATOR} field of a {@link Parcelable}.
     *
     * @param clazz The class associated with the type the the Creator will create.
     * @param <T>   The type the Creator will create.
     * @return A fully implemented Creator for the specified type.
     * @throws PostmanException if there is no {@link Parceler} associated with the given type.
     */
    public static <T> Parcelable.Creator<T> getCreator(Class<T> clazz)
            throws PostmanException {
        @SuppressWarnings("unchecked")
        Parcelable.Creator<T> creator = (Parcelable.Creator<T>) creatorMap.get(clazz);
        if (creator == null) {
            creator = newCreator(clazz);
            creatorMap.put(clazz, creator);
        }
        return creator;
    }

    private static <T> Parcelable.Creator<T> newCreator(final Class<T> clazz) {
        return new Parcelable.Creator<T>() {
            @Override
            public T createFromParcel(Parcel source) {
                return readFromParcel(clazz, source);
            }

            @Override
            public T[] newArray(int size) {
                return Postman.newArray(clazz, size);
            }
        };
    }

    private static <T> Parceler<T> getParcelerForClass(Class<T> clazz) {
        @SuppressWarnings("unchecked")
        Parceler<T> parceler = (com.workday.postman.parceler.Parceler<T>) parcelerMap.get(clazz);
        if (parceler == null) {
            String name = ConcreteTypeNames.constructClassName(clazz, Names.PARCELER_SUFFIX);
            try {
                @SuppressWarnings("unchecked")
                Parceler<T> newParceler = (Parceler<T>) Class.forName(name).newInstance();
                parceler = newParceler;
            }
            catch (InstantiationException | IllegalAccessException e) {
                String message = "Postman experienced an internal error. Please report this issue.";
                throw new PostmanException(message, e);
            }
            catch (ClassNotFoundException e) {
                String message = String.format(
                        "No %s was found for class %s. Check that (1) you annotated the class with @%s, " +
                        "(2) the Postman processor ran, and (3) ProGuard did not remove the Parcelers.",
                        Parceler.class.getSimpleName(), clazz.getCanonicalName(),
                        Parceled.class.getSimpleName());
                throw new PostmanException(message, e);
            }
            parcelerMap.put(clazz, parceler);
        }
        return parceler;
    }

}
