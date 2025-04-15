/*NativeCrypto: fix some DSA/ECDSA key generation

We were trying to generate a public key from a private key spec which
obviously doesn't work.

Also fix the error messages that indicated public key when it meant
private key.

Change-Id:Ifae417bc3e4c56aced5b7583a34965c7f31c9c66*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLDSAKeyFactory.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLDSAKeyFactory.java
//Synthetic comment -- index e3f6ed5..12e3a77 100644

//Synthetic comment -- @@ -72,7 +72,7 @@
throw new InvalidKeySpecException(e);
}
}
        throw new InvalidKeySpecException("Must use DSAPrivateKeySpec or PKCS8EncodedKeySpec; was "
+ keySpec.getClass().getName());
}

//Synthetic comment -- @@ -158,7 +158,7 @@
BigInteger g = params.getG();

try {
                return engineGeneratePrivate(new DSAPrivateKeySpec(x, p, q, g));
} catch (InvalidKeySpecException e) {
throw new InvalidKeyException(e);
}








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECKeyFactory.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECKeyFactory.java
//Synthetic comment -- index 13b1203..f4b4dbb 100644

//Synthetic comment -- @@ -73,7 +73,7 @@
throw new InvalidKeySpecException(e);
}
}
        throw new InvalidKeySpecException("Must use ECPrivateKeySpec or PKCS8EncodedKeySpec; was "
+ keySpec.getClass().getName());
}

//Synthetic comment -- @@ -147,7 +147,7 @@
ECParameterSpec params = ecKey.getParams();

try {
                return engineGeneratePrivate(new ECPrivateKeySpec(s, params));
} catch (InvalidKeySpecException e) {
throw new InvalidKeyException(e);
}







