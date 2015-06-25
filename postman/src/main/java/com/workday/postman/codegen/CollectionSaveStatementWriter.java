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
import com.workday.postman.parceler.CollectionBundler;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Collection;

/**
 * @author nathan.taylor
 * @since 2015-04-06
 */
class CollectionSaveStatementWriter implements SaveStatementWriter {

    private final MetaTypes metaTypes;
    private final ProcessingEnvironment processingEnv;
    private final Initializers initializers;
    private final CollectionItemTypeValidator itemTypeValidator;

    CollectionSaveStatementWriter(ParcelerGenerator parcelerGenerator) {
        this.metaTypes = parcelerGenerator.metaTypes;
        this.processingEnv = parcelerGenerator.processingEnv;

        initializers = new Initializers(metaTypes);
        itemTypeValidator = new CollectionItemTypeValidator(processingEnv);
    }

    @Override
    public boolean isApplicable(VariableElement field) {
        return metaTypes.isSubtypeErasure(field.asType(), Collection.class);
    }

    @Override
    public void writeFieldReadStatement(VariableElement field, Collection<ExecutableElement> postCreateChildMethods,
                                        JavaWriter writer) throws IOException {
        DeclaredType type = (DeclaredType) field.asType();
        TypeMirror itemType = type.getTypeArguments().get(0);
        validateTypeArugment(itemType, field);

        String collectionInitializer;
        try {
            collectionInitializer = initializers.findCollectionInitializer(type);
        } catch (InvalidTypeException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage(), field);
            collectionInitializer = "null";
        }
        writer.beginControlFlow("if (bundle.containsKey(\"%s\"))", field.getSimpleName());
        writer.emitStatement("object.%s = %s", field.getSimpleName(), collectionInitializer);
        writer.emitStatement(CollectionBundler.class.getCanonicalName()
                                     + ".readCollectionFromBundle(object.%1$s, bundle, %2$s.class, \"%1$s\")",
                             field.getSimpleName(), itemType);

        writePostCreateChildMethodCalls(field, itemType, postCreateChildMethods, writer);
        writer.endControlFlow();
    }

    @Override
    public void writeFieldWriteStatement(VariableElement field, JavaWriter writer) throws IOException {
        DeclaredType type = (DeclaredType) field.asType();
        TypeMirror itemType = type.getTypeArguments().get(0);
        validateTypeArugment(itemType, field);

        writer.beginControlFlow("if (object.%s != null)", field.getSimpleName());
        writer.emitStatement(CollectionBundler.class.getCanonicalName()
                                     + ".writeCollectionToBundle(object.%1$s, bundle, %2$s.class, \"%1$s\")",
                             field.getSimpleName(), itemType);
        writer.endControlFlow();
    }

    private void writePostCreateChildMethodCalls(VariableElement field, TypeMirror itemType,
                                                 Collection<ExecutableElement> postCreateChildMethods,
                                                 JavaWriter writer) throws IOException {

        if (!postCreateChildMethods.isEmpty() && metaTypes.isSubtype(itemType, Names.PARCELABLE)) {
            writer.beginControlFlow("for (Object child : object.%s)", field.getSimpleName());
            for (ExecutableElement method : postCreateChildMethods) {
                writer.emitStatement("object.%s(child)", method.getSimpleName());
            }
            writer.endControlFlow();
        }
    }

    private void validateTypeArugment(TypeMirror typeArgument, Element offendingElement) {
        itemTypeValidator.validateTypeArugment(typeArgument, offendingElement,
                                               "Postman cannot handle Collections containing items of type "
                                                       + typeArgument);
    }
}
