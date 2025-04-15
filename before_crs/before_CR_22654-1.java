/*Restrict scan of external media

Just scan the folders that are actually documented to be used:http://developer.android.com/guide/topics/data/data-storage.html#filesExternalI know, it's a novel idea, making the behavior match the documentation. But
sometimes you just gotta live on the edge.

Also seehttp://code.google.com/p/android/issues/detail?id=11731Change-Id:I0ef8ac9657b627394232cd023b56384a9f488821*/
//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaScannerService.java b/src/com/android/providers/media/MediaScannerService.java
//Synthetic comment -- index 0c084bd..da8dd13 100644

//Synthetic comment -- @@ -257,8 +257,17 @@
}
else if (MediaProvider.EXTERNAL_VOLUME.equals(volume)) {
// scan external storage
directories = new String[] {
                                Environment.getExternalStorageDirectory().getPath(),
};
}








