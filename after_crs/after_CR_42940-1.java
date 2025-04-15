/*Don't replace file extension when mime-type is incorrect

When downloading content from a server that claims content to be
text/plain or application/octet-stream a guess is made of the proper
mime-type from a possible "file-extension" in the URL. When creating
the filename of the downloaded content any file extension that does
not match the mime-type is replaced with one derived from the
mime-type (.txt for text/plain, none for application/octet-stream).

However the guessed mime-type is not used in the filename
creation, so content with a proper file extension but a text/plain
mime-type will have its file extension replaced with .txt derived
from the incorrect mime-type.

This fix will use the guessed mime-type when creating the filename
to avoid replacing a correct file extension.

Change-Id:I5df642e94948914708af99a4d902b253ac8a48dd*/




//Synthetic comment -- diff --git a/src/com/android/browser/FetchUrlMimeType.java b/src/com/android/browser/FetchUrlMimeType.java
//Synthetic comment -- index 07c9b93..33b5808 100644

//Synthetic comment -- @@ -121,6 +121,7 @@
MimeTypeMap.getSingleton().getMimeTypeFromExtension(
MimeTypeMap.getFileExtensionFromUrl(mUri));
if (newMimeType != null) {
                   mimeType = newMimeType;
mRequest.setMimeType(newMimeType);
}
}







