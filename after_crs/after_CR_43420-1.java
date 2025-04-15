/*Fix missing class detector to work for incremental manifest edits

The missing manifest detector provides multiple issues; both the
missing class detector (which requires not just manifest scope but
also class scope), as well as some checks to ensure that for example
inner classes are registered correctly, which only requires manifest
scope. Because of this second issue, the detector runs in incremental
manifest editing context, but the code to handle missing classes would
also run. The check is simple; don't check missing class references
unless we also have class scope.

Change-Id:Iff83c372abf2335e3d8e991740702d6094fd65bd*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/MissingClassDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/MissingClassDetector.java
//Synthetic comment -- index dac3b82..d4f4e95 100644

//Synthetic comment -- @@ -209,8 +209,9 @@

@Override
public void afterCheckProject(@NonNull Context context) {
        if (!context.getProject().isLibrary()
                && mReferencedClasses != null && !mReferencedClasses.isEmpty()
                && context.getDriver().getScope().contains(Scope.CLASS_FILE)) {
List<String> classes = new ArrayList<String>(mReferencedClasses.keySet());
Collections.sort(classes);
for (String owner : classes) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/AbstractCheckTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/AbstractCheckTest.java
//Synthetic comment -- index 8469644..8b06c4d 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Project;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
//Synthetic comment -- @@ -46,6 +47,7 @@
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import junit.framework.TestCase;
//Synthetic comment -- @@ -304,6 +306,10 @@
return result;
}

    protected EnumSet<Scope> getLintScope(List<File> file) {
        return null;
    }

public class TestLintClient extends Main {
private StringWriter mWriter = new StringWriter();

//Synthetic comment -- @@ -313,7 +319,7 @@

public String analyze(List<File> files) throws Exception {
mDriver = new LintDriver(new CustomIssueRegistry(), this);
            mDriver.analyze(files, getLintScope(files));

Collections.sort(mWarnings);









//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/MissingClassDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/MissingClassDetectorTest.java
//Synthetic comment -- index d8f18b6..cda68a1 100644

//Synthetic comment -- @@ -17,18 +17,29 @@
package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Scope;

import java.io.File;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

@SuppressWarnings("javadoc")
public class MissingClassDetectorTest extends AbstractCheckTest {
    private EnumSet<Scope> mScopes;

@Override
protected Detector getDetector() {
return new MissingClassDetector();
}

    @Override
    protected EnumSet<Scope> getLintScope(List<File> file) {
        return mScopes;
    }

public void test() throws Exception {
        mScopes = null;
assertEquals(
"AndroidManifest.xml:13: Error: Class referenced in the manifest, test.pkg.TestProvider, was not found in the project or the libraries [MissingRegistered]\n" +
"        <activity android:name=\".TestProvider\" />\n" +
//Synthetic comment -- @@ -53,7 +64,19 @@
));
}

    public void testIncrementalInManifest() throws Exception {
        mScopes = Scope.MANIFEST_SCOPE;
        assertEquals(
            "No warnings.",

            lintProject(
                "bytecode/AndroidManifestWrongRegs.xml=>AndroidManifest.xml",
                "bytecode/.classpath=>.classpath"
            ));
    }

public void testOkClasses() throws Exception {
        mScopes = null;
assertEquals(
"No warnings.",

//Synthetic comment -- @@ -74,6 +97,7 @@
}

public void testOkLibraries() throws Exception {
        mScopes = null;
assertEquals(
"No warnings.",

//Synthetic comment -- @@ -85,6 +109,7 @@
}

public void testLibraryProjects() throws Exception {
        mScopes = null;
File master = getProjectDir("MasterProject",
// Master project
"bytecode/AndroidManifestWrongRegs.xml=>AndroidManifest.xml",
//Synthetic comment -- @@ -115,6 +140,7 @@
}

public void testInnerClassStatic() throws Exception {
        mScopes = null;
assertEquals(
"src/test/pkg/Foo.java:8: Warning: This inner class should be static (test.pkg.Foo.Baz) [Instantiatable]\n" +
"    public class Baz extends Activity {\n" +
//Synthetic comment -- @@ -132,6 +158,7 @@
}

public void testInnerClassPublic() throws Exception {
        mScopes = null;
assertEquals(
"src/test/pkg/Foo/Bar.java:6: Warning: The default constructor must be public [Instantiatable]\n" +
"    private Bar() {\n" +
//Synthetic comment -- @@ -147,6 +174,7 @@
}

public void testInnerClass() throws Exception {
        mScopes = null;
assertEquals(
"AndroidManifest.xml:14: Error: Class referenced in the manifest, test.pkg.Foo.Bar, was not found in the project or the libraries [MissingRegistered]\n" +
"        <activity\n" +
//Synthetic comment -- @@ -164,6 +192,7 @@
}

public void testInnerClass2() throws Exception {
        mScopes = null;
assertEquals(
"AndroidManifest.xml:14: Error: Class referenced in the manifest, test.pkg.Foo.Bar, was not found in the project or the libraries [MissingRegistered]\n" +
"        <activity\n" +
//Synthetic comment -- @@ -178,6 +207,7 @@
}

public void testWrongSeparator1() throws Exception {
        mScopes = null;
assertEquals(
"AndroidManifest.xml:14: Error: Class referenced in the manifest, test.pkg.Foo.Bar, was not found in the project or the libraries [MissingRegistered]\n" +
"        <activity\n" +
//Synthetic comment -- @@ -192,6 +222,7 @@
}

public void testWrongSeparator2() throws Exception {
        mScopes = null;
assertEquals(
"AndroidManifest.xml:14: Error: Class referenced in the manifest, test.pkg.Foo.Bar, was not found in the project or the libraries [MissingRegistered]\n" +
"        <activity\n" +







