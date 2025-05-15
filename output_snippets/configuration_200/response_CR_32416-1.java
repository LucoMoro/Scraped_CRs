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

    public void testCreateContainer() {
        try {
            Assert.assertEquals(StorageResultCode.OperationSucceeded,
            // missing logic for the test
        } catch (Exception e) {
            failStr(e.getMessage());
        }
    }

    public void testCreateMinSizeContainer() {
        try {
            Assert.assertEquals(StorageResultCode.OperationSucceeded,
            // missing logic for the test
        } catch (Exception e) {
            failStr(e.getMessage());
        }
    }
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


warnOnNotMounted();

int rc = StorageResultCode.OperationSucceeded;
Locale locale = Locale.US; // Specifying the locale
String cmd = String.format(locale, "asec create %s %d %s %s %d", id, sizeMb, fstype, key, ownerUid);
try {
    mConnector.doCommand(cmd);
} catch (NativeDaemonConnectorException e) {
    Log.e(TAG, "Command execution failed with error: " + e.getMessage() + " using locale: " + locale);
}

//<End of snippet n. 1>