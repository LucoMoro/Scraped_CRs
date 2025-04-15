/*Fix X509Certificate.getBasicConstraints implementation for DRLCertFactory

Change-Id:I5d1ba078b0f8f9ec8e2950e0da02481e8162a5e6*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/security/cert/X509Certificate.java b/luni/src/main/java/java/security/cert/X509Certificate.java
//Synthetic comment -- index 90737d7..ac4be18 100644

//Synthetic comment -- @@ -364,9 +364,11 @@
* Returns the path length of the certificate constraints from the {@code
* BasicContraints} extension.
*
     * @return the path length of the certificate constraints if the extension
     *         is present or {@code -1} if the extension is not present. {@code
     *         Integer.MAX_VALUE} if there's not limit.
*/
public abstract int getBasicConstraints();









//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/provider/cert/X509CertImpl.java b/luni/src/main/java/org/apache/harmony/security/provider/cert/X509CertImpl.java
//Synthetic comment -- index b15e8ac..68bcec6 100644

//Synthetic comment -- @@ -305,9 +305,9 @@

public int getBasicConstraints() {
if (extensions == null) {
            return Integer.MAX_VALUE;
}
        return extensions.valueOfBasicConstrains();
}

public Collection<List<?>> getSubjectAlternativeNames() throws CertificateParsingException {








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/x509/BasicConstraints.java b/luni/src/main/java/org/apache/harmony/security/x509/BasicConstraints.java
//Synthetic comment -- index 6a473f5..fbdaec4 100644

//Synthetic comment -- @@ -58,6 +58,10 @@
}
}

public int getPathLenConstraint() {
return pathLenConstraint;
}








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/x509/Extensions.java b/luni/src/main/java/org/apache/harmony/security/x509/Extensions.java
//Synthetic comment -- index 3336b0d..614165b 100644

//Synthetic comment -- @@ -222,14 +222,20 @@
* </pre>
* (as specified in RFC 3280)
*
     * @return the value of pathLenConstraint field if extension presents,
     * and Integer.MAX_VALUE if does not.
*/
    public int valueOfBasicConstrains() {
Extension extension = getExtensionByOID("2.5.29.19");
        BasicConstraints bc;
        if ((extension == null) || ((bc = extension.getBasicConstraintsValue()) == null)) {
            return Integer.MAX_VALUE;
}
return bc.getPathLenConstraint();
}








//Synthetic comment -- diff --git a/luni/src/test/java/tests/security/cert/X509CertSelectorTest.java b/luni/src/test/java/tests/security/cert/X509CertSelectorTest.java
//Synthetic comment -- index 1fc6426..049f3a0 100644

//Synthetic comment -- @@ -17,8 +17,6 @@

package tests.security.cert;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
//Synthetic comment -- @@ -53,19 +51,17 @@
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.security.auth.x500.X500Principal;


import org.apache.harmony.security.tests.support.cert.MyCRL;
import org.apache.harmony.security.tests.support.cert.TestUtils;
import org.apache.harmony.security.tests.support.TestKeyPair;
import org.apache.harmony.security.asn1.ASN1Boolean;
import org.apache.harmony.security.asn1.ASN1Integer;
import org.apache.harmony.security.asn1.ASN1OctetString;
import org.apache.harmony.security.asn1.ASN1Oid;
import org.apache.harmony.security.asn1.ASN1Sequence;
import org.apache.harmony.security.asn1.ASN1Type;
import org.apache.harmony.security.x501.Name;
import org.apache.harmony.security.x509.CertificatePolicies;
import org.apache.harmony.security.x509.GeneralName;
//Synthetic comment -- @@ -141,7 +137,7 @@
new X509CertSelector().addSubjectAlternativeName(types[i],
(byte[]) null);
fail("No expected NullPointerException for type: " + types[i]);
            } catch (NullPointerException e) {
}
}
}
//Synthetic comment -- @@ -165,7 +161,7 @@
new X509CertSelector().addSubjectAlternativeName(types[i],
"-0xDFRF");
fail("IOException expected for type: " + types[i]);
            } catch (IOException e) {
}
}
}
//Synthetic comment -- @@ -188,7 +184,7 @@
try {
new X509CertSelector().addPathToName(types[i], (byte[]) null);
fail("No expected NullPointerException for type: " + types[i]);
            } catch (NullPointerException e) {
}
}
}
//Synthetic comment -- @@ -201,9 +197,8 @@
for (int type = 0; type <= 8; type++) {
try {
new X509CertSelector().addPathToName(type, (String) null);
                fail("IOException expected!");
            } catch (IOException ioe) {
                // expected
}
}

//Synthetic comment -- @@ -214,12 +209,7 @@
* java.security.cert.X509CertSelector#X509CertSelector()
*/
public void test_X509CertSelector() {
        X509CertSelector selector = null;
        try {
            selector = new X509CertSelector();
        } catch (Exception e) {
            fail("Unexpected exception " + e.getMessage());
        }
assertEquals(-1, selector.getBasicConstraints());
assertTrue(selector.getMatchAllSubjectAltNames());
}
//Synthetic comment -- @@ -231,49 +221,34 @@
X509CertSelector selector = new X509CertSelector();
X509CertSelector selector1 = (X509CertSelector) selector.clone();

        assertEquals(selector.getMatchAllSubjectAltNames(), selector1
                .getMatchAllSubjectAltNames());
        assertEquals(selector.getAuthorityKeyIdentifier(), selector1
                .getAuthorityKeyIdentifier());
        assertEquals(selector.getBasicConstraints(), selector1
                .getBasicConstraints());
assertEquals(selector.getCertificate(), selector1.getCertificate());
        assertEquals(selector.getCertificateValid(), selector1
                .getCertificateValid());
        assertEquals(selector.getExtendedKeyUsage(), selector1
                .getExtendedKeyUsage());
assertEquals(selector.getIssuer(), selector1.getIssuer());
assertEquals(selector.getIssuerAsBytes(), selector1.getIssuerAsBytes());
        assertEquals(selector.getIssuerAsString(), selector1
                .getIssuerAsString());
assertEquals(selector.getKeyUsage(), selector1.getKeyUsage());
        assertEquals(selector.getNameConstraints(), selector1
                .getNameConstraints());
assertEquals(selector.getPathToNames(), selector1.getPathToNames());
assertEquals(selector.getPolicy(), selector1.getPolicy());
        assertEquals(selector.getPrivateKeyValid(), selector1
                .getPrivateKeyValid());
