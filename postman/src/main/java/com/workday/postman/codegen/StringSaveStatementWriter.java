/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.codegen;

import com.squareup.javawriter.JavaWriter;
import com.workday.meta.MetaTypes;

import java.io.IOException;
import java.util.Collection;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

/**
 * @author nathan.taylor
 * @since 2013-12-30
 */
class StringSaveStatementWriter
        implements SaveStatementWriter {

    private final MetaTypes metaTypes;

    StringSaveStatementWriter(MetaTypes metaTypes) {
        this.metaTypes = metaTypes;
    }

    @Override
    public boolean isApplicable(VariableElement field) {
        return metaTypes.isString(field.asType());
    }

    @Override
    public void writeFieldReadStatement(VariableElement field,
                                        Collection<ExecutableElement> postCreateChildMethods,
                                        JavaWriter writer)
            throws IOException {
        writer.emitStatement("object.%s = bundle.getString(\"%s\")",
                             field.getSimpleName(),
                             field.getSimpleName());
    }

    @Override
    public void writeFieldWriteStatement(VariableElement field, JavaWriter writer)
            throws IOException {
        writer.emitStatement("bundle.putString(\"%s\", object.%s)",
                             field.getSimpleName(),
                             field.getSimpleName());
    }
}
