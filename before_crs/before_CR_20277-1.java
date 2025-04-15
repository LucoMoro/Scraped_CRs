/*Replaced /sdcard/ with Method call

Change-Id:I6f669fc2315041e938ff12fc6fc76c871488df45*/
//Synthetic comment -- diff --git a/media/java/android/media/MediaScanner.java b/media/java/android/media/MediaScanner.java
//Synthetic comment -- index 3333268..61900ee 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import android.database.SQLException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;
//Synthetic comment -- @@ -1054,7 +1055,7 @@

private void pruneDeadThumbnailFiles() {
HashSet<String> existingFiles = new HashSet<String>();
        String directory = "/sdcard/DCIM/.thumbnails";
String [] files = (new File(directory)).list();
if (files == null)
files = new String[0];







