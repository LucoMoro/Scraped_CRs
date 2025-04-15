/*APK Export from IDE should use the merged manifest.

Change-Id:I532e71770886dec3a56a93882d9428c06e4ec3db*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java
//Synthetic comment -- index cf59d7b..467602f 100644

//Synthetic comment -- @@ -142,9 +142,15 @@
// Make sure the PNG crunch cache is up to date
helper.updateCrunchCache();

// package the resources.
helper.packageResources(
                    project.getFile(SdkConstants.FN_ANDROID_MANIFEST_XML),
libProjects,
null,   // res filter
0,      // versionCode
//Synthetic comment -- @@ -199,7 +205,6 @@

// get the proguard file output by aapt
if (proguardConfigFiles != null) {
                    IFolder androidOutputFolder = BaseProjectHelper.getAndroidOutputFolder(project);
IFile proguardFile = androidOutputFolder.getFile(AdtConstants.FN_AAPT_PROGUARD);
proguardConfigFiles.add(proguardFile.getLocation().toFile());
}







