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
//Synthetic comment -- index c4e9cae..b471c3f 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.ide.eclipse.adt.internal.build.RenderScriptProcessor;
import com.android.ide.eclipse.adt.internal.build.SourceProcessor;
import com.android.ide.eclipse.adt.internal.lint.EclipseLintClient;
import com.android.ide.eclipse.adt.internal.lint.LintDeltaProcessor;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
//Synthetic comment -- @@ -294,6 +295,11 @@
dv = new PreCompilerDeltaVisitor(this, sourceFolderPathList, mProcessors);
delta.accept(dv);

                    // Check for errors on save/build, if enabled
                    if (AdtPrefs.getPrefs().isLintOnSave()) {
                        LintDeltaProcessor.create().process(delta);
                    }

// Check to see if Manifest.xml, Manifest.java, or R.java have changed:
mMustCompileResources |= dv.getCompileResources();









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index 3c976b0..803478b 100644

//Synthetic comment -- @@ -588,13 +588,15 @@
* Utility method that creates a Job to run Lint on the current document.
* Does not wait for the job to finish - just returns immediately.
*
     * @return a new job, or null
     * @see EclipseLintRunner#startLint(java.util.List, IResource, IDocument,
     *      boolean, boolean)
*/
@Nullable
public Job startLintJob() {
IFile file = getInputFile();
if (file != null) {
            return EclipseLintRunner.startLint(Collections.singletonList(file), file,
getStructuredDocument(), false /*fatalOnly*/, false /*show*/);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintRunner.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintRunner.java
//Synthetic comment -- index 36a388a..25e80fc 100644

//Synthetic comment -- @@ -15,8 +15,12 @@
*/
package com.android.ide.eclipse.adt.internal.lint;

import static com.android.ide.eclipse.adt.AdtConstants.DOT_CLASS;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_JAVA;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
//Synthetic comment -- @@ -60,14 +64,19 @@
* true if fatal errors were found.
*
* @param resources the resources (project, folder or file) to be analyzed
     * @param source if checking a single source file, the source file
* @param doc the associated document, if known, or null
* @param fatalOnly if true, only report fatal issues (severity=error)
* @return true if any fatal errors were encountered.
*/
    private static boolean runLint(
            @NonNull List<? extends IResource> resources,
            @Nullable IResource source,
            @Nullable IDocument doc,
boolean fatalOnly) {
resources = addLibraries(resources);
        CheckFileJob job = (CheckFileJob) startLint(resources, source,  doc, fatalOnly,
                false /*show*/);
try {
job.join();
boolean fatal = job.isFatal();
//Synthetic comment -- @@ -85,23 +94,34 @@
}

/**
     * Runs lint and updates the markers. Does not wait for the job to finish -
     * just returns immediately.
*
* @param resources the resources (project, folder or file) to be analyzed
     * @param source if checking a single source file, the source file. When
     *            single checking an XML file, this is typically the same as the
     *            file passed in the list in the first parameter, but when
     *            checking the .class files of a Java file for example, the
     *            .class file and all the inner classes of the Java file are
     *            passed in the first parameter, and the corresponding .java
     *            source file is passed here.
* @param doc the associated document, if known, or null
* @param fatalOnly if true, only report fatal issues (severity=error)
* @param show if true, show the results in a {@link LintViewPart}
* @return the job running lint in the background.
*/
    public static Job startLint(
            @NonNull List<? extends IResource> resources,
            @Nullable IResource source,
            @Nullable IDocument doc,
            boolean fatalOnly,
            boolean show) {
if (resources != null && !resources.isEmpty()) {
resources = addLibraries(resources);

cancelCurrentJobs(false);

            CheckFileJob job = new CheckFileJob(resources, source, doc, fatalOnly);
job.schedule();

if (show) {
//Synthetic comment -- @@ -125,8 +145,8 @@
*/
public static boolean runLintOnExport(Shell shell, IProject project) {
if (AdtPrefs.getPrefs().isLintOnExport()) {
            boolean fatal = EclipseLintRunner.runLint(Collections.singletonList(project),
                    null, null, true /*fatalOnly*/);
if (fatal) {
MessageDialog.openWarning(shell,
"Export Aborted",
//Synthetic comment -- @@ -214,15 +234,20 @@
/** Job family */
private static final Object FAMILY_RUN_LINT = new Object();
private final List<? extends IResource> mResources;
        private final IResource mSource;
private final IDocument mDocument;
private LintDriver mLint;
private boolean mFatal;
private boolean mFatalOnly;

        private CheckFileJob(
                @NonNull List<? extends IResource> resources,
                @Nullable IResource source,
                @Nullable IDocument doc,
boolean fatalOnly) {
super("Running Android Lint");
mResources = resources;
            mSource = source;
mDocument = doc;
mFatalOnly = fatalOnly;
}
//Synthetic comment -- @@ -245,36 +270,64 @@
try {
monitor.beginTask("Looking for errors", IProgressMonitor.UNKNOWN);
IssueRegistry registry = EclipseLintClient.getRegistry();
                EnumSet<Scope> scope = null;
List<File> files = new ArrayList<File>(mResources.size());
for (IResource resource : mResources) {
File file = AdtUtils.getAbsolutePath(resource).toFile();
files.add(file);

                    if (resource instanceof IProject && mSource == null) {
scope = Scope.ALL;
} else {
                        String name = resource.getName();
                        if (AdtUtils.endsWithIgnoreCase(name, DOT_XML)) {
                            if (name.equals(SdkConstants.FN_ANDROID_MANIFEST_XML)) {
                                scope = EnumSet.of(Scope.MANIFEST);
                            } else {
                                scope = Scope.RESOURCE_FILE_SCOPE;
                            }
                        } else if (name.endsWith(DOT_JAVA) && resource instanceof IFile) {
                            if (scope != null) {
                                if (!scope.contains(Scope.JAVA_FILE)) {
                                    scope = EnumSet.copyOf(scope);
                                    scope.add(Scope.JAVA_FILE);
                                }
                            } else {
                                scope = Scope.JAVA_FILE_SCOPE;
                            }
                        } else if (name.endsWith(DOT_CLASS) && resource instanceof IFile) {
                            if (scope != null) {
                                if (!scope.contains(Scope.CLASS_FILE)) {
                                    scope = EnumSet.copyOf(scope);
                                    scope.add(Scope.CLASS_FILE);
                                }
                            } else {
                                scope = Scope.CLASS_FILE_SCOPE;
                            }
                        } else {
                            return new Status(Status.ERROR, AdtPlugin.PLUGIN_ID, Status.ERROR,
                                    "Only XML files are supported for single file lint", null); //$NON-NLS-1$
                        }
}
}
                if (scope == null) {
                    scope = Scope.ALL;
                }
                if (mSource == null) {
                    assert !Scope.checkSingleFile(scope) : scope + " with " + mResources;
                }
                // Check single file?
                if (mSource != null) {
// Delete specific markers
                    IMarker[] markers = EclipseLintClient.getMarkers(mSource);
                    for (IMarker marker : markers) {
                        String id = marker.getAttribute(MARKER_CHECKID_PROPERTY, ""); //$NON-NLS-1$
                        Issue issue = registry.getIssue(id);
                        if (issue == null) {
                            continue;
                        }
                        if (issue.isAdequate(scope)) {
                            marker.delete();
}
}
} else {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintDeltaProcessor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintDeltaProcessor.java
new file mode 100644
//Synthetic comment -- index 0000000..e5fe46e

//Synthetic comment -- @@ -0,0 +1,125 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.ide.eclipse.adt.internal.lint;

import static com.android.ide.eclipse.adt.AdtConstants.DOT_CLASS;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_JAVA;

import com.android.annotations.NonNull;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.swt.widgets.Display;

import java.util.ArrayList;
import java.util.List;

/**
 * Delta processor for Java files, which runs single-file lints if it finds that
 * the currently active file has been updated.
 */
public class LintDeltaProcessor implements Runnable {
    private List<IResource> mFiles;
    private IFile mActiveFile;

    private LintDeltaProcessor() {
    }

    /**
     * Creates a new {@link LintDeltaProcessor}
     *
     * @return a visitor
     */
    @NonNull
    public static LintDeltaProcessor create() {
        return new LintDeltaProcessor();
    }

    /**
     * Process the given delta: update lint on any Java source and class files found.
     *
     * @param delta the delta describing recently changed files
     */
    public void process(@NonNull IResourceDelta delta)  {
        // Get the active editor file, if any
        Display display = AdtPlugin.getDisplay();
        if (display == null) {
            return;
        }
        if (display.getThread() != Thread.currentThread()) {
            display.syncExec(this);
        } else {
            run();
        }

        if (mActiveFile == null || !mActiveFile.getName().endsWith(DOT_JAVA)) {
            return;
        }

        mFiles = new ArrayList<IResource>();
        gatherFiles(delta);

        if (!mFiles.isEmpty()) {
            EclipseLintRunner.startLint(mFiles, mActiveFile, null,
                    false /*fatalOnly*/, false /*show*/);
        }
    }

    /**
     * Collect .java and .class files to be run in lint. Only collects files
     * that match the active editor.
     */
    private void gatherFiles(@NonNull IResourceDelta delta) {
        IResource resource = delta.getResource();
        String name = resource.getName();
        if (name.endsWith(DOT_JAVA)) {
            if (resource.equals(mActiveFile)) {
                mFiles.add(resource);
            }
        } else if (name.endsWith(DOT_CLASS)) {
            // Make sure this class corresponds to the .java file, meaning it has
            // the same basename, or that it is an inner class of a class that
            // matches the same basename. (We could potentially make sure the package
            // names match too, but it's unlikely that the class names match without a
            // package match, and there's no harm in including some extra classes here,
            // since lint will resolve full paths and the resource markers won't go
            // to the wrong place, we simply end up analyzing some extra files.)
            String className = mActiveFile.getName();
            if (name.regionMatches(0, className, 0, className.length() - DOT_JAVA.length())) {
                if (name.length() == className.length() - DOT_JAVA.length() + DOT_CLASS.length()
                        || name.charAt(className.length() - DOT_JAVA.length()) == '$') {
                    mFiles.add(resource);
                }
            }
        } else {
            IResourceDelta[] children = delta.getAffectedChildren();
            if (children != null && children.length > 0) {
                for (IResourceDelta d : children) {
                    gatherFiles(d);
                }
            }
        }
    }

    @Override
    public void run() {
        // Get the active file: this must be run on the GUI thread
        mActiveFile = AdtUtils.getActiveFile();
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintViewPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintViewPart.java
//Synthetic comment -- index e9f94cc..39c6d25 100644

//Synthetic comment -- @@ -474,7 +474,7 @@
if (resources == null) {
return;
}
                        Job job = EclipseLintRunner.startLint(resources, null, null,
false /*fatalOnly*/, false /*show*/);
if (job != null && workbench != null) {
job.addJobChangeListener(LintViewPart.this);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/RunLintAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/RunLintAction.java
//Synthetic comment -- index e77e910..18f3db3 100644

//Synthetic comment -- @@ -76,7 +76,7 @@
List<IProject> projects = getProjects(mSelection, true /* warn */);

if (!projects.isEmpty()) {
            EclipseLintRunner.startLint(projects, null, null, false /*fatalOnly*/, true /*show*/);
} else {
MessageDialog.openWarning(AdtPlugin.getDisplay().getActiveShell(), "Lint",
"Could not run Lint: Select a project first.");
//Synthetic comment -- @@ -210,7 +210,7 @@

ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
ImageDescriptor clear = images.getImageDescriptor(ISharedImages.IMG_ELCL_REMOVEALL);
        LintMenuAction clearAction = new LintMenuAction("Clear Lint Warnings", clear, true, null);
addSeparator();
addAction(clearAction);

//Synthetic comment -- @@ -271,7 +271,8 @@
if (mClear) {
EclipseLintClient.clearMarkers(resources);
} else {
                EclipseLintRunner.startLint(resources, null, null, false /*fatalOnly*/,
                        true /*show*/);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/LintPreferencePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/LintPreferencePage.java
//Synthetic comment -- index bd2423e..827f8a4 100644

//Synthetic comment -- @@ -378,7 +378,7 @@
}
}

                EclipseLintRunner.startLint(androidProjects, null,  null, false /*fatalOnly*/,
true /*show*/);
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/IssueRegistry.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/IssueRegistry.java
//Synthetic comment -- index 7d9471d..74df385 100644

//Synthetic comment -- @@ -124,7 +124,7 @@
}

// Determine if the scope matches
                if (!issue.isAdequate(scope)) {
continue;
}









//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index e221faa..0f985f0 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import static com.android.tools.lint.detector.api.LintConstants.DOT_CLASS;
import static com.android.tools.lint.detector.api.LintConstants.DOT_JAR;
import static com.android.tools.lint.detector.api.LintConstants.DOT_JAVA;
import static com.android.tools.lint.detector.api.LintConstants.DOT_XML;
import static com.android.tools.lint.detector.api.LintConstants.OLD_PROGUARD_FILE;
import static com.android.tools.lint.detector.api.LintConstants.PROGUARD_FILE;
import static com.android.tools.lint.detector.api.LintConstants.RES_FOLDER;
//Synthetic comment -- @@ -245,7 +246,7 @@
String name = file.getName();
if (name.equals(ANDROID_MANIFEST_XML)) {
mScope.add(Scope.MANIFEST);
                        } else if (name.endsWith(DOT_XML)) {
mScope.add(Scope.RESOURCE_FILE);
} else if (name.equals(PROGUARD_FILE) || name.equals(OLD_PROGUARD_FILE)) {
mScope.add(Scope.PROGUARD_FILE);
//Synthetic comment -- @@ -766,8 +767,13 @@
List<Detector> checks = union(mScopeDetectors.get(Scope.JAVA_FILE),
mScopeDetectors.get(Scope.ALL_JAVA_FILES));
if (checks != null && checks.size() > 0) {
                if (project.getSubset() != null) {
                    checkIndividualJavaFiles(project, main, checks,
                            project.getSubset());
                } else {
                    List<File> sourceFolders = project.getJavaSourceFolders();
                    checkJava(project, main, sourceFolders, checks);
                }
}
}

//Synthetic comment -- @@ -775,7 +781,9 @@
return;
}

        if (mScope.contains(Scope.CLASS_FILE) || mScope.contains(Scope.JAVA_LIBRARIES)) {
            checkClasses(project, main);
        }

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
        if (project.getSubset() != null) {
            checkIndividualClassFiles(project, main, project.getSubset());
return;
}

//Synthetic comment -- @@ -935,6 +943,47 @@
runClassDetectors(Scope.CLASS_FILE, classEntries, project, main);
}

    private void checkIndividualClassFiles(
            @NonNull Project project,
            @Nullable Project main,
            @NonNull List<File> files) {
        List<ClassEntry> entries = new ArrayList<ClassEntry>(files.size());

        List<File> classFolders = project.getJavaClassFolders();
        if (!classFolders.isEmpty()) {
            for (File file : files) {
                String path = file.getPath();
                if (file.isFile() && path.endsWith(DOT_CLASS)) {
                    try {
                        byte[] bytes = Files.toByteArray(file);
                        if (bytes != null) {
                            for (File dir : classFolders) {
                                if (path.startsWith(dir.getPath())) {
                                    entries.add(new ClassEntry(file, null /* jarFile*/, dir,
                                            bytes));
                                    break;
                                }
                            }
                        }
                    } catch (IOException e) {
                        mClient.log(e, null);
                        continue;
                    }

                    if (mCanceled) {
                        return;
                    }
                }
            }

            if (entries.size() > 0) {
                // No superclass info available on individual lint runs
                mSuperClassMap = Collections.emptyMap();
                runClassDetectors(Scope.CLASS_FILE, entries, project, main);
            }
        }
    }

/**
* Stack of {@link ClassNode} nodes for outer classes of the currently
* processed class, including that class itself. Populated by
//Synthetic comment -- @@ -1186,6 +1235,32 @@
}
}

    private void checkIndividualJavaFiles(
            @NonNull Project project,
            @Nullable Project main,
            @NonNull List<Detector> checks,
            @NonNull List<File> files) {

        IJavaParser javaParser = mClient.getJavaParser();
        if (javaParser == null) {
            mClient.log(null, "No java parser provided to lint: not running Java checks");
            return;
        }

        JavaVisitor visitor = new JavaVisitor(javaParser, checks);

        for (File file : files) {
            if (file.isFile() && file.getPath().endsWith(DOT_JAVA)) {
                JavaContext context = new JavaContext(this, project, main, file);
                fireEvent(EventType.SCANNING_FILE, context);
                visitor.visitFile(context, file);
                if (mCanceled) {
                    return;
                }
            }
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;


/**
//Synthetic comment -- @@ -47,6 +50,7 @@
private String mMoreInfoUrl;
private boolean mEnabledByDefault = true;
private final EnumSet<Scope> mScope;
    private List<EnumSet<Scope>> mAnalysisScopes;
private final Class<? extends Detector> mClass;

// Use factory methods
//Synthetic comment -- @@ -244,6 +248,135 @@
}

/**
     * Returns the sets of scopes required to analyze this issue, or null if all
     * scopes named by {@link Issue#getScope()} are necessary. Note that only
     * <b>one</b> match out of this collection is required, not all, and that
     * the scope set returned by {@link #getScope()} does not have to be returned
     * by this method, but is always implied to be included.
     * <p>
     * The scopes returned by {@link Issue#getScope()} list all the various
     * scopes that are <b>affected</b> by this issue, meaning the detector
     * should consider it. Frequently, the detector must analyze all these
     * scopes in order to properly decide whether an issue is found. For
     * example, the unused resource detector needs to consider both the XML
     * resource files and the Java source files in order to decide if a resource
     * is unused. If it analyzes just the Java files for example, it might
     * incorrectly conclude that a resource is unused because it did not
     * discover a resource reference in an XML file.
     * <p>
     * However, there are other issues where the issue can occur in a variety of
     * files, but the detector can consider each in isolation. For example, the
     * API checker is affected by both XML files and Java class files (detecting
     * both layout constructor references in XML layout files as well as code
     * references in .class files). It doesn't have to analyze both; it is
     * capable of incrementally analyzing just an XML file, or just a class
     * file, without considering the other.
     * <p>
     * The required scope list provides a list of scope sets that can be used to
     * analyze this issue. For each scope set, all the scopes must be matched by
     * the incremental analysis, but any one of the scope sets can be analyzed
     * in isolation.
     * <p>
     * The required scope list is not required to include the full scope set
     * returned by {@link #getScope()}; that set is always assumed to be
     * included.
     * <p>
     * NOTE: You would normally call {@link #isAdequate(EnumSet)} rather
     * than calling this method directly.
     *
     * @return a list of required scopes, or null.
     */
    @Nullable
    public Collection<EnumSet<Scope>> getAnalysisScopes() {
        return mAnalysisScopes;
    }

    /**
     * Sets the collection of scopes that are allowed to be analyzed independently.
     * See the {@link #getAnalysisScopes()} method for a full explanation.
     * Note that you usually want to just call {@link #addAnalysisScope(EnumSet)}
     * instead of constructing a list up front and passing it in here. This
     * method exists primarily such that commonly used share sets of analysis
     * scopes can be reused and set directly.
     *
     * @param required the collection of scopes
     * @return this, for constructor chaining
     */
    public Issue setAnalysisScopes(@Nullable List<EnumSet<Scope>> required) {
        mAnalysisScopes = required;

        return this;
    }

    /**
     * Returns true if the given scope is adequate for analyzing this issue.
     * This looks through the analysis scopes (see
     * {@link #addAnalysisScope(EnumSet)}) and if the scope passed in fully
     * covers at least one of them, or if it covers the scope of the issue
     * itself (see {@link #getScope()}, which should be a superset of all the
     * analysis scopes) returns true.
     * <p>
     * The scope set returned by {@link Issue#getScope()} lists all the various
     * scopes that are <b>affected</b> by this issue, meaning the detector
     * should consider it. Frequently, the detector must analyze all these
     * scopes in order to properly decide whether an issue is found. For
     * example, the unused resource detector needs to consider both the XML
     * resource files and the Java source files in order to decide if a resource
     * is unused. If it analyzes just the Java files for example, it might
     * incorrectly conclude that a resource is unused because it did not
     * discover a resource reference in an XML file.
     * <p>
     * However, there are other issues where the issue can occur in a variety of
     * files, but the detector can consider each in isolation. For example, the
     * API checker is affected by both XML files and Java class files (detecting
     * both layout constructor references in XML layout files as well as code
     * references in .class files). It doesn't have to analyze both; it is
     * capable of incrementally analyzing just an XML file, or just a class
     * file, without considering the other.
     * <p>
     * An issue can register additional scope sets that can are adequate
     * for analyzing the issue, by calling {@link #addAnalysisScope(EnumSet)}.
     * This method returns true if the given scope matches one or more analysis
     * scope, or the overall scope.
     *
     * @param scope the scope available for analysis
     * @return true if this issue can be analyzed with the given available scope
     */
    public boolean isAdequate(@Nullable EnumSet<Scope> scope) {
        if (scope.containsAll(mScope)) {
            return true;
        }

        if (mAnalysisScopes != null) {
            for (EnumSet<Scope> analysisScope : mAnalysisScopes) {
                if (mScope.containsAll(analysisScope)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Adds a scope set that can be analyzed independently to uncover this issue.
     * See the {@link #getAnalysisScopes()} method for a full explanation.
     * Note that the {@link #getScope()} does not have to be added here; it is
     * always considered an analysis scope.
     *
     * @param scope the additional scope which can analyze this issue independently
     * @return this, for constructor chaining
     */
    public Issue addAnalysisScope(@Nullable EnumSet<Scope> scope) {
        if (mAnalysisScopes == null) {
            mAnalysisScopes = new ArrayList<EnumSet<Scope>>(2);
        }
        mAnalysisScopes.add(scope);

        return this;
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
        int size = scopes.size();
        if (size == 2) {
            // When single checking a Java source file, we check both its Java source
            // and the associated class files
            return scopes.contains(JAVA_FILE) && scopes.contains(CLASS_FILE);
        } else {
            return size == 1 &&
(scopes.contains(JAVA_FILE)
|| scopes.contains(CLASS_FILE)
|| scopes.contains(RESOURCE_FILE)
|| scopes.contains(PROGUARD_FILE)
|| scopes.contains(MANIFEST));
        }
}

/**
//Synthetic comment -- @@ -123,4 +130,8 @@
public static final EnumSet<Scope> ALL_RESOURCES_SCOPE = EnumSet.of(ALL_RESOURCE_FILES);
/** Scope-set used for detectors which are affected by a single Java source file */
public static final EnumSet<Scope> JAVA_FILE_SCOPE = EnumSet.of(JAVA_FILE);
    /** Scope-set used for detectors which are affected by a single Java class file */
    public static final EnumSet<Scope> CLASS_FILE_SCOPE = EnumSet.of(CLASS_FILE);
    /** Scope-set used for detectors which are affected by the manifest only */
    public static final EnumSet<Scope> MANIFEST_SCOPE = EnumSet.of(MANIFEST);
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index a19b4c4..43bd7a8 100644

//Synthetic comment -- @@ -81,7 +81,9 @@
6,
Severity.ERROR,
ApiDetector.class,
            EnumSet.of(Scope.CLASS_FILE, Scope.RESOURCE_FILE))
            .addAnalysisScope(Scope.RESOURCE_FILE_SCOPE)
            .addAnalysisScope(Scope.CLASS_FILE_SCOPE);

private ApiLookup mApiDatabase;
private int mMinApi = -1;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/FieldGetterDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/FieldGetterDetector.java
//Synthetic comment -- index 8bbc749..656c226 100644

//Synthetic comment -- @@ -38,7 +38,6 @@

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
//Synthetic comment -- @@ -63,7 +62,7 @@
4,
Severity.WARNING,
FieldGetterDetector.class,
            Scope.CLASS_FILE_SCOPE).
// This is a micro-optimization: not enabled by default
setEnabledByDefault(false).setMoreInfo(
"http://developer.android.com/guide/practices/design/performance.html#internal_get_set"); //$NON-NLS-1$








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/HardcodedDebugModeDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/HardcodedDebugModeDetector.java
//Synthetic comment -- index 306f546..df8c728 100644

//Synthetic comment -- @@ -34,7 +34,6 @@
import java.io.File;
import java.util.Collection;
import java.util.Collections;

/**
* Checks for hardcoded debug mode in manifest files
//Synthetic comment -- @@ -59,7 +58,7 @@
5,
Severity.WARNING,
HardcodedDebugModeDetector.class,
            Scope.MANIFEST_SCOPE);

/** Constructs a new {@link HardcodedDebugModeDetector} check */
public HardcodedDebugModeDetector() {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ManifestOrderDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ManifestOrderDetector.java
//Synthetic comment -- index 856bd26..ccf6e98 100644

//Synthetic comment -- @@ -68,7 +68,7 @@
5,
Severity.WARNING,
ManifestOrderDetector.class,
            Scope.MANIFEST_SCOPE);

/** Missing a {@code <uses-sdk>} element */
public static final Issue USES_SDK = Issue.create(
//Synthetic comment -- @@ -84,7 +84,7 @@
2,
Severity.WARNING,
ManifestOrderDetector.class,
            Scope.MANIFEST_SCOPE).setMoreInfo(
"http://developer.android.com/guide/topics/manifest/uses-sdk-element.html"); //$NON-NLS-1$

/** Missing a {@code <uses-sdk>} element */
//Synthetic comment -- @@ -101,7 +101,7 @@
6,
Severity.FATAL,
ManifestOrderDetector.class,
            Scope.MANIFEST_SCOPE).setMoreInfo(
"http://developer.android.com/guide/topics/manifest/uses-sdk-element.html"); //$NON-NLS-1$

/** Missing a {@code <uses-sdk>} element */








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/MathDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/MathDetector.java
//Synthetic comment -- index 91d2b5a..06258dd 100644

//Synthetic comment -- @@ -34,7 +34,6 @@
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//Synthetic comment -- @@ -60,7 +59,7 @@
3,
Severity.WARNING,
MathDetector.class,
            Scope.CLASS_FILE_SCOPE).setMoreInfo(
//"http://developer.android.com/reference/android/util/FloatMath.html"); //$NON-NLS-1$
"http://developer.android.com/guide/practices/design/performance.html#avoidfloat"); //$NON-NLS-1$









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/SecurityDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/SecurityDetector.java
//Synthetic comment -- index 13b5c83..807d515 100644

//Synthetic comment -- @@ -52,7 +52,6 @@
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

//Synthetic comment -- @@ -81,7 +80,7 @@
5,
Severity.WARNING,
SecurityDetector.class,
            Scope.MANIFEST_SCOPE);

/** Exported content providers */
public static final Issue EXPORTED_PROVIDER = Issue.create(
//Synthetic comment -- @@ -96,7 +95,7 @@
5,
Severity.WARNING,
SecurityDetector.class,
            Scope.MANIFEST_SCOPE);

/** Content provides which grant all URIs access */
public static final Issue OPEN_PROVIDER = Issue.create(
//Synthetic comment -- @@ -109,7 +108,7 @@
7,
Severity.WARNING,
SecurityDetector.class,
            Scope.MANIFEST_SCOPE);

/** Using the world-writable flag */
public static final Issue WORLD_WRITEABLE = Issue.create(








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ViewConstructorDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ViewConstructorDetector.java
//Synthetic comment -- index e5d1e7e..6c3b42a 100644

//Synthetic comment -- @@ -31,7 +31,6 @@
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.util.List;

/**
//Synthetic comment -- @@ -65,7 +64,7 @@
3,
Severity.WARNING,
ViewConstructorDetector.class,
            Scope.CLASS_FILE_SCOPE);

/** Constructs a new {@link ViewConstructorDetector} check */
public ViewConstructorDetector() {







