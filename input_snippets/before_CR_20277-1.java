
//<Beginning of snippet n. 0>


import android.database.SQLException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;

private void pruneDeadThumbnailFiles() {
HashSet<String> existingFiles = new HashSet<String>();
        String directory = "/sdcard/DCIM/.thumbnails";
String [] files = (new File(directory)).list();
if (files == null)
files = new String[0];

//<End of snippet n. 0>








