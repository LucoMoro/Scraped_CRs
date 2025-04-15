/*Set source of VideoModel to fullname of video file

Some operator will only send the MT the part I (smil) multipart body of Mms
m-retrieve-conf without part II (the real mutimedia files) if "src", "Content-
Type", "Name", "Content-Id" and "Content-Location" of the Mms m-send-req are
set to the full path in MT.

So MT will retrieve a Mms recognized as a Video Mms, but with no parts in
this case.

Change-Id:I2b70a0dbb45f081a93781a639bcebab87cf2d095Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/model/VideoModel.java b/src/com/android/mms/model/VideoModel.java
//Synthetic comment -- index a426b42..a71e455 100644

//Synthetic comment -- @@ -70,7 +70,8 @@
}

private void initFromFile(Uri uri) {
        String path = uri.getPath();
        mSrc = path.substring(path.lastIndexOf('/') + 1);
MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
String extension = MimeTypeMap.getFileExtensionFromUrl(mSrc);
if (TextUtils.isEmpty(extension)) {







