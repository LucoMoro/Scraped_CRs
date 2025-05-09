/*Add raw RSA Cipher to OpenSSLProvider

Recent changes in the way that Android Keystore (accessed via KeyChain)
necessitate all key operations be done with a provider that understands
the new OpenSSLKey object.

This adds Cipher support for the RSA algorithm in "RSA/ECB/NoPadding"
and "RSA/None/NoPadding" modes.

Change-Id:I98a8eaf3514763a863b2751bba999fbd48609c96*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLCipherRawRSA.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLCipherRawRSA.java
new file mode 100644
//Synthetic comment -- index 0000000..8312013

//Synthetic comment -- @@ -0,0 +1,227 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

public class OpenSSLCipherRawRSA extends CipherSpi {
    /**
     * An empty list that is returned from update calls.
     */
    private static final byte[] emptyList = new byte[0];

    /**
     * The current OpenSSL key we're operating on.
     */
    private OpenSSLKey key;

    /**
     * Current cipher mode: encrypting or decrypting.
     */
    private boolean usingPrivateKey;

    /**
     * Buffer for operations
     */
    private byte[] buffer;

    /**
     * Current offset in the buffer.
     */
    private int bufferOffset;

    /**
     * Flag that indicates an exception should be thrown when the input is too
     * large during doFinal.
     */
    private boolean inputTooLarge;

    @Override
    protected void engineSetMode(String mode) throws NoSuchAlgorithmException {
        final String modeUpper = mode.toUpperCase();
        if ("NONE".equals(modeUpper) || "ECB".equals(modeUpper)) {
            return;
        }

        throw new NoSuchAlgorithmException("mode not supported: " + mode);
    }

    @Override
    protected void engineSetPadding(String padding) throws NoSuchPaddingException {
        final String paddingUpper = padding.toUpperCase();
        if ("NOPADDING".equals(paddingUpper)) {
            return;
        }

        throw new NoSuchPaddingException("padding not supported: " + padding);
    }

    @Override
    protected int engineGetBlockSize() {
        return 0;
    }

    @Override
    protected int engineGetOutputSize(int inputLen) {
        if (key == null) {
            throw new IllegalStateException("cipher is not initialized");
        }

        return buffer.length;
    }

    @Override
    protected byte[] engineGetIV() {
        return null;
    }

    @Override
    protected AlgorithmParameters engineGetParameters() {
        return null;
    }

    private void engineInitInternal(int opmode, Key key) throws InvalidKeyException {
        if (key instanceof OpenSSLRSAPrivateKey) {
            OpenSSLRSAPrivateKey rsaPrivateKey = (OpenSSLRSAPrivateKey) key;
            usingPrivateKey = true;
            this.key = rsaPrivateKey.getOpenSSLKey();
        } else if (key instanceof RSAPrivateCrtKey) {
            RSAPrivateCrtKey rsaPrivateKey = (RSAPrivateCrtKey) key;
            usingPrivateKey = true;
            this.key = OpenSSLRSAPrivateCrtKey.getInstance(rsaPrivateKey);
        } else if (key instanceof RSAPrivateKey) {
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) key;
            usingPrivateKey = true;
            this.key = OpenSSLRSAPrivateKey.getInstance(rsaPrivateKey);
        } else if (key instanceof OpenSSLRSAPublicKey) {
            OpenSSLRSAPublicKey rsaPublicKey = (OpenSSLRSAPublicKey) key;
            usingPrivateKey = false;
            this.key = rsaPublicKey.getOpenSSLKey();
        } else if (key instanceof RSAPublicKey) {
            RSAPublicKey rsaPublicKey = (RSAPublicKey) key;
            usingPrivateKey = false;
            this.key = OpenSSLRSAPublicKey.getInstance(rsaPublicKey);
        } else {
            throw new InvalidKeyException("Need RSA private or public key");
        }

        buffer = new byte[NativeCrypto.RSA_size(this.key.getPkeyContext())];
        inputTooLarge = false;
    }

    @Override
    protected void engineInit(int opmode, Key key, SecureRandom random) throws InvalidKeyException {
        engineInitInternal(opmode, key);
    }

    @Override
    protected void engineInit(int opmode, Key key, AlgorithmParameterSpec params,
            SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (params != null) {
            throw new InvalidAlgorithmParameterException("unknown param type: "
                    + params.getClass().getName());
        }

        engineInitInternal(opmode, key);
    }

    @Override
    protected void engineInit(int opmode, Key key, AlgorithmParameters params, SecureRandom random)
            throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (params != null) {
            throw new InvalidAlgorithmParameterException("unknown param type: "
                    + params.getClass().getName());
        }

        engineInitInternal(opmode, key);
    }

    @Override
    protected byte[] engineUpdate(byte[] input, int inputOffset, int inputLen) {
        if (bufferOffset + inputLen > buffer.length) {
            inputTooLarge = true;
            return emptyList;
        }

        System.arraycopy(input, inputOffset, buffer, bufferOffset, inputLen);
        bufferOffset += inputLen;
        return emptyList;
    }

    @Override
    protected int engineUpdate(byte[] input, int inputOffset, int inputLen, byte[] output,
            int outputOffset) throws ShortBufferException {
        engineUpdate(input, inputOffset, inputLen);
        return 0;
    }

    @Override
    protected byte[] engineDoFinal(byte[] input, int inputOffset, int inputLen)
            throws IllegalBlockSizeException, BadPaddingException {
        if (input != null) {
            engineUpdate(input, inputOffset, inputLen);
        }

        if (inputTooLarge) {
            throw new IllegalBlockSizeException("input must be under " + buffer.length + " bytes");
        }

        final byte[] tmpBuf;
        if (bufferOffset != buffer.length) {
            tmpBuf = new byte[buffer.length];
            System.arraycopy(buffer, 0, tmpBuf, buffer.length - bufferOffset, bufferOffset);
        } else {
            tmpBuf = buffer;
        }

        final byte[] output = new byte[buffer.length];
        if (usingPrivateKey) {
            NativeCrypto.RSA_private_encrypt(tmpBuf.length, tmpBuf, output, key.getPkeyContext(),
                    NativeCrypto.RSA_NO_PADDING);
        } else {
            NativeCrypto.RSA_public_decrypt(tmpBuf.length, tmpBuf, output, key.getPkeyContext(),
                    NativeCrypto.RSA_NO_PADDING);
        }

        bufferOffset = 0;
        return output;
    }

    @Override
    protected int engineDoFinal(byte[] input, int inputOffset, int inputLen, byte[] output,
            int outputOffset) throws ShortBufferException, IllegalBlockSizeException,
            BadPaddingException {
        byte[] b = engineDoFinal(input, inputOffset, inputLen);
        System.arraycopy(output, 0, b, outputOffset, b.length);
        return b.length;
    }
}








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLProvider.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLProvider.java
//Synthetic comment -- index efb9387..e13ccda 100644

//Synthetic comment -- @@ -120,5 +120,9 @@
*/
put("SecureRandom.SHA1PRNG", OpenSSLRandom.class.getName());
put("SecureRandom.SHA1PRNG ImplementedIn", "Software");

        // Cipher
        put("Cipher.RSA/ECB/NoPadding", OpenSSLCipherRawRSA.class.getName());
        put("Alg.Alias.Cipher.RSA/None/NoPadding", "RSA/ECB/NoPadding");
}
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/crypto/CipherTest.java b/luni/src/test/java/libcore/javax/crypto/CipherTest.java
//Synthetic comment -- index 7919b35..a94a9d0 100644

