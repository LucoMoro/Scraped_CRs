/*Add support for invoking methods on views.

This CL adds support for invoking methods on a Android View
object. To invoke a view, the method name along with all its
arguments are passed to the device via the DDM channel.

The existing commands to layout a view and invalidate a view
are now replaced by just calling the generic invoke view method
operation with appropriate parameters.

Change-Id:I1fe345f9c6d7db0569656256feb091c6f1b664b6*/
//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/HandleViewDebug.java b/ddmlib/src/main/java/com/android/ddmlib/HandleViewDebug.java
//Synthetic comment -- index 3c79e55..0f5872e 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;

import java.io.IOException;
import java.nio.ByteBuffer;
//Synthetic comment -- @@ -52,20 +53,31 @@
/** Obtain the Display List corresponding to the view. */
private static final int VUOP_DUMP_DISPLAYLIST = 2;

    /** Invalidate View. */
    private static final int VUOP_INVALIDATE_VIEW = 3;

    /** Re-layout given view. */
    private static final int VUOP_LAYOUT_VIEW = 4;

/** Profile a view. */
    private static final int VUOP_PROFILE_VIEW = 5;

private HandleViewDebug() {}

public static void register(MonitorThread mt) {
// TODO: add chunk type for auto window updates
// and register here
}

@Override
//Synthetic comment -- @@ -161,11 +173,16 @@
}

private static void sendViewOpPacket(@NonNull Client client, int op, @NonNull String viewRoot,
            @NonNull String view, @Nullable ViewDumpHandler handler) throws IOException {
int bufLen = 4 +                        // opcode
4 + viewRoot.length() * 2 +     // view root strlen + view root
4 + view.length() * 2;          // view strlen + view

ByteBuffer buf = allocBuffer(bufLen);
JdwpPacket packet = new JdwpPacket(buf);
ByteBuffer chunkBuf = getChunkDataBuf(buf);
//Synthetic comment -- @@ -177,6 +194,10 @@
chunkBuf.putInt(view.length());
putString(chunkBuf, view);

finishChunkPacket(packet, CHUNK_VUOP, chunkBuf.position());
if (handler != null) {
client.sendAndConsume(packet, handler);
//Synthetic comment -- @@ -187,32 +208,115 @@

public static void profileView(@NonNull Client client, @NonNull String viewRoot,
@NonNull String view, @NonNull ViewDumpHandler handler) throws IOException {
        sendViewOpPacket(client, VUOP_PROFILE_VIEW, viewRoot, view, handler);
}

public static void captureView(@NonNull Client client, @NonNull String viewRoot,
@NonNull String view, @NonNull ViewDumpHandler handler) throws IOException {
        sendViewOpPacket(client, VUOP_CAPTURE_VIEW, viewRoot, view, handler);
}

public static void invalidateView(@NonNull Client client, @NonNull String viewRoot,
@NonNull String view) throws IOException {
        sendViewOpPacket(client, VUOP_INVALIDATE_VIEW, viewRoot, view, null);
}

public static void requestLayout(@NonNull Client client, @NonNull String viewRoot,
@NonNull String view) throws IOException {
        sendViewOpPacket(client, VUOP_LAYOUT_VIEW, viewRoot, view, null);
}

public static void dumpDisplayList(@NonNull Client client, @NonNull String viewRoot,
@NonNull String view) throws IOException {
        sendViewOpPacket(client, VUOP_DUMP_DISPLAYLIST, viewRoot, view, null);
}

@Override
public void handleChunk(Client client, int type, ByteBuffer data,
boolean isReply, int msgId) {
}

public static void sendStartGlTracing(Client client) throws IOException {







