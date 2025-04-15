/*Add lint check to make sure SharedPreference editors are committed

Change-Id:Ifacc8df1ef9169c57725652ae9d921ed2b3129bd*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index b62f34a..ad359ce 100644

//Synthetic comment -- @@ -53,7 +53,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 88;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -144,6 +144,7 @@
issues.add(JavaPerformanceDetector.USE_SPARSEARRAY);
issues.add(SetJavaScriptEnabledDetector.ISSUE);
issues.add(ToastDetector.ISSUE);

assert initialCapacity >= issues.size() : issues.size();









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/SharedPrefsDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/SharedPrefsDetector.java
new file mode 100644
//Synthetic comment -- index 0000000..525ab6d

//Synthetic comment -- @@ -0,0 +1,172 @@








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ToastDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ToastDetector.java
//Synthetic comment -- index c97027f..f199438 100644

//Synthetic comment -- @@ -54,7 +54,8 @@
ToastDetector.class,
Scope.JAVA_FILE_SCOPE);

    /** Constructs a new {@link FieldGetterDetector} check */
public ToastDetector() {
}

//Synthetic comment -- @@ -94,6 +95,10 @@
@Override
public void visitMethod(JavaContext context, AstVisitor visitor, MethodInvocation node) {
assert node.astName().astValue().equals("makeText");

String operand = node.astOperand().toString();
if (!(operand.equals("Toast") || operand.endsWith(".Toast"))) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SharedPrefsDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SharedPrefsDetectorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..0f50d59

//Synthetic comment -- @@ -0,0 +1,35 @@








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/SharedPrefsTest.java.txt b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/SharedPrefsTest.java.txt
new file mode 100644
//Synthetic comment -- index 0000000..e6c7300

//Synthetic comment -- @@ -0,0 +1,64 @@







