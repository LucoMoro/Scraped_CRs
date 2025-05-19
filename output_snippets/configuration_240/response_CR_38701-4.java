//<Beginning of snippet n. 0>

if (clientCert != null && clientKeyExchange != null && !clientKeyExchange.isEmpty()) {
    if (clientCert.getCertificates() != null && clientCert.getCertificates().length > 0 && areCertificatesValid(clientCert.getCertificates())) {
        String authType = clientKey.getAlgorithm();
        DigitalSignature ds = new DigitalSignature(authType);
    } else {
        unexpectedMessage("Invalid client certificate or no valid certificates present.");
    }
} else {
    if ((parameters.getNeedClientAuth() && clientCert == null) || clientKeyExchange == null) {
        unexpectedMessage("Client authentication is required, or missing key exchange.");
    }
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

c.close();
}

//<End of snippet n. 1>

//<Beginning of snippet n. 2>

public void test_SSLEngine_getEnableSessionCreation() throws Exception {
    TestSSLContext c = TestSSLContext.create();
    SSLEngine e = c.clientContext.createSSLEngine();
    assertNull("Client certificate should be null", e.getSession().getPeerCertificates());
    assertTrue("ClientKeyExchange should be empty", e.getSession().getPeerCertificates() == null || e.getSession().getPeerCertificates().length == 0);
}

//<End of snippet n. 2>