/*CertificateRequest should handle case where certificate is requested but none is available.

Android SSL client was not handling a CertificateRequest where there was no cert to send.
It had a problem because it was assuming that if the CertificateMessage response is not null,
it means there is a cert included, which is not true (if it has no cert to send an empty CertificateMessage
is sent to the server). So I updated the CertificateVerify creation check to also check whether the CertificateMessage
contained any certs (ClientHandshakeImpl.java).
In testing I found that the same error was in the server code so I made the same change there
(ServerHandshakeImpl.java).
I added two test cases to SSLEngineTest - one to directly test the scenario (test_SSLEngine_clientAuthWantedNoClientCert)
and one to just double-check that the server would not allow the connection if setNeedClientAuth (test_SSLEngine_clientAuthNeededNoClientCert).

Bug:http://code.google.com/p/android/issues/detail?id=31903Change-Id:Ideb57d6ccbcdd54ca24dc3063e60aba2653c8414*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/ClientHandshakeImpl.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/ClientHandshakeImpl.java
//Synthetic comment -- index d21aa2c..71fd0ba 100644

//Synthetic comment -- @@ -499,7 +499,7 @@

// send certificate verify for all certificates except those containing
// fixed DH parameters
        if (clientCert != null && !clientKeyExchange.isEmpty()) {
// Certificate verify
String authType = clientKey.getAlgorithm();
DigitalSignature ds = new DigitalSignature(authType);








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/ServerHandshakeImpl.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/ServerHandshakeImpl.java
//Synthetic comment -- index b6a65b4..5ee47cd 100644

//Synthetic comment -- @@ -651,7 +651,7 @@
} else {
if ((parameters.getNeedClientAuth() && clientCert == null)
|| clientKeyExchange == null
                    || (clientCert != null
&& !clientKeyExchange.isEmpty()
&& certificateVerify == null)) {
unexpectedMessage();








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/net/ssl/SSLEngineTest.java b/luni/src/test/java/libcore/javax/net/ssl/SSLEngineTest.java
//Synthetic comment -- index 5e91dc1..c125c34 100644

//Synthetic comment -- @@ -346,6 +346,49 @@
c.close();
}

public void test_SSLEngine_getEnableSessionCreation() throws Exception {
TestSSLContext c = TestSSLContext.create();
SSLEngine e = c.clientContext.createSSLEngine();







