/*Lint: Don't check match_parent versus fill_parent in api check

Those constants are tied to the build target, not the minimum
or target SDK versions (so compilation will already complain
if you're using a too old version.)

Change-Id:I54376eb7c44f749e1494ff8a39987ec1aa6117ad*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index 05b640d..43ea4ef 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.TARGET_API;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
//Synthetic comment -- @@ -135,20 +134,6 @@

String value = attribute.getValue();

if (value.startsWith(ANDROID_RESOURCE_PREFIX)) {
// Convert @android:type/foo into android/R$type and "foo"
int index = value.indexOf('/', ANDROID_RESOURCE_PREFIX.length());








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ApiDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ApiDetectorTest.java
//Synthetic comment -- index 01ab449..667c34a 100644

//Synthetic comment -- @@ -32,7 +32,6 @@
"layout.xml:21: Error: View requires API level 14 (current min is 1): <GridLayout>\n" +
"layout.xml:22: Error: @android:attr/actionBarSplitStyle requires API level 14 (current min is 1)\n" +
"layout.xml:23: Error: @android:color/holo_red_light requires API level 14 (current min is 1)\n" +
"layout.xml:9: Error: View requires API level 5 (current min is 1): <QuickContactBadge>\n" +
"themes.xml:9: Error: @android:color/holo_red_light requires API level 14 (current min is 1)",








