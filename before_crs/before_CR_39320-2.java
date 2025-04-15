/*35049: @SuppressLint("NewApi") doesn't work on local variables

The @SuppressLint annotation can deliberately be placed on not only
classes and methods but on parameters and local variables too.

For AST-based (Java source-based) lint checks, this works fine.

However, some lint checks, such as the API Check, is based on
analyzing the bytecode. Annotations placed on local variables and
parameters do not make it into the .class file, so these annotations
do not work to suppress errors when placed on local variables (or
parameters).

The @TargetApi annotation applies only to the bytecode based API
check, so its metadata only allows the annotation to be placed on
methods and classes and constructors. However, the @SuppressLint
annotation needs to continue to be available for the AST-based checks.

This CL adds a new lint check, a "meta" check, which actually looks
for invalid @SuppressLint annotations, and warns about these. With the
new lint-on-save behavior, this means you instantly get feedback if
you attempt to suppress an annotation in the wrong place.  (Note that
the quickfix for adding annotations has always enforced this and
placed annotations out at the method level, but as shown in issue
35049, developers place them there deliberately themselves.)

This CL also fixes an unrelated problem (shown in issue 34198) that
the add suppress annotation code could sometimes add multiple versions
of the same id into the annotation.

Change-Id:I5bc61c6315edfcfc20103d1e580e389dd8e6a09b*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAnnotation.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAnnotation.java
//Synthetic comment -- index 1280bc7..f755e1e 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.tools.lint.checks.ApiDetector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -174,10 +175,14 @@
} else {
Expression existingValue = existing.getValue();
if (existingValue instanceof StringLiteral) {
// Create a new array initializer holding the old string plus the new id
ArrayInitializer array = ast.newArrayInitializer();
StringLiteral old = ast.newStringLiteral();
                StringLiteral stringLiteral = (StringLiteral) existingValue;
old.setLiteralValue(stringLiteral.getLiteralValue());
array.expressions().add(old);
StringLiteral value = ast.newStringLiteral();
//Synthetic comment -- @@ -187,6 +192,17 @@
} else if (existingValue instanceof ArrayInitializer) {
// Existing array: just append the new string
ArrayInitializer array = (ArrayInitializer) existingValue;
StringLiteral value = ast.newStringLiteral();
value.setLiteralValue(mId);
ListRewrite listRewrite = rewriter.getListRewrite(array, EXPRESSIONS_PROPERTY);
//Synthetic comment -- @@ -282,6 +298,7 @@
if (document == null) {
return;
}
IWorkingCopyManager manager = JavaUI.getWorkingCopyManager();
ICompilationUnit compilationUnit = manager.getWorkingCopy(editorInput);
int offset = 0;
//Synthetic comment -- @@ -311,6 +328,11 @@
Issue issue = EclipseLintClient.getRegistry().getIssue(id);
boolean isClassDetector = issue != null && issue.getScope().contains(Scope.CLASS_FILE);

NodeFinder nodeFinder = new NodeFinder(root, offset, length);
ASTNode coveringNode;
if (offset <= 0) {








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index b4e4a7d..08a5c19 100644

//Synthetic comment -- @@ -175,6 +175,16 @@
}

/**
* Returns the project containing a given file, or null if not found. This searches
* only among the currently checked project and its library projects, not among all
* possible projects being scanned sequentially.








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/AnnotationDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/AnnotationDetector.java
new file mode 100644
//Synthetic comment -- index 0000000..aa0c84d

//Synthetic comment -- @@ -0,0 +1,169 @@








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 1dec5f6..dd1e973 100644

//Synthetic comment -- @@ -54,7 +54,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 100;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -157,6 +157,7 @@
issues.add(SharedPrefsDetector.ISSUE);
issues.add(NonInternationalizedSmsDetector.ISSUE);
issues.add(PrivateKeyDetector.ISSUE);

assert initialCapacity >= issues.size() : issues.size();









//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/AnnotationDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/AnnotationDetectorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..962b559

//Synthetic comment -- @@ -0,0 +1,54 @@








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/WrongAnnotation.java.txt b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/WrongAnnotation.java.txt
new file mode 100644
//Synthetic comment -- index 0000000..9256055

//Synthetic comment -- @@ -0,0 +1,19 @@







