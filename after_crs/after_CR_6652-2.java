/*Force sync the downloaded file to the storage after completion.

This will write the downlaoded file to the storage (sdcard in default).
It can prevent file corruption if the user removes the sdcard unsafely
after a download completes.*/




//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadThread.java b/src/com/android/providers/downloads/DownloadThread.java
//Synthetic comment -- index 127cc46..e787229 100644

//Synthetic comment -- @@ -38,10 +38,12 @@
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SyncFailedException;
import java.net.URI;
import java.net.URISyntaxException;

//Synthetic comment -- @@ -672,6 +674,19 @@
} else if (Downloads.isStatusSuccess(finalStatus)) {
// make sure the file is readable
FileUtils.setPermissions(filename, 0644, -1, -1);

                    // Sync to storage after completion
                    try {
                        new FileOutputStream(filename, true).getFD().sync();
                    } catch (FileNotFoundException ex) {
                        Log.w(Constants.TAG, "file " + filename + " not found: " + ex);
                    } catch (SyncFailedException ex) {
                        Log.w(Constants.TAG, "file " + filename + " sync failed: " + ex);
                    } catch (IOException ex) {
                        Log.w(Constants.TAG, "IOException trying to sync " + filename + ": " + ex);
                    } catch (RuntimeException ex) {
                        Log.w(Constants.TAG, "exception while syncing file: ", ex);
                    }
}
}
notifyDownloadCompleted(finalStatus, countRetry, retryAfter, redirectCount,







