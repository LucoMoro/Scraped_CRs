/*Findbugs cleanup

Fix a couple of issues found by findbugs,
and some test stability fixes.

Change-Id:I97390ea606ea25d6a68e5b7f8245e5e689117995*/
//Synthetic comment -- diff --git a/common/src/com/android/utils/PositionXmlParser.java b/common/src/com/android/utils/PositionXmlParser.java
//Synthetic comment -- index 1eae641..73574d5 100644

//Synthetic comment -- @@ -208,7 +208,7 @@
int prologueStart = -1;
for (int lineEnd = offset; lineEnd < data.length; lineEnd++) {
if (data[lineEnd] == 0) {
                if ((lineEnd - offset) % 1 == 0) {
seenEvenZero = true;
} else {
seenOddZero = true;
//Synthetic comment -- @@ -255,7 +255,7 @@

// No prologue on the first line, and no byte order mark: Assume UTF-8/16
if (charset == null) {
            charset = seenOddZero ? UTF_16 : seenEvenZero ? UTF_16LE : UTF_8;
}

String xml = null;








//Synthetic comment -- diff --git a/common/tests/src/com/android/utils/PositionXmlParserTest.java b/common/tests/src/com/android/utils/PositionXmlParserTest.java
//Synthetic comment -- index 7d5b78b..18eda43 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.utils;

import com.android.utils.PositionXmlParser;
import com.android.utils.PositionXmlParser.Position;

import org.w3c.dom.Attr;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeRenameParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeRenameParticipant.java
//Synthetic comment -- index 383fd13..759879a 100644

//Synthetic comment -- @@ -134,7 +134,7 @@
} else {
mNewFqcn = getArguments().getNewName();
}
            if (mOldFqcn == null || mOldFqcn == null) {
return false;
}
if (!RefactoringUtil.isRefactorAppPackage() && mNewFqcn.indexOf('.') == -1) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplatePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplatePage.java
//Synthetic comment -- index 1a2d3ca..57cf5c8 100644

//Synthetic comment -- @@ -870,7 +870,6 @@
p.type == Parameter.Type.SEPARATOR) {
continue;
}
                p.suggest.indexOf(id);
if (!p.suggest.contains(id)) {
continue;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RefactoringTestBase.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RefactoringTestBase.java
//Synthetic comment -- index 8b7b817..1a97b83 100644

//Synthetic comment -- @@ -55,6 +55,11 @@
checkRefactoring(refactoring, expected, null);
}

protected void checkRefactoring(Refactoring refactoring, String expected,
@Nullable String expectedWarnings) throws Exception {
RefactoringStatus status = refactoring.checkAllConditions(new NullProgressMonitor());
//Synthetic comment -- @@ -62,7 +67,16 @@
if (expectedWarnings == null) {
expectedWarnings = "<OK\n>";
}
        assertEquals(status.toString().trim(), expectedWarnings.trim());
if (!status.isOK()) {
return;
}
//Synthetic comment -- @@ -75,9 +89,17 @@
}
}

protected IProject createProject(Object[] testData) throws Exception {
String name = getName();
IProject project = createProject(name);
File projectDir = AdtUtils.getAbsolutePath(project).toFile();
assertNotNull(projectDir);
assertTrue(projectDir.getPath(), projectDir.exists());








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintUtils.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintUtils.java
//Synthetic comment -- index 44c0dc2..17ea37e 100644

//Synthetic comment -- @@ -508,7 +508,7 @@
boolean seenEvenZero = false;
for (int lineEnd = offset; lineEnd < data.length; lineEnd++) {
if (data[lineEnd] == 0) {
                if ((lineEnd - offset) % 1 == 0) {
seenEvenZero = true;
} else {
seenOddZero = true;
//Synthetic comment -- @@ -519,7 +519,7 @@
}

if (charset == null) {
            charset = seenOddZero ? UTF_16 : seenEvenZero ? UTF_16LE : UTF_8;
}

String text = null;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index 323ec37..aff9705 100644

//Synthetic comment -- @@ -814,13 +814,6 @@
Object value = annotation.values.get(i + 1);
if (value instanceof Integer) {
return ((Integer) value).intValue();
                                } else if (value instanceof List) {
                                    List list = (List) value;
                                    for (Object v : list) {
                                        if (v instanceof Integer) {
                                            return ((Integer) value).intValue();
                                        }
                                    }
}
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ExtraTextDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ExtraTextDetector.java
//Synthetic comment -- index 7e2ecb2..f76b5cc 100644

//Synthetic comment -- @@ -63,7 +63,7 @@
public boolean appliesTo(@NonNull ResourceFolderType folderType) {
return folderType == ResourceFolderType.LAYOUT
|| folderType == ResourceFolderType.MENU
                || folderType == ResourceFolderType.ANIMATOR
|| folderType == ResourceFolderType.ANIMATOR
|| folderType == ResourceFolderType.DRAWABLE
|| folderType == ResourceFolderType.COLOR;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/LocaleDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/LocaleDetector.java
//Synthetic comment -- index 1b34494..e28603b 100644

//Synthetic comment -- @@ -143,10 +143,10 @@
|| desc.equals("()V")                                           //$NON-NLS-1$
|| desc.equals("(Ljava/lang/String;)V")) {                      //$NON-NLS-1$
Location location = context.getLocation(call);
                String message = String.format(
"To get local formatting use getDateInstance(), getDateTimeInstance(), " +
"or getTimeInstance(), or use new SimpleDateFormat(String template, " +
                    "Locale locale) with for example Locale.US for ASCII dates.", name);
context.report(DATE_FORMAT, method, call, location, message, null);
}
return;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ManifestOrderDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ManifestOrderDetector.java
//Synthetic comment -- index 71b5191..16bca84 100644

//Synthetic comment -- @@ -451,9 +451,9 @@
if (!element.hasAttributeNS(ANDROID_URI, SdkConstants.ATTR_ALLOW_BACKUP)
&& context.isEnabled(ALLOW_BACKUP)) {
context.report(ALLOW_BACKUP, element, context.getLocation(element),
                        String.format("Should explicitly set android:allowBackup to true or " +
"false (it's true by default, and that can have some security " +
                            "implications for the application's data)", tag), null);
}
} else if (mSeenApplication) {
if (context.isEnabled(ORDER)) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/MissingClassDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/MissingClassDetector.java
//Synthetic comment -- index 2bcde0e..3be7815 100644

//Synthetic comment -- @@ -192,8 +192,8 @@
}
}
if (!haveUpperCase) {
                    String message = String.format("Use '$' instead of '.' for inner classes " +
                            "(or use only lowercase letters in package names)", className);
Location location = context.getLocation(classNameNode);
context.report(INNERCLASS, element, location, message, null);
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/OverrideDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/OverrideDetector.java
//Synthetic comment -- index 26f8d38..7e6d440 100644

//Synthetic comment -- @@ -256,7 +256,7 @@

String signature = method.name + method.desc;
if (methods.containsKey(signature)){
                    if (method != null && context.getDriver().isSuppressed(ISSUE, classNode,
method, null)) {
Map<String, String> errors = mErrors.get(classNode.name);
if (errors != null) {







