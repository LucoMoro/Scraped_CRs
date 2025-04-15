/*Lint infrastructure fixes

This changeset contains various unrelated fixes to the lint
infrastructure:

(1) Tweak the way the classpaths are computed in the default lint
    client method such that rather than reading and parsing the
    .classpath file 3 times, once for each of source-path, output-path
    and library-path, it's now processing it once and storing the
    results for all 3.

(2) Override the lookup-classpath method in Eclipse to directly query
    the Eclipse APIs for obtaining the classpath info.

(3) Add in user libraries found in libs/, since these don't
    necessarily show up in the .classpath file.

(4) Fix a couple of bugs related to checking .class files: First, when
    locating the project for a .class file, lint would search upwards
    for the surrounding project, which meant looking for the nearest
    parent containing an AndroidManifest.xml file.  However, in the
    case of .class files, it will first encounter the bin/ directory,
    which can contain a manifest file, so it would compute a project
    for the bin/ folder rather than its parent, which meant the source
    paths would be wrong.

    Second, the list of class entries to be processed by lint must be
    sorted prior to processing; the code dealing with innerclasses
    depends on that.

(5) Some minor code cleanup: Move some generic utility code and some
    string literals out of specific detectors and into the generic
    utility and constant classes.

(6) Cache results of the lint-project to eclipse-project lookup method
    since that method is called repeatedly with the same (current)
    project.

Change-Id:I33603eed8381ca54314202620cb1bb033e70f775*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index 09092e7..47e650d 100644

//Synthetic comment -- @@ -444,6 +444,23 @@
}

/**
* Converts a {@link File} to an {@link IFile}, if possible.
*
* @param file a file to be converted








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java
//Synthetic comment -- index 703be80..b5810a4 100644

//Synthetic comment -- @@ -15,8 +15,11 @@
*/
package com.android.ide.eclipse.adt.internal.lint;

import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.MARKER_LINT;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -45,6 +48,7 @@
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;
import com.android.util.Pair;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
//Synthetic comment -- @@ -52,7 +56,9 @@
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
//Synthetic comment -- @@ -85,6 +91,7 @@

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
//Synthetic comment -- @@ -233,19 +240,38 @@
return null;
}

private IProject getProject(Project project) {
if (mResources != null) {
if (mResources.size() == 1) {
                return mResources.get(0).getProject();
}

for (IResource resource : mResources) {
IProject p = resource.getProject();
                if (project.getDir().equals(AdtUtils.getAbsolutePath(p).toFile())) {
                    return p;
}
}
}
return null;
}

//Synthetic comment -- @@ -669,6 +695,111 @@
return new LazyLocation(context.file, model.getStructuredDocument(), (IndexedRegion) node);
}

/**
* Returns the registry of issues to check from within Eclipse.
*








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintClient.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintClient.java
//Synthetic comment -- index 8d6367b..80c0104 100644

//Synthetic comment -- @@ -16,15 +16,19 @@

package com.android.tools.lint.client.api;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Project;
import com.android.tools.lint.detector.api.Severity;
import com.google.common.annotations.Beta;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
//Synthetic comment -- @@ -177,7 +181,7 @@
*/
@NonNull
public List<File> getJavaSourceFolders(@NonNull Project project) {
        return getEclipseClasspath(project, "src", "src", "gen"); //$NON-NLS-1$ //$NON-NLS-2$
}

/**
//Synthetic comment -- @@ -188,7 +192,8 @@
*/
@NonNull
public List<File> getJavaClassFolders(@NonNull Project project) {
        return getEclipseClasspath(project, "output", "bin"); //$NON-NLS-1$ //$NON-NLS-2$
}

/**
//Synthetic comment -- @@ -199,7 +204,7 @@
*/
@NonNull
public List<File> getJavaLibraries(@NonNull Project project) {
        return getEclipseClasspath(project, "lib"); //$NON-NLS-1$
}

