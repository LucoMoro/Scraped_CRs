/*Display error for missing required attributes in ant tasks

Change-Id:I4a9babcda7a541a7defd226e36bb8f74f88853ed*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/ApkBuilderTask.java b/anttasks/src/com/android/ant/ApkBuilderTask.java
//Synthetic comment -- index f7b36bc..0d9b8fb 100644

//Synthetic comment -- @@ -195,6 +195,14 @@
throw new BuildException("missing attribute 'apkFilepath'");
}

// check dexPath is only one file.
File dexFile = null;
if (mHasCode) {







