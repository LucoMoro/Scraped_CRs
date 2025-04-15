/*SubjectPublicKeyInfo: use algorithm OID as fallback

If the algorithm has a name, but there are no KeyFactory available for
that particular algorithm name, try to use the algorithm OID to find a
KeyFactory before falling back to X509PublicKey.

Change-Id:I2b294f2db3388372479c964f53fdff7fb62f3d8f*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/x509/SubjectPublicKeyInfo.java b/luni/src/main/java/org/apache/harmony/security/x509/SubjectPublicKeyInfo.java
//Synthetic comment -- index 545d489..aef7a65 100644

//Synthetic comment -- @@ -26,13 +26,13 @@
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.apache.harmony.security.asn1.ASN1BitString;
import org.apache.harmony.security.asn1.ASN1Sequence;
import org.apache.harmony.security.asn1.ASN1Type;
import org.apache.harmony.security.asn1.BerInputStream;
import org.apache.harmony.security.asn1.BitString;

/**
* The class encapsulates the ASN.1 DER encoding/decoding work
//Synthetic comment -- @@ -103,32 +103,54 @@
}

/**
     * Returns the PublicKey corresponding to this SubjectPublicKeyInfo
* instance.
*/
public PublicKey getPublicKey() {
if (publicKey == null) {
            final byte[] encoded = getEncoded();
            final KeySpec keySpec = new X509EncodedKeySpec(encoded);

            /* Try using the algorithm name first. */
            final String algName = algorithmID.getAlgorithmName();
            publicKey = generateKeyForAlgorithm(keySpec, algName);

            /*
             * Fall back to using the algorithm OID if it's not the same as the
             * algorithm name.
             */
            final String algOid = algorithmID.getAlgorithm();
            if (publicKey == null && !algOid.equals(algName)) {
                publicKey = generateKeyForAlgorithm(keySpec, algOid);
}

            /*
             * Encode this as an X.509 public key since we didn't have any
             * KeyFactory that could handle this algorithm name or OID. Perhaps
             * the thing that's using this can decode it.
             */
if (publicKey == null) {
                publicKey = new X509PublicKey(algOid, encoded, subjectPublicKey);
}
}
return publicKey;
}

    /**
     * Try to generate a PublicKey for a given {@code keySpec} and
     * {@code algorithm} identifier. If there a problem generating the key like
     * a missing {@code KeyFactory} or invalid {@code KeySpec}, it will return
     * {@code null}.
     */
    private static PublicKey generateKeyForAlgorithm(KeySpec keySpec, String algorithm) {
        try {
            return KeyFactory.getInstance(algorithm).generatePublic(keySpec);
        } catch (InvalidKeySpecException ignored) {
        } catch (NoSuchAlgorithmException ignored) {
        }
        return null;
    }