assertEquals(selector.getSerialNumber(), selector1.getSerialNumber());
assertEquals(selector.getSubject(), selector1.getSubject());
        assertEquals(selector.getSubjectAlternativeNames(), selector1
                .getSubjectAlternativeNames());
        assertEquals(selector.getSubjectAsBytes(), selector1
                .getSubjectAsBytes());
        assertEquals(selector.getSubjectAsString(), selector1
                .getSubjectAsString());
        assertEquals(selector.getSubjectKeyIdentifier(), selector1
                .getSubjectKeyIdentifier());
        assertEquals(selector.getSubjectPublicKey(), selector1
                .getSubjectPublicKey());
        assertEquals(selector.getSubjectPublicKeyAlgID(), selector1
                .getSubjectPublicKeyAlgID());

selector = null;
try {
selector.clone();
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected
}
}

//Synthetic comment -- @@ -285,15 +260,15 @@
byte[] akid2 = new byte[] { 4, 5, 5, 4, 3, 2, 1 }; // random value
X509CertSelector selector = new X509CertSelector();

        assertNull("Selector should return null", selector
                .getAuthorityKeyIdentifier());
selector.setAuthorityKeyIdentifier(akid1);
        assertTrue("The returned keyID should be equal to specified", Arrays
                .equals(akid1, selector.getAuthorityKeyIdentifier()));
        assertTrue("The returned keyID should be equal to specified", Arrays
                .equals(akid1, selector.getAuthorityKeyIdentifier()));
        assertFalse("The returned keyID should differ", Arrays.equals(akid2,
                selector.getAuthorityKeyIdentifier()));
}

/**
//Synthetic comment -- @@ -311,16 +286,16 @@
/**
* java.security.cert.X509CertSelector#getCertificate()
*/
    public void test_getCertificate() throws CertificateException {
X509CertSelector selector = new X509CertSelector();
CertificateFactory certFact = CertificateFactory.getInstance("X509");
        X509Certificate cert1 = (X509Certificate) certFact
                .generateCertificate(new ByteArrayInputStream(TestUtils
                        .getX509Certificate_v3()));

        X509Certificate cert2 = (X509Certificate) certFact
                .generateCertificate(new ByteArrayInputStream(TestUtils
                        .getX509Certificate_v1()));

selector.setCertificate(cert1);
assertEquals(cert1, selector.getCertificate());
//Synthetic comment -- @@ -341,19 +316,19 @@
Date date3 = Calendar.getInstance().getTime();
X509CertSelector selector = new X509CertSelector();

        assertNull("Selector should return null", selector
                .getCertificateValid());
selector.setCertificateValid(date1);
        assertTrue("The returned date should be equal to specified", date1
                .equals(selector.getCertificateValid()));
selector.getCertificateValid().setTime(200);
        assertTrue("The returned date should be equal to specified", date1
                .equals(selector.getCertificateValid()));
        assertFalse("The returned date should differ", date2.equals(selector
                .getCertificateValid()));
selector.setCertificateValid(date3);
        assertTrue("The returned date should be equal to specified", date3
                .equals(selector.getCertificateValid()));
selector.setCertificateValid(null);
assertNull(selector.getCertificateValid());
}
//Synthetic comment -- @@ -361,30 +336,28 @@
/**
* java.security.cert.X509CertSelector#getExtendedKeyUsage()
*/
    public void test_getExtendedKeyUsage() {
        HashSet<String> ku = new HashSet<String>(Arrays
                .asList(new String[] { "1.3.6.1.5.5.7.3.1",
                        "1.3.6.1.5.5.7.3.2", "1.3.6.1.5.5.7.3.3",
                        "1.3.6.1.5.5.7.3.4", "1.3.6.1.5.5.7.3.8",
                        "1.3.6.1.5.5.7.3.9", "1.3.6.1.5.5.7.3.5",
                        "1.3.6.1.5.5.7.3.6", "1.3.6.1.5.5.7.3.7" }));
X509CertSelector selector = new X509CertSelector();

        assertNull("Selector should return null", selector
                .getExtendedKeyUsage());
        try {
            selector.setExtendedKeyUsage(ku);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown.");
        }
        assertTrue(
                "The returned extendedKeyUsage should be equal to specified",
                ku.equals(selector.getExtendedKeyUsage()));
try {
selector.getExtendedKeyUsage().add("KRIBLEGRABLI");
fail("The returned Set should be immutable.");
        } catch (UnsupportedOperationException e) {
            // expected
}
}

//Synthetic comment -- @@ -398,16 +371,16 @@

assertNull("Selector should return null", selector.getIssuer());
selector.setIssuer(iss1);
        assertEquals("The returned issuer should be equal to specified", iss1,
                selector.getIssuer());
        assertFalse("The returned issuer should differ", iss2.equals(selector
                .getIssuer()));
}

