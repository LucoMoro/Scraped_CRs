/*Additional fixes to filter out library project lint warnings. DO NOT MERGE

This is a cherrypick of just the bug fix portion ofhttps://android-review.googlesource.com/#/c/45952Lint already has the concept of whether a project should report lint
warnings or not; a project can be included in analysis (e.g. for
unused resource checks), but can filter out any warnings local to that
project. This is useful when you are using a library, but don't want
to see errors from that library which may not be under your control.

The way this is handled from the command line is that lint will only
report errors for projects you've referenced; e.g. if you run "lint
/foo/bar" this will show errors in /foo/bar, but exclude errors found
in the library project /foo/bar/../library".

However, there were several lint checks which needed additional fixes
for this, because (like the unused error detector) they gather data
from multiple projects and process and report in the after-project
hook.

Change-Id:I4c4b3ae451866231ae03dc75e8e1c32f27a84534*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ArraySizeDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ArraySizeDetector.java
//Synthetic comment -- index 8898507..4f63913 100644

//Synthetic comment -- @@ -80,8 +80,6 @@
ArraySizeDetector.class,
Scope.ALL_RESOURCES_SCOPE);

private Multimap<File, Pair<String, Integer>> mFileToArrayCount;

/** Locations for each array name. Populated during phase 2, if necessary */
//Synthetic comment -- @@ -238,8 +236,10 @@
} else {
String name = attribute.getValue();
if (phase == 1) {
                if (context.getProject().getReportIssues()) {
                    int childCount = LintUtils.getChildCount(element);
                    mFileToArrayCount.put(context.file, Pair.of(name, childCount));
                }
} else {
assert phase == 2;
if (mLocations.containsKey(name)) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ButtonDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ButtonDetector.java
//Synthetic comment -- index 240c3da..a06fc1f 100644

//Synthetic comment -- @@ -366,6 +366,11 @@
return;
}

        if (!context.getProject().getReportIssues()) {
            // If this is a library project not being analyzed, ignore it
            return;
        }

if (mApplicableResources == null) {
mApplicableResources = new HashSet<String>();
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/DuplicateIdDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/DuplicateIdDetector.java
//Synthetic comment -- index 720845d..48e8661 100644

//Synthetic comment -- @@ -20,9 +20,9 @@
import static com.android.SdkConstants.ATTR_ID;
import static com.android.SdkConstants.ATTR_LAYOUT;
import static com.android.SdkConstants.DOT_XML;
import static com.android.SdkConstants.LAYOUT_RESOURCE_PREFIX;
import static com.android.SdkConstants.NEW_ID_PREFIX;
import static com.android.SdkConstants.VIEW_INCLUDE;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
//Synthetic comment -- @@ -211,6 +211,11 @@
layout = layout.substring(LAYOUT_RESOURCE_PREFIX.length());

if (context.getPhase() == 1) {
                if (!context.getProject().getReportIssues()) {
                    // If this is a library project not being analyzed, ignore it
                    return;
                }

List<String> to = mIncludes.get(context.file);
if (to == null) {
to = new ArrayList<String>();








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java
//Synthetic comment -- index 9d9cf14..1e4c450 100644

//Synthetic comment -- @@ -335,6 +335,11 @@

@Override
public void afterCheckLibraryProject(@NonNull Context context) {
        if (!context.getProject().getReportIssues()) {
            // If this is a library project not being analyzed, ignore it
            return;
        }

checkResourceFolder(context, context.getProject());
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/MergeRootFrameLayoutDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/MergeRootFrameLayoutDetector.java
//Synthetic comment -- index 8769893..1b76c03 100644

//Synthetic comment -- @@ -23,9 +23,9 @@
import static com.android.SdkConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.SdkConstants.DOT_JAVA;
import static com.android.SdkConstants.FRAME_LAYOUT;
import static com.android.SdkConstants.LAYOUT_RESOURCE_PREFIX;
import static com.android.SdkConstants.R_LAYOUT_RESOURCE_PREFIX;
import static com.android.SdkConstants.VIEW_INCLUDE;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -167,6 +167,11 @@
Handle handle = context.parser.createLocationHandle(context, element);
handle.setClientData(element);

                if (!context.getProject().getReportIssues()) {
                    // If this is a library project not being analyzed, ignore it
                    return;
                }

if (mPending == null) {
mPending = new ArrayList<Pair<String,Handle>>();
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/MissingClassDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/MissingClassDetector.java
//Synthetic comment -- index 891a8af..2bcde0e 100644

//Synthetic comment -- @@ -170,6 +170,11 @@
return;
}

        if (!context.getProject().getReportIssues()) {
            // If this is a library project not being analyzed, ignore it
            return;
        }

if (mReferencedClasses == null) {
mReferencedClasses = Maps.newHashMapWithExpectedSize(16);
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ObsoleteLayoutParamsDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ObsoleteLayoutParamsDetector.java
//Synthetic comment -- index 08d53a3..24eb7ba 100644

//Synthetic comment -- @@ -55,12 +55,12 @@
import static com.android.SdkConstants.ATTR_LAYOUT_Y;
import static com.android.SdkConstants.DOT_XML;
import static com.android.SdkConstants.GRID_LAYOUT;
import static com.android.SdkConstants.LAYOUT_RESOURCE_PREFIX;
import static com.android.SdkConstants.LINEAR_LAYOUT;
import static com.android.SdkConstants.RELATIVE_LAYOUT;
import static com.android.SdkConstants.TABLE_ROW;
import static com.android.SdkConstants.VIEW_INCLUDE;
import static com.android.SdkConstants.VIEW_MERGE;
import static com.android.SdkConstants.VIEW_TAG;

import com.android.annotations.NonNull;
//Synthetic comment -- @@ -306,6 +306,11 @@
if (parent.getNodeType() == Node.ELEMENT_NODE) {
String tag = parent.getNodeName();
if (tag.indexOf('.') == -1 && !tag.equals(VIEW_MERGE)) {
                    if (!context.getProject().getReportIssues()) {
                        // If this is a library project not being analyzed, ignore it
                        return;
                    }

if (mIncludes == null) {
mIncludes = new HashMap<String, List<Pair<File, String>>>();
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/OnClickDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/OnClickDetector.java
//Synthetic comment -- index 24dcf02..5b2f3f7 100644

//Synthetic comment -- @@ -134,6 +134,11 @@
context.report(ISSUE, attribute, context.getLocation(attribute),
"There should be no whitespace around attribute values", null);
} else if (!value.startsWith(PREFIX_RESOURCE_REF)) { // Not resolved
            if (!context.getProject().getReportIssues()) {
                // If this is a library project not being analyzed, ignore it
                return;
            }

if (mNames == null) {
mNames = new HashMap<String, Location.Handle>();
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/OverdrawDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/OverdrawDetector.java
//Synthetic comment -- index 2c3999d..4994271 100644

//Synthetic comment -- @@ -289,6 +289,11 @@
return;
}

            if (!context.getProject().getReportIssues()) {
                // If this is a library project not being analyzed, ignore it
                return;
            }

Location location = context.getLocation(attribute);
location.setClientData(attribute);
if (mRootAttributes == null) {
//Synthetic comment -- @@ -472,6 +477,9 @@

@Override
public AstVisitor createJavaVisitor(@NonNull JavaContext context) {
        if (!context.getProject().getReportIssues()) {
            return null;
        }
return new OverdrawVisitor();
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/OverrideDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/OverrideDetector.java
//Synthetic comment -- index 041536f..26f8d38 100644

//Synthetic comment -- @@ -215,6 +215,11 @@
@SuppressWarnings("rawtypes") // ASM4 API
@Override
public void checkClass(@NonNull ClassContext context, @NonNull ClassNode classNode) {
        if (!context.getProject().getReportIssues()) {
            // If this is a library project not being analyzed, ignore it
            return;
        }

List methodList = classNode.methods;
if (context.getPhase() == 1) {
for (Object m : methodList) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/PrivateKeyDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/PrivateKeyDetector.java
//Synthetic comment -- index b1c0bd7..ba7c29f 100644

//Synthetic comment -- @@ -16,9 +16,6 @@

package com.android.tools.lint.checks;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
//Synthetic comment -- @@ -30,6 +27,8 @@
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
//Synthetic comment -- @@ -98,6 +97,11 @@

@Override
public void afterCheckProject(@NonNull Context context) {
        if (!context.getProject().getReportIssues()) {
            // If this is a library project not being analyzed, ignore it
            return;
        }

Project project = context.getProject();
File projectFolder = project.getDir();

//Synthetic comment -- @@ -105,7 +109,7 @@
checkFolder(context, new File(projectFolder, "assets"));

for (File srcFolder : project.getJavaSourceFolders()) {
            checkFolder(context, srcFolder);
}
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/RequiredAttributeDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/RequiredAttributeDetector.java
//Synthetic comment -- index 9fe8440..cbe2c02 100644

//Synthetic comment -- @@ -359,6 +359,11 @@
return;
}

                if (!context.getProject().getReportIssues()) {
                    // If this is a library project not being analyzed, ignore it
                    return;
                }

boolean certain = true;
boolean isRoot = isRootElement(element);
if (isRoot || isRootElement(element.getParentNode())








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/StringFormatDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/StringFormatDetector.java
//Synthetic comment -- index 28a3f3d..670f095 100644

//Synthetic comment -- @@ -298,6 +298,11 @@
}

if (found && name != null) {
            if (!context.getProject().getReportIssues()) {
                // If this is a library project not being analyzed, ignore it
                return;
            }

// Record it for analysis when seen in Java code
if (mFormatStrings == null) {
mFormatStrings = new HashMap<String, List<Pair<Handle,String>>>();








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/WakelockDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/WakelockDetector.java
//Synthetic comment -- index 65759ea..a8ce2f2 100644

//Synthetic comment -- @@ -112,6 +112,11 @@
public void checkCall(@NonNull ClassContext context, @NonNull ClassNode classNode,
@NonNull MethodNode method, @NonNull MethodInsnNode call) {
if (call.owner.equals(WAKELOCK_OWNER)) {
            if (!context.getProject().getReportIssues()) {
                // If this is a library project not being analyzed, ignore it
                return;
            }

String name = call.name;
if (name.equals(ACQUIRE_METHOD)) {
mHasAcquire = true;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/WrongIdDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/WrongIdDetector.java
//Synthetic comment -- index 7efbfbf..e8a1bbf 100644

//Synthetic comment -- @@ -156,6 +156,11 @@
@Override
public void afterCheckFile(@NonNull Context context) {
if (mRelativeLayouts != null) {
            if (!context.getProject().getReportIssues()) {
                // If this is a library project not being analyzed, ignore it
                return;
            }

for (Element layout : mRelativeLayouts) {
NodeList children = layout.getChildNodes();
for (int j = 0, childCount = children.getLength(); j < childCount; j++) {







