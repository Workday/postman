/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.annotations;

import android.os.Parcel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When applied to a type, this annotation indicates to the Postman framework that all non-final, non-private fields
 * (except those annotated with {@literal @}{@link NotParceled}) should be included in the {@link Parcel}. When applied
 * to a field, this annotation indicates that the field should be included in the Parcel. Note that the Postman
 * framework cannot access private and final fields.
 *
 * @author nathan.taylor
 * @since 2013-9-25-14:59
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface Parceled {
}
