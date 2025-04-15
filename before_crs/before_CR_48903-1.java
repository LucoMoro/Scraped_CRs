/*Code cleanup

Fix typos, remove redundant semicolons, fix incorrect names
(using an m prefix where they should be using s, turn fields
with constant names into actual constants), etc.

Change-Id:I44a5ab82c1158007d0803d43ce14b361aff240b8*/
//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/LintCliXmlParserTest.java b/lint/cli/src/test/java/com/android/tools/lint/LintCliXmlParserTest.java
//Synthetic comment -- index a6f4aaf..9fa310f 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.tools.lint;

import com.android.tools.lint.checks.BuiltinIssueRegistry;
import com.android.tools.lint.client.api.LintClient;
import com.android.tools.lint.client.api.LintDriver;
//Synthetic comment -- @@ -154,8 +156,13 @@

private static class TestClient extends Main {
@Override
        public void report(Context context, Issue issue, Severity severity, Location location,
                String message, Object data) {
System.out.println(location + ":" + message);
}
}








//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/AbstractCheckTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/AbstractCheckTest.java
//Synthetic comment -- index 4f427a2..b73db8c 100644

//Synthetic comment -- @@ -348,7 +348,7 @@
}

@Override
        public Configuration getConfiguration(Project project) {
return AbstractCheckTest.this.getConfiguration(this, project);
}

//Synthetic comment -- @@ -434,18 +434,18 @@
}

@Override
        public boolean isEnabled(Issue issue) {
return AbstractCheckTest.this.isEnabled(issue);
}

@Override
        public void ignore(Context context, Issue issue, Location location, String message,
                Object data) {
fail("Not supported in tests.");
}

@Override
        public void setSeverity(Issue issue, Severity severity) {
fail("Not supported in tests.");
}
}








//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/ApiLookupTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/ApiLookupTest.java
//Synthetic comment -- index 252ec5e..eaba6e6 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Severity;

//Synthetic comment -- @@ -189,8 +191,11 @@
}

@Override
        public void log(Severity severity, Throwable exception, String format,
                Object... args) {
if (format != null) {
mLogBuffer.append(String.format(format, args));
mLogBuffer.append('\n');








//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/IconDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/IconDetectorTest.java
//Synthetic comment -- index e2c20ba..7cbbdc5 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.tools.lint.checks;

import com.android.tools.lint.client.api.LintClient;
import com.android.tools.lint.client.api.LintDriver;
import com.android.tools.lint.detector.api.Detector;
//Synthetic comment -- @@ -67,7 +68,7 @@
protected TestConfiguration getConfiguration(LintClient client, Project project) {
return new TestConfiguration(client, project, null) {
@Override
            public boolean isEnabled(Issue issue) {
return super.isEnabled(issue) && mEnabled.contains(issue);
}
};








//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/ManifestOrderDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/ManifestOrderDetectorTest.java
//Synthetic comment -- index e845b57..06e6fba 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.tools.lint.checks;

import com.android.tools.lint.client.api.LintClient;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
//Synthetic comment -- @@ -40,7 +41,7 @@
protected TestConfiguration getConfiguration(LintClient client, Project project) {
return new TestConfiguration(client, project, null) {
@Override
            public boolean isEnabled(Issue issue) {
return super.isEnabled(issue) && mEnabled.contains(issue);
}
};








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Location.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Location.java
//Synthetic comment -- index e255ede..34116d0 100644

//Synthetic comment -- @@ -648,7 +648,7 @@
*/
private boolean mConstructor;

        private SearchHints(SearchDirection direction) {
super();
mDirection = direction;
}
//Synthetic comment -- @@ -659,7 +659,8 @@
* @param direction the direction to search in for the pattern
* @return a new @link SearchHints} object
*/
        public static SearchHints create(SearchDirection direction) {
return new SearchHints(direction);
}

//Synthetic comment -- @@ -668,6 +669,7 @@

* @return this, for constructor chaining
*/
public SearchHints matchWholeWord() {
mWholeWord = true;

//Synthetic comment -- @@ -684,6 +686,7 @@
*
* @return this, for constructor chaining
*/
public SearchHints matchJavaSymbol() {
mJavaSymbol = true;
mWholeWord = true;
//Synthetic comment -- @@ -702,6 +705,7 @@
*
* @return this, for constructor chaining
*/
public SearchHints matchConstructor() {
mConstructor = true;
mWholeWord = true;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index 2c0d773..ab6e48a 100644

//Synthetic comment -- @@ -804,7 +804,7 @@
}

/**
     * Return the {@code @TargeTApi} level to use for the given {@code classNode};
* this will be the {@code @TargetApi} annotation on the class, or any outer
* methods (for anonymous inner classes) or outer classes (for inner classes)
* of the given class.







