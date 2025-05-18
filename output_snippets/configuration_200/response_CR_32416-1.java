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
try {
Assert.assertEquals(StorageResultCode.OperationSucceeded,
}
}

public void testCreateMinSizeContainer() {
try {
Assert.assertEquals(StorageResultCode.OperationSucceeded,

//<End of snippet n. 0>




 // <Beginning of snippet n. 1>

warnOnNotMounted();

if (sizeMb < 0 || ownerUid < 0) {
    throw new IllegalArgumentException("Invalid values for sizeMb or ownerUid");
}

Locale currentLocale = Locale.getDefault();
String cmd = String.format(currentLocale, "asec create %s %d %s %s %d", id, sizeMb, fstype, key, ownerUid);
try {
    mConnector.doCommand(cmd);
} catch (NativeDaemonConnectorException e) {
    Log.e(TAG, "Command execution failed. Command: " + cmd + ", sizeMb: " + sizeMb + ", ownerUid: " + ownerUid, e);
}

//<End of snippet n. 1>
