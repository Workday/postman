/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.codegen;

import com.google.common.collect.Sets;
import com.workday.postman.annotations.NotParceled;
import com.workday.postman.annotations.Parceled;
import com.workday.postman.annotations.PostCreateChild;
import com.workday.postman.parceler.Parceler;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * The {@link Processor} that handles {@literal @}{@link Parceled} and {@literal @}{@link NotParceled} annotations and
 * generates implemenations of {@link Parceler}.
 *
 * @author nathan.taylor
 * @since 2013-9-25-14:58
 */
public class PostmanProcessor extends AbstractProcessor {

    private Set<TypeElement> handledElements = new HashSet<TypeElement>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if (annotations == null || annotations.isEmpty()) {
            return false;
        }

        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(Parceled.class);
        for (Element e : annotatedElements) {
            ElementKind kind = e.getKind();
            if (kind == ElementKind.FIELD) {
                TypeElement parent = (TypeElement) e.getEnclosingElement();
                handledElements.add(parent);
            } else if (kind == ElementKind.CLASS) {
                handledElements.add((TypeElement) e);
            }
        }

        for (TypeElement handledElement : handledElements) {
            ParcelerGenerator generator = new ParcelerGenerator(processingEnv, handledElement);
            try {
                generator.generateParceler();
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage(), handledElement);
            }
        }

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Sets.newHashSet(Parceled.class.getCanonicalName(), NotParceled.class.getCanonicalName(),
                               PostCreateChild.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
