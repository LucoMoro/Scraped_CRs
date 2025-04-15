/*Fixed error in ApkBuilderTask for jar files with resources

jar files specified with <jarfolder> are searched twice
for resources. The build fails when resources are found
the second time.

Change-Id:I4ce9028df7116587396aa67e0853eab34ba854fe*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/ApkBuilderTask.java b/anttasks/src/com/android/ant/ApkBuilderTask.java
//Synthetic comment -- index 5b3654f..958503b 100644

//Synthetic comment -- @@ -349,25 +349,6 @@
apkBuilder.addSourceFolder(f);
}

// now go through the list of jar files.
for (File f : jarFileList) {
apkBuilder.addResourcesFromJar(f);







