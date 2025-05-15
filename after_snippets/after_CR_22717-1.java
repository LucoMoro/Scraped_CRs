
//<Beginning of snippet n. 0>


"Revision %1$s\n" +
"Add-on XML Schema #%2$d\n" +
"Repository XML Schema #%3$d\n" +
                "Copyright (C) 2009-2011 The Android Open Source Project.",
getRevision(),
SdkAddonConstants.NS_LATEST_VERSION,
SdkRepoConstants.NS_LATEST_VERSION));

//<End of snippet n. 0>










//<Beginning of snippet n. 1>



/**
* Loads the Platforms from the SDK.
     * Creates the "platforms" folder if necessary.
     *
* @param sdkOsPath Location of the SDK
* @param list the list to fill with the platforms.
* @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
     * @throws RuntimeException when the "platforms" folder is missing and cannot be created.
*/
private static void loadPlatforms(String sdkOsPath, ArrayList<IAndroidTarget> list,
ISdkLog log) {
File platformFolder = new File(sdkOsPath, SdkConstants.FD_PLATFORMS);

if (platformFolder.isDirectory()) {
File[] platforms  = platformFolder.listFiles();

return;
}

        // Try to create it or complain if something else is in the way.
        if (!platformFolder.exists()) {
            if (!platformFolder.mkdir()) {
                throw new RuntimeException(
                        String.format("Failed to create %1$s.",
                                platformFolder.getAbsolutePath()));
            }
} else {
            throw new RuntimeException(
                    String.format("%1$s is not a folder.",
                            platformFolder.getAbsolutePath()));
}
}

/**
* @return an array of strings containing all the abi names for the target
*/
private static String[] getAbiList(String path) {
        ArrayList<String> list = new ArrayList<String>();

File imagesFolder = new File(path + File.separator + SdkConstants.OS_IMAGES_FOLDER);
File[] files = imagesFolder.listFiles();

/**
* Loads the Add-on from the SDK.
     * Creates the "add-ons" folder if necessary.
     *
* @param osSdkPath Location of the SDK
* @param list the list to fill with the add-ons.
* @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
     * @throws RuntimeException when the "add-ons" folder is missing and cannot be created.
*/
private static void loadAddOns(String osSdkPath, ArrayList<IAndroidTarget> list, ISdkLog log) {
File addonFolder = new File(osSdkPath, SdkConstants.FD_ADDONS);

if (addonFolder.isDirectory()) {
File[] addons  = addonFolder.listFiles();

return;
}

        // Try to create it or complain if something else is in the way.
        if (!addonFolder.exists()) {
            if (!addonFolder.mkdir()) {
                throw new RuntimeException(
                        String.format("Failed to create %1$s.",
                                addonFolder.getAbsolutePath()));
            }
} else {
            throw new RuntimeException(
                    String.format("%1$s is not a folder.",
                            addonFolder.getAbsolutePath()));
}
}

/**

//<End of snippet n. 1>








