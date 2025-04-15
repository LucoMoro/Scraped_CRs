/*Make sure alternativeNames are unmodifiable

Add tests to make sure the Collection<List<?>> instance returned from
getSubjectAlternativeNames and getIssuerAlternativeNames is
unmodifiable. Also test the byte arrays returned are clones of
anything kept long-term.

Change-Id:Ia7564665643e63cb04a264b011eeebaeeed3811f*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/x509/Extensions.java b/luni/src/main/java/org/apache/harmony/security/x509/Extensions.java
//Synthetic comment -- index 1856777..9539054 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
//Synthetic comment -- @@ -256,23 +257,7 @@
* null if does not.
*/
public Collection<List<?>> valueOfSubjectAlternativeName() throws IOException {
        Extension extension = getExtensionByOID("2.5.29.17");
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
//Synthetic comment -- @@ -291,11 +276,31 @@
* null if does not.
*/
public Collection<List<?>> valueOfIssuerAlternativeName() throws IOException {
        Extension extension = getExtensionByOID("2.5.29.18");
if (extension == null) {
return null;
}
        return ((GeneralNames) GeneralNames.ASN1.decode(extension.getExtnValue())).getPairsList();
}

/**








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java b/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java
//Synthetic comment -- index 5e24dba..3077d2a 100644

//Synthetic comment -- @@ -47,6 +47,7 @@
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
//Synthetic comment -- @@ -801,6 +802,15 @@
private void checkAlternativeNames(Collection<List<?>> col) {
assertNotNull(col);

/*
* There should be 9 types of alternative names in this test
* certificate.
//Synthetic comment -- @@ -808,6 +818,15 @@
boolean[] typesFound = new boolean[9];

for (List<?> item : col) {
assertTrue(item.get(0) instanceof Integer);
int type = (Integer) item.get(0);
typesFound[type] = true;
//Synthetic comment -- @@ -901,7 +920,15 @@

/* OID:1.2.3.4, UTF8:test1 */
final byte[] der = getOIDTestBytes();
        assertEquals(Arrays.toString(der), Arrays.toString((byte[]) item.get(1)));
}

private void getSubjectAlternativeNames_Email(CertificateFactory f) throws Exception {







