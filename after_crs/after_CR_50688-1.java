/*Add tests for CertificateFactory byte offset

Make sure that CertificateFactory ends at the place that it should when
reading an InputStream that supports mark and reset.

Change-Id:I3bc20c1e9766f80f1597908707e69d65a6c3b216*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/cert/CertificateFactoryTest.java b/luni/src/test/java/libcore/java/security/cert/CertificateFactoryTest.java
//Synthetic comment -- index e9a91dd..58472ae 100644

//Synthetic comment -- @@ -17,6 +17,10 @@
package libcore.java.security.cert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
//Synthetic comment -- @@ -84,10 +88,18 @@
+ "-----END CERTIFICATE-----";

public void test_generateCertificate() throws Exception {
        Provider[] providers = Security.getProviders("CertificateFactory.X509");
        for (Provider p : providers) {
            CertificateFactory cf = CertificateFactory.getInstance("X509", p);
            try {
                test_generateCertificate(cf);
                testGenerateCertificate_InputStream_Offset_Correct(cf);
            } catch (Exception e) {
                throw new Exception("Problem testing " + p.getName(), e);
            }
        }
}

private void test_generateCertificate(CertificateFactory cf) throws Exception {
byte[] valid = VALID_CERTIFICATE_PEM.getBytes();
Certificate c = cf.generateCertificate(new ByteArrayInputStream(valid));
//Synthetic comment -- @@ -101,4 +113,91 @@
}
}

    private void testGenerateCertificate_InputStream_Offset_Correct(CertificateFactory cf)
            throws Exception {
        byte[] valid = VALID_CERTIFICATE_PEM.getBytes();

        byte[] doubleCertificateData = new byte[valid.length * 2];
        System.arraycopy(valid, 0, doubleCertificateData, 0, valid.length);
        System.arraycopy(valid, 0, doubleCertificateData, valid.length, valid.length);
        MeasuredInputStream certStream = new MeasuredInputStream(new ByteArrayInputStream(
                doubleCertificateData));
        Certificate certificate = cf.generateCertificate(certStream);
        assertNotNull(certificate);
        assertEquals(valid.length, certStream.getCount());
    }

    /**
     * Proxy that counts the number of bytes read from an InputStream.
     */
    private static class MeasuredInputStream extends InputStream {
        private long mCount = 0;

        private long mMarked = 0;

        private InputStream mStream;

        public MeasuredInputStream(InputStream is) {
            mStream = is;
        }

        public long getCount() {
            return mCount;
        }

        @Override
        public int read() throws IOException {
            int nextByte = mStream.read();
            mCount++;
            return nextByte;
        }

        @Override
        public int read(byte[] buffer) throws IOException {
            int count = mStream.read(buffer);
            mCount += count;
            return count;
        }

        @Override
        public int read(byte[] buffer, int offset, int length) throws IOException {
            int count = mStream.read(buffer, offset, length);
            mCount += count;
            return count;
        }

        @Override
        public long skip(long byteCount) throws IOException {
            long count = mStream.skip(byteCount);
            mCount += count;
            return count;
        }

        @Override
        public int available() throws IOException {
            return mStream.available();
        }

        @Override
        public void close() throws IOException {
            mStream.close();
        }

        @Override
        public void mark(int readlimit) {
            mMarked = mCount;
            mStream.mark(readlimit);
        }

        @Override
        public boolean markSupported() {
            return mStream.markSupported();
        }

        @Override
        public synchronized void reset() throws IOException {
            mCount = mMarked;
            mStream.reset();
        }
    }
}








//Synthetic comment -- diff --git a/luni/src/test/java/tests/security/cert/X509Certificate2Test.java b/luni/src/test/java/tests/security/cert/X509Certificate2Test.java
//Synthetic comment -- index 58cc100..8595d77 100644

//Synthetic comment -- @@ -51,8 +51,9 @@

// extension value is empty sequence
byte[] extnValue = pemCert.getExtensionValue("2.5.29.35");
        assertEquals(
                Arrays.toString(new byte[] { 0x04, 0x02, 0x30, 0x00 }),
                Arrays.toString(extnValue));
assertNotNull(pemCert.toString());
// End regression for HARMONY-3384
}
//Synthetic comment -- @@ -499,7 +500,7 @@
assertEquals(0, coll.size());
}

    public void testCertificateException() throws Exception {
try {
generateCert(CERT_TAMPERED);
fail();







