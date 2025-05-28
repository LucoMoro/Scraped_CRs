
//<Beginning of snippet n. 0>


"Revision %1$s\n" +
"Add-on XML Schema #%2$d\n" +
"Repository XML Schema #%3$d\n" +
                "Copyright (C) 2009-2010 The Android Open Source Project.",
getRevision(),
SdkAddonConstants.NS_LATEST_VERSION,
SdkRepoConstants.NS_LATEST_VERSION));

//<End of snippet n. 0>










//<Beginning of snippet n. 1>



/**
* Loads the Platforms from the SDK.
* @param sdkOsPath Location of the SDK
* @param list the list to fill with the platforms.
* @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
*/
private static void loadPlatforms(String sdkOsPath, ArrayList<IAndroidTarget> list,
ISdkLog log) {
File platformFolder = new File(sdkOsPath, SdkConstants.FD_PLATFORMS);
if (platformFolder.isDirectory()) {
File[] platforms  = platformFolder.listFiles();

return;
}

        String message = null;
        if (platformFolder.exists() == false) {
            message = "%s is missing.";
} else {
            message = "%s is not a folder.";
}

        throw new IllegalArgumentException(String.format(message,
                platformFolder.getAbsolutePath()));
}

/**
* @return an array of strings containing all the abi names for the target
*/
private static String[] getAbiList(String path) {
        ArrayList list = new ArrayList();

File imagesFolder = new File(path + File.separator + SdkConstants.OS_IMAGES_FOLDER);
File[] files = imagesFolder.listFiles();

/**
* Loads the Add-on from the SDK.
* @param osSdkPath Location of the SDK
* @param list the list to fill with the add-ons.
* @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
*/
private static void loadAddOns(String osSdkPath, ArrayList<IAndroidTarget> list, ISdkLog log) {
File addonFolder = new File(osSdkPath, SdkConstants.FD_ADDONS);
if (addonFolder.isDirectory()) {
File[] addons  = addonFolder.listFiles();

return;
}

        String message = null;
        if (addonFolder.exists() == false) {
            message = "%s is missing.";
} else {
            message = "%s is not a folder.";
}

        throw new IllegalArgumentException(String.format(message,
                addonFolder.getAbsolutePath()));
}

/**

//<End of snippet n. 1>








