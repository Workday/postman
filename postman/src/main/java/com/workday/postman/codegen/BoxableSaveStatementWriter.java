/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.codegen;

import com.squareup.javawriter.JavaWriter;
import com.workday.meta.MetaTypes;

import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.Collection;

/**
 * @author nathan.taylor
 * @since 2013-12-30
 */
class BoxableSaveStatementWriter
        implements SaveStatementWriter {

    private final MetaTypes metaTypes;

    public BoxableSaveStatementWriter(MetaTypes metaTypes) {
        this.metaTypes = metaTypes;
    }

    @Override
    public boolean isApplicable(VariableElement field) {
        return metaTypes.isBoxable(field.asType());
    }

    @Override
    public void writeFieldReadStatement(VariableElement field, Collection<ExecutableElement> postCreateChildMethods,
                                        JavaWriter writer)
            throws IOException {
        writer.emitStatement("object.%s = bundle.get%s(\"%s\")", field.getSimpleName(), getSaveType(field.asType()),
                             field.getSimpleName());
    }

    @Override
    public void writeFieldWriteStatement(VariableElement field, JavaWriter writer)
            throws IOException {
        writer.emitStatement("bundle.put%s(\"%s\", object.%s)", getSaveType(field.asType()), field.getSimpleName(),
                             field.getSimpleName());
    }

    private String getSaveType(TypeMirror type) {
        return StringUtils.capitalize(metaTypes.asPrimitive(type).toString());
    }
}
