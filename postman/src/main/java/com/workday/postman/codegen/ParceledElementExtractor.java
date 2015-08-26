/*
 * Copyright 2015 Workday, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.codegen;

import com.workday.meta.CodeAnalysisUtils;
import com.workday.meta.MetaTypes;
import com.workday.postman.annotations.NotParceled;
import com.workday.postman.annotations.Parceled;
import com.workday.postman.annotations.PostCreateChild;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;

/**
 * A helper class that runs validation on classes or fields annotated with {@literal@}{@link
 * Parceled} and extracts all valid fields eligible to be included in the Parcel.
 *
 * @author nathan.taylor
 * @since 2013-10-8
 */
class ParceledElementExtractor {

    private final ProcessingEnvironment processingEnv;
    private final Messager messager;
    private final MetaTypes metatypes;

    public ParceledElementExtractor(ProcessingEnvironment processingEnv) {

        this.processingEnv = processingEnv;
        metatypes = new MetaTypes(processingEnv);
        messager = processingEnv.getMessager();
    }

    /**
     * Finds and returns all valid methods annotated with {@literal@}{@link PostCreateChild}.
     *
     * @param classToParcel The element representing the class that will be parceled.
     */
    public Collection<ExecutableElement> extractPostCreateChildMethods(TypeElement classToParcel) {
        Collection<ExecutableElement> allMethods = ElementFilter.methodsIn(
                processingEnv.getElementUtils().getAllMembers(classToParcel));

        Collection<ExecutableElement> postCreateChildMethods = new ArrayList<>();
        for (ExecutableElement method : allMethods) {
            PostCreateChild annotation = method.getAnnotation(PostCreateChild.class);
            if (annotation != null && assertValidPostCreateMethod(method)) {
                postCreateChildMethods.add(method);
            }
        }

        return postCreateChildMethods;
    }

    /**
     * Returns {@code true} if the given method is valid for the {@link PostCreateChild} annotation,
     * and {@code false}. If the method is not valid, this method will generate all the relevant
     * compilation errors.
     */
    private boolean assertValidPostCreateMethod(ExecutableElement method) {
        boolean isValid = true;
        if (CodeAnalysisUtils.isPrivate(method)) {
            isValid = false;
            String message = String.format("Method annotated with @%s cannot be private.",
                                           PostCreateChild.class.getSimpleName());
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, method);
        }

        if (CodeAnalysisUtils.isStatic(method)) {
            isValid = false;
            String message = String.format("Method annotated with @%s cannot be static.",
                                           PostCreateChild.class.getSimpleName());
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, method);
        }

        if (method.getParameters().size() != 1
                || !metatypes.isSameType(method.getParameters().get(0).asType(), Object.class)) {
            isValid = false;
            String message = String.format(Locale.US,
                                           "Method annotated with @%s must take a single argument"
                                                   + " of type Object.",
                                           PostCreateChild.class.getSimpleName());
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, method);
        }

        return isValid;
    }

    /**
     * Extracts all fields that should be included in the parcel. If {@code classToParcel} is
     * annotated with {@literal@}{@link Parceled}, then all fields except those that are final,
     * private, or annotated with {@literal@}{@link NotParceled} will be included. Otherwise, all
     * fields annotated with {@literal@}Parceled are included.
     *
     * @param classToParcel The element representing the class that will be parceled.
     *
     * @return A collection of all fields that should be included in the parcel.
     */
    public Collection<VariableElement> extractParceledFields(TypeElement classToParcel) {
        Collection<VariableElement> allFields = ElementFilter.fieldsIn(
                processingEnv.getElementUtils().getAllMembers(classToParcel));

        if (hasParceledAnnotation(classToParcel)) {
            return extractValidFields(allFields);
        } else {
            return extractAnnotatedFields(allFields);
        }
    }

    private Collection<VariableElement> extractValidFields(Collection<VariableElement> allFields) {
        Collection<VariableElement> validFields = new ArrayList<VariableElement>(allFields.size());
        for (VariableElement field : allFields) {
            boolean shouldAdd = true;

            if (CodeAnalysisUtils.isConstant(field) || hasNotParceledAnnotation(field)) {
                shouldAdd = false;
            } else {
                if (CodeAnalysisUtils.isPrivate(field)) {
                    String message = String.format("Parceler will ignore private field %s.",
                                                   field.getSimpleName());
                    messager.printMessage(Diagnostic.Kind.WARNING, message, field);
                    shouldAdd = false;
                }

                if (CodeAnalysisUtils.isFinal(field)) {
                    String message = String.format("Parceler will ignore final field %s.",
                                                   field.getSimpleName());
                    messager.printMessage(Diagnostic.Kind.WARNING, message, field);
                    shouldAdd = false;
                }

                if (hasParceledAnnotation(field)) {
                    String message = String.format(
                            "@%s annotations are ignored when the enclosing class is annotated "
                                    + "with @%s.",
                            Parceled.class.getSimpleName(), Parceled.class.getSimpleName());
                    messager.printMessage(Diagnostic.Kind.WARNING, message, field);
                }
            }

            if (shouldAdd) {
                validFields.add(field);
            }
        }
        return validFields;
    }

    private Collection<VariableElement> extractAnnotatedFields(Collection<VariableElement>
                                                                       allFields) {
        Collection<VariableElement> validAnnotatedFields =
                new ArrayList<VariableElement>(allFields.size());

        for (VariableElement field : allFields) {
            boolean shouldAdd = hasParceledAnnotation(field);

            if (hasNotParceledAnnotation(field)) {
                String message = String.format(
                        "@%s annotations are ignored when the enclosing class is not annotated "
                                + "with @%s",
                        NotParceled.class.getSimpleName(),
                        Parceled.class.getSimpleName());
                messager.printMessage(Diagnostic.Kind.WARNING, message, field);
            }

            if (hasParceledAnnotation(field) && CodeAnalysisUtils.isPrivate(field)) {
                String message = "Cannot access private fields when parceling.";
                messager.printMessage(Diagnostic.Kind.ERROR, message, field);
                shouldAdd = false;
            }

            if (hasParceledAnnotation(field) && CodeAnalysisUtils.isFinal(field)) {
                String message = "Cannot access final fields when parceling.";
                messager.printMessage(Diagnostic.Kind.ERROR, message, field);
                shouldAdd = false;
            }

            if (shouldAdd) {
                validAnnotatedFields.add(field);
            }
        }
        return validAnnotatedFields;
    }

    private boolean hasNotParceledAnnotation(Element element) {
        return element.getAnnotation(NotParceled.class) != null;
    }

    private boolean hasParceledAnnotation(Element element) {
        return element.getAnnotation(Parceled.class) != null;
    }

}
