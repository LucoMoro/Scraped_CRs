//<Beginning of snippet n. 0>
if (certificateMessage == null || certificateMessage.getCertificates().isEmpty()) {
    throw new IllegalArgumentException("No certificates provided in CertificateMessage.");
}

if (clientCert != null && !clientKeyExchange.isEmpty()) {
    // Certificate verify
    String authType = clientKey.getAlgorithm();
    DigitalSignature ds = new DigitalSignature(authType);
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
} else {
    if ((parameters.getNeedClientAuth() && clientCert == null) || clientKeyExchange == null) {
        unexpectedMessage();
        return; // Added return to exit the method correctly
    }

    if (clientCert == null && certificateVerify != null) {
        throw new IllegalStateException("CertificateVerify cannot be processed without a client certificate.");
    }
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
    // Close resources
    c.close();
}

public void test_SSLEngine_getEnableSessionCreation() throws Exception {
    TestSSLContext c = TestSSLContext.create();
    SSLEngine e = c.clientContext.createSSLEngine();
//<End of snippet n. 2>