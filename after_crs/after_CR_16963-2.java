/*Make sure OutOfMemoryError is handled by WallpaperManager

Make sure exception OutOfMemoryError is handled when calling
BitmapFactory.decodeFileDescriptor and BitmapFactory.decodeStream
to avoid crash in the system server.

Change-Id:I954a6388d1225dab86d2617ab0602154b2a7f493*/




//Synthetic comment -- diff --git a/core/java/android/app/WallpaperManager.java b/core/java/android/app/WallpaperManager.java
//Synthetic comment -- index e455a59..92b7cf51 100644

//Synthetic comment -- @@ -235,8 +235,13 @@
if (width <= 0 || height <= 0) {
// Degenerate case: no size requested, just load
// bitmap as-is.
                        Bitmap bm = null;
                        try {
                            bm = BitmapFactory.decodeFileDescriptor(
                                   fd.getFileDescriptor(), null, null);
                        } catch (OutOfMemoryError e) {
                            Log.w(TAG, "Can't decode file", e);
                        }
try {
fd.close();
} catch (IOException e) {
//Synthetic comment -- @@ -277,7 +282,12 @@
if (width <= 0 || height <= 0) {
// Degenerate case: no size requested, just load
// bitmap as-is.
                        Bitmap bm = null;
                        try {
                            bm = BitmapFactory.decodeStream(is, null, null);
                        } catch (OutOfMemoryError e) {
                            Log.w(TAG, "Can't decode stream", e);
                        }
try {
is.close();
} catch (IOException e) {







