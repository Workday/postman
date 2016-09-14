/*
 * Copyright 2016 Google, Inc.
 *
 * This software is available under the MIT license.
 * Please see the LICENSE.txt file in this project.
 */

package com.workday.postman.codegen;

import com.google.testing.compile.JavaFileObjects;
import com.workday.postman.util.Names;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * @author ndtaylor
 * @since 2016-07-10.
 */
@RunWith(JUnit4.class)
public class PostmanProcessorTest {

    @Test
    public void testEmptyAnnotatedClass() throws Exception {
        //language=JAVA
        final String sourceString = "package test;"
                + "import com.workday.postman.annotations.Parceled;"
                + ""
                + " @Parceled"
                + " public class TestClass { }";
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestClass", sourceString);
        assertAbout(javaSource()).that(source)
                                 .processedWith(new PostmanProcessor())
                                 .compilesWithoutError()
                                 .and()
                                 .generatesFileNamed(StandardLocation.SOURCE_OUTPUT,
                                                     "test",
                                                     getParcelerFileName("TestClass"));
    }

    @Test
    public void testPrivateAnnotatedFieldRaisesCompilationError() throws Exception {
        //language=JAVA
        final String sourceString = "package test;\n"
                + "import com.workday.postman.annotations.Parceled;\n"
                + "\n"
                + " public class TestClass {\n"
                + "   @Parceled\n"
                + "   private String myString;\n"
                + " }";
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestClass", sourceString);
        assertAbout(javaSource()).that(source)
                                 .processedWith(new PostmanProcessor())
                                 .failsToCompile()
                                 .withErrorContaining("Cannot access private fields when "
                                                              + "parceling.")
                                 .in(source)
                                 .onLine(6);
    }

    @Test
    public void testFinalAnnotatedFieldRaisesCompilationError() throws Exception {
        //language=JAVA
        final String sourceString = "package test;\n"
                + "import com.workday.postman.annotations.Parceled;\n"
                + "\n"
                + " public class TestClass {\n"
                + "   @Parceled\n"
                + "   final String myString = \"The final word.\";\n"
                + " }";
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestClass", sourceString);
        assertAbout(javaSource()).that(source)
                                 .processedWith(new PostmanProcessor())
                                 .failsToCompile()
                                 .withErrorContaining("Cannot access final fields when parceling.")
                                 .in(source)
                                 .onLine(6);
    }

    @Test
    public void testStaticAnnotatedFieldRaisesWarning() throws Exception {
        //language=JAVA
        final String sourceString = "package test;\n"
                + "import com.workday.postman.annotations.Parceled;\n"
                + "\n"
                + " public class TestClass {\n"
                + "   @Parceled\n"
                + "   static String myString;\n"
                + " }";
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestClass", sourceString);
        assertAbout(javaSource()).that(source)
                                 .processedWith(new PostmanProcessor())
                                 .compilesWithoutError()
                                 .withWarningContaining(
                                         "You marked static field myString as parceled")
                                 .in(source)
                                 .onLine(6);
    }

    @Test
    public void testStaticFieldInAnnotatedClassRaisesWarning() throws Exception {
        //language=JAVA
        final String sourceString = "package test;\n"
                + "import com.workday.postman.annotations.Parceled;\n"
                + "\n"
                + " @Parceled\n"
                + " public class TestClass {\n"
                + "   static String myString;\n"
                + " }";
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestClass", sourceString);
        assertAbout(javaSource()).that(source)
                                 .processedWith(new PostmanProcessor())
                                 .compilesWithoutError()
                                 .withWarningContaining(
                                         "Static field myString will be parceled, but you "
                                                 + "probably don't want it to be. You can mark "
                                                 + "this field as not parceled with @NotParceled.")
                                 .in(source)
                                 .onLine(6);
    }

    @Test
    public void testNotParceledlAnnotatedFieldRaisesWarning() throws Exception {
        //language=JAVA
        final String sourceString = "package test;\n"
                + "import com.workday.postman.annotations.NotParceled;import com.workday.postman"
                + ".annotations.Parceled;\n"
                + "\n"
                + " public class TestClass {\n"
                + "   @NotParceled\n"
                + "   String myString;\n"
                + "   @Parceled\n"
                + "   String myParceledString;"
                + " }";
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestClass", sourceString);
        assertAbout(javaSource()).that(source)
                                 .processedWith(new PostmanProcessor())
                                 .compilesWithoutError()
                                 .withWarningContaining(
                                         "@NotParceled annotations are ignored when the enclosing "
                                                 + "class is not annotated with @Parceled.")
                                 .in(source)
                                 .onLine(6);
    }

