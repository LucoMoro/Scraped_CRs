/*Code cleanup. DO NOT MERGE

Fix typos, remove redundant semicolons, fix incorrect names
(using an m prefix where they should be using s, turn fields
with constant names into actual constants), etc.

Change-Id:I3873dfa9bf53825d6cbf0e46b867ee751b41ee18*/




//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/LintCliXmlParserTest.java b/lint/cli/src/test/java/com/android/tools/lint/LintCliXmlParserTest.java
//Synthetic comment -- index a6f4aaf..9fa310f 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.tools.lint;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.checks.BuiltinIssueRegistry;
import com.android.tools.lint.client.api.LintClient;
import com.android.tools.lint.client.api.LintDriver;
//Synthetic comment -- @@ -154,8 +156,13 @@

private static class TestClient extends Main {
@Override
        public void report(
                @NonNull Context context,
                @NonNull Issue issue,
                @NonNull Severity severity,
                @Nullable Location location,
                @NonNull String message,
                @Nullable Object data) {
System.out.println(location + ":" + message);
}
}








//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/AbstractCheckTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/AbstractCheckTest.java
//Synthetic comment -- index 4f427a2..b73db8c 100644

//Synthetic comment -- @@ -348,7 +348,7 @@
}

@Override
        public Configuration getConfiguration(@NonNull Project project) {
return AbstractCheckTest.this.getConfiguration(this, project);
}

//Synthetic comment -- @@ -434,18 +434,18 @@
}

@Override
        public boolean isEnabled(@NonNull Issue issue) {
return AbstractCheckTest.this.isEnabled(issue);
}

@Override
        public void ignore(@NonNull Context context, @NonNull Issue issue,
                @Nullable Location location, @NonNull String message, @Nullable Object data) {
fail("Not supported in tests.");
}

@Override
        public void setSeverity(@NonNull Issue issue, @Nullable Severity severity) {
fail("Not supported in tests.");
}
}








//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/ApiLookupTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/ApiLookupTest.java
//Synthetic comment -- index 252ec5e..eaba6e6 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.tools.lint.checks;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Severity;

//Synthetic comment -- @@ -189,8 +191,11 @@
}

@Override
        public void log(
                @NonNull Severity severity,
                @Nullable Throwable exception,
                @Nullable String format,
                @Nullable Object... args) {
if (format != null) {
mLogBuffer.append(String.format(format, args));
mLogBuffer.append('\n');








//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/IconDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/IconDetectorTest.java
//Synthetic comment -- index e2c20ba..7cbbdc5 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.tools.lint.checks;

import com.android.annotations.NonNull;
import com.android.tools.lint.client.api.LintClient;
import com.android.tools.lint.client.api.LintDriver;
import com.android.tools.lint.detector.api.Detector;
//Synthetic comment -- @@ -67,7 +68,7 @@
protected TestConfiguration getConfiguration(LintClient client, Project project) {
return new TestConfiguration(client, project, null) {
@Override
            public boolean isEnabled(@NonNull Issue issue) {
return super.isEnabled(issue) && mEnabled.contains(issue);
}
};








//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/ManifestOrderDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/ManifestOrderDetectorTest.java
//Synthetic comment -- index e845b57..06e6fba 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.tools.lint.checks;

import com.android.annotations.NonNull;
import com.android.tools.lint.client.api.LintClient;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
//Synthetic comment -- @@ -40,7 +41,7 @@
protected TestConfiguration getConfiguration(LintClient client, Project project) {
return new TestConfiguration(client, project, null) {
@Override
            public boolean isEnabled(@NonNull Issue issue) {
return super.isEnabled(issue) && mEnabled.contains(issue);
}
};








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Location.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Location.java
//Synthetic comment -- index e255ede..34116d0 100644

//Synthetic comment -- @@ -648,7 +648,7 @@
*/
private boolean mConstructor;

        private SearchHints(@NonNull SearchDirection direction) {
super();
mDirection = direction;
}
//Synthetic comment -- @@ -659,7 +659,8 @@
* @param direction the direction to search in for the pattern
* @return a new @link SearchHints} object
*/
        @NonNull
        public static SearchHints create(@NonNull SearchDirection direction) {
return new SearchHints(direction);
}

//Synthetic comment -- @@ -668,6 +669,7 @@

* @return this, for constructor chaining
*/
        @NonNull
public SearchHints matchWholeWord() {
mWholeWord = true;

//Synthetic comment -- @@ -684,6 +686,7 @@
*
* @return this, for constructor chaining
*/
        @NonNull
public SearchHints matchJavaSymbol() {
mJavaSymbol = true;
mWholeWord = true;
//Synthetic comment -- @@ -702,6 +705,7 @@
*
* @return this, for constructor chaining
*/
        @NonNull
public SearchHints matchConstructor() {
mConstructor = true;
mWholeWord = true;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index 2c0d773..ab6e48a 100644

//Synthetic comment -- @@ -804,7 +804,7 @@
}

/**
     * Return the {@code @TargetApi} level to use for the given {@code classNode};
* this will be the {@code @TargetApi} annotation on the class, or any outer
* methods (for anonymous inner classes) or outer classes (for inner classes)
* of the given class.







