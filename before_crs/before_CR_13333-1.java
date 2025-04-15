/*Remove lock from OpenSSLSessionImpl finalizer to fix a deadlock

In commit f68a91f63595904de706fe068c8c5f336d61dc85 code that “touches native SSL Sessions” in OpenSSLSocketImpl.java were
made synchronized by locking the OpenSSLSocketImpl class. Unfortunately, the same lock was used for instance counting, which
lead to situations where the native calls could end up blocking the garbage collector. That was fixed in commit
f681a0fc7db58aeead2d6970a717d8a466c1b516.
But it looks like we have run into a similar situation as in the latter case above, again. In the former commit, above, a
synchronization block was added to OpenSSLSessionImpl#finalize as well. So, what we see now is a crash where the garbage
collector is blocked from finalizing an OpenSSLSessionImpl object because of a native call in OpenSSLSocketImpl. Please see
the crash stack below.
The solution we see is to remove the unnecessary synchronized block in OpenSSLSessionImpl#finalize.
Removing the synchronization block is safe as in the cases where the OpenSSLSocketImpl needs to touch the sessionId it holds
a reference to a living OpenSSLSessionImpl object. The OpenSSLSessionImpl object cannot be collected as it lives and
therefore the synchronization on the OpenSSLSessionImpl.class is not needed.

01-14 03:40:37.288 I/dalvikvm(16601): "Thread-9" prio=5 tid=17 NATIVE
01-14 03:40:37.288 I/dalvikvm(16601):   | group="main" sCount=1 dsCount=0 s=N obj=0x2e5b5f00 self=0x178438
01-14 03:40:37.288 I/dalvikvm(16601):   | sysTid=16608 nice=10 sched=0/0 handle=1541464
01-14 03:40:37.288 I/dalvikvm(16601):   at org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImpl.nativeconnect(Native
Method)
01-14 03:40:37.288 I/dalvikvm(16601):   at
org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImpl.startHandshake(OpenSSLSocketImpl.java:305)
01-14 03:40:37.288 I/dalvikvm(16601):   at
org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImpl$SSLInputStream.<init>(OpenSSLSocketImpl.java:502)
01-14 03:40:37.288 I/dalvikvm(16601):   at
org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImpl.getInputStream(OpenSSLSocketImpl.java:443)
01-14 03:40:37.298 I/dalvikvm(16601):   at com.android.email.mail.transport.MailTransport.open(MailTransport.java:161)
01-14 03:40:37.298 I/dalvikvm(16601):   at com.android.email.mail.transport.SmtpSender.open(SmtpSender.java:128)
01-14 03:40:37.298 I/dalvikvm(16601):   at com.android.email.mail.transport.SmtpSender.sendMessage(SmtpSender.java:207)
01-14 03:40:37.298 I/dalvikvm(16601):   at
com.android.email.MessagingController.sendPendingMessagesSynchronous(MessagingController.java:1751)

...

01-14 03:40:37.298 I/dalvikvm(16601): "HeapWorker" daemon prio=5 tid=5 MONITOR
01-14 03:40:37.298 I/dalvikvm(16601):   | group="system" sCount=1 dsCount=0 s=N obj=0x2d7e8e60 self=0x155268
01-14 03:40:37.298 I/dalvikvm(16601):   | sysTid=16602 nice=0 sched=0/0 handle=1132792
01-14 03:40:37.298 I/dalvikvm(16601):   at
org.apache.harmony.xnet.provider.jsse.OpenSSLSessionImpl.finalize(OpenSSLSessionImpl.java:-1)
01-14 03:40:37.298 I/dalvikvm(16601):   at dalvik.system.NativeStart.run(Native Method)*/
//Synthetic comment -- diff --git a/libcore/x-net/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLSessionImpl.java b/libcore/x-net/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLSessionImpl.java
//Synthetic comment -- index b218271..ca7d6f8 100644

//Synthetic comment -- @@ -516,8 +516,6 @@
* Frees the OpenSSL session in the memory.
*/
protected void finalize() {
        synchronized (OpenSSLSocketImpl.class) {
            nativefree(session);
        }
}
}







