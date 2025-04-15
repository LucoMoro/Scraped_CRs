/*Fixing media scanner inappropriate removal of media files.

This commit fixes the inappropriate removal of media files
after adding a .nomedia file into a folder that has been
previously scanned.

for details see:http://code.google.com/p/android/issues/detail?id=3692Change-Id:I307a405bbd2faa257925364c270995c7a5166202Signed-off-by: David Sobreira Marques <dpsmarques@gmail.com>*/




//Synthetic comment -- diff --git a/media/java/android/media/MediaScanner.java b/media/java/android/media/MediaScanner.java
//Synthetic comment -- index 4296afb..9627dd9 100644

//Synthetic comment -- @@ -1099,6 +1099,14 @@
if (inScanDirectory(path, directories)) {
// we didn't see this file in the scan directory.
fileMissing = true;
                    
                    // If the file was not seen it has been either removed
                    // or is inside a hidden folder. For the last case set
                    // the data field empty to avoid hidden files removal
                    // before removing from media provider.
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.MediaColumns.DATA, "");
                    mMediaProvider.update(ContentUris.withAppendedId(entry.mTableUri, entry.mRowId), values, null, null);
} else {
// the file is outside of our scan directory,
// so we need to check for file existence here.







