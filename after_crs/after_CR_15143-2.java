/*Improved logging when there's an error creating the
directories in getFilesDir();

Due to mkdirs() only returns false on errors,
it's better to output the directory path so the dev
knows the path that can not be created
See also Issue 8886http://code.google.com/p/android/issues/detail?id=8886Change-Id:I44d6adc8508ef9ca57f000b5d7bceeb0cfa3ed13*/




//Synthetic comment -- diff --git a/core/java/android/app/ApplicationContext.java b/core/java/android/app/ApplicationContext.java
//Synthetic comment -- index f48f150..0eec0f9 100644

//Synthetic comment -- @@ -416,7 +416,7 @@
}
if (!mFilesDir.exists()) {
if(!mFilesDir.mkdirs()) {
                    Log.w(TAG, "Unable to create files directory " + mFilesDir.getPath());
return null;
}
FileUtils.setPermissions(