    @Test
    public void testNotParceledlAnnotatedFieldRaisesErrorInNonParceledClass() throws Exception {
        //language=JAVA
        final String sourceString = "package test;\n"
                + "import com.workday.postman.annotations.NotParceled;import com.workday.postman"
                + ".annotations.Parceled;\n"
                + "\n"
                + " public class TestClass {\n"
                + "   @NotParceled\n"
                + "   String myString;\n"
                + " }";
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestClass", sourceString);
        assertAbout(javaSource()).that(source)
                                 .processedWith(new PostmanProcessor())
                                 .failsToCompile()
                                 .withErrorContaining(
                                         "You marked an element with @NotParceled in a class that "
                                                 + "has no @Parceled annotations. The enclosing "
                                                 + "class will not be parceled.")
                                 .in(source)
                                 .onLine(6);
    }

    @Test
    public void testPrivateFieldRaisesWarning() throws Exception {
        //language=JAVA
        final String sourceString = "package test;\n"
                + "import com.workday.postman.annotations.Parceled;\n"
                + "\n"
                + " @Parceled\n"
                + " public class TestClass {\n"
                + "   private String myString;\n"
                + " }";
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestClass", sourceString);
        assertAbout(javaSource()).that(source)
                                 .processedWith(new PostmanProcessor())
                                 .compilesWithoutError()
                                 .withWarningContaining(
                                         "Parceler will ignore private field myString.")
                                 .in(source)
                                 .onLine(6);
    }

    @Test
    public void testFinalFieldRaisesWarning() throws Exception {
        //language=JAVA
        final String sourceString = "package test;\n"
                + "import com.workday.postman.annotations.Parceled;\n"
                + "\n"
                + " @Parceled\n"
                + " public class TestClass {\n"
                + "   final String myString = \"The final word.\";\n"
                + " }";
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestClass", sourceString);
        assertAbout(javaSource()).that(source)
                                 .processedWith(new PostmanProcessor())
                                 .compilesWithoutError()
                                 .withWarningContaining(
                                         "Parceler will ignore final field myString.")
                                 .in(source)
                                 .onLine(6);
    }

    @Test
    public void testParceledAnnotatedFieldRaisesWarningInAnnotatedClass() throws Exception {
        //language=JAVA
        final String sourceString = "package test;\n"
                + "import com.workday.postman.annotations.Parceled;\n"
                + "\n"
                + " @Parceled\n"
                + " public class TestClass {\n"
                + "   @Parceled\n"
                + "   String myString;\n"
                + " }";
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestClass", sourceString);
        assertAbout(javaSource()).that(source)
                                 .processedWith(new PostmanProcessor())
                                 .compilesWithoutError()
                                 .withWarningContaining(
                                         "@Parceled annotations are ignored on fields when the "
                                                 + "enclosing class is annotated with @Parceled.")
                                 .in(source)
                                 .onLine(7);
    }

    @Test
    public void testPrivatePostCreateChildRaisesError() throws Exception {
        //language=JAVA
        final String sourceString = "package test;\n"
                + "import com.workday.postman.annotations.Parceled;\n"
                + "import com.workday.postman.annotations.PostCreateChild;\n"
                + "\n"
                + " @Parceled\n"
                + " public class TestClass {\n"
                + "   @PostCreateChild\n"
                + "   private void onPostCreateChild(Object o) { }\n"
                + " }";
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestClass", sourceString);
        assertAbout(javaSource()).that(source)
                                 .processedWith(new PostmanProcessor())
                                 .failsToCompile()
                                 .withErrorContaining(
                                         "Methods annotated with @PostCreateChild cannot be "
                                                 + "private.")
                                 .in(source)
                                 .onLine(8);
    }

    @Test
    public void testStaticPostCreateChildRaisesError() throws Exception {
        //language=JAVA
        final String sourceString = "package test;\n"
                + "import com.workday.postman.annotations.Parceled;\n"
                + "import com.workday.postman.annotations.PostCreateChild;\n"
                + "\n"
                + " @Parceled\n"
                + " public class TestClass {\n"
                + "   @PostCreateChild\n"
                + "   static void onPostCreateChild(Object o) { }\n"
                + " }";
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestClass", sourceString);
        assertAbout(javaSource()).that(source)
                                 .processedWith(new PostmanProcessor())
                                 .failsToCompile()
                                 .withErrorContaining(
                                         "Methods annotated with @PostCreateChild cannot be "
                                                 + "static.")
                                 .in(source)
                                 .onLine(8);
    }

