
//<Beginning of snippet n. 0>


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
String message = ("Problem trying to connect cipher suite " + cipherSuite
+ " client=" + clientProvider
+ " server=" + serverProvider);
                    if (false) {
                        throw new Exception(message);
                    }
System.out.println(message);
maybeExpected.printStackTrace();
error.append(message);
}
}
}
c.close();
        return error;
}

public void test_SSLSocket_getEnabledCipherSuites() throws Exception {

//<End of snippet n. 0>








