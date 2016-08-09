/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.codegen;

import com.workday.postman.annotations.NotParceled;
import com.workday.postman.annotations.Parceled;
import com.workday.postman.annotations.PostCreateChild;
import com.workday.postman.parceler.Parceler;
import com.workday.postman.util.CollectionUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * The {@link Processor} that handles {@literal @}{@link Parceled} and {@literal @}{@link
 * NotParceled} annotations and generates implemenations of {@link Parceler}.
 *
 * @author nathan.taylor
 * @since 2013-9-25-14:58
 */
public class PostmanProcessor extends AbstractProcessor {


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if (annotations == null || annotations.isEmpty()) {
            return false;
        }

        final Set<TypeElement> handledElements = new HashSet<>();

        Set<? extends Element> annotatedElements =
                roundEnv.getElementsAnnotatedWith(Parceled.class);
        for (Element e : annotatedElements) {
            ElementKind kind = e.getKind();
            if (kind == ElementKind.FIELD) {
                TypeElement parent = (TypeElement) e.getEnclosingElement();
                handledElements.add(parent);
            } else if (kind == ElementKind.CLASS) {
                handledElements.add((TypeElement) e);
            }
        }

        checkIfParentsParceled(roundEnv, handledElements, NotParceled.class);
        checkIfParentsParceled(roundEnv, handledElements, PostCreateChild.class);

        for (TypeElement handledElement : handledElements) {
            ParcelerGenerator generator = new ParcelerGenerator(processingEnv, handledElement);
            try {
                generator.generateParceler();
            } catch (IOException e) {
                processingEnv.getMessager()
                             .printMessage(Diagnostic.Kind.ERROR, e.getMessage(), handledElement);
            }
        }

        return true;
    }

    private void checkIfParentsParceled(RoundEnvironment roundEnv,
                                        Set<TypeElement> handledElements,
                                        Class<? extends Annotation> annotationType) {
        Set<? extends Element> annotatedElements =
                roundEnv.getElementsAnnotatedWith(annotationType);
        for (Element e : annotatedElements) {
            TypeElement parent = (TypeElement) e.getEnclosingElement();
            if (!handledElements.contains(parent)) {
                final String message = String.format(Locale.US,
                                                     "You marked an element with @%s in a class "
                                                             + "that has no @%s annotations. The "
                                                             + "enclosing class will not be "
                                                             + "parceled.",
                                                     annotationType.getSimpleName(),
                                                     Parceled.class.getSimpleName());
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, e);
            }
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return CollectionUtils.newHashSet(Parceled.class.getCanonicalName(),
                                          NotParceled.class.getCanonicalName(),
                                          PostCreateChild.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
