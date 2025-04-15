/*SSLSocketTest: Run through all providers after error

Previously an error would cause the SSLSocketTest to terminate
prematurely. This change make sure it runs through all intended
combinations.

Change-Id:Id7d08c83b3449f204be8c3511a8a4e989e744084*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/net/ssl/SSLSocketTest.java b/luni/src/test/java/libcore/javax/net/ssl/SSLSocketTest.java
//Synthetic comment -- index 0aa254d..331b981 100644

//Synthetic comment -- @@ -73,41 +73,51 @@
.aliasPrefix("rsa-dsa-ec")
.ca(true)
.build();
        StringBuilder error = new StringBuilder();
if (StandardNames.IS_RI) {
test_SSLSocket_getSupportedCipherSuites_connect(testKeyStore,
StandardNames.JSSE_PROVIDER_NAME,
StandardNames.JSSE_PROVIDER_NAME,
true,
                                                            true,
                                                            error);
} else  {
test_SSLSocket_getSupportedCipherSuites_connect(testKeyStore,
"HarmonyJSSE",
"HarmonyJSSE",
false,
                                                            false,
                                                            error);
test_SSLSocket_getSupportedCipherSuites_connect(testKeyStore,
"AndroidOpenSSL",
"AndroidOpenSSL",
true,
                                                            true,
                                                            error);
test_SSLSocket_getSupportedCipherSuites_connect(testKeyStore,
"HarmonyJSSE",
"AndroidOpenSSL",
false,
                                                            true,
                                                            error);
test_SSLSocket_getSupportedCipherSuites_connect(testKeyStore,
"AndroidOpenSSL",
"HarmonyJSSE",
true,
                                                            false,
                                                            error);
}
        if (error.length() > 0) {
            throw new Exception("One or more problems in "
                    + "test_SSLSocket_getSupportedCipherSuites_connect:\n" + error);
        }
}
private void test_SSLSocket_getSupportedCipherSuites_connect(TestKeyStore testKeyStore,
String clientProvider,
String serverProvider,
boolean clientSecureRenegotiation,
                                                                 boolean serverSecureRenegotiation,
                                                                 StringBuilder error)
throws Exception {

String clientToServerString = "this is sent from the client to the server...";
//Synthetic comment -- @@ -130,7 +140,6 @@
cipherSuites = cs.toArray(new String[cs.size()]);
}

for (String cipherSuite : cipherSuites) {
boolean errorExpected = StandardNames.IS_RI && cipherSuite.endsWith("_SHA256");
try {
//Synthetic comment -- @@ -193,10 +202,6 @@
}
}
}
c.close();
}








