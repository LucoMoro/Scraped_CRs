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
     * Converts a workspace-relative path to an absolute file path
     *
     * @param path the workspace-relative path to convert
     * @return the corresponding absolute file in the file system
     */
    @NonNull
    public static File workspacePathToFile(@NonNull IPath path) {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IResource res = root.findMember(path);
        if (res != null) {
            return res.getLocation().toFile();
        }

        return path.toFile();
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

import static com.android.ide.eclipse.adt.AdtConstants.DOT_JAR;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.MARKER_LINT;
import static com.android.ide.eclipse.adt.AdtUtils.workspacePathToFile;
import static com.android.sdklib.SdkConstants.FD_NATIVE_LIBS;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -45,6 +48,7 @@
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;
import com.android.util.Pair;
import com.google.common.collect.Maps;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
//Synthetic comment -- @@ -52,7 +56,9 @@
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
//Synthetic comment -- @@ -85,6 +91,7 @@

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
//Synthetic comment -- @@ -233,19 +240,38 @@
return null;
}

    // Cache for {@link getProject}
    private IProject mLastEclipseProject;
    private Project mLastLintProject;

private IProject getProject(Project project) {
        if (project == mLastLintProject) {
            return mLastEclipseProject;
        }

        mLastLintProject = project;
        mLastEclipseProject = null;

if (mResources != null) {
if (mResources.size() == 1) {
                IProject p = mResources.get(0).getProject();
                mLastEclipseProject = p;
                return p;
}

            IProject last = null;
for (IResource resource : mResources) {
IProject p = resource.getProject();
                if (p != last) {
                    if (project.getDir().equals(AdtUtils.getAbsolutePath(p).toFile())) {
                        mLastEclipseProject = p;
                        return p;
                    }
                    last = p;
}
}
}

return null;
}

//Synthetic comment -- @@ -669,6 +695,111 @@
return new LazyLocation(context.file, model.getStructuredDocument(), (IndexedRegion) node);
}

    private Map<Project, ClassPathInfo> mProjectInfo;

    @Override
    @NonNull
    protected ClassPathInfo getClassPath(@NonNull Project project) {
        ClassPathInfo info;
        if (mProjectInfo == null) {
            mProjectInfo = Maps.newHashMap();
            info = null;
        } else {
            info = mProjectInfo.get(project);
        }

        if (info == null) {
            List<File> sources = null;
            List<File> classes = null;
            List<File> libraries = null;

            IProject p = getProject(project);
            if (p != null) {
                try {
                    IJavaProject javaProject = BaseProjectHelper.getJavaProject(p);

                    // Output path
                    File file = workspacePathToFile(javaProject.getOutputLocation());
                    classes = Collections.singletonList(file);

                    // Source path
                    IClasspathEntry[] entries = javaProject.getRawClasspath();
                    sources = new ArrayList<File>(entries.length);
                    libraries = new ArrayList<File>(entries.length);
                    for (int i = 0; i < entries.length; i++) {
                        IClasspathEntry entry = entries[i];
                        int kind = entry.getEntryKind();

                        if (kind == IClasspathEntry.CPE_VARIABLE) {
                            entry = JavaCore.getResolvedClasspathEntry(entry);
                            kind = entry.getEntryKind();
                        }

                        if (kind == IClasspathEntry.CPE_SOURCE) {
                            sources.add(workspacePathToFile(entry.getPath()));
                        } else if (kind == IClasspathEntry.CPE_LIBRARY) {
                            libraries.add(entry.getPath().toFile());
                        }
                        // Note that we ignore IClasspathEntry.CPE_CONTAINER:
                        // Normal Android Eclipse projects supply both
                        //   AdtConstants.CONTAINER_FRAMEWORK
                        // and
                        //   AdtConstants.CONTAINER_LIBRARIES
                        // here. We ignore the framework classes for obvious reasons,
                        // but we also ignore the library container because lint will
                        // process the libraries differently. When Eclipse builds a
                        // project, it gets the .jar output of the library projects
                        // from this container, which means it doesn't have to process
                        // the library sources. Lint on the other hand wants to process
                        // the source code, so instead it actually looks at the
                        // project.properties file to find the libraries, and then it
                        // iterates over all the library projects in turn and analyzes
                        // those separately (but passing the main project for context,
                        // such that the including project's manifest declarations
                        // are used for data like minSdkVersion level).
                        //
                        // Note that this container will also contain *other*
                        // libraries (Java libraries, not library projects) that we
                        // *should* include. However, we can't distinguish these
                        // class path entries from the library project jars,
                        // so instead of looking at these, we simply listFiles() in
                        // the libs/ folder after processing the classpath info
                    }

                    // Add in libraries
                    File libs = new File(project.getDir(), FD_NATIVE_LIBS);
                    if (libs.isDirectory()) {
                        File[] jars = libs.listFiles();
                        if (jars != null) {
                            for (File jar : jars) {
                                if (AdtUtils.endsWith(jar.getPath(), DOT_JAR)) {
                                    libraries.add(jar);
                                }
                            }
                        }
                    }
                } catch (CoreException e) {
                    AdtPlugin.log(e, null);
                }
            }

            if (sources == null) {
                sources = super.getClassPath(project).getSourceFolders();
            }
            if (classes == null) {
                classes = super.getClassPath(project).getClassFolders();
            }
            if (libraries == null) {
                libraries = super.getClassPath(project).getLibraries();
            }

            info = new ClassPathInfo(sources, classes, libraries);
            mProjectInfo.put(project, info);
        }

        return info;
    }

