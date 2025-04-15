/*Allow "all" to be used in the lint.xml file to match all issues

Change-Id:Iea1bf8c25d14d7e84efadc0ed9459cc8efd7a4e9*/
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








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index 435ccab..18dd953 100644

//Synthetic comment -- @@ -460,12 +460,10 @@
while (owner != null) {
// For virtual dispatch, walk up the inheritance chain checking
// each inherited method
                if (owner.startsWith("android/support/")) { //$NON-NLS-1$
                    // Continue
                } else if ((owner.startsWith("android/")                //$NON-NLS-1$
&& !owner.startsWith("android/support/"))   //$NON-NLS-1$
|| owner.startsWith("java/")                    //$NON-NLS-1$
                        || owner.startsWith("javax/")) {                 //$NON-NLS-1$
frameworkParent = owner;
break;
}







