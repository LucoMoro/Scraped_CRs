/*Add serialization to OpenSSL-based keys

Any OpenSSL keys that aren't ENGINE-based are serializable, so add the
code to be able to keep the Serializable contract.

Bug:http://code.google.com/p/android/issues/detail?id=37880Change-Id:I6d5fd9a1c6817d97d7890e4cccd8c95253e95279*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLDSAPrivateKey.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLDSAPrivateKey.java
//Synthetic comment -- index 761b08e..54e5d08 100644

//Synthetic comment -- @@ -16,6 +16,10 @@

package org.apache.harmony.xnet.provider.jsse;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.interfaces.DSAParams;
//Synthetic comment -- @@ -26,9 +30,9 @@
public class OpenSSLDSAPrivateKey implements DSAPrivateKey {
private static final long serialVersionUID = 6524734576187424628L;

    private final OpenSSLKey key;

    private OpenSSLDSAParams params;

OpenSSLDSAPrivateKey(OpenSSLKey key) {
this.key = key;
//Synthetic comment -- @@ -200,4 +204,33 @@

return sb.toString();
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLDSAPublicKey.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLDSAPublicKey.java
//Synthetic comment -- index 25869bb..84e0d36 100644

//Synthetic comment -- @@ -16,6 +16,10 @@

package org.apache.harmony.xnet.provider.jsse;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.interfaces.DSAParams;
//Synthetic comment -- @@ -26,9 +30,9 @@
public class OpenSSLDSAPublicKey implements DSAPublicKey {
private static final long serialVersionUID = 5238609500353792232L;

    private final OpenSSLKey key;

    private OpenSSLDSAParams params;

OpenSSLDSAPublicKey(OpenSSLKey key) {
this.key = key;
//Synthetic comment -- @@ -147,4 +151,33 @@

return sb.toString();
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLRSAPrivateCrtKey.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLRSAPrivateCrtKey.java
//Synthetic comment -- index 48e1494..f1e12c2 100644

//Synthetic comment -- @@ -16,6 +16,10 @@

package org.apache.harmony.xnet.provider.jsse;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.interfaces.RSAPrivateCrtKey;
//Synthetic comment -- @@ -24,6 +28,8 @@
import java.security.spec.RSAPrivateCrtKeySpec;

public class OpenSSLRSAPrivateCrtKey extends OpenSSLRSAPrivateKey implements RSAPrivateCrtKey {
private BigInteger publicExponent;

private BigInteger primeP;
//Synthetic comment -- @@ -298,4 +304,27 @@

return sb.toString();
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLRSAPrivateKey.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLRSAPrivateKey.java
//Synthetic comment -- index 5c2f075..fa455c7 100644

//Synthetic comment -- @@ -16,6 +16,10 @@

package org.apache.harmony.xnet.provider.jsse;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.interfaces.RSAPrivateKey;
//Synthetic comment -- @@ -25,13 +29,13 @@
public class OpenSSLRSAPrivateKey implements RSAPrivateKey {
private static final long serialVersionUID = 4872170254439578735L;

    private final OpenSSLKey key;

    private boolean fetchedParams;

    private BigInteger modulus;

    private BigInteger privateExponent;

OpenSSLRSAPrivateKey(OpenSSLKey key) {
this.key = key;
//Synthetic comment -- @@ -259,4 +263,27 @@

return sb.toString();
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLRSAPublicKey.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLRSAPublicKey.java
//Synthetic comment -- index 1613cf0..4959a16 100644

//Synthetic comment -- @@ -16,6 +16,9 @@

package org.apache.harmony.xnet.provider.jsse;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.interfaces.RSAPublicKey;
//Synthetic comment -- @@ -25,13 +28,13 @@
public class OpenSSLRSAPublicKey implements RSAPublicKey {
private static final long serialVersionUID = 123125005824688292L;

    private final OpenSSLKey key;

private BigInteger publicExponent;

private BigInteger modulus;

    private boolean fetchedParams;

OpenSSLRSAPublicKey(OpenSSLKey key) {
this.key = key;
//Synthetic comment -- @@ -162,4 +165,24 @@

return sb.toString();
}
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/KeyPairGeneratorTest.java b/luni/src/test/java/libcore/java/security/KeyPairGeneratorTest.java
//Synthetic comment -- index cc90147..93b58df 100644

//Synthetic comment -- @@ -16,6 +16,10 @@

package libcore.java.security;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
//Synthetic comment -- @@ -149,6 +153,19 @@
assertNotNull(k.getEncoded());
assertNotNull(k.getFormat());

test_KeyWithAllKeyFactories(k);
}








