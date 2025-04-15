/*Fix X509Certificate.getBasicConstraints implementation for DRLCertFactory

Change-Id:I5d1ba078b0f8f9ec8e2950e0da02481e8162a5e6*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/security/cert/X509Certificate.java b/luni/src/main/java/java/security/cert/X509Certificate.java
//Synthetic comment -- index 90737d7..ac4be18 100644

//Synthetic comment -- @@ -364,9 +364,11 @@
* Returns the path length of the certificate constraints from the {@code
* BasicContraints} extension.
*
     * If the certificate has no basic constraints or is not a
     * certificate authority, {@code -1} is returned. If the
     * certificate is a certificate authority without a path length,
     * {@code Integer.MAX_VALUE} is returned. Otherwise, the
     * certificate authority's path length is returned.
*/
public abstract int getBasicConstraints();









//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/provider/cert/X509CertImpl.java b/luni/src/main/java/org/apache/harmony/security/provider/cert/X509CertImpl.java
//Synthetic comment -- index b15e8ac..68bcec6 100644

//Synthetic comment -- @@ -305,9 +305,9 @@

public int getBasicConstraints() {
if (extensions == null) {
            return -1;
}
        return extensions.valueOfBasicConstraints();
}

public Collection<List<?>> getSubjectAlternativeNames() throws CertificateParsingException {








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/x509/BasicConstraints.java b/luni/src/main/java/org/apache/harmony/security/x509/BasicConstraints.java
//Synthetic comment -- index 6a473f5..fbdaec4 100644

//Synthetic comment -- @@ -58,6 +58,10 @@
}
}

    public boolean getCa() {
        return ca;
    }

public int getPathLenConstraint() {
return pathLenConstraint;
}








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/x509/Extensions.java b/luni/src/main/java/org/apache/harmony/security/x509/Extensions.java
//Synthetic comment -- index 3336b0d..2c3704d 100644

//Synthetic comment -- @@ -225,11 +225,14 @@
* @return the value of pathLenConstraint field if extension presents,
* and Integer.MAX_VALUE if does not.
*/
    public int valueOfBasicConstraints() {
Extension extension = getExtensionByOID("2.5.29.19");
        if (extension == null) {
            return -1;
        }
        BasicConstraints bc = extension.getBasicConstraintsValue();
        if (bc == null || !bc.getCa()) {
            return -1;
}
return bc.getPathLenConstraint();
}








//Synthetic comment -- diff --git a/luni/src/test/java/tests/security/cert/X509CertSelectorTest.java b/luni/src/test/java/tests/security/cert/X509CertSelectorTest.java
//Synthetic comment -- index 1fc6426..9b94444 100644

//Synthetic comment -- @@ -17,8 +17,6 @@

package tests.security.cert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
//Synthetic comment -- @@ -53,19 +51,17 @@
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import junit.framework.TestCase;
import org.apache.harmony.security.asn1.ASN1Boolean;
import org.apache.harmony.security.asn1.ASN1Integer;
import org.apache.harmony.security.asn1.ASN1OctetString;
import org.apache.harmony.security.asn1.ASN1Oid;
import org.apache.harmony.security.asn1.ASN1Sequence;
import org.apache.harmony.security.asn1.ASN1Type;
import org.apache.harmony.security.tests.support.TestKeyPair;
import org.apache.harmony.security.tests.support.cert.MyCRL;
import org.apache.harmony.security.tests.support.cert.TestUtils;
import org.apache.harmony.security.x501.Name;
import org.apache.harmony.security.x509.CertificatePolicies;
import org.apache.harmony.security.x509.GeneralName;
//Synthetic comment -- @@ -141,7 +137,7 @@
new X509CertSelector().addSubjectAlternativeName(types[i],
(byte[]) null);
fail("No expected NullPointerException for type: " + types[i]);
            } catch (NullPointerException expected) {
}
}
}
//Synthetic comment -- @@ -165,7 +161,7 @@
new X509CertSelector().addSubjectAlternativeName(types[i],
"-0xDFRF");
fail("IOException expected for type: " + types[i]);
            } catch (IOException expected) {
}
}
}
//Synthetic comment -- @@ -188,7 +184,7 @@
try {
new X509CertSelector().addPathToName(types[i], (byte[]) null);
fail("No expected NullPointerException for type: " + types[i]);
            } catch (NullPointerException expected) {
}
}
}
//Synthetic comment -- @@ -201,9 +197,8 @@
for (int type = 0; type <= 8; type++) {
try {
new X509CertSelector().addPathToName(type, (String) null);
                fail();
            } catch (IOException expected) {
}
}

//Synthetic comment -- @@ -214,12 +209,7 @@
* java.security.cert.X509CertSelector#X509CertSelector()
*/
public void test_X509CertSelector() {
        X509CertSelector selector = new X509CertSelector();
assertEquals(-1, selector.getBasicConstraints());
assertTrue(selector.getMatchAllSubjectAltNames());
}
//Synthetic comment -- @@ -231,49 +221,34 @@
X509CertSelector selector = new X509CertSelector();
X509CertSelector selector1 = (X509CertSelector) selector.clone();

        assertEquals(selector.getMatchAllSubjectAltNames(), selector1.getMatchAllSubjectAltNames());
        assertEquals(selector.getAuthorityKeyIdentifier(), selector1.getAuthorityKeyIdentifier());
        assertEquals(selector.getBasicConstraints(), selector1.getBasicConstraints());
assertEquals(selector.getCertificate(), selector1.getCertificate());
        assertEquals(selector.getCertificateValid(), selector1.getCertificateValid());
        assertEquals(selector.getExtendedKeyUsage(), selector1.getExtendedKeyUsage());
assertEquals(selector.getIssuer(), selector1.getIssuer());
assertEquals(selector.getIssuerAsBytes(), selector1.getIssuerAsBytes());
        assertEquals(selector.getIssuerAsString(), selector1.getIssuerAsString());
assertEquals(selector.getKeyUsage(), selector1.getKeyUsage());
        assertEquals(selector.getNameConstraints(), selector1.getNameConstraints());
assertEquals(selector.getPathToNames(), selector1.getPathToNames());
assertEquals(selector.getPolicy(), selector1.getPolicy());
        assertEquals(selector.getPrivateKeyValid(), selector1.getPrivateKeyValid());
assertEquals(selector.getSerialNumber(), selector1.getSerialNumber());
assertEquals(selector.getSubject(), selector1.getSubject());
        assertEquals(selector.getSubjectAlternativeNames(), selector1.getSubjectAlternativeNames());
        assertEquals(selector.getSubjectAsBytes(), selector1.getSubjectAsBytes());
        assertEquals(selector.getSubjectAsString(), selector1.getSubjectAsString());
        assertEquals(selector.getSubjectKeyIdentifier(), selector1.getSubjectKeyIdentifier());
        assertEquals(selector.getSubjectPublicKey(), selector1.getSubjectPublicKey());
        assertEquals(selector.getSubjectPublicKeyAlgID(), selector1.getSubjectPublicKeyAlgID());

selector = null;
try {
selector.clone();
            fail();
        } catch (NullPointerException expected) {
}
}

//Synthetic comment -- @@ -285,15 +260,15 @@
byte[] akid2 = new byte[] { 4, 5, 5, 4, 3, 2, 1 }; // random value
X509CertSelector selector = new X509CertSelector();

        assertNull("Selector should return null",
                   selector.getAuthorityKeyIdentifier());
selector.setAuthorityKeyIdentifier(akid1);
        assertTrue("The returned keyID should be equal to specified",
                   Arrays.equals(akid1, selector.getAuthorityKeyIdentifier()));
        assertTrue("The returned keyID should be equal to specified",
                   Arrays.equals(akid1, selector.getAuthorityKeyIdentifier()));
        assertFalse("The returned keyID should differ",
                    Arrays.equals(akid2,selector.getAuthorityKeyIdentifier()));
}

