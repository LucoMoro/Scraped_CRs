/*WIP: hv via DDM

Change-Id:Ic1fffd4fef43ef9b2c5e4d432f16d941323d75f7*/




//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/AndroidDebugBridge.java b/ddmlib/src/main/java/com/android/ddmlib/AndroidDebugBridge.java
//Synthetic comment -- index c787a12..000d92a 100644

//Synthetic comment -- @@ -213,6 +213,7 @@
HandleWait.register(monitorThread);
HandleProfiling.register(monitorThread);
HandleNativeHeap.register(monitorThread);
        HandleViewDebug.register(monitorThread);
}

/**








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/ChunkHandler.java b/ddmlib/src/main/java/com/android/ddmlib/ChunkHandler.java
//Synthetic comment -- index 74fa318..2cc6494 100644

//Synthetic comment -- @@ -94,7 +94,7 @@
* This is here because multiple chunk handlers can make use of it,
* and there's nowhere better to put it.
*/
    public static String getString(ByteBuffer buf, int len) {
char[] data = new char[len];
for (int i = 0; i < len; i++)
data[i] = buf.getChar();








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/ClientData.java b/ddmlib/src/main/java/com/android/ddmlib/ClientData.java
//Synthetic comment -- index aa383cf..3741df2 100644

//Synthetic comment -- @@ -136,6 +136,12 @@
public final static String FEATURE_OPENGL_TRACING = "opengl-tracing"; //$NON-NLS-1$

/**
     * String for feature indicating support for providing view hierarchy.
     * @see #hasFeature(String)
     */
    public static final String FEATURE_VIEW_HIERARCHY = "view-hierarchy"; //$NON-NLS-1$

    /**
* String for feature allowing to dump hprof files
* @see #hasFeature(String)
*/








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/HandleViewDebug.java b/ddmlib/src/main/java/com/android/ddmlib/HandleViewDebug.java
new file mode 100644
//Synthetic comment -- index 0000000..01e4403

//Synthetic comment -- @@ -0,0 +1,191 @@
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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

final public class HandleViewDebug extends ChunkHandler {
    /** Enable/Disable tracing of OpenGL calls. */
    public static final int CHUNK_VUGL = type("VUGL");

    /** List {@link ViewRootImpl}'s of this process. */
    public static final int CHUNK_VULW = type("VULW");

    /** Dump view hierarchy. */
    public static final int CHUNK_VUDP = type("VUDP");

    /** Capture View Layers. */
    public static final int CHUNK_VUCL = type("VUCL");

    /** Capture View Layers. */
    public static final int CHUNK_VUCV = type("VUCV");

    /** Obtain the Display List corresponding to the view. */
    public static final int CHUNK_VUDL = type("VUDL");

    /** Invalidate View. */
    public static final int CHUNK_VUIV = type("VUIV");

    /** Re-layout given view. */
    public static final int CHUNK_VULT = type("VULT");

    /** Profile a view. */
    public static final int CHUNK_VUPR = type("VUPR");

    private HandleViewDebug() {}

    public static void register(MonitorThread mt) {
        // TODO: add chunk type for auto window updates
        // and register here
    }

    @Override
    public void clientReady(Client client) throws IOException {}

    @Override
    public void clientDisconnected(Client client) {}

    public static abstract class ViewDumpHandler extends ChunkHandler {
        private final CountDownLatch mLatch = new CountDownLatch(1);
        private final int mChunkType;

        public ViewDumpHandler(int chunkType) {
            mChunkType = chunkType;
        }

        @Override
        void clientReady(Client client) throws IOException {
        }

        @Override
        void clientDisconnected(Client client) {
        }

        @Override
        void handleChunk(Client client, int type, ByteBuffer data,
                boolean isReply, int msgId) {
            if (type != mChunkType) {
                handleUnknownChunk(client, type, data, isReply, msgId);
                return;
            }

            handleViewDebugResult(data);
            mLatch.countDown();
        }

        protected abstract void handleViewDebugResult(ByteBuffer data);

        protected void waitForResult(long timeout, TimeUnit unit) {
            try {
                mLatch.await(timeout, unit);
            } catch (InterruptedException e) {
                // pass
            }
        }
    }

    public static void listViewRoots(Client client, ViewDumpHandler replyHandler)
            throws IOException {
        ByteBuffer buf = allocBuffer(4);
        JdwpPacket packet = new JdwpPacket(buf);
        ByteBuffer chunkBuf = getChunkDataBuf(buf);
        chunkBuf.putInt(1);
        finishChunkPacket(packet, CHUNK_VULW, chunkBuf.position());
        client.sendAndConsume(packet, replyHandler);
    }

    public static void dumpViewHierarchy(Client client, String viewRoot,
            boolean skipChildren, boolean includeProperties, ViewDumpHandler handler)
                    throws IOException {
        ByteBuffer buf = allocBuffer(4      // view root length
                + viewRoot.length() * 2     // view root
                + 4                         // skip children
                + 4);                       // include view properties
        JdwpPacket packet = new JdwpPacket(buf);
        ByteBuffer chunkBuf = getChunkDataBuf(buf);

        chunkBuf.putInt(viewRoot.length());
        putString(chunkBuf, viewRoot);
        chunkBuf.putInt(skipChildren ? 1 : 0);
        chunkBuf.putInt(includeProperties ? 1 : 0);

        finishChunkPacket(packet, CHUNK_VUDP, chunkBuf.position());
        client.sendAndConsume(packet, handler);
    }

    private static void sendJdwpPacket(Client client, int type, String viewRoot, String view,
            ViewDumpHandler handler) throws IOException {
        int bufLen = 4 + viewRoot.length() * 2;

        if (view != null) {
            bufLen += 4 + view.length() * 2;
        }

        ByteBuffer buf = allocBuffer(bufLen);
        JdwpPacket packet = new JdwpPacket(buf);
        ByteBuffer chunkBuf = getChunkDataBuf(buf);

        chunkBuf.putInt(viewRoot.length());
        putString(chunkBuf, viewRoot);

        if (view != null) {
            chunkBuf.putInt(view.length());
            putString(chunkBuf, view);
        }

        finishChunkPacket(packet, type, chunkBuf.position());
        if (handler != null) {
            client.sendAndConsume(packet, handler);
        } else {
            client.sendAndConsume(packet);
        }
    }

    public static void captureLayers(Client client, String viewRoot, ViewDumpHandler handler)
            throws IOException {
        sendJdwpPacket(client, CHUNK_VUCL, viewRoot, null, handler);
    }

    public static void captureView(Client client, String viewRoot, String view,
            ViewDumpHandler handler) throws IOException {
        sendJdwpPacket(client, CHUNK_VUCV, viewRoot, view, handler);
    }

    public static void invalidateView(Client client, String viewRoot, String view)
            throws IOException {
        sendJdwpPacket(client, CHUNK_VUIV, viewRoot, view, null);
    }

    public static void requestLayout(Client client, String viewRoot, String view)
            throws IOException {
        sendJdwpPacket(client, CHUNK_VULT, viewRoot, view, null);
    }

    public static void dumpDisplayList(Client client, String viewRoot, String view)
            throws IOException {
        sendJdwpPacket(client, CHUNK_VUDL, viewRoot, view, null);
    }

    @Override
    public void handleChunk(Client client, int type, ByteBuffer data,
            boolean isReply, int msgId) {
    }
}








