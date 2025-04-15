/*Add new Android-only algos to  StandardNames

The ProviderTest fails if we don't add these to StandardNames.

Change the name of Signature.RAWRSA to "NONEwithRSA" so it matches the
convention in existing algorithms.

Change-Id:Id126eca46ee3b9f9d19aee596c1babd489693c7a*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLProvider.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLProvider.java
//Synthetic comment -- index e13ccda..126408f 100644

//Synthetic comment -- @@ -109,8 +109,7 @@
put("Alg.Alias.Signature.DSAWithSHA1", "SHA1withDSA");
put("Alg.Alias.Signature.1.2.840.10040.4.3", "SHA1withDSA");

        put("Signature.NONEwithRSA", OpenSSLSignatureRawRSA.class.getName());

// SecureRandom
/*








//Synthetic comment -- diff --git a/support/src/test/java/libcore/java/security/StandardNames.java b/support/src/test/java/libcore/java/security/StandardNames.java
//Synthetic comment -- index 6539e2c..310a854 100644

//Synthetic comment -- @@ -334,6 +334,10 @@
unprovide("Signature", "SHA512WithRSA");
provide("Signature", "SHA512WithRSAEncryption");

            // Added to support Android Keystore operations
            provide("Signature", "NONEwithRSA");
            provide("Cipher", "RSA/ECB/NOPADDING");

// different names: JSSE Reference Guide says PKIX aka X509
unprovide("TrustManagerFactory", "PKIX");
provide("TrustManagerFactory", "X509");







