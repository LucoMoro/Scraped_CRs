/*42565: Lint reports Analytics resource as unused

Change-Id:I27f5714539f1d28efc6ace1b9c9918fbd5f47825*/
//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/UnusedResourceDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/UnusedResourceDetectorTest.java
//Synthetic comment -- index 9113e79..d668a81 100644

//Synthetic comment -- @@ -263,4 +263,15 @@
"src/test/pkg/Foo.java.txt=>src/test/pkg/Foo.java",
"AndroidManifest.xml"));
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/UnusedResourceDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/UnusedResourceDetector.java
//Synthetic comment -- index 2a2d77c..f960118 100644

//Synthetic comment -- @@ -396,12 +396,17 @@
if (mUnused.containsKey(resource)) {
if (context.getDriver().isSuppressed(getIssue(resource), item)) {
mUnused.remove(resource);
                                return;
}
if (!context.getProject().getReportIssues()) {
mUnused.remove(resource);
                                return;
}
recordLocation(resource, context.getLocation(nameAttribute));
}
}
//Synthetic comment -- @@ -418,6 +423,18 @@
}
}

private void checkChildRefs(Element item) {
// Look for ?attr/ and @dimen/foo etc references in the item children
NodeList childNodes = item.getChildNodes();







