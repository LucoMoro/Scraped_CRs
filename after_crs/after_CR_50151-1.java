/*Make test_SSLSocket_getSupportedCipherSuites_connect continue to next provider on exception

Change-Id:I7d124b4508ffbbde32e60d6901df5f386ed7c5d9*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/net/ssl/SSLSocketTest.java b/luni/src/test/java/libcore/javax/net/ssl/SSLSocketTest.java
//Synthetic comment -- index 0aa254d..619bc12 100644

//Synthetic comment -- @@ -73,41 +73,49 @@
.aliasPrefix("rsa-dsa-ec")
.ca(true)
.build();
        StringBuilder error = new StringBuilder();
if (StandardNames.IS_RI) {
            error.append(test_SSLSocket_getSupportedCipherSuites_connect(
                testKeyStore,
                StandardNames.JSSE_PROVIDER_NAME,
                StandardNames.JSSE_PROVIDER_NAME,
                true,
                true));
        } else {
            error.append(test_SSLSocket_getSupportedCipherSuites_connect(testKeyStore,
                                                                         "HarmonyJSSE",
                                                                         "HarmonyJSSE",
                                                                         false,
                                                                         false));
            error.append(test_SSLSocket_getSupportedCipherSuites_connect(testKeyStore,
                                                                         "AndroidOpenSSL",
                                                                         "AndroidOpenSSL",
                                                                         true,
                                                                         true));
            error.append(test_SSLSocket_getSupportedCipherSuites_connect(testKeyStore,
                                                                         "HarmonyJSSE",
                                                                         "AndroidOpenSSL",
                                                                         false,
                                                                         true));
            error.append(test_SSLSocket_getSupportedCipherSuites_connect(testKeyStore,
                                                                         "AndroidOpenSSL",
                                                                         "HarmonyJSSE",
                                                                         true,
                                                                         false));
}

        if (error.length() != 0) {
            throw new Exception("One or more problems in "
                                + "test_SSLSocket_getSupportedCipherSuites_connect:\n" + error);
        }
}

    private StringBuilder test_SSLSocket_getSupportedCipherSuites_connect(
        TestKeyStore testKeyStore,
        String clientProvider,
        String serverProvider,
        boolean clientSecureRenegotiation,
        boolean serverSecureRenegotiation)
throws Exception {

String clientToServerString = "this is sent from the client to the server...";
//Synthetic comment -- @@ -186,6 +194,9 @@
String message = ("Problem trying to connect cipher suite " + cipherSuite
+ " client=" + clientProvider
+ " server=" + serverProvider);
                    if (false) {
                        throw new Exception(message);
                    }
System.out.println(message);
maybeExpected.printStackTrace();
error.append(message);
//Synthetic comment -- @@ -193,11 +204,8 @@
}
}
}
c.close();
        return error;
}

public void test_SSLSocket_getEnabledCipherSuites() throws Exception {







