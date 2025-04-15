/*Close resource

Change-Id:I4683dc68a9f74ad8a2c709c8313c17ccbfbd64a1*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 7e7916e..be43e5c 100644

//Synthetic comment -- @@ -263,8 +263,9 @@

/** Add the issues found in the given jar file into the given list of issues */
private static void addIssuesFromJar(File jarFile, List<Issue> issues) {
try {
            JarFile jarfile = new JarFile(jarFile);
Manifest manifest = jarfile.getManifest();
Attributes attrs = manifest.getMainAttributes();
Object object = attrs.get(new Attributes.Name(MF_LINT_REGISTRY));
//Synthetic comment -- @@ -291,6 +292,14 @@
}
} catch (IOException e) {
log(e);
}
}








