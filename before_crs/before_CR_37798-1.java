/*Added detection code for malformed phone numbers that are passed to the SMS sending API functions

Change-Id:Iad5fd3e1e9c0add65da6717a601fad8ea2e9b84e*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 146da7f..b975ec1 100644

//Synthetic comment -- @@ -54,7 +54,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 92;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -149,6 +149,7 @@
issues.add(SetJavaScriptEnabledDetector.ISSUE);
issues.add(ToastDetector.ISSUE);
issues.add(SharedPrefsDetector.ISSUE);

assert initialCapacity >= issues.size() : issues.size();









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/NonInternationalizedSmsDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/NonInternationalizedSmsDetector.java
new file mode 100644
//Synthetic comment -- index 0000000..b9a7b79

//Synthetic comment -- @@ -0,0 +1,102 @@
\ No newline at end of file








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/NonInternationalizedSmsDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/NonInternationalizedSmsDetectorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..04cdfd3

//Synthetic comment -- @@ -0,0 +1,33 @@
\ No newline at end of file








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/NonInternationalizedSmsDetectorTest.java.txt b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/NonInternationalizedSmsDetectorTest.java.txt
new file mode 100644
//Synthetic comment -- index 0000000..6379749

//Synthetic comment -- @@ -0,0 +1,20 @@







