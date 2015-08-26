/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.codegen;

import com.workday.meta.MetaTypes;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

/**
 * @author nathan.taylor
 * @since 2015-04-06
 */
public class CollectionItemTypeValidator {

    private final ProcessingEnvironment processingEnv;
    private final MetaTypes metaTypes;

    public CollectionItemTypeValidator(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        metaTypes = new MetaTypes(processingEnv);
    }

    /**
     * Checks if the parameter type of a {@link Collection} is valid, i.e. an {@link ArrayList} of
     * that item type can be saved to a Bundle.
     *
     * @param typeArgument The type for of the parameter.
     * @param offendingElement The element (usually a field) that is parameterized with {@code
     * typeArgument}.
     * @param errorMessage The message to print if {@code typeArgument} is not valid.
     *
     * @return {@code true} if type is valid, otherwise {@code false}.
     */
    public boolean validateTypeArgument(TypeMirror typeArgument,
                                        Element offendingElement,
                                        String errorMessage) {
        if (!(metaTypes.isString(typeArgument)
                || metaTypes.isCharSequecne(typeArgument)
                || metaTypes.isInt(typeArgument)
                || metaTypes.isSubtype(typeArgument, Names.PARCELABLE))) {

            processingEnv.getMessager()
                         .printMessage(Diagnostic.Kind.ERROR, errorMessage, offendingElement);
            return false;
        }
        return true;
    }
}
