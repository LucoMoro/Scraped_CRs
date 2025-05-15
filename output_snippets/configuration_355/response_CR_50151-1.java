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
    List<String> errorMessages = new ArrayList<>();
    test_SSLSocket_getSupportedCipherSuites_connect(testKeyStore, "HarmonyJSSE", "HarmonyJSSE", false, false, errorMessages);
    test_SSLSocket_getSupportedCipherSuites_connect(testKeyStore, "AndroidOpenSSL", "AndroidOpenSSL", true, true, errorMessages);
    test_SSLSocket_getSupportedCipherSuites_connect(testKeyStore, "HarmonyJSSE", "AndroidOpenSSL", false, true, errorMessages);
    test_SSLSocket_getSupportedCipherSuites_connect(testKeyStore, "AndroidOpenSSL", "HarmonyJSSE", true, false, errorMessages);
    
    if (!errorMessages.isEmpty()) {
        StringBuilder errors = new StringBuilder("One or more problems in test_SSLSocket_getSupportedCipherSuites_connect:\n");
        for (String error : errorMessages) {
            errors.append(error).append("\n");
        }
        throw new Exception(errors.toString());
    }
}
c.close();

public void test_SSLSocket_getEnabledCipherSuites() throws Exception {
    // Method implementation here
}

private void test_SSLSocket_getSupportedCipherSuites_connect(TestKeyStore testKeyStore,
                                                             String clientProvider,
                                                             String serverProvider,
                                                             boolean clientSecureRenegotiation,
                                                             boolean serverSecureRenegotiation,
                                                             List<String> errorMessages) {
    String clientToServerString = "this is sent from the client to the server...";
    String message = ("Problem trying to connect cipher suite " + cipherSuite
            + " client=" + clientProvider
            + " server=" + serverProvider);
    errorMessages.add(message);
    Logger.getLogger(getClass().getName()).warning(message); // Using Logger for safe logging
}
//<End of snippet n. 0>