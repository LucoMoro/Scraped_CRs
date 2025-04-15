/*Harmony: fix DirName handling in X.509 certs

In SubjectAlternativeName, the directoryName (4) entry is actually a
CHOICE of a single possibility. Harmony was trying to decode it as the
single possibility itself and running into parsing issues.

Additionally, change T61String (TeletexString) handling to assume UTF8
encoding like the RI does.

Change-Id:Ic0b9541dfed21b59940fa50a27cee2e7704d8950*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/asn1/ASN1StringType.java b/luni/src/main/java/org/apache/harmony/security/asn1/ASN1StringType.java
//Synthetic comment -- index 71f5b0e..024b65f 100644

//Synthetic comment -- @@ -32,43 +32,39 @@
*/
public abstract class ASN1StringType extends ASN1Type {

    // TODO: what about defining them as separate classes?
    // TODO: check decoded/encoded characters
    public static final ASN1StringType BMPSTRING = new ASN1StringType(
            TAG_BMPSTRING) {
    };

    public static final ASN1StringType IA5STRING = new ASN1StringType(
            TAG_IA5STRING) {
    };

    public static final ASN1StringType GENERALSTRING = new ASN1StringType(
            TAG_GENERALSTRING) {
    };

    public static final ASN1StringType PRINTABLESTRING = new ASN1StringType(
            TAG_PRINTABLESTRING) {
    };

    public static final ASN1StringType TELETEXSTRING = new ASN1StringType(
            TAG_TELETEXSTRING) {
    };

    public static final ASN1StringType UNIVERSALSTRING = new ASN1StringType(
            TAG_UNIVERSALSTRING) {
    };

    public static final ASN1StringType UTF8STRING = new ASN1StringType(TAG_UTF8STRING) {
        @Override public Object getDecodedObject(BerInputStream in) throws IOException {
return new String(in.buffer, in.contentOffset, in.length, Charsets.UTF_8);
}

        @Override public void setEncodingContent(BerOutputStream out) {
byte[] bytes = ((String) out.content).getBytes(Charsets.UTF_8);
out.content = bytes;
out.length = bytes.length;
}
    };

public ASN1StringType(int tagNumber) {
super(tagNumber);








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/x509/DNParser.java b/luni/src/main/java/org/apache/harmony/security/x509/DNParser.java
//Synthetic comment -- index 0d5da91..4946419 100644

//Synthetic comment -- @@ -380,7 +380,7 @@
/**
* Parses DN
*
     * @return a list of Relative Distinguished Names(RND),
*         each RDN is represented as a list of AttributeTypeAndValue objects
*/
public List<List<AttributeTypeAndValue>> parse() throws IOException {








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/x509/GeneralName.java b/luni/src/main/java/org/apache/harmony/security/x509/GeneralName.java
//Synthetic comment -- index 5dd8077..99bd0ef 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
//Synthetic comment -- @@ -37,6 +38,7 @@
import org.apache.harmony.security.asn1.ASN1Implicit;
import org.apache.harmony.security.asn1.ASN1OctetString;
import org.apache.harmony.security.asn1.ASN1Oid;
import org.apache.harmony.security.asn1.ASN1StringType;
import org.apache.harmony.security.asn1.ASN1Type;
import org.apache.harmony.security.asn1.BerInputStream;
//Synthetic comment -- @@ -666,12 +668,23 @@
}
}

public static final ASN1Choice ASN1 = new ASN1Choice(new ASN1Type[] {
new ASN1Implicit(0, OtherName.ASN1),
new ASN1Implicit(1, ASN1StringType.IA5STRING),
new ASN1Implicit(2, ASN1StringType.IA5STRING),
new ASN1Implicit(3, ORAddress.ASN1),
           new ASN1Implicit(4, Name.ASN1),
new ASN1Implicit(5, EDIPartyName.ASN1),
new ASN1Implicit(6, ASN1StringType.IA5STRING),
new ASN1Implicit(7, ASN1OctetString.getInstance()),








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java b/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java
//Synthetic comment -- index 151c64a..440140e 100644

//Synthetic comment -- @@ -790,18 +790,7 @@

private void getSubjectAlternativeNames(CertificateFactory f) throws Exception {
X509Certificate c = getCertificate(f, CERT_RSA);
        Collection<List<?>> col = null;
        try {
            col = c.getSubjectAlternativeNames();
            if ("DRLCertFactory".equals(f.getProvider().getName())) {
                fail("Harmony should throw (Provider=" + f.getProvider().getName() + ")");
            }
        } catch (CertificateParsingException e) {
            if (!"DRLCertFactory".equals(f.getProvider().getName())) {
                throw new Exception("Non-Harmony shouldn't throw", e);
            }
            return;
        }

// BouncyCastle is broken
if ("BC".equals(f.getProvider().getName())) {
//Synthetic comment -- @@ -976,19 +965,7 @@

private void getSubjectAlternativeNames_DirName(CertificateFactory f) throws Exception {
X509Certificate c = getCertificate(f, CERT_ALT_DIRNAME);
        Collection<List<?>> col = null;
        try {
            col = c.getSubjectAlternativeNames();
            if ("DRLCertFactory".equals(f.getProvider().getName())) {
                fail("Harmony throws (Provider=" + f.getProvider().getName() + ")");
            }
        } catch (CertificateParsingException e) {
            // Harmony is broken
            if (!"DRLCertFactory".equals(f.getProvider().getName())) {
                fail("Non-Harmony shouldn't throw");
            }
            return;
        }

// BouncyCastle is broken
if ("BC".equals(f.getProvider().getName())) {
//Synthetic comment -- @@ -1054,18 +1031,7 @@

private void getIssuerAlternativeNames(CertificateFactory f) throws Exception {
X509Certificate c = getCertificate(f, CERT_RSA);
        Collection<List<?>> col = null;
        try {
            col = c.getSubjectAlternativeNames();
            if ("DRLCertFactory".equals(f.getProvider().getName())) {
                fail("Harmony should throw (Provider=" + f.getProvider().getName() + ")");
            }
        } catch (CertificateParsingException e) {
            if (!"DRLCertFactory".equals(f.getProvider().getName())) {
                throw new Exception("Non-Harmony shouldn't throw", e);
            }
            return;
        }

// BouncyCastle is broken
if ("BC".equals(f.getProvider().getName())) {







