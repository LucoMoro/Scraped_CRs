/*Make locale safe paths

getVideoOutputMediaFileTitle was using SimpleDateFormat
with the default locale to create paths. In some locales
this resulted in characters that are not safe to use on
all file systems. Explicitly use Locale.US to be locale
safe.

Change-Id:I3727a239396da82146a171d254a9bf5a5f8dcfef*/
//Synthetic comment -- diff --git a/src/com/android/videoeditor/VideoEditorActivity.java b/src/com/android/videoeditor/VideoEditorActivity.java
//Synthetic comment -- index 193bae3..ca0b770 100755

//Synthetic comment -- @@ -18,6 +18,7 @@

import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
//Synthetic comment -- @@ -665,7 +666,7 @@
private String getVideoOutputMediaFileTitle() {
long dateTaken = System.currentTimeMillis();
Date date = new Date(dateTaken);
        SimpleDateFormat dateFormat = new SimpleDateFormat("'VID'_yyyyMMdd_HHmmss");

return dateFormat.format(date);
}







