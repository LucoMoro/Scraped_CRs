/*Add unit test for lint CLI

Change-Id:I8434515179b8fd2ee3872506fc237c467a98db44*/
//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/IssueRegistry.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/IssueRegistry.java
//Synthetic comment -- index cf45b2e..e780d79 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
//Synthetic comment -- @@ -238,4 +239,15 @@
}
return sIdToIssue.get(id);
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 8915e81..ca73df0 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import static com.android.tools.lint.detector.api.LintUtils.endsWith;

import com.android.annotations.NonNull;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.tools.lint.client.api.IssueRegistry;
//Synthetic comment -- @@ -319,4 +320,14 @@

return sAdtFixes.contains(issue);
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/MainTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/MainTest.java
//Synthetic comment -- index d4fd069..6b4ca31 100644

//Synthetic comment -- @@ -16,10 +16,25 @@

package com.android.tools.lint;

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class MainTest extends TestCase {
public void testWrap() {
String s =
"Hardcoding text attributes directly in layout files is bad for several reasons:\n" +
//Synthetic comment -- @@ -65,4 +80,143 @@
"    adding new translations for existing string resources.\n",
wrapped);
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/AbstractCheckTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/AbstractCheckTest.java
//Synthetic comment -- index de5b65e..8469644 100644

//Synthetic comment -- @@ -188,7 +188,8 @@

private StringBuilder mOutput = null;

    private static File sTempDir = null;
protected File getTempDir() {
if (sTempDir == null) {
File base = new File(System.getProperty("java.io.tmpdir"));     //$NON-NLS-1$
//Synthetic comment -- @@ -286,6 +287,23 @@
return false;
}

public class TestLintClient extends Main {
private StringWriter mWriter = new StringWriter();








