/*Remove ExportedActivity check

Change-Id:I428628956c7790b5206d83310211411c4ea54c68*/




//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/SecurityDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/SecurityDetectorTest.java
//Synthetic comment -- index 1c293c3..198058d 100644

//Synthetic comment -- @@ -143,58 +143,6 @@
"src/test/pkg/WorldWriteableFile.java.txt=>src/test/pkg/WorldWriteableFile.java"));
}

public void testReceiver0() throws Exception {
// Activities that do not have intent-filters do not need warnings
assertEquals(








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 8d0f9ca..fa4e40e 100644

//Synthetic comment -- @@ -136,7 +136,6 @@
issues.add(ManifestOrderDetector.ILLEGAL_REFERENCE);
issues.add(SecurityDetector.EXPORTED_PROVIDER);
issues.add(SecurityDetector.EXPORTED_SERVICE);
issues.add(SecurityDetector.EXPORTED_RECEIVER);
issues.add(SecurityDetector.OPEN_PROVIDER);
issues.add(SecurityDetector.WORLD_READABLE);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SecurityDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/SecurityDetector.java
//Synthetic comment -- index bc8d47b..b6cc957 100644

//Synthetic comment -- @@ -102,20 +102,6 @@
SecurityDetector.class,
Scope.MANIFEST_SCOPE);

/** Exported receivers */
public static final Issue EXPORTED_RECEIVER = Issue.create(
"ExportedReceiver", //$NON-NLS-1$
//Synthetic comment -- @@ -211,8 +197,6 @@
checkGrantPermission(context, element);
} else if (tag.equals(TAG_PROVIDER)) {
checkProvider(context, element);
} else if (tag.equals(TAG_RECEIVER)) {
checkReceiver(context, element);
}
//Synthetic comment -- @@ -267,16 +251,6 @@
return false;
}

private static boolean isStandardReceiver(Element element) {
// Checks whether a broadcast receiver receives a standard Android action
for (Element child : LintUtils.getChildren(element)) {







