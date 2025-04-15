/*Add OIDs for algorithms

This allows things from a PKCS#7 container (or any other container that
specifies algorithms by OID) to get an instance via OID instead of the
common name.

Bug:http://code.google.com/p/android/issues/detail?id=38321Change-Id:Ie766751a3f7894a558f7e40e7d520800bf7a8a08*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLProvider.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLProvider.java
//Synthetic comment -- index 485043f..8cb81c2 100644

//Synthetic comment -- @@ -18,13 +18,23 @@

import java.security.Provider;

public final class OpenSSLProvider extends Provider {
public static final String PROVIDER_NAME = "AndroidOpenSSL";

public OpenSSLProvider() {
super(PROVIDER_NAME, 1.0, "Android's OpenSSL-backed security provider");

        // SSL Contexts
put("SSLContext.SSL", OpenSSLContextImpl.class.getName());
put("SSLContext.SSLv3", OpenSSLContextImpl.class.getName());
put("SSLContext.TLS", OpenSSLContextImpl.class.getName());
//Synthetic comment -- @@ -33,7 +43,7 @@
put("SSLContext.TLSv1.2", OpenSSLContextImpl.class.getName());
put("SSLContext.Default", DefaultSSLContextImpl.class.getName());

        // Message Digests
put("MessageDigest.SHA-1",
"org.apache.harmony.xnet.provider.jsse.OpenSSLMessageDigestJDK$SHA1");
put("Alg.Alias.MessageDigest.SHA1", "SHA-1");
//Synthetic comment -- @@ -55,24 +65,25 @@
put("Alg.Alias.MessageDigest.SHA512", "SHA-512");
put("Alg.Alias.MessageDigest.2.16.840.1.101.3.4.2.3", "SHA-512");

put("MessageDigest.MD5",
"org.apache.harmony.xnet.provider.jsse.OpenSSLMessageDigestJDK$MD5");
put("Alg.Alias.MessageDigest.1.2.840.113549.2.5", "MD5");

        // KeyPairGenerators
put("KeyPairGenerator.RSA", OpenSSLRSAKeyPairGenerator.class.getName());
put("Alg.Alias.KeyPairGenerator.1.2.840.113549.1.1.1", "RSA");

put("KeyPairGenerator.DSA", OpenSSLDSAKeyPairGenerator.class.getName());

        // KeyFactory

put("KeyFactory.RSA", OpenSSLRSAKeyFactory.class.getName());
put("Alg.Alias.KeyFactory.1.2.840.113549.1.1.1", "RSA");

// put("KeyFactory.DSA", OpenSSLDSAKeyFactory.class.getName());

        // Signatures
put("Signature.MD5WithRSAEncryption", OpenSSLSignature.MD5RSA.class.getName());
put("Alg.Alias.Signature.MD5WithRSA", "MD5WithRSAEncryption");
put("Alg.Alias.Signature.MD5/RSA", "MD5WithRSAEncryption");
//Synthetic comment -- @@ -92,14 +103,22 @@
put("Signature.SHA256WithRSAEncryption", OpenSSLSignature.SHA256RSA.class.getName());
put("Alg.Alias.Signature.SHA256WithRSA", "SHA256WithRSAEncryption");
put("Alg.Alias.Signature.1.2.840.113549.1.1.11", "SHA256WithRSAEncryption");

put("Signature.SHA384WithRSAEncryption", OpenSSLSignature.SHA384RSA.class.getName());
put("Alg.Alias.Signature.SHA384WithRSA", "SHA384WithRSAEncryption");
put("Alg.Alias.Signature.1.2.840.113549.1.1.12", "SHA384WithRSAEncryption");

put("Signature.SHA512WithRSAEncryption", OpenSSLSignature.SHA512RSA.class.getName());
put("Alg.Alias.Signature.SHA512WithRSA", "SHA512WithRSAEncryption");
put("Alg.Alias.Signature.1.2.840.113549.1.1.13", "SHA512WithRSAEncryption");

put("Signature.SHA1withDSA", OpenSSLSignature.SHA1DSA.class.getName());
put("Alg.Alias.Signature.SHA/DSA", "SHA1withDSA");
//Synthetic comment -- @@ -111,7 +130,7 @@

put("Signature.NONEwithRSA", OpenSSLSignatureRawRSA.class.getName());

        // SecureRandom
/*
* We have to specify SHA1PRNG because various documentation mentions
* that algorithm by name instead of just recommending calling
//Synthetic comment -- @@ -120,7 +139,7 @@
put("SecureRandom.SHA1PRNG", OpenSSLRandom.class.getName());
put("SecureRandom.SHA1PRNG ImplementedIn", "Software");

        // Cipher
put("Cipher.RSA/ECB/NoPadding", OpenSSLCipherRSA.Raw.class.getName());
put("Alg.Alias.Cipher.RSA/None/NoPadding", "RSA/ECB/NoPadding");
put("Cipher.RSA/ECB/PKCS1Padding", OpenSSLCipherRSA.PKCS1.class.getName());
//Synthetic comment -- @@ -150,11 +169,27 @@
put("Cipher.DESEDE/OFB/NoPadding", OpenSSLCipher.DESEDE.OFB.NoPadding.class.getName());
put("Cipher.DESEDE/OFB/PKCS5Padding", OpenSSLCipher.DESEDE.OFB.PKCS5Padding.class.getName());

        // Mac
put("Mac.HmacMD5", OpenSSLMac.HmacMD5.class.getName());
put("Mac.HmacSHA1", OpenSSLMac.HmacSHA1.class.getName());
put("Mac.HmacSHA256", OpenSSLMac.HmacSHA256.class.getName());
put("Mac.HmacSHA384", OpenSSLMac.HmacSHA384.class.getName());
put("Mac.HmacSHA512", OpenSSLMac.HmacSHA512.class.getName());
}
}







