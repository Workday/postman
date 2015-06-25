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
 * @since 2014-12-30
 */
class CharSequenceSaveStatementWriter implements SaveStatementWriter {

    private final MetaTypes metaTypes;

    CharSequenceSaveStatementWriter(MetaTypes metaTypes) {
        this.metaTypes = metaTypes;
    }

    @Override
    public boolean isApplicable(VariableElement field) {
        return metaTypes.isCharSequecne(field.asType());
    }

    @Override
    public void writeFieldReadStatement(VariableElement field, Collection<ExecutableElement> postCreateChildMethods,
                                        JavaWriter writer) throws IOException {
        writer.emitStatement("object.%s = bundle.getCharSequence(\"%s\")", field.getSimpleName(),
                             field.getSimpleName());
    }

    @Override
    public void writeFieldWriteStatement(VariableElement field, JavaWriter writer) throws IOException {
        writer.emitStatement("bundle.putCharSequence(\"%s\", object.%s)", field.getSimpleName(), field.getSimpleName());
    }
}
