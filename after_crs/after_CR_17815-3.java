/*Check for platform-tools presence.

Change-Id:Ieaf6e42bc67829b01ebb0fa799bc615f85fc1a6d*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/SetupTask.java b/anttasks/src/com/android/ant/SetupTask.java
//Synthetic comment -- index ba62403..4f14d62 100644

//Synthetic comment -- @@ -92,6 +92,17 @@
System.out.println("Android SDK Tools Revision " + toolsRevison);
}

        // detect that the platform tools is there.
        File platformTools = new File(sdkDir, SdkConstants.FD_PLATFORM_TOOLS);
        if (platformTools.isDirectory() == false) {
            throw new BuildException(String.format(
                    "SDK Platform Tools component is missing. " +
                    "Please install it with the SDK Manager (%1$s%2$c%3$s)",
                    SdkConstants.FD_TOOLS,
                    File.separatorChar,
                    SdkConstants.androidCmdName()));
        }

// get the target property value
String targetHashString = antProject.getProperty(ProjectProperties.PROPERTY_TARGET);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index 4bd5121..801b165 100644

//Synthetic comment -- @@ -788,6 +788,19 @@
return false;
}

        // check that we have both the tools component and the platform-tools component.
        String platformTools = osSdkLocation + SdkConstants.OS_SDK_PLATFORM_TOOLS_FOLDER;
        if (checkFolder(platformTools) == false) {
            return errorHandler.handleError("SDK Platform Tools component is missing!\n" +
                    "Please use the SDK Manager to install it.");
        }

        String tools = osSdkLocation + SdkConstants.OS_SDK_TOOLS_FOLDER;
        if (checkFolder(tools) == false) {
            return errorHandler.handleError("SDK Tools component is missing!\n" +
                    "Please use the SDK Manager to install it.");
        }

// check the path to various tools we use to make sure nothing is missing. This is
// not meant to be exhaustive.
String[] filesToCheck = new String[] {
//Synthetic comment -- @@ -818,6 +831,20 @@
}

/**
     * Checks if a path reference a valid existing folder.
     * @param osPath the os path to check.
     * @return true if the folder exists and is, in fact, a folder.
     */
    private boolean checkFolder(String osPath) {
        File file = new File(osPath);
        if (file.isDirectory() == false) {
            return false;
        }

        return true;
    }

    /**
* Creates a job than can ping the usage server.
*/
private Job createPingUsageServerJob() {







