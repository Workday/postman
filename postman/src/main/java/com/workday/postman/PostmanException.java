/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman;

import com.workday.postman.parceler.Parceler;

/**
 * An exception thrown if there is a problem accessing {@link Parceler}s from {@link Postman}.
 *
 * @author nathan.taylor
 * @since 2013-10-8
 */
public class PostmanException
        extends RuntimeException {

    private static final long serialVersionUID = -6754998129606805110L;

    public PostmanException(String message) {
        super(message);
    }

    public PostmanException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostmanException(Throwable cause) {
        super(cause);
    }
}
