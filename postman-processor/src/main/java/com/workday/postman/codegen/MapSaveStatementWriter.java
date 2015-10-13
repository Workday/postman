/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.codegen;

import com.squareup.javawriter.JavaWriter;
import com.workday.meta.Initializers;
import com.workday.meta.InvalidTypeException;
import com.workday.meta.MetaTypes;
import com.workday.postman.parceler.MapBundler;
import com.workday.postman.util.Names;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

/**
 * @author nathan.taylor
 * @since 2014-10-29.
 */
class MapSaveStatementWriter implements SaveStatementWriter {

    private final MetaTypes metaTypes;
    private final ProcessingEnvironment processingEnv;
    private final Initializers initializers;

    MapSaveStatementWriter(ParcelerGenerator parcelerGenerator) {
        this.metaTypes = parcelerGenerator.metaTypes;
        this.processingEnv = parcelerGenerator.processingEnv;

        initializers = new Initializers(metaTypes);
    }

    @Override
    public boolean isApplicable(VariableElement field) {
        return metaTypes.isSubtypeErasure(field.asType(), Map.class);
    }

    @Override
    public void writeFieldReadStatement(VariableElement field,
                                        Collection<ExecutableElement> postCreateChildMethods,
                                        JavaWriter writer) throws IOException {
        DeclaredType type = (DeclaredType) field.asType();
        List<? extends TypeMirror> typeArguments = type.getTypeArguments();
        TypeMirror keyType = typeArguments.get(0);
        TypeMirror valueType = typeArguments.get(1);

        try {
            writer.emitStatement("object.%s = %s", field.getSimpleName(),
                                 initializers.findMapInitializer(type));
        } catch (InvalidTypeException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage(), field);
        }
        writer.emitStatement(
                "%1$s.readMapFromBundle(object.%2$s, bundle, %3$s.class, %4$s.class, \"%2$s\")",
                MapBundler.class.getCanonicalName(),
                field.getSimpleName(),
                keyType,
                valueType);

        if (!postCreateChildMethods.isEmpty()
                && (metaTypes.isSubtype(keyType, Names.PARCELABLE)
                || metaTypes.isSubtype(valueType, Names.PARCELABLE))) {

            writer.beginControlFlow(
                    "for (java.util.Map.Entry<%s, %s> entry : object.%s.entrySet())",
                    keyType.toString(),
                    valueType.toString(),
                    field.getSimpleName());

            if (metaTypes.isSubtype(valueType, Names.PARCELABLE)) {
                for (ExecutableElement method : postCreateChildMethods) {
                    writer.emitStatement("object.%s(entry.getKey())", method.getSimpleName());
                }
            }

            if (metaTypes.isSubtype(valueType, Names.PARCELABLE)) {
                for (ExecutableElement method : postCreateChildMethods) {
                    writer.emitStatement("object.%s(entry.getValue())", method.getSimpleName());
                }
            }
            writer.endControlFlow();
        }
    }

    @Override
    public void writeFieldWriteStatement(VariableElement field, JavaWriter writer)
            throws IOException {
        DeclaredType type = (DeclaredType) field.asType();
        List<? extends TypeMirror> typeArguments = type.getTypeArguments();
        TypeMirror keyType = typeArguments.get(0);
        TypeMirror valueType = typeArguments.get(1);

        writer.beginControlFlow("if (object.%s != null)", field.getSimpleName());
        writer.emitStatement(
                "%1$s.writeMapToBundle(object.%2$s, bundle, %3$s.class, %4$s.class, \"%2$s\")",
                MapBundler.class.getCanonicalName(),
                field.getSimpleName(),
                keyType,
                valueType);
        writer.endControlFlow();
    }

}
