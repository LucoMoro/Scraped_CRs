/*OpenSSL KeyFactory for DSA and EC

Add KeyFactory for EC. Uncomment the KeyFactory for DSA.

Remove useless template parameters from RSA KeyFactory.

Change-Id:Id7c4d3624719b5088abf239482ba58c7a2557d61*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECKeyFactory.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECKeyFactory.java
new file mode 100644
//Synthetic comment -- index 0000000..13b1203

//Synthetic comment -- @@ -0,0 +1,159 @@
/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.harmony.xnet.provider.jsse;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactorySpi;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class OpenSSLECKeyFactory extends KeyFactorySpi {

    @Override
    protected PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof ECPublicKeySpec) {
            ECPublicKeySpec ecKeySpec = (ECPublicKeySpec) keySpec;

            return new OpenSSLECPublicKey(ecKeySpec);
        } else if (keySpec instanceof X509EncodedKeySpec) {
            X509EncodedKeySpec x509KeySpec = (X509EncodedKeySpec) keySpec;

            try {
                final OpenSSLKey key = new OpenSSLKey(
                        NativeCrypto.d2i_PUBKEY(x509KeySpec.getEncoded()));
                return new OpenSSLECPublicKey(key);
            } catch (Exception e) {
                throw new InvalidKeySpecException(e);
            }
        }
        throw new InvalidKeySpecException("Must use ECPublicKeySpec or X509EncodedKeySpec; was "
                + keySpec.getClass().getName());
    }

    @Override
    protected PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof ECPrivateKeySpec) {
            ECPrivateKeySpec ecKeySpec = (ECPrivateKeySpec) keySpec;

            return new OpenSSLECPrivateKey(ecKeySpec);
        } else if (keySpec instanceof PKCS8EncodedKeySpec) {
            PKCS8EncodedKeySpec pkcs8KeySpec = (PKCS8EncodedKeySpec) keySpec;

            try {
                final OpenSSLKey key = new OpenSSLKey(
                        NativeCrypto.d2i_PKCS8_PRIV_KEY_INFO(pkcs8KeySpec.getEncoded()));
                return new OpenSSLECPrivateKey(key);
            } catch (Exception e) {
                throw new InvalidKeySpecException(e);
            }
        }
        throw new InvalidKeySpecException("Must use ECPublicKeySpec or PKCS8EncodedKeySpec; was "
                + keySpec.getClass().getName());
    }

    @Override
    protected <T extends KeySpec> T engineGetKeySpec(Key key, Class<T> keySpec)
            throws InvalidKeySpecException {
        if (key == null) {
            throw new InvalidKeySpecException("key == null");
        }

        if (keySpec == null) {
            throw new InvalidKeySpecException("keySpec == null");
        }

        if (key instanceof ECPublicKey) {
            ECPublicKey ecKey = (ECPublicKey) key;

            if (ECPublicKeySpec.class.equals(keySpec)) {
                ECParameterSpec params = ecKey.getParams();

                ECPoint w = ecKey.getW();

                return (T) new ECPublicKeySpec(w, params);
            } else if (PKCS8EncodedKeySpec.class.equals(keySpec)) {
                return (T) new PKCS8EncodedKeySpec(key.getEncoded());
            } else {
                throw new InvalidKeySpecException("Must be ECPublicKeySpec or PKCS8EncodedKeySpec");
            }
        } else if (key instanceof ECPrivateKey) {
            ECPrivateKey ecKey = (ECPrivateKey) key;

            if (ECPrivateKeySpec.class.equals(keySpec)) {
                ECParameterSpec params = ecKey.getParams();

                BigInteger s = ecKey.getS();

                return (T) new ECPrivateKeySpec(s, params);
            } else if (X509EncodedKeySpec.class.equals(keySpec)) {
                return (T) new X509EncodedKeySpec(ecKey.getEncoded());
            } else {
                throw new InvalidKeySpecException("Must be ECPrivateKeySpec or X509EncodedKeySpec");
            }
        } else {
            throw new InvalidKeySpecException("Must be ECPublicKey or ECPrivateKey");
        }
    }

    @Override
    protected Key engineTranslateKey(Key key) throws InvalidKeyException {
        if (key == null) {
            throw new InvalidKeyException("key == null");
        }

        if (key instanceof ECPublicKey) {
            ECPublicKey ecKey = (ECPublicKey) key;

            ECPoint w = ecKey.getW();

            ECParameterSpec params = ecKey.getParams();

            try {
                return engineGeneratePublic(new ECPublicKeySpec(w, params));
            } catch (InvalidKeySpecException e) {
                throw new InvalidKeyException(e);
            }
        } else if (key instanceof ECPrivateKey) {
            ECPrivateKey ecKey = (ECPrivateKey) key;

            BigInteger s = ecKey.getS();

            ECParameterSpec params = ecKey.getParams();

            try {
                return engineGeneratePublic(new ECPrivateKeySpec(s, params));
            } catch (InvalidKeySpecException e) {
                throw new InvalidKeyException(e);
            }
        } else {
            throw new InvalidKeyException("Key is not ECPublicKey or ECPrivateKey");
        }
    }

}








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECPrivateKey.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECPrivateKey.java
//Synthetic comment -- index f80fbeb..508354e 100644

//Synthetic comment -- @@ -25,6 +25,8 @@
import java.security.InvalidKeyException;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public final class OpenSSLECPrivateKey implements ECPrivateKey, OpenSSLKeyHolder {
//Synthetic comment -- @@ -47,6 +49,18 @@
this.key = key;
}

    public OpenSSLECPrivateKey(ECPrivateKeySpec ecKeySpec) throws InvalidKeySpecException {
        try {
            OpenSSLECGroupContext group = OpenSSLECGroupContext.getInstance(ecKeySpec
                    .getParams());
            final BigInteger privKey = ecKeySpec.getS();
            key = new OpenSSLKey(NativeCrypto.EVP_PKEY_new_EC_KEY(group.getContext(), 0,
                    privKey.toByteArray()));
        } catch (Exception e) {
            throw new InvalidKeySpecException(e);
        }
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
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public final class OpenSSLECPublicKey implements ECPublicKey, OpenSSLKeyHolder {
//Synthetic comment -- @@ -48,6 +50,18 @@
this.key = key;
}

    public OpenSSLECPublicKey(ECPublicKeySpec ecKeySpec) throws InvalidKeySpecException {
        try {
            OpenSSLECGroupContext group = OpenSSLECGroupContext.getInstance(ecKeySpec.getParams());
            OpenSSLECPointContext pubKey = OpenSSLECPointContext.getInstance(
                    NativeCrypto.get_EC_GROUP_type(group.getContext()), group, ecKeySpec.getW());
            key = new OpenSSLKey(NativeCrypto.EVP_PKEY_new_EC_KEY(group.getContext(),
                    pubKey.getContext(), null));
        } catch (Exception e) {
            throw new InvalidKeySpecException(e);
        }
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

        put("KeyFactory.DSA", OpenSSLDSAKeyFactory.class.getName());

        put("KeyFactory.EC", OpenSSLECKeyFactory.class.getName());

/* == Signatures == */
put("Signature.MD5WithRSA", OpenSSLSignature.MD5RSA.class.getName());








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLRSAKeyFactory.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLRSAKeyFactory.java
//Synthetic comment -- index 49d31d3..60be4da 100644

//Synthetic comment -- @@ -33,7 +33,7 @@
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class OpenSSLRSAKeyFactory extends KeyFactorySpi {

@Override
protected PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {







