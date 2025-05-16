//<Beginning of snippet n. 0>


import android.database.SQLException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

private void pruneDeadThumbnailFiles(Context context) {
    if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        return;
    }
    
    HashSet<String> existingFiles = new HashSet<String>();
    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/.thumbnails";
    File thumbnailDir = new File(directory);
    
    if (!thumbnailDir.exists() || !thumbnailDir.isDirectory()) {
        return;
    }
    
    String[] files = thumbnailDir.list();
    if (files == null) {
        files = new String[0];
    }

//<End of snippet n. 0>