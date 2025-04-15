/*Add support for invoking methods on views.

Change-Id:I1fe345f9c6d7db0569656256feb091c6f1b664b6*/




//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/HandleViewDebug.java b/ddmlib/src/main/java/com/android/ddmlib/HandleViewDebug.java
//Synthetic comment -- index 3c79e55..6816538 100644

//Synthetic comment -- @@ -52,20 +52,28 @@
/** Obtain the Display List corresponding to the view. */
private static final int VUOP_DUMP_DISPLAYLIST = 2;

/** Profile a view. */
    private static final int VUOP_PROFILE_VIEW = 3;

    /** Invoke a method on the view. */
    private static final int VUOP_INVOKE_VIEW_METHOD = 4;

    /** Set layout parameter. */
    private static final int VUOP_SET_LAYOUT_PARAMETER = 5;

    private static final String TAG = "ddmlib"; //$NON-NLS-1$

    private static final HandleViewDebug sInstance = new HandleViewDebug();

private HandleViewDebug() {}

public static void register(MonitorThread mt) {
// TODO: add chunk type for auto window updates
// and register here
        mt.registerChunkHandler(CHUNK_VUGL, sInstance);
        mt.registerChunkHandler(CHUNK_VULW, sInstance);
        mt.registerChunkHandler(CHUNK_VUOP, sInstance);
        mt.registerChunkHandler(CHUNK_VURT, sInstance);
}

@Override
//Synthetic comment -- @@ -161,11 +169,16 @@
}

private static void sendViewOpPacket(@NonNull Client client, int op, @NonNull String viewRoot,
            @NonNull String view, @Nullable byte[] extra, @Nullable ViewDumpHandler handler)
                    throws IOException {
int bufLen = 4 +                        // opcode
4 + viewRoot.length() * 2 +     // view root strlen + view root
4 + view.length() * 2;          // view strlen + view

        if (extra != null) {
            bufLen += extra.length;
        }

ByteBuffer buf = allocBuffer(bufLen);
JdwpPacket packet = new JdwpPacket(buf);
ByteBuffer chunkBuf = getChunkDataBuf(buf);
//Synthetic comment -- @@ -177,6 +190,10 @@
chunkBuf.putInt(view.length());
putString(chunkBuf, view);

        if (extra != null) {
            chunkBuf.put(extra);
        }

finishChunkPacket(packet, CHUNK_VUOP, chunkBuf.position());
if (handler != null) {
client.sendAndConsume(packet, handler);
//Synthetic comment -- @@ -187,32 +204,115 @@

public static void profileView(@NonNull Client client, @NonNull String viewRoot,
@NonNull String view, @NonNull ViewDumpHandler handler) throws IOException {
        sendViewOpPacket(client, VUOP_PROFILE_VIEW, viewRoot, view, null, handler);
}

public static void captureView(@NonNull Client client, @NonNull String viewRoot,
@NonNull String view, @NonNull ViewDumpHandler handler) throws IOException {
        sendViewOpPacket(client, VUOP_CAPTURE_VIEW, viewRoot, view, null, handler);
}

public static void invalidateView(@NonNull Client client, @NonNull String viewRoot,
@NonNull String view) throws IOException {
        invokeMethod(client, viewRoot, view, "invalidate");
}

public static void requestLayout(@NonNull Client client, @NonNull String viewRoot,
@NonNull String view) throws IOException {
        invokeMethod(client, viewRoot, view, "requestLayout");
}

public static void dumpDisplayList(@NonNull Client client, @NonNull String viewRoot,
@NonNull String view) throws IOException {
        sendViewOpPacket(client, VUOP_DUMP_DISPLAYLIST, viewRoot, view, null, null);
    }

    private static class ErrorChunkHandler extends ViewDumpHandler {
        public ErrorChunkHandler(int chunkType) {
            super(chunkType);
        }

        @Override
        protected void handleViewDebugResult(ByteBuffer data) {
            System.out.println("error!!");
        }
    }

    public static void invokeMethod(@NonNull Client client, @NonNull String viewRoot,
            @NonNull String view, @NonNull String method, Object... args) throws IOException {
        int len = 4 + method.length() * 2;
        if (args != null) {
            // # of args
            len += 4;

            // for each argument, we send a char type specifier (2 bytes) and
            // the arg value (max primitive size = sizeof(double) = 8
            len += 10 * args.length;
        }

        byte[] extra = new byte[len];
        ByteBuffer b = ByteBuffer.wrap(extra);

        b.putInt(method.length());
        putString(b, method);

        if (args != null) {
            b.putInt(args.length);

            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg instanceof Boolean) {
                    b.putChar('Z');
                    b.put((byte) (((Boolean) arg).booleanValue() ? 1 : 0));
                } else if (arg instanceof Byte) {
                    b.putChar('B');
                    b.put(((Byte) arg).byteValue());
                } else if (arg instanceof Character) {
                    b.putChar('C');
                    b.putChar(((Character) arg).charValue());
                } else if (arg instanceof Short) {
                    b.putChar('S');
                    b.putShort(((Short) arg).shortValue());
                } else if (arg instanceof Integer) {
                    b.putChar('I');
                    b.putInt(((Integer) arg).intValue());
                } else if (arg instanceof Long) {
                    b.putChar('J');
                    b.putLong(((Long) arg).longValue());
                } else if (arg instanceof Float) {
                    b.putChar('F');
                    b.putFloat(((Float) arg).floatValue());
                } else if (arg instanceof Double) {
                    b.putChar('D');
                    b.putDouble(((Double) arg).doubleValue());
                } else {
                    Log.e(TAG, "View method invocation only supports primitive arguments, supplied: " + arg);
                    return;
                }
            }
        }

        sendViewOpPacket(client, VUOP_INVOKE_VIEW_METHOD, viewRoot, view, extra,
                new ErrorChunkHandler(CHUNK_VUOP));
    }

    public static void setLayoutParameter(@NonNull Client client, @NonNull String viewRoot,
            @NonNull String view, @NonNull String parameter, int value) throws IOException {
        int len = 4 + parameter.length() * 2 + 4;
        byte[] extra = new byte[len];
        ByteBuffer b = ByteBuffer.wrap(extra);

        b.putInt(parameter.length());
        putString(b, parameter);
        b.putInt(value);
        sendViewOpPacket(client, VUOP_SET_LAYOUT_PARAMETER, viewRoot, view, extra,
                new ErrorChunkHandler(CHUNK_VUOP));
}

@Override
public void handleChunk(Client client, int type, ByteBuffer data,
boolean isReply, int msgId) {
        System.out.println("HandleViewDebug: handled chunk");
}

public static void sendStartGlTracing(Client client) throws IOException {







