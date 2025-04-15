/*Run lint on save in Java files, checking .java and .class files

This changeset adds support for per-save file checking in Java source
files.  It will run both source file and bytecode based checks,
possibly at different times (since they are updated at different
times). This required some changes to the incremental lint runner,
since now incremental checking means possibly touching more than one
file (multiple inner classes for a single source), as well as
distinguishing between the source files containing markers (the .java
file) and the actual files being analyzed (the .class files).

This changeset also formalizes incremental lint checking a bit: it now
distinguishes between the affected scope of an issue (all the various
file types that can affect an issue), as well as the scope sets that
are capable of analyzing the issue independently.

Take the API check for example. Its affected scope includes both XML
files and Java class files, since both can contain API references (in
the case of XML, a <GridLayout> reference is an invocation of a
constructor of the GridLayout class for example).  However, we can
analyze a standalone class file, or a standalone XML file, and
incrementally update issues found in the file, without regard for the
other. Therefore, the API detector has two separate analysis scopes:
classes, and XML resources.  The manifest registration detector on the
other hand needs to look at both the manifest file and the class
files; it cannot look at just a subset of these.

Change-Id:Ibf5ca8a90846256e0817b419908ee53f8354412a*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index c4e9cae..e5a5e8d 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.ide.eclipse.adt.internal.build.RenderScriptProcessor;
import com.android.ide.eclipse.adt.internal.build.SourceProcessor;
import com.android.ide.eclipse.adt.internal.lint.EclipseLintClient;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
//Synthetic comment -- @@ -294,6 +295,11 @@
dv = new PreCompilerDeltaVisitor(this, sourceFolderPathList, mProcessors);
delta.accept(dv);

// Check to see if Manifest.xml, Manifest.java, or R.java have changed:
mMustCompileResources |= dv.getCompileResources();









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index 3c976b0..803478b 100644

//Synthetic comment -- @@ -588,13 +588,15 @@
* Utility method that creates a Job to run Lint on the current document.
* Does not wait for the job to finish - just returns immediately.
*
     * @see EclipseLintRunner#startLint(java.util.List, IDocument, boolean, boolean)
*/
@Nullable
public Job startLintJob() {
IFile file = getInputFile();
if (file != null) {
            return EclipseLintRunner.startLint(Collections.singletonList(file),
getStructuredDocument(), false /*fatalOnly*/, false /*show*/);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintRunner.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintRunner.java
//Synthetic comment -- index 36a388a..25e80fc 100644

//Synthetic comment -- @@ -15,8 +15,12 @@
*/
package com.android.ide.eclipse.adt.internal.lint;

import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
//Synthetic comment -- @@ -60,14 +64,19 @@
* true if fatal errors were found.
*
* @param resources the resources (project, folder or file) to be analyzed
* @param doc the associated document, if known, or null
* @param fatalOnly if true, only report fatal issues (severity=error)
* @return true if any fatal errors were encountered.
*/
    private static boolean runLint(List<? extends IResource> resources, IDocument doc,
boolean fatalOnly) {
resources = addLibraries(resources);
        CheckFileJob job = (CheckFileJob) startLint(resources, doc, fatalOnly, false /*show*/);
try {
job.join();
boolean fatal = job.isFatal();
//Synthetic comment -- @@ -85,23 +94,34 @@
}

/**
     * Runs lint and updates the markers. Does not wait for the job to
     * finish - just returns immediately.
*
* @param resources the resources (project, folder or file) to be analyzed
* @param doc the associated document, if known, or null
* @param fatalOnly if true, only report fatal issues (severity=error)
* @param show if true, show the results in a {@link LintViewPart}
* @return the job running lint in the background.
*/
    public static Job startLint(List<? extends IResource> resources,
            IDocument doc, boolean fatalOnly, boolean show) {
if (resources != null && !resources.isEmpty()) {
resources = addLibraries(resources);

cancelCurrentJobs(false);

            CheckFileJob job = new CheckFileJob(resources, doc, fatalOnly);
job.schedule();

if (show) {
//Synthetic comment -- @@ -125,8 +145,8 @@
*/
public static boolean runLintOnExport(Shell shell, IProject project) {
if (AdtPrefs.getPrefs().isLintOnExport()) {
            boolean fatal = EclipseLintRunner.runLint(Collections.singletonList(project), null,
                    true /*fatalOnly*/);
if (fatal) {
MessageDialog.openWarning(shell,
"Export Aborted",
//Synthetic comment -- @@ -214,15 +234,20 @@
/** Job family */
private static final Object FAMILY_RUN_LINT = new Object();
private final List<? extends IResource> mResources;
private final IDocument mDocument;
private LintDriver mLint;
private boolean mFatal;
private boolean mFatalOnly;

        private CheckFileJob(List<? extends IResource> resources, IDocument doc,
boolean fatalOnly) {
super("Running Android Lint");
mResources = resources;
mDocument = doc;
mFatalOnly = fatalOnly;
}
//Synthetic comment -- @@ -245,36 +270,64 @@
try {
monitor.beginTask("Looking for errors", IProgressMonitor.UNKNOWN);
IssueRegistry registry = EclipseLintClient.getRegistry();
                EnumSet<Scope> scope = Scope.ALL;
List<File> files = new ArrayList<File>(mResources.size());
for (IResource resource : mResources) {
File file = AdtUtils.getAbsolutePath(resource).toFile();
files.add(file);

                    if (resource instanceof IProject) {
scope = Scope.ALL;
                    } else if (resource instanceof IFile
                            && AdtUtils.endsWithIgnoreCase(resource.getName(), DOT_XML)) {
                        if (resource.getName().equals(SdkConstants.FN_ANDROID_MANIFEST_XML)) {
                            scope = EnumSet.of(Scope.MANIFEST);
                        } else {
                            scope = Scope.RESOURCE_FILE_SCOPE;
                        }
} else {
                        return new Status(Status.ERROR, AdtPlugin.PLUGIN_ID, Status.ERROR,
                                "Only XML files are supported for single file lint", null); //$NON-NLS-1$
}
}
                if (Scope.checkSingleFile(scope)) {
// Delete specific markers
                    for (IResource resource : mResources) {
                        IMarker[] markers = EclipseLintClient.getMarkers(resource);
                        for (IMarker marker : markers) {
                            String id = marker.getAttribute(MARKER_CHECKID_PROPERTY, ""); //$NON-NLS-1$
                            Issue issue = registry.getIssue(id);
                            if (issue == null || issue.getScope().equals(scope)) {
                                marker.delete();
                            }
}
}
} else {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintDeltaVisitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintDeltaVisitor.java
new file mode 100644
//Synthetic comment -- index 0000000..6903127

//Synthetic comment -- @@ -0,0 +1,129 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintViewPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintViewPart.java
//Synthetic comment -- index e9f94cc..39c6d25 100644

//Synthetic comment -- @@ -474,7 +474,7 @@
if (resources == null) {
return;
}
                        Job job = EclipseLintRunner.startLint(resources, null,
false /*fatalOnly*/, false /*show*/);
if (job != null && workbench != null) {
job.addJobChangeListener(LintViewPart.this);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/RunLintAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/RunLintAction.java
//Synthetic comment -- index e77e910..dc58cf7 100644

//Synthetic comment -- @@ -76,7 +76,7 @@
List<IProject> projects = getProjects(mSelection, true /* warn */);

if (!projects.isEmpty()) {
            EclipseLintRunner.startLint(projects, null, false /*fatalOnly*/, true /*show*/);
} else {
MessageDialog.openWarning(AdtPlugin.getDisplay().getActiveShell(), "Lint",
"Could not run Lint: Select a project first.");
//Synthetic comment -- @@ -271,7 +271,8 @@
if (mClear) {
EclipseLintClient.clearMarkers(resources);
} else {
                EclipseLintRunner.startLint(resources, null, false /*fatalOnly*/, true /*show*/);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/LintPreferencePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/LintPreferencePage.java
//Synthetic comment -- index bd2423e..827f8a4 100644

//Synthetic comment -- @@ -378,7 +378,7 @@
}
}

                EclipseLintRunner.startLint(androidProjects, null, false /*fatalOnly*/,
true /*show*/);
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/IssueRegistry.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/IssueRegistry.java
//Synthetic comment -- index 7d9471d..74df385 100644

//Synthetic comment -- @@ -124,7 +124,7 @@
}

// Determine if the scope matches
                if (!scope.containsAll(issueScope)) {
continue;
}









//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index e221faa..8d27b89 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import static com.android.tools.lint.detector.api.LintConstants.DOT_CLASS;
import static com.android.tools.lint.detector.api.LintConstants.DOT_JAR;
import static com.android.tools.lint.detector.api.LintConstants.DOT_JAVA;
import static com.android.tools.lint.detector.api.LintConstants.OLD_PROGUARD_FILE;
import static com.android.tools.lint.detector.api.LintConstants.PROGUARD_FILE;
import static com.android.tools.lint.detector.api.LintConstants.RES_FOLDER;
//Synthetic comment -- @@ -245,7 +246,7 @@
String name = file.getName();
if (name.equals(ANDROID_MANIFEST_XML)) {
mScope.add(Scope.MANIFEST);
                        } else if (name.endsWith(".xml")) {
mScope.add(Scope.RESOURCE_FILE);
} else if (name.equals(PROGUARD_FILE) || name.equals(OLD_PROGUARD_FILE)) {
mScope.add(Scope.PROGUARD_FILE);
//Synthetic comment -- @@ -766,8 +767,13 @@
List<Detector> checks = union(mScopeDetectors.get(Scope.JAVA_FILE),
mScopeDetectors.get(Scope.ALL_JAVA_FILES));
if (checks != null && checks.size() > 0) {
                List<File> sourceFolders = project.getJavaSourceFolders();
                checkJava(project, main, sourceFolders, checks);
}
}

//Synthetic comment -- @@ -775,7 +781,9 @@
return;
}

        checkClasses(project, main);

if (mCanceled) {
return;
//Synthetic comment -- @@ -785,7 +793,6 @@
checkProGuard(project, main);
}
}

private void checkProGuard(Project project, Project main) {
List<Detector> detectors = mScopeDetectors.get(Scope.PROGUARD_FILE);
if (detectors != null) {
//Synthetic comment -- @@ -884,7 +891,8 @@

/** Check the classes in this project (and if applicable, in any library projects */
private void checkClasses(Project project, Project main) {
        if (!(mScope.contains(Scope.CLASS_FILE) || mScope.contains(Scope.JAVA_LIBRARIES))) {
return;
}

//Synthetic comment -- @@ -935,6 +943,46 @@
runClassDetectors(Scope.CLASS_FILE, classEntries, project, main);
}

/**
* Stack of {@link ClassNode} nodes for outer classes of the currently
* processed class, including that class itself. Populated by
//Synthetic comment -- @@ -1186,6 +1234,32 @@
}
}

private void gatherJavaFiles(@NonNull File dir, @NonNull List<File> result) {
File[] files = dir.listFiles();
if (files != null) {








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Issue.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Issue.java
//Synthetic comment -- index 3e49bc4..9f42fb4 100644

//Synthetic comment -- @@ -21,7 +21,10 @@
import com.android.tools.lint.client.api.Configuration;
import com.google.common.annotations.Beta;

import java.util.EnumSet;


/**
//Synthetic comment -- @@ -47,6 +50,7 @@
private String mMoreInfoUrl;
private boolean mEnabledByDefault = true;
private final EnumSet<Scope> mScope;
private final Class<? extends Detector> mClass;

// Use factory methods
//Synthetic comment -- @@ -244,6 +248,135 @@
}

/**
* Returns the class of the detector to use to find this issue
*
* @return the class of the detector to use to find this issue








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Scope.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Scope.java
//Synthetic comment -- index 4b9da60..a917c11 100644

//Synthetic comment -- @@ -90,12 +90,19 @@
* @return true if the scope set references a single file
*/
public static boolean checkSingleFile(@NonNull EnumSet<Scope> scopes) {
        return scopes.size() == 1 &&
(scopes.contains(JAVA_FILE)
|| scopes.contains(CLASS_FILE)
|| scopes.contains(RESOURCE_FILE)
|| scopes.contains(PROGUARD_FILE)
|| scopes.contains(MANIFEST));
}

/**
//Synthetic comment -- @@ -123,4 +130,8 @@
public static final EnumSet<Scope> ALL_RESOURCES_SCOPE = EnumSet.of(ALL_RESOURCE_FILES);
/** Scope-set used for detectors which are affected by a single Java source file */
public static final EnumSet<Scope> JAVA_FILE_SCOPE = EnumSet.of(JAVA_FILE);
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index a19b4c4..43bd7a8 100644

//Synthetic comment -- @@ -81,7 +81,9 @@
6,
Severity.ERROR,
ApiDetector.class,
            EnumSet.of(Scope.CLASS_FILE, Scope.RESOURCE_FILE));

private ApiLookup mApiDatabase;
private int mMinApi = -1;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/FieldGetterDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/FieldGetterDetector.java
//Synthetic comment -- index 8bbc749..656c226 100644

//Synthetic comment -- @@ -38,7 +38,6 @@

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
//Synthetic comment -- @@ -63,7 +62,7 @@
4,
Severity.WARNING,
FieldGetterDetector.class,
            EnumSet.of(Scope.CLASS_FILE)).
// This is a micro-optimization: not enabled by default
setEnabledByDefault(false).setMoreInfo(
"http://developer.android.com/guide/practices/design/performance.html#internal_get_set"); //$NON-NLS-1$








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/HardcodedDebugModeDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/HardcodedDebugModeDetector.java
//Synthetic comment -- index 306f546..df8c728 100644

//Synthetic comment -- @@ -34,7 +34,6 @@
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;

/**
* Checks for hardcoded debug mode in manifest files
//Synthetic comment -- @@ -59,7 +58,7 @@
5,
Severity.WARNING,
HardcodedDebugModeDetector.class,
            EnumSet.of(Scope.MANIFEST));

/** Constructs a new {@link HardcodedDebugModeDetector} check */
public HardcodedDebugModeDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ManifestOrderDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ManifestOrderDetector.java
//Synthetic comment -- index 856bd26..ccf6e98 100644

//Synthetic comment -- @@ -68,7 +68,7 @@
5,
Severity.WARNING,
ManifestOrderDetector.class,
            EnumSet.of(Scope.MANIFEST));

/** Missing a {@code <uses-sdk>} element */
public static final Issue USES_SDK = Issue.create(
//Synthetic comment -- @@ -84,7 +84,7 @@
2,
Severity.WARNING,
ManifestOrderDetector.class,
            EnumSet.of(Scope.MANIFEST)).setMoreInfo(
"http://developer.android.com/guide/topics/manifest/uses-sdk-element.html"); //$NON-NLS-1$

/** Missing a {@code <uses-sdk>} element */
//Synthetic comment -- @@ -101,7 +101,7 @@
6,
Severity.FATAL,
ManifestOrderDetector.class,
            EnumSet.of(Scope.MANIFEST)).setMoreInfo(
"http://developer.android.com/guide/topics/manifest/uses-sdk-element.html"); //$NON-NLS-1$

/** Missing a {@code <uses-sdk>} element */








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/MathDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/MathDetector.java
//Synthetic comment -- index 91d2b5a..06258dd 100644

//Synthetic comment -- @@ -34,7 +34,6 @@
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//Synthetic comment -- @@ -60,7 +59,7 @@
3,
Severity.WARNING,
MathDetector.class,
            EnumSet.of(Scope.CLASS_FILE)).setMoreInfo(
//"http://developer.android.com/reference/android/util/FloatMath.html"); //$NON-NLS-1$
"http://developer.android.com/guide/practices/design/performance.html#avoidfloat"); //$NON-NLS-1$









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/SecurityDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/SecurityDetector.java
//Synthetic comment -- index 13b5c83..807d515 100644

//Synthetic comment -- @@ -52,7 +52,6 @@
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

//Synthetic comment -- @@ -81,7 +80,7 @@
5,
Severity.WARNING,
SecurityDetector.class,
            EnumSet.of(Scope.MANIFEST));

/** Exported content providers */
public static final Issue EXPORTED_PROVIDER = Issue.create(
//Synthetic comment -- @@ -96,7 +95,7 @@
5,
Severity.WARNING,
SecurityDetector.class,
            EnumSet.of(Scope.MANIFEST));

/** Content provides which grant all URIs access */
public static final Issue OPEN_PROVIDER = Issue.create(
//Synthetic comment -- @@ -109,7 +108,7 @@
7,
Severity.WARNING,
SecurityDetector.class,
            EnumSet.of(Scope.MANIFEST));

/** Using the world-writable flag */
public static final Issue WORLD_WRITEABLE = Issue.create(








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ViewConstructorDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ViewConstructorDetector.java
//Synthetic comment -- index e5d1e7e..6c3b42a 100644

//Synthetic comment -- @@ -31,7 +31,6 @@
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.util.EnumSet;
import java.util.List;

/**
//Synthetic comment -- @@ -65,7 +64,7 @@
3,
Severity.WARNING,
ViewConstructorDetector.class,
            EnumSet.of(Scope.CLASS_FILE));

/** Constructs a new {@link ViewConstructorDetector} check */
public ViewConstructorDetector() {







