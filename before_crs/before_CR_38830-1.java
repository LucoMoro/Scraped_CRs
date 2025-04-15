/*Prevent exceptions for invalid project definitions

Change-Id:Ie2b5929a87144c77f1ce0d43b2520cc498df5081*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index b6aaf19..c9ef657 100644

//Synthetic comment -- @@ -463,7 +463,11 @@
IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
IResource res = root.findMember(path);
if (res != null) {
            return res.getLocation().toFile();
}

return path.toFile();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java
//Synthetic comment -- index f5d3711..0ec26fe 100644

//Synthetic comment -- @@ -763,6 +763,10 @@

if (kind == IClasspathEntry.CPE_VARIABLE) {
entry = JavaCore.getResolvedClasspathEntry(entry);
kind = entry.getEntryKind();
}









//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index 288a976..3e4e922 100644

//Synthetic comment -- @@ -1137,6 +1137,9 @@
for (File classPathEntry : classPath) {
if (classPathEntry.getName().endsWith(DOT_JAR)) {
File jarFile = classPathEntry;
ZipInputStream zis = null;
try {
FileInputStream fis = new FileInputStream(jarFile);







