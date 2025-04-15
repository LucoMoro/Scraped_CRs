/*Add lint comment checker

Change-Id:I5ba847838e0035ab8ad44967779fdf814d1901a6*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 3a40115..e521803 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 126;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -105,6 +105,8 @@
issues.add(HardcodedValuesDetector.ISSUE);
issues.add(Utf8Detector.ISSUE);
issues.add(DosLineEndingDetector.ISSUE);
issues.add(ProguardDetector.WRONGKEEP);
issues.add(ProguardDetector.SPLITCONFIG);
issues.add(PxUsageDetector.PX_ISSUE);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/CommentDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/CommentDetector.java
new file mode 100644
//Synthetic comment -- index 0000000..0e5d8d0

//Synthetic comment -- @@ -0,0 +1,196 @@








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/CommentDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/CommentDetectorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..c352962

//Synthetic comment -- @@ -0,0 +1,53 @@








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/Hidden.java.txt b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/Hidden.java.txt
new file mode 100644
//Synthetic comment -- index 0000000..847cd88

//Synthetic comment -- @@ -0,0 +1,14 @@







