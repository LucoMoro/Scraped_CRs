/*Add support for checking lint on other files

This adds a new scope, Scope.OTHER, which can be used to indicate
that the check wants to investigate files other than the ones
specially handled as XML, Java, manifest or ProGuard files.
The PrivateKeyDetector now uses this such that it doesn't have
to perform its own directory iteration.

Change-Id:Ic15a1545a6cef9a8a63a54d372e88c6983e1d153*/
//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/IssueRegistry.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/IssueRegistry.java
//Synthetic comment -- index dcfed69..0b8b141 100644

//Synthetic comment -- @@ -48,6 +48,12 @@
private static Map<EnumSet<Scope>, List<Issue>> sScopeIssues = Maps.newHashMap();

/**
* Issue reported by lint (not a specific detector) when it cannot even
* parse an XML file prior to analysis
*/








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintClient.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintClient.java
//Synthetic comment -- index 8a43a6c..ea1aba1 100644

//Synthetic comment -- @@ -350,13 +350,12 @@
*/
@Nullable
public File findResource(@NonNull String relativePath) {
        File dir = getLintBinDir();
        if (dir == null) {
throw new IllegalArgumentException("Lint must be invoked with the System property "
                    + PROP_BIN_DIR + " pointing to the ANDROID_SDK tools directory");
}

        File top = dir.getParentFile();
File file = new File(top, relativePath);
if (file.exists()) {
return file;








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index 28168a6..dc62b5d 100644

//Synthetic comment -- @@ -134,6 +134,7 @@
private Project mCurrentProject;
private boolean mAbbreviating = true;
private boolean mParserErrors;

/**
* Creates a new {@link LintDriver}
//Synthetic comment -- @@ -172,6 +173,38 @@
}

/**
* Returns the current phase number. The first pass is numbered 1. Only one pass
* will be performed, unless a {@link Detector} calls {@link #requestRepeat}.
*
//Synthetic comment -- @@ -545,6 +578,13 @@
assert detector instanceof Detector.ClassScanner : detector;
}
}
}
}

//Synthetic comment -- @@ -880,6 +920,14 @@
checkClasses(project, main);
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

/**
* Map from VM class name to corresponding super class VM name, if available.
* This map is typically null except <b>during</b> class processing.








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/OtherFileVisitor.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/OtherFileVisitor.java
new file mode 100644
//Synthetic comment -- index 0000000..adc36de

//Synthetic comment -- @@ -0,0 +1,143 @@








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Detector.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Detector.java
//Synthetic comment -- index 443746d..a3412cf 100644

//Synthetic comment -- @@ -18,9 +18,11 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.client.api.LintDriver;
import com.google.common.annotations.Beta;

import lombok.ast.Node;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
//Synthetic comment -- @@ -33,11 +35,9 @@
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.ast.AstVisitor;
import lombok.ast.MethodInvocation;

/**
* A detector is able to find a particular problem. It might also be thought of as enforcing
* a rule, but "rule" is a bit overloaded in ADT terminology since ViewRules are used in
//Synthetic comment -- @@ -372,6 +372,25 @@
// We want to distinguish this from just an *empty* list returned by the caller!
}

/**
* Runs the detector. This method will not be called for certain specialized
* detectors, such as {@link XmlScanner} and {@link JavaScanner}, where
//Synthetic comment -- @@ -579,4 +598,14 @@
@NonNull MethodNode method, @NonNull AbstractInsnNode instruction) {
}

}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Project.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Project.java
//Synthetic comment -- index 67c3b16..968b9b1 100644

//Synthetic comment -- @@ -622,6 +622,7 @@
*
* @return the path to the manifest file, or null if it does not exist
*/
public File getManifestFile() {
File manifestFile = new File(mDir, ANDROID_MANIFEST_XML);
if (manifestFile.exists()) {








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Scope.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Scope.java
//Synthetic comment -- index fcafb64..0e33f1b 100644

//Synthetic comment -- @@ -87,7 +87,13 @@
* The analysis considers classes in the libraries for this project. These
* will be analyzed before the classes themselves.
*/
    JAVA_LIBRARIES;

/**
* Returns true if the given scope set corresponds to scanning a single file
//Synthetic comment -- @@ -141,4 +147,6 @@
public static final EnumSet<Scope> CLASS_FILE_SCOPE = EnumSet.of(CLASS_FILE);
/** Scope-set used for detectors which are affected by the manifest only */
public static final EnumSet<Scope> MANIFEST_SCOPE = EnumSet.of(MANIFEST);
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index ab6e48a..1d223ec 100644

//Synthetic comment -- @@ -196,7 +196,7 @@
private static final String SWITCH_TABLE_PREFIX = "$SWITCH_TABLE$";  //$NON-NLS-1$
private static final String ORDINAL_METHOD = "ordinal"; //$NON-NLS-1$

    private ApiLookup mApiDatabase;
private int mMinApi = -1;
private Set<String> mWarnedFields;

//Synthetic comment -- @@ -359,7 +359,7 @@
}
}

    private int getMinSdk(Context context) {
if (mMinApi == -1) {
mMinApi = context.getMainProject().getMinSdk();
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 762446b..6838a24 100644

//Synthetic comment -- @@ -266,7 +266,6 @@
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
import com.android.tools.lint.detector.api.Project;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;
//Synthetic comment -- @@ -32,11 +31,12 @@

import java.io.File;
import java.io.IOException;

/**
* Looks for packaged private key files.
*/
public class PrivateKeyDetector extends Detector {
/** Packaged private key files */
public static final Issue ISSUE = Issue.create(
"PackagedPrivateKey", //$NON-NLS-1$
//Synthetic comment -- @@ -48,7 +48,7 @@
8,
Severity.WARNING,
PrivateKeyDetector.class,
            Scope.ALL_RESOURCES_SCOPE);

/** Constructs a new {@link PrivateKeyDetector} check */
public PrivateKeyDetector() {
//Synthetic comment -- @@ -73,51 +73,30 @@
return false;
}

    private static void checkFolder(Context context, File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        checkFolder(context, file);
                    } else {
                        if (isPrivateKeyFile(file)) {
                            String fileName = file.getParentFile().getName() + File.separator
                                + file.getName();
                            String message = String.format(
                                "The %1$s file seems to be a private key file. " +
                                "Please make sure not to embed this in your APK file.", fileName);
                            context.report(ISSUE, Location.create(file), message, null);
                        }
                    }
                }
            }
        }
}

@Override
    public void afterCheckProject(@NonNull Context context) {
if (!context.getProject().getReportIssues()) {
// If this is a library project not being analyzed, ignore it
return;
}

        Project project = context.getProject();
        File projectFolder = project.getDir();

        for (File res : project.getResourceFolders()) {
            checkFolder(context, res);
}
        checkFolder(context, new File(projectFolder, "assets"));

        for (File srcFolder : project.getJavaSourceFolders()) {
            checkFolder(context, srcFolder);
        }
    }

    @Override
    public boolean appliesTo(@NonNull Context context, @NonNull File file) {
        return true;
}

@NonNull







