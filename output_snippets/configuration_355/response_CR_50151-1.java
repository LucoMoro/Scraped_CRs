//<Beginning of snippet n. 0>
if (StandardNames.IS_RI) {
    test_SSLSocket_getSupportedCipherSuites_connect(testKeyStore,
                                                    StandardNames.JSSE_PROVIDER_NAME,
                                                    StandardNames.JSSE_PROVIDER_NAME,
                                                    true,
                                                    true);
} else {
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

private void test_SSLSocket_getSupportedCipherSuites_connect(TestKeyStore testKeyStore,
                                                             String clientProvider,
                                                             String serverProvider,
                                                             boolean clientSecureRenegotiation,
                                                             boolean serverSecureRenegotiation) {
    if (testKeyStore == null) {
        throw new IllegalArgumentException("TestKeyStore cannot be null.");
    }

    StringBuilder error = new StringBuilder();
    try (Connection c = createConnection(clientProvider, serverProvider)) {
        for (String cipherSuite : getSupportedCipherSuites(clientProvider, serverProvider)) {
            try {
                // Logic to connect using the cipher suite
            } catch (Exception e) {
                error.append("Problem trying to connect cipher suite ").append(cipherSuite)
                     .append(" client=").append(clientProvider)
                     .append(" server=").append(serverProvider).append("\n");
            }
        }
    } catch (IOException ioException) {
        error.append("Failed to establish connection: ").append(ioException.getMessage()).append("\n");
    }

    if (error.length() != 0) {
        logger.error("One or more problems in test_SSLSocket_getSupportedCipherSuites_connect:\n" + error);
    }
}

public void test_SSLSocket_getEnabledCipherSuites() throws Exception {
//<End of snippet n. 0>