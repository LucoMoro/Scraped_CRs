/*Fix Thread.interrupt and PipedInputStream/PipedReader issues found by InterruptedStreamTest

Bug: 6951157
Change-Id:I558f8eb5c435f01c98c080ac38cc7c165e7aee25*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/io/PipedInputStream.java b/luni/src/main/java/java/io/PipedInputStream.java
//Synthetic comment -- index aa77e6e..739fb98 100644

//Synthetic comment -- @@ -18,6 +18,7 @@
package java.io;

import java.util.Arrays;
import libcore.io.IoUtils;

/**
* Receives information from a communications pipe. When two threads want to
//Synthetic comment -- @@ -235,7 +236,7 @@
wait(1000);
}
} catch (InterruptedException e) {
            IoUtils.throwInterruptedIoException();
}

int result = buffer[out++] & 0xff;
//Synthetic comment -- @@ -314,7 +315,7 @@
wait(1000);
}
} catch (InterruptedException e) {
            IoUtils.throwInterruptedIoException();
}

int totalCopied = 0;
//Synthetic comment -- @@ -394,7 +395,7 @@
wait(1000);
}
} catch (InterruptedException e) {
            IoUtils.throwInterruptedIoException();
}
if (buffer == null) {
throw new IOException("Pipe is closed");








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/PipedReader.java b/luni/src/main/java/java/io/PipedReader.java
//Synthetic comment -- index 6190c86..8450a34 100644

//Synthetic comment -- @@ -18,6 +18,7 @@
package java.io;

import java.util.Arrays;
import libcore.io.IoUtils;

/**
* Receives information on a communications pipe. When two threads want to pass
//Synthetic comment -- @@ -261,7 +262,7 @@
wait(1000);
}
} catch (InterruptedException e) {
            IoUtils.throwInterruptedIoException();
}

int copyLength = 0;
//Synthetic comment -- @@ -362,7 +363,7 @@
}
}
} catch (InterruptedException e) {
            IoUtils.throwInterruptedIoException();
}
if (buffer == null) {
throw new IOException("Pipe is closed");
//Synthetic comment -- @@ -411,7 +412,7 @@
}
}
} catch (InterruptedException e) {
                IoUtils.throwInterruptedIoException();
}
if (buffer == null) {
throw new IOException("Pipe is closed");








//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Thread.java b/luni/src/main/java/java/lang/Thread.java
//Synthetic comment -- index 318bfaa..a076a04 100644

//Synthetic comment -- @@ -657,16 +657,19 @@
* @see Thread#isInterrupted
*/
public void interrupt() {
        // Interrupt this thread before running actions so that other
        // threads that observe the interrupt as a result of an action
        // will see that this thread is in the interrupted state.
        VMThread vmt = this.vmThread;
        if (vmt != null) {
            vmt.interrupt();
        }

synchronized (interruptActions) {
for (int i = interruptActions.size() - 1; i >= 0; i--) {
interruptActions.get(i).run();
}
}
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/IoUtils.java b/luni/src/main/java/libcore/io/IoUtils.java
//Synthetic comment -- index 243d8d8..7177f3f 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.nio.charset.Charsets;
//Synthetic comment -- @@ -145,4 +146,13 @@
}
}
}

    public static void throwInterruptedIoException() throws InterruptedIOException {
        // This is typically thrown in response to an
        // InterruptedException which does not leave the thread in an
        // interrupted state, so explicitly interrupt here.
        Thread.currentThread().interrupt();
        // TODO: set InterruptedIOException.bytesTransferred
        throw new InterruptedIOException();
    }
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/io/InterruptedStreamTest.java b/luni/src/test/java/libcore/java/io/InterruptedStreamTest.java
//Synthetic comment -- index e1f51bd..39c0ad3 100755

//Synthetic comment -- @@ -46,10 +46,18 @@

private Socket[] sockets;

    @Override protected void setUp() throws Exception {
        // Clear the interrupted bit to make sure an earlier test did
        // not leave us in a bad state.
        Thread.interrupted();
        super.tearDown();
    }

@Override protected void tearDown() throws Exception {
if (sockets != null) {
sockets[0].close();
sockets[1].close();
            sockets = null;
}
Thread.interrupted(); // clear interrupted bit
super.tearDown();
//Synthetic comment -- @@ -111,56 +119,66 @@
}

private void testInterruptInputStream(final InputStream in) throws Exception {
        Thread thread = interruptMeLater();
try {
in.read();
fail();
} catch (InterruptedIOException expected) {
        } finally {
            confirmInterrupted(thread);
}
}

private void testInterruptReader(final PipedReader reader) throws Exception {
        Thread thread = interruptMeLater();
try {
reader.read();
fail();
} catch (InterruptedIOException expected) {
        } finally {
            confirmInterrupted(thread);
}
}

private void testInterruptReadableChannel(final ReadableByteChannel channel) throws Exception {
        Thread thread = interruptMeLater();
try {
channel.read(ByteBuffer.allocate(BUFFER_SIZE));
fail();
} catch (ClosedByInterruptException expected) {
        } finally {
            confirmInterrupted(thread);
}
}

private void testInterruptOutputStream(final OutputStream out) throws Exception {
        Thread thread = interruptMeLater();
try {
// this will block when the receiving buffer fills up
while (true) {
out.write(new byte[BUFFER_SIZE]);
}
} catch (InterruptedIOException expected) {
        } finally {
            confirmInterrupted(thread);
}
}

private void testInterruptWriter(final PipedWriter writer) throws Exception {
        Thread thread = interruptMeLater();
try {
// this will block when the receiving buffer fills up
while (true) {
writer.write(new char[BUFFER_SIZE]);
}
} catch (InterruptedIOException expected) {
        } finally {
            confirmInterrupted(thread);
}
}

private void testInterruptWritableChannel(final WritableByteChannel channel) throws Exception {
        Thread thread = interruptMeLater();
try {
// this will block when the receiving buffer fills up
while (true) {
//Synthetic comment -- @@ -168,12 +186,14 @@
}
} catch (ClosedByInterruptException expected) {
} catch (ClosedChannelException expected) {
        } finally {
            confirmInterrupted(thread);
}
}

    private Thread interruptMeLater() throws Exception {
final Thread toInterrupt = Thread.currentThread();
        Thread thread = new Thread(new Runnable () {
@Override public void run() {
try {
Thread.sleep(1000);
//Synthetic comment -- @@ -181,6 +201,14 @@
}
toInterrupt.interrupt();
}
        });
        thread.start();
        return thread;
    }

    private static void confirmInterrupted(Thread thread) throws InterruptedException {
        // validate and clear interrupted bit before join
        assertTrue(Thread.interrupted());
        thread.join();
}
}







