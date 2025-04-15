/*Update fragment issue from warning to error. DO NOT MERGE

Also update constants to stay in sync with sdk/ version.

Change-Id:I5803994bb307fe359a4899772186238393811d3e*/
//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/FragmentDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/FragmentDetectorTest.java
//Synthetic comment -- index f732f71..0654ddc 100644

//Synthetic comment -- @@ -27,26 +27,25 @@

public void test() throws Exception {
assertEquals(
            "src/test/pkg/FragmentTest.java:10: Warning: This fragment class should be public (test.pkg.FragmentTest.Fragment1) [ValidFragment]\n" +
" private static class Fragment1 extends Fragment {\n" +
" ^\n" +
            "src/test/pkg/FragmentTest.java:15: Warning: This fragment inner class should be static (test.pkg.FragmentTest.Fragment2) [ValidFragment]\n" +
" public class Fragment2 extends Fragment {\n" +
" ^\n" +
            "src/test/pkg/FragmentTest.java:21: Warning: The default constructor must be public [ValidFragment]\n" +
"  private Fragment3() {\n" +
"          ~~~~~~~~~\n" +
            "src/test/pkg/FragmentTest.java:26: Warning: This fragment should provide a default constructor (a public constructor with no arguments) (test.pkg.FragmentTest.Fragment4) [ValidFragment]\n" +
" public static class Fragment4 extends Fragment {\n" +
"                     ~~~~~~~~~\n" +
            "src/test/pkg/FragmentTest.java:27: Warning: Avoid non-default constructors in fragments: use a default constructor plus Fragment#setArguments(Bundle) instead [ValidFragment]\n" +
"  private Fragment4(int dummy) {\n" +
"          ~~~~~~~~~\n" +
            "src/test/pkg/FragmentTest.java:36: Warning: Avoid non-default constructors in fragments: use a default constructor plus Fragment#setArguments(Bundle) instead [ValidFragment]\n" +
"  public Fragment5(int dummy) {\n" +
"         ~~~~~~~~~\n" +
            "0 errors, 6 warnings\n" +
            "",

lintProject(
"bytecode/FragmentTest$Fragment1.class.data=>bin/classes/test/pkg/FragmentTest$Fragment1.class",








//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/MissingClassDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/MissingClassDetectorTest.java
//Synthetic comment -- index 27875e8..b3ff056 100644

//Synthetic comment -- @@ -373,4 +373,21 @@
"bytecode/OnClickActivity.class.data=>bin/classes/test/pkg/OnClickActivity.class"
));
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index 0ff3a9a..dd268a5 100644

//Synthetic comment -- @@ -287,10 +287,13 @@
try {
projects = computeProjects(files);
} catch (CircularDependencyException e) {
            Context context = new Context(this, e.getProject(), null, e.getLocation().getFile());
mCurrentProject = e.getProject();
            context.report(IssueRegistry.LINT_ERROR, e.getLocation(), e.getMessage(), null);
            mCurrentProject = null;
return;
}
if (projects.isEmpty()) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/FragmentDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/FragmentDetector.java
//Synthetic comment -- index 932d307..f6ebcd6 100644

//Synthetic comment -- @@ -65,7 +65,7 @@

Category.CORRECTNESS,
6,
        Severity.WARNING,
FragmentDetector.class,
Scope.CLASS_FILE_SCOPE).setMoreInfo(
"http://developer.android.com/reference/android/app/Fragment.html#Fragment()"); //$NON-NLS-1$
//Synthetic comment -- @@ -145,6 +145,7 @@
}
} else if (!method.desc.contains("()")) { //$NON-NLS-1$
context.report(ISSUE, context.getLocation(method, classNode),
"Avoid non-default constructors in fragments: use a default constructor " +
"plus Fragment#setArguments(Bundle) instead",
null);







