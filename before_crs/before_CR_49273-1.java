/*Harmony: ignore invalid IP in subject alt. names

The RI and other providers will ignore invalid IP lengths instead of
throwing a parsing exception. Bring Harmony in line with those other
providers.

Change-Id:I65715a58614543ab296493829338239cf402ae5d*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/x509/Extensions.java b/luni/src/main/java/org/apache/harmony/security/x509/Extensions.java
//Synthetic comment -- index 614165b..1856777 100644

//Synthetic comment -- @@ -260,7 +260,19 @@
if (extension == null) {
return null;
}
        return ((GeneralNames) GeneralNames.ASN1.decode(extension.getExtnValue())).getPairsList();
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/x509/GeneralName.java b/luni/src/main/java/org/apache/harmony/security/x509/GeneralName.java
//Synthetic comment -- index e216029..5dd8077 100644

//Synthetic comment -- @@ -219,10 +219,6 @@
*  component is doubled (to 8 and 32 bytes respectively).
*/
public GeneralName(byte[] name) throws IllegalArgumentException {
        int length = name.length;
        if (length != 4 && length != 8 && length != 16 && length != 32) {
            throw new IllegalArgumentException("name.length invalid");
        }
this.tag = IP_ADDR;
this.name = new byte[name.length];
System.arraycopy(name, 0, this.name, 0, name.length);
//Synthetic comment -- @@ -390,6 +386,20 @@
byte[] address = (byte[]) name;
byte[] _address = (byte[]) gname.getName();
int length = address.length;
int _length = _address.length;
if (length == _length) {
return Arrays.equals(address, _address);
//Synthetic comment -- @@ -644,8 +654,9 @@
}

/**
     * Returns the string form of the given IP address. Addresses of length 2x
     * the canonical length are treated as a route/mask pair.
*/
public static String ipBytesToStr(byte[] ip) {
try {








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/x509/GeneralNames.java b/luni/src/main/java/org/apache/harmony/security/x509/GeneralNames.java
//Synthetic comment -- index 93c4af1..e8a78dd 100644

//Synthetic comment -- @@ -84,7 +84,18 @@
return result;
}
for (GeneralName generalName : generalNames) {
            result.add(generalName.getAsList());
}
return result;
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java b/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java
//Synthetic comment -- index 77241e1..151c64a 100644

//Synthetic comment -- @@ -903,17 +903,7 @@

private void getSubjectAlternativeNames_InvalidIP(CertificateFactory f) throws Exception {
X509Certificate c = getCertificate(f, CERT_INVALIDIP);
        Collection<List<?>> col = null;
        try {
            col = c.getSubjectAlternativeNames();
            if ("DRLCertFactory".equals(f.getProvider().getName())) {
                fail("Harmony throws (Provider=" + f.getProvider().getName() + ")");
            }
        } catch (IllegalArgumentException expected) {
            if (!"DRLCertFactory".equals(f.getProvider().getName())) {
                fail("Non-Harmony shouldn't throw");
            }
        }
assertNull(col);
}








