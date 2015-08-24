/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates to the Postman framework that a field should not be included in the Parcel. This
 * annotation only makes sense if the enclosing class of the field is annotated with
 * {@literal@}{@link Parceled}.
 *
 * @author nathan.taylor
 * @since 2013-10-8
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface NotParceled {
}
