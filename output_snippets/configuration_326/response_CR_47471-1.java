//<Beginning of snippet n. 0>


import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import java.io.File;

import com.android.org.bouncycastle.util.encoders.Base64;

public LocalTransport(Context context) {
    mContext = context;
}

public Intent configurationIntent() {
    // Implement restorecon calls for backup file labeling
    try {
        File backupDir = new File(Environment.getDataDirectory(), "backup");
        backupDir.mkdirs();
        ProcessBuilder pb = new ProcessBuilder("restorecon", "-R", backupDir.getAbsolutePath());
        pb.start().waitFor();
        
        if (isEncryptedFileSystemEnabled()) {
            File secureBackupDir = new File(Environment.getSecureDataDirectory(), "backup");
            secureBackupDir.mkdirs();
            pb = new ProcessBuilder("restorecon", "-R", secureBackupDir.getAbsolutePath());
            pb.start().waitFor();
        }
        
        File cacheDir = new File(Environment.getDownloadCacheDirectory(), "<package_name>.data");
        cacheDir.mkdirs();
        pb = new ProcessBuilder("restorecon", "-R", cacheDir.getAbsolutePath());
        pb.start().waitFor();
        
        File cacheBackupDir = new File(Environment.getDownloadCacheDirectory(), "backup");
        cacheBackupDir.mkdirs();
        pb = new ProcessBuilder("restorecon", "-R", cacheBackupDir.getAbsolutePath());
        pb.start().waitFor();
        
    } catch (Exception e) {
        Log.e("LocalTransport", "restorecon failed", e);
    }
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>


import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import java.io.File;

// Correct directory.
mBaseStateDir = new File(Environment.getSecureDataDirectory(), "backup");
mBaseStateDir.mkdirs();
mDataDir = Environment.getDownloadCacheDirectory();

mPasswordHashFile = new File(mBaseStateDir, "pwhash");
ParcelFileDescriptor pfd = ParcelFileDescriptor.open(mPasswordHashFile, 
    ParcelFileDescriptor.MODE_CREATE | ParcelFileDescriptor.MODE_TRUNCATE);

mNewState = ParcelFileDescriptor.open(mNewStateName,
    ParcelFileDescriptor.MODE_READ_WRITE |
    ParcelFileDescriptor.MODE_CREATE |
    ParcelFileDescriptor.MODE_TRUNCATE);

if (mTransport.getRestoreData(mBackupData) != BackupConstants.TRANSPORT_OK) {
    // Transport-level failure, so we wind everything up and
    // terminate the restore operation.
//<End of snippet n. 1>