/*Change SSLCertificateSocketFactoryTest.testCreateSocket host

The test may be flaky because it depends on a live server.
Switch to a different server that may be more reliable.

Bug: 3188260
Change-Id:Ibba872489650914db8ddd9a117529556787f66ec*/
//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/SSLCertificateSocketFactoryTest.java b/tests/tests/net/src/android/net/cts/SSLCertificateSocketFactoryTest.java
//Synthetic comment -- index 258ac4d..f125550 100644

//Synthetic comment -- @@ -102,11 +102,10 @@
args = {int.class}
)
})
    @BrokenTest("flaky")
public void testCreateSocket() throws Exception {
new SSLCertificateSocketFactory(100);
int port = 443;
        String host = "www.fortify.net";
InetAddress inetAddress = null;
inetAddress = InetAddress.getLocalHost();
try {