    @Test
    public void testPostCreateChildWithNoArgumentRaisesError() throws Exception {
        //language=JAVA
        final String sourceString = "package test;\n"
                + "import com.workday.postman.annotations.Parceled;\n"
                + "import com.workday.postman.annotations.PostCreateChild;\n"
                + "\n"
                + " @Parceled\n"
                + " public class TestClass {\n"
                + "   @PostCreateChild\n"
                + "   void onPostCreateChild() { }\n"
                + " }";
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestClass", sourceString);
        assertAbout(javaSource()).that(source)
                                 .processedWith(new PostmanProcessor())
                                 .failsToCompile()
                                 .withErrorContaining(
                                         "Methods annotated with @PostCreateChild must take a "
                                                 + "single argument of type Object.")
                                 .in(source)
                                 .onLine(8);
    }

    @Test
    public void testPostCreateChildWithWrongArgumentTypeRaisesError() throws Exception {
        //language=JAVA
        final String sourceString = "package test;\n"
                + "import com.workday.postman.annotations.Parceled;\n"
                + "import com.workday.postman.annotations.PostCreateChild;\n"
                + "\n"
                + " @Parceled\n"
                + " public class TestClass {\n"
                + "   @PostCreateChild\n"
                + "   void onPostCreateChild(String s) { }\n"
                + " }";
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestClass", sourceString);
        assertAbout(javaSource()).that(source)
                                 .processedWith(new PostmanProcessor())
                                 .failsToCompile()
                                 .withErrorContaining(
                                         "Methods annotated with @PostCreateChild must take a "
                                                 + "single argument of type Object.")
                                 .in(source)
                                 .onLine(8);
    }

    @Test
    public void testPostCreateChildWithTooMayArgumentsRaisesError() throws Exception {
        //language=JAVA
        final String sourceString = "package test;\n"
                + "import com.workday.postman.annotations.Parceled;\n"
                + "import com.workday.postman.annotations.PostCreateChild;\n"
                + "\n"
                + " @Parceled\n"
                + " public class TestClass {\n"
                + "   @PostCreateChild\n"
                + "   void onPostCreateChild(Object o1, Object o2) { }\n"
                + " }";
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestClass", sourceString);
        assertAbout(javaSource()).that(source)
                                 .processedWith(new PostmanProcessor())
                                 .failsToCompile()
                                 .withErrorContaining(
                                         "Methods annotated with @PostCreateChild must take a "
                                                 + "single argument of type Object.")
                                 .in(source)
                                 .onLine(8);
    }

    @Test
    public void testPostCreateInClassWithNoParceledAnnotationsRaisesError() throws Exception {
        //language=JAVA
        final String sourceString = "package test;\n"
                + "import com.workday.postman.annotations.PostCreateChild;\n"
                + "\n"
                + " public class TestClass {\n"
                + "   @PostCreateChild\n"
                + "   void onPostCreateChild(Object o1) { }\n"
                + " }";
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestClass", sourceString);
        assertAbout(javaSource()).that(source)
                                 .processedWith(new PostmanProcessor())
                                 .failsToCompile()
                                 .withErrorContaining(
                                         "You marked an element with @PostCreateChild in a class "
                                                 + "that has no @Parceled annotations. The "
                                                 + "enclosing class will not be parceled.")
                                 .in(source)
                                 .onLine(6);
    }

    @Test
    public void testMultipleErrorsAndWarnings() throws Exception {
        //language=JAVA
        final String sourceString = "package test;\n"
                + "import com.workday.postman.annotations.NotParceled;\n"
                + "import com.workday.postman.annotations.Parceled;\n"
                + "import com.workday.postman.annotations.PostCreateChild;\n"
                + "\n"
                + " public class TestClass {\n"
                + "   @Parceled\n"
                + "   final String myFinal = \"The final word.\";\n"
                + "   @NotParceled\n"
                + "   String myNotParceled;\n"
                + "   @Parceled\n"
                + "   private String myPrivate;\n"
                + "   @Parceled\n"
                + "   static String myStatic;\n"
                + "   @PostCreateChild\n"
                + "   private void onPostCreateChild(Object o) { }\n"
                + " }";
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestClass", sourceString);
        assertAbout(javaSource()).that(source)
                                 .processedWith(new PostmanProcessor())
                                 .failsToCompile()
                                 .withErrorCount(3)
                                 .withErrorContaining("Cannot access final field")
                                 .and()
                                 .withErrorContaining("Cannot access private field")
                                 .and()
                                 .withErrorContaining(
                                         "Methods annotated with @PostCreateChild cannot be "
                                                 + "private")
                                 .and()
                                 .withWarningCount(2)
                                 .withWarningContaining(
                                         "You marked static field myStatic as parceled")
                                 .and()
                                 .withWarningContaining("NotParceled annotations are ignored");

    }

    private String getParcelerFileName(String className) {
        return className + Names.PARCELER_SUFFIX + ".java";
    }
}
