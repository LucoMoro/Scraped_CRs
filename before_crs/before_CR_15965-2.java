/*Bugs when connecting a SocketChannel registered in a Selector

Seehttp://code.google.com/p/android/issues/detail?id=6309Bug fixed in org.apache.harmony.nio.internal.SelectorImpl by verifying
that there is a connection pending when adding a writable FD to the
internal list of readyKeys.

A unit test is also provided in java.nio.SelectorTest.

Change-Id:Icf47e81b3bcc299608d0672365daf797e44cdfdd*/
//Synthetic comment -- diff --git a/luni/src/test/java/java/nio/SelectorTest.java b/luni/src/test/java/java/nio/SelectorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..b860620

//Synthetic comment -- @@ -0,0 +1,99 @@








//Synthetic comment -- diff --git a/nio/src/main/java/org/apache/harmony/nio/internal/SelectorImpl.java b/nio/src/main/java/org/apache/harmony/nio/internal/SelectorImpl.java
//Synthetic comment -- index 9d96454..9a735b6 100644

//Synthetic comment -- @@ -281,6 +281,14 @@
}
}

/**
* Prepare the readableFDs, writableFDs, readyKeys and flags arrays in
* preparation for a call to {@code INetworkSystem#select()}. After they're
//Synthetic comment -- @@ -315,7 +323,7 @@
readyKeys[r] = key;
r++;
}
            if ((CONNECT_OR_WRITE & interestOps) != 0) {
writableFDs[w] = ((FileDescriptorHandler) key.channel()).getFD();
readyKeys[w + numReadable] = key;
w++;







