/*Remove ExportedActivity check.

Change-Id:I6c029bfdbae44974f057c4959b9dbc2563d1a2da*/




//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/SecurityDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/SecurityDetectorTest.java
//Synthetic comment -- index 1c293c3..198058d 100644

//Synthetic comment -- @@ -143,58 +143,6 @@
"src/test/pkg/WorldWriteableFile.java.txt=>src/test/pkg/WorldWriteableFile.java"));
}

public void testReceiver0() throws Exception {
// Activities that do not have intent-filters do not need warnings
assertEquals(








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 8d0f9ca..b19327c 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 137;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
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







