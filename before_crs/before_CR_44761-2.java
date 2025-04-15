/*Try to more robustly identify algorithm for JARs

Change-Id:I17f339efd030fff38e8c59f8d607625de2627d4e*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/pkcs7/SignerInfo.java b/luni/src/main/java/org/apache/harmony/security/pkcs7/SignerInfo.java
//Synthetic comment -- index baaa090..4015e70 100644

//Synthetic comment -- @@ -93,10 +93,18 @@
return digestAlgorithm.getAlgorithm();
}

public String getDigestEncryptionAlgorithm() {
return digestEncryptionAlgorithm.getAlgorithm();
}

public List<AttributeTypeAndValue> getAuthenticatedAttributes() {
if (authenticatedAttributes == null) {
return null;








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/utils/JarUtils.java b/luni/src/main/java/org/apache/harmony/security/utils/JarUtils.java
//Synthetic comment -- index 0f95484..f31754b 100644

//Synthetic comment -- @@ -114,27 +114,56 @@
}

// Get Signature instance
        Signature sig = null;
        String da = sigInfo.getDigestAlgorithm();
        String dea = sigInfo.getDigestEncryptionAlgorithm();
String alg = null;
        if (da != null && dea != null) {
            alg = da + "with" +  dea;
            try {
                sig = Signature.getInstance(alg);
            } catch (NoSuchAlgorithmException e) {}
        }
        if (sig == null) {
            alg = da;
            if (alg == null) {
                return null;
            }
try {
sig = Signature.getInstance(alg);
} catch (NoSuchAlgorithmException e) {
                return null;
}
}
sig.initVerify(certs[issuerSertIndex]);

// If the authenticatedAttributes field of SignerInfo contains more than zero attributes,







