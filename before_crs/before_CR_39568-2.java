/*35228: Strip whitespace from default project name

Change-Id:I14041db3996be2afa3bfec82aa1f0a2191eb4872*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ProjectNamePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ProjectNamePage.java
//Synthetic comment -- index 7d3114b..1ab60a9 100644

//Synthetic comment -- @@ -60,7 +60,6 @@

import java.io.File;
import java.net.URI;
import java.util.regex.Pattern;

/**
* Initial page shown when creating projects which asks for the project name,
//Synthetic comment -- @@ -70,12 +69,6 @@
private final NewProjectWizardState mValues;
/** Flag used when setting button/text state manually to ignore listener updates */
private boolean mIgnore;
    /**
     * Pattern for characters accepted in a project name. Since this will be
     * used as a directory name, we're being a bit conservative on purpose and
     * limiting it to shell-agnostic characters. It cannot start with a space.
     */
    private static final Pattern sProjectNamePattern = Pattern.compile("^[\\w][\\w. -]*$");  //$NON-NLS-1$
/** Last user-browsed location, static so that it be remembered for the whole session */
private static String sCustomLocationOsPath = "";  //$NON-NLS-1$
private static boolean sAutoComputeCustomLocation = true;
//Synthetic comment -- @@ -581,9 +574,6 @@
if (projectName == null || projectName.length() == 0) {
return new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
"Project name must be specified");
        } else if (!sProjectNamePattern.matcher(projectName).matches()) {
            return new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                    "The project name must start with an alphanumeric characters, followed by one or more alphanumerics, digits, dots, dashes, underscores or spaces.");
} else {
IWorkspace workspace = ResourcesPlugin.getWorkspace();
IStatus nameStatus = workspace.validateName(projectName, IResource.PROJECT);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java
//Synthetic comment -- index 2daf5ed..69d3d1d 100644

//Synthetic comment -- @@ -437,15 +437,33 @@
}

private String appNameToProjectName(String appName) {
IWorkspace workspace = ResourcesPlugin.getWorkspace();
IStatus nameStatus = workspace.validateName(appName, IResource.PROJECT);
if (nameStatus.isOK()) {
return appName;
}
        StringBuilder sb = new StringBuilder(appName.length());
for (int i = 0, n = appName.length(); i < n; i++) {
char c = appName.charAt(i);
            if (Character.isLetterOrDigit(c) || c == '.' || c == '-' || c == ' ') {
sb.append(c);
}
}
//Synthetic comment -- @@ -609,7 +627,11 @@
sb.append(SAMPLE_PACKAGE_PREFIX);
appendPackage(sb, original);

            mValues.packageName = sb.toString();
try {
mIgnore = true;
mPackageText.setText(mValues.packageName);
//Synthetic comment -- @@ -625,7 +647,7 @@
if (i == 0 && Character.isJavaIdentifierStart(c)
|| i != 0 && Character.isJavaIdentifierPart(c)) {
sb.append(Character.toLowerCase(c));
            } else if ((c == '.' || c == ' ')
&& (sb.length() > 0 && sb.charAt(sb.length() - 1) != '.')) {
sb.append('.');
} else if (c == '-') {