/**
* java.security.cert.X509CertSelector#getIssuerAsBytes()
*/
    public void test_getIssuerAsBytes() {
byte[] name1 = new byte[]
// manually obtained DER encoding of "O=First Org." issuer name;
{ 48, 21, 49, 19, 48, 17, 6, 3, 85, 4, 10, 19, 10, 70, 105, 114, 115,
//Synthetic comment -- @@ -421,20 +394,14 @@
X500Principal iss2 = new X500Principal(name2);
X509CertSelector selector = new X509CertSelector();

        try {
            assertNull("Selector should return null", selector
                    .getIssuerAsBytes());
            selector.setIssuer(iss1);
            assertTrue("The returned issuer should be equal to specified",
                    Arrays.equals(name1, selector.getIssuerAsBytes()));
            assertFalse("The returned issuer should differ", name2
                    .equals(selector.getIssuerAsBytes()));
            selector.setIssuer(iss2);
            assertTrue("The returned issuer should be equal to specified",
                    Arrays.equals(name2, selector.getIssuerAsBytes()));
        } catch (IOException e) {
            fail("Unexpected IOException was thrown.");
        }
}

/**
//Synthetic comment -- @@ -450,12 +417,12 @@
assertNull("Selector should return null", selector.getIssuerAsString());
selector.setIssuer(iss1);
assertEquals("The returned issuer should be equal to specified", name1,
                selector.getIssuerAsString());
        assertFalse("The returned issuer should differ", name2.equals(selector
                .getIssuerAsString()));
selector.setIssuer(iss2);
assertEquals("The returned issuer should be equal to specified", name2,
                selector.getIssuerAsString());
}

/**
//Synthetic comment -- @@ -468,12 +435,12 @@

assertNull("Selector should return null", selector.getKeyUsage());
selector.setKeyUsage(ku);
        assertTrue("The returned date should be equal to specified", Arrays
                .equals(ku, selector.getKeyUsage()));
boolean[] result = selector.getKeyUsage();
result[0] = !result[0];
        assertTrue("The returned keyUsage should be equal to specified", Arrays
                .equals(ku, selector.getKeyUsage()));
}

/**
//Synthetic comment -- @@ -481,11 +448,11 @@
*/
public void test_getMatchAllSubjectAltNames() {
X509CertSelector selector = new X509CertSelector();
        assertTrue("The matchAllNames initially should be true", selector
                .getMatchAllSubjectAltNames());
selector.setMatchAllSubjectAltNames(false);
        assertFalse("The value should be false", selector
                .getMatchAllSubjectAltNames());
}

/**
//Synthetic comment -- @@ -523,67 +490,68 @@

for (int i = 0; i < constraintBytes.length; i++) {
selector.setNameConstraints(constraintBytes[i]);
            assertTrue(Arrays.equals(constraintBytes[i], selector
                    .getNameConstraints()));
}
}

/**
* java.security.cert.X509CertSelector#getPathToNames()
*/
    public void test_getPathToNames() {
        try {
            GeneralName san0 = new GeneralName(new OtherName("1.2.3.4.5",
                    new byte[] { 1, 2, 0, 1 }));
            GeneralName san1 = new GeneralName(1, "rfc@822.Name");
            GeneralName san2 = new GeneralName(2, "dNSName");
            GeneralName san3 = new GeneralName(new ORAddress());
            GeneralName san4 = new GeneralName(new Name("O=Organization"));
            GeneralName san6 = new GeneralName(6, "http://uniform.Resource.Id");
            GeneralName san7 = new GeneralName(7, "1.1.1.1");
            GeneralName san8 = new GeneralName(8, "1.2.3.4444.55555");

            GeneralNames sans1 = new GeneralNames();
            sans1.addName(san0);
            sans1.addName(san1);
            sans1.addName(san2);
            sans1.addName(san3);
            sans1.addName(san4);
            sans1.addName(san6);
            sans1.addName(san7);
            sans1.addName(san8);
            GeneralNames sans2 = new GeneralNames();
            sans2.addName(san0);

            TestCert cert1 = new TestCert(sans1);
            TestCert cert2 = new TestCert(sans2);
            X509CertSelector selector = new X509CertSelector();
            selector.setMatchAllSubjectAltNames(true);

            selector.setPathToNames(null);
            assertTrue("Any certificate should match in the case of null "
                    + "subjectAlternativeNames criteria.", selector
                    .match(cert1)
                    && selector.match(cert2));

            Collection<List<?>> sans = sans1.getPairsList();

            selector.setPathToNames(sans);
            selector.getPathToNames();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected IOException was thrown.");
        }
}

/**
* java.security.cert.X509CertSelector#getPolicy()
*/
public void test_getPolicy() throws IOException {
        String[] policies1 = new String[] { "1.3.6.1.5.5.7.3.1",
                "1.3.6.1.5.5.7.3.2", "1.3.6.1.5.5.7.3.3", "1.3.6.1.5.5.7.3.4",
                "1.3.6.1.5.5.7.3.8", "1.3.6.1.5.5.7.3.9", "1.3.6.1.5.5.7.3.5",
                "1.3.6.1.5.5.7.3.6", "1.3.6.1.5.5.7.3.7" };

String[] policies2 = new String[] { "1.3.6.7.3.1" };

//Synthetic comment -- @@ -612,13 +580,13 @@

assertNull("Selector should return null", selector.getPrivateKeyValid());
selector.setPrivateKeyValid(date1);
        assertTrue("The returned date should be equal to specified", date1
                .equals(selector.getPrivateKeyValid()));
selector.getPrivateKeyValid().setTime(200);
        assertTrue("The returned date should be equal to specified", date1
                .equals(selector.getPrivateKeyValid()));
        assertFalse("The returned date should differ", date2.equals(selector
                .getPrivateKeyValid()));
}

/**
//Synthetic comment -- @@ -632,9 +600,9 @@
assertNull("Selector should return null", selector.getSerialNumber());
selector.setSerialNumber(ser1);
assertEquals("The returned serial number should be equal to specified",
                ser1, selector.getSerialNumber());
        assertFalse("The returned serial number should differ", ser2
                .equals(selector.getSerialNumber()));
}

/**
//Synthetic comment -- @@ -648,73 +616,65 @@
assertNull("Selector should return null", selector.getSubject());
selector.setSubject(sub1);
assertEquals("The returned subject should be equal to specified", sub1,
                selector.getSubject());
        assertFalse("The returned subject should differ", sub2.equals(selector
                .getSubject()));
}

/**
* java.security.cert.X509CertSelector#getSubjectAlternativeNames()
*/
    public void test_getSubjectAlternativeNames() {
        try {
            GeneralName san1 = new GeneralName(1, "rfc@822.Name");
            GeneralName san2 = new GeneralName(2, "dNSName");

            GeneralNames sans = new GeneralNames();
            sans.addName(san1);
            sans.addName(san2);

            TestCert cert_1 = new TestCert(sans);
            X509CertSelector selector = new X509CertSelector();

            assertNull("Selector should return null", selector
                    .getSubjectAlternativeNames());

            selector.setSubjectAlternativeNames(sans.getPairsList());
            assertTrue("The certificate should match the selection criteria.",
                    selector.match(cert_1));
            selector.getSubjectAlternativeNames().clear();
            assertTrue("The modification of initialization object "
                    + "should not affect the modification "
                    + "of internal object.", selector.match(cert_1));
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected IOException was thrown.");
        }
}

/**
* java.security.cert.X509CertSelector#getSubjectAsBytes()
*/
    public void test_getSubjectAsBytes() {
byte[] name1 = new byte[]
// manually obtained DER encoding of "O=First Org." issuer name;
        { 48, 21, 49, 19, 48, 17, 6, 3, 85, 4, 10, 19, 10, 70, 105, 114, 115,
                116, 32, 79, 114, 103, 46 };
byte[] name2 = new byte[]
// manually obtained DER encoding of "O=Second Org." issuer name;
        { 48, 22, 49, 20, 48, 18, 6, 3, 85, 4, 10, 19, 11, 83, 101, 99, 111,
                110, 100, 32, 79, 114, 103, 46 };

X500Principal sub1 = new X500Principal(name1);
X500Principal sub2 = new X500Principal(name2);
X509CertSelector selector = new X509CertSelector();

        try {
            assertNull("Selector should return null", selector
                    .getSubjectAsBytes());
            selector.setSubject(sub1);
            assertTrue("The returned issuer should be equal to specified",
                    Arrays.equals(name1, selector.getSubjectAsBytes()));
            assertFalse("The returned issuer should differ", name2
                    .equals(selector.getSubjectAsBytes()));
            selector.setSubject(sub2);
            assertTrue("The returned issuer should be equal to specified",
                    Arrays.equals(name2, selector.getSubjectAsBytes()));
        } catch (IOException e) {
            fail("Unexpected IOException was thrown.");
        }
}

/**
//Synthetic comment -- @@ -730,12 +690,12 @@
assertNull("Selector should return null", selector.getSubjectAsString());
selector.setSubject(sub1);
assertEquals("The returned subject should be equal to specified",
                name1, selector.getSubjectAsString());
        assertFalse("The returned subject should differ", name2.equals(selector
                .getSubjectAsString()));
selector.setSubject(sub2);
assertEquals("The returned subject should be equal to specified",
                name2, selector.getSubjectAsString());
}

/**
//Synthetic comment -- @@ -746,16 +706,15 @@
byte[] skid2 = new byte[] { 4, 5, 5, 4, 3, 2, 1 }; // random value
X509CertSelector selector = new X509CertSelector();

        assertNull("Selector should return null", selector
                .getSubjectKeyIdentifier());
selector.setSubjectKeyIdentifier(skid1);
        assertTrue("The returned keyID should be equal to specified", Arrays
                .equals(skid1, selector.getSubjectKeyIdentifier()));
selector.getSubjectKeyIdentifier()[0]++;
        assertTrue("The returned keyID should be equal to specified", Arrays
                .equals(skid1, selector.getSubjectKeyIdentifier()));
        assertFalse("The returned keyID should differ", Arrays.equals(skid2,
                selector.getSubjectKeyIdentifier()));
}

/**
//Synthetic comment -- @@ -793,14 +752,12 @@
/**
* java.security.cert.X509CertSelector#getSubjectPublicKeyAlgID()
*/
    public void test_getSubjectPublicKeyAlgID() {

X509CertSelector selector = new X509CertSelector();
        String[] validOIDs = { "0.0.20", "1.25.0", "2.0.39", "0.2.10", "1.35.15",
                "2.17.89" };

        assertNull("Selector should return null", selector
                .getSubjectPublicKeyAlgID());

for (int i = 0; i < validOIDs.length; i++) {
try {
//Synthetic comment -- @@ -815,33 +772,28 @@
String pkaid1 = "1.2.840.113549.1.1.1"; // RSA encryption
String pkaid2 = "1.2.840.113549.1.1.4"; // MD5 with RSA encryption

        try {
            selector.setSubjectPublicKeyAlgID(pkaid1);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown.");
        }
        assertTrue("The returned oid should be equal to specified", pkaid1
                .equals(selector.getSubjectPublicKeyAlgID()));
        assertFalse("The returned oid should differ", pkaid2.equals(selector
                .getSubjectPublicKeyAlgID()));
}

/**
* java.security.cert.X509CertSelector#match(java.security.cert.Certificate)
*/
    public void test_matchLjava_security_cert_Certificate()
            throws CertificateException {
X509CertSelector selector = new X509CertSelector();
assertFalse(selector.match(null));

CertificateFactory certFact = CertificateFactory.getInstance("X509");
        X509Certificate cert1 = (X509Certificate) certFact
                .generateCertificate(new ByteArrayInputStream(TestUtils
                        .getX509Certificate_v3()));

        X509Certificate cert2 = (X509Certificate) certFact
                .generateCertificate(new ByteArrayInputStream(TestUtils
                        .getX509Certificate_v1()));

selector.setCertificate(cert1);
assertTrue(selector.match(cert1));
//Synthetic comment -- @@ -855,7 +807,7 @@
/**
* java.security.cert.X509CertSelector#setAuthorityKeyIdentifier(byte[])
*/
    public void test_setAuthorityKeyIdentifierLB$() throws CertificateException {
X509CertSelector selector = new X509CertSelector();

byte[] akid1 = new byte[] { 1, 2, 3, 4, 5 }; // random value
//Synthetic comment -- @@ -895,9 +847,7 @@
for (int i = 0; i < invalidValues.length; i++) {
try {
selector.setBasicConstraints(-3);
                fail("IllegalArgumentException expected");
            } catch (IllegalArgumentException e) {
                // expected
}
}

//Synthetic comment -- @@ -912,7 +862,7 @@
* java.security.cert.X509CertSelector#setCertificate(java.security.cert.Certificate)
*/
public void test_setCertificateLjava_security_cert_X509Certificate()
            throws CertificateException {

TestCert cert1 = new TestCert("same certificate");
TestCert cert2 = new TestCert("other certificate");
//Synthetic comment -- @@ -920,16 +870,16 @@

selector.setCertificate(null);
assertTrue("Any certificates should match in the case of null "
                + "certificateEquals criteria.", selector.match(cert1)
                && selector.match(cert2));
selector.setCertificate(cert1);
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert1));
assertFalse("The certificate should not match the selection criteria.",
                selector.match(cert2));
selector.setCertificate(cert2);
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert2));
selector.setCertificate(null);
assertNull(selector.getCertificate());
}
//Synthetic comment -- @@ -938,7 +888,7 @@
* java.security.cert.X509CertSelector#setCertificateValid(java.util.Date)
*/
public void test_setCertificateValidLjava_util_Date()
            throws CertificateException {
X509CertSelector selector = new X509CertSelector();

Date date1 = new Date(100);
//Synthetic comment -- @@ -962,50 +912,48 @@
/**
* java.security.cert.X509CertSelector#setExtendedKeyUsage(Set<String>)
*/
    public void test_setExtendedKeyUsageLjava_util_Set()
            throws CertificateException {
        HashSet<String> ku1 = new HashSet<String>(Arrays
                .asList(new String[] { "1.3.6.1.5.5.7.3.1",
                        "1.3.6.1.5.5.7.3.2", "1.3.6.1.5.5.7.3.3",
                        "1.3.6.1.5.5.7.3.4", "1.3.6.1.5.5.7.3.8",
                        "1.3.6.1.5.5.7.3.9", "1.3.6.1.5.5.7.3.5",
                        "1.3.6.1.5.5.7.3.6", "1.3.6.1.5.5.7.3.7" }));
HashSet<String> ku2 = new HashSet<String>(Arrays.asList(new String[] {
                "1.3.6.1.5.5.7.3.1", "1.3.6.1.5.5.7.3.2", "1.3.6.1.5.5.7.3.3",
                "1.3.6.1.5.5.7.3.4", "1.3.6.1.5.5.7.3.8", "1.3.6.1.5.5.7.3.9",
                "1.3.6.1.5.5.7.3.5", "1.3.6.1.5.5.7.3.6" }));
TestCert cert1 = new TestCert(ku1);
TestCert cert2 = new TestCert(ku2);

X509CertSelector selector = new X509CertSelector();

        try {
            selector.setExtendedKeyUsage(null);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown.");
        }
assertTrue("Any certificate should match in the case of null "
                + "extendedKeyUsage criteria.", selector.match(cert1)
                && selector.match(cert2));
        try {
            selector.setExtendedKeyUsage(ku1);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown.");
        }
assertEquals(ku1, selector.getExtendedKeyUsage());

        try {
            selector.setExtendedKeyUsage(ku2);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown.");
        }
assertEquals(ku2, selector.getExtendedKeyUsage());
}

/**
* java.security.cert.X509CertSelector#setIssuer(byte[])
*/
    public void test_setIssuerLB$() throws CertificateException {
byte[] name1 = new byte[]
// manually obtained DER encoding of "O=First Org." issuer name;
{ 48, 21, 49, 19, 48, 17, 6, 3, 85, 4, 10, 19, 10, 70, 105, 114, 115,
//Synthetic comment -- @@ -1021,36 +969,24 @@

X509CertSelector selector = new X509CertSelector();

        try {
            selector.setIssuer((byte[]) null);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown.");
        }
assertTrue("Any certificates should match "
                + "in the case of null issuer criteria.", selector.match(cert1)
                && selector.match(cert2));
        try {
            selector.setIssuer(name1);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown.");
        }
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert1));
assertFalse("The certificate should not match the selection criteria.",
                selector.match(cert2));
        try {
            selector.setIssuer(name2);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown.");
        }
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert2));
}

