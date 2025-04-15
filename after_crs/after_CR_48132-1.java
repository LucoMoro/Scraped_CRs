/*ddmlib: Add controls for OpenGL tracing via jdwp

WIP

Change-Id:I99afd04eed00925a044e778e1172d5873a7e1ba4*/




//Synthetic comment -- diff --git a/ddms/app/src/com/android/ddms/UIThread.java b/ddms/app/src/com/android/ddms/UIThread.java
//Synthetic comment -- index 8aaa806..9e99772 100644

//Synthetic comment -- @@ -186,6 +186,7 @@
private ToolItem mTBCauseGc;
private ToolItem mTBDumpHprof;
private ToolItem mTBProfiling;
    private ToolItem mGlTracing;

private final class FilterStorage implements ILogFilterStorageManager {

//Synthetic comment -- @@ -1159,6 +1160,18 @@
}
});

        // add start OpenGL tracer
        mGlTracing = new ToolItem(toolBar, SWT.PUSH);
        mGlTracing.setToolTipText("Start OpenGl Tracing");
        mGlTracing.setEnabled(false);
        mGlTracing.setImage(mTracingStartImage);
        mGlTracing.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                mDevicePanel.startOpenGlTracing();
            }
        });

new ToolItem(toolBar, SWT.SEPARATOR);

// add "kill VM" button; need to make this visually distinct from
//Synthetic comment -- @@ -1722,6 +1735,14 @@
mTBProfiling.setImage(mTracingStartImage);
mTBProfiling.setToolTipText("Start Method Profiling (not supported by this VM)");
}

            if (data.hasFeature(ClientData.FEATURE_OPENGL_TRACING)) {
                mGlTracing.setEnabled(true);
                mGlTracing.setToolTipText("Start OpenGL Tracing.");
            } else {
                mGlTracing.setEnabled(false);
                mGlTracing.setToolTipText("OpenGL Tracing not supported by this VM.");
            }
} else {
// list is empty, disable these
mTBShowThreadUpdates.setSelection(false);








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/Client.java b/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/Client.java
//Synthetic comment -- index 5b03462..d6e623e 100644

//Synthetic comment -- @@ -271,6 +271,21 @@
}
}

    public boolean startOpenGlTracing() {
        boolean canTraceOpenGl = mClientData.hasFeature(ClientData.FEATURE_OPENGL_TRACING);
        if (!canTraceOpenGl) {
            return false;
        }

        try {
            HandleOpenGlTracing.sendStartGlTracing(this);
            return true;
        } catch (IOException e) {
            Log.w("ddms", "Start OpenGL Tracing failed");
            return false;
        }
    }

/**
* Sends a request to the VM to send the enable status of the method profiling.
* This is asynchronous.








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/ClientData.java b/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/ClientData.java
//Synthetic comment -- index ff83c37..f490c1a 100644

//Synthetic comment -- @@ -130,6 +130,12 @@
public final static String FEATURE_PROFILING_STREAMING = "method-trace-profiling-streaming"; //$NON-NLS-1$

/**
     * String for feature indicating support for tracing OpenGL calls.
     * @see #hasFeature(String)
     */
    public final static String FEATURE_OPENGL_TRACING = "opengl-tracing"; //$NON-NLS-1$

    /**
* String for feature allowing to dump hprof files
* @see #hasFeature(String)
*/








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/HandleOpenGlTracing.java b/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/HandleOpenGlTracing.java
new file mode 100644
//Synthetic comment -- index 0000000..d69d438

//Synthetic comment -- @@ -0,0 +1,41 @@
package com.android.ddmlib;

import java.io.IOException;
import java.nio.ByteBuffer;

public class HandleOpenGlTracing extends ChunkHandler {
    /** GL Trace Start. */
    public static final int CHUNK_GLTS = type("GLTS");

    /** GL Trace End. */
    public static final int CHUNK_GLTE = type("GLTE");

    private HandleOpenGlTracing() {}

    @Override
    void clientReady(Client client) throws IOException {
    }

    @Override
    void clientDisconnected(Client client) {
    }

    @Override
    void handleChunk(Client client, int type, ByteBuffer data, boolean isReply, int msgId) {
        handleUnknownChunk(client, type, data, isReply, msgId);
    }

    public static void sendStartGlTracing(Client client) throws IOException {
        ByteBuffer buf = allocBuffer(0);
        JdwpPacket packet = new JdwpPacket(buf);
        finishChunkPacket(packet, CHUNK_GLTS, buf.position());
        client.sendAndConsume(packet);
    }

    public static void sendStopGlTracing(Client client) throws IOException {
        ByteBuffer buf = allocBuffer(0);
        JdwpPacket packet = new JdwpPacket(buf);
        finishChunkPacket(packet, CHUNK_GLTE, buf.position());
        client.sendAndConsume(packet);
    }
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/DevicePanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/DevicePanel.java
//Synthetic comment -- index a24b8a0..ddb1b02 100644

//Synthetic comment -- @@ -452,6 +452,12 @@
}
}

    public void startOpenGlTracing() {
        if (mCurrentClient != null) {
            mCurrentClient.startOpenGlTracing();
        }
    }

public void setEnabledHeapOnSelectedClient(boolean enable) {
if (mCurrentClient != null) {
mCurrentClient.setHeapUpdateEnabled(enable);







