/*Set "isdrm" info in Mediastore

The mediascanner member mIsDrm was never set
according to the drmframework canHandle call.
This subsequently caused that the isdrm column
in mediastore was never set, and was defaulted
to false for all files.
mIsDrm is now set according to drmframework
canHandle result for each files that is scanned.

Change-Id:Id557d921c4e3e3dfc35da56b69471f4bd6b3c8bf*/
//Synthetic comment -- diff --git a/media/java/android/media/MediaScanner.java b/media/java/android/media/MediaScanner.java
//Synthetic comment -- index 0117d73..0f01aae 100644

//Synthetic comment -- @@ -441,6 +441,7 @@
mMimeType = mimeType;
mFileType = 0;
mFileSize = fileSize;

if (!isDirectory) {
if (!noMedia && isNoMediaFile(path)) {
//Synthetic comment -- @@ -503,7 +504,6 @@
mLastModified = lastModified;
mWriter = null;
mCompilation = 0;
            mIsDrm = false;
mWidth = 0;
mHeight = 0;

//Synthetic comment -- @@ -1039,6 +1039,7 @@
}

if (mDrmManagerClient.canHandle(path, null)) {
String drmMimetype = mDrmManagerClient.getOriginalMimeType(path);
if (drmMimetype != null) {
mMimeType = drmMimetype;







