/*Sync sdk=>tools/base: "ddmlib: Add controls for OpenGL tracing via jdwp"

Merged from platforms/sdk.git 94e177b50dc1ad3739f563668e1f2e43a9f97f46
and only merging the ddmlib changes.

Original description:

Currently, applications have to be launched with gltrace
enabled for OpenGL tracing to work. This patch provides the host side
support for dynamically enabling/disabling tracing on running apps.

At a high level, the functionality is similar to traceview:
    - ClientData#FEATURE_OPENGL_TRACING indicates whether the VM on
      the device supports this feature.
    - If the feature is supported, then JDWP is used to send the
      enable or disable messages.
    - Users can trigger OpenGL tracing via a toolbar item in the DDMS
      device view.

Change-Id:Ib6f1764ef74fb0e71a062501cd8b64d6a9d5c97e*/




//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/Client.java b/ddmlib/src/main/java/com/android/ddmlib/Client.java
//Synthetic comment -- index 8ea1193..15acd88 100644

//Synthetic comment -- @@ -271,6 +271,36 @@
}
}

    public boolean startOpenGlTracing() {
        boolean canTraceOpenGl = mClientData.hasFeature(ClientData.FEATURE_OPENGL_TRACING);
        if (!canTraceOpenGl) {
            return false;
        }

        try {
            OpenGlTraceChunkHandler.sendStartGlTracing(this);
            return true;
        } catch (IOException e) {
            Log.w("ddms", "Start OpenGL Tracing failed");
            return false;
        }
    }

    public boolean stopOpenGlTracing() {
        boolean canTraceOpenGl = mClientData.hasFeature(ClientData.FEATURE_OPENGL_TRACING);
        if (!canTraceOpenGl) {
            return false;
        }

        try {
            OpenGlTraceChunkHandler.sendStopGlTracing(this);
            return true;
        } catch (IOException e) {
            Log.w("ddms", "Stop OpenGL Tracing failed");
            return false;
        }
    }

/**
* Sends a request to the VM to send the enable status of the method profiling.
* This is asynchronous.








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/ClientData.java b/ddmlib/src/main/java/com/android/ddmlib/ClientData.java
//Synthetic comment -- index bec4c61..aa383cf 100644

//Synthetic comment -- @@ -130,6 +130,12 @@
public static final String FEATURE_PROFILING_STREAMING = "method-trace-profiling-streaming"; //$NON-NLS-1$

/**
     * String for feature indicating support for tracing OpenGL calls.
     * @see #hasFeature(String)
     */
    public final static String FEATURE_OPENGL_TRACING = "opengl-tracing"; //$NON-NLS-1$

    /**
* String for feature allowing to dump hprof files
* @see #hasFeature(String)
*/








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/OpenGlTraceChunkHandler.java b/ddmlib/src/main/java/com/android/ddmlib/OpenGlTraceChunkHandler.java
new file mode 100644
//Synthetic comment -- index 0000000..12ba142

//Synthetic comment -- @@ -0,0 +1,63 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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

public class OpenGlTraceChunkHandler extends ChunkHandler {
    /** GL TRace Control: data in the packet enables or disables tracing. */
    public static final int CHUNK_GLTR = type("GLTR");

    private OpenGlTraceChunkHandler() {
    }

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
        ByteBuffer buf = allocBuffer(4);
        JdwpPacket packet = new JdwpPacket(buf);

        ByteBuffer chunkBuf = getChunkDataBuf(buf);
        chunkBuf.putInt(1);
        finishChunkPacket(packet, CHUNK_GLTR, chunkBuf.position());

        client.sendAndConsume(packet);
    }

    public static void sendStopGlTracing(Client client) throws IOException {
        ByteBuffer buf = allocBuffer(4);
        JdwpPacket packet = new JdwpPacket(buf);

        ByteBuffer chunkBuf = getChunkDataBuf(buf);
        chunkBuf.putInt(0);
        finishChunkPacket(packet, CHUNK_GLTR, chunkBuf.position());

        client.sendAndConsume(packet);
    }
}







