/*SocketOutputStream: Properly handle short writes

Even blocking writes can return before the full amount is
written, e.g. if a signal is received.

Since the OutputStream write methods return void, there is no
way of signalling a short write to the caller, it has to retry
until everything is written or writing fails.

Change-Id:I8d644daa746f8404eb14f5102db41c867218cb49*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/luni/net/SocketOutputStream.java b/luni/src/main/java/org/apache/harmony/luni/net/SocketOutputStream.java
//Synthetic comment -- index 7c7bd18..5534cd6 100644

//Synthetic comment -- @@ -45,7 +45,7 @@

@Override
public void write(byte[] buffer) throws IOException {
        socket.write(buffer, 0, buffer.length);
}

@Override
//Synthetic comment -- @@ -54,7 +54,13 @@
throw new NullPointerException("buffer == null");
}
if (0 <= offset && offset <= buffer.length && 0 <= count && count <= buffer.length - offset) {
            socket.write(buffer, offset, count);
} else {
throw new ArrayIndexOutOfBoundsException();
}







