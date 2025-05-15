
//<Beginning of snippet n. 0>



// send certificate verify for all certificates except those containing
// fixed DH parameters
        if (clientCert != null && clientCert.certs.length > 0 && !clientKeyExchange.isEmpty()) {
// Certificate verify
String authType = clientKey.getAlgorithm();
DigitalSignature ds = new DigitalSignature(authType);

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


} else {
if ((parameters.getNeedClientAuth() && clientCert == null)
|| clientKeyExchange == null
                    || (clientCert != null && clientCert.certs.length > 0
&& !clientKeyExchange.isEmpty()
&& certificateVerify == null)) {
unexpectedMessage();

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


c.close();
}

   /**
    * http://code.google.com/p/android/issues/detail?id=31903
    * This test case directly tests the fix for the issue.
    */
    public void test_SSLEngine_clientAuthWantedNoClientCert() throws Exception {
        TestSSLContext clientAuthContext
                = TestSSLContext.create(TestKeyStore.getClient(),
                                        TestKeyStore.getServer());
        TestSSLEnginePair p = TestSSLEnginePair.create(clientAuthContext,
                                                       new TestSSLEnginePair.Hooks() {
            @Override
            void beforeBeginHandshake(SSLEngine client, SSLEngine server) {
                server.setWantClientAuth(true);
            }
        });
        assertConnected(p);
        clientAuthContext.close();
    }

   /**
    * http://code.google.com/p/android/issues/detail?id=31903
    * This test case verifies that if the server requires a client cert
    * (setNeedClientAuth) but the client does not provide one SSL connection
    * establishment will fail
    */
    public void test_SSLEngine_clientAuthNeededNoClientCert() throws Exception {
        boolean handshakeExceptionCaught = false;
        TestSSLContext clientAuthContext
                = TestSSLContext.create(TestKeyStore.getClient(),
                                        TestKeyStore.getServer());
        try {
            TestSSLEnginePair.create(clientAuthContext,
                             new TestSSLEnginePair.Hooks() {
                @Override
                void beforeBeginHandshake(SSLEngine client, SSLEngine server) {
                    server.setNeedClientAuth(true);
                }
            });
            fail();
        } catch (SSLHandshakeException expected) {
        } finally {
            clientAuthContext.close();
        }
    }

public void test_SSLEngine_getEnableSessionCreation() throws Exception {
TestSSLContext c = TestSSLContext.create();
SSLEngine e = c.clientContext.createSSLEngine();

//<End of snippet n. 2>








