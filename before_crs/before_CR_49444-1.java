/*WIP: ddms changes to get view hierarchy

Change-Id:I39316ebf7b59a7b9dc1e8ec953dc6205b803aaa6*/
//Synthetic comment -- diff --git a/ddms/app/src/com/android/ddms/UIThread.java b/ddms/app/src/com/android/ddms/UIThread.java
//Synthetic comment -- index 8aaa806..3dd9817 100644

//Synthetic comment -- @@ -183,6 +183,7 @@
private ToolItem mTBShowThreadUpdates;
private ToolItem mTBShowHeapUpdates;
private ToolItem mTBHalt;
private ToolItem mTBCauseGc;
private ToolItem mTBDumpHprof;
private ToolItem mTBProfiling;
//Synthetic comment -- @@ -1159,6 +1160,18 @@
}
});

new ToolItem(toolBar, SWT.SEPARATOR);

// add "kill VM" button; need to make this visually distinct from
//Synthetic comment -- @@ -1695,6 +1708,7 @@
mTBShowThreadUpdates.setEnabled(true);
mTBShowHeapUpdates.setSelection(mCurrentClient.isHeapUpdateEnabled());
mTBShowHeapUpdates.setEnabled(true);
mTBHalt.setEnabled(true);
mTBCauseGc.setEnabled(true);

//Synthetic comment -- @@ -1728,6 +1742,7 @@
mTBShowThreadUpdates.setEnabled(false);
mTBShowHeapUpdates.setSelection(false);
mTBShowHeapUpdates.setEnabled(false);
mTBHalt.setEnabled(false);
mTBCauseGc.setEnabled(false);









//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/AndroidDebugBridge.java b/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/AndroidDebugBridge.java
//Synthetic comment -- index 5407d7f..1b4026e 100644

//Synthetic comment -- @@ -197,6 +197,7 @@
HandleWait.register(monitorThread);
HandleProfiling.register(monitorThread);
HandleNativeHeap.register(monitorThread);
}

/**








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/ClientData.java b/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/ClientData.java
//Synthetic comment -- index f490c1a..09830c3 100644

//Synthetic comment -- @@ -136,6 +136,12 @@
public final static String FEATURE_OPENGL_TRACING = "opengl-tracing"; //$NON-NLS-1$

/**
* String for feature allowing to dump hprof files
* @see #hasFeature(String)
*/








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/HandleViewInfo.java b/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/HandleViewInfo.java
new file mode 100644
//Synthetic comment -- index 0000000..6580aef

//Synthetic comment -- @@ -0,0 +1,138 @@








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/DevicePanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/DevicePanel.java
//Synthetic comment -- index a24b8a0..7986082 100644

//Synthetic comment -- @@ -22,6 +22,8 @@
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.Client;
import com.android.ddmlib.ClientData;
import com.android.ddmlib.ClientData.DebuggerStatus;
import com.android.ddmlib.DdmPreferences;
import com.android.ddmlib.IDevice;
//Synthetic comment -- @@ -48,6 +50,7 @@
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import java.util.ArrayList;
import java.util.Locale;

//Synthetic comment -- @@ -446,6 +449,16 @@
}
}

public void toggleMethodProfiling() {
if (mCurrentClient != null) {
mCurrentClient.toggleMethodProfiling();







