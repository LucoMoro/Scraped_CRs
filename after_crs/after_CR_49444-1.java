/*WIP: ddms changes to get view hierarchy

Change-Id:I39316ebf7b59a7b9dc1e8ec953dc6205b803aaa6*/




//Synthetic comment -- diff --git a/ddms/app/src/com/android/ddms/UIThread.java b/ddms/app/src/com/android/ddms/UIThread.java
//Synthetic comment -- index 8aaa806..3dd9817 100644

//Synthetic comment -- @@ -183,6 +183,7 @@
private ToolItem mTBShowThreadUpdates;
private ToolItem mTBShowHeapUpdates;
private ToolItem mTBHalt;
    private ToolItem mTBDumpView;
private ToolItem mTBCauseGc;
private ToolItem mTBDumpHprof;
private ToolItem mTBProfiling;
//Synthetic comment -- @@ -1159,6 +1160,18 @@
}
});

        mTBDumpView = new ToolItem(toolBar, SWT.PUSH);
        mTBDumpView.setToolTipText("Dump View Hierarchy");
        mTBDumpView.setEnabled(true);
        mTBDumpView.setImage(mDdmUiLibLoader.loadImage(display,
                DevicePanel.ICON_HALT, DevicePanel.ICON_WIDTH, DevicePanel.ICON_WIDTH, null));
        mTBDumpView.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                mDevicePanel.dumpViewHierarchy();
            }
        });

new ToolItem(toolBar, SWT.SEPARATOR);

// add "kill VM" button; need to make this visually distinct from
//Synthetic comment -- @@ -1695,6 +1708,7 @@
mTBShowThreadUpdates.setEnabled(true);
mTBShowHeapUpdates.setSelection(mCurrentClient.isHeapUpdateEnabled());
mTBShowHeapUpdates.setEnabled(true);
            mTBDumpView.setEnabled(true);
mTBHalt.setEnabled(true);
mTBCauseGc.setEnabled(true);

//Synthetic comment -- @@ -1728,6 +1742,7 @@
mTBShowThreadUpdates.setEnabled(false);
mTBShowHeapUpdates.setSelection(false);
mTBShowHeapUpdates.setEnabled(false);
            mTBDumpView.setEnabled(false);
mTBHalt.setEnabled(false);
mTBCauseGc.setEnabled(false);









//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/AndroidDebugBridge.java b/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/AndroidDebugBridge.java
//Synthetic comment -- index 5407d7f..1b4026e 100644

//Synthetic comment -- @@ -197,6 +197,7 @@
HandleWait.register(monitorThread);
HandleProfiling.register(monitorThread);
HandleNativeHeap.register(monitorThread);
        HandleViewInfo.register(monitorThread);
}

/**








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/ClientData.java b/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/ClientData.java
//Synthetic comment -- index f490c1a..09830c3 100644

//Synthetic comment -- @@ -136,6 +136,12 @@
public final static String FEATURE_OPENGL_TRACING = "opengl-tracing"; //$NON-NLS-1$

/**
     * String for feature indicating support for providing view hierarchy.
     * @see #hasFeature(String)
     */
    public final static String FEATURE_VIEW_HIERARCHY = "view-hierarchy"; //$NON-NLS-1$

    /**
* String for feature allowing to dump hprof files
* @see #hasFeature(String)
*/








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/HandleViewInfo.java b/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/HandleViewInfo.java
new file mode 100644
//Synthetic comment -- index 0000000..6580aef

//Synthetic comment -- @@ -0,0 +1,138 @@
/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ddmlib;

import sun.awt.CharsetString;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

final public class HandleViewInfo extends ChunkHandler {
    /** Enable/Disable tracing of OpenGL calls. */
    private static final int CHUNK_VUGL = type("VUGL");

    /** List {@link ViewRootImpl}'s of this process. */
    private static final int CHUNK_VULW = type("VULW");

    /** Dump view hierarchy. */
    private static final int CHUNK_VUDP = type("VUDP");

    /** Capture View Layers. */
    private static final int CHUNK_VUCL = type("VUCL");

    /** Obtain the Display List corresponding to the view. */
    private static final int CHUNK_VUDL = type("VUDL");

    /** Invalidate View. */
    private static final int CHUNK_VUIV = type("VUIV");