/**
* java.security.cert.X509CertSelector#setIssuer(java.lang.String)
*/
    public void test_setIssuerLjava_lang_String() throws CertificateException {

String name1 = "O=First Org.";
String name2 = "O=Second Org.";
//Synthetic comment -- @@ -1061,37 +997,25 @@

X509CertSelector selector = new X509CertSelector();

        try {
            selector.setIssuer((String) null);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown.");
        }
assertTrue("Any certificates should match "
                + "in the case of null issuer criteria.", selector.match(cert1)
                && selector.match(cert2));
        try {
            selector.setIssuer(name1);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown.");
        }
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert1));
assertFalse("The certificate should not match the selection criteria.",
                selector.match(cert2));
        try {
            selector.setIssuer(name2);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown.");
        }
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert2));
}

/**
* java.security.cert.X509CertSelector#setIssuer(javax.security.auth.x500.X500Principal)
*/
public void test_setIssuerLjavax_security_auth_x500_X500Principal()
            throws CertificateException {
X500Principal iss1 = new X500Principal("O=First Org.");
X500Principal iss2 = new X500Principal("O=Second Org.");
TestCert cert1 = new TestCert(iss1);
//Synthetic comment -- @@ -1100,22 +1024,22 @@

selector.setIssuer((X500Principal) null);
assertTrue("Any certificates should match "
                + "in the case of null issuer criteria.", selector.match(cert1)
                && selector.match(cert2));
selector.setIssuer(iss1);
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert1));
assertFalse("The certificate should not match the selection criteria.",
                selector.match(cert2));
