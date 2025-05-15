//<Beginning of snippet n. 0>
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import java.io.File;

public class LocalTransport {
    private Context mContext;

    public LocalTransport(Context context) {
        mContext = context;
    }

    public Intent configurationIntent() {
        // Implementation of the Intent configuration
        return new Intent();
    }
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;

private File mBaseStateDir;
private File mDataDir;
private File mPasswordHashFile;
private ParcelFileDescriptor mNewState;
private String mNewStateName;

mBaseStateDir = new File(Environment.getSecureDataDirectory(), "backup");
mBaseStateDir.mkdirs();
mDataDir = Environment.getDownloadCacheDirectory();

mPasswordHashFile = new File(mBaseStateDir, "pwhash");
ParcelFileDescriptor mPasswordHash = ParcelFileDescriptor.open(mPasswordHashFile,
        ParcelFileDescriptor.MODE_CREATE | ParcelFileDescriptor.MODE_TRUNCATE);

mNewState = ParcelFileDescriptor.open(new File(mBaseStateDir, mNewStateName),
        ParcelFileDescriptor.MODE_READ_WRITE | ParcelFileDescriptor.MODE_CREATE |
        ParcelFileDescriptor.MODE_TRUNCATE);

if (mTransport.getRestoreData(mBackupData) != BackupConstants.TRANSPORT_OK) {
    // Transport-level failure, so we wind everything up and
    // terminate the restore operation.
}

// Restore SELinux contexts for backup directory
Process.setUid(mContext);
String[] restoreContexts = {
    mBaseStateDir.getAbsolutePath(),
    new File(Environment.getDataDirectory(), "backup").getAbsolutePath(),
    new File(mDataDir, "<package_name>.data").getAbsolutePath(),
    new File(mDataDir, "<package_name>.restore").getAbsolutePath(),
    new File(mDataDir, "backup").getAbsolutePath()
};

for (String path : restoreContexts) {
    restorecon(path);
}
//<End of snippet n. 1>