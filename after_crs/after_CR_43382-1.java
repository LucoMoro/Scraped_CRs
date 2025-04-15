/*Don't flag missing classes if project has not been built yet

The missing class detector in lint will warn if the manifest file
contains references to class files and lint does not see those class
files in the class output folder (or in any of the library jars).
This is useful for catching typos and deleted classes.

However, if you run lint on a project that has not yet been built,
this can give misleading or confusing errors, since lint decides that
a class exists by whether a .class file is actually there, not by
whether a source file exists which would result in a class file
getting produced with that name.

Change-Id:I3105157a37f575e00b550ba90d883f24c02c16a9*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/MissingClassDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/MissingClassDetector.java
//Synthetic comment -- index dac3b82..9fef36c 100644

//Synthetic comment -- @@ -114,6 +114,7 @@
Scope.MANIFEST_SCOPE);

private Map<String, Location.Handle> mReferencedClasses;
    private boolean mHaveClasses;

/** Constructs a new {@link MissingClassDetector} */
public MissingClassDetector() {
//Synthetic comment -- @@ -210,7 +211,7 @@
@Override
public void afterCheckProject(@NonNull Context context) {
if (!context.getProject().isLibrary() && mReferencedClasses != null &&
                !mReferencedClasses.isEmpty() && mHaveClasses) {
List<String> classes = new ArrayList<String>(mReferencedClasses.keySet());
Collections.sort(classes);
for (String owner : classes) {
//Synthetic comment -- @@ -243,6 +244,7 @@

@Override
public void checkClass(@NonNull ClassContext context, @NonNull ClassNode classNode) {
        mHaveClasses = true;
String curr = classNode.name;
if (mReferencedClasses != null && mReferencedClasses.containsKey(curr)) {
mReferencedClasses.remove(curr);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/MissingClassDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/MissingClassDetectorTest.java
//Synthetic comment -- index d8f18b6..705cf82 100644

//Synthetic comment -- @@ -49,10 +49,22 @@

lintProject(
"bytecode/AndroidManifestWrongRegs.xml=>AndroidManifest.xml",
                "apicheck/ApiCallTest.class.data=>bin/classes/foo/bar/ApiCallTest.class",
"bytecode/.classpath=>.classpath"
));
}

    public void testNoWarningBeforeBuild() throws Exception {
        assertEquals(
            "No warnings.",

            lintProject(
                "bytecode/AndroidManifestWrongRegs.xml=>AndroidManifest.xml",
                "bytecode/.classpath=>.classpath"
            ));
    }


public void testOkClasses() throws Exception {
assertEquals(
"No warnings.",
//Synthetic comment -- @@ -159,6 +171,7 @@
lintProject(
"registration/AndroidManifest.xml=>AndroidManifest.xml",
"bytecode/.classpath=>.classpath",
                "apicheck/ApiCallTest.class.data=>bin/classes/foo/bar/ApiCallTest.class",
"registration/Foo.java.txt=>src/test/pkg/Foo.java"
));
}
//Synthetic comment -- @@ -173,6 +186,7 @@
lintProject(
"registration/AndroidManifestInner.xml=>AndroidManifest.xml",
"bytecode/.classpath=>.classpath",
                "apicheck/ApiCallTest.class.data=>bin/classes/foo/bar/ApiCallTest.class",
"registration/Bar.java.txt=>src/test/pkg/Foo/Bar.java"
));
}
//Synthetic comment -- @@ -187,6 +201,7 @@
lintProject(
"registration/AndroidManifestWrong.xml=>AndroidManifest.xml",
"bytecode/.classpath=>.classpath",
                "apicheck/ApiCallTest.class.data=>bin/classes/foo/bar/ApiCallTest.class",
"registration/Bar.java.txt=>src/test/pkg/Foo/Bar.java"
));
}
//Synthetic comment -- @@ -204,6 +219,7 @@
lintProject(
"registration/AndroidManifestWrong2.xml=>AndroidManifest.xml",
"bytecode/.classpath=>.classpath",
                "apicheck/ApiCallTest.class.data=>bin/classes/foo/bar/ApiCallTest.class",
"registration/Bar.java.txt=>src/test/pkg/Foo/Bar.java"
));
}







