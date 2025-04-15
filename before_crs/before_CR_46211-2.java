/*Add lint detector finding duplicate resource definitions

Change-Id:I03d90622f0eedf8121c1a8c32600a5be6bfae1a4*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 147100d..9f0c064 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 122;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -66,6 +66,7 @@
issues.add(ApiDetector.UNSUPPORTED);
issues.add(DuplicateIdDetector.CROSS_LAYOUT);
issues.add(DuplicateIdDetector.WITHIN_LAYOUT);
issues.add(WrongIdDetector.UNKNOWN_ID);
issues.add(WrongIdDetector.UNKNOWN_ID_LAYOUT);
issues.add(StateListDetector.ISSUE);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/DuplicateResourceDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/DuplicateResourceDetector.java
new file mode 100644
//Synthetic comment -- index 0000000..fce4633

//Synthetic comment -- @@ -0,0 +1,166 @@








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/DuplicateResourceDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/DuplicateResourceDetectorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..a4a5a68

//Synthetic comment -- @@ -0,0 +1,63 @@