/**
//Synthetic comment -- @@ -311,16 +286,16 @@
/**
* java.security.cert.X509CertSelector#getCertificate()
*/
    public void test_getCertificate() throws Exception {
X509CertSelector selector = new X509CertSelector();
CertificateFactory certFact = CertificateFactory.getInstance("X509");
        X509Certificate cert1 = (X509Certificate)
                certFact.generateCertificate(new ByteArrayInputStream(
                        TestUtils.getX509Certificate_v3()));

        X509Certificate cert2 = (X509Certificate)
                certFact.generateCertificate(new ByteArrayInputStream(
                        TestUtils.getX509Certificate_v1()));

selector.setCertificate(cert1);
assertEquals(cert1, selector.getCertificate());
//Synthetic comment -- @@ -341,19 +316,19 @@
Date date3 = Calendar.getInstance().getTime();
X509CertSelector selector = new X509CertSelector();

        assertNull("Selector should return null",
                   selector.getCertificateValid());
selector.setCertificateValid(date1);
        assertTrue("The returned date should be equal to specified",
                   date1.equals(selector.getCertificateValid()));
selector.getCertificateValid().setTime(200);
        assertTrue("The returned date should be equal to specified",
                   date1.equals(selector.getCertificateValid()));
        assertFalse("The returned date should differ",
                    date2.equals(selector.getCertificateValid()));
selector.setCertificateValid(date3);
        assertTrue("The returned date should be equal to specified",
                   date3.equals(selector.getCertificateValid()));
selector.setCertificateValid(null);
assertNull(selector.getCertificateValid());
}
//Synthetic comment -- @@ -361,30 +336,27 @@
/**
* java.security.cert.X509CertSelector#getExtendedKeyUsage()
*/
    public void test_getExtendedKeyUsage() throws Exception {
        HashSet<String> ku = new HashSet<String>(
                Arrays.asList(new String[] { "1.3.6.1.5.5.7.3.1",
                                             "1.3.6.1.5.5.7.3.2",
                                             "1.3.6.1.5.5.7.3.3",
                                             "1.3.6.1.5.5.7.3.4",
                                             "1.3.6.1.5.5.7.3.8",
                                             "1.3.6.1.5.5.7.3.9",
                                             "1.3.6.1.5.5.7.3.5",
                                             "1.3.6.1.5.5.7.3.6",
                                             "1.3.6.1.5.5.7.3.7" }));
X509CertSelector selector = new X509CertSelector();

        assertNull("Selector should return null", selector.getExtendedKeyUsage());
        selector.setExtendedKeyUsage(ku);
        assertTrue("The returned extendedKeyUsage should be equal to specified",
                   ku.equals(selector.getExtendedKeyUsage()));
try {
selector.getExtendedKeyUsage().add("KRIBLEGRABLI");
fail("The returned Set should be immutable.");
        } catch (UnsupportedOperationException expected) {
}
}

//Synthetic comment -- @@ -398,16 +370,16 @@

assertNull("Selector should return null", selector.getIssuer());
selector.setIssuer(iss1);
        assertEquals("The returned issuer should be equal to specified",
                     iss1, selector.getIssuer());
        assertFalse("The returned issuer should differ",
                    iss2.equals(selector.getIssuer()));
}

/**
* java.security.cert.X509CertSelector#getIssuerAsBytes()
*/
    public void test_getIssuerAsBytes() throws Exception {
byte[] name1 = new byte[]
// manually obtained DER encoding of "O=First Org." issuer name;
{ 48, 21, 49, 19, 48, 17, 6, 3, 85, 4, 10, 19, 10, 70, 105, 114, 115,
//Synthetic comment -- @@ -421,20 +393,14 @@
X500Principal iss2 = new X500Principal(name2);
X509CertSelector selector = new X509CertSelector();

        assertNull("Selector should return null", selector.getIssuerAsBytes());
        selector.setIssuer(iss1);
        assertTrue("The returned issuer should be equal to specified",
                   Arrays.equals(name1, selector.getIssuerAsBytes()));
        assertFalse("The returned issuer should differ", name2.equals(selector.getIssuerAsBytes()));
        selector.setIssuer(iss2);
        assertTrue("The returned issuer should be equal to specified",
                   Arrays.equals(name2, selector.getIssuerAsBytes()));
}

/**
//Synthetic comment -- @@ -450,12 +416,12 @@
assertNull("Selector should return null", selector.getIssuerAsString());
selector.setIssuer(iss1);
assertEquals("The returned issuer should be equal to specified", name1,
                     selector.getIssuerAsString());
        assertFalse("The returned issuer should differ",
                    name2.equals(selector.getIssuerAsString()));
selector.setIssuer(iss2);
assertEquals("The returned issuer should be equal to specified", name2,
                     selector.getIssuerAsString());
}

/**
//Synthetic comment -- @@ -468,12 +434,12 @@

assertNull("Selector should return null", selector.getKeyUsage());
selector.setKeyUsage(ku);
        assertTrue("The returned date should be equal to specified",
                   Arrays.equals(ku, selector.getKeyUsage()));
boolean[] result = selector.getKeyUsage();
result[0] = !result[0];
        assertTrue("The returned keyUsage should be equal to specified",
                   Arrays.equals(ku, selector.getKeyUsage()));
}

/**
//Synthetic comment -- @@ -481,11 +447,11 @@
*/
public void test_getMatchAllSubjectAltNames() {
X509CertSelector selector = new X509CertSelector();
        assertTrue("The matchAllNames initially should be true",
                   selector.getMatchAllSubjectAltNames());
selector.setMatchAllSubjectAltNames(false);
        assertFalse("The value should be false",
                    selector.getMatchAllSubjectAltNames());
}

/**
//Synthetic comment -- @@ -523,57 +489,51 @@

for (int i = 0; i < constraintBytes.length; i++) {
selector.setNameConstraints(constraintBytes[i]);
            assertTrue(Arrays.equals(constraintBytes[i],
                                     selector.getNameConstraints()));
}
}

/**
* java.security.cert.X509CertSelector#getPathToNames()
*/
    public void test_getPathToNames() throws Exception {
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
                   + "subjectAlternativeNames criteria.",
                   selector.match(cert1) && selector.match(cert2));

        Collection<List<?>> sans = sans1.getPairsList();

        selector.setPathToNames(sans);
        selector.getPathToNames();
}

/**
//Synthetic comment -- @@ -612,13 +572,13 @@

assertNull("Selector should return null", selector.getPrivateKeyValid());
selector.setPrivateKeyValid(date1);
        assertTrue("The returned date should be equal to specified",
                   date1.equals(selector.getPrivateKeyValid()));
selector.getPrivateKeyValid().setTime(200);
        assertTrue("The returned date should be equal to specified",
                   date1.equals(selector.getPrivateKeyValid()));
        assertFalse("The returned date should differ",
                    date2.equals(selector.getPrivateKeyValid()));
}

/**
//Synthetic comment -- @@ -632,9 +592,9 @@
assertNull("Selector should return null", selector.getSerialNumber());
selector.setSerialNumber(ser1);
assertEquals("The returned serial number should be equal to specified",
                     ser1, selector.getSerialNumber());
        assertFalse("The returned serial number should differ",
                    ser2.equals(selector.getSerialNumber()));
}

/**
//Synthetic comment -- @@ -648,73 +608,65 @@
assertNull("Selector should return null", selector.getSubject());
selector.setSubject(sub1);
assertEquals("The returned subject should be equal to specified", sub1,
                     selector.getSubject());
        assertFalse("The returned subject should differ",
                    sub2.equals(selector.getSubject()));
}

/**
* java.security.cert.X509CertSelector#getSubjectAlternativeNames()
*/
    public void test_getSubjectAlternativeNames() throws Exception {
        GeneralName san1 = new GeneralName(1, "rfc@822.Name");
        GeneralName san2 = new GeneralName(2, "dNSName");

        GeneralNames sans = new GeneralNames();
        sans.addName(san1);
        sans.addName(san2);

        TestCert cert_1 = new TestCert(sans);
        X509CertSelector selector = new X509CertSelector();

        assertNull("Selector should return null",
                   selector.getSubjectAlternativeNames());

        selector.setSubjectAlternativeNames(sans.getPairsList());
        assertTrue("The certificate should match the selection criteria.",
                   selector.match(cert_1));
        selector.getSubjectAlternativeNames().clear();
        assertTrue("The modification of initialization object "
                   + "should not affect the modification "
                   + "of internal object.",
                   selector.match(cert_1));
}

/**
* java.security.cert.X509CertSelector#getSubjectAsBytes()
*/
    public void test_getSubjectAsBytes() throws Exception {
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

        assertNull("Selector should return null",
                   selector.getSubjectAsBytes());
        selector.setSubject(sub1);
        assertTrue("The returned issuer should be equal to specified",
                   Arrays.equals(name1, selector.getSubjectAsBytes()));
        assertFalse("The returned issuer should differ",
                    name2.equals(selector.getSubjectAsBytes()));
        selector.setSubject(sub2);
        assertTrue("The returned issuer should be equal to specified",
                   Arrays.equals(name2, selector.getSubjectAsBytes()));
}