/**
* Returns the registry of issues to check from within Eclipse.
*








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintClient.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintClient.java
//Synthetic comment -- index 8d6367b..80c0104 100644

//Synthetic comment -- @@ -16,15 +16,19 @@

package com.android.tools.lint.client.api;

import static com.android.tools.lint.detector.api.LintConstants.DOT_JAR;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Project;
import com.android.tools.lint.detector.api.Severity;
import com.google.common.annotations.Beta;
import com.google.common.collect.Maps;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
//Synthetic comment -- @@ -177,7 +181,7 @@
*/
@NonNull
public List<File> getJavaSourceFolders(@NonNull Project project) {
        return getClassPath(project).getSourceFolders();
}

/**
//Synthetic comment -- @@ -188,7 +192,8 @@
*/
@NonNull
public List<File> getJavaClassFolders(@NonNull Project project) {
        return getClassPath(project).getClassFolders();

}

/**
//Synthetic comment -- @@ -199,7 +204,7 @@
*/
@NonNull
public List<File> getJavaLibraries(@NonNull Project project) {
        return getClassPath(project).getLibraries();
}

/**
//Synthetic comment -- @@ -286,53 +291,142 @@
}
}

    private Map<Project, ClassPathInfo> mProjectInfo;

/**
     * Information about class paths (sources, class files and libraries)
     * usually associated with a project.
     */
    protected static class ClassPathInfo {
        private final List<File> mClassFolders;
        private final List<File> mSourceFolders;
        private final List<File> mLibraries;

        public ClassPathInfo(
                @NonNull List<File> sourceFolders,
                @NonNull List<File> classFolders,
                @NonNull List<File> libraries) {
            mSourceFolders = sourceFolders;
            mClassFolders = classFolders;
            mLibraries = libraries;
        }

        @NonNull
        public List<File> getSourceFolders() {
            return mSourceFolders;
        }

        @NonNull
        public List<File> getClassFolders() {
            return mClassFolders;
        }

        @NonNull
        public List<File> getLibraries() {
            return mLibraries;
        }
    }

    /**
     * Considers the given project as an Eclipse project and returns class path
     * information for the project - the source folder(s), the output folder and
     * any libraries.
     * <p>
     * Callers will not cache calls to this method, so if it's expensive to compute
     * the classpath info, this method should perform its own caching.
     *
     * @param project the project to look up class path info for
     * @return a class path info object, never null
*/
@NonNull
    protected ClassPathInfo getClassPath(@NonNull Project project) {
        ClassPathInfo info;
        if (mProjectInfo == null) {
            mProjectInfo = Maps.newHashMap();
            info = null;
        } else {
            info = mProjectInfo.get(project);
        }

        if (info == null) {
            List<File> sources = new ArrayList<File>(2);
            List<File> classes = new ArrayList<File>(1);
            List<File> libraries = new ArrayList<File>();

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
                        List<File> addTo = null;
                        if (kind.equals("src")) {            //$NON-NLS-1$
                            addTo = sources;
                        } else if (kind.equals("output")) {  //$NON-NLS-1$
                            addTo = classes;
                        } else if (kind.equals("lib")) {     //$NON-NLS-1$
                            addTo = libraries;
                        }
                        if (addTo != null) {
                            String path = element.getAttribute("path"); //$NON-NLS-1$
                            File folder = new File(projectDir, path);
                            if (folder.exists()) {
                                addTo.add(folder);
                            }
                        }
                    }
                } catch (Exception e) {
                    log(null, null);
                }
            }

            // Add in libraries that aren't specified in the .classpath file
            File libs = new File(project.getDir(), "libs"); //$NON-NLS-1$
            if (libs.isDirectory()) {
                File[] jars = libs.listFiles();
                if (jars != null) {
                    for (File jar : jars) {
                        if (LintUtils.endsWith(jar.getPath(), DOT_JAR)
                                && !libraries.contains(jar)) {
                            libraries.add(jar);
}
}
}
}

            // Fallback, in case there is no Eclipse project metadata here
            if (sources.size() == 0) {
                File src = new File(projectDir, "src"); //$NON-NLS-1$
                if (src.exists()) {
                    sources.add(src);
                }
                File gen = new File(projectDir, "gen"); //$NON-NLS-1$
                if (gen.exists()) {
                    sources.add(gen);
}
}
            if (classes.size() == 0) {
                File folder = new File(projectDir,
                        "bin" + File.separator + "classes"); //$NON-NLS-1$ //$NON-NLS-2$
                if (folder.exists()) {
                    classes.add(folder);
                }
            }

            info = new ClassPathInfo(sources, classes, libraries);
            mProjectInfo.put(project, info);
}

        return info;
}

