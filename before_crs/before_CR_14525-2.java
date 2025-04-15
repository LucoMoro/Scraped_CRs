/*Fix for bug 2467152 files with spaces fail to open.

fromhttp://code.google.com/p/android/issues/detail?id=4638If you try to use a WebView to open an image (of any type)
that is stored in your project's assets/ directory the image
will be written to the screen as text (instead of drawn as
an image) if the filename contains a space.

The problem stems from MimeTypeMap, which determines the
file type from the url.  Spaces, represented as '%20'
are not included in the list of acceptable characters for
an image file name, causing the image to be treated as plain
text.  I am remedying this by adding '%' to the list.

Change-Id:I29e3da57f3cdaa63ed60b1e6977ba62a0dd108e5*/
//Synthetic comment -- diff --git a/core/java/android/webkit/MimeTypeMap.java b/core/java/android/webkit/MimeTypeMap.java
//Synthetic comment -- index fffba1b..60dfce7 100644

//Synthetic comment -- @@ -67,7 +67,7 @@
// if the filename contains special characters, we don't
// consider it valid for our matching purposes:
if (filename.length() > 0 &&
                Pattern.matches("[a-zA-Z_0-9\\.\\-\\(\\)]+", filename)) {
int dotPos = filename.lastIndexOf('.');
if (0 <= dotPos) {
return filename.substring(dotPos + 1);







