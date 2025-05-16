//<Beginning of snippet n. 0>

if (clientCert != null && !clientKeyExchange.isEmpty()) {
// Certificate verify
    String authType = clientKey.getAlgorithm();
    DigitalSignature ds = new DigitalSignature(authType);

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

} else {
    if ((parameters.getNeedClientAuth() && clientCert == null)
        || (clientKeyExchange == null || clientKeyExchange.isEmpty())
        || (clientCert != null && certificateVerify == null)) {
        unexpectedMessage();
        c.close();
        return;
    }
//<End of snippet n. 1>

//<Beginning of snippet n. 2>

c.close();
}

public void test_SSLEngine_getEnableSessionCreation() throws Exception {
    TestSSLContext c = TestSSLContext.create();
    SSLEngine e = c.clientContext.createSSLEngine();

//<End of snippet n. 2>