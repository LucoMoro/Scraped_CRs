/*Apps could not be moved to SD card for some languages

Locale was not specified for String.format.
If having a language set in the phone which uses
Non-West-Arabic Numerals, the resulting string will have
representations of the int values (sizeMb and ownerUid)
that couldnt be handled when they later where used.
(system/vold/CommandListener.cpp::AsecCmd::runCommand,
value of numSectors becomes 0)

Change-Id:I0af84fa1aafe7b0d614be5bd1a3f25a64892268f*/
//Synthetic comment -- diff --git a/core/tests/coretests/src/android/os/storage/AsecTests.java b/core/tests/coretests/src/android/os/storage/AsecTests.java
//Synthetic comment -- index 5efbd88..4374446 100755

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -12,26 +13,35 @@
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

import junit.framework.Assert;

public class AsecTests extends AndroidTestCase {
private static final boolean localLOGV = true;
public static final String TAG="AsecTests";

void failStr(String errMsg) {
Log.w(TAG, "errMsg="+errMsg);
//Synthetic comment -- @@ -150,6 +160,28 @@
}
}

public void testCreateContainer() {
try {
Assert.assertEquals(StorageResultCode.OperationSucceeded,
//Synthetic comment -- @@ -160,6 +192,19 @@
}
}

public void testCreateMinSizeContainer() {
try {
Assert.assertEquals(StorageResultCode.OperationSucceeded,








//Synthetic comment -- diff --git a/services/java/com/android/server/MountService.java b/services/java/com/android/server/MountService.java
//Synthetic comment -- index 5425813..1cee8ed 100644

//Synthetic comment -- @@ -1456,7 +1456,7 @@
warnOnNotMounted();

int rc = StorageResultCode.OperationSucceeded;
        String cmd = String.format("asec create %s %d %s %s %d", id, sizeMb, fstype, key, ownerUid);
try {
mConnector.doCommand(cmd);
} catch (NativeDaemonConnectorException e) {







