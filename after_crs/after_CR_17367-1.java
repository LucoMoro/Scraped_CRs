/*Move adb to platform-tools.

This changes ADT, DDMS, HierarchyViewer which all care
where adb is (to launch it).

Also fixed the packaging of hierarchyViewer in the SDK.

Change-Id:I3c869159d7b0e0ad9aaea06f376b7ba3e53bfc7f*/




//Synthetic comment -- diff --git a/ddms/app/src/com/android/ddms/UIThread.java b/ddms/app/src/com/android/ddms/UIThread.java
//Synthetic comment -- index 9ba1ea0..d9ea3f1 100644

//Synthetic comment -- @@ -455,9 +455,18 @@
ClientData.setMethodProfilingHandler(new MethodProfilingHandler(shell));

// [try to] ensure ADB is running
        // in the new SDK, adb is in the platform-tools, but when run from the command line
        // in the Android source tree, then adb is next to ddms.
String adbLocation;
if (ddmsParentLocation != null && ddmsParentLocation.length() != 0) {
            // check if there's a platform-tools folder
            File platformTools = new File(new File(ddmsParentLocation).getParent(),
                    "platform-tools");  //$NON-NLS-1$
            if (platformTools.isDirectory()) {
                adbLocation = platformTools.getAbsolutePath() + File.separator + "adb"; //$NON-NLS-1$
            } else {
                adbLocation = ddmsParentLocation + File.separator + "adb"; //$NON-NLS-1$
            }
} else {
adbLocation = "adb"; //$NON-NLS-1$
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index afe04ae..5930635 100644

//Synthetic comment -- @@ -310,7 +310,7 @@

/** Returns the adb path relative to the sdk folder */
public static String getOsRelativeAdb() {
        return SdkConstants.OS_SDK_PLATFORM_TOOLS_FOLDER + SdkConstants.FN_ADB;
}

/** Returns the zipalign path relative to the sdk folder */








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplicationDirector.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplicationDirector.java
//Synthetic comment -- index d1a5b8c..b6a1f58 100644

//Synthetic comment -- @@ -47,9 +47,20 @@
@Override
public String getAdbLocation() {
String hvParentLocation = System.getProperty("com.android.hierarchyviewer.bindir"); //$NON-NLS-1$

        // in the new SDK, adb is in the platform-tools, but when run from the command line
        // in the Android source tree, then adb is next to hierarchyviewer.
if (hvParentLocation != null && hvParentLocation.length() != 0) {
            // check if there's a platform-tools folder
            File platformTools = new File(new File(hvParentLocation).getParent(),
                    SdkConstants.FD_PLATFORM_TOOLS);
            if (platformTools.isDirectory()) {
                return platformTools.getAbsolutePath() + File.separator + SdkConstants.FN_ADB;
            }

return hvParentLocation + File.separator + SdkConstants.FN_ADB;
}

return SdkConstants.FN_ADB;
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/LocalSdkParser.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/LocalSdkParser.java
//Synthetic comment -- index ebcc0fb..ffc6663 100755

//Synthetic comment -- @@ -91,6 +91,13 @@
visited.add(dir);
}

        dir = new File(osSdkRoot, SdkConstants.FD_PLATFORM_TOOLS);
        pkg = scanPlatformTools(dir, log);
        if (pkg != null) {
            packages.add(pkg);
            visited.add(dir);
        }

File samplesRoot = new File(osSdkRoot, SdkConstants.FD_SAMPLES);

// for platforms, add-ons and samples, rely on the SdkManager parser
//Synthetic comment -- @@ -234,13 +241,12 @@
for (File file : toolFolder.listFiles()) {
names.add(file.getName());
}
        if (!names.contains(SdkConstants.androidCmdName()) ||
!names.contains(SdkConstants.FN_EMULATOR)) {
return null;
}

        // Create our package. use the properties if we found any.
try {
ToolPackage pkg = new ToolPackage(
null,                       //source
//Synthetic comment -- @@ -262,6 +268,48 @@
}

/**
     * Try to find a platform-tools package at the given location.
     * Returns null if not found.
     */
    private Package scanPlatformTools(File platformToolsFolder, ISdkLog log) {
        // Can we find some properties?
        Properties props = parseProperties(new File(platformToolsFolder, SdkConstants.FN_SOURCE_PROP));

        // We're not going to check that all tools are present. At the very least
        // we should expect to find adb, android and an emulator adapted to the current OS.
        Set<String> names = new HashSet<String>();
        for (File file : platformToolsFolder.listFiles()) {
            names.add(file.getName());
        }
        if (!names.contains(SdkConstants.FN_ADB) ||
                !names.contains(SdkConstants.FN_AAPT) ||
                !names.contains(SdkConstants.FN_AIDL) ||
                !names.contains(SdkConstants.FN_DX)) {
            return null;
        }

        // Create our package. use the properties if we found any.
        try {
            PlatformToolPackage pkg = new PlatformToolPackage(
                    null,                           //source
                    props,                          //properties
                    0,                              //revision
                    null,                           //license
                    "Platform Tools",               //description
                    null,                           //descUrl
                    Os.getCurrentOs(),              //archiveOs
                    Arch.getCurrentArch(),          //archiveArch
                    platformToolsFolder.getPath()   //archiveOsPath
                    );

            return pkg;
        } catch (Exception e) {
            log.error(e, null);
        }
        return null;
    }

    /**
* Try to find a docs package at the given location.
* Returns null if not found.
*/








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AdbWrapper.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AdbWrapper.java
//Synthetic comment -- index cb553d1..241f30f 100755

//Synthetic comment -- @@ -50,7 +50,8 @@
if (!osSdkPath.endsWith(File.separator)) {
osSdkPath += File.separator;
}
        mAdbOsLocation = osSdkPath + SdkConstants.OS_SDK_PLATFORM_TOOLS_FOLDER
                + SdkConstants.FN_ADB;
}

private void display(String format, Object...args) {







