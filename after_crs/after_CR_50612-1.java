/*Allow "all" to be used in the lint.xml file to match all issues

Change-Id:Iea1bf8c25d14d7e84efadc0ed9459cc8efd7a4e9*/




//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/DefaultConfiguration.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/DefaultConfiguration.java
//Synthetic comment -- index db82556..d6b925c 100644

//Synthetic comment -- @@ -76,6 +76,8 @@
private static final String ATTR_PATH = "path"; //$NON-NLS-1$
@NonNull
private static final String TAG_IGNORE = "ignore"; //$NON-NLS-1$
    @NonNull
    private static final String VALUE_ALL = "all"; //$NON-NLS-1$

private final Configuration mParent;
private final Project mProject;
//Synthetic comment -- @@ -149,6 +151,9 @@

String id = issue.getId();
List<String> paths = mSuppressed.get(id);
        if (paths == null) {
            paths = mSuppressed.get(VALUE_ALL);
        }
if (paths != null && location != null) {
File file = location.getFile();
String relativePath = context.getProject().getRelativePath(file);
//Synthetic comment -- @@ -181,6 +186,10 @@
ensureInitialized();

Severity severity = mSeverity.get(issue.getId());
        if (severity == null) {
            severity = mSeverity.get(VALUE_ALL);
        }

if (severity != null) {
return severity;
}







