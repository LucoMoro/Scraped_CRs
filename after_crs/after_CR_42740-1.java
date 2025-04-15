/*Add unit test for lint CLI

Change-Id:I8434515179b8fd2ee3872506fc237c467a98db44*/




//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/IssueRegistry.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/IssueRegistry.java
//Synthetic comment -- index cf45b2e..e780d79 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.annotations.VisibleForTesting;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
//Synthetic comment -- @@ -238,4 +239,15 @@
}
return sIdToIssue.get(id);
}

    /**
     * Reset the registry such that it recomputes its available issues.
     * <p>
     * NOTE: This is only intended for testing purposes.
     */
    @VisibleForTesting
    protected static void reset() {
        sIdToIssue = null;
        sCategories = null;
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 8915e81..ca73df0 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import static com.android.tools.lint.detector.api.LintUtils.endsWith;

import com.android.annotations.NonNull;
import com.android.annotations.VisibleForTesting;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.tools.lint.client.api.IssueRegistry;
//Synthetic comment -- @@ -319,4 +320,14 @@

return sAdtFixes.contains(issue);
}

    /**
     * Reset the registry such that it recomputes its available issues.
     * <p>
     * NOTE: This is only intended for testing purposes.
     */
    @VisibleForTesting
    public static void reset() {
        IssueRegistry.reset();
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/MainTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/MainTest.java
//Synthetic comment -- index d4fd069..6b4ca31 100644

//Synthetic comment -- @@ -16,10 +16,25 @@

package com.android.tools.lint;

import com.android.tools.lint.checks.AbstractCheckTest;
import com.android.tools.lint.checks.AccessibilityDetector;
import com.android.tools.lint.checks.BuiltinIssueRegistry;
import com.android.tools.lint.detector.api.Detector;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.security.Permission;
import java.util.List;

@SuppressWarnings("javadoc")
public class MainTest extends AbstractCheckTest {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        BuiltinIssueRegistry.reset();
    }

public void testWrap() {
String s =
"Hardcoding text attributes directly in layout files is bad for several reasons:\n" +
//Synthetic comment -- @@ -65,4 +80,143 @@
"    adding new translations for existing string resources.\n",
wrapped);
}

    protected String checkLint(String[] args, List<File> files) throws Exception {
        PrintStream previousOut = System.out;
        try {
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            System.setOut(new PrintStream(output));

            Main.main(args);

            return output.toString();
        } finally {
            System.setOut(previousOut);
        }
    }

    private void checkDriver(String expectedOutput, String expectedError, String[] args)
            throws Exception {
        PrintStream previousOut = System.out;
        PrintStream previousErr = System.err;
        try {
            // Trap System.exit calls:
            System.setSecurityManager(new SecurityManager() {
                @Override
                public void checkPermission(Permission perm)
                {
                        // allow anything.
                }
                @Override
                public void checkPermission(Permission perm, Object context)
                {
                        // allow anything.
                }
                @Override
                public void checkExit(int status) {
                    throw new ExitException();
                }
            });

            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            System.setOut(new PrintStream(output));
            final ByteArrayOutputStream error = new ByteArrayOutputStream();
            System.setErr(new PrintStream(error));

            try {
                Main.main(args);
            } catch (ExitException e) {
                // Allow
            }

            assertEquals(expectedError, cleanup(error.toString()));
            assertEquals(expectedOutput, cleanup(output.toString()));
        } finally {
            // Re-enable system exit for unit test
            System.setSecurityManager(null);

            System.setOut(previousOut);
            System.setErr(previousErr);
        }
    }


    public void testArguments() throws Exception {
        checkDriver(
        // Expected output
        "\n" +
        "Scanning MainTest_testArguments: .\n" +
        "res/layout/accessibility.xml:4: Warning: [Accessibility] Missing contentDescription attribute on image [ContentDescription]\n" +
        "    <ImageView android:id=\"@+id/android_logo\" android:layout_width=\"wrap_content\" android:layout_height=\"wrap_content\" android:src=\"@drawable/android_button\" android:focusable=\"false\" android:clickable=\"false\" android:layout_weight=\"1.0\" />\n" +
        "    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
        "res/layout/accessibility.xml:5: Warning: [Accessibility] Missing contentDescription attribute on image [ContentDescription]\n" +
        "    <ImageButton android:importantForAccessibility=\"yes\" android:id=\"@+id/android_logo2\" android:layout_width=\"wrap_content\" android:layout_height=\"wrap_content\" android:src=\"@drawable/android_button\" android:focusable=\"false\" android:clickable=\"false\" android:layout_weight=\"1.0\" />\n" +
        "    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
        "0 errors, 2 warnings\n",

        // Expected error
        "",

        // Args
        new String[] {
                "--check",
                "ContentDescription",
                "--disable",
                "LintError",
                getProjectDir(null, "res/layout/accessibility.xml").getPath()

        });
    }

    public void testShowDescription() throws Exception {
        checkDriver(
        // Expected output
        "NewApi\n" +
        "------\n" +
        "Summary: Finds API accesses to APIs that are not supported in all targeted API\n" +
        "versions\n" +
        "\n" +
        "Priority: 6 / 10\n" +
        "Severity: Error\n" +
        "Category: Correctness\n" +
        "\n" +
        "This check scans through all the Android API calls in the application and\n" +
        "warns about any calls that are not available on all versions targeted by this\n" +
        "application (according to its minimum SDK attribute in the manifest).\n" +
        "\n" +
        "If you really want to use this API and don't need to support older devices\n" +
        "just set the minSdkVersion in your AndroidManifest.xml file.\n" +
        "If your code is deliberately accessing newer APIs, and you have ensured (e.g.\n" +
        "with conditional execution) that this code will only ever be called on a\n" +
        "supported platform, then you can annotate your class or method with the\n" +
        "@TargetApi annotation specifying the local minimum SDK to apply, such as\n" +
        "@TargetApi(11), such that this check considers 11 rather than your manifest\n" +
        "file's minimum SDK as the required API level.\n" +
        "\n" +
        "\n",

        // Expected error
        "",

        // Args
        new String[] {
                "--show",
                "NewApi"
        });
    }


    @Override
    protected Detector getDetector() {
        // Sample issue to check by the main driver
        return new AccessibilityDetector();
    }

    private static class ExitException extends SecurityException {
        private static final long serialVersionUID = 1L;

        private ExitException() {
            super("Unit test");
        }
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/AbstractCheckTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/AbstractCheckTest.java
//Synthetic comment -- index de5b65e..8469644 100644

//Synthetic comment -- @@ -188,7 +188,8 @@

private StringBuilder mOutput = null;

    protected static File sTempDir = null;

protected File getTempDir() {
if (sTempDir == null) {
File base = new File(System.getProperty("java.io.tmpdir"));     //$NON-NLS-1$
//Synthetic comment -- @@ -286,6 +287,23 @@
return false;
}

    protected static String cleanup(String result) throws IOException {
        if (sTempDir != null && result.contains(sTempDir.getPath())) {
            result = result.replace(sTempDir.getCanonicalFile().getPath(), "/TESTROOT");
            result = result.replace(sTempDir.getAbsoluteFile().getPath(), "/TESTROOT");
            result = result.replace(sTempDir.getPath(), "/TESTROOT");
        }

        // The output typically contains a few directory/filenames.
        // On Windows we need to change the separators to the unix-style
        // forward slash to make the test as OS-agnostic as possible.
        if (File.separatorChar != '/') {
            result = result.replace(File.separatorChar, '/');
        }

        return result;
    }

public class TestLintClient extends Main {
private StringWriter mWriter = new StringWriter();