public static final ASN1Sequence ASN1 = new ASN1Sequence(new ASN1Type[] {
AlgorithmIdentifier.ASN1, ASN1BitString.getInstance() }) {
@Override protected Object getDecodedObject(BerInputStream in) {








//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/security/tests/x509/SubjectPublicKeyInfoTest.java b/luni/src/test/java/org/apache/harmony/security/tests/x509/SubjectPublicKeyInfoTest.java
new file mode 100644
//Synthetic comment -- index 0000000..0417be0

//Synthetic comment -- @@ -0,0 +1,162 @@
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

package org.apache.harmony.security.tests.x509;

import org.apache.harmony.security.asn1.ASN1Integer;
import org.apache.harmony.security.asn1.ASN1Sequence;
import org.apache.harmony.security.asn1.ASN1Type;
import org.apache.harmony.security.x509.AlgorithmIdentifier;
import org.apache.harmony.security.x509.SubjectPublicKeyInfo;
import org.apache.harmony.security.x509.X509PublicKey;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactorySpi;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import junit.framework.TestCase;

public class SubjectPublicKeyInfoTest extends TestCase {
    private static final byte[] ENCODED_BROKEN = "BROKEN!".getBytes(Charset.forName("ASCII"));

    public void test_getPublicKey_WellKnownOid() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        KeyPair pair = kpg.generateKeyPair();

        final RSAPublicKey rsaPubKey = (RSAPublicKey) pair.getPublic();

        /* Do some fancy footwork to get an ASN.1 SubjectPublicKey for RSA */
        final ASN1Sequence rsaPubKeyInfo = new ASN1Sequence(new ASN1Type[] {
                ASN1Integer.getInstance(), ASN1Integer.getInstance(),
        }) {
            @Override
            protected void getValues(Object object, Object[] values) {
                values[0] = rsaPubKey.getModulus().toByteArray();
                values[1] = rsaPubKey.getPublicExponent().toByteArray();
            }
        };

        /* The algorithm ID for RSA encryption */
        AlgorithmIdentifier algid = new AlgorithmIdentifier("1.2.840.113549.1.1.1");

        SubjectPublicKeyInfo spki = new SubjectPublicKeyInfo(algid, rsaPubKeyInfo.encode(null));

        PublicKey pubKey = spki.getPublicKey();
        assertNotNull(pubKey);
        assertTrue(pubKey instanceof RSAPublicKey);
    }

    public void test_getPublicKey_Unknown_OID() throws Exception {
        AlgorithmIdentifier algid = new AlgorithmIdentifier("1.30.9999999999.8734878");
        SubjectPublicKeyInfo spki = new SubjectPublicKeyInfo(algid, ENCODED_BROKEN);
        PublicKey pubKey = spki.getPublicKey();
        assertNotNull(pubKey);
        assertEquals(X509PublicKey.class, pubKey.getClass());
    }

    private static final String MY_TEST_KEY_OID = "1.30.987654321.1.1.1.2.2.2";

    public void test_getPublicKey_Only_OID() throws Exception {
        Security.addProvider(new MyTestProvider());
        try {
            AlgorithmIdentifier algid = new AlgorithmIdentifier(MY_TEST_KEY_OID);
            SubjectPublicKeyInfo spki = new SubjectPublicKeyInfo(algid, ENCODED_BROKEN);
            PublicKey pubKey = spki.getPublicKey();
            assertNotNull(pubKey);
            assertEquals(MyTestPublicKey.class, pubKey.getClass());
            byte[] encoded = pubKey.getEncoded();
            assertEquals(
                    Arrays.toString(ENCODED_BROKEN),
                    Arrays.toString(Arrays.copyOfRange(encoded, encoded.length
                            - ENCODED_BROKEN.length, encoded.length)));
        } finally {
            Security.removeProvider(MyTestProvider.NAME);
        }
    }

    public static class MyTestProvider extends Provider {
        public static final String NAME = "MyTestProvider";

        protected MyTestProvider() {
            super(NAME, 1.0, "MyTestProvider");

            put("KeyFactory." + MY_TEST_KEY_OID, MyTestKeyFactory.class.getName());
        }
    }

    public static class MyTestKeyFactory extends KeyFactorySpi {
        @Override
        protected PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {
            if (!(keySpec instanceof X509EncodedKeySpec)) {
                throw new InvalidKeySpecException("Only X509EncodedKeySpec supported");
            }

            X509EncodedKeySpec x509ks = (X509EncodedKeySpec) keySpec;
            return new MyTestPublicKey(x509ks.getEncoded());
        }

        @Override
        protected PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException {
            throw new UnsupportedOperationException();
        }

        @Override
        protected <T extends KeySpec> T engineGetKeySpec(Key key, Class<T> keySpec)
                throws InvalidKeySpecException {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Key engineTranslateKey(Key key) throws InvalidKeyException {
            throw new UnsupportedOperationException();
        }
    }

    public static class MyTestPublicKey implements PublicKey {
        private final byte[] data;

        public MyTestPublicKey(byte[] data) {
            this.data = data;
        }

        @Override
        public String getAlgorithm() {
            return "MyTestPublicKey";
        }

        @Override
        public String getFormat() {
            return null;
        }

        @Override
        public byte[] getEncoded() {
            return data;
        }
    }
}







