/*Add support for using newer Ant rules distributed by the tools.

Right now, the Ant rules are packaged with each platform. This
is a problem when we want to release newer versions of the rules,
as we would have to respin newer rev for all major platforms just
for that.

The new mechanism has the Ant rules both in the tools folder
and in the platforms. The platform indicates which versions
it supports (support is based on the features of external tools
such as aapt, aidl and dex). The custom tasks can
figure out a newer but still compatible (ie not relying on
newer options in aapt/aidl/dex) version from the tools folder
and then uses this newer version.

Change-Id:I763533ec85fa9647ecf96ac6cbaefbfd1ad68fb0*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/SetupTask.java b/anttasks/src/com/android/ant/SetupTask.java
//Synthetic comment -- index 5263f12..85ebea1 100644

//Synthetic comment -- @@ -92,6 +92,24 @@
// ref id to the <path> object containing all the boot classpaths.
private final static String REF_CLASSPATH = "android.target.classpath";

    /**
     * Compatibility range for the Ant rules.
     * The goal is to specify range of the rules that are compatible between them. For instance if
     * a range is 10-15 and a platform indicate that it supports rev 12, but the tools have rules
     * revision 15, then the rev 15 will be used.
     * Compatibility is broken when a new rev of the rules relies on a new option in the external
     * tools contained in the platform.
     *
     * For instance if rules 10 uses a newly introduced aapt option, then it would be considered
     * incompatible with 9, and therefore would be the start of a new compatibility range.
     * A platform declaring it supports 9 would not be made to use 10, as its aapt version wouldn't
     * support it.
     */
    private final static int ANT_COMPATIBILITY_RANGES[][] = new int[][] {
        new int[] { 1, 1 },
        new int[] { 2, 3 },
    };

private boolean mDoImport = true;

@Override
//Synthetic comment -- @@ -278,16 +296,28 @@

// Now the import section. This is only executed if the task actually has to import a file.
if (mDoImport) {
            // check if there's a more recent version of the rules in the tools folder.
            int toolsRulesRev = getAntRulesFromTools(antBuildVersion);

            File rulesFolder;
            if (toolsRulesRev == -1) {
                // no more recent Ant rules from the tools, folder. Find them inside the platform.
                // find the folder containing the file to import
                int folderID = antBuildVersion == 1 ? IAndroidTarget.TEMPLATES : IAndroidTarget.ANT;
                String rulesOSPath = androidTarget.getPath(folderID);
                rulesFolder = new File(rulesOSPath);
            } else {
                // in this case we import the rules from the ant folder in the tools.
                rulesFolder = new File(new File(sdkLocation, SdkConstants.FD_TOOLS),
                        SdkConstants.FD_ANT);
                // the new rev is:
                antBuildVersion = toolsRulesRev;
            }

// make sure the file exists.
if (rulesFolder.isDirectory() == false) {
throw new BuildException(String.format("Rules directory '%s' is missing.",
                        rulesFolder.getAbsolutePath()));
}

String importedRulesFileName;
//Synthetic comment -- @@ -327,6 +357,16 @@
}
}

    private int getAntRulesFromTools(int rulesRev) {
        for (int[] range : ANT_COMPATIBILITY_RANGES) {
            if (range[0] <= rulesRev && rulesRev <= range[1]) {
                return range[1];
            }
        }

        return -1;
    }

/**
* Sets the value of the "import" attribute.
* @param value the value.







