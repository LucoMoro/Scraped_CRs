/*Fix in MethodProfilingHandler.

The local and remote file paths were inverted.

Change-Id:Ibf83f236473c18b71ab59629199496889e717870*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/handler/MethodProfilingHandler.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/handler/MethodProfilingHandler.java
//Synthetic comment -- index f6e2f19..10680f7 100644

//Synthetic comment -- @@ -17,14 +17,14 @@
package com.android.ddmuilib.handler;

import com.android.ddmlib.Client;
import com.android.ddmlib.ClientData.IMethodProfilingHandler;
import com.android.ddmlib.DdmConstants;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.SyncService;
import com.android.ddmlib.SyncService.ISyncProgressMonitor;
import com.android.ddmlib.TimeoutException;
import com.android.ddmuilib.DdmUiPreferences;
import com.android.ddmuilib.SyncProgressHelper;
import com.android.ddmuilib.SyncProgressHelper.SyncRunnable;
//Synthetic comment -- @@ -126,7 +126,7 @@
SyncProgressHelper.run(new SyncRunnable() {
public void run(ISyncProgressMonitor monitor)
throws SyncException, IOException, TimeoutException {
                        sync.pullFile(remoteFilePath, tempPath, monitor);
}

public void close() {







