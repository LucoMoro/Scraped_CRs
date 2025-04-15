/*In BluetoothSocket.close, destroyNative() called twice and caused a crash.

In close(), function destroyNative() is invoked twice. This causes a crash when
the second thread calls BluetoothSocket.close() before first thread sets
BluetoothSocket.mClosed flag to true.

Change-Id:I36c1d3367a9c802dde425a4de7c5c22c629de532Signed-off-by: Kausik Sinnaswamy <kausik@broadcom.com>*/




//Synthetic comment -- diff --git a/core/java/android/bluetooth/BluetoothSocket.java b/core/java/android/bluetooth/BluetoothSocket.java
//Synthetic comment -- index 719d730..66e3b75 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.atomic.AtomicBoolean;

/**
* A connected or connecting Bluetooth socket.
//Synthetic comment -- @@ -96,6 +97,7 @@

/** prevents all native calls after destroyNative() */
private boolean mClosed;
    private AtomicBoolean mClosing = new AtomicBoolean(false);

/** protects mClosed */
private final ReentrantReadWriteLock mLock;
//Synthetic comment -- @@ -213,6 +215,10 @@
* throw an IOException.
*/
public void close() throws IOException {
        if (mClosing.compareAndSet(false, true) == false) {
            Log.d(TAG, "socket already in closing state:" + this);
            return;
        }
// abort blocking operations on the socket
mLock.readLock().lock();
try {







