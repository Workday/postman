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
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import java.io.IOException;
import java.util.Collection;

/**
 * @author nathan.taylor
 * @since 2013-12-30
 */
class EnumSaveStatementWriter
        implements SaveStatementWriter {

    private final MetaTypes metaTypes;

    EnumSaveStatementWriter(MetaTypes metaTypes) {
        this.metaTypes = metaTypes;
    }

    @Override
    public boolean isApplicable(VariableElement field) {
        return metaTypes.isSubtype(field.asType(), Enum.class);
    }

    @Override
    public void writeFieldReadStatement(VariableElement field, Collection<ExecutableElement> postCreateChildMethods,
                                        JavaWriter writer)
            throws IOException {
        String enumType = field.asType().toString();
        Name fieldName = field.getSimpleName();
        String fieldNameVariable = fieldName + "Name";
        writer.emitStatement("String %s = bundle.getString(\"%s\")", fieldNameVariable, fieldName);
        writer.beginControlFlow(String.format("if (%s != null)", fieldNameVariable));
        writer.emitStatement("object.%s = %s.valueOf(%s)", fieldName, enumType, fieldNameVariable);
        writer.endControlFlow();
    }

    @Override
    public void writeFieldWriteStatement(VariableElement field, JavaWriter writer)
            throws IOException {
        Name fieldName = field.getSimpleName();
        writer.beginControlFlow(String.format("if (object.%s != null)", fieldName));
        writer.emitStatement("bundle.putString(\"%s\", object.%s.name())", fieldName, fieldName);
        writer.endControlFlow();
    }
}