selector.setIssuer(iss2);
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert2));
}

/**
* java.security.cert.X509CertSelector#setKeyUsage(boolean)
*/
    public void test_setKeyUsageZ() throws CertificateException {
boolean[] ku1 = new boolean[] { true, true, true, true, true, true,
true, true, true };
// decipherOnly is disallowed
//Synthetic comment -- @@ -1128,17 +1052,16 @@
X509CertSelector selector = new X509CertSelector();

selector.setKeyUsage(null);
        assertTrue("Any certificate should match in the case of null "
                + "keyUsage criteria.", selector.match(cert1)
                && selector.match(cert2));
selector.setKeyUsage(ku1);
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert1));
assertFalse("The certificate should not match the selection criteria.",
                selector.match(cert2));
assertTrue("The certificate which does not have a keyUsage extension "
                + "implicitly allows all keyUsage values.", selector
                .match(cert3));
selector.setKeyUsage(ku2);
ku2[0] = !ku2[0];
assertTrue("The certificate should match the selection criteria.",
//Synthetic comment -- @@ -1186,67 +1109,67 @@

for (int i = 0; i < constraintBytes.length; i++) {
selector.setNameConstraints(constraintBytes[i]);
            assertTrue(Arrays.equals(constraintBytes[i], selector
                    .getNameConstraints()));
}
}

/**
* java.security.cert.X509CertSelector#setPathToNames(Collection<List<?>>)
*/
    public void test_setPathToNamesLjava_util_Collection() {
        try {
            GeneralName san0 = new GeneralName(new OtherName("1.2.3.4.5",
                    new byte[] { 1, 2, 0, 1 }));
            GeneralName san1 = new GeneralName(1, "rfc@822.Name");
            GeneralName san2 = new GeneralName(2, "dNSName");
            GeneralName san3 = new GeneralName(new ORAddress());
            GeneralName san4 = new GeneralName(new Name("O=Organization"));
            GeneralName san6 = new GeneralName(6, "http://uniform.Resource.Id");
            GeneralName san7 = new GeneralName(7, "1.1.1.1");
            GeneralName san8 = new GeneralName(8, "1.2.3.4444.55555");

            GeneralNames sans1 = new GeneralNames();
            sans1.addName(san0);
            sans1.addName(san1);
            sans1.addName(san2);
            sans1.addName(san3);
            sans1.addName(san4);
            sans1.addName(san6);
            sans1.addName(san7);
            sans1.addName(san8);
            GeneralNames sans2 = new GeneralNames();
            sans2.addName(san0);

            TestCert cert1 = new TestCert(sans1);
            TestCert cert2 = new TestCert(sans2);
            X509CertSelector selector = new X509CertSelector();
            selector.setMatchAllSubjectAltNames(true);

            selector.setPathToNames(null);
            assertTrue("Any certificate should match in the case of null "
                    + "subjectAlternativeNames criteria.", selector
                    .match(cert1)
                    && selector.match(cert2));

            Collection<List<?>> sans = sans1.getPairsList();

            selector.setPathToNames(sans);
            selector.getPathToNames();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected IOException was thrown.");
        }
}

/**
* java.security.cert.X509CertSelector#setPolicy(Set<String>)
*/
public void test_setPolicyLjava_util_Set() throws IOException {
        String[] policies1 = new String[] { "1.3.6.1.5.5.7.3.1",
                "1.3.6.1.5.5.7.3.2", "1.3.6.1.5.5.7.3.3", "1.3.6.1.5.5.7.3.4",
                "1.3.6.1.5.5.7.3.8", "1.3.6.1.5.5.7.3.9", "1.3.6.1.5.5.7.3.5",
                "1.3.6.1.5.5.7.3.6", "1.3.6.1.5.5.7.3.7" };

String[] policies2 = new String[] { "1.3.6.7.3.1" };

//Synthetic comment -- @@ -1260,27 +1183,27 @@

selector.setPolicy(null);
assertTrue("Any certificate should match in the case of null "
                + "privateKeyValid criteria.", selector.match(cert1)
                && selector.match(cert2));

selector.setPolicy(p1);
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert1));
assertFalse("The certificate should not match the selection criteria.",
                selector.match(cert2));

