//<Beginning of snippet n. 0>
.aliasPrefix("rsa-dsa-ec")
.ca(true)
.build();
if (StandardNames.IS_RI) {
    test_SSLSocket_getSupportedCipherSuites_connect(testKeyStore,
                                                    StandardNames.JSSE_PROVIDER_NAME,
                                                    StandardNames.JSSE_PROVIDER_NAME,
                                                    true,
                                                    true);
} else  {
    test_SSLSocket_getSupportedCipherSuites_connect(testKeyStore,
                                                    "HarmonyJSSE",
                                                    "HarmonyJSSE",
                                                    false,
                                                    false);
    test_SSLSocket_getSupportedCipherSuites_connect(testKeyStore,
                                                    "AndroidOpenSSL",
                                                    "AndroidOpenSSL",
                                                    true,
                                                    true);
    test_SSLSocket_getSupportedCipherSuites_connect(testKeyStore,
                                                    "HarmonyJSSE",
                                                    "AndroidOpenSSL",
                                                    false,
                                                    true);
    test_SSLSocket_getSupportedCipherSuites_connect(testKeyStore,
                                                    "AndroidOpenSSL",
                                                    "HarmonyJSSE",
                                                    true,
                                                    false);
}

}
private void test_SSLSocket_getSupportedCipherSuites_connect(TestKeyStore testKeyStore,
                                                             String clientProvider,
                                                             String serverProvider,
                                                             boolean clientSecureRenegotiation,
                                                             boolean serverSecureRenegotiation) {
    String clientToServerString = "this is sent from the client to the server...";
    String message = ("Problem trying to connect cipher suite " + cipherSuite
            + " client=" + clientProvider
            + " server=" + serverProvider);
    StringBuilder error = new StringBuilder();

    try {
        System.out.println(message);
        // [assuming maybeExpected and additional logic is defined elsewhere]

    } catch (Exception maybeExpected) {
        System.err.println("Error in provider: " + clientProvider + " / " + serverProvider);
        maybeExpected.printStackTrace();
        error.append(message).append("\n");
    }

    if (error.length() != 0) {
        System.err.println("One or more problems in test_SSLSocket_getSupportedCipherSuites_connect:\n" + error);
    }
}
}
//<End of snippet n. 0>