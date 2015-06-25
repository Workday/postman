/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.codegen;

import com.squareup.javawriter.JavaWriter;
import com.workday.meta.MetaTypes;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.io.IOException;
import java.util.Collection;

/**
 * @author nathan.taylor
 * @since 2013-12-30
 */
class ParcelableSaveStatementWriter
        implements SaveStatementWriter {

    private final MetaTypes metaTypes;

    ParcelableSaveStatementWriter(MetaTypes metaTypes) {
        this.metaTypes = metaTypes;
    }

    @Override
    public boolean isApplicable(VariableElement field) {
        return metaTypes.isSubtype(field.asType(), Names.PARCELABLE);
    }

    @Override
    public void writeFieldReadStatement(VariableElement field, Collection<ExecutableElement> postCreateChildMethods,
                                        JavaWriter writer)
            throws IOException {
        writer.emitStatement("object.%s = bundle.getParcelable(\"%s\")", field.getSimpleName(), field.getSimpleName());
        for (ExecutableElement method : postCreateChildMethods) {
            writer.emitStatement("object.%s(object.%s)", method.getSimpleName(), field.getSimpleName());
        }
    }

    @Override
    public void writeFieldWriteStatement(VariableElement field, JavaWriter writer)
            throws IOException {
        writer.emitStatement("bundle.putParcelable(\"%s\", object.%s)", field.getSimpleName(), field.getSimpleName());
    }
}
