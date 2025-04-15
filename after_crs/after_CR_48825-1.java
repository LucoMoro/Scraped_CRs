/*Test for certificate generation stopping at right

Make sure that generateCertificate and generateCertificates end at the
right point when there is a end marker.

The support for this seems spotty at best. Various implementations have
broken support in different parts.

Change-Id:Ib8a3f7c451adca4e05fcb3e8409e93c319557497*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java b/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java
//Synthetic comment -- index 9950a10..461cde5 100644

//Synthetic comment -- @@ -268,11 +268,17 @@
hasUnsupportedCriticalExtension(f);
getEncoded(f);
verify(f);
                generateCertificate_PEM_TrailingData(f);
                generateCertificate_DER_TrailingData(f);
generateCertificates_X509_PEM(f);
generateCertificates_X509_DER(f);
generateCertificates_PKCS7_PEM(f);
generateCertificates_PKCS7_DER(f);
generateCertificates_Empty(f);
                generateCertificates_X509_PEM_TrailingData(f);
                generateCertificates_X509_DER_TrailingData(f);
                generateCertificates_PKCS7_PEM_TrailingData(f);
                generateCertificates_PKCS7_DER_TrailingData(f);
} catch (Throwable e) {
out.append("Error encountered checking " + p.getName() + "\n");
e.printStackTrace(out);
//Synthetic comment -- @@ -1102,6 +1108,31 @@
assertEquals(Arrays.toString(cBytes), Arrays.toString(c.getEncoded()));
}

    private void generateCertificate_PEM_TrailingData(CertificateFactory f) throws Exception {
        byte[] certsBytes = getResourceAsBytes(CERTS_X509_PEM);
        byte[] certsTwice = new byte[certsBytes.length * 2];
        System.arraycopy(certsBytes, 0, certsTwice, 0, certsBytes.length);
        System.arraycopy(certsBytes, 0, certsTwice, certsBytes.length, certsBytes.length);
        ByteArrayInputStream bais = new ByteArrayInputStream(certsTwice);

        assertEquals(certsBytes.length * 2, bais.available());
        X509Certificate cert1 = (X509Certificate) f.generateCertificate(bais);
        // TODO: If we had a single PEM certificate, we could know exact bytes.
        assertTrue(certsBytes.length < bais.available());
    }

    private void generateCertificate_DER_TrailingData(CertificateFactory f) throws Exception {
        byte[] cert1Bytes = getResourceAsBytes(CERT_RSA);
        byte[] cert1WithTrailing = new byte[cert1Bytes.length * 2];
        System.arraycopy(cert1Bytes, 0, cert1WithTrailing, 0, cert1Bytes.length);
        System.arraycopy(cert1Bytes, 0, cert1WithTrailing, cert1Bytes.length, cert1Bytes.length);
        ByteArrayInputStream bais = new ByteArrayInputStream(cert1WithTrailing);

        assertEquals(cert1Bytes.length * 2, bais.available());
        X509Certificate cert1 = (X509Certificate) f.generateCertificate(bais);
        assertEquals(cert1Bytes.length, bais.available());
    }

private void generateCertificates_X509_DER(CertificateFactory f) throws Exception {
/* DER-encoded list of certificates */
Collection<? extends X509Certificate> certs = getCertificates(f, CERTS_X509_DER);
//Synthetic comment -- @@ -1139,9 +1170,10 @@
final InputStream is = new ByteArrayInputStream(new byte[0]);

final Collection<? extends Certificate> certs;

        // DRLCertFactory is broken
try {
certs = f.generateCertificates(is);
if ("DRLCertFactory".equals(f.getProvider().getName())) {
fail("should throw when no certificates present");
}
//Synthetic comment -- @@ -1151,10 +1183,107 @@
}
throw e;
}

assertNotNull(certs);
assertEquals(0, certs.size());
}

    private void generateCertificates_X509_PEM_TrailingData(CertificateFactory f) throws Exception {
        byte[] certBytes = getResourceAsBytes(CERTS_X509_PEM);
        byte[] certsPlusExtra = new byte[certBytes.length + 4096];
        System.arraycopy(certBytes, 0, certsPlusExtra, 0, certBytes.length);
        ByteArrayInputStream bais = new ByteArrayInputStream(certsPlusExtra);

        assertEquals(certsPlusExtra.length, bais.available());

        // RI is broken
        try {
            Collection<? extends X509Certificate> certs = (Collection<? extends X509Certificate>)
                    f.generateCertificates(bais);
            if (StandardNames.IS_RI) {
                fail("RI fails on this test.");
            }
        } catch (CertificateParsingException e) {
            if (StandardNames.IS_RI) {
                return;
            }
            throw e;
        }

        // Bouncycastle is broken
        if ("BC".equals(f.getProvider().getName())) {
            assertEquals(0, bais.available());
        } else {
            assertEquals(4096, bais.available());
        }
    }

    private void generateCertificates_X509_DER_TrailingData(CertificateFactory f) throws Exception {
        byte[] certBytes = getResourceAsBytes(CERTS_X509_DER);
        byte[] certsPlusExtra = new byte[certBytes.length + 4096];
        System.arraycopy(certBytes, 0, certsPlusExtra, 0, certBytes.length);
        ByteArrayInputStream bais = new ByteArrayInputStream(certsPlusExtra);

        assertEquals(certsPlusExtra.length, bais.available());

        // RI is broken
        try {
            Collection<? extends X509Certificate> certs = (Collection<? extends X509Certificate>)
                    f.generateCertificates(bais);
            if (StandardNames.IS_RI) {
                fail("RI fails on this test.");
            }
        } catch (CertificateParsingException e) {
            if (StandardNames.IS_RI) {
                return;
            }
            throw e;
        }

        // Bouncycastle is broken
        if ("BC".equals(f.getProvider().getName())) {
            assertEquals(0, bais.available());
        } else {
            assertEquals(4096, bais.available());
        }
    }

    private void generateCertificates_PKCS7_PEM_TrailingData(CertificateFactory f) throws Exception {
        byte[] certBytes = getResourceAsBytes(CERTS_PKCS7_PEM);
        byte[] certsPlusExtra = new byte[certBytes.length + 4096];
        System.arraycopy(certBytes, 0, certsPlusExtra, 0, certBytes.length);
        ByteArrayInputStream bais = new ByteArrayInputStream(certsPlusExtra);

        assertEquals(certsPlusExtra.length, bais.available());
        Collection<? extends X509Certificate> certs = (Collection<? extends X509Certificate>)
                f.generateCertificates(bais);

        // Bouncycastle is broken
        if ("BC".equals(f.getProvider().getName())) {
            assertEquals(0, bais.available());
        } else {
            assertEquals(4096, bais.available());
        }
    }

    private void generateCertificates_PKCS7_DER_TrailingData(CertificateFactory f) throws Exception {
        byte[] certBytes = getResourceAsBytes(CERTS_PKCS7_DER);
        byte[] certsPlusExtra = new byte[certBytes.length + 4096];
        System.arraycopy(certBytes, 0, certsPlusExtra, 0, certBytes.length);
        ByteArrayInputStream bais = new ByteArrayInputStream(certsPlusExtra);

        assertEquals(certsPlusExtra.length, bais.available());
        Collection<? extends X509Certificate> certs = (Collection<? extends X509Certificate>)
                f.generateCertificates(bais);

        // RI is broken
        if (StandardNames.IS_RI) {
            assertEquals(0, bais.available());
        } else {
            assertEquals(4096, bais.available());
        }
    }

@Override
protected void setUp() throws Exception {
super.setUp();







