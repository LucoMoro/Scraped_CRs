/*Make locale safe paths

getTarget was using SimpleDateFormat with the default
locale to create paths. In some locales this resulted
in characters that are not safe to use on all file
systems. Explicitly use Locale.US to be locale safe.

Change-Id:Id0d994231fa82d5695e7b0fdbae32333b9e77a6e*/




//Synthetic comment -- diff --git a/src/com/android/browser/Controller.java b/src/com/android/browser/Controller.java
//Synthetic comment -- index 56a9e20..d9ad2e0 100644

//Synthetic comment -- @@ -98,6 +98,7 @@
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
//Synthetic comment -- @@ -2228,7 +2229,7 @@
*/
private File getTarget(DataUri uri) throws IOException {
File dir = mActivity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            DateFormat format = new SimpleDateFormat(IMAGE_BASE_FORMAT, Locale.US);
String nameBase = format.format(new Date());
String mimeType = uri.getMimeType();
MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();







