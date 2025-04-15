/*APK Export from IDE should use the merged manifest.

(cherry picked from commit f22fc319cefd16470645cdf905d2439b7b813606)

Change-Id:I23e78b4e053599bb41620e835bc78b9f32d0c3f3*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java
//Synthetic comment -- index cf59d7b..467602f 100644

//Synthetic comment -- @@ -142,9 +142,15 @@
// Make sure the PNG crunch cache is up to date
helper.updateCrunchCache();

            // get the merged manifest
            IFolder androidOutputFolder = BaseProjectHelper.getAndroidOutputFolder(project);
            IFile mergedManifestFile = androidOutputFolder.getFile(
                    SdkConstants.FN_ANDROID_MANIFEST_XML);


// package the resources.
helper.packageResources(
                    mergedManifestFile,
libProjects,
null,   // res filter
0,      // versionCode
//Synthetic comment -- @@ -199,7 +205,6 @@

// get the proguard file output by aapt
if (proguardConfigFiles != null) {
IFile proguardFile = androidOutputFolder.getFile(AdtConstants.FN_AAPT_PROGUARD);
proguardConfigFiles.add(proguardFile.getLocation().toFile());
}







