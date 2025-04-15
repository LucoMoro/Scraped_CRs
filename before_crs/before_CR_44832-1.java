/*Add lint support for checking frameworks/base/core

This changeset adds basic support for running lint against
the AOSP frameworks/base/core codebase.

It also makes the resource folder provided by the lint
client.

Change-Id:I7e38b0925cb032f776c54f975b924b91d6ab7a24*/
//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintClient.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintClient.java
//Synthetic comment -- index 2e21c06..2a68ddf 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import static com.android.SdkConstants.DOT_JAR;
import static com.android.SdkConstants.GEN_FOLDER;
import static com.android.SdkConstants.LIBS_FOLDER;
import static com.android.SdkConstants.SRC_FOLDER;

import com.android.SdkConstants;
//Synthetic comment -- @@ -233,6 +234,23 @@
}

/**
* Returns the {@link SdkInfo} to use for the given project.
*
* @param project the project to look up an {@link SdkInfo} for








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index aad1ad7..415c305 100644

//Synthetic comment -- @@ -819,8 +819,8 @@
if (files != null) {
checkIndividualResources(project, main, xmlDetectors, files);
} else {
                        File res = new File(project.getDir(), RES_FOLDER);
                        if (res.exists() && xmlDetectors.size() > 0) {
checkResFolder(project, main, res, xmlDetectors);
}
}
//Synthetic comment -- @@ -1664,6 +1664,12 @@

@Override
@Nullable
public IDomParser getDomParser() {
return mDelegate.getDomParser();
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintUtils.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintUtils.java
//Synthetic comment -- index 60c9e97..c0d5a85 100644

//Synthetic comment -- @@ -668,6 +668,9 @@
return false;
}
}
}

return hasManifest;








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Project.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Project.java
//Synthetic comment -- index b584020..eb41807 100644

//Synthetic comment -- @@ -25,9 +25,11 @@
import static com.android.SdkConstants.ATTR_TARGET_SDK_VERSION;
import static com.android.SdkConstants.PROGUARD_CONFIG;
import static com.android.SdkConstants.PROJECT_PROPERTIES;
import static com.android.SdkConstants.TAG_USES_SDK;
import static com.android.SdkConstants.VALUE_TRUE;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.client.api.Configuration;
//Synthetic comment -- @@ -247,6 +249,9 @@
@NonNull
public List<File> getJavaSourceFolders() {
if (mJavaSourceFolders == null) {
if (isAospBuildEnvironment()) {
String top = getAospTop();
if (mDir.getAbsolutePath().startsWith(top)) {
//Synthetic comment -- @@ -268,6 +273,21 @@
@NonNull
public List<File> getJavaClassFolders() {
if (mJavaClassFolders == null) {
if (isAospBuildEnvironment()) {
String top = getAospTop();
if (mDir.getAbsolutePath().startsWith(top)) {
//Synthetic comment -- @@ -304,6 +324,28 @@
}

/**
* Returns the relative path of a given file relative to the user specified
* directory (which is often the project directory but sometimes a higher up
* directory when a directory tree is being scanned
//Synthetic comment -- @@ -643,6 +685,31 @@
return sAospBuild.booleanValue();
}

/** Get the root AOSP dir, if any */
private static String getAospTop() {
return System.getenv("ANDROID_BUILD_TOP");   //$NON-NLS-1$








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java
//Synthetic comment -- index 2f416b5..9d9cf14 100644

//Synthetic comment -- @@ -32,7 +32,6 @@
import static com.android.SdkConstants.DRAWABLE_PREFIX;
import static com.android.SdkConstants.DRAWABLE_XHDPI;
import static com.android.SdkConstants.MENU_TYPE;
import static com.android.SdkConstants.RES_FOLDER;
import static com.android.SdkConstants.R_CLASS;
import static com.android.SdkConstants.R_DRAWABLE_PREFIX;
import static com.android.SdkConstants.TAG_ACTIVITY;
//Synthetic comment -- @@ -53,6 +52,7 @@
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
//Synthetic comment -- @@ -335,17 +335,17 @@

@Override
public void afterCheckLibraryProject(@NonNull Context context) {
        checkResourceFolder(context, context.getProject().getDir());
}

@Override
public void afterCheckProject(@NonNull Context context) {
        checkResourceFolder(context, context.getProject().getDir());
}

    private void checkResourceFolder(Context context, File dir) {
        File res = new File(dir, RES_FOLDER);
        if (res.isDirectory()) {
File[] folders = res.listFiles();
if (folders != null) {
boolean checkFolders = context.isEnabled(ICON_DENSITIES)








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/UnusedResourceDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/UnusedResourceDetector.java
//Synthetic comment -- index 1b4163e..5a3f174 100644

//Synthetic comment -- @@ -26,7 +26,6 @@
import static com.android.SdkConstants.RESOURCE_CLR_STYLEABLE;
import static com.android.SdkConstants.RESOURCE_CLZ_ARRAY;
import static com.android.SdkConstants.RESOURCE_CLZ_ID;
import static com.android.SdkConstants.RES_FOLDER;
import static com.android.SdkConstants.R_ATTR_PREFIX;
import static com.android.SdkConstants.R_CLASS;
import static com.android.SdkConstants.R_ID_PREFIX;
//Synthetic comment -- @@ -261,8 +260,11 @@
if (type != null && LintUtils.isFileBasedResourceType(type)) {
String name = resource.substring(secondDot + 1);

                        File res = new File(context.getProject().getDir(), RES_FOLDER);
                        File[] folders = res.listFiles();
if (folders != null) {
// Process folders in alphabetical order such that we process
// based folders first: we want the locations in base folder







