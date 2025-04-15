/*Remove InterruptReadable/WritableChannel test

Currently Thread#interrupt() API cannot interrupt (just close fd)
even if a thread is blocking in I/O operation for an InterruptibleChannel.
In other words, if a thread is blocked by read()/write() operation,
then the Thread#interrupt() from another thread will close fd,
but it cannot interrupt the blocking operation of Channel.
In this test case, if the calling sequence is interrupt() -->read()/
write() (actually it occurs on many dual core ICS devices),
then the test passes. Because the fd has already been closed.
However, if the order of the calling sequence would change to
read()/write() --> interrupt(), the test fails (i.e, I/O operation
will not return). It occurs on a device which has a  single core
processor. Indeed, if I change the test case by adding wait()
before interrupt() then it fails on Galaxy Nexus.

The blocking call stack is below;
at libcore.io.Posix.readBytes(Native Method) <-- BLOCKED!
at libcore.io.Posix.read(Posix.java:113)
at libcore.io.BlockGuardOs.read(BlockGuardOs.java:144)
at java.nio.FileChannelImpl.readImpl(FileChannelImpl.java:294)
at java.nio.FileChannelImpl.read(FileChannelImpl.java:278)
at java.nio.PipeImpl$PipeSourceChannel.read(PipeImpl.java:89)
at libcore.java.io.InterruptedStreamTest.
        testInterruptReadableChannel(InterruptedStreamTest.java:133)
...

So we think the both test cases should be removed at this time.
They should be back after the interruption of blocked operation
in Channel is implemented.

And also I correct a wrong method call from
testInterruptWritableSocketChannel().

Change-Id:Iaae3128d264070e818dc82d831ea8048bb43ad4a*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/io/InterruptedStreamTest.java b/luni/src/test/java/libcore/java/io/InterruptedStreamTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index e973b8f..31a7acb

//Synthetic comment -- @@ -78,14 +78,6 @@
testInterruptWriter(writer);
}

public void testInterruptReadableSocketChannel() throws Exception {
sockets = newSocketChannelPair();
testInterruptReadableChannel(sockets[0].getChannel());
//Synthetic comment -- @@ -93,7 +85,7 @@

public void testInterruptWritableSocketChannel() throws Exception {
sockets = newSocketChannelPair();
        testInterruptWritableChannel(sockets[0].getChannel());
}

/**







