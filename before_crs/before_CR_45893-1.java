/*Fix the --showall flag to work with all the icon issues

Change-Id:I6c06889e26dc7c5e610069c71f58f81ffa6294a8*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java
//Synthetic comment -- index 9d9cf14..8ea2e3e 100644

//Synthetic comment -- @@ -867,7 +867,8 @@
context.report(ICON_NODPI, location,
String.format(
"The following images appear in both -nodpi and in a density folder: %1$s",
                            LintUtils.formatList(list, 10)),
null);
}
}
//Synthetic comment -- @@ -907,7 +908,8 @@
}
if (defined.size() > 0) {
foundIn = String.format(" (found in %1$s)",
                                    LintUtils.formatList(defined, 5));
}
}

//Synthetic comment -- @@ -915,7 +917,8 @@
String.format(
"Missing the following drawables in %1$s: %2$s%3$s",
file.getName(),
                                    LintUtils.formatList(delta, 5),
foundIn),
null);
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/AbstractCheckTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/AbstractCheckTest.java
//Synthetic comment -- index e7774f5..cd0d6e7 100644

//Synthetic comment -- @@ -146,6 +146,9 @@
return new TestConfiguration();
}

/**
* Run lint on the given files when constructed as a separate project
* @return The output of the lint check. On Windows, this transforms all directory
//Synthetic comment -- @@ -341,6 +344,7 @@

public String analyze(List<File> files) throws Exception {
mDriver = new LintDriver(new CustomIssueRegistry(), this);
mDriver.analyze(files, getLintScope(files));

Collections.sort(mWarnings);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/IconDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/IconDetectorTest.java
//Synthetic comment -- index f382085..af11d22 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Project;
//Synthetic comment -- @@ -32,6 +33,8 @@
}

private Set<Issue> mEnabled = new HashSet<Issue>();
private static Set<Issue> ALL = new HashSet<Issue>();
static {
ALL.add(IconDetector.DUPLICATES_CONFIGURATIONS);
//Synthetic comment -- @@ -47,6 +50,17 @@
}

@Override
protected TestConfiguration getConfiguration(Project project) {
return new TestConfiguration() {
@Override
//Synthetic comment -- @@ -329,4 +343,70 @@
"res/drawable/ic_launcher.png=>res/drawable-mdpi/ic_launcher2.png"
));
}
}
\ No newline at end of file







