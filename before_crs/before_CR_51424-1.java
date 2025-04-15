/*Fix import paths for LogCatMessage

Change-Id:If1068713f3faa60db2a22e7dc6464a2af4928743*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/LogCatMonitor.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/LogCatMonitor.java
//Synthetic comment -- index 65ddbd4..e99a637 100644

//Synthetic comment -- @@ -19,8 +19,8 @@
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmuilib.logcat.ILogCatBufferChangeListener;
import com.android.ddmuilib.logcat.LogCatMessage;
import com.android.ddmuilib.logcat.LogCatReceiver;
import com.android.ddmuilib.logcat.LogCatReceiverFactory;
import com.android.ide.eclipse.ddms.views.LogCatView;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/LogCatView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/LogCatView.java
//Synthetic comment -- index 3514db0..9f78c4a 100644

//Synthetic comment -- @@ -15,8 +15,8 @@
*/
package com.android.ide.eclipse.ddms.views;

import com.android.ddmuilib.logcat.ILogCatMessageSelectionListener;
import com.android.ddmuilib.logcat.LogCatMessage;
import com.android.ddmuilib.logcat.LogCatPanel;
import com.android.ddmuilib.logcat.LogCatStackTraceParser;
import com.android.ide.eclipse.ddms.DdmsPlugin;







