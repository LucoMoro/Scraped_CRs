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
public static final String TAG="AsecTests";

void failStr(String errMsg) {
Log.w(TAG, "errMsg="+errMsg);
}

public void testCreateContainer() {
    int sizeMb = 1; // Example value, should be validated
    int ownerUid = 1000; // Example value, should be validated
    String id = "exampleId"; // Example value
    String fstype = "ext4"; // Example value
    String key = "exampleKey"; // Example value

    if (sizeMb <= 0) {
        failStr("Invalid sizeMb: " + sizeMb);
        return;
    }
    if (ownerUid <= 0) {
        failStr("Invalid ownerUid: " + ownerUid);
        return;
    }

    try {
        Assert.assertEquals(StorageResultCode.OperationSucceeded,
            performCreateContainer(id, sizeMb, fstype, key, ownerUid));
    } catch (Exception e) {
        failStr("Exception occurred: " + e.getMessage());
    }
}

public void testCreateMinSizeContainer() {
    int sizeMb = 1; // Example value, should be validated
    int ownerUid = 1000; // Example value, should be validated
    String id = "exampleId"; // Example value
    String fstype = "ext4"; // Example value
    String key = "exampleKey"; // Example value

    if (sizeMb <= 0) {
        failStr("Invalid sizeMb: " + sizeMb);
        return;
    }
    if (ownerUid <= 0) {
        failStr("Invalid ownerUid: " + ownerUid);
        return;
    }

    try {
        Assert.assertEquals(StorageResultCode.OperationSucceeded,
            performCreateContainer(id, sizeMb, fstype, key, ownerUid));
    } catch (Exception e) {
        failStr("Exception occurred: " + e.getMessage());
    }
}

private int performCreateContainer(String id, int sizeMb, String fstype, String key, int ownerUid) {
    warnOnNotMounted();

    String cmd = String.format(Locale.US, "asec create %s %d %s %s %d", id, sizeMb, fstype, key, ownerUid);
    int rc = StorageResultCode.OperationSucceeded;
    try {
        mConnector.doCommand(cmd);
    } catch (NativeDaemonConnectorException e) {
        failStr("Command execution failed: " + e.getMessage());
    }
    return rc;
}
//<End of snippet n. 0>