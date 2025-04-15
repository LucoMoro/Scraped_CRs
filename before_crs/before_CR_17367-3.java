/*Make the tools work with the new location of adb.

adb has been moved to the platform-tools folder.

This changes ADT, DDMS, HierarchyViewer which all care
where adb is (to launch it).

Also fixed the local SDK parser of the SDK Updater to find
the platform-tools package.

Change-Id:I3c869159d7b0e0ad9aaea06f376b7ba3e53bfc7f*/
//Synthetic comment -- diff --git a/ddms/app/src/com/android/ddms/UIThread.java b/ddms/app/src/com/android/ddms/UIThread.java
//Synthetic comment -- index 9ba1ea0..d9ea3f1 100644

//Synthetic comment -- @@ -455,9 +455,18 @@
ClientData.setMethodProfilingHandler(new MethodProfilingHandler(shell));

// [try to] ensure ADB is running
String adbLocation;
if (ddmsParentLocation != null && ddmsParentLocation.length() != 0) {
            adbLocation = ddmsParentLocation + File.separator + "adb"; //$NON-NLS-1$
} else {
adbLocation = "adb"; //$NON-NLS-1$
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index afe04ae..5930635 100644

//Synthetic comment -- @@ -310,7 +310,7 @@

/** Returns the adb path relative to the sdk folder */
public static String getOsRelativeAdb() {
        return SdkConstants.OS_SDK_TOOLS_FOLDER + SdkConstants.FN_ADB;
}

/** Returns the zipalign path relative to the sdk folder */








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplicationDirector.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplicationDirector.java
//Synthetic comment -- index d1a5b8c..b6a1f58 100644

//Synthetic comment -- @@ -47,9 +47,20 @@
@Override
public String getAdbLocation() {
String hvParentLocation = System.getProperty("com.android.hierarchyviewer.bindir"); //$NON-NLS-1$
if (hvParentLocation != null && hvParentLocation.length() != 0) {
return hvParentLocation + File.separator + SdkConstants.FN_ADB;
}
return SdkConstants.FN_ADB;
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/LocalSdkParser.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/LocalSdkParser.java
//Synthetic comment -- index ebcc0fb..53aa61b 100755

//Synthetic comment -- @@ -91,6 +91,13 @@
visited.add(dir);
}

File samplesRoot = new File(osSdkRoot, SdkConstants.FD_SAMPLES);

// for platforms, add-ons and samples, rely on the SdkManager parser
//Synthetic comment -- @@ -234,13 +241,12 @@
for (File file : toolFolder.listFiles()) {
names.add(file.getName());
}
        if (!names.contains(SdkConstants.FN_ADB) ||
                !names.contains(SdkConstants.androidCmdName()) ||
!names.contains(SdkConstants.FN_EMULATOR)) {
return null;
}

        // Create are package. use the properties if we found any.
try {
ToolPackage pkg = new ToolPackage(
null,                       //source
//Synthetic comment -- @@ -262,6 +268,49 @@
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
        mAdbOsLocation = osSdkPath + SdkConstants.OS_SDK_TOOLS_FOLDER + SdkConstants.FN_ADB;
}

private void display(String format, Object...args) {







