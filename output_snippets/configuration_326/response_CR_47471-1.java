//<Beginning of snippet n. 0>
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;

import com.android.org.bouncycastle.util.encoders.Base64;

public LocalTransport(Context context) {
    mContext = context;
}

public Intent configurationIntent() {
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import java.io.File;
import android.os.FileUtils;

mBaseStateDir = new File(Environment.getSecureDataDirectory(), "backup");
mBaseStateDir.mkdirs();
FileUtils.restorecon(mBaseStateDir);

mDataDir = Environment.getDownloadCacheDirectory();

mPasswordHashFile = new File(mBaseStateDir, "pwhash");
ParcelFileDescriptor.open(mPasswordHashFile,
        ParcelFileDescriptor.MODE_CREATE |
        ParcelFileDescriptor.MODE_TRUNCATE);

mNewState = ParcelFileDescriptor.open(mNewStateName,
        ParcelFileDescriptor.MODE_READ_WRITE |
        ParcelFileDescriptor.MODE_CREATE |
        ParcelFileDescriptor.MODE_TRUNCATE);

if (mTransport.getRestoreData(mBackupData) != BackupConstants.TRANSPORT_OK) {
    // Transport-level failure, so we wind everything up and
    // terminate the restore operation.
}

// Apply restorecon for secure backup directory
File secureBackupDir = new File(Environment.getSecureDataDirectory(), "backup");
FileUtils.restorecon(secureBackupDir);

// Apply restorecon for cache files
File cacheFile = new File(Environment.getDownloadCacheDirectory(), "<package name>.data");
FileUtils.restorecon(cacheFile);
cacheFile = new File(Environment.getDownloadCacheDirectory(), "<package name>.restore");
FileUtils.restorecon(cacheFile);

// Apply restorecon for cache backup directory
File cacheBackupDir = new File(Environment.getDownloadCacheDirectory(), "backup");
FileUtils.restorecon(cacheBackupDir);
//<End of snippet n. 1>