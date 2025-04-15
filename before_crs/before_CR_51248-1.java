/*NativeCrypto: reduce strict Class equality check

For requested keySpec, we don't necessarily need the strict equality
check.

Also, remove code that is unreachable: RSAPrivateCrtKeySpec is a child
of RSAPrivateKeySpec, so there is no need to check whether the keySpec
is assignable to the CRT spec.

Change-Id:I8070541b015167d9314b83b45bd1410663487865*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLDSAKeyFactory.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLDSAKeyFactory.java
//Synthetic comment -- index bad8db6..2bf6f57 100644

//Synthetic comment -- @@ -90,7 +90,7 @@
if (key instanceof DSAPublicKey) {
DSAPublicKey dsaKey = (DSAPublicKey) key;

            if (DSAPublicKeySpec.class.equals(keySpec)) {
BigInteger y = dsaKey.getY();

DSAParams params = dsaKey.getParams();
//Synthetic comment -- @@ -99,7 +99,7 @@
BigInteger g = params.getG();

return (T) new DSAPublicKeySpec(y, p, q, g);
            } else if (X509EncodedKeySpec.class.equals(keySpec)) {
return (T) new X509EncodedKeySpec(key.getEncoded());
} else {
throw new InvalidKeySpecException(
//Synthetic comment -- @@ -108,7 +108,7 @@
} else if (key instanceof DSAPrivateKey) {
DSAPrivateKey dsaKey = (DSAPrivateKey) key;

            if (DSAPrivateKeySpec.class.equals(keySpec)) {
BigInteger x = dsaKey.getX();

DSAParams params = dsaKey.getParams();
//Synthetic comment -- @@ -117,7 +117,7 @@
BigInteger g = params.getG();

return (T) new DSAPrivateKeySpec(x, p, q, g);
            } else if (PKCS8EncodedKeySpec.class.equals(keySpec)) {
return (T) new PKCS8EncodedKeySpec(dsaKey.getEncoded());
} else {
throw new InvalidKeySpecException(








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECKeyFactory.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECKeyFactory.java
//Synthetic comment -- index cb13876..e7fec4a 100644

//Synthetic comment -- @@ -91,13 +91,13 @@
if (key instanceof ECPublicKey) {
ECPublicKey ecKey = (ECPublicKey) key;

            if (ECPublicKeySpec.class.equals(keySpec)) {
ECParameterSpec params = ecKey.getParams();

ECPoint w = ecKey.getW();

return (T) new ECPublicKeySpec(w, params);
            } else if (X509EncodedKeySpec.class.equals(keySpec)) {
return (T) new X509EncodedKeySpec(key.getEncoded());
} else {
throw new InvalidKeySpecException(
//Synthetic comment -- @@ -106,13 +106,13 @@
} else if (key instanceof ECPrivateKey) {
ECPrivateKey ecKey = (ECPrivateKey) key;

            if (ECPrivateKeySpec.class.equals(keySpec)) {
ECParameterSpec params = ecKey.getParams();

BigInteger s = ecKey.getS();

return (T) new ECPrivateKeySpec(s, params);
            } else if (PKCS8EncodedKeySpec.class.equals(keySpec)) {
return (T) new PKCS8EncodedKeySpec(ecKey.getEncoded());
} else {
throw new InvalidKeySpecException(








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLRSAKeyFactory.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLRSAKeyFactory.java
//Synthetic comment -- index 60be4da..e8b0afa 100644

//Synthetic comment -- @@ -95,11 +95,11 @@
if (key instanceof RSAPublicKey) {
RSAPublicKey rsaKey = (RSAPublicKey) key;

            if (RSAPublicKeySpec.class.equals(keySpec)) {
BigInteger modulus = rsaKey.getModulus();
BigInteger publicExponent = rsaKey.getPublicExponent();
return (T) new RSAPublicKeySpec(modulus, publicExponent);
            } else if (X509EncodedKeySpec.class.equals(keySpec)) {
return (T) new X509EncodedKeySpec(key.getEncoded());
} else {
throw new InvalidKeySpecException("Must be RSAPublicKeySpec or X509EncodedKeySpec");
//Synthetic comment -- @@ -107,11 +107,7 @@
} else if (key instanceof RSAPrivateCrtKey) {
RSAPrivateCrtKey rsaKey = (RSAPrivateCrtKey) key;

            if (RSAPrivateKeySpec.class.equals(keySpec)) {
                BigInteger modulus = rsaKey.getModulus();
                BigInteger privateExponent = rsaKey.getPrivateExponent();
                return (T) new RSAPrivateKeySpec(modulus, privateExponent);
            } else if (RSAPrivateCrtKeySpec.class.equals(keySpec)) {
BigInteger modulus = rsaKey.getModulus();
BigInteger publicExponent = rsaKey.getPublicExponent();
BigInteger privateExponent = rsaKey.getPrivateExponent();
//Synthetic comment -- @@ -122,7 +118,11 @@
BigInteger crtCoefficient = rsaKey.getCrtCoefficient();
return (T) new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent,
primeP, primeQ, primeExponentP, primeExponentQ, crtCoefficient);
            } else if (PKCS8EncodedKeySpec.class.equals(keySpec)) {
return (T) new PKCS8EncodedKeySpec(rsaKey.getEncoded());
} else {
throw new InvalidKeySpecException(
//Synthetic comment -- @@ -131,16 +131,11 @@
} else if (key instanceof RSAPrivateKey) {
RSAPrivateKey rsaKey = (RSAPrivateKey) key;

            if (RSAPrivateKeySpec.class.equals(keySpec)) {
BigInteger modulus = rsaKey.getModulus();
BigInteger privateExponent = rsaKey.getPrivateExponent();
return (T) new RSAPrivateKeySpec(modulus, privateExponent);
            } else if (RSAPrivateCrtKeySpec.class.equals(keySpec)) {
                BigInteger modulus = rsaKey.getModulus();
                BigInteger privateExponent = rsaKey.getPrivateExponent();
                return (T) new RSAPrivateCrtKeySpec(modulus, null, privateExponent, null, null,
                        null, null, null);
            } else if (PKCS8EncodedKeySpec.class.equals(keySpec)) {
return (T) new PKCS8EncodedKeySpec(rsaKey.getEncoded());
} else {
throw new InvalidKeySpecException(







