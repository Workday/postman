/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.annotations;

import android.os.Parcelable;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the target method should be called whenever a {@link Parcelable} or {@link Serializable} object is
 * read from the parcel and assigned to one of this object's fields. The method must be non-private, non-static, and
 * must take a single argument of type {@link Object}. Objects contained directly inside of lists or maps (keys or
 * values in the case of maps) that are fields of this object will also be passed to the target method. The method
 * should handle a null argument gracefully.
 * <p>
 * This can be useful if this object needs to recreate pointers back from the child to itself.
 *
 * @author nathan.taylor
 * @since 2014-12-02
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface PostCreateChild {

}
