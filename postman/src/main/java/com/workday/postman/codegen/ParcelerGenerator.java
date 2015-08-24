/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.codegen;

import com.squareup.javawriter.JavaWriter;
import com.workday.meta.MetaTypeNames;
import com.workday.meta.MetaTypes;
import com.workday.meta.Modifiers;
import com.workday.postman.parceler.Parceler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * @author nathan.taylor
 * @since 2013-9-25-17:51
 */
class ParcelerGenerator {

    final ProcessingEnvironment processingEnv;
    private final Collection<VariableElement> parceledFields;
    private final TypeElement elementToParcel;
    private final String parcelerName;
    final MetaTypes metaTypes;
    private final Collection<ExecutableElement> postCreateChildMethods;
    String elementCompressedName;
    final String elementQualifiedName;
    private final List<SaveStatementWriter> saveStatementWriters;
    private static final SaveStatementWriter
            DO_NOTHING_SAVE_STATEMENT_WRITER = DoNothingSaveStatementWriter.INSTANCE;

    public ParcelerGenerator(ProcessingEnvironment processingEnv, TypeElement elementToParcel) {
        this.processingEnv = processingEnv;
        metaTypes = new MetaTypes(processingEnv);

        this.elementToParcel = elementToParcel;
        elementQualifiedName = elementToParcel.getQualifiedName().toString();
        parcelerName = MetaTypeNames.constructTypeName(elementToParcel, Names.PARCELER_SUFFIX);
        ParceledElementExtractor parceledElementExtractor =
                new ParceledElementExtractor(processingEnv);
        this.parceledFields = parceledElementExtractor.extractParceledFields(elementToParcel);
        this.postCreateChildMethods =
                parceledElementExtractor.extractPostCreateChildMethods(elementToParcel);
        saveStatementWriters = createSaveStatementWriterList();
    }

    public void generateParceler() throws IOException {

        JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(parcelerName);
        JavaWriter writer = new JavaWriter(sourceFile.openWriter());

        writer.emitPackage(processingEnv.getElementUtils().getPackageOf(
                elementToParcel).getQualifiedName().toString());

        writer.emitImports(getImports());
        writer.emitEmptyLine();
        elementCompressedName = writer.compressType(elementToParcel.getQualifiedName().toString());
        writer.beginType(parcelerName, "class", EnumSet.of(Modifier.PUBLIC, Modifier.FINAL), null,
                         JavaWriter.type(Parceler.class, elementCompressedName));
        writer.emitEmptyLine();

        writeWriteToParcelMethod(writer);
        writer.emitEmptyLine();
        writeReadFromParcelMethod(writer);
        writer.emitEmptyLine();
        writeNewArrayMethod(writer);
        writer.emitEmptyLine();

        writer.endType();
        writer.close();
    }

    private Set<String> getImports() {
        Set<String> imports = new HashSet<String>();
        imports.add("android.os.Bundle");
        imports.add("android.os.Parcel");
        imports.add(Parceler.class.getCanonicalName());
        return imports;
    }

    private void writeWriteToParcelMethod(JavaWriter writer) throws IOException {
        List<String> parameters = new ArrayList<String>(4);
        parameters.add(elementCompressedName);
        parameters.add("object");
        parameters.add("Parcel");
        parameters.add("dest");

        writer.emitAnnotation(Override.class);
        writer.beginMethod("void", "writeToParcel", Modifiers.PUBLIC, parameters, null);
        writer.emitStatement("Bundle bundle = new Bundle()");

        for (VariableElement field : parceledFields) {
            getSaveStatementWriter(field).writeFieldWriteStatement(field, writer);
        }

        writer.emitStatement("dest.writeBundle(bundle)");
        writer.endMethod();
    }

    private void writeReadFromParcelMethod(JavaWriter writer) throws IOException {
        writer.emitAnnotation(Override.class);
        writer.beginMethod(elementCompressedName, "readFromParcel", Modifiers.PUBLIC, "Parcel",
                           "parcel");

        writer.emitStatement("%s object = new %s()", elementCompressedName, elementCompressedName);
        writer.emitStatement("Bundle bundle = parcel.readBundle()");
        writer.emitStatement("bundle.setClassLoader(%s.class.getClassLoader())",
                             elementCompressedName);
        for (VariableElement field : parceledFields) {
            getSaveStatementWriter(field).writeFieldReadStatement(field,
                                                                  postCreateChildMethods,
                                                                  writer);
        }
        writer.emitStatement("return object");
        writer.endMethod();
    }

    private void writeNewArrayMethod(JavaWriter writer) throws IOException {
        writer.emitAnnotation(Override.class);
        writer.beginMethod(String.format("%s[]", elementCompressedName),
                           "newArray",
                           Modifiers.PUBLIC,
                           "int",
                           "size");
        writer.emitStatement("return new %s[size]", elementCompressedName);
        writer.endMethod();
    }

    private com.workday.postman.codegen.SaveStatementWriter getSaveStatementWriter(
            VariableElement field) {
        for (com.workday.postman.codegen.SaveStatementWriter saveStatementWriter :
                saveStatementWriters) {
            if (saveStatementWriter.isApplicable(field)) {
                return saveStatementWriter;
            }
        }
        String errorMessage = String.format("%s.%s of type %s cannot be written to a Bundle.",
                                            elementQualifiedName,
                                            field.getSimpleName().toString(),
                                            field.asType().toString());
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, errorMessage, field);
        return DO_NOTHING_SAVE_STATEMENT_WRITER;
    }

    private List<com.workday.postman.codegen.SaveStatementWriter> createSaveStatementWriterList() {
        List<SaveStatementWriter> list = new ArrayList<>();
        list.add(new BoxableSaveStatementWriter(metaTypes));
        list.add(new StringSaveStatementWriter(metaTypes));
        list.add(new CharSequenceSaveStatementWriter(metaTypes));
        list.add(new EnumSaveStatementWriter(metaTypes));
        list.add(new ParcelableSaveStatementWriter(metaTypes));
        list.add(new CollectionSaveStatementWriter(this));
        list.add(new MapSaveStatementWriter(this));
        list.add(new SerializableSaveStatementWriter(metaTypes));
        return Collections.unmodifiableList(list);
    }

}