selector.setPolicy(p2);
assertFalse("The certificate should not match the selection criteria.",
                selector.match(cert1));
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert2));
}

/**
* java.security.cert.X509CertSelector#setPrivateKeyValid(java.util.Date)
*/
public void test_setPrivateKeyValidLjava_util_Date()
            throws CertificateException {
Date date1 = new Date(100000000);
Date date2 = new Date(200000000);
Date date3 = new Date(300000000);
//Synthetic comment -- @@ -1293,24 +1216,24 @@

selector.setPrivateKeyValid(null);
assertTrue("Any certificate should match in the case of null "
                + "privateKeyValid criteria.", selector.match(cert1)
                && selector.match(cert2));
selector.setPrivateKeyValid(date4);
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert1));
assertFalse("The certificate should not match the selection criteria.",
                selector.match(cert2));
selector.setPrivateKeyValid(date5);
date5.setTime(date4.getTime());
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert2));
}

/**
* java.security.cert.X509CertSelector#setSerialNumber(java.math.BigInteger)
*/
public void test_setSerialNumberLjava_math_BigInteger()
            throws CertificateException {
BigInteger ser1 = new BigInteger("10000");
BigInteger ser2 = new BigInteger("10001");
TestCert cert1 = new TestCert(ser1);
//Synthetic comment -- @@ -1319,22 +1242,22 @@

selector.setSerialNumber(null);
assertTrue("Any certificate should match in the case of null "
                + "serialNumber criteria.", selector.match(cert1)
                && selector.match(cert2));
selector.setSerialNumber(ser1);
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert1));
assertFalse("The certificate should not match the selection criteria.",
                selector.match(cert2));
selector.setSerialNumber(ser2);
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert2));
}

/**
* java.security.cert.X509CertSelector#setSubject(byte[])
*/
    public void test_setSubjectLB$() throws CertificateException {
byte[] name1 = new byte[]
// manually obtained DER encoding of "O=First Org." issuer name;
{ 48, 21, 49, 19, 48, 17, 6, 3, 85, 4, 10, 19, 10, 70, 105, 114, 115,
//Synthetic comment -- @@ -1350,36 +1273,24 @@

X509CertSelector selector = new X509CertSelector();

        try {
            selector.setSubject((byte[]) null);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown.");
        }
assertTrue("Any certificates should match "
                + "in the case of null issuer criteria.", selector.match(cert1)
                && selector.match(cert2));
        try {
            selector.setSubject(name1);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown.");
        }
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert1));
assertFalse("The certificate should not match the selection criteria.",
                selector.match(cert2));
        try {
            selector.setSubject(name2);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown.");
        }
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert2));
}

/**
* java.security.cert.X509CertSelector#setSubject(java.lang.String)
*/
    public void test_setSubjectLjava_lang_String() throws CertificateException {
String name1 = "O=First Org.";
String name2 = "O=Second Org.";
X500Principal sub1 = new X500Principal(name1);
//Synthetic comment -- @@ -1388,38 +1299,25 @@
TestCert cert2 = new TestCert(sub2);
X509CertSelector selector = new X509CertSelector();

        try {
            selector.setSubject((String) null);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown.");
        }
assertTrue("Any certificates should match "
                + "in the case of null subject criteria.", selector
                .match(cert1)
                && selector.match(cert2));
        try {
            selector.setSubject(name1);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown.");
        }
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert1));
assertFalse("The certificate should not match the selection criteria.",
                selector.match(cert2));
        try {
            selector.setSubject(name2);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown.");
        }
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert2));
}

/**
* java.security.cert.X509CertSelector#setSubject(javax.security.auth.x500.X500Principal)
*/
public void test_setSubjectLjavax_security_auth_x500_X500Principal()
            throws CertificateException {
X500Principal sub1 = new X500Principal("O=First Org.");
X500Principal sub2 = new X500Principal("O=Second Org.");
TestCert cert1 = new TestCert(sub1);
//Synthetic comment -- @@ -1428,73 +1326,66 @@

selector.setSubject((X500Principal) null);
assertTrue("Any certificates should match "
                + "in the case of null subjcet criteria.", selector
                .match(cert1)
                && selector.match(cert2));
selector.setSubject(sub1);
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert1));
assertFalse("The certificate should not match the selection criteria.",
                selector.match(cert2));
selector.setSubject(sub2);
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert2));
}

/**
* java.security.cert.X509CertSelector#setSubjectAlternativeNames(Collection<List<?>>)
*/
    public void test_setSubjectAlternativeNamesLjava_util_Collection() {

        try {
            GeneralName san0 = new GeneralName(new OtherName("1.2.3.4.5",
                    new byte[] { 1, 2, 0, 1 }));
            GeneralName san1 = new GeneralName(1, "rfc@822.Name");
            GeneralName san2 = new GeneralName(2, "dNSName");
            GeneralName san3 = new GeneralName(new ORAddress());
            GeneralName san4 = new GeneralName(new Name("O=Organization"));
            GeneralName san6 = new GeneralName(6, "http://uniform.Resource.Id");
            GeneralName san7 = new GeneralName(7, "1.1.1.1");
            GeneralName san8 = new GeneralName(8, "1.2.3.4444.55555");

            GeneralNames sans1 = new GeneralNames();
            sans1.addName(san0);
            sans1.addName(san1);
            sans1.addName(san2);
            sans1.addName(san3);
            sans1.addName(san4);
            sans1.addName(san6);
            sans1.addName(san7);
            sans1.addName(san8);
            GeneralNames sans2 = new GeneralNames();
            sans2.addName(san0);

            TestCert cert1 = new TestCert(sans1);
            TestCert cert2 = new TestCert(sans2);
            X509CertSelector selector = new X509CertSelector();
            selector.setMatchAllSubjectAltNames(true);

            selector.setSubjectAlternativeNames(null);
            assertTrue("Any certificate should match in the case of null "
                    + "subjectAlternativeNames criteria.", selector
                    .match(cert1)
                    && selector.match(cert2));

            Collection<List<?>> sans = sans1.getPairsList();

            selector.setSubjectAlternativeNames(sans);

            selector.getSubjectAlternativeNames();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected IOException was thrown.");
        }
}

