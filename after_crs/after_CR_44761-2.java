/*Try to more robustly identify algorithm for JARs

Change-Id:I17f339efd030fff38e8c59f8d607625de2627d4e*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/pkcs7/SignerInfo.java b/luni/src/main/java/org/apache/harmony/security/pkcs7/SignerInfo.java
//Synthetic comment -- index baaa090..4015e70 100644

//Synthetic comment -- @@ -93,10 +93,18 @@
return digestAlgorithm.getAlgorithm();
}

    public String getDigestAlgorithmName() {
        return digestAlgorithm.getAlgorithmName();
    }

public String getDigestEncryptionAlgorithm() {
return digestEncryptionAlgorithm.getAlgorithm();
}

    public String getDigestEncryptionAlgorithmName() {
        return digestEncryptionAlgorithm.getAlgorithmName();
    }

public List<AttributeTypeAndValue> getAuthenticatedAttributes() {
if (authenticatedAttributes == null) {
return null;








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/utils/JarUtils.java b/luni/src/main/java/org/apache/harmony/security/utils/JarUtils.java
//Synthetic comment -- index 0f95484..f31754b 100644

//Synthetic comment -- @@ -114,27 +114,56 @@
}

// Get Signature instance
        final String daOid = sigInfo.getDigestAlgorithm();
        final String daName = sigInfo.getDigestAlgorithmName();
        final String deaOid = sigInfo.getDigestEncryptionAlgorithm();

String alg = null;
        Signature sig = null;

        if (daOid != null && deaOid != null) {
            alg = daOid + "with" + deaOid;
try {
sig = Signature.getInstance(alg);
} catch (NoSuchAlgorithmException e) {
            }

            // Try to convert to names instead of OID.
            if (sig == null) {
                final String deaName = sigInfo.getDigestEncryptionAlgorithmName();
                alg = daName + "with" + deaName;
                try {
                    sig = Signature.getInstance(alg);
                } catch (NoSuchAlgorithmException e) {
                }
}
}

        /*
         * TODO figure out the case in which we'd only use digestAlgorithm and
         * add a test for it.
         */
        if (sig == null && daOid != null) {
            alg = daOid;
            try {
                sig = Signature.getInstance(alg);
            } catch (NoSuchAlgorithmException e) {
            }

            if (sig == null && daName != null) {
                alg = daName;
                try {
                    sig = Signature.getInstance(alg);
                } catch (NoSuchAlgorithmException e) {
                }
            }
        }

        // We couldn't find a valid Signature type.
        if (sig == null) {
            return null;
        }

sig.initVerify(certs[issuerSertIndex]);

// If the authenticatedAttributes field of SignerInfo contains more than zero attributes,