    /** Re-layout given view. */
    private static final int CHUNK_VULT = type("VULT");

    /** Profile a view. */
    private static final int CHUNK_VUPR = type("VUPR");

    private static final HandleViewInfo sInst = new HandleViewInfo();

    private HandleViewInfo() {}

    public static void register(MonitorThread mt) {
        mt.registerChunkHandler(CHUNK_VULW, sInst);
        mt.registerChunkHandler(CHUNK_VUDP, sInst);
        mt.registerChunkHandler(CHUNK_VUCL, sInst);
        mt.registerChunkHandler(CHUNK_VUDL, sInst);
        mt.registerChunkHandler(CHUNK_VUIV, sInst);
        mt.registerChunkHandler(CHUNK_VULT, sInst);
        mt.registerChunkHandler(CHUNK_VUPR, sInst);
    }

    @Override
    public void clientReady(Client client) throws IOException {}

    @Override
    public void clientDisconnected(Client client) {}

    @Override
    public void handleChunk(Client client, int type, ByteBuffer data,
            boolean isReply, int msgId) {
        Log.d("ddm-appname", "handling " + ChunkHandler.name(type));
        System.out.println("handling VIHI");

        if (type == CHUNK_VULW) {
            handleListWindows(client, data);
        } else if (type == CHUNK_VUDP) {
            handleViewHierarchyDump(client, data);
        } else {
            handleUnknownChunk(client, type, data, isReply, msgId);
        }
    }

    public static void sendListViewRoots(Client client) throws IOException {
        ByteBuffer buf = allocBuffer(4);
        JdwpPacket packet = new JdwpPacket(buf);
        ByteBuffer chunkBuf = getChunkDataBuf(buf);
        chunkBuf.putInt(1);
        finishChunkPacket(packet, CHUNK_VULW, chunkBuf.position());
        client.sendAndConsume(packet, sInst);

        System.out.println("sent VULW packet");
    }

    public static void sendDumpViewHierarchy(Client client, String viewRoot) throws IOException {
        ByteBuffer buf = allocBuffer(4      // view root length
                + viewRoot.length() * 2     // view root
                + 4                         // skip children
                + 4);                       // include view properties
        JdwpPacket packet = new JdwpPacket(buf);
        ByteBuffer chunkBuf = getChunkDataBuf(buf);

        chunkBuf.putInt(viewRoot.length());
        putString(chunkBuf, viewRoot);
        chunkBuf.putInt(0);     // skip children     
        chunkBuf.putInt(1);     // include properties

        finishChunkPacket(packet, CHUNK_VUDP, chunkBuf.position());
        client.sendAndConsume(packet, sInst);

        System.out.println("sent VUDP packet");        
    }

    private void handleListWindows(Client client, ByteBuffer data) {
        int nWindows = data.getInt();
        System.out.println("# of windows = " + nWindows);

        for (int i = 0; i < nWindows; i++) {
            int len = data.getInt();
            String name = getString(data, len);
            System.out.println(name);
            try {
                HandleViewInfo.sendDumpViewHierarchy(client, name);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleViewHierarchyDump(Client client, ByteBuffer data) {
        byte[] b = new byte[data.remaining()];
        data.get(b);
        String s = new String(b, Charset.forName("UTF-8"));
        System.out.println(s);
    }
 }









//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/DevicePanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/DevicePanel.java
//Synthetic comment -- index a24b8a0..7986082 100644

//Synthetic comment -- @@ -22,6 +22,8 @@
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.Client;
import com.android.ddmlib.ClientData;
import com.android.ddmlib.HandleViewInfo;
import com.android.ddmlib.Log;
import com.android.ddmlib.ClientData.DebuggerStatus;
import com.android.ddmlib.DdmPreferences;
import com.android.ddmlib.IDevice;
//Synthetic comment -- @@ -48,6 +50,7 @@
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

//Synthetic comment -- @@ -446,6 +449,16 @@
}
}

    public void dumpViewHierarchy() {
        if (mCurrentClient != null) {
            try {
                HandleViewInfo.sendListViewRoots(mCurrentClient);
            } catch (IOException e) {
                Log.e("ddms", "Dump View Hierarchy failed: " + e.getMessage());
            }
        }
    }

public void toggleMethodProfiling() {
if (mCurrentClient != null) {
mCurrentClient.toggleMethodProfiling();







