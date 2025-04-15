/*Make sure OutOfMemoryError is handled by WallpaperManager

Make sure exception OutOfMemoryError is handled when calling
BitmapFactory.decodeFileDescriptor and BitmapFactory.decodeStream
to avoid crash in the system server.

Change-Id:I954a6388d1225dab86d2617ab0602154b2a7f493*/
//Synthetic comment -- diff --git a/core/java/android/app/WallpaperManager.java b/core/java/android/app/WallpaperManager.java
//Synthetic comment -- index e455a59..c6ccd7f 100644

//Synthetic comment -- @@ -235,8 +235,13 @@
if (width <= 0 || height <= 0) {
// Degenerate case: no size requested, just load
// bitmap as-is.
                        Bitmap bm = BitmapFactory.decodeFileDescriptor(
                                fd.getFileDescriptor(), null, null);
try {
fd.close();
} catch (IOException e) {
//Synthetic comment -- @@ -277,7 +282,12 @@
if (width <= 0 || height <= 0) {
// Degenerate case: no size requested, just load
// bitmap as-is.
                        Bitmap bm = BitmapFactory.decodeStream(is, null, null);
try {
is.close();
} catch (IOException e) {







