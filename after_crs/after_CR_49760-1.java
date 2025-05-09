/*Add support for checking lint on other files

This adds a new scope, Scope.OTHER, which can be used to indicate
that the check wants to investigate files other than the ones
specially handled as XML, Java, manifest or ProGuard files.
The PrivateKeyDetector now uses this such that it doesn't have
to perform its own directory iteration.

Change-Id:Ic38bf2db656f00c01d94c0c4e0112bc77218ea08*/




//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/IssueRegistry.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/IssueRegistry.java
//Synthetic comment -- index dcfed69..0b8b141 100644

//Synthetic comment -- @@ -48,6 +48,12 @@
private static Map<EnumSet<Scope>, List<Issue>> sScopeIssues = Maps.newHashMap();

/**
     * Creates a new {@linkplain IssueRegistry}
     */
    protected IssueRegistry() {
    }

    /**
* Issue reported by lint (not a specific detector) when it cannot even
* parse an XML file prior to analysis
*/








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintClient.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintClient.java
//Synthetic comment -- index 6c4b202..b8118b7 100644

//Synthetic comment -- @@ -347,13 +347,12 @@
*/
@Nullable
public File findResource(@NonNull String relativePath) {
        File top = getSdkHome();
        if (top == null) {
throw new IllegalArgumentException("Lint must be invoked with the System property "
                   + PROP_BIN_DIR + " pointing to the ANDROID_SDK tools directory");
}

File file = new File(top, relativePath);
if (file.exists()) {
return file;








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index 28168a6..dc62b5d 100644

//Synthetic comment -- @@ -134,6 +134,7 @@
private Project mCurrentProject;
private boolean mAbbreviating = true;
private boolean mParserErrors;
    private Map<Object,Object> mProperties;

/**
* Creates a new {@link LintDriver}
//Synthetic comment -- @@ -172,6 +173,38 @@
}

/**
     * Records a property for later retrieval by {@link #getProperty(Object)}
     *
     * @param key the key to associate the value with
     * @param value the value, or null to remove a previous binding
     */
    public void putProperty(@NonNull Object key, @Nullable Object value) {
        if (mProperties == null) {
            mProperties = Maps.newHashMap();
        }
        if (value == null) {
            mProperties.remove(key);
        } else {
            mProperties.put(key, value);
        }
    }

    /**
     * Returns the property previously stored with the given key, or null
     *
     * @param key the key
     * @return the value or null if not found
     */
    @Nullable
    public Object getProperty(@NonNull Object key) {
        if (mProperties != null) {
            return mProperties.get(key);
        }

        return null;
    }

    /**
* Returns the current phase number. The first pass is numbered 1. Only one pass
* will be performed, unless a {@link Detector} calls {@link #requestRepeat}.
*
//Synthetic comment -- @@ -545,6 +578,13 @@
assert detector instanceof Detector.ClassScanner : detector;
}
}

            List<Detector> otherDetectors = mScopeDetectors.get(Scope.OTHER_SCOPE);
            if (otherDetectors != null) {
                for (Detector detector : otherDetectors) {
                    assert detector instanceof Detector.OtherFileScanner : detector;
                }
            }
}
}

//Synthetic comment -- @@ -880,6 +920,14 @@
checkClasses(project, main);
}

        if (mScope.contains(Scope.OTHER)) {
            List<Detector> checks = mScopeDetectors.get(Scope.OTHER);
            if (checks != null) {
                OtherFileVisitor visitor = new OtherFileVisitor(checks);
                visitor.scan(this, project, main);
            }
        }

if (mCanceled) {
return;
}
//Synthetic comment -- @@ -888,6 +936,7 @@
checkProGuard(project, main);
}
}

private void checkProGuard(Project project, Project main) {
List<Detector> detectors = mScopeDetectors.get(Scope.PROGUARD_FILE);
if (detectors != null) {
//Synthetic comment -- @@ -934,6 +983,11 @@
}
}

    /** True if execution has been canceled */
    boolean isCanceled() {
        return mCanceled;
    }

/**
* Map from VM class name to corresponding super class VM name, if available.
* This map is typically null except <b>during</b> class processing.








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/OtherFileVisitor.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/OtherFileVisitor.java
new file mode 100644
//Synthetic comment -- index 0000000..adc36de

//Synthetic comment -- @@ -0,0 +1,143 @@
/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.tools.lint.client.api;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Project;
import com.android.tools.lint.detector.api.Scope;
import com.google.common.collect.Lists;

import java.io.File;
import java.util.*;

import static com.android.SdkConstants.FD_ASSETS;
import static com.android.tools.lint.detector.api.Detector.OtherFileScanner;

/**
 * Visitor for "other" files: files that aren't java sources,
 * XML sources, etc -- or which should have custom handling in some
 * other way.
 */
class OtherFileVisitor {
    @NonNull
    private final List<Detector> mDetectors;

    @NonNull
    private Map<Scope, List<File>> mFiles = new EnumMap<Scope, List<File>>(Scope.class);

    OtherFileVisitor(@NonNull List<Detector> detectors) {
        mDetectors = detectors;
    }

    /** Analyze other files in the given project */
    void scan(
            @NonNull LintDriver driver,
            @NonNull Project project,
            @Nullable Project main) {
        // Collect all project files
        File projectFolder = project.getDir();

        EnumSet<Scope> scopes = EnumSet.noneOf(Scope.class);
        for (Detector detector : mDetectors) {
            OtherFileScanner fileScanner = (OtherFileScanner) detector;
            EnumSet<Scope> applicable = fileScanner.getApplicableFiles();
            if (applicable.contains(Scope.OTHER)) {
                scopes = Scope.ALL;
                break;
            }
            scopes.addAll(applicable);
        }

        if (scopes.contains(Scope.RESOURCE_FILE)) {
            List<File> files = Lists.newArrayListWithExpectedSize(100);
            for (File res : project.getResourceFolders()) {
                collectFiles(files, res);
            }
            File assets = new File(projectFolder, FD_ASSETS);
            if (assets.exists()) {
                collectFiles(files, assets);
            }
            if (!files.isEmpty()) {
                mFiles.put(Scope.RESOURCE_FILE, files);
            }
        }

        if (scopes.contains(Scope.JAVA_FILE)) {
            List<File> files = Lists.newArrayListWithExpectedSize(100);
            for (File srcFolder : project.getJavaSourceFolders()) {
                collectFiles(files, srcFolder);
            }
            mFiles.put(Scope.JAVA_FILE, files);
        }

        if (scopes.contains(Scope.CLASS_FILE)) {
            List<File> files = Lists.newArrayListWithExpectedSize(100);
            for (File classFolder : project.getJavaClassFolders()) {
                collectFiles(files, classFolder);
            }
            mFiles.put(Scope.JAVA_FILE, files);
        }

        if (scopes.contains(Scope.MANIFEST)) {
            File manifestFile = project.getManifestFile();
            if (manifestFile != null) {
                mFiles.put(Scope.MANIFEST, Collections.<File>singletonList(manifestFile));
            }
        }

        for (Map.Entry<Scope, List<File>> entry : mFiles.entrySet()) {
            Scope scope = entry.getKey();
            List<File> files = entry.getValue();
            List<Detector> applicable = new ArrayList<Detector>(mDetectors.size());
            for (Detector detector : mDetectors) {
                OtherFileScanner fileScanner = (OtherFileScanner) detector;
                EnumSet<Scope> appliesTo = fileScanner.getApplicableFiles();
                if (appliesTo.contains(Scope.OTHER) || appliesTo.contains(scope)) {
                    applicable.add(detector);
                }
            }
            if (!applicable.isEmpty()) {
                for (File file : files) {
                    Context context = new Context(driver, project, main, file);
                    for (Detector detector : mDetectors) {
                        detector.beforeCheckFile(context);
                        detector.run(context);
                        detector.afterCheckFile(context);
                    }
                    if (driver.isCanceled()) {
                        return;
                    }
                }
            }
        }
    }

    private static void collectFiles(List<File> files, File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    collectFiles(files, child);
                }
            }
        } else {
            files.add(file);
        }
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Detector.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Detector.java
//Synthetic comment -- index 443746d..a3412cf 100644

//Synthetic comment -- @@ -18,9 +18,11 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.client.api.LintDriver;
import com.google.common.annotations.Beta;
import lombok.ast.AstVisitor;
import lombok.ast.MethodInvocation;
import lombok.ast.Node;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
//Synthetic comment -- @@ -33,11 +35,9 @@
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

/**
* A detector is able to find a particular problem. It might also be thought of as enforcing
* a rule, but "rule" is a bit overloaded in ADT terminology since ViewRules are used in
//Synthetic comment -- @@ -372,6 +372,25 @@
// We want to distinguish this from just an *empty* list returned by the caller!
}

    /** Specialized interface for detectors that scan other files */
    public interface OtherFileScanner {
        /**
         * Returns the set of files this scanner wants to consider.  If this includes
         * {@link Scope#OTHER} then all source files will be checked. Note that the
         * set of files will not just include files of the indicated type, but all files
         * within the relevant source folder. For example, returning {@link Scope#JAVA_FILE}
         * will not just return {@code .java} files, but also other resource files such as
         * {@code .html} and other files found within the Java source folders.
         * <p>
         * Lint will call the {@link #run(Context)}} method when the file should be checked.
         *
         * @return set of scopes that define the types of source files the
         *    detector wants to consider
         */
        @NonNull
        EnumSet<Scope> getApplicableFiles();
    }

/**
* Runs the detector. This method will not be called for certain specialized
* detectors, such as {@link XmlScanner} and {@link JavaScanner}, where
//Synthetic comment -- @@ -579,4 +598,14 @@
@NonNull MethodNode method, @NonNull AbstractInsnNode instruction) {
}

    // ---- Dummy implementations to make implementing an OtherFileScanner easier: ----

    public boolean appliesToFolder(@NonNull Scope scope, @Nullable ResourceFolderType folderType) {
        return false;
    }

    @NonNull
    public EnumSet<Scope> getApplicableFiles() {
        return Scope.OTHER_SCOPE;
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Project.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Project.java
//Synthetic comment -- index 67c3b16..968b9b1 100644

//Synthetic comment -- @@ -622,6 +622,7 @@
*
* @return the path to the manifest file, or null if it does not exist
*/
    @Nullable
public File getManifestFile() {
File manifestFile = new File(mDir, ANDROID_MANIFEST_XML);
if (manifestFile.exists()) {








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Scope.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Scope.java
//Synthetic comment -- index fcafb64..0e33f1b 100644

//Synthetic comment -- @@ -87,7 +87,13 @@
* The analysis considers classes in the libraries for this project. These
* will be analyzed before the classes themselves.
*/
    JAVA_LIBRARIES,

    /**
     * Scope for other files. Issues that specify a custom scope will be called unconditionally.
     * This will call {@link Detector#run(Context)}} on the detectors unconditionally.
     */
    OTHER;

/**
* Returns true if the given scope set corresponds to scanning a single file
//Synthetic comment -- @@ -141,4 +147,6 @@
public static final EnumSet<Scope> CLASS_FILE_SCOPE = EnumSet.of(CLASS_FILE);
/** Scope-set used for detectors which are affected by the manifest only */
public static final EnumSet<Scope> MANIFEST_SCOPE = EnumSet.of(MANIFEST);
    /** Scope-set used for detectors which correspond to some other context */
    public static final EnumSet<Scope> OTHER_SCOPE = EnumSet.of(OTHER);
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index 2721a30..de7706e 100644

//Synthetic comment -- @@ -234,7 +234,7 @@
private static final String SWITCH_TABLE_PREFIX = "$SWITCH_TABLE$";  //$NON-NLS-1$
private static final String ORDINAL_METHOD = "ordinal"; //$NON-NLS-1$

    protected ApiLookup mApiDatabase;
private int mMinApi = -1;
private Map<String, List<Pair<String, Location>>> mPendingFields;

//Synthetic comment -- @@ -411,7 +411,7 @@
}
}

    protected int getMinSdk(Context context) {
if (mMinApi == -1) {
mMinApi = context.getMainProject().getMinSdk();
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 070ca3e..90ebfd8 100644

//Synthetic comment -- @@ -267,7 +267,6 @@
}
}
}
}

