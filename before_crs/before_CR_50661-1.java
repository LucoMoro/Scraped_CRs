/*Allow "all" to be used in the lint.xml file to match all issues

Change-Id:Ieedd879648883fb2d1a2eed571b690f3280a1850*/
//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/DefaultConfiguration.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/DefaultConfiguration.java
//Synthetic comment -- index db82556..d6b925c 100644

//Synthetic comment -- @@ -76,6 +76,8 @@
private static final String ATTR_PATH = "path"; //$NON-NLS-1$
@NonNull
private static final String TAG_IGNORE = "ignore"; //$NON-NLS-1$

private final Configuration mParent;
private final Project mProject;
//Synthetic comment -- @@ -149,6 +151,9 @@

String id = issue.getId();
List<String> paths = mSuppressed.get(id);
if (paths != null && location != null) {
File file = location.getFile();
String relativePath = context.getProject().getRelativePath(file);
//Synthetic comment -- @@ -181,6 +186,10 @@
ensureInitialized();

Severity severity = mSeverity.get(issue.getId());
if (severity != null) {
return severity;
}







