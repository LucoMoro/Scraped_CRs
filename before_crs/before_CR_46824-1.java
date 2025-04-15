/*Don't warn about missing classes before a build. DO NOT MERGE.

The missing class detector warns about classes declared in
the manifest, but not found on disk. In order not to warn
before you've built the project, it only does this if it has
seen at least one class.

However, it turned out that it would also look at library jars,
such that a not-built project using the support library would
be considered to have classes, and would therefore flag newly
added activities.

Change-Id:I267b6b88cf109172217a434d6431b80be30e9788*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/MissingClassDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/MissingClassDetector.java
//Synthetic comment -- index 336f820..891a8af 100644

//Synthetic comment -- @@ -245,7 +245,9 @@

@Override
public void checkClass(@NonNull ClassContext context, @NonNull ClassNode classNode) {
        mHaveClasses = true;
String curr = classNode.name;
if (mReferencedClasses != null && mReferencedClasses.containsKey(curr)) {
mReferencedClasses.remove(curr);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/MissingClassDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/MissingClassDetectorTest.java
//Synthetic comment -- index 3c6e8cb..4edf345 100644

//Synthetic comment -- @@ -254,4 +254,17 @@
"registration/Bar.java.txt=>src/test/pkg/Foo/Bar.java"
));
}
}







