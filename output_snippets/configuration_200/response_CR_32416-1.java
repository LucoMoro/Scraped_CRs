//<Beginning of snippet n. 0>
/*
* Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package android.os.storage;

import android.content.Context;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.test.AndroidTestCase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

import junit.framework.Assert;

public class AsecTests extends AndroidTestCase {
    private static final boolean localLOGV = true;
    public static final String TAG = "AsecTests";

    void failStr(String errMsg) {
        Log.w(TAG, "errMsg=" + errMsg);
    }

    private boolean isValidSize(int sizeMb) {
        return sizeMb > 0;
    }

    private boolean isValidOwnerUid(int ownerUid) {
        return ownerUid > 0;
    }

    public void testCreateContainer() {
        try {
            int id = 0; 
            int sizeMb = 100;
            String fstype = "ext4";
            String key = "myKey"; 
            int ownerUid = 1000; 

            if (!isValidSize(sizeMb) || !isValidOwnerUid(ownerUid)) {
                failStr("Invalid parameters for create container");
                return;
            }

            Assert.assertEquals(StorageResultCode.OperationSucceeded,
                mConnector.doCommand(String.format(Locale.US, "create %d %d %s %s %d", id, sizeMb, fstype, key, ownerUid)));
        } catch (Exception e) {
            failStr("Command execution failed: " + e.getMessage());
        }
    }

    public void testCreateMinSizeContainer() {
        try {
            int id = 1; 
            int sizeMb = 10; 
            String fstype = "ext4"; 
            String key = "myMinKey"; 
            int ownerUid = 1000; 

            if (!isValidSize(sizeMb) || !isValidOwnerUid(ownerUid)) {
                failStr("Invalid parameters for createMinSize container");
                return;
            }

            Assert.assertEquals(StorageResultCode.OperationSucceeded,
                mConnector.doCommand(String.format(Locale.US, "createMinSize %d %d %s %s %d", id, sizeMb, fstype, key, ownerUid)));
        } catch (Exception e) {
            failStr("Command execution failed: " + e.getMessage());
        }
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
warnOnNotMounted();

String cmd = String.format(Locale.US, "asec create %d %d %s %s %d", id, sizeMb, fstype, key, ownerUid);
if (!isValidSize(sizeMb) || !isValidOwnerUid(ownerUid)) {
    failStr("Invalid parameters for asec create command");
    return;
}
try {
    mConnector.doCommand(cmd);
} catch (NativeDaemonConnectorException e) {
    failStr("Command execution failed: " + e.getMessage());
}
//<End of snippet n. 1>