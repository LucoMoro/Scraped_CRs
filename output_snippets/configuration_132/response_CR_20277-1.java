//<Beginning of snippet n. 0>


import android.database.SQLException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.Environment;

private void pruneDeadThumbnailFiles() {
    HashSet<String> existingFiles = new HashSet<String>();
    String directory = getThumbnailDirectory();
    String[] files = (new File(directory)).list();
    if (files == null)
        files = new String[0];
}

private String getThumbnailDirectory() {
    return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/.thumbnails";
}

//<End of snippet n. 0>