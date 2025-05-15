//<Beginning of snippet n. 0>

if (clientCert != null && !clientKeyExchange.isEmpty()) {
// Certificate verify
String authType = clientKey.getAlgorithm();
DigitalSignature ds = new DigitalSignature(authType);

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

} else {
    if ((parameters.getNeedClientAuth() && clientCert == null)
            || clientKeyExchange == null
            || (clientCert != null && !clientKeyExchange.isEmpty() && certificateVerify == null)) {
        unexpectedMessage();
    } else {
        // Handle the case where CertificateMessage is empty 
        if (clientCert == null || clientCert.isEmpty()) {
            unexpectedMessage();
        }
    }

//<End of snippet n. 1>

//<Beginning of snippet n. 2>

c.close();
}

public void test_SSLEngine_getEnableSessionCreation() throws Exception {
    TestSSLContext c = TestSSLContext.create();
    SSLEngine e = c.clientContext.createSSLEngine();
    // TODO: Add test cases for scenarios with no client certificate available.
//<End of snippet n. 2>