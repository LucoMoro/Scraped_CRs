/*Fix NPE when a library does not have a libs folder.

Change-Id:I382fb659073a8d2df8d018acde37d4769ac40cc0*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/SetupTask.java b/anttasks/src/com/android/ant/SetupTask.java
//Synthetic comment -- index 0fbd3a0..184fd2a 100644

//Synthetic comment -- @@ -520,9 +520,11 @@
// get the jars from it too
File libsFolder = new File(rootPath, SdkConstants.FD_NATIVE_LIBS);
File[] jarFiles = libsFolder.listFiles(filter);
            if (jarFiles != null) {
                for (File jarFile : jarFiles) {
                    element = jarsPath.createPathElement();
                    element.setPath(jarFile.getAbsolutePath());
                }
}

// get the package from the manifest.







