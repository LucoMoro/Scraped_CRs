/*Ant: ensure SDK path ends with dir separator.

SDK Bug: 2906094

Change-Id:Ic4053c97c8dd5a32a2276e22644b5e09c91d52f0*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/SetupTask.java b/anttasks/src/com/android/ant/SetupTask.java
//Synthetic comment -- index 0cd3e19..c66b956 100644

//Synthetic comment -- @@ -119,11 +119,16 @@
Project antProject = getProject();

// get the SDK location
        File sdk = TaskHelper.getSdkLocation(antProject);
        String sdkLocation = sdk.getPath();

// display SDK Tools revision
        int toolsRevison = TaskHelper.getToolsRevision(sdk);
if (toolsRevison != -1) {
System.out.println("Android SDK Tools Revision " + toolsRevison);
}
//Synthetic comment -- @@ -143,7 +148,7 @@

// load up the sdk targets.
final ArrayList<String> messages = new ArrayList<String>();
        SdkManager manager = SdkManager.createManager(sdkLocation, new ISdkLog() {
public void error(Throwable t, String errorFormat, Object... args) {
if (errorFormat != null) {
messages.add(String.format("Error: " + errorFormat, args));
//Synthetic comment -- @@ -310,7 +315,7 @@
rulesFolder = new File(rulesOSPath);
} else {
// in this case we import the rules from the ant folder in the tools.
                rulesFolder = new File(new File(sdkLocation, SdkConstants.FD_TOOLS),
SdkConstants.FD_ANT);
// the new rev is:
antBuildVersion = toolsRulesRev;
//Synthetic comment -- @@ -329,7 +334,7 @@
} else {
importedRulesFileName = String.format(
isLibrary ? RULES_LIBRARY : isTestProject ? RULES_TEST : RULES_MAIN,
                        antBuildVersion);;
}

// now check the rules file exists.
//Synthetic comment -- @@ -342,14 +347,14 @@

// display the file being imported.
// figure out the path relative to the SDK
            String rulesLocation = rules.getAbsolutePath();
            if (rulesLocation.startsWith(sdkLocation)) {
                rulesLocation = rulesLocation.substring(sdkLocation.length());
                if (rulesLocation.startsWith(File.separator)) {
                    rulesLocation = rulesLocation.substring(1);
}
}
            System.out.println("\nImporting rules file: " + rulesLocation);

// set the file location to import
setFile(rules.getAbsolutePath());
//Synthetic comment -- @@ -624,7 +629,6 @@
* project properties.
* @param baseFolder the base folder of the project (to resolve relative paths)
* @param source a source of project properties.
     * @return
*/
private ArrayList<File> getDirectDependencies(File baseFolder, IPropertySource source) {
ArrayList<File> libraries = new ArrayList<File>();








//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/TaskHelper.java b/anttasks/src/com/android/ant/TaskHelper.java
//Synthetic comment -- index 22cfb30..e29175b 100644

//Synthetic comment -- @@ -35,22 +35,27 @@

static File getSdkLocation(Project antProject) {
// get the SDK location
        String sdkLocation = antProject.getProperty(ProjectProperties.PROPERTY_SDK);

// check if it's valid and exists
        if (sdkLocation == null || sdkLocation.length() == 0) {
// LEGACY support: project created with 1.6 or before may be using a different
// property to declare the location of the SDK. At this point, we cannot
// yet check which target is running so we check both always.
            sdkLocation = antProject.getProperty(ProjectProperties.PROPERTY_SDK_LEGACY);
            if (sdkLocation == null || sdkLocation.length() == 0) {
throw new BuildException("SDK Location is not set.");
}
}

        File sdk = new File(sdkLocation);
if (sdk.isDirectory() == false) {
            throw new BuildException(String.format("SDK Location '%s' is not valid.", sdkLocation));
}

return sdk;







