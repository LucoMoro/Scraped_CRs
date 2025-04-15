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

        Collection<List<?>> collection = ((GeneralNames) GeneralNames.ASN1.decode(extension
                .getExtnValue())).getPairsList();

        /*
         * If the extension had any invalid entries, we may have an empty
         * collection at this point, so just return null.
         */
        if (collection.size() == 0) {
            return null;
        }

        return collection;
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/x509/GeneralName.java b/luni/src/main/java/org/apache/harmony/security/x509/GeneralName.java
//Synthetic comment -- index e216029..5dd8077 100644

//Synthetic comment -- @@ -219,10 +219,6 @@
*  component is doubled (to 8 and 32 bytes respectively).
*/
public GeneralName(byte[] name) throws IllegalArgumentException {
this.tag = IP_ADDR;
this.name = new byte[name.length];
System.arraycopy(name, 0, this.name, 0, name.length);
//Synthetic comment -- @@ -390,6 +386,20 @@
byte[] address = (byte[]) name;
byte[] _address = (byte[]) gname.getName();
int length = address.length;

                /*
                 * For IP v4, as specified in RFC 791, the address must contain
                 * exactly 4 byte component. For IP v6, as specified in RFC
                 * 1883, the address must contain exactly 16 byte component. If
                 * GeneralName structure is used as a part of Name Constraints
                 * extension, to represent an address range the number of
                 * address component is doubled (to 8 and 32 bytes
                 * respectively).
                 */
                if (length != 4 && length != 8 && length != 16 && length != 32) {
                    return false;
                }

int _length = _address.length;
if (length == _length) {
return Arrays.equals(address, _address);
//Synthetic comment -- @@ -644,8 +654,9 @@
}

/**
     * Returns the string form of the given IP address. If the address is not 4
     * octets for IPv4 or 16 octets for IPv6, an IllegalArgumentException will
     * be thrown.
*/
public static String ipBytesToStr(byte[] ip) {
try {








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/x509/GeneralNames.java b/luni/src/main/java/org/apache/harmony/security/x509/GeneralNames.java
//Synthetic comment -- index 93c4af1..e8a78dd 100644

//Synthetic comment -- @@ -84,7 +84,18 @@
return result;
}
for (GeneralName generalName : generalNames) {
            /*
             * If we have an error decoding one of the GeneralNames, we'll just
             * omit it from the final list.
             */
            final List<Object> genNameList;
            try {
                genNameList = generalName.getAsList();
            } catch (IllegalArgumentException ignored) {
                continue;
            }

            result.add(genNameList);
}
return result;
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java b/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java
//Synthetic comment -- index 77241e1..151c64a 100644

//Synthetic comment -- @@ -903,17 +903,7 @@

private void getSubjectAlternativeNames_InvalidIP(CertificateFactory f) throws Exception {
X509Certificate c = getCertificate(f, CERT_INVALIDIP);
        Collection<List<?>> col = c.getSubjectAlternativeNames();
assertNull(col);
}