/**
* java.security.cert.X509CertSelector#setSubjectKeyIdentifier(byte[])
*/
    public void test_setSubjectKeyIdentifierLB$() throws CertificateException {
byte[] skid1 = new byte[] { 1, 2, 3, 4, 5 }; // random value
byte[] skid2 = new byte[] { 5, 4, 3, 2, 1 }; // random value
TestCert cert1 = new TestCert(skid1);
//Synthetic comment -- @@ -1503,17 +1394,17 @@

selector.setSubjectKeyIdentifier(null);
assertTrue("Any certificate should match in the case of null "
                + "serialNumber criteria.", selector.match(cert1)
                && selector.match(cert2));
selector.setSubjectKeyIdentifier(skid1);
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert1));
assertFalse("The certificate should not match the selection criteria.",
                selector.match(cert2));
selector.setSubjectKeyIdentifier(skid2);
skid2[0]++;
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert2));
}

/**
//Synthetic comment -- @@ -1545,15 +1436,9 @@
* java.security.cert.X509CertSelector#setSubjectPublicKey(java.security.PublicKey key)
*/
public void test_setSubjectPublicKeyLjava_security_PublicKey()
            throws CertificateException {
        PublicKey pkey1 = null;
        PublicKey pkey2 = null;
        try {
            pkey1 = new TestKeyPair("RSA").getPublic();
            pkey2 = new TestKeyPair("DSA").getPublic();
        } catch (Exception e) {
            fail("Unexpected Exception was thrown: " + e.getMessage());
        }

TestCert cert1 = new TestCert(pkey1);
TestCert cert2 = new TestCert(pkey2);
//Synthetic comment -- @@ -1561,90 +1446,75 @@

selector.setSubjectPublicKey((PublicKey) null);
assertTrue("Any certificate should match in the case of null "
                + "subjectPublicKey criteria.", selector.match(cert1)
                && selector.match(cert2));
selector.setSubjectPublicKey(pkey1);
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert1));
assertFalse("The certificate should not match the selection criteria.",
                selector.match(cert2));
selector.setSubjectPublicKey(pkey2);
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert2));
}

/**
* java.security.cert.X509CertSelector#setSubjectPublicKeyAlgID(java.lang.String)
*/
    public void test_setSubjectPublicKeyAlgIDLjava_lang_String()
            throws CertificateException {

X509CertSelector selector = new X509CertSelector();
String pkaid1 = "1.2.840.113549.1.1.1"; // RSA (source:
// http://asn1.elibel.tm.fr)
String pkaid2 = "1.2.840.10040.4.1"; // DSA (source:
// http://asn1.elibel.tm.fr)
        PublicKey pkey1;
        PublicKey pkey2;
        try {
            pkey1 = new TestKeyPair("RSA").getPublic();
            pkey2 = new TestKeyPair("DSA").getPublic();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected Exception was thrown: " + e.getMessage());
            return;
        }
TestCert cert1 = new TestCert(pkey1);
TestCert cert2 = new TestCert(pkey2);

        try {
            selector.setSubjectPublicKeyAlgID(null);
        } catch (IOException e) {

            fail("Unexpected IOException was thrown.");
        }
assertTrue("Any certificate should match in the case of null "
                + "subjectPublicKeyAlgID criteria.", selector.match(cert1)
                && selector.match(cert2));

        String[] validOIDs = { "0.0.20", "1.25.0", "2.0.39", "0.2.10", "1.35.15",
                "2.17.89", "2.5.29.16", "2.5.29.17", "2.5.29.30", "2.5.29.32",
                "2.5.29.37" };

for (int i = 0; i < validOIDs.length; i++) {
            try {
                selector.setSubjectPublicKeyAlgID(validOIDs[i]);
                assertEquals(validOIDs[i], selector.getSubjectPublicKeyAlgID());
            } catch (IOException e) {
                fail("Unexpected exception " + e.getMessage());
            }
}

        String[] invalidOIDs = { "0.20", "1.25", "2.39", "3.10"};
for (int i = 0; i < invalidOIDs.length; i++) {
try {
selector.setSubjectPublicKeyAlgID(invalidOIDs[i]);
fail("IOException wasn't thrown for " + invalidOIDs[i]);
            } catch (IOException e) {
}
}

        try {
            selector.setSubjectPublicKeyAlgID(pkaid1);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown.");
        }
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert1));
assertFalse("The certificate should not match the selection criteria.",
                selector.match(cert2));
        try {
            selector.setSubjectPublicKeyAlgID(pkaid2);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown.");
        }
assertTrue("The certificate should match the selection criteria.",
                selector.match(cert2));
}

/**
//Synthetic comment -- @@ -1792,13 +1662,11 @@
}

public void setExtendedKeyUsage(Set<String> extKeyUsage) {
            this.extKeyUsage = (extKeyUsage == null) ? null : new ArrayList<String>(
                    extKeyUsage);
}

public void setKeyUsage(boolean[] keyUsage) {
            this.keyUsage = (keyUsage == null) ? null : (boolean[]) keyUsage
                    .clone();
}

public void setPublicKey(PublicKey key) {
//Synthetic comment -- @@ -2009,8 +1877,7 @@
ASN1Integer.getInstance() })
.encode(new Object[] {
new Boolean(pathLen != 1),
                                        BigInteger.valueOf(pathLen)
                                                .toByteArray() }));
}
if ("2.5.29.17".equals(oid) && (sans != null)) {
if (sans.getNames() == null) {
//Synthetic comment -- @@ -2074,8 +1941,9 @@
BigInteger revokedSerialNumber = BigInteger.valueOf(1);
crl = new MyCRL("X.509");
//        X509CRL rootCRL = X509CRL;
//        X509CRL interCRL = X509CRLExample.createCRL(interCert, interPair
//                .getPrivate(), revokedSerialNumber);

// create CertStore to support path building
List<Object> list = new ArrayList<Object>();
//Synthetic comment -- @@ -2083,16 +1951,14 @@
list.add(rootCertificate);
list.add(endCertificate);

//        CollectionCertStoreParameters params = new CollectionCertStoreParameters(
//                list);
//        CertStore store = CertStore.getInstance("Collection", params);
//
theCertSelector = new X509CertSelector();
theCertSelector.setCertificate(endCertificate);
        theCertSelector.setIssuer(endCertificate.getIssuerX500Principal()
                .getEncoded());

     // build the path
builder = CertPathBuilder.getInstance("PKIX");

}
//Synthetic comment -- @@ -2103,8 +1969,7 @@
Collections.singleton(new TrustAnchor(rootCertificate, null)),
theCertSelector);
try {
        result = (PKIXCertPathBuilderResult) builder
        .build(buildParams);
} catch(CertPathBuilderException e) {
return null;
}








//Synthetic comment -- diff --git a/luni/src/test/java/tests/security/cert/X509Certificate2Test.java b/luni/src/test/java/tests/security/cert/X509Certificate2Test.java
//Synthetic comment -- index 5578234..58cc100 100644

//Synthetic comment -- @@ -57,9 +57,6 @@
// End regression for HARMONY-3384
}

    /**
     * java.security.cert.X509Certificate#X509Certificate()
     */
