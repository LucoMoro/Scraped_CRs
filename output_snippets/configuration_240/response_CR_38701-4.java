//<Beginning of snippet n. 0>

if (clientCert != null && !clientKeyExchange.isEmpty() && validateCertificateMessage(clientKeyExchange)) {
// Certificate verify
    String authType = clientKey.getAlgorithm();
    DigitalSignature ds = new DigitalSignature(authType);
//<End of snippet n. 0>

//<Beginning of snippet n. 1>

} else {
    if (parameters.getNeedClientAuth()) {
        if (clientCert == null) {
            unexpectedMessage("Client certificate is null.");
            c.close();
            return;
        }
        if (clientKeyExchange.isEmpty()) {
            unexpectedMessage("Client key exchange is empty.");
            c.close();
            return;
        }
    }
    c.close();
}

private boolean validateCertificateMessage(String clientKeyExchange) {
    // Implement validation logic for clientKeyExchange contents
}

public void test_SSLEngine_getEnableSessionCreation() throws Exception {
    TestSSLContext c = TestSSLContext.create();
    SSLEngine e = c.clientContext.createSSLEngine();
//<End of snippet n. 2>