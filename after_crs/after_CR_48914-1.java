/*Update fragment issue from warning to error

Also update constants to stay in sync with sdk/ version.

Change-Id:I8e454a03d83c81036bf8cc73609babc96a9c7739*/




//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/FragmentDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/FragmentDetectorTest.java
//Synthetic comment -- index f732f71..0654ddc 100644

//Synthetic comment -- @@ -27,26 +27,25 @@

public void test() throws Exception {
assertEquals(
            "src/test/pkg/FragmentTest.java:10: Error: This fragment class should be public (test.pkg.FragmentTest.Fragment1) [ValidFragment]\n" +
" private static class Fragment1 extends Fragment {\n" +
" ^\n" +
            "src/test/pkg/FragmentTest.java:15: Error: This fragment inner class should be static (test.pkg.FragmentTest.Fragment2) [ValidFragment]\n" +
" public class Fragment2 extends Fragment {\n" +
" ^\n" +
            "src/test/pkg/FragmentTest.java:21: Error: The default constructor must be public [ValidFragment]\n" +
"  private Fragment3() {\n" +
"          ~~~~~~~~~\n" +
            "src/test/pkg/FragmentTest.java:26: Error: This fragment should provide a default constructor (a public constructor with no arguments) (test.pkg.FragmentTest.Fragment4) [ValidFragment]\n" +
" public static class Fragment4 extends Fragment {\n" +
"                     ~~~~~~~~~\n" +
            "src/test/pkg/FragmentTest.java:27: Error: Avoid non-default constructors in fragments: use a default constructor plus Fragment#setArguments(Bundle) instead [ValidFragment]\n" +
"  private Fragment4(int dummy) {\n" +
"          ~~~~~~~~~\n" +
            "src/test/pkg/FragmentTest.java:36: Error: Avoid non-default constructors in fragments: use a default constructor plus Fragment#setArguments(Bundle) instead [ValidFragment]\n" +
"  public Fragment5(int dummy) {\n" +
"         ~~~~~~~~~\n" +
            "6 errors, 0 warnings\n",

lintProject(
"bytecode/FragmentTest$Fragment1.class.data=>bin/classes/test/pkg/FragmentTest$Fragment1.class",








//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/MissingClassDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/MissingClassDetectorTest.java
//Synthetic comment -- index 27875e8..b3ff056 100644

//Synthetic comment -- @@ -373,4 +373,21 @@
"bytecode/OnClickActivity.class.data=>bin/classes/test/pkg/OnClickActivity.class"
));
}

    public void testFragments() throws Exception {
        // Ensure that we don't do instantiation checks here since they are handled by
        // the FragmentDetector
        assertEquals(
                "No warnings.",

                lintProject(
                        "bytecode/FragmentTest$Fragment1.class.data=>bin/classes/test/pkg/FragmentTest$Fragment1.class",
                        "bytecode/FragmentTest$Fragment2.class.data=>bin/classes/test/pkg/FragmentTest$Fragment2.class",
                        "bytecode/FragmentTest$Fragment3.class.data=>bin/classes/test/pkg/FragmentTest$Fragment3.class",
                        "bytecode/FragmentTest$Fragment4.class.data=>bin/classes/test/pkg/FragmentTest$Fragment4.class",
                        "bytecode/FragmentTest$Fragment5.class.data=>bin/classes/test/pkg/FragmentTest$Fragment5.class",
                        "bytecode/FragmentTest$Fragment6.class.data=>bin/classes/test/pkg/FragmentTest$Fragment6.class",
                        "bytecode/FragmentTest$NotAFragment.class.data=>bin/classes/test/pkg/FragmentTest$NotAFragment.class",
                        "bytecode/FragmentTest.java.txt=>src/test/pkg/FragmentTest.java"));
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index 0ff3a9a..dd268a5 100644

//Synthetic comment -- @@ -287,10 +287,13 @@
try {
projects = computeProjects(files);
} catch (CircularDependencyException e) {
mCurrentProject = e.getProject();
            if (mCurrentProject != null) {
                File file = e.getLocation().getFile();
                Context context = new Context(this, mCurrentProject, null, file);
                context.report(IssueRegistry.LINT_ERROR, e.getLocation(), e.getMessage(), null);
                mCurrentProject = null;
            }
return;
}
if (projects.isEmpty()) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/FragmentDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/FragmentDetector.java
//Synthetic comment -- index 932d307..f6ebcd6 100644

//Synthetic comment -- @@ -65,7 +65,7 @@

Category.CORRECTNESS,
6,
        Severity.ERROR,
FragmentDetector.class,
Scope.CLASS_FILE_SCOPE).setMoreInfo(
"http://developer.android.com/reference/android/app/Fragment.html#Fragment()"); //$NON-NLS-1$
//Synthetic comment -- @@ -145,6 +145,7 @@
}
} else if (!method.desc.contains("()")) { //$NON-NLS-1$
context.report(ISSUE, context.getLocation(method, classNode),
                            // TODO: Use separate issue for this which isn't an error
"Avoid non-default constructors in fragments: use a default constructor " +
"plus Fragment#setArguments(Bundle) instead",
null);







