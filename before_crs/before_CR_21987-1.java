/*Test infrastructure fixes

Add methods which allow a test case to specify that it needs a unique
project for the testcase (or even for each individual test
method). This allows tests which modify the project (such as the
quickfix-resource-creation tests) to not cause side effects that
affect other tests (e.g. resources created by the quickfix could show
up in code completion results by the code completion tests, which
meant the goldenfiles would have to either include them or exclude
them, which meant the tests must always be run together or never run
together.

The changeset also adds an environment variable pointing to the git
working copy allowing tests with no golden file to create the golden
files directly. This makes it easy to add new test cases; just write
the test scenarios, run them, which will fail all the tests but also
write out the expected golden files. Verify them and check them in and
running the tests again should pass if the output is stable.

Change-Id:I2e2789c00c84a31a9fbc603851879d8d83342ad3*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/build/AaptQuickFixTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/build/AaptQuickFixTest.java
//Synthetic comment -- index cc4453b..08c075a 100644

//Synthetic comment -- @@ -49,6 +49,13 @@
import java.util.List;

public class AaptQuickFixTest extends AdtProjectTest {
public void testQuickFix1() throws Exception {
// Test adding a value into an existing file (res/values/strings.xml)
checkFixes("quickfix1.xml", "android:text=\"@string/firs^tstring\"",








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/AdtProjectTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/AdtProjectTest.java
//Synthetic comment -- index fb094ad..f271f96 100644

//Synthetic comment -- @@ -57,31 +57,73 @@
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("restriction")
public class AdtProjectTest extends SdkTestCase {
/**
     * Individual tests don't share an instance of the TestCase so we stash the test
     * project in a static field such that we don't need to keep recreating it -- should
     * be much faster.
*/
    protected static IProject sProject;

@Override
protected void setUp() throws Exception {
super.setUp();

        if (sProject == null) {
            IProject project = null;
            String projectName = "testproject-" + System.currentTimeMillis();
            project = createProject(projectName);
            assertNotNull(project);
            sProject = project;
}
}

protected IProject getProject() {
        return sProject;
}

protected IFile getTestDataFile(IProject project, String name) throws Exception {
//Synthetic comment -- @@ -328,7 +370,7 @@
+ "-expected-" + testName + '.' + newExtension;
String expected = readTestFile(expectedName, false);
if (expected == null) {
            File expectedPath = new File(getTempDir(), expectedName);
AdtPlugin.writeFile(expectedPath, actual);
System.out.println("Expected - written to " + expectedPath + ":\n");
System.out.println(actual);
//Synthetic comment -- @@ -339,7 +381,7 @@
File expectedPath = new File(getTempDir(), expectedName);
File actualPath = new File(getTempDir(),
expectedName.replace("expected", "actual"));
               AdtPlugin.writeFile(expectedPath, expected);
AdtPlugin.writeFile(actualPath, actual);
System.out.println("The files differ - see " + expectedPath + " versus "
+ actualPath);
//Synthetic comment -- @@ -349,6 +391,27 @@
}
}

protected File getTempDir() {
if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_DARWIN) {
return new File("/tmp"); //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeLayoutRefactoringTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeLayoutRefactoringTest.java
//Synthetic comment -- index 227e354..afb514d 100644

//Synthetic comment -- @@ -65,7 +65,7 @@
}

private void checkRefactoring(String basename, boolean flatten) throws Exception {
        IFile file = getLayoutFile(sProject, basename);
TestContext info = setupTestContext(file, basename);
TestLayoutEditor layoutEditor = info.mLayoutEditor;
CanvasViewInfo rootView = info.mRootView;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeViewRefactoringTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeViewRefactoringTest.java
//Synthetic comment -- index 610b48f..c24ce6a 100644

//Synthetic comment -- @@ -43,7 +43,7 @@
String... ids) throws Exception {
assertTrue(ids.length > 0);

        IFile file = getLayoutFile(sProject, basename);
TestContext info = setupTestContext(file, basename);
TestLayoutEditor layoutEditor = info.mLayoutEditor;
List<Element> selectedElements = getElements(info.mElement, ids);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractIncludeRefactoringTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractIncludeRefactoringTest.java
//Synthetic comment -- index 203a0bb..6a5839d 100644

//Synthetic comment -- @@ -84,7 +84,7 @@
int expectedModifiedFileCount, String... ids) throws Exception {
assertTrue(ids.length > 0);

        IFile file = getLayoutFile(sProject, basename);
TestContext info = setupTestContext(file, basename);
TestLayoutEditor layoutEditor = info.mLayoutEditor;
List<Element> selectedElements = getElements(info.mElement, ids);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/WrapInRefactoringTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/WrapInRefactoringTest.java
//Synthetic comment -- index 7c43443..4cf3ece 100644

//Synthetic comment -- @@ -44,7 +44,7 @@
private void checkRefactoring(String basename, String fqcn, String... ids) throws Exception {
assertTrue(ids.length > 0);

        IFile file = getLayoutFile(sProject, basename);
TestContext info = setupTestContext(file, basename);
TestLayoutEditor layoutEditor = info.mLayoutEditor;
List<Element> selectedElements = getElements(info.mElement, ids);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/xml/HyperlinksTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/xml/HyperlinksTest.java
//Synthetic comment -- index a9a66c2..1265f20 100644

//Synthetic comment -- @@ -41,7 +41,13 @@
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class HyperlinksTest extends AdtProjectTest {
public void testFqnRegexp() throws Exception {
assertTrue(Hyperlinks.CLASS_PATTERN.matcher("com.android.Foo").matches());
assertTrue(Hyperlinks.CLASS_PATTERN.matcher("com.android.pk_g.Foo_Bar1").
//Synthetic comment -- @@ -156,7 +162,6 @@
// class attributes
// Test that the correct file is actually opened!

    @SuppressWarnings("restriction")
private void checkXmlNavigation(String basename, String targetPath,
String caretLocation) throws Exception {
IFile file = getTestDataFile(getProject(), basename, targetPath, true);







