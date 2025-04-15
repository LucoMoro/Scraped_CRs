/*OpenSSL KeyFactory for DSA and EC

Add KeyFactory for EC. Uncomment the KeyFactory for DSA.

Remove useless template parameters from RSA KeyFactory.

Change-Id:Id7c4d3624719b5088abf239482ba58c7a2557d61*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECKeyFactory.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECKeyFactory.java
new file mode 100644
//Synthetic comment -- index 0000000..13b1203

//Synthetic comment -- @@ -0,0 +1,159 @@








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECPrivateKey.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECPrivateKey.java
//Synthetic comment -- index f80fbeb..508354e 100644

//Synthetic comment -- @@ -25,6 +25,8 @@
import java.security.InvalidKeyException;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.ECParameterSpec;
import java.util.Arrays;

public final class OpenSSLECPrivateKey implements ECPrivateKey, OpenSSLKeyHolder {
//Synthetic comment -- @@ -47,6 +49,18 @@
this.key = key;
}

public static OpenSSLKey getInstance(ECPrivateKey ecPrivateKey) throws InvalidKeyException {
try {
OpenSSLECGroupContext group = OpenSSLECGroupContext.getInstance(ecPrivateKey








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECPublicKey.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECPublicKey.java
//Synthetic comment -- index 118df67..951ee0b 100644

//Synthetic comment -- @@ -26,6 +26,8 @@
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.util.Arrays;

public final class OpenSSLECPublicKey implements ECPublicKey, OpenSSLKeyHolder {
//Synthetic comment -- @@ -48,6 +50,18 @@
this.key = key;
}

public static OpenSSLKey getInstance(ECPublicKey ecPublicKey) throws InvalidKeyException {
try {
OpenSSLECGroupContext group = OpenSSLECGroupContext








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLProvider.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLProvider.java
//Synthetic comment -- index 8aab3bb..1c6a86e 100644

//Synthetic comment -- @@ -79,11 +79,12 @@
put("KeyPairGenerator.EC", OpenSSLECKeyPairGenerator.class.getName());

/* == KeyFactory == */

put("KeyFactory.RSA", OpenSSLRSAKeyFactory.class.getName());
put("Alg.Alias.KeyFactory.1.2.840.113549.1.1.1", "RSA");

        // put("KeyFactory.DSA", OpenSSLDSAKeyFactory.class.getName());

/* == Signatures == */
put("Signature.MD5WithRSA", OpenSSLSignature.MD5RSA.class.getName());








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLRSAKeyFactory.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLRSAKeyFactory.java
//Synthetic comment -- index 49d31d3..60be4da 100644

//Synthetic comment -- @@ -33,7 +33,7 @@
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class OpenSSLRSAKeyFactory<T, S> extends KeyFactorySpi {

@Override
protected PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {







