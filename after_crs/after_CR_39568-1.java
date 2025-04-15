/*35228: Strip whitespace from default project name

Change-Id:I14041db3996be2afa3bfec82aa1f0a2191eb4872*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ProjectNamePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ProjectNamePage.java
//Synthetic comment -- index 7d3114b..65a548f 100644

//Synthetic comment -- @@ -581,9 +581,6 @@
if (projectName == null || projectName.length() == 0) {
return new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
"Project name must be specified");
} else {
IWorkspace workspace = ResourcesPlugin.getWorkspace();
IStatus nameStatus = workspace.validateName(projectName, IResource.PROJECT);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java
//Synthetic comment -- index 2daf5ed..69d3d1d 100644

//Synthetic comment -- @@ -437,15 +437,33 @@
}

private String appNameToProjectName(String appName) {
        // Strip out whitespace (and capitalize subsequent words where spaces were removed
        boolean upcaseNext = false;
        StringBuilder sb = new StringBuilder(appName.length());
        for (int i = 0, n = appName.length(); i < n; i++) {
            char c = appName.charAt(i);
            if (c == ' ') {
                upcaseNext = true;
            } else if (upcaseNext) {
                sb.append(Character.toUpperCase(c));
                upcaseNext = false;
            } else {
                sb.append(c);
            }
        }

        appName = sb.toString().trim();

IWorkspace workspace = ResourcesPlugin.getWorkspace();
IStatus nameStatus = workspace.validateName(appName, IResource.PROJECT);
if (nameStatus.isOK()) {
return appName;
}

        sb = new StringBuilder(appName.length());
for (int i = 0, n = appName.length(); i < n; i++) {
char c = appName.charAt(i);
            if (Character.isLetterOrDigit(c) || c == '.' || c == '-') {
sb.append(c);
}
}
//Synthetic comment -- @@ -609,7 +627,11 @@
sb.append(SAMPLE_PACKAGE_PREFIX);
appendPackage(sb, original);

            String pkg = sb.toString();
            if (pkg.endsWith(".")) { //$NON-NLS-1$
                pkg = pkg.substring(0, pkg.length() - 1);
            }
            mValues.packageName = pkg;
try {
mIgnore = true;
mPackageText.setText(mValues.packageName);
//Synthetic comment -- @@ -625,7 +647,7 @@
if (i == 0 && Character.isJavaIdentifierStart(c)
|| i != 0 && Character.isJavaIdentifierPart(c)) {
sb.append(Character.toLowerCase(c));
            } else if ((c == '.')
&& (sb.length() > 0 && sb.charAt(sb.length() - 1) != '.')) {
sb.append('.');
} else if (c == '-') {