/**








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index 0f985f0..6c456c9 100644

//Synthetic comment -- @@ -638,7 +638,22 @@
}

private boolean isProjectDir(@NonNull File dir) {
        boolean hasManifest = new File(dir, ANDROID_MANIFEST_XML).exists();
        if (hasManifest) {
            // Special case: the bin/ folder can also contain a copy of the
            // manifest file, but this is *not* a project directory
            if (dir.getName().equals("bin")) { //$NON-NLS-1$
                // ...unless of course it just *happens* to be a project named bin, in
                // which case we peek at its parent to see if this is the case
                dir = dir.getParentFile();
                if (dir != null && isProjectDir(dir)) {
                    // Yes, it's a bin/ directory inside a real project: ignore this dir
                    return false;
                }
            }
        }

        return hasManifest;
}

private void checkProject(@NonNull Project project) {
//Synthetic comment -- @@ -865,6 +880,25 @@
return mSuperClassMap.get(name);
}

    /**
     * Returns true if the given class is a subclass of the given super class.
     *
     * @param className the fully qualifier class name (in VM format, e.g.
     *            java/lang/Integer, not java.lang.Integer).
     * @param superClassName the fully qualified super class name
     * @return true if the given class is a subclass of the given super class
     */
    public boolean isSubclassOf(@NonNull String className, @NonNull String superClassName) {
        while (className != null) {
            if (className.equals(superClassName)) {
                return true;
            }
            className = getSuperClass(className);
        }

        return false;
    }

@Nullable
private static List<Detector> union(
@Nullable List<Detector> list1,
//Synthetic comment -- @@ -977,6 +1011,7 @@
}