/**
//Synthetic comment -- @@ -286,53 +291,142 @@
}
}

/**
     * Considers the given directory as an Eclipse project and returns either
     * its source or its output folders depending on the {@code attribute} parameter.
*/
@NonNull
    private List<File> getEclipseClasspath(@NonNull Project project, @NonNull String attribute,
            @NonNull String... fallbackPaths) {
        List<File> folders = new ArrayList<File>();
        File projectDir = project.getDir();
        File classpathFile = new File(projectDir, ".classpath"); //$NON-NLS-1$
        if (classpathFile.exists()) {
            String classpathXml = readFile(classpathFile);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            InputSource is = new InputSource(new StringReader(classpathXml));
            factory.setNamespaceAware(false);
            factory.setValidating(false);
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(is);
                NodeList tags = document.getElementsByTagName("classpathentry"); //$NON-NLS-1$
                for (int i = 0, n = tags.getLength(); i < n; i++) {
                    Element element = (Element) tags.item(i);
                    String kind = element.getAttribute("kind"); //$NON-NLS-1$
                    if (kind.equals(attribute)) {
                        String path = element.getAttribute("path"); //$NON-NLS-1$
                        File sourceFolder = new File(projectDir, path);
                        if (sourceFolder.exists()) {
                            folders.add(sourceFolder);
}
}
}
            } catch (Exception e) {
                log(null, null);
}
        }

        // Fallback?
        if (folders.size() == 0) {
            for (String fallbackPath : fallbackPaths) {
                File folder = new File(projectDir, fallbackPath);
                if (folder.exists()) {
                    folders.add(folder);
}
}
}

        return folders;
}

/**








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index 0f985f0..3eeec94 100644

//Synthetic comment -- @@ -638,7 +638,22 @@
}

private boolean isProjectDir(@NonNull File dir) {
        return new File(dir, ANDROID_MANIFEST_XML).exists();
}

private void checkProject(@NonNull Project project) {
//Synthetic comment -- @@ -865,6 +880,30 @@
return mSuperClassMap.get(name);
}

@Nullable
private static List<Detector> union(
@Nullable List<Detector> list1,
//Synthetic comment -- @@ -977,6 +1016,7 @@
}

if (entries.size() > 0) {
// No superclass info available on individual lint runs
mSuperClassMap = Collections.emptyMap();
runClassDetectors(Scope.CLASS_FILE, entries, project, main);








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/ClassContext.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/ClassContext.java
//Synthetic comment -- index 44d5392..7b3cffd 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.tools.lint.detector.api;

import static com.android.tools.lint.detector.api.LintConstants.DOT_CLASS;
import static com.android.tools.lint.detector.api.LintConstants.DOT_JAVA;

//Synthetic comment -- @@ -444,6 +445,35 @@
return null;
}

private static boolean isAnonymousClass(@NonNull String fqcn) {
int lastIndex = fqcn.lastIndexOf('$');
if (lastIndex != -1 && lastIndex < fqcn.length() - 1) {








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintConstants.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintConstants.java
//Synthetic comment -- index a2757cb..2730637 100644

//Synthetic comment -- @@ -316,4 +316,7 @@
public static final String TARGET_API = "TargetApi";                 //$NON-NLS-1$
public static final String FQCN_SUPPRESS_LINT = "android.annotation." + SUPPRESS_LINT; //$NON-NLS-1$
public static final String FQCN_TARGET_API = "android.annotation." + TARGET_API; //$NON-NLS-1$
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintUtils.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintUtils.java
//Synthetic comment -- index eef6d48..154ba67 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.tools.lint.detector.api;

import static com.android.tools.lint.detector.api.LintConstants.DOT_XML;
import static com.android.tools.lint.detector.api.LintConstants.ID_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.NEW_ID_RESOURCE_PREFIX;
//Synthetic comment -- @@ -29,6 +30,10 @@
import com.google.common.annotations.Beta;
import com.google.common.io.Files;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//Synthetic comment -- @@ -489,4 +494,28 @@
}
return text;
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index 43bd7a8..4f6df4f 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.TARGET_API;

import com.android.annotations.NonNull;
//Synthetic comment -- @@ -205,7 +206,7 @@
}
// TODO: Consider other widgets outside of android.widget.*
int api = mApiDatabase.getCallVersion("android/widget/" + tag,  //$NON-NLS-1$
                    "<init>", //$NON-NLS-1$
// Not all views provided this constructor right away, for example,
// LinearLayout added it in API 11 yet LinearLayout is much older:
// "(Landroid/content/Context;Landroid/util/AttributeSet;I)V"); //$NON-NLS-1$
//Synthetic comment -- @@ -463,7 +464,7 @@

// If looking for a constructor, the string we'll see in the source is not the
// method name (<init>) but the class name
        if (patternStart != null && patternStart.equals("<init>") //$NON-NLS-1$
&& node instanceof MethodInsnNode) {
String owner = ((MethodInsnNode) node).owner;
patternStart = owner.substring(owner.lastIndexOf('/') + 1);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/HandlerDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/HandlerDetector.java
//Synthetic comment -- index 03a611f..425edac 100644

//Synthetic comment -- @@ -16,26 +16,21 @@

package com.android.tools.lint.checks;

import com.android.annotations.NonNull;
import com.android.tools.lint.client.api.LintDriver;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Detector.ClassScanner;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.io.File;
import java.util.EnumSet;
import java.util.List;

/**
* Checks that Handler implementations are top level classes or static.
//Synthetic comment -- @@ -58,7 +53,7 @@
4,
Severity.WARNING,
HandlerDetector.class,
        EnumSet.of(Scope.CLASS_FILE));

/** Constructs a new {@link HandlerDetector} */
public HandlerDetector() {
//Synthetic comment -- @@ -82,40 +77,13 @@
return;
}

        // Note: We can't just filter out static inner classes like this:
        //     (classNode.access & Opcodes.ACC_STATIC) != 0
        // because the static flag only appears on methods and fields in the class
        // file. Instead, look for the synthetic this pointer.

        LintDriver driver = context.getDriver();
        String name = classNode.name;
        while (name != null) {
            if (name.equals("android/os/Handler")) { //$NON-NLS-1$
                if (isStaticInnerClass(classNode)) {
                    return;
                }

                Location location = context.getLocation(classNode);
                context.report(ISSUE, location, String.format(
                        "This Handler class should be static or leaks might occur (%1$s)",
                            ClassContext.createSignature(classNode.name, null, null)),
                        null);
                return;
            }
            name = driver.getSuperClass(name);
}
}

    private boolean isStaticInnerClass(@NonNull ClassNode classNode) {
        @SuppressWarnings("rawtypes") // ASM API
        List fieldList = classNode.fields;
        for (Object f : fieldList) {
            FieldNode field = (FieldNode) f;
            if (field.name.startsWith("this$") && (field.access & Opcodes.ACC_SYNTHETIC) != 0) {
                return false;
            }
        }

        return true;
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ViewConstructorDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ViewConstructorDetector.java
//Synthetic comment -- index 6c3b42a..72aaa77 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
//Synthetic comment -- @@ -122,7 +124,7 @@
List methods = classNode.methods;
for (Object methodObject : methods) {
MethodNode method = (MethodNode) methodObject;
            if (method.name.equals("<init>")) { //$NON-NLS-1$
String desc = method.desc;
if (desc.equals(SIG1) || desc.equals(SIG2) || desc.equals(SIG3)) {
return;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/HandlerDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/HandlerDetectorTest.java
//Synthetic comment -- index 1631a23..979a7b0 100644

//Synthetic comment -- @@ -27,8 +27,8 @@

public void testRegistered() throws Exception {
assertEquals(
            "HandlerTest$1.class: Warning: This Handler class should be static or leaks might occur (test.pkg.HandlerTest.1)\n" +
            "HandlerTest$Inner.class: Warning: This Handler class should be static or leaks might occur (test.pkg.HandlerTest.Inner)",

lintProject(
"bytecode/HandlerTest.java.txt=>src/test/pkg/HandlerTest.java",