//Synthetic comment -- @@ -17,9 +17,32 @@
package libcore.javax.crypto;

import com.android.org.bouncycastle.asn1.x509.KeyUsage;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import junit.framework.TestCase;
import libcore.java.security.TestKeyStore;

//Synthetic comment -- @@ -108,4 +131,716 @@
.build()
.getPrivateKey("RSA", "RSA").getCertificate();
}

    /*
     * Test vectors generated with this private key:
     *
     * -----BEGIN RSA PRIVATE KEY-----
     * MIIEpAIBAAKCAQEA4Ec+irjyKE/rnnQv+XSPoRjtmGM8kvUq63ouvg075gMpvnZq
     * 0Q62pRXQ0s/ZvqeTDwwwZTeJn3lYzT6FsB+IGFJNMSWEqUslHjYltUFB7b/uGYgI
     * 4buX/Hy0m56qr2jpyY19DtxTu8D6ADQ1bWMF+7zDxwAUBThqu8hzyw8+90JfPTPf
     * ezFa4DbSoLZq/UdQOxab8247UWJRW3Ff2oPeryxYrrmr+zCXw8yd2dvl7ylsF2E5
     * Ao6KZx5jBW1F9AGI0sQTNJCEXeUsJTTpxrJHjAe9rpKII7YtBmx3cPn2Pz26JH9T
     * CER0e+eqqF2FO4vSRKzsPePImrRkU6tNJMOsaQIDAQABAoIBADd4R3al8XaY9ayW
     * DfuDobZ1ZOZIvQWXz4q4CHGG8macJ6nsvdSA8Bl6gNBzCebGqW+SUzHlf4tKxvTU
     * XtpFojJpwJ/EKMB6Tm7fc4oV3sl/q9Lyu0ehTyDqcvz+TDbgGtp3vRN82NTaELsW
     * LpSkZilx8XX5hfoYjwVsuX7igW9Dq503R2Ekhs2owWGWwwgYqZXshdOEZ3kSZ7O/
     * IfJzcQppJYYldoQcW2cSwS1L0govMpmtt8E12l6VFavadufK8qO+gFUdBzt4vxFi
     * xIrSt/R0OgI47k0lL31efmUzzK5kzLOTYAdaL9HgNOw65c6cQIzL8OJeQRQCFoez
     * 3UdUroECgYEA9UGIS8Nzeyki1BGe9F4t7izUy7dfRVBaFXqlAJ+Zxzot8HJKxGAk
     * MGMy6omBd2NFRl3G3x4KbxQK/ztzluaomUrF2qloc0cv43dJ0U6z4HXmKdvrNYMz
     * im82SdCiZUp6Qv2atr+krE1IHTkLsimwZL3DEcwb4bYxidp8QM3s8rECgYEA6hp0
     * LduIHO23KIyH442GjdekCdFaQ/RF1Td6C1cx3b/KLa8oqOE81cCvzsM0fXSjniNa
     * PNljPydN4rlPkt9DgzkR2enxz1jyfeLgj/RZZMcg0+whOdx8r8kSlTzeyy81Wi4s
     * NaUPrXVMs7IxZkJLo7bjESoriYw4xcFe2yOGkzkCgYBRgo8exv2ZYCmQG68dfjN7
     * pfCvJ+mE6tiVrOYr199O5FoiQInyzBUa880XP84EdLywTzhqLNzA4ANrokGfVFeS
     * YtRxAL6TGYSj76Bb7PFBV03AebOpXEqD5sQ/MhTW3zLVEt4ZgIXlMeYWuD/X3Z0f
     * TiYHwzM9B8VdEH0dOJNYcQKBgQDbT7UPUN6O21P/NMgJMYigUShn2izKBIl3WeWH
     * wkQBDa+GZNWegIPRbBZHiTAfZ6nweAYNg0oq29NnV1toqKhCwrAqibPzH8zsiiL+
     * OVeVxcbHQitOXXSh6ajzDndZufwtY5wfFWc+hOk6XvFQb0MVODw41Fy9GxQEj0ch
     * 3IIyYQKBgQDYEUWTr0FfthLb8ZI3ENVNB0hiBadqO0MZSWjA3/HxHvD2GkozfV/T
     * dBu8lkDkR7i2tsR8OsEgQ1fTsMVbqShr2nP2KSlvX6kUbYl2NX08dR51FIaWpAt0
     * aFyCzjCQLWOdck/yTV4ulAfuNO3tLjtN9lqpvP623yjQe6aQPxZXaA==
     * -----END RSA PRIVATE KEY-----
     *
     */

    private static final BigInteger RSA_2048_modulus = new BigInteger(new byte[] {
        (byte) 0x00, (byte) 0xe0, (byte) 0x47, (byte) 0x3e, (byte) 0x8a, (byte) 0xb8, (byte) 0xf2, (byte) 0x28,
        (byte) 0x4f, (byte) 0xeb, (byte) 0x9e, (byte) 0x74, (byte) 0x2f, (byte) 0xf9, (byte) 0x74, (byte) 0x8f,
        (byte) 0xa1, (byte) 0x18, (byte) 0xed, (byte) 0x98, (byte) 0x63, (byte) 0x3c, (byte) 0x92, (byte) 0xf5,
        (byte) 0x2a, (byte) 0xeb, (byte) 0x7a, (byte) 0x2e, (byte) 0xbe, (byte) 0x0d, (byte) 0x3b, (byte) 0xe6,
        (byte) 0x03, (byte) 0x29, (byte) 0xbe, (byte) 0x76, (byte) 0x6a, (byte) 0xd1, (byte) 0x0e, (byte) 0xb6,
        (byte) 0xa5, (byte) 0x15, (byte) 0xd0, (byte) 0xd2, (byte) 0xcf, (byte) 0xd9, (byte) 0xbe, (byte) 0xa7,
        (byte) 0x93, (byte) 0x0f, (byte) 0x0c, (byte) 0x30, (byte) 0x65, (byte) 0x37, (byte) 0x89, (byte) 0x9f,
        (byte) 0x79, (byte) 0x58, (byte) 0xcd, (byte) 0x3e, (byte) 0x85, (byte) 0xb0, (byte) 0x1f, (byte) 0x88,
        (byte) 0x18, (byte) 0x52, (byte) 0x4d, (byte) 0x31, (byte) 0x25, (byte) 0x84, (byte) 0xa9, (byte) 0x4b,
        (byte) 0x25, (byte) 0x1e, (byte) 0x36, (byte) 0x25, (byte) 0xb5, (byte) 0x41, (byte) 0x41, (byte) 0xed,
        (byte) 0xbf, (byte) 0xee, (byte) 0x19, (byte) 0x88, (byte) 0x08, (byte) 0xe1, (byte) 0xbb, (byte) 0x97,
        (byte) 0xfc, (byte) 0x7c, (byte) 0xb4, (byte) 0x9b, (byte) 0x9e, (byte) 0xaa, (byte) 0xaf, (byte) 0x68,
        (byte) 0xe9, (byte) 0xc9, (byte) 0x8d, (byte) 0x7d, (byte) 0x0e, (byte) 0xdc, (byte) 0x53, (byte) 0xbb,
        (byte) 0xc0, (byte) 0xfa, (byte) 0x00, (byte) 0x34, (byte) 0x35, (byte) 0x6d, (byte) 0x63, (byte) 0x05,
        (byte) 0xfb, (byte) 0xbc, (byte) 0xc3, (byte) 0xc7, (byte) 0x00, (byte) 0x14, (byte) 0x05, (byte) 0x38,
        (byte) 0x6a, (byte) 0xbb, (byte) 0xc8, (byte) 0x73, (byte) 0xcb, (byte) 0x0f, (byte) 0x3e, (byte) 0xf7,
        (byte) 0x42, (byte) 0x5f, (byte) 0x3d, (byte) 0x33, (byte) 0xdf, (byte) 0x7b, (byte) 0x31, (byte) 0x5a,
        (byte) 0xe0, (byte) 0x36, (byte) 0xd2, (byte) 0xa0, (byte) 0xb6, (byte) 0x6a, (byte) 0xfd, (byte) 0x47,
        (byte) 0x50, (byte) 0x3b, (byte) 0x16, (byte) 0x9b, (byte) 0xf3, (byte) 0x6e, (byte) 0x3b, (byte) 0x51,
        (byte) 0x62, (byte) 0x51, (byte) 0x5b, (byte) 0x71, (byte) 0x5f, (byte) 0xda, (byte) 0x83, (byte) 0xde,
        (byte) 0xaf, (byte) 0x2c, (byte) 0x58, (byte) 0xae, (byte) 0xb9, (byte) 0xab, (byte) 0xfb, (byte) 0x30,
        (byte) 0x97, (byte) 0xc3, (byte) 0xcc, (byte) 0x9d, (byte) 0xd9, (byte) 0xdb, (byte) 0xe5, (byte) 0xef,
        (byte) 0x29, (byte) 0x6c, (byte) 0x17, (byte) 0x61, (byte) 0x39, (byte) 0x02, (byte) 0x8e, (byte) 0x8a,
        (byte) 0x67, (byte) 0x1e, (byte) 0x63, (byte) 0x05, (byte) 0x6d, (byte) 0x45, (byte) 0xf4, (byte) 0x01,
        (byte) 0x88, (byte) 0xd2, (byte) 0xc4, (byte) 0x13, (byte) 0x34, (byte) 0x90, (byte) 0x84, (byte) 0x5d,
        (byte) 0xe5, (byte) 0x2c, (byte) 0x25, (byte) 0x34, (byte) 0xe9, (byte) 0xc6, (byte) 0xb2, (byte) 0x47,
        (byte) 0x8c, (byte) 0x07, (byte) 0xbd, (byte) 0xae, (byte) 0x92, (byte) 0x88, (byte) 0x23, (byte) 0xb6,
        (byte) 0x2d, (byte) 0x06, (byte) 0x6c, (byte) 0x77, (byte) 0x70, (byte) 0xf9, (byte) 0xf6, (byte) 0x3f,
        (byte) 0x3d, (byte) 0xba, (byte) 0x24, (byte) 0x7f, (byte) 0x53, (byte) 0x08, (byte) 0x44, (byte) 0x74,
        (byte) 0x7b, (byte) 0xe7, (byte) 0xaa, (byte) 0xa8, (byte) 0x5d, (byte) 0x85, (byte) 0x3b, (byte) 0x8b,
        (byte) 0xd2, (byte) 0x44, (byte) 0xac, (byte) 0xec, (byte) 0x3d, (byte) 0xe3, (byte) 0xc8, (byte) 0x9a,
        (byte) 0xb4, (byte) 0x64, (byte) 0x53, (byte) 0xab, (byte) 0x4d, (byte) 0x24, (byte) 0xc3, (byte) 0xac,
        (byte) 0x69,
    });

    private static final BigInteger RSA_2048_privateExponent = new BigInteger(new byte[] {
        (byte) 0x37, (byte) 0x78, (byte) 0x47, (byte) 0x76, (byte) 0xa5, (byte) 0xf1, (byte) 0x76, (byte) 0x98,
        (byte) 0xf5, (byte) 0xac, (byte) 0x96, (byte) 0x0d, (byte) 0xfb, (byte) 0x83, (byte) 0xa1, (byte) 0xb6,
        (byte) 0x75, (byte) 0x64, (byte) 0xe6, (byte) 0x48, (byte) 0xbd, (byte) 0x05, (byte) 0x97, (byte) 0xcf,
        (byte) 0x8a, (byte) 0xb8, (byte) 0x08, (byte) 0x71, (byte) 0x86, (byte) 0xf2, (byte) 0x66, (byte) 0x9c,
        (byte) 0x27, (byte) 0xa9, (byte) 0xec, (byte) 0xbd, (byte) 0xd4, (byte) 0x80, (byte) 0xf0, (byte) 0x19,
        (byte) 0x7a, (byte) 0x80, (byte) 0xd0, (byte) 0x73, (byte) 0x09, (byte) 0xe6, (byte) 0xc6, (byte) 0xa9,
        (byte) 0x6f, (byte) 0x92, (byte) 0x53, (byte) 0x31, (byte) 0xe5, (byte) 0x7f, (byte) 0x8b, (byte) 0x4a,
        (byte) 0xc6, (byte) 0xf4, (byte) 0xd4, (byte) 0x5e, (byte) 0xda, (byte) 0x45, (byte) 0xa2, (byte) 0x32,
        (byte) 0x69, (byte) 0xc0, (byte) 0x9f, (byte) 0xc4, (byte) 0x28, (byte) 0xc0, (byte) 0x7a, (byte) 0x4e,
        (byte) 0x6e, (byte) 0xdf, (byte) 0x73, (byte) 0x8a, (byte) 0x15, (byte) 0xde, (byte) 0xc9, (byte) 0x7f,
        (byte) 0xab, (byte) 0xd2, (byte) 0xf2, (byte) 0xbb, (byte) 0x47, (byte) 0xa1, (byte) 0x4f, (byte) 0x20,
        (byte) 0xea, (byte) 0x72, (byte) 0xfc, (byte) 0xfe, (byte) 0x4c, (byte) 0x36, (byte) 0xe0, (byte) 0x1a,
        (byte) 0xda, (byte) 0x77, (byte) 0xbd, (byte) 0x13, (byte) 0x7c, (byte) 0xd8, (byte) 0xd4, (byte) 0xda,
        (byte) 0x10, (byte) 0xbb, (byte) 0x16, (byte) 0x2e, (byte) 0x94, (byte) 0xa4, (byte) 0x66, (byte) 0x29,
        (byte) 0x71, (byte) 0xf1, (byte) 0x75, (byte) 0xf9, (byte) 0x85, (byte) 0xfa, (byte) 0x18, (byte) 0x8f,
        (byte) 0x05, (byte) 0x6c, (byte) 0xb9, (byte) 0x7e, (byte) 0xe2, (byte) 0x81, (byte) 0x6f, (byte) 0x43,
        (byte) 0xab, (byte) 0x9d, (byte) 0x37, (byte) 0x47, (byte) 0x61, (byte) 0x24, (byte) 0x86, (byte) 0xcd,
        (byte) 0xa8, (byte) 0xc1, (byte) 0x61, (byte) 0x96, (byte) 0xc3, (byte) 0x08, (byte) 0x18, (byte) 0xa9,
        (byte) 0x95, (byte) 0xec, (byte) 0x85, (byte) 0xd3, (byte) 0x84, (byte) 0x67, (byte) 0x79, (byte) 0x12,
        (byte) 0x67, (byte) 0xb3, (byte) 0xbf, (byte) 0x21, (byte) 0xf2, (byte) 0x73, (byte) 0x71, (byte) 0x0a,
        (byte) 0x69, (byte) 0x25, (byte) 0x86, (byte) 0x25, (byte) 0x76, (byte) 0x84, (byte) 0x1c, (byte) 0x5b,
        (byte) 0x67, (byte) 0x12, (byte) 0xc1, (byte) 0x2d, (byte) 0x4b, (byte) 0xd2, (byte) 0x0a, (byte) 0x2f,
        (byte) 0x32, (byte) 0x99, (byte) 0xad, (byte) 0xb7, (byte) 0xc1, (byte) 0x35, (byte) 0xda, (byte) 0x5e,
        (byte) 0x95, (byte) 0x15, (byte) 0xab, (byte) 0xda, (byte) 0x76, (byte) 0xe7, (byte) 0xca, (byte) 0xf2,
        (byte) 0xa3, (byte) 0xbe, (byte) 0x80, (byte) 0x55, (byte) 0x1d, (byte) 0x07, (byte) 0x3b, (byte) 0x78,
        (byte) 0xbf, (byte) 0x11, (byte) 0x62, (byte) 0xc4, (byte) 0x8a, (byte) 0xd2, (byte) 0xb7, (byte) 0xf4,
        (byte) 0x74, (byte) 0x3a, (byte) 0x02, (byte) 0x38, (byte) 0xee, (byte) 0x4d, (byte) 0x25, (byte) 0x2f,
        (byte) 0x7d, (byte) 0x5e, (byte) 0x7e, (byte) 0x65, (byte) 0x33, (byte) 0xcc, (byte) 0xae, (byte) 0x64,
        (byte) 0xcc, (byte) 0xb3, (byte) 0x93, (byte) 0x60, (byte) 0x07, (byte) 0x5a, (byte) 0x2f, (byte) 0xd1,
        (byte) 0xe0, (byte) 0x34, (byte) 0xec, (byte) 0x3a, (byte) 0xe5, (byte) 0xce, (byte) 0x9c, (byte) 0x40,
        (byte) 0x8c, (byte) 0xcb, (byte) 0xf0, (byte) 0xe2, (byte) 0x5e, (byte) 0x41, (byte) 0x14, (byte) 0x02,
        (byte) 0x16, (byte) 0x87, (byte) 0xb3, (byte) 0xdd, (byte) 0x47, (byte) 0x54, (byte) 0xae, (byte) 0x81,
    });

    private static final BigInteger RSA_2048_publicExponent = new BigInteger(new byte[] {
        (byte) 0x01, (byte) 0x00, (byte) 0x01,
    });

    private static final BigInteger RSA_2048_primeP = new BigInteger(new byte[] {
        (byte) 0x00, (byte) 0xf5, (byte) 0x41, (byte) 0x88, (byte) 0x4b, (byte) 0xc3, (byte) 0x73, (byte) 0x7b,
        (byte) 0x29, (byte) 0x22, (byte) 0xd4, (byte) 0x11, (byte) 0x9e, (byte) 0xf4, (byte) 0x5e, (byte) 0x2d,
        (byte) 0xee, (byte) 0x2c, (byte) 0xd4, (byte) 0xcb, (byte) 0xb7, (byte) 0x5f, (byte) 0x45, (byte) 0x50,
        (byte) 0x5a, (byte) 0x15, (byte) 0x7a, (byte) 0xa5, (byte) 0x00, (byte) 0x9f, (byte) 0x99, (byte) 0xc7,
        (byte) 0x3a, (byte) 0x2d, (byte) 0xf0, (byte) 0x72, (byte) 0x4a, (byte) 0xc4, (byte) 0x60, (byte) 0x24,
        (byte) 0x30, (byte) 0x63, (byte) 0x32, (byte) 0xea, (byte) 0x89, (byte) 0x81, (byte) 0x77, (byte) 0x63,
        (byte) 0x45, (byte) 0x46, (byte) 0x5d, (byte) 0xc6, (byte) 0xdf, (byte) 0x1e, (byte) 0x0a, (byte) 0x6f,
        (byte) 0x14, (byte) 0x0a, (byte) 0xff, (byte) 0x3b, (byte) 0x73, (byte) 0x96, (byte) 0xe6, (byte) 0xa8,
        (byte) 0x99, (byte) 0x4a, (byte) 0xc5, (byte) 0xda, (byte) 0xa9, (byte) 0x68, (byte) 0x73, (byte) 0x47,
        (byte) 0x2f, (byte) 0xe3, (byte) 0x77, (byte) 0x49, (byte) 0xd1, (byte) 0x4e, (byte) 0xb3, (byte) 0xe0,
        (byte) 0x75, (byte) 0xe6, (byte) 0x29, (byte) 0xdb, (byte) 0xeb, (byte) 0x35, (byte) 0x83, (byte) 0x33,
        (byte) 0x8a, (byte) 0x6f, (byte) 0x36, (byte) 0x49, (byte) 0xd0, (byte) 0xa2, (byte) 0x65, (byte) 0x4a,
        (byte) 0x7a, (byte) 0x42, (byte) 0xfd, (byte) 0x9a, (byte) 0xb6, (byte) 0xbf, (byte) 0xa4, (byte) 0xac,
        (byte) 0x4d, (byte) 0x48, (byte) 0x1d, (byte) 0x39, (byte) 0x0b, (byte) 0xb2, (byte) 0x29, (byte) 0xb0,
        (byte) 0x64, (byte) 0xbd, (byte) 0xc3, (byte) 0x11, (byte) 0xcc, (byte) 0x1b, (byte) 0xe1, (byte) 0xb6,
        (byte) 0x31, (byte) 0x89, (byte) 0xda, (byte) 0x7c, (byte) 0x40, (byte) 0xcd, (byte) 0xec, (byte) 0xf2,
        (byte) 0xb1,
    });

    private static final BigInteger RSA_2048_primeQ = new BigInteger(new byte[] {
        (byte) 0x00, (byte) 0xea, (byte) 0x1a, (byte) 0x74, (byte) 0x2d, (byte) 0xdb, (byte) 0x88, (byte) 0x1c,
        (byte) 0xed, (byte) 0xb7, (byte) 0x28, (byte) 0x8c, (byte) 0x87, (byte) 0xe3, (byte) 0x8d, (byte) 0x86,
        (byte) 0x8d, (byte) 0xd7, (byte) 0xa4, (byte) 0x09, (byte) 0xd1, (byte) 0x5a, (byte) 0x43, (byte) 0xf4,
        (byte) 0x45, (byte) 0xd5, (byte) 0x37, (byte) 0x7a, (byte) 0x0b, (byte) 0x57, (byte) 0x31, (byte) 0xdd,
        (byte) 0xbf, (byte) 0xca, (byte) 0x2d, (byte) 0xaf, (byte) 0x28, (byte) 0xa8, (byte) 0xe1, (byte) 0x3c,
        (byte) 0xd5, (byte) 0xc0, (byte) 0xaf, (byte) 0xce, (byte) 0xc3, (byte) 0x34, (byte) 0x7d, (byte) 0x74,
        (byte) 0xa3, (byte) 0x9e, (byte) 0x23, (byte) 0x5a, (byte) 0x3c, (byte) 0xd9, (byte) 0x63, (byte) 0x3f,
        (byte) 0x27, (byte) 0x4d, (byte) 0xe2, (byte) 0xb9, (byte) 0x4f, (byte) 0x92, (byte) 0xdf, (byte) 0x43,
        (byte) 0x83, (byte) 0x39, (byte) 0x11, (byte) 0xd9, (byte) 0xe9, (byte) 0xf1, (byte) 0xcf, (byte) 0x58,
        (byte) 0xf2, (byte) 0x7d, (byte) 0xe2, (byte) 0xe0, (byte) 0x8f, (byte) 0xf4, (byte) 0x59, (byte) 0x64,
        (byte) 0xc7, (byte) 0x20, (byte) 0xd3, (byte) 0xec, (byte) 0x21, (byte) 0x39, (byte) 0xdc, (byte) 0x7c,
        (byte) 0xaf, (byte) 0xc9, (byte) 0x12, (byte) 0x95, (byte) 0x3c, (byte) 0xde, (byte) 0xcb, (byte) 0x2f,
        (byte) 0x35, (byte) 0x5a, (byte) 0x2e, (byte) 0x2c, (byte) 0x35, (byte) 0xa5, (byte) 0x0f, (byte) 0xad,
        (byte) 0x75, (byte) 0x4c, (byte) 0xb3, (byte) 0xb2, (byte) 0x31, (byte) 0x66, (byte) 0x42, (byte) 0x4b,
        (byte) 0xa3, (byte) 0xb6, (byte) 0xe3, (byte) 0x11, (byte) 0x2a, (byte) 0x2b, (byte) 0x89, (byte) 0x8c,
        (byte) 0x38, (byte) 0xc5, (byte) 0xc1, (byte) 0x5e, (byte) 0xdb, (byte) 0x23, (byte) 0x86, (byte) 0x93,
        (byte) 0x39,
    });

    /**
     * Test data is PKCS#1 padded "Android.\n" which can be generated by:
     * echo "Android." | openssl rsautl -inkey rsa.key -sign | openssl rsautl -inkey rsa.key -raw -verify | recode ../x1
     */
    private static final byte[] RSA_2048_Vector1 = new byte[] {
        (byte) 0x00, (byte) 0x01, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0x00, (byte) 0x41, (byte) 0x6E, (byte) 0x64, (byte) 0x72, (byte) 0x6F,
        (byte) 0x69, (byte) 0x64, (byte) 0x2E, (byte) 0x0A,
    };

    /**
     * This vector is simply "Android.\n" which is too short.
     */
    private static final byte[] TooShort_Vector = new byte[] {
        (byte) 0x41, (byte) 0x6E, (byte) 0x64, (byte) 0x72, (byte) 0x6F, (byte) 0x69,
        (byte) 0x64, (byte) 0x2E, (byte) 0x0A,
    };

    /**
     * This vector is simply "Android.\n" padded with zeros.
     */
    private static final byte[] TooShort_Vector_Zero_Padded = new byte[] {
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x41, (byte) 0x6e, (byte) 0x64, (byte) 0x72, (byte) 0x6f,
        (byte) 0x69, (byte) 0x64, (byte) 0x2e, (byte) 0x0a,
    };

    /**
     * openssl rsautl -raw -sign -inkey rsa.key | recode ../x1 | sed 's/0x/(byte) 0x/g'
     */
    private static final byte[] RSA_Vector1_Encrypt_Private = new byte[] {
        (byte) 0x35, (byte) 0x43, (byte) 0x38, (byte) 0x44, (byte) 0xAD, (byte) 0x3F,
        (byte) 0x97, (byte) 0x02, (byte) 0xFB, (byte) 0x59, (byte) 0x1F, (byte) 0x4A,
        (byte) 0x2B, (byte) 0xB9, (byte) 0x06, (byte) 0xEC, (byte) 0x66, (byte) 0xE6,
        (byte) 0xD2, (byte) 0xC5, (byte) 0x8B, (byte) 0x7B, (byte) 0xE3, (byte) 0x18,
        (byte) 0xBF, (byte) 0x07, (byte) 0xD6, (byte) 0x01, (byte) 0xF9, (byte) 0xD9,
        (byte) 0x89, (byte) 0xC4, (byte) 0xDB, (byte) 0x00, (byte) 0x68, (byte) 0xFF,
        (byte) 0x9B, (byte) 0x43, (byte) 0x90, (byte) 0xF2, (byte) 0xDB, (byte) 0x83,
        (byte) 0xF4, (byte) 0x7E, (byte) 0xC6, (byte) 0x81, (byte) 0x01, (byte) 0x3A,
        (byte) 0x0B, (byte) 0xE5, (byte) 0xED, (byte) 0x08, (byte) 0x73, (byte) 0x3E,
        (byte) 0xE1, (byte) 0x3F, (byte) 0xDF, (byte) 0x1F, (byte) 0x07, (byte) 0x6D,
        (byte) 0x22, (byte) 0x8D, (byte) 0xCC, (byte) 0x4E, (byte) 0xE3, (byte) 0x9A,
        (byte) 0xBC, (byte) 0xCC, (byte) 0x8F, (byte) 0x9E, (byte) 0x9B, (byte) 0x02,
        (byte) 0x48, (byte) 0x00, (byte) 0xAC, (byte) 0x9F, (byte) 0xA4, (byte) 0x8F,
        (byte) 0x87, (byte) 0xA1, (byte) 0xA8, (byte) 0xE6, (byte) 0x9D, (byte) 0xCD,
        (byte) 0x8B, (byte) 0x05, (byte) 0xE9, (byte) 0xD2, (byte) 0x05, (byte) 0x8D,
        (byte) 0xC9, (byte) 0x95, (byte) 0x16, (byte) 0xD0, (byte) 0xCD, (byte) 0x43,
        (byte) 0x25, (byte) 0x8A, (byte) 0x11, (byte) 0x46, (byte) 0xD7, (byte) 0x74,
        (byte) 0x4C, (byte) 0xCF, (byte) 0x58, (byte) 0xF9, (byte) 0xA1, (byte) 0x30,
        (byte) 0x84, (byte) 0x52, (byte) 0xC9, (byte) 0x01, (byte) 0x5F, (byte) 0x24,
        (byte) 0x4C, (byte) 0xB1, (byte) 0x9F, (byte) 0x7D, (byte) 0x12, (byte) 0x38,
        (byte) 0x27, (byte) 0x0F, (byte) 0x5E, (byte) 0xFF, (byte) 0xE0, (byte) 0x55,
        (byte) 0x8B, (byte) 0xA3, (byte) 0xAD, (byte) 0x60, (byte) 0x35, (byte) 0x83,
        (byte) 0x58, (byte) 0xAF, (byte) 0x99, (byte) 0xDE, (byte) 0x3F, (byte) 0x5D,
        (byte) 0x80, (byte) 0x80, (byte) 0xFF, (byte) 0x9B, (byte) 0xDE, (byte) 0x5C,
        (byte) 0xAB, (byte) 0x97, (byte) 0x43, (byte) 0x64, (byte) 0xD9, (byte) 0x9F,
        (byte) 0xFB, (byte) 0x67, (byte) 0x65, (byte) 0xA5, (byte) 0x99, (byte) 0xE7,
        (byte) 0xE6, (byte) 0xEB, (byte) 0x05, (byte) 0x95, (byte) 0xFC, (byte) 0x46,
        (byte) 0x28, (byte) 0x4B, (byte) 0xD8, (byte) 0x8C, (byte) 0xF5, (byte) 0x0A,
        (byte) 0xEB, (byte) 0x1F, (byte) 0x30, (byte) 0xEA, (byte) 0xE7, (byte) 0x67,
        (byte) 0x11, (byte) 0x25, (byte) 0xF0, (byte) 0x44, (byte) 0x75, (byte) 0x74,
        (byte) 0x94, (byte) 0x06, (byte) 0x78, (byte) 0xD0, (byte) 0x21, (byte) 0xF4,
        (byte) 0x3F, (byte) 0xC8, (byte) 0xC4, (byte) 0x4A, (byte) 0x57, (byte) 0xBE,
        (byte) 0x02, (byte) 0x3C, (byte) 0x93, (byte) 0xF6, (byte) 0x95, (byte) 0xFB,
        (byte) 0xD1, (byte) 0x77, (byte) 0x8B, (byte) 0x43, (byte) 0xF0, (byte) 0xB9,
        (byte) 0x7D, (byte) 0xE0, (byte) 0x32, (byte) 0xE1, (byte) 0x72, (byte) 0xB5,
        (byte) 0x62, (byte) 0x3F, (byte) 0x86, (byte) 0xC3, (byte) 0xD4, (byte) 0x5F,
        (byte) 0x5E, (byte) 0x54, (byte) 0x1B, (byte) 0x5B, (byte) 0xE6, (byte) 0x74,
        (byte) 0xA1, (byte) 0x0B, (byte) 0xE5, (byte) 0x18, (byte) 0xD2, (byte) 0x4F,
        (byte) 0x93, (byte) 0xF3, (byte) 0x09, (byte) 0x58, (byte) 0xCE, (byte) 0xF0,
        (byte) 0xA3, (byte) 0x61, (byte) 0xE4, (byte) 0x6E, (byte) 0x46, (byte) 0x45,
        (byte) 0x89, (byte) 0x50, (byte) 0xBD, (byte) 0x03, (byte) 0x3F, (byte) 0x38,
        (byte) 0xDA, (byte) 0x5D, (byte) 0xD0, (byte) 0x1B, (byte) 0x1F, (byte) 0xB1,
        (byte) 0xEE, (byte) 0x89, (byte) 0x59, (byte) 0xC5,
    };

    private static final byte[] RSA_Vector1_ZeroPadded_Encrypted = new byte[] {
        (byte) 0x60, (byte) 0x4a, (byte) 0x12, (byte) 0xa3, (byte) 0xa7, (byte) 0x4a,
        (byte) 0xa4, (byte) 0xbf, (byte) 0x6c, (byte) 0x36, (byte) 0xad, (byte) 0x66,
        (byte) 0xdf, (byte) 0xce, (byte) 0xf1, (byte) 0xe4, (byte) 0x0f, (byte) 0xd4,
        (byte) 0x54, (byte) 0x5f, (byte) 0x03, (byte) 0x15, (byte) 0x4b, (byte) 0x9e,
        (byte) 0xeb, (byte) 0xfe, (byte) 0x9e, (byte) 0x24, (byte) 0xce, (byte) 0x8e,
        (byte) 0xc3, (byte) 0x36, (byte) 0xa5, (byte) 0x76, (byte) 0xf6, (byte) 0x54,
        (byte) 0xb7, (byte) 0x84, (byte) 0x48, (byte) 0x2f, (byte) 0xd4, (byte) 0x45,
        (byte) 0x74, (byte) 0x48, (byte) 0x5f, (byte) 0x08, (byte) 0x4e, (byte) 0x9c,
        (byte) 0x89, (byte) 0xcc, (byte) 0x34, (byte) 0x40, (byte) 0xb1, (byte) 0x5f,
        (byte) 0xa7, (byte) 0x0e, (byte) 0x11, (byte) 0x4b, (byte) 0xb5, (byte) 0x94,
        (byte) 0xbe, (byte) 0x14, (byte) 0xaa, (byte) 0xaa, (byte) 0xe0, (byte) 0x38,
        (byte) 0x1c, (byte) 0xce, (byte) 0x40, (byte) 0x61, (byte) 0xfc, (byte) 0x08,
        (byte) 0xcb, (byte) 0x14, (byte) 0x2b, (byte) 0xa6, (byte) 0x54, (byte) 0xdf,
        (byte) 0x05, (byte) 0x5c, (byte) 0x9b, (byte) 0x4f, (byte) 0x14, (byte) 0x93,
        (byte) 0xb0, (byte) 0x70, (byte) 0xd9, (byte) 0x32, (byte) 0xdc, (byte) 0x24,
        (byte) 0xe0, (byte) 0xae, (byte) 0x48, (byte) 0xfc, (byte) 0x53, (byte) 0xee,
        (byte) 0x7c, (byte) 0x9f, (byte) 0x69, (byte) 0x34, (byte) 0xf4, (byte) 0x76,
        (byte) 0xee, (byte) 0x67, (byte) 0xb2, (byte) 0xa7, (byte) 0x33, (byte) 0x1c,
        (byte) 0x47, (byte) 0xff, (byte) 0x5c, (byte) 0xf0, (byte) 0xb8, (byte) 0x04,
        (byte) 0x2c, (byte) 0xfd, (byte) 0xe2, (byte) 0xb1, (byte) 0x4a, (byte) 0x0a,
        (byte) 0x69, (byte) 0x1c, (byte) 0x80, (byte) 0x2b, (byte) 0xb4, (byte) 0x50,
        (byte) 0x65, (byte) 0x5c, (byte) 0x76, (byte) 0x78, (byte) 0x9a, (byte) 0x0c,
        (byte) 0x05, (byte) 0x62, (byte) 0xf0, (byte) 0xc4, (byte) 0x1c, (byte) 0x38,
        (byte) 0x15, (byte) 0xd0, (byte) 0xe2, (byte) 0x5a, (byte) 0x3d, (byte) 0xb6,
        (byte) 0xe0, (byte) 0x88, (byte) 0x85, (byte) 0xd1, (byte) 0x4f, (byte) 0x7e,
        (byte) 0xfc, (byte) 0x77, (byte) 0x0d, (byte) 0x2a, (byte) 0x45, (byte) 0xd5,
        (byte) 0xf8, (byte) 0x3c, (byte) 0x7b, (byte) 0x2d, (byte) 0x1b, (byte) 0x82,
        (byte) 0xfe, (byte) 0x58, (byte) 0x22, (byte) 0x47, (byte) 0x06, (byte) 0x58,
        (byte) 0x8b, (byte) 0x4f, (byte) 0xfb, (byte) 0x9b, (byte) 0x1c, (byte) 0x70,
        (byte) 0x36, (byte) 0x12, (byte) 0x04, (byte) 0x17, (byte) 0x47, (byte) 0x8a,
        (byte) 0x0a, (byte) 0xec, (byte) 0x12, (byte) 0x3b, (byte) 0xf8, (byte) 0xd2,
        (byte) 0xdc, (byte) 0x3c, (byte) 0xc8, (byte) 0x46, (byte) 0xc6, (byte) 0x51,
        (byte) 0x06, (byte) 0x06, (byte) 0xcb, (byte) 0x84, (byte) 0x67, (byte) 0xb5,
        (byte) 0x68, (byte) 0xd9, (byte) 0x9c, (byte) 0xd4, (byte) 0x16, (byte) 0x5c,
        (byte) 0xb4, (byte) 0xe2, (byte) 0x55, (byte) 0xe6, (byte) 0x3a, (byte) 0x73,
        (byte) 0x01, (byte) 0x1d, (byte) 0x6f, (byte) 0x30, (byte) 0x31, (byte) 0x59,
        (byte) 0x8b, (byte) 0x2f, (byte) 0x4c, (byte) 0xe7, (byte) 0x86, (byte) 0x4c,
        (byte) 0x39, (byte) 0x4e, (byte) 0x67, (byte) 0x3b, (byte) 0x22, (byte) 0x9b,
        (byte) 0x85, (byte) 0x5a, (byte) 0xc3, (byte) 0x29, (byte) 0xaf, (byte) 0x8c,
        (byte) 0x7c, (byte) 0x59, (byte) 0x4a, (byte) 0x24, (byte) 0xfa, (byte) 0xba,
        (byte) 0x55, (byte) 0x40, (byte) 0x13, (byte) 0x64, (byte) 0xd8, (byte) 0xcb,
        (byte) 0x4b, (byte) 0x98, (byte) 0x3f, (byte) 0xae, (byte) 0x20, (byte) 0xfd,
        (byte) 0x8a, (byte) 0x50, (byte) 0x73, (byte) 0xe4,
    };

    public void testRSA_ECB_NoPadding_Private_OnlyDoFinal_Success() throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(RSA_2048_modulus,
                RSA_2048_privateExponent);

        final PrivateKey privKey = kf.generatePrivate(keySpec);

        Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");

        /*
         * You're actually decrypting with private keys, but there is no
         * distinction made here. It's all keyed off of what kind of key you're
         * using. ENCRYPT_MODE and DECRYPT_MODE are the same.
         */
        c.init(Cipher.ENCRYPT_MODE, privKey);
        byte[] encrypted = c.doFinal(RSA_2048_Vector1);
        assertTrue("Encrypted should match expected",
                Arrays.equals(RSA_Vector1_Encrypt_Private, encrypted));

        c.init(Cipher.DECRYPT_MODE, privKey);
        encrypted = c.doFinal(RSA_2048_Vector1);
        assertTrue("Encrypted should match expected",
                Arrays.equals(RSA_Vector1_Encrypt_Private, encrypted));
    }

    public void testRSA_ECB_NoPadding_Private_UpdateThenEmptyDoFinal_Success() throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(RSA_2048_modulus,
                RSA_2048_privateExponent);

        final PrivateKey privKey = kf.generatePrivate(keySpec);

        Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");

        /*
         * You're actually decrypting with private keys, but there is no
         * distinction made here. It's all keyed off of what kind of key you're
         * using. ENCRYPT_MODE and DECRYPT_MODE are the same.
         */
        c.init(Cipher.ENCRYPT_MODE, privKey);
        c.update(RSA_2048_Vector1);
        byte[] encrypted = c.doFinal();
        assertTrue("Encrypted should match expected",
                Arrays.equals(RSA_Vector1_Encrypt_Private, encrypted));

        c.init(Cipher.DECRYPT_MODE, privKey);
        c.update(RSA_2048_Vector1);
        encrypted = c.doFinal();
        assertTrue("Encrypted should match expected",
                Arrays.equals(RSA_Vector1_Encrypt_Private, encrypted));
    }

    public void testRSA_ECB_NoPadding_Private_SingleByteUpdateThenEmptyDoFinal_Success()
            throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(RSA_2048_modulus,
                RSA_2048_privateExponent);

        final PrivateKey privKey = kf.generatePrivate(keySpec);

        Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");

        /*
         * You're actually decrypting with private keys, but there is no
         * distinction made here. It's all keyed off of what kind of key you're
         * using. ENCRYPT_MODE and DECRYPT_MODE are the same.
         */
        c.init(Cipher.ENCRYPT_MODE, privKey);
        int i;
        for (i = 0; i < RSA_2048_Vector1.length / 2; i++) {
            c.update(RSA_2048_Vector1, i, 1);
        }
        byte[] encrypted = c.doFinal(RSA_2048_Vector1, i, RSA_2048_Vector1.length - i);
        assertTrue("Encrypted should match expected",
                Arrays.equals(RSA_Vector1_Encrypt_Private, encrypted));

        c.init(Cipher.DECRYPT_MODE, privKey);
        for (i = 0; i < RSA_2048_Vector1.length / 2; i++) {
            c.update(RSA_2048_Vector1, i, 1);
        }
        encrypted = c.doFinal(RSA_2048_Vector1, i, RSA_2048_Vector1.length - i);
        assertTrue("Encrypted should match expected",
                Arrays.equals(RSA_Vector1_Encrypt_Private, encrypted));
    }

    public void testRSA_ECB_NoPadding_Public_OnlyDoFinal_Success() throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(RSA_2048_modulus, RSA_2048_publicExponent);

        final PublicKey privKey = kf.generatePublic(keySpec);

        Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");

        /*
         * You're actually encrypting with public keys, but there is no
         * distinction made here. It's all keyed off of what kind of key you're
         * using. ENCRYPT_MODE and DECRYPT_MODE are the same.
         */
        c.init(Cipher.ENCRYPT_MODE, privKey);
        byte[] encrypted = c.doFinal(RSA_Vector1_Encrypt_Private);
        assertTrue("Encrypted should match expected",
                Arrays.equals(RSA_2048_Vector1, encrypted));

        c.init(Cipher.DECRYPT_MODE, privKey);
        encrypted = c.doFinal(RSA_Vector1_Encrypt_Private);
        assertTrue("Encrypted should match expected",
                Arrays.equals(RSA_2048_Vector1, encrypted));
    }

    public void testRSA_ECB_NoPadding_Public_UpdateThenEmptyDoFinal_Success() throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(RSA_2048_modulus, RSA_2048_publicExponent);

        final PublicKey privKey = kf.generatePublic(keySpec);

        Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");

        /*
         * You're actually encrypting with public keys, but there is no
         * distinction made here. It's all keyed off of what kind of key you're
         * using. ENCRYPT_MODE and DECRYPT_MODE are the same.
         */
        c.init(Cipher.ENCRYPT_MODE, privKey);
        c.update(RSA_Vector1_Encrypt_Private);
        byte[] encrypted = c.doFinal();
        assertTrue("Encrypted should match expected", Arrays.equals(RSA_2048_Vector1, encrypted));

        c.init(Cipher.DECRYPT_MODE, privKey);
        c.update(RSA_Vector1_Encrypt_Private);
        encrypted = c.doFinal();
        assertTrue("Encrypted should match expected", Arrays.equals(RSA_2048_Vector1, encrypted));
    }

    public void testRSA_ECB_NoPadding_Public_SingleByteUpdateThenEmptyDoFinal_Success()
            throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(RSA_2048_modulus, RSA_2048_publicExponent);

        final PublicKey privKey = kf.generatePublic(keySpec);

        Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");

        /*
         * You're actually encrypting with public keys, but there is no
         * distinction made here. It's all keyed off of what kind of key you're
         * using. ENCRYPT_MODE and DECRYPT_MODE are the same.
         */
        c.init(Cipher.ENCRYPT_MODE, privKey);
        int i;
        for (i = 0; i < RSA_Vector1_Encrypt_Private.length / 2; i++) {
            c.update(RSA_Vector1_Encrypt_Private, i, 1);
        }
        byte[] encrypted = c.doFinal(RSA_Vector1_Encrypt_Private, i, RSA_2048_Vector1.length - i);
        assertTrue("Encrypted should match expected", Arrays.equals(RSA_2048_Vector1, encrypted));

        c.init(Cipher.DECRYPT_MODE, privKey);
        for (i = 0; i < RSA_Vector1_Encrypt_Private.length / 2; i++) {
            c.update(RSA_Vector1_Encrypt_Private, i, 1);
        }
        encrypted = c.doFinal(RSA_Vector1_Encrypt_Private, i, RSA_2048_Vector1.length - i);
        assertTrue("Encrypted should match expected", Arrays.equals(RSA_2048_Vector1, encrypted));
    }

    public void testRSA_ECB_NoPadding_Public_TooSmall_Success() throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(RSA_2048_modulus, RSA_2048_publicExponent);

        final PublicKey privKey = kf.generatePublic(keySpec);

        Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");

        /*
         * You're actually encrypting with public keys, but there is no
         * distinction made here. It's all keyed off of what kind of key you're
         * using. ENCRYPT_MODE and DECRYPT_MODE are the same.
         */
        c.init(Cipher.ENCRYPT_MODE, privKey);
        byte[] encrypted = c.doFinal(TooShort_Vector);
        assertTrue("Encrypted should match expected",
                Arrays.equals(RSA_Vector1_ZeroPadded_Encrypted, encrypted));

        c.init(Cipher.DECRYPT_MODE, privKey);
        encrypted = c.doFinal(TooShort_Vector);
        assertTrue("Encrypted should match expected",
                Arrays.equals(RSA_Vector1_ZeroPadded_Encrypted, encrypted));
    }

    public void testRSA_ECB_NoPadding_Private_TooSmall_Success() throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(RSA_2048_modulus,
                RSA_2048_privateExponent);

        final PrivateKey privKey = kf.generatePrivate(keySpec);

        Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");

        /*
         * You're actually encrypting with public keys, but there is no
         * distinction made here. It's all keyed off of what kind of key you're
         * using. ENCRYPT_MODE and DECRYPT_MODE are the same.
         */
        c.init(Cipher.ENCRYPT_MODE, privKey);
        byte[] encrypted = c.doFinal(RSA_Vector1_ZeroPadded_Encrypted);
        assertTrue("Encrypted should match expected",
                Arrays.equals(TooShort_Vector_Zero_Padded, encrypted));

        c.init(Cipher.DECRYPT_MODE, privKey);
        encrypted = c.doFinal(RSA_Vector1_ZeroPadded_Encrypted);
        assertTrue("Encrypted should match expected",
                Arrays.equals(TooShort_Vector_Zero_Padded, encrypted));
    }

    public void testRSA_ECB_NoPadding_Private_CombinedUpdateAndDoFinal_TooBig_Failure()
            throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(RSA_2048_modulus,
                RSA_2048_privateExponent);

        final PrivateKey privKey = kf.generatePrivate(keySpec);

        Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");

        /*
         * You're actually encrypting with public keys, but there is no
         * distinction made here. It's all keyed off of what kind of key you're
         * using. ENCRYPT_MODE and DECRYPT_MODE are the same.
         */
        c.init(Cipher.ENCRYPT_MODE, privKey);
        c.update(RSA_Vector1_ZeroPadded_Encrypted);

        try {
            c.doFinal(RSA_Vector1_ZeroPadded_Encrypted);
            fail("Should have error when block size is too big.");
        } catch (IllegalBlockSizeException success) {
        }
    }

    public void testRSA_ECB_NoPadding_Private_UpdateInAndOutPlusDoFinal_TooBig_Failure()
            throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(RSA_2048_modulus,
                RSA_2048_privateExponent);

        final PrivateKey privKey = kf.generatePrivate(keySpec);

        Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");

        /*
         * You're actually encrypting with public keys, but there is no
         * distinction made here. It's all keyed off of what kind of key you're
         * using. ENCRYPT_MODE and DECRYPT_MODE are the same.
         */
        c.init(Cipher.ENCRYPT_MODE, privKey);

        byte[] output = new byte[RSA_2048_Vector1.length];
        c.update(RSA_Vector1_ZeroPadded_Encrypted, 0, RSA_Vector1_ZeroPadded_Encrypted.length,
                output);

        try {
            c.doFinal(RSA_Vector1_ZeroPadded_Encrypted);
            fail("Should have error when block size is too big.");
        } catch (IllegalBlockSizeException success) {
        }
    }

    public void testRSA_ECB_NoPadding_Private_OnlyDoFinal_TooBig_Failure() throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(RSA_2048_modulus,
                RSA_2048_privateExponent);

        final PrivateKey privKey = kf.generatePrivate(keySpec);

        Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");

        /*
         * You're actually encrypting with public keys, but there is no
         * distinction made here. It's all keyed off of what kind of key you're
         * using. ENCRYPT_MODE and DECRYPT_MODE are the same.
         */
        c.init(Cipher.ENCRYPT_MODE, privKey);

        byte[] tooBig_Vector = new byte[RSA_Vector1_ZeroPadded_Encrypted.length * 2];
        System.arraycopy(RSA_Vector1_ZeroPadded_Encrypted, 0, tooBig_Vector, 0,
                RSA_Vector1_ZeroPadded_Encrypted.length);
        System.arraycopy(RSA_Vector1_ZeroPadded_Encrypted, 0, tooBig_Vector,
                RSA_Vector1_ZeroPadded_Encrypted.length, RSA_Vector1_ZeroPadded_Encrypted.length);

        try {
            c.doFinal(tooBig_Vector);
            fail("Should have error when block size is too big.");
        } catch (IllegalBlockSizeException success) {
        }
    }

    public void testRSA_ECB_NoPadding_GetBlockSize_Success() throws Exception {
        Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");
        assertEquals("RSA is not a block cipher and should return block size of 0",
                0, c.getBlockSize());

        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(RSA_2048_modulus,
                RSA_2048_publicExponent);
        final PublicKey pubKey = kf.generatePublic(pubKeySpec);
        c.init(Cipher.ENCRYPT_MODE, pubKey);

        assertEquals("RSA is not a block cipher and should return block size of 0",
                0, c.getBlockSize());
    }

    public void testRSA_ECB_NoPadding_GetOutputSize_NoInit_Failure() throws Exception {
        Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");
        try {
            c.getOutputSize(RSA_2048_Vector1.length);
            fail("Should throw IllegalStateException if getOutputSize is called before init");
        } catch (IllegalStateException success) {
        }
    }

    public void testRSA_ECB_NoPadding_GetOutputSize_Success() throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(RSA_2048_modulus,
                RSA_2048_publicExponent);
        final PublicKey pubKey = kf.generatePublic(pubKeySpec);

        Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, pubKey);

        final int modulusInBytes = RSA_2048_modulus.bitLength() / 8;
        assertEquals("Output size should be equal to modulus size",
                modulusInBytes, c.getOutputSize(RSA_2048_Vector1.length));

        assertEquals("Output size should be equal to modulus size",
                modulusInBytes, c.getOutputSize(RSA_2048_Vector1.length * 2));

        assertEquals("Output size should be equal to modulus size",
                modulusInBytes, c.getOutputSize(0));
    }

    public void testRSA_ECB_NoPadding_GetIV_Success() throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(RSA_2048_modulus,
                RSA_2048_publicExponent);
        final PublicKey pubKey = kf.generatePublic(pubKeySpec);

        Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");
        assertNull("ECB mode has no IV and should be null", c.getIV());

        c.init(Cipher.ENCRYPT_MODE, pubKey);

        assertNull("ECB mode has no IV and should be null", c.getIV());
    }

    public void testRSA_ECB_NoPadding_GetParameters_NoneProvided_Success() throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(RSA_2048_modulus,
                RSA_2048_publicExponent);
        final PublicKey pubKey = kf.generatePublic(pubKeySpec);

        Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");
        assertNull("Parameters should be null", c.getParameters());
    }
}







