/*BackupManagerService files need new security labeling.

Use restorecon to label files and directories
specific to BMS.

 * /data/backup : provide default type labeling
    for all files/dirs used by BMS.

 * /data/secure/backup : provide default type
    labeling used by BMS when encrypted
    file system is enabled.

 * /cache/<package name>.{data|restore} :
    specifically target files opened and
    passed by BMS to apps that have backup
    capabilities.

 * /cache/backup : default labeling for the
    directory used by the LocalTransport
    that stores backup files for local testing.

Change-Id:Id4d46e82cd13abc2e395e0c30c515f0a33df93e7Signed-off-by: rpcraig <rpcraig@tycho.ncsc.mil>*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/backup/LocalTransport.java b/core/java/com/android/internal/backup/LocalTransport.java
//Synthetic comment -- index eed3e67..eb2d1fe 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;

import com.android.org.bouncycastle.util.encoders.Base64;
//Synthetic comment -- @@ -64,6 +65,10 @@

public LocalTransport(Context context) {
mContext = context;
}

public Intent configurationIntent() {








//Synthetic comment -- diff --git a/services/java/com/android/server/BackupManagerService.java b/services/java/com/android/server/BackupManagerService.java
//Synthetic comment -- index 7ac314b..91e3330 100644

//Synthetic comment -- @@ -63,6 +63,7 @@
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
//Synthetic comment -- @@ -743,6 +744,9 @@
// correct directory.
mBaseStateDir = new File(Environment.getSecureDataDirectory(), "backup");
mBaseStateDir.mkdirs();
mDataDir = Environment.getDownloadCacheDirectory();

mPasswordHashFile = new File(mBaseStateDir, "pwhash");
//Synthetic comment -- @@ -2133,6 +2137,10 @@
ParcelFileDescriptor.MODE_CREATE |
ParcelFileDescriptor.MODE_TRUNCATE);

mNewState = ParcelFileDescriptor.open(mNewStateName,
ParcelFileDescriptor.MODE_READ_WRITE |
ParcelFileDescriptor.MODE_CREATE |
//Synthetic comment -- @@ -4572,6 +4580,10 @@
ParcelFileDescriptor.MODE_CREATE |
ParcelFileDescriptor.MODE_TRUNCATE);

if (mTransport.getRestoreData(mBackupData) != BackupConstants.TRANSPORT_OK) {
// Transport-level failure, so we wind everything up and
// terminate the restore operation.







