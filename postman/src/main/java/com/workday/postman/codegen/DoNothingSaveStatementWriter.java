/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.codegen;

import com.squareup.javawriter.JavaWriter;

import java.io.IOException;
import java.util.Collection;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

/**
 * @author nathan.taylor
 * @since 2013-12-30
 */
class DoNothingSaveStatementWriter implements SaveStatementWriter {

    public static final DoNothingSaveStatementWriter INSTANCE = new DoNothingSaveStatementWriter();

    private DoNothingSaveStatementWriter() {
    }

    @Override
    public boolean isApplicable(VariableElement field) {
        return false;
    }

    @Override
    public void writeFieldReadStatement(VariableElement field,
                                        Collection<ExecutableElement> postCreateChildMethods,
                                        JavaWriter writer)
            throws IOException {
        // do nothing

    }

    @Override
    public void writeFieldWriteStatement(VariableElement field, JavaWriter writer)
            throws IOException {
        // do nothing
    }
}
