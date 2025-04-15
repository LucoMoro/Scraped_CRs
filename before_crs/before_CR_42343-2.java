/*Add lint rule to find incorrect alias files

This helps catch errors likehttp://code.google.com/p/android/issues/detail?id=36821Change-Id:I1ceda3183d8b1e29510ba0be2523dc6eb9c7a23b*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 8e4ab9d..3c08e82 100644

//Synthetic comment -- @@ -54,7 +54,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 104;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -145,6 +145,7 @@
issues.add(TypoDetector.ISSUE);
issues.add(ViewTypeDetector.ISSUE);
issues.add(WrongImportDetector.ISSUE);
issues.add(ViewConstructorDetector.ISSUE);
issues.add(NamespaceDetector.CUSTOMVIEW);
issues.add(NamespaceDetector.UNUSED);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/WrongLocationDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/WrongLocationDetector.java
new file mode 100644
//Synthetic comment -- index 0000000..8a19e20

//Synthetic comment -- @@ -0,0 +1,68 @@








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/WrongLocationDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/WrongLocationDetectorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..f30a604

//Synthetic comment -- @@ -0,0 +1,43 @@







