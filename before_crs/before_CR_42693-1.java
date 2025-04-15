/*Don't report unused resources in projects with errors

If a project contains errors in the source code, lint won't be able to
parse the source code, and in that case it might draw the wrong
conclusions about unused resources.

This can also happen if there's an actual bug in the Java parser,
which is sometimes the case; see for examplehttp://code.google.com/p/projectlombok/issues/detail?id=415http://code.google.com/p/projectlombok/issues/detail?id=311In both cases, when we encounter a failure to parse a Java file, we
record the fact that not all Java files were properly processed, and
rules, such as the UnusedResource detector, can (and now does) use
this to for example skip reporting unused resources in this case since
it is operating with incomplete data.

Change-Id:I00991c10d05965ce151fb0dd322f32229dcd12cd*/
//Synthetic comment -- diff --git a/lint/cli/src/com/android/tools/lint/LombokParser.java b/lint/cli/src/com/android/tools/lint/LombokParser.java
//Synthetic comment -- index 2c263c6..bc841e2 100644

//Synthetic comment -- @@ -48,6 +48,8 @@
// Don't analyze files containing errors
List<ParseProblem> problems = source.getProblems();
if (problems != null && problems.size() > 0) {
/* Silently ignore the errors. There are still some bugs in Lombok/Parboiled
* (triggered if you run lint on the AOSP framework directory for example),
* and having these show up as fatal errors when it's really a tool bug
//Synthetic comment -- @@ -61,7 +63,7 @@
// See http://code.google.com/p/projectlombok/issues/detail?id=313
String message = problem.getMessage();
context.report(
                            IssueRegistry.PARSER_ERROR, location,
message,
null);









//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index 352fd1d..068bf2a 100644

//Synthetic comment -- @@ -129,6 +129,7 @@
private Project[] mCurrentProjects;
private Project mCurrentProject;
private boolean mAbbreviating = true;

/**
* Creates a new {@link LintDriver}
//Synthetic comment -- @@ -230,6 +231,30 @@
}

/**
* Returns the projects being analyzed
*
* @return the projects being analyzed








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/UnusedResourceDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/UnusedResourceDetector.java
//Synthetic comment -- index bcbdd85..3217a09 100644

//Synthetic comment -- @@ -227,7 +227,7 @@
unused.removeAll(ids);
}

            if (unused.size() > 0) {
mUnused = new HashMap<String, Location>(unused.size());
for (String resource : unused) {
mUnused.put(resource, null);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/UnusedResourceDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/UnusedResourceDetectorTest.java
//Synthetic comment -- index c21c1b1..4bd5a88 100644

//Synthetic comment -- @@ -252,4 +252,16 @@

checkLint(Arrays.asList(master, library)));
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/Foo.java.txt b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/Foo.java.txt
new file mode 100644
//Synthetic comment -- index 0000000..6e16a72

//Synthetic comment -- @@ -0,0 +1,7 @@