public void test_X509Certificate() {
MyX509Certificate s = null;
try {
//Synthetic comment -- @@ -99,7 +96,7 @@
// (see RFC 3280 at http://www.ietf.org/rfc/rfc3280.txt)
// (generated by using of classes from
// org.apache.harmony.security.x509 package)
    static String base64cert =
"MIIByzCCATagAwIBAgICAiswCwYJKoZIhvcNAQEFMB0xGzAZBgNVBAoT"
+ "EkNlcnRpZmljYXRlIElzc3VlcjAeFw0wNjA0MjYwNjI4MjJaFw0zMzAz"
+ "MDExNjQ0MDlaMB0xGzAZBgNVBAoTEkNlcnRpZmljYXRlIElzc3VlcjCB"
//Synthetic comment -- @@ -112,7 +109,7 @@
+ "XEa7ONzcHQTYTG10poHfOK/a0BaULF3GlctDESilwQYbW5BdfpAlZpbH"
+ "AFLcUDh6Eq50kc0A/anh/j3mgBNuvbIMo7hHNnZB6k/prswm2BszyLD"
+ "yw==";
    static String base64certCorrect =
"-----BEGIN CERTIFICATE-----\n"
+ "MIIC+jCCAragAwIBAgICAiswDAYHKoZIzjgEAwEBADAdMRswGQYDVQQKExJDZXJ0a"
+ "WZpY2F0ZSBJc3N1ZXIwIhgPMTk3MDAxMTIxMzQ2NDBaGA8xOTcwMDEyNDAzMzMyMF"
//Synthetic comment -- @@ -132,9 +129,7 @@
+ "7jrj84/GZlhm09DsCFQCBKGKCGbrP64VtUt4JPmLjW1VxQA==\n"
+ "-----END CERTIFICATE-----";

    private X509Certificate cert;

    static String base64certTampered = "-----BEGIN CERTIFICATE-----\n"
+ "MIIC+jCCAragAwIBAgICAiswDAYHKoZIzjgEAwEBADAdMRswGQYDVQQKExJDZXJ0a"
+ "WZpY2F0ZSBJc3N1ZXIwIhgPMTk3MDAxMTIxMzQ2NDBaGA8xOTcwMDEyNDAzMzMyMF"
+ "owHzEdMBsGA1UEChMUU3ViamVjdCBPcmdhbml6YXRpb24wGTAMBgcqhkjOOAQDAQE"
//Synthetic comment -- @@ -157,7 +152,7 @@
// (see RFC 3280 at http://www.ietf.org/rfc/rfc3280.txt)
// (generated by using of classes from
// org.apache.harmony.security.x509 package)
    static String base64crl =
"MIHXMIGXAgEBMAkGByqGSM44BAMwFTETMBEGA1UEChMKQ1JMIElzc3Vl"
+ "chcNMDYwNDI3MDYxMzQ1WhcNMDYwNDI3MDYxNTI1WjBBMD8CAgIrFw0w"
+ "NjA0MjcwNjEzNDZaMCowCgYDVR0VBAMKAQEwHAYDVR0YBBUYEzIwMDYw"
//Synthetic comment -- @@ -272,16 +267,10 @@
}
}

    /**
     * java.security.cert.X509Certificate#getType()
     */
public void testGetType() {
assertEquals("X.509", new MyX509Certificate().getType());
}

    /**
     * java.security.cert.X509Certificate#getIssuerX500Principal()
     */
public void testGetIssuerX500Principal() {
// return valid encoding
MyX509Certificate cert = new MyX509Certificate() {
//Synthetic comment -- @@ -295,9 +284,6 @@
assertEquals(new X500Principal("CN=Z"), cert.getIssuerX500Principal());
}

    /**
     * java.security.cert.X509Certificate#getSubjectX500Principal()
     */
public void testGetSubjectX500Principal() {
// return valid encoding
MyX509Certificate cert = new MyX509Certificate() {
//Synthetic comment -- @@ -311,123 +297,227 @@
assertEquals(new X500Principal("CN=Y"), cert.getSubjectX500Principal());
}

    /**
     * @throws CertificateException
     * java.security.cert.X509Certificate#getExtendedKeyUsage()
     */
    public void testGetExtendedKeyUsage() throws CertificateException {
assertNull(new MyX509Certificate().getExtendedKeyUsage());

List<String> l = cert.getExtendedKeyUsage();
assertNotNull(l);

        try {
            l.clear();
        } catch (Exception e) {
            // ok
        }

        try {
            l.add("Test");
        } catch (Exception e) {
            // ok
        }

        try {
            if (l.size() > 0) {
                l.remove(0);
            }
        } catch (Exception e) {
            // ok
        }

}

    /**
     * java.security.cert.X509Certificate#getSubjectAlternativeNames()
     */
    public void testGetSubjectAlternativeNames()
            throws CertificateParsingException {

assertNull(new MyX509Certificate().getSubjectAlternativeNames());

Collection<List<?>> coll = cert.getSubjectAlternativeNames();
//getSubjectAlternativeNames method is not supported
assertNotNull(coll);

        try {
            coll.clear();
        } catch (Exception e) {
            // ok
        }

        try {
            if (coll.size() > 0) {
                coll.remove(0);
            }
        } catch (Exception e) {
            // ok
        }

        assertTrue(coll.size() < 10);

}

    /**
     * java.security.cert.X509Certificate#getIssuerAlternativeNames()
     */
    public void testGetIssuerAlternativeNames()
            throws CertificateParsingException {

assertNull(new MyX509Certificate().getIssuerAlternativeNames());

Collection<List<?>> coll = cert.getIssuerAlternativeNames();
// getIssuerAlternativeNames returns null.
assertNotNull(coll);

        try {
            coll.clear();
        } catch (Exception e) {
            // ok
        }

        try {
            if (coll.size() > 0) {
                coll.remove(0);
            }
        } catch (Exception e) {
            // ok
        }

        assertTrue(coll.size() < 10);
}

    public void testCerficateException() {
try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream bais = new ByteArrayInputStream(
                    base64certTampered.getBytes());
            cert = (X509Certificate) cf.generateCertificate(bais);
        } catch (CertificateException e) {
            // ok
}

try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream bais = new ByteArrayInputStream(base64cert
                    .getBytes());
            cert = (X509Certificate) cf.generateCertificate(bais);
        } catch (CertificateException e) {
            // ok
}
}

    public void setUp() throws Exception {
        super.setUp();
CertificateFactory cf = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream bais = new ByteArrayInputStream(base64certCorrect
                .getBytes());
        cert = (X509Certificate) cf.generateCertificate(bais);
assertNotNull(cert);
}
}







