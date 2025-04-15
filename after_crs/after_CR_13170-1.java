/*There is multiple bugs when trying to connect a SocketChannel
registered in a Selector:
- The selector will generate spurious socket-connect ready
  operations.  Calling finishConnect on this ready op will
  trigger NoConnectionPendingException.
- If the OP_CONNECT operation is not removed from the interest
  set then subsequent call to select() or select(long) will return
  immediately and the thread will probably 100% of the CPU.
The problem is that the underlying file description is registered
in the OS select() as soon OP_CONNECT is requested when it should
only be set when the connection is pending.*/




//Synthetic comment -- diff --git a/libcore/nio/src/main/java/org/apache/harmony/nio/internal/SelectorImpl.java b/libcore/nio/src/main/java/org/apache/harmony/nio/internal/SelectorImpl.java
//Synthetic comment -- index f8e7d80..7b47298 100644

//Synthetic comment -- @@ -216,6 +216,14 @@
return true;
}

    private boolean isConnectionPending(SelectionKeyImpl key) {
        SelectableChannel channel = key.channel();
        if (channel instanceof SocketChannel) {
            return ((SocketChannel) channel).isConnectionPending();
        }
        return true;
    }

// Prepares and adds channels to list for selection
private void prepareChannels() {
readableFDs.add(sourcefd);        
//Synthetic comment -- @@ -227,7 +235,7 @@
SelectionKeyImpl key = (SelectionKeyImpl) i.next();
key.oldInterestOps = key.interestOps();
boolean isReadableChannel = ((SelectionKey.OP_ACCEPT | SelectionKey.OP_READ) & key.oldInterestOps) != 0;
                boolean isWritableChannel = (((isConnectionPending(key) ? SelectionKey.OP_CONNECT : 0) | SelectionKey.OP_WRITE) & key.oldInterestOps) != 0;
SelectableChannel channel = key.channel();                  
if (isReadableChannel) {
readChannelList.add(channel.keyFor(this));