/** Add the issues found in the given jar file into the given list of issues */








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/PrivateKeyDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/PrivateKeyDetector.java
//Synthetic comment -- index f957331..e8887a5 100644

//Synthetic comment -- @@ -23,7 +23,6 @@
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;
//Synthetic comment -- @@ -32,11 +31,12 @@

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;

/**
* Looks for packaged private key files.
*/
public class PrivateKeyDetector extends Detector implements Detector.OtherFileScanner {
/** Packaged private key files */
public static final Issue ISSUE = Issue.create(
"PackagedPrivateKey", //$NON-NLS-1$
//Synthetic comment -- @@ -48,7 +48,7 @@
8,
Severity.WARNING,
PrivateKeyDetector.class,
            Scope.OTHER_SCOPE);

/** Constructs a new {@link PrivateKeyDetector} check */
public PrivateKeyDetector() {
//Synthetic comment -- @@ -73,51 +73,30 @@
return false;
}

    // ---- Implements OtherFileScanner ----

    @NonNull
    @Override
    public EnumSet<Scope> getApplicableFiles() {
        return Scope.OTHER_SCOPE;
}

@Override
    public void run(@NonNull Context context) {
if (!context.getProject().getReportIssues()) {
// If this is a library project not being analyzed, ignore it
return;
}

        File file = context.file;
        if (isPrivateKeyFile(file)) {
            String fileName = file.getParentFile().getName() + File.separator
                + file.getName();
            String message = String.format(
                "The %1$s file seems to be a private key file. " +
                "Please make sure not to embed this in your APK file.", fileName);
            context.report(ISSUE, Location.create(file), message, null);
}
}

@NonNull