if (entries.size() > 0) {
                Collections.sort(entries);
// No superclass info available on individual lint runs
mSuperClassMap = Collections.emptyMap();
runClassDetectors(Scope.CLASS_FILE, entries, project, main);








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/ClassContext.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/ClassContext.java
//Synthetic comment -- index 44d5392..7b3cffd 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.tools.lint.detector.api;

import static com.android.tools.lint.detector.api.LintConstants.CONSTRUCTOR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.DOT_CLASS;
import static com.android.tools.lint.detector.api.LintConstants.DOT_JAVA;

//Synthetic comment -- @@ -444,6 +445,35 @@
return null;
}

    /**
     * Returns a location for the given {@link MethodNode}.
     *
     * @param methodNode the class in the current context
     * @param classNode the class containing the method
     * @return a location pointing to the class declaration, or as close to it
     *         as possible
     */
    @NonNull
    public Location getLocation(@NonNull MethodNode methodNode,
            @NonNull ClassNode classNode) {
        // Attempt to find a proper location for this class. This is tricky
        // since classes do not have line number entries in the class file; we need
        // to find a method, look up the corresponding line number then search
        // around it for a suitable tag, such as the class name.
        String pattern;
        if (methodNode.name.equals(CONSTRUCTOR_NAME)) {
            if (isAnonymousClass(classNode.name)) {
                pattern = classNode.superName.substring(classNode.superName.lastIndexOf('/') + 1);
            } else {
                pattern = classNode.name.substring(classNode.name.lastIndexOf('$') + 1);
            }
        } else {
            pattern = methodNode.name;
        }

        return getLocationForLine(findLineNumber(methodNode), pattern, null);
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

    // Class Names
    public static final String CONSTRUCTOR_NAME = "<init>";                          //$NON-NLS-1$
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintUtils.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintUtils.java
//Synthetic comment -- index eef6d48..154ba67 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.tools.lint.detector.api;

import static com.android.tools.lint.detector.api.LintConstants.CONSTRUCTOR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.DOT_XML;
import static com.android.tools.lint.detector.api.LintConstants.ID_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.NEW_ID_RESOURCE_PREFIX;
//Synthetic comment -- @@ -29,6 +30,10 @@
import com.google.common.annotations.Beta;
import com.google.common.io.Files;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//Synthetic comment -- @@ -489,4 +494,28 @@
}
return text;
}

    /**
     * Returns true if the given class node represents a static inner class.
     *
     * @param classNode the inner class to be checked
     * @return true if the class node represents an inner class that is static
     */
    public static boolean isStaticInnerClass(@NonNull ClassNode classNode) {
        // Note: We can't just filter out static inner classes like this:
        //     (classNode.access & Opcodes.ACC_STATIC) != 0
        // because the static flag only appears on methods and fields in the class
        // file. Instead, look for the synthetic this pointer.

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








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index 43bd7a8..4f6df4f 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.CONSTRUCTOR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.TARGET_API;

import com.android.annotations.NonNull;
//Synthetic comment -- @@ -205,7 +206,7 @@
}
// TODO: Consider other widgets outside of android.widget.*
int api = mApiDatabase.getCallVersion("android/widget/" + tag,  //$NON-NLS-1$
                    CONSTRUCTOR_NAME,
// Not all views provided this constructor right away, for example,
// LinearLayout added it in API 11 yet LinearLayout is much older:
// "(Landroid/content/Context;Landroid/util/AttributeSet;I)V"); //$NON-NLS-1$
//Synthetic comment -- @@ -463,7 +464,7 @@

// If looking for a constructor, the string we'll see in the source is not the
// method name (<init>) but the class name
        if (patternStart != null && patternStart.equals(CONSTRUCTOR_NAME)
&& node instanceof MethodInsnNode) {
String owner = ((MethodInsnNode) node).owner;
patternStart = owner.substring(owner.lastIndexOf('/') + 1);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/HandlerDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/HandlerDetector.java
//Synthetic comment -- index 03a611f..b0d61ca 100644

//Synthetic comment -- @@ -16,26 +16,21 @@

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Detector.ClassScanner;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;

import org.objectweb.asm.tree.ClassNode;

import java.io.File;

/**
* Checks that Handler implementations are top level classes or static.
//Synthetic comment -- @@ -58,7 +53,7 @@
4,
Severity.WARNING,
HandlerDetector.class,
        Scope.CLASS_FILE_SCOPE);

/** Constructs a new {@link HandlerDetector} */
public HandlerDetector() {
//Synthetic comment -- @@ -82,40 +77,13 @@
return;
}

        if (context.getDriver().isSubclassOf(classNode.name, "android/os/Handler") //$NON-NLS-1$
                && !LintUtils.isStaticInnerClass(classNode)) {
            Location location = context.getLocation(classNode);
            context.report(ISSUE, location, String.format(
                    "This Handler class should be static or leaks might occur (%1$s)",
                        ClassContext.createSignature(classNode.name, null, null)),
                    null);
}
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ViewConstructorDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ViewConstructorDetector.java
//Synthetic comment -- index 6c3b42a..72aaa77 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.CONSTRUCTOR_NAME;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
//Synthetic comment -- @@ -122,7 +124,7 @@
List methods = classNode.methods;
for (Object methodObject : methods) {
MethodNode method = (MethodNode) methodObject;
            if (method.name.equals(CONSTRUCTOR_NAME)) {
String desc = method.desc;
if (desc.equals(SIG1) || desc.equals(SIG2) || desc.equals(SIG3)) {
return;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/HandlerDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/HandlerDetectorTest.java
//Synthetic comment -- index 1631a23..979a7b0 100644

//Synthetic comment -- @@ -27,8 +27,8 @@

public void testRegistered() throws Exception {
assertEquals(
            "HandlerTest.java:14: Warning: This Handler class should be static or leaks might occur (test.pkg.HandlerTest.Inner)\n" +
            "HandlerTest.java:20: Warning: This Handler class should be static or leaks might occur (test.pkg.HandlerTest.1)",

lintProject(
"bytecode/HandlerTest.java.txt=>src/test/pkg/HandlerTest.java",







