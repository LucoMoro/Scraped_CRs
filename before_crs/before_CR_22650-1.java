/*SdkManager: handle missing platforms/ and add-ons/ folders.

A while ago, when we were dealing with monolithics SDKs,
the strategy was that we wanted to make sure that
the 'android' command was invoked from a properly setup
SDK folder. Consequently the SdkManager required at least
the platforms/ and add-ons/ folders to be present, even if
empty.

I don't think that behavior is really necessary anymore.
The 'android' tool is invoked with knowledge from its tools
folder, so we can safely assume the top parent is "the SDK
folder" and create these directories if they are missing.

We do however try to create them as early as possible if
they are missing, so that the tool aborts early if we can't
create them. The other alternative would be to just try to
create them when we're actually installing a package.

Change-Id:I8ea58f23add89c2dac0a22142f6fb5e71b8203aa*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/internal/repository/AboutPage.java b/sdkmanager/app/src/com/android/sdkmanager/internal/repository/AboutPage.java
//Synthetic comment -- index cb2f981..4c72e1e 100755

//Synthetic comment -- @@ -70,7 +70,7 @@
"Revision %1$s\n" +
"Add-on XML Schema #%2$d\n" +
"Repository XML Schema #%3$d\n" +
                "Copyright (C) 2009-2010 The Android Open Source Project.",
getRevision(),
SdkAddonConstants.NS_LATEST_VERSION,
SdkRepoConstants.NS_LATEST_VERSION));








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index d6662c1..d924d3d 100644

//Synthetic comment -- @@ -233,13 +233,17 @@

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

//Synthetic comment -- @@ -257,15 +261,18 @@
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
//Synthetic comment -- @@ -388,7 +395,7 @@
* @return an array of strings containing all the abi names for the target
*/
private static String[] getAbiList(String path) {
        ArrayList list = new ArrayList();

File imagesFolder = new File(path + File.separator + SdkConstants.OS_IMAGES_FOLDER);
File[] files = imagesFolder.listFiles();
//Synthetic comment -- @@ -409,12 +416,16 @@

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

//Synthetic comment -- @@ -433,15 +444,18 @@
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