/**
//Synthetic comment -- @@ -730,12 +682,12 @@
assertNull("Selector should return null", selector.getSubjectAsString());
selector.setSubject(sub1);
assertEquals("The returned subject should be equal to specified",
                     name1, selector.getSubjectAsString());
        assertFalse("The returned subject should differ",
                    name2.equals(selector.getSubjectAsString()));
selector.setSubject(sub2);
assertEquals("The returned subject should be equal to specified",
                     name2, selector.getSubjectAsString());
}

/**
//Synthetic comment -- @@ -746,16 +698,15 @@
byte[] skid2 = new byte[] { 4, 5, 5, 4, 3, 2, 1 }; // random value
X509CertSelector selector = new X509CertSelector();

        assertNull("Selector should return null", selector.getSubjectKeyIdentifier());
selector.setSubjectKeyIdentifier(skid1);
        assertTrue("The returned keyID should be equal to specified",
                   Arrays.equals(skid1, selector.getSubjectKeyIdentifier()));
selector.getSubjectKeyIdentifier()[0]++;
        assertTrue("The returned keyID should be equal to specified",
                   Arrays.equals(skid1, selector.getSubjectKeyIdentifier()));
        assertFalse("The returned keyID should differ",
                    Arrays.equals(skid2, selector.getSubjectKeyIdentifier()));
}

/**
//Synthetic comment -- @@ -793,14 +744,13 @@
/**
* java.security.cert.X509CertSelector#getSubjectPublicKeyAlgID()
*/
    public void test_getSubjectPublicKeyAlgID() throws Exception {

X509CertSelector selector = new X509CertSelector();
String[] validOIDs = { "0.0.20", "1.25.0", "2.0.39", "0.2.10", "1.35.15",
"2.17.89" };

        assertNull("Selector should return null", selector.getSubjectPublicKeyAlgID());

for (int i = 0; i < validOIDs.length; i++) {
try {
//Synthetic comment -- @@ -815,33 +765,28 @@
String pkaid1 = "1.2.840.113549.1.1.1"; // RSA encryption
String pkaid2 = "1.2.840.113549.1.1.4"; // MD5 with RSA encryption

        selector.setSubjectPublicKeyAlgID(pkaid1);
        assertTrue("The returned oid should be equal to specified",
                   pkaid1.equals(selector.getSubjectPublicKeyAlgID()));
        assertFalse("The returned oid should differ",
                    pkaid2.equals(selector.getSubjectPublicKeyAlgID()));
}

/**
* java.security.cert.X509CertSelector#match(java.security.cert.Certificate)
*/
    public void test_matchLjava_security_cert_Certificate() throws Exception {
X509CertSelector selector = new X509CertSelector();
assertFalse(selector.match(null));

CertificateFactory certFact = CertificateFactory.getInstance("X509");
        X509Certificate cert1 = (X509Certificate)
                certFact.generateCertificate(new ByteArrayInputStream(
                        TestUtils.getX509Certificate_v3()));

        X509Certificate cert2 = (X509Certificate)
                certFact.generateCertificate(new ByteArrayInputStream(
                        TestUtils.getX509Certificate_v1()));

selector.setCertificate(cert1);
assertTrue(selector.match(cert1));
//Synthetic comment -- @@ -855,7 +800,7 @@
/**
* java.security.cert.X509CertSelector#setAuthorityKeyIdentifier(byte[])
*/
    public void test_setAuthorityKeyIdentifierLB$() throws Exception {
X509CertSelector selector = new X509CertSelector();

byte[] akid1 = new byte[] { 1, 2, 3, 4, 5 }; // random value
//Synthetic comment -- @@ -895,9 +840,7 @@
for (int i = 0; i < invalidValues.length; i++) {
try {
selector.setBasicConstraints(-3);
            } catch (IllegalArgumentException expected) {
}
}

//Synthetic comment -- @@ -912,7 +855,7 @@
* java.security.cert.X509CertSelector#setCertificate(java.security.cert.Certificate)
*/
public void test_setCertificateLjava_security_cert_X509Certificate()
            throws Exception {

TestCert cert1 = new TestCert("same certificate");
TestCert cert2 = new TestCert("other certificate");
//Synthetic comment -- @@ -920,16 +863,16 @@

selector.setCertificate(null);
assertTrue("Any certificates should match in the case of null "
                + "certificateEquals criteria.",
                   selector.match(cert1) && selector.match(cert2));
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
//Synthetic comment -- @@ -938,7 +881,7 @@
* java.security.cert.X509CertSelector#setCertificateValid(java.util.Date)
*/
public void test_setCertificateValidLjava_util_Date()
            throws Exception {
X509CertSelector selector = new X509CertSelector();

Date date1 = new Date(100);
//Synthetic comment -- @@ -962,10 +905,9 @@
/**
* java.security.cert.X509CertSelector#setExtendedKeyUsage(Set<String>)
*/
    public void test_setExtendedKeyUsageLjava_util_Set() throws Exception {
        HashSet<String> ku1 = new HashSet<String>(
                Arrays.asList(new String[] { "1.3.6.1.5.5.7.3.1",
"1.3.6.1.5.5.7.3.2", "1.3.6.1.5.5.7.3.3",
"1.3.6.1.5.5.7.3.4", "1.3.6.1.5.5.7.3.8",
"1.3.6.1.5.5.7.3.9", "1.3.6.1.5.5.7.3.5",
//Synthetic comment -- @@ -979,33 +921,21 @@

X509CertSelector selector = new X509CertSelector();

        selector.setExtendedKeyUsage(null);
assertTrue("Any certificate should match in the case of null "
                   + "extendedKeyUsage criteria.",
                   selector.match(cert1)&& selector.match(cert2));
        selector.setExtendedKeyUsage(ku1);
assertEquals(ku1, selector.getExtendedKeyUsage());

        selector.setExtendedKeyUsage(ku2);
assertEquals(ku2, selector.getExtendedKeyUsage());
}

/**
* java.security.cert.X509CertSelector#setIssuer(byte[])
*/
    public void test_setIssuerLB$() throws Exception {
byte[] name1 = new byte[]
// manually obtained DER encoding of "O=First Org." issuer name;
{ 48, 21, 49, 19, 48, 17, 6, 3, 85, 4, 10, 19, 10, 70, 105, 114, 115,
//Synthetic comment -- @@ -1021,36 +951,24 @@

X509CertSelector selector = new X509CertSelector();

        selector.setIssuer((byte[]) null);
assertTrue("Any certificates should match "
                   + "in the case of null issuer criteria.", selector.match(cert1)
                   && selector.match(cert2));
        selector.setIssuer(name1);
assertTrue("The certificate should match the selection criteria.",
                   selector.match(cert1));
assertFalse("The certificate should not match the selection criteria.",
                    selector.match(cert2));
        selector.setIssuer(name2);
assertTrue("The certificate should match the selection criteria.",
                   selector.match(cert2));
}

/**
* java.security.cert.X509CertSelector#setIssuer(java.lang.String)
*/
    public void test_setIssuerLjava_lang_String() throws Exception {

String name1 = "O=First Org.";
String name2 = "O=Second Org.";
//Synthetic comment -- @@ -1061,37 +979,25 @@

X509CertSelector selector = new X509CertSelector();

        selector.setIssuer((String) null);
assertTrue("Any certificates should match "
                   + "in the case of null issuer criteria.",
                   selector.match(cert1) && selector.match(cert2));
        selector.setIssuer(name1);
assertTrue("The certificate should match the selection criteria.",
                   selector.match(cert1));
assertFalse("The certificate should not match the selection criteria.",
                    selector.match(cert2));
        selector.setIssuer(name2);
assertTrue("The certificate should match the selection criteria.",
                   selector.match(cert2));
}

/**
* java.security.cert.X509CertSelector#setIssuer(javax.security.auth.x500.X500Principal)
*/
public void test_setIssuerLjavax_security_auth_x500_X500Principal()
            throws Exception {
X500Principal iss1 = new X500Principal("O=First Org.");
X500Principal iss2 = new X500Principal("O=Second Org.");
TestCert cert1 = new TestCert(iss1);
//Synthetic comment -- @@ -1100,22 +1006,22 @@

selector.setIssuer((X500Principal) null);
assertTrue("Any certificates should match "
                   + "in the case of null issuer criteria.",
                   selector.match(cert1) && selector.match(cert2));
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
    public void test_setKeyUsageZ() throws Exception {
boolean[] ku1 = new boolean[] { true, true, true, true, true, true,
true, true, true };
// decipherOnly is disallowed
//Synthetic comment -- @@ -1128,17 +1034,16 @@
X509CertSelector selector = new X509CertSelector();

selector.setKeyUsage(null);
        assertTrue("Any certificate should match in the case of null keyUsage criteria.",
                   selector.match(cert1) && selector.match(cert2));
selector.setKeyUsage(ku1);
assertTrue("The certificate should match the selection criteria.",
                   selector.match(cert1));
assertFalse("The certificate should not match the selection criteria.",
                    selector.match(cert2));
assertTrue("The certificate which does not have a keyUsage extension "
                   + "implicitly allows all keyUsage values.",
                   selector.match(cert3));
selector.setKeyUsage(ku2);
ku2[0] = !ku2[0];
assertTrue("The certificate should match the selection criteria.",
//Synthetic comment -- @@ -1186,57 +1091,50 @@

for (int i = 0; i < constraintBytes.length; i++) {
selector.setNameConstraints(constraintBytes[i]);
            assertTrue(Arrays.equals(constraintBytes[i], selector.getNameConstraints()));
}
}

/**
* java.security.cert.X509CertSelector#setPathToNames(Collection<List<?>>)
*/
    public void test_setPathToNamesLjava_util_Collection() throws Exception {
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
                   + "subjectAlternativeNames criteria.",
                   selector.match(cert1) && selector.match(cert2));

        Collection<List<?>> sans = sans1.getPairsList();

        selector.setPathToNames(sans);
        selector.getPathToNames();
}

/**
//Synthetic comment -- @@ -1260,27 +1158,27 @@

selector.setPolicy(null);
assertTrue("Any certificate should match in the case of null "
                + "privateKeyValid criteria.",
                   selector.match(cert1) && selector.match(cert2));

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
            throws Exception {
Date date1 = new Date(100000000);
Date date2 = new Date(200000000);
Date date3 = new Date(300000000);
//Synthetic comment -- @@ -1293,24 +1191,24 @@

selector.setPrivateKeyValid(null);
assertTrue("Any certificate should match in the case of null "
                + "privateKeyValid criteria.",
                   selector.match(cert1) && selector.match(cert2));
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
            throws Exception {
BigInteger ser1 = new BigInteger("10000");
BigInteger ser2 = new BigInteger("10001");
TestCert cert1 = new TestCert(ser1);
//Synthetic comment -- @@ -1319,22 +1217,22 @@

selector.setSerialNumber(null);
assertTrue("Any certificate should match in the case of null "
                   + "serialNumber criteria.",
                   selector.match(cert1) && selector.match(cert2));
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
    public void test_setSubjectLB$() throws Exception {
byte[] name1 = new byte[]
// manually obtained DER encoding of "O=First Org." issuer name;
{ 48, 21, 49, 19, 48, 17, 6, 3, 85, 4, 10, 19, 10, 70, 105, 114, 115,
//Synthetic comment -- @@ -1350,36 +1248,24 @@

X509CertSelector selector = new X509CertSelector();

        selector.setSubject((byte[]) null);
assertTrue("Any certificates should match "
                   + "in the case of null issuer criteria.",
                   selector.match(cert1) && selector.match(cert2));
        selector.setSubject(name1);
assertTrue("The certificate should match the selection criteria.",
                   selector.match(cert1));
assertFalse("The certificate should not match the selection criteria.",
                    selector.match(cert2));
        selector.setSubject(name2);
assertTrue("The certificate should match the selection criteria.",
                   selector.match(cert2));
}

/**
* java.security.cert.X509CertSelector#setSubject(java.lang.String)
*/
    public void test_setSubjectLjava_lang_String() throws Exception {
String name1 = "O=First Org.";
String name2 = "O=Second Org.";
X500Principal sub1 = new X500Principal(name1);
//Synthetic comment -- @@ -1388,38 +1274,25 @@
TestCert cert2 = new TestCert(sub2);
X509CertSelector selector = new X509CertSelector();

        selector.setSubject((String) null);
assertTrue("Any certificates should match "
                   + "in the case of null subject criteria.",
                   selector.match(cert1) && selector.match(cert2));
        selector.setSubject(name1);
assertTrue("The certificate should match the selection criteria.",
                   selector.match(cert1));
assertFalse("The certificate should not match the selection criteria.",
                    selector.match(cert2));
        selector.setSubject(name2);
assertTrue("The certificate should match the selection criteria.",
                   selector.match(cert2));
}

/**
* java.security.cert.X509CertSelector#setSubject(javax.security.auth.x500.X500Principal)
*/
public void test_setSubjectLjavax_security_auth_x500_X500Principal()
            throws Exception {
X500Principal sub1 = new X500Principal("O=First Org.");
X500Principal sub2 = new X500Principal("O=Second Org.");
TestCert cert1 = new TestCert(sub1);
//Synthetic comment -- @@ -1428,73 +1301,66 @@

selector.setSubject((X500Principal) null);
assertTrue("Any certificates should match "
                   + "in the case of null subjcet criteria.",
                   selector.match(cert1) && selector.match(cert2));
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
    public void test_setSubjectAlternativeNamesLjava_util_Collection() throws Exception {

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
                   + "subjectAlternativeNames criteria.",
                   selector.match(cert1) && selector.match(cert2));

        Collection<List<?>> sans = sans1.getPairsList();

        selector.setSubjectAlternativeNames(sans);

        selector.getSubjectAlternativeNames();
}

/**
* java.security.cert.X509CertSelector#setSubjectKeyIdentifier(byte[])
*/
    public void test_setSubjectKeyIdentifierLB$() throws Exception {
byte[] skid1 = new byte[] { 1, 2, 3, 4, 5 }; // random value
byte[] skid2 = new byte[] { 5, 4, 3, 2, 1 }; // random value
TestCert cert1 = new TestCert(skid1);
//Synthetic comment -- @@ -1503,17 +1369,17 @@

selector.setSubjectKeyIdentifier(null);
assertTrue("Any certificate should match in the case of null "
                + "serialNumber criteria.",
                   selector.match(cert1) && selector.match(cert2));
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
//Synthetic comment -- @@ -1545,15 +1411,9 @@
* java.security.cert.X509CertSelector#setSubjectPublicKey(java.security.PublicKey key)
*/
public void test_setSubjectPublicKeyLjava_security_PublicKey()
            throws Exception {
        PublicKey pkey1 = new TestKeyPair("RSA").getPublic();
        PublicKey pkey2 = new TestKeyPair("DSA").getPublic();

TestCert cert1 = new TestCert(pkey1);
TestCert cert2 = new TestCert(pkey2);
//Synthetic comment -- @@ -1561,63 +1421,46 @@

selector.setSubjectPublicKey((PublicKey) null);
assertTrue("Any certificate should match in the case of null "
                   + "subjectPublicKey criteria.",
                   selector.match(cert1) && selector.match(cert2));
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
    public void test_setSubjectPublicKeyAlgIDLjava_lang_String() throws Exception {

X509CertSelector selector = new X509CertSelector();
String pkaid1 = "1.2.840.113549.1.1.1"; // RSA (source:
// http://asn1.elibel.tm.fr)
String pkaid2 = "1.2.840.10040.4.1"; // DSA (source:
// http://asn1.elibel.tm.fr)
        PublicKey pkey1 = new TestKeyPair("RSA").getPublic();;
        PublicKey pkey2 = new TestKeyPair("DSA").getPublic();;

TestCert cert1 = new TestCert(pkey1);
TestCert cert2 = new TestCert(pkey2);

        selector.setSubjectPublicKeyAlgID(null);
assertTrue("Any certificate should match in the case of null "
                   + "subjectPublicKeyAlgID criteria.",
                   selector.match(cert1) && selector.match(cert2));

String[] validOIDs = { "0.0.20", "1.25.0", "2.0.39", "0.2.10", "1.35.15",
"2.17.89", "2.5.29.16", "2.5.29.17", "2.5.29.30", "2.5.29.32",
"2.5.29.37" };

for (int i = 0; i < validOIDs.length; i++) {
            selector.setSubjectPublicKeyAlgID(validOIDs[i]);
            assertEquals(validOIDs[i], selector.getSubjectPublicKeyAlgID());
}

String[] invalidOIDs = { "0.20", "1.25", "2.39", "3.10"};
//Synthetic comment -- @@ -1625,26 +1468,18 @@
try {
selector.setSubjectPublicKeyAlgID(invalidOIDs[i]);
fail("IOException wasn't thrown for " + invalidOIDs[i]);
            } catch (IOException expected) {
}
}

        selector.setSubjectPublicKeyAlgID(pkaid1);
assertTrue("The certificate should match the selection criteria.",
                   selector.match(cert1));
assertFalse("The certificate should not match the selection criteria.",
                    selector.match(cert2));
        selector.setSubjectPublicKeyAlgID(pkaid2);
assertTrue("The certificate should match the selection criteria.",
                   selector.match(cert2));
}

/**
//Synthetic comment -- @@ -1792,13 +1627,11 @@
}

public void setExtendedKeyUsage(Set<String> extKeyUsage) {
            this.extKeyUsage = (extKeyUsage == null) ? null : new ArrayList<String>(extKeyUsage);
}

public void setKeyUsage(boolean[] keyUsage) {
            this.keyUsage = (keyUsage == null) ? null : (boolean[]) keyUsage.clone();
}

public void setPublicKey(PublicKey key) {
//Synthetic comment -- @@ -2009,8 +1842,7 @@
ASN1Integer.getInstance() })
.encode(new Object[] {
new Boolean(pathLen != 1),
                                        BigInteger.valueOf(pathLen).toByteArray() }));
}
if ("2.5.29.17".equals(oid) && (sans != null)) {
if (sans.getNames() == null) {
//Synthetic comment -- @@ -2074,8 +1906,9 @@
BigInteger revokedSerialNumber = BigInteger.valueOf(1);
crl = new MyCRL("X.509");
//        X509CRL rootCRL = X509CRL;
//        X509CRL interCRL = X509CRLExample.createCRL(interCert,
//                                                    interPair.getPrivate(),
//                                                    revokedSerialNumber);

// create CertStore to support path building
List<Object> list = new ArrayList<Object>();
//Synthetic comment -- @@ -2083,16 +1916,14 @@
list.add(rootCertificate);
list.add(endCertificate);

//        CollectionCertStoreParameters params = new CollectionCertStoreParameters(list);
//        CertStore store = CertStore.getInstance("Collection", params);
//
theCertSelector = new X509CertSelector();
theCertSelector.setCertificate(endCertificate);
        theCertSelector.setIssuer(endCertificate.getIssuerX500Principal().getEncoded());

        // build the path
builder = CertPathBuilder.getInstance("PKIX");

}
//Synthetic comment -- @@ -2103,8 +1934,7 @@
Collections.singleton(new TrustAnchor(rootCertificate, null)),
theCertSelector);
try {
        result = (PKIXCertPathBuilderResult) builder.build(buildParams);
} catch(CertPathBuilderException e) {
return null;
}








//Synthetic comment -- diff --git a/luni/src/test/java/tests/security/cert/X509Certificate2Test.java b/luni/src/test/java/tests/security/cert/X509Certificate2Test.java
//Synthetic comment -- index 5578234..58cc100 100644

//Synthetic comment -- @@ -57,9 +57,6 @@
// End regression for HARMONY-3384
}

public void test_X509Certificate() {
MyX509Certificate s = null;
try {
//Synthetic comment -- @@ -99,7 +96,7 @@
// (see RFC 3280 at http://www.ietf.org/rfc/rfc3280.txt)
// (generated by using of classes from
// org.apache.harmony.security.x509 package)
    private static String CERT =
"MIIByzCCATagAwIBAgICAiswCwYJKoZIhvcNAQEFMB0xGzAZBgNVBAoT"
+ "EkNlcnRpZmljYXRlIElzc3VlcjAeFw0wNjA0MjYwNjI4MjJaFw0zMzAz"
+ "MDExNjQ0MDlaMB0xGzAZBgNVBAoTEkNlcnRpZmljYXRlIElzc3VlcjCB"
//Synthetic comment -- @@ -112,7 +109,7 @@
+ "XEa7ONzcHQTYTG10poHfOK/a0BaULF3GlctDESilwQYbW5BdfpAlZpbH"
+ "AFLcUDh6Eq50kc0A/anh/j3mgBNuvbIMo7hHNnZB6k/prswm2BszyLD"
+ "yw==";
    private static String CERT_CORRECT =
"-----BEGIN CERTIFICATE-----\n"
+ "MIIC+jCCAragAwIBAgICAiswDAYHKoZIzjgEAwEBADAdMRswGQYDVQQKExJDZXJ0a"
+ "WZpY2F0ZSBJc3N1ZXIwIhgPMTk3MDAxMTIxMzQ2NDBaGA8xOTcwMDEyNDAzMzMyMF"
//Synthetic comment -- @@ -132,9 +129,7 @@
+ "7jrj84/GZlhm09DsCFQCBKGKCGbrP64VtUt4JPmLjW1VxQA==\n"
+ "-----END CERTIFICATE-----";

    private static String CERT_TAMPERED = "-----BEGIN CERTIFICATE-----\n"
+ "MIIC+jCCAragAwIBAgICAiswDAYHKoZIzjgEAwEBADAdMRswGQYDVQQKExJDZXJ0a"
+ "WZpY2F0ZSBJc3N1ZXIwIhgPMTk3MDAxMTIxMzQ2NDBaGA8xOTcwMDEyNDAzMzMyMF"
+ "owHzEdMBsGA1UEChMUU3ViamVjdCBPcmdhbml6YXRpb24wGTAMBgcqhkjOOAQDAQE"
//Synthetic comment -- @@ -157,7 +152,7 @@
// (see RFC 3280 at http://www.ietf.org/rfc/rfc3280.txt)
// (generated by using of classes from
// org.apache.harmony.security.x509 package)
    private static String CRL =
"MIHXMIGXAgEBMAkGByqGSM44BAMwFTETMBEGA1UEChMKQ1JMIElzc3Vl"
+ "chcNMDYwNDI3MDYxMzQ1WhcNMDYwNDI3MDYxNTI1WjBBMD8CAgIrFw0w"
+ "NjA0MjcwNjEzNDZaMCowCgYDVR0VBAMKAQEwHAYDVR0YBBUYEzIwMDYw"
//Synthetic comment -- @@ -272,16 +267,10 @@
}
}

public void testGetType() {
assertEquals("X.509", new MyX509Certificate().getType());
}

public void testGetIssuerX500Principal() {
// return valid encoding
MyX509Certificate cert = new MyX509Certificate() {
//Synthetic comment -- @@ -295,9 +284,6 @@
assertEquals(new X500Principal("CN=Z"), cert.getIssuerX500Principal());
}

public void testGetSubjectX500Principal() {
// return valid encoding
MyX509Certificate cert = new MyX509Certificate() {
//Synthetic comment -- @@ -311,123 +297,227 @@
assertEquals(new X500Principal("CN=Y"), cert.getSubjectX500Principal());
}

    public void testGetExtendedKeyUsage() throws Exception {
assertNull(new MyX509Certificate().getExtendedKeyUsage());
        X509Certificate cert = generateCert(CERT_CORRECT);
List<String> l = cert.getExtendedKeyUsage();
assertNotNull(l);
        l.clear();
        l.add("Test");
        assertEquals(1, l.size());
        l.remove(0);
}

    private static final String CERT_WITHOUT_BASIC
        = ("-----BEGIN CERTIFICATE-----\n"
           + "MIIG9TCCBd2gAwIBAgIPLXR4AWpp9+O6Jn4rZpkgMA0GCSqGSIb3DQEBBQUAME0x\n"
           + "CzAJBgNVBAYTAkNIMRUwEwYDVQQKEwxTd2lzc1NpZ24gQUcxJzAlBgNVBAMTHlN3\n"
           + "aXNzU2lnbiBFViBHb2xkIENBIDIwMDkgLSBHMjAeFw0xMjA3MjYwODU4MTNaFw0x\n"
           + "NDA3MjYwODU4MTNaMIIBITELMAkGA1UEBhMCQ0gxEDAOBgNVBAgMB1rDvHJpY2gx\n"
           + "EzARBgNVBAcTCkdsYXR0YnJ1Z2cxFTATBgNVBAoTDFN3aXNzU2lnbiBBRzEWMBQG\n"
           + "A1UEAxMNc3dpc3NzaWduLmNvbTEnMCUGCSqGSIb3DQEJARYYb3BlcmF0aW9uc0Bz\n"
           + "d2lzc3NpZ24uY29tMRswGQYDVQQJDBJTw6RnZXJlaXN0cmFzc2UgMjUxDTALBgNV\n"
           + "BBETBDgxNTIxEzARBgsrBgEEAYI3PAIBAxMCQ0gxGDAWBgsrBgEEAYI3PAIBAgwH\n"
           + "WsO8cmljaDEbMBkGA1UEBRMSQ0gtMDIwLjMuMDI1LjExMC03MRswGQYDVQQPExJW\n"
           + "MS4wLCBDbGF1c2UgNS4oYikwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIB\n"
           + "AQDLjzHfEcDeIwdEatC73JRs/xRaDLDmzwwHSZCjCvIKe8/yXxLR3cUIBG8mKrql\n"
           + "1yICAMEpNM7J/fwN248OV6X/UosJpC4vmbpzgAN8y2q1DGnOyX7Eyi3UDXLTXtfA\n"
           + "4294BMqCym5zzdS932aQPYBayFkzcsQSp6DHRAuj2Xxd9bly/urNKTumO8ZE0RFR\n"
           + "wVgNU7o3OQepsH3bhe060Jlr6EBLFas0scH6ll8fREI8g+xhs8yHBOL/meE3zVQC\n"
           + "/3KTyhY82R4xJy38YHCFPrwrtz5ZHpJqQ1LjiG+cX+FReoHp5VoV7LBNj+eL8oZb\n"
           + "G6Zn5xlsBQgTlOxEIbXLVV13AgMBAAGjggL6MIIC9jBLBgNVHREERDBCgg1zd2lz\n"
           + "c3NpZ24uY29tghF3d3cuc3dpc3NzaWduLmNvbYIMc3dpc3NzaWduLmNoghB3d3cu\n"
           + "c3dpc3NzaWduLmNoMA4GA1UdDwEB/wQEAwIFoDAdBgNVHSUEFjAUBggrBgEFBQcD\n"
           + "AQYIKwYBBQUHAwIwHQYDVR0OBBYEFPDLqEiSWR5JiwPlqngv2n2WJwVRMB8GA1Ud\n"
           + "IwQYMBaAFIh0Rm3HfLX6cnEZ3r8nXg1o4PcnMIH/BgNVHR8EgfcwgfQwR6BFoEOG\n"
           + "QWh0dHA6Ly9jcmwuc3dpc3NzaWduLm5ldC84ODc0NDY2REM3N0NCNUZBNzI3MTE5\n"
           + "REVCRjI3NUUwRDY4RTBGNzI3MIGooIGloIGihoGfbGRhcDovL2RpcmVjdG9yeS5z\n"
           + "d2lzc3NpZ24ubmV0L0NOPTg4NzQ0NjZEQzc3Q0I1RkE3MjcxMTlERUJGMjc1RTBE\n"
           + "NjhFMEY3MjclMkNPPVN3aXNzU2lnbiUyQ0M9Q0g/Y2VydGlmaWNhdGVSZXZvY2F0\n"
           + "aW9uTGlzdD9iYXNlP29iamVjdENsYXNzPWNSTERpc3RyaWJ1dGlvblBvaW50MGIG\n"
           + "A1UdIARbMFkwVwYJYIV0AVkBAgEBMEowSAYIKwYBBQUHAgEWPGh0dHA6Ly9yZXBv\n"
           + "c2l0b3J5LnN3aXNzc2lnbi5jb20vU3dpc3NTaWduLUdvbGQtQ1AtQ1BTLVI1LnBk\n"
           + "ZjCB0QYIKwYBBQUHAQEEgcQwgcEwZAYIKwYBBQUHMAKGWGh0dHA6Ly9zd2lzc3Np\n"
           + "Z24ubmV0L2NnaS1iaW4vYXV0aG9yaXR5L2Rvd25sb2FkLzg4NzQ0NjZEQzc3Q0I1\n"
           + "RkE3MjcxMTlERUJGMjc1RTBENjhFMEY3MjcwWQYIKwYBBQUHMAGGTWh0dHA6Ly9n\n"
           + "b2xkLWV2LWcyLm9jc3Auc3dpc3NzaWduLm5ldC84ODc0NDY2REM3N0NCNUZBNzI3\n"
           + "MTE5REVCRjI3NUUwRDY4RTBGNzI3MA0GCSqGSIb3DQEBBQUAA4IBAQA8kdxUZdXa\n"
           + "qu1EATZM77OhA4jw4rmrVNA+iQDb1NdlPldbc5PyQoIWdn7dJgzZrmupgOurRsol\n"
           + "kUoXb2GrZDaiSK+2sW7VQAcS3p4yK1MawGpcekVcOiFkCjFvuqkwdgnOeZpFIJzP\n"
           + "Nh6W0wkAxbAVwP/cAOFSoCKTdTfxLMU2g8g+7J49BagYm/b3h1UmvL+B4s7XzL+D\n"
           + "QDiKzIUvb4xwmbDYksgflkOBwliG3sC8H6LDD+2n3ukFOOKyiXQnoz2QJ57R/Jhj\n"
           + "kgKyXcr7+6RxatGM7K1u7RlfhuxQxvvrb0NTS8ojLwx6fZL1qYqRGjDWhTv36aRu\n"
           + "nbZMIuE5QJQs\n"
           + "-----END CERTIFICATE-----\n");

    private static final String CERT_WITH_BASIC_NON_CA
        = ("-----BEGIN CERTIFICATE-----\n"
           + "MIIGwDCCBaigAwIBAgIQBXBpbXU7lyKUBaP2n+mqwjANBgkqhkiG9w0BAQUFADCB\n"
           + "vjELMAkGA1UEBhMCVVMxFzAVBgNVBAoTDlZlcmlTaWduLCBJbmMuMR8wHQYDVQQL\n"
           + "ExZWZXJpU2lnbiBUcnVzdCBOZXR3b3JrMTswOQYDVQQLEzJUZXJtcyBvZiB1c2Ug\n"
           + "YXQgaHR0cHM6Ly93d3cudmVyaXNpZ24uY29tL3JwYSAoYykwNjE4MDYGA1UEAxMv\n"
           + "VmVyaVNpZ24gQ2xhc3MgMyBFeHRlbmRlZCBWYWxpZGF0aW9uIFNTTCBTR0MgQ0Ew\n"
           + "HhcNMTIxMDEwMDAwMDAwWhcNMTQxMDEwMjM1OTU5WjCCASUxEzARBgsrBgEEAYI3\n"
           + "PAIBAxMCVVMxGTAXBgsrBgEEAYI3PAIBAhMIRGVsYXdhcmUxHTAbBgNVBA8TFFBy\n"
           + "aXZhdGUgT3JnYW5pemF0aW9uMRAwDgYDVQQFEwcyMTU4MTEzMQswCQYDVQQGEwJV\n"
           + "UzEOMAwGA1UEERQFOTQwNDMxEzARBgNVBAgTCkNhbGlmb3JuaWExFjAUBgNVBAcU\n"
           + "DU1vdW50YWluIFZpZXcxGTAXBgNVBAkUEDM1MCBFbGxpcyBTdHJlZXQxHTAbBgNV\n"
           + "BAoUFFN5bWFudGVjIENvcnBvcmF0aW9uMSMwIQYDVQQLFBpJbmZyYXN0cnVjdHVy\n"
           + "ZSAgT3BlcmF0aW9uczEZMBcGA1UEAxQQd3d3LnZlcmlzaWduLmNvbTCCASIwDQYJ\n"
           + "KoZIhvcNAQEBBQADggEPADCCAQoCggEBAM0oFzrY8FaYnXJSzme9WCwB3wPB+HS8\n"
           + "blBuW6DbI11w7In0P6BCVwt/WqI1a+VwSfliKv7pD2P6eHvu6eb8ipPGF3xBRmtr\n"
           + "Ttg9am77taHkB+w1trx9xXio0viFOPYf9mt2yNhCatjKeXnPRH8IoLoI5bqBhv8V\n"
           + "u/Mg9s1Wwe8mW1zxztD3D0fVkWqMpQRLFLrs3Us58SbnaxbFLEmAQHPgrDwi+IC4\n"
           + "aQWcf4UbCkA5P0at+svsu/G+KwYBrsVFL6NaoATcyqimckyCVxeKK6QEPRPM34ae\n"
           + "7HpT9OWmCu+r4GhM7AQS2mY3wF1EhtigXyUUteU/H06kWyVybpy2VwcCAwEAAaOC\n"
           + "Ak4wggJKMIHEBgNVHREEgbwwgbmCEHd3dy52ZXJpc2lnbi5jb22CDHZlcmlzaWdu\n"
           + "LmNvbYIQd3d3LnZlcmlzaWduLm5ldIIMdmVyaXNpZ24ubmV0ghF3d3cudmVyaXNp\n"
           + "Z24ubW9iaYINdmVyaXNpZ24ubW9iaYIPd3d3LnZlcmlzaWduLmV1ggt2ZXJpc2ln\n"
           + "bi5ldYIVZm9ybXMud3Muc3ltYW50ZWMuY29tgg1zc2xyZXZpZXcuY29tghF3d3cu\n"
           + "c3NscmV2aWV3LmNvbTAJBgNVHRMEAjAAMB0GA1UdDgQWBBSFo5HyhWbCi1NFKniM\n"
           + "6xYHuroUUDAOBgNVHQ8BAf8EBAMCBaAwPgYDVR0fBDcwNTAzoDGgL4YtaHR0cDov\n"
           + "L0VWSW50bC1jcmwudmVyaXNpZ24uY29tL0VWSW50bDIwMDYuY3JsMEQGA1UdIAQ9\n"
           + "MDswOQYLYIZIAYb4RQEHFwYwKjAoBggrBgEFBQcCARYcaHR0cHM6Ly93d3cudmVy\n"
           + "aXNpZ24uY29tL2NwczAoBgNVHSUEITAfBggrBgEFBQcDAQYIKwYBBQUHAwIGCWCG\n"
           + "SAGG+EIEATAfBgNVHSMEGDAWgBROQ8gddu83U3pP8lhvlPM44tW93zB2BggrBgEF\n"
           + "BQcBAQRqMGgwKwYIKwYBBQUHMAGGH2h0dHA6Ly9FVkludGwtb2NzcC52ZXJpc2ln\n"
           + "bi5jb20wOQYIKwYBBQUHMAKGLWh0dHA6Ly9FVkludGwtYWlhLnZlcmlzaWduLmNv\n"
           + "bS9FVkludGwyMDA2LmNlcjANBgkqhkiG9w0BAQUFAAOCAQEAUh48IWs1csaAU3kK\n"
           + "hOZV4vde2ECxgVc0gRNz4V5fVdLsFv04S0V4pSZX77rQn56CFNkj6eImdAaTJVbd\n"
           + "Wk8bB2FIwhjNnWScPXuNxzigVOpfRGuNRJymvkqG1+wq4BlG6aXa8aGu7aiuBCqN\n"
           + "rmRSCj5WZQ94K3NCBUIiQQ9Ll1OGOYO3EM/rylGqUcnPf5aSET2kCIBfN3sG6veH\n"
           + "wex+op4GuETJ48+PCoP0d1WrGGGs++nAgBYjjGCZciYfIxoqyrVaC5Yt5iYpXZA0\n"
           + "ZzqJNbzmUD/l2rJeakdAHK0XYPwbQqvNvI1+dUNR9jlRxSKR8XX6mPe5ZgzMqYu+\n"
           + "CQTDhg==\n"
           + "-----END CERTIFICATE-----\n");

    private static final String CERT_WITH_BASIC_CA_ZERO_PATH_LENGTH
        = ("-----BEGIN CERTIFICATE-----\n"
           + "MIIGqTCCBJGgAwIBAgIJAPeSt8SBjARYMA0GCSqGSIb3DQEBBQUAMEUxCzAJBgNV\n"
           + "BAYTAkNIMRUwEwYDVQQKEwxTd2lzc1NpZ24gQUcxHzAdBgNVBAMTFlN3aXNzU2ln\n"
           + "biBHb2xkIENBIC0gRzIwHhcNMDkwNjEwMDkyOTM5WhcNMjQwNjA2MDkyOTM5WjBN\n"
           + "MQswCQYDVQQGEwJDSDEVMBMGA1UEChMMU3dpc3NTaWduIEFHMScwJQYDVQQDEx5T\n"
           + "d2lzc1NpZ24gRVYgR29sZCBDQSAyMDA5IC0gRzIwggEiMA0GCSqGSIb3DQEBAQUA\n"
           + "A4IBDwAwggEKAoIBAQDQnYs8uZZJHHloM5ucf7q7XcRN1Bl8QMoZiruC8oPmghom\n"
           + "gZyb1qF0nAU/qx13UhcGWrV0goF/2Z8nMUGHjSeHuU65AS6rxm83XvnyI7rLKEcg\n"
           + "4XXgibW3+bKldwjYfgPujGrZXC8gwx3jA+uF35VMIYpkWayAbl6kmoIsN7s7ZOVw\n"
           + "T9gRIyZ+GVhFGgmeYGlUYEY1dQ66nMhwQQtTfVcMIiJPbBnppxU+5D0LM7vOwRX8\n"
           + "tsEOVZyojP3bDqtHo/iWkeMPYSazOEdq4BB0QSc1mXVnu9Vh/NjBm00d0Agd/KsQ\n"
           + "Nn/pR+tbgUYkiBhnu3oJ+XFNBsyFrOxGLJkg9P6fAgMBAAGjggKSMIICjjAOBgNV\n"
           + "HQ8BAf8EBAMCAQYwEgYDVR0TAQH/BAgwBgEB/wIBADAdBgNVHQ4EFgQUiHRGbcd8\n"
           + "tfpycRnevydeDWjg9ycwHwYDVR0jBBgwFoAUWyV7lqRlUX64OfPAeGZe6Drn8O4w\n"
           + "gf8GA1UdHwSB9zCB9DBHoEWgQ4ZBaHR0cDovL2NybC5zd2lzc3NpZ24ubmV0LzVC\n"
           + "MjU3Qjk2QTQ2NTUxN0VCODM5RjNDMDc4NjY1RUU4M0FFN0YwRUUwgaiggaWggaKG\n"
           + "gZ9sZGFwOi8vZGlyZWN0b3J5LnN3aXNzc2lnbi5uZXQvQ049NUIyNTdCOTZBNDY1\n"
           + "NTE3RUI4MzlGM0MwNzg2NjVFRTgzQUU3RjBFRSUyQ089U3dpc3NTaWduJTJDQz1D\n"
           + "SD9jZXJ0aWZpY2F0ZVJldm9jYXRpb25MaXN0P2Jhc2U/b2JqZWN0Q2xhc3M9Y1JM\n"
           + "RGlzdHJpYnV0aW9uUG9pbnQwXQYDVR0gBFYwVDBSBgRVHSAAMEowSAYIKwYBBQUH\n"
           + "AgEWPGh0dHA6Ly9yZXBvc2l0b3J5LnN3aXNzc2lnbi5jb20vU3dpc3NTaWduLUdv\n"
           + "bGQtQ1AtQ1BTLVI0LnBkZjCBxgYIKwYBBQUHAQEEgbkwgbYwZAYIKwYBBQUHMAKG\n"
           + "WGh0dHA6Ly9zd2lzc3NpZ24ubmV0L2NnaS1iaW4vYXV0aG9yaXR5L2Rvd25sb2Fk\n"
           + "LzVCMjU3Qjk2QTQ2NTUxN0VCODM5RjNDMDc4NjY1RUU4M0FFN0YwRUUwTgYIKwYB\n"
           + "BQUHMAGGQmh0dHA6Ly9vY3NwLnN3aXNzc2lnbi5uZXQvNUIyNTdCOTZBNDY1NTE3\n"
           + "RUI4MzlGM0MwNzg2NjVFRTgzQUU3RjBFRTANBgkqhkiG9w0BAQUFAAOCAgEARJJo\n"
           + "SpTCFSg5U+D4W8Cdc7vxEr83McOZY+D1fX490SAv3sDJ7XcbdXODL5m4UeK4s4bg\n"
           + "UR1ZgCFiK8A4GRFpIvD4qse8E+Z20PGbQmtlSUIJztL3y3y4hLcM2Vt+mZz7M+aN\n"
           + "xVlFbIrje+3PwgnvDTrIOLNt+LtV/uonA4A9SpAxlUCroFfSpfA71a3SJll/C4OG\n"
           + "uvPZjHuX1ResF91+JJoyCiHcdi9h6w0yEf29zXdzKkUsaOZ0CikPTKdCZQ4MbIGX\n"
           + "D5qMY65PK0mpT7uAt93ZIITXfQs93RWJWZtF7HrHGjIeloeKkXofsylmqP3JfgeV\n"
           + "/mjuYz/9HS5MAxVE5+Wcb08tMGaoqSRxYhnv2Tmx2s8mPHyCXocgxMhXJtCN++Ba\n"
           + "oO7JQRXeoiUZzIMac67dWb3rScOtEdF4lkIWB0yyts6LUPJtXXbRog3EI3i65ofc\n"
           + "nW3ZdQijbE5t3F03yY/qRoHO8I/Be3qe1zk+7FCpjx7B8VLB1+lajfvLml0sgvCY\n"
           + "O/O9/RRmqFhdhfDnsPj/pWkM6nKu8KjXX6WZmW6FTuC57yG81dI2AYqoO3qlzDdt\n"
           + "IgVXouBar3TAgWRIka5FsxudaWOUK+Mj9TiKSQBYglHWhkdlEUpjOZfZhHKkMht4\n"
           + "Y5mbkvu5+9xcWGhKNBLBq/isdBPkyfLVeVWxxkQ=\n"
           + "-----END CERTIFICATE-----\n");

    private static final String CERT_WITH_BASIC_CA_NO_PATH_LENGTH
        = ("-----BEGIN CERTIFICATE-----\n"
           + "MIIFujCCA6KgAwIBAgIJALtAHEP1Xk+wMA0GCSqGSIb3DQEBBQUAMEUxCzAJBgNV\n"
           + "BAYTAkNIMRUwEwYDVQQKEwxTd2lzc1NpZ24gQUcxHzAdBgNVBAMTFlN3aXNzU2ln\n"
           + "biBHb2xkIENBIC0gRzIwHhcNMDYxMDI1MDgzMDM1WhcNMzYxMDI1MDgzMDM1WjBF\n"
           + "MQswCQYDVQQGEwJDSDEVMBMGA1UEChMMU3dpc3NTaWduIEFHMR8wHQYDVQQDExZT\n"
           + "d2lzc1NpZ24gR29sZCBDQSAtIEcyMIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIIC\n"
           + "CgKCAgEAr+TufoskDhJuqVAtFkQ7kpJcyrhdhJJCEyq8ZVeCQD5XJM1QiyUqt2/8\n"
           + "76LQwB8CJEoTlo8jE+YoWACjR8cGp4QjK7u9lit/VcyLwVcfDmJlD909Vopz2q5+\n"
           + "bbqBHH5CjCA12UNNhPqE21Is8w4ndwtrvxEvcnifLtg+5hg3Wipy+dpikJKVyh+c\n"
           + "6bM8K8vzARO/Ws/BtQpgvd21mWRTuKCWs2/iJneRjOBiEAKfNA+k1ZIzUd6+jbqE\n"
           + "emA8atufK+ze3gE/bk3lUIbLtK/tREDFylqM2tIrfKjuvqblCqoOpd8FUrdVxyJd\n"
           + "MmqXl2MT28nbeTZ7hTpKxVKJ+STnnXepgv9VHKVxaSvRAiTysybUa9oEVeXBCsdt\n"
           + "MDeQKuSeFDNeFhdVxVu1yzSJkvGdJo+hB9TGsnhQ2wwMC3wLjEHXuendjIj3o02y\n"
           + "MszYF9rNt85mndT9Xv+9lz4pded+p2JYryU0pUHHPbwNUMoDAw8IWh+Vc3hiv69y\n"
           + "FGkOpeUDDniOJihC8AcLYiAQZzlG+qkDzAQ4embvIIO1jEpWjpEA/I5cgt6IoMPi\n"
           + "aG59je883WX0XaxR7ySArqpWl2/5rX3aYT+YdzylkbYcjCbaZaIJbcHiVOO5ykxM\n"
           + "gI93e2CaHt+28kgeDrpOVG2Y4OGiGqJ3UM/EY5LsRxmd6+ZrzsECAwEAAaOBrDCB\n"
           + "qTAOBgNVHQ8BAf8EBAMCAQYwDwYDVR0TAQH/BAUwAwEB/zAdBgNVHQ4EFgQUWyV7\n"
           + "lqRlUX64OfPAeGZe6Drn8O4wHwYDVR0jBBgwFoAUWyV7lqRlUX64OfPAeGZe6Drn\n"
           + "8O4wRgYDVR0gBD8wPTA7BglghXQBWQECAQEwLjAsBggrBgEFBQcCARYgaHR0cDov\n"
           + "L3JlcG9zaXRvcnkuc3dpc3NzaWduLmNvbS8wDQYJKoZIhvcNAQEFBQADggIBACe6\n"
           + "45R88a7A3hfm5djV9VSwg/S7zV4Fe0+fdWavPOhWfvxyeDgD2StiGwC5+OlgzczO\n"
           + "UYrHUDFu4Up+GC9pWbY9ZIEr44OE5iKHjn3g7gKZYbge9LgriBIWhMIxkziWMaa5\n"
           + "O1M/wySTVltpkuzFwbs4AOPsF6m43Md8AYOfMke6UiI0HTJ6CVanfCU2qT1L2sCC\n"
           + "bwq7EsiHSycR+R4tx5M/nttfJmtS2S6K8RTGRI0Vqbe/vd6mGu6uLftIdxf+u+yv\n"
           + "GPUqUfA5hJeVbG4bwyvEdGB5JbAKJ9/fXtI5z0V9QkvfsywexcZdylU6oJxpmo/a\n"
           + "77KwPJ+HbBIrZXAVUjEaJM9vMSNQH4xPjyPDdEFjHFWoFN0+4FFQz/EbMFYOkrCC\n"
           + "hdiDyyJkvC24JdVUorgG6q2SpCSgwYa1ShNqR88uC1aVVMvOmttqtKay20EIhid3\n"
           + "92qgQmwLOM7XdVAyksLfKzAiSNDVQTglXaTpXZ/GlHXQRf0wl0OPkKsKx4ZzYEpp\n"
           + "Ld6leNcG2mqeSz53OiATIgHQv2ieY2BrNU0LbbqhPcCT4H8js1WtciVORvnSFu+w\n"
           + "ZMEBnunKoGqYDs/YYPIvSbjkQuE4NRb0yG5P94FW6LqjviOvrv1vA+ACOzB2+htt\n"
           + "Qc8Bsem4yWb02ybzOqR08kkkW8mw0FfB+j564ZfJ\n"
           + "-----END CERTIFICATE-----\n");

    public void testGetBasicConstraints() throws Exception {
        assertEquals(5, generateCert(CERT_CORRECT).getBasicConstraints());
        assertEquals(-1, generateCert(CERT_WITHOUT_BASIC).getBasicConstraints());
        assertEquals(-1, generateCert(CERT_WITH_BASIC_NON_CA).getBasicConstraints());
        assertEquals(0, generateCert(CERT_WITH_BASIC_CA_ZERO_PATH_LENGTH).getBasicConstraints());
        assertEquals(Integer.MAX_VALUE, generateCert(CERT_WITH_BASIC_CA_NO_PATH_LENGTH).getBasicConstraints());
    }

    public void testGetSubjectAlternativeNames() throws Exception {

assertNull(new MyX509Certificate().getSubjectAlternativeNames());

        X509Certificate cert = generateCert(CERT_CORRECT);
Collection<List<?>> coll = cert.getSubjectAlternativeNames();
//getSubjectAlternativeNames method is not supported
assertNotNull(coll);
        coll.clear();
        assertEquals(0, coll.size());

}

    public void testGetIssuerAlternativeNames() throws Exception {

assertNull(new MyX509Certificate().getIssuerAlternativeNames());

        X509Certificate cert = generateCert(CERT_CORRECT);
Collection<List<?>> coll = cert.getIssuerAlternativeNames();
// getIssuerAlternativeNames returns null.
assertNotNull(coll);
        coll.clear();
        assertEquals(0, coll.size());
}

    public void testCerficateException() throws Exception {
try {
            generateCert(CERT_TAMPERED);
            fail();
        } catch (CertificateException expected) {
}

try {
            generateCert(CERT);
            fail();
        } catch (CertificateException expected) {
}
}

    public X509Certificate generateCert(String string) throws Exception {
CertificateFactory cf = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream bais = new ByteArrayInputStream(string.getBytes());
        X509Certificate cert = (X509Certificate) cf.generateCertificate(bais);
assertNotNull(cert);
        return cert;
}
}







