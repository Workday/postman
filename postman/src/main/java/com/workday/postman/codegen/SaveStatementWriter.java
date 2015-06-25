/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.codegen;

import com.squareup.javawriter.JavaWriter;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.io.IOException;
import java.util.Collection;

/**
 * @author nathan.taylor
 * @since 2013-12-30
 */
interface SaveStatementWriter {

    boolean isApplicable(VariableElement field);

    void writeFieldReadStatement(VariableElement field, Collection<ExecutableElement> postCreateChildMethods,
                                 JavaWriter writer)
            throws IOException;

    void writeFieldWriteStatement(VariableElement field, JavaWriter writer)
            throws IOException;
}
