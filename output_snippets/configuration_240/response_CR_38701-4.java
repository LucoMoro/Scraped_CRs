//<Beginning of snippet n. 0>
if (clientCert != null && !clientKeyExchange.isEmpty()) {
// Certificate verify
String authType = clientKey.getAlgorithm();
DigitalSignature ds = new DigitalSignature(authType);
//<End of snippet n. 0>

}else {
if ((parameters.getNeedClientAuth() && clientCert == null)
|| clientKeyExchange == null
|| (clientCert != null
&& !clientKeyExchange.isEmpty()
&& certificateVerify == null)) {
unexpectedMessage();
}

// Enhanced error handling
if (clientCert == null) {
    throw new CertificateException("No client certificates provided but client auth is required.");
}
if (clientKeyExchange == null) {
    throw new ProtocolException("Client key exchange is missing.");
}
if (certificateVerify == null) {
    throw new ProtocolException("Certificate verify message is missing.");
}

//<End of snippet n. 1>
c.close();
}

//<Beginning of snippet n. 2>
public void test_SSLEngine_getEnableSessionCreation() throws Exception {
    TestSSLContext c = TestSSLContext.create();
    SSLEngine e = c.clientContext.createSSLEngine();
//<End of snippet n. 2>