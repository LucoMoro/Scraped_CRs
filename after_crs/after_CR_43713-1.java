/*Add more CipherTest tests

Change-Id:I29f55e41335021945029e410d4e51e2c8f564285*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/crypto/CipherTest.java b/luni/src/test/java/libcore/javax/crypto/CipherTest.java
//Synthetic comment -- index d31825a..1bf96f8 100644

//Synthetic comment -- @@ -17,9 +17,13 @@
package libcore.javax.crypto;

import com.android.org.bouncycastle.asn1.x509.KeyUsage;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
//Synthetic comment -- @@ -28,17 +32,29 @@
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import junit.framework.TestCase;
import libcore.java.security.StandardNames;
import libcore.java.security.TestKeyStore;

public final class CipherTest extends TestCase {
//Synthetic comment -- @@ -76,10 +92,13 @@
return Cipher.DECRYPT_MODE;
}

    private static String getBaseAlgorithm(String algorithm) {
if (algorithm.equals("AESWRAP")) {
return "AES";
}
        if (algorithm.startsWith("AES/")) {
            return "AES";
        }
if (algorithm.equals("PBEWITHMD5AND128BITAES-CBC-OPENSSL")) {
return "AES";
}
//Synthetic comment -- @@ -114,18 +133,18 @@
return "DES";
}
if (algorithm.equals("DESEDEWRAP")) {
            return "DESEDE";
}
if (algorithm.equals("PBEWITHSHAAND2-KEYTRIPLEDES-CBC")) {
            return "DESEDE";
}
if (algorithm.equals("PBEWITHSHAAND3-KEYTRIPLEDES-CBC")) {
            return "DESEDE";
}
        if (algorithm.equals("RSA/ECB/NOPADDING")) {
return "RSA";
}
        if (algorithm.equals("RSA/ECB/PKCS1PADDING")) {
return "RSA";
}
if (algorithm.equals("PBEWITHSHAAND40BITRC4")) {
//Synthetic comment -- @@ -138,7 +157,7 @@
}

private static boolean isAsymmetric(String algorithm) {
        return getBaseAlgorithm(algorithm).equals("RSA");
}

private static boolean isWrap(String algorithm) {
//Synthetic comment -- @@ -151,8 +170,8 @@
if (key != null) {
return key;
}
        algorithm = getBaseAlgorithm(algorithm);
        if (algorithm.startsWith("RSA")) {
KeyFactory kf = KeyFactory.getInstance("RSA");
RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(RSA_2048_modulus,
RSA_2048_privateExponent);
//Synthetic comment -- @@ -171,8 +190,8 @@
if (key != null) {
return key;
}
        algorithm = getBaseAlgorithm(algorithm);
        if (algorithm.startsWith("RSA")) {
KeyFactory kf = KeyFactory.getInstance("RSA");
RSAPublicKeySpec keySpec = new RSAPublicKeySpec(RSA_2048_modulus,
RSA_2048_publicExponent);
//Synthetic comment -- @@ -216,11 +235,15 @@

EXPECTED_BLOCK_SIZE.put("DESEDEWRAP", 0);

        EXPECTED_BLOCK_SIZE.put("RSA", 0);
        EXPECTED_BLOCK_SIZE.put("RSA/ECB/NOPADDING", 0);
        EXPECTED_BLOCK_SIZE.put("RSA/ECB/PKCS1PADDING", 0);
}
private static int getExpectedBlockSize(String algorithm) {
        final int firstSlash = algorithm.indexOf('/');
        if (firstSlash != -1) {
            algorithm = algorithm.substring(0, firstSlash);
        }
Integer expected = EXPECTED_BLOCK_SIZE.get(algorithm);
assertNotNull(algorithm, expected);
return expected;
//Synthetic comment -- @@ -258,11 +281,11 @@
EXPECTED_OUTPUT_SIZE.put("DESEDEWRAP", -1);

EXPECTED_OUTPUT_SIZE.put("RSA", 256);
        EXPECTED_OUTPUT_SIZE.put("RSA/ECB/NOPADDING", 256);
        EXPECTED_OUTPUT_SIZE.put("RSA/ECB/PKCS1PADDING", 256);
}
private static int getExpectedOutputSize(String algorithm) {
        Integer expected = EXPECTED_OUTPUT_SIZE.get(getBaseAlgorithm(algorithm));
assertNotNull(algorithm, expected);
return expected;
}
//Synthetic comment -- @@ -356,13 +379,16 @@
};

private static byte[] getExpectedPlainText(String algorithm) {
        if (algorithm.equals("RSA/ECB/NOPADDING")) {
return PKCS1_BLOCK_TYPE_00_PADDED_PLAIN_TEXT;
}
return ORIGINAL_PLAIN_TEXT;
}

public void test_getInstance() throws Exception {
        final ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(errBuffer);

Provider[] providers = Security.getProviders();
for (Provider provider : providers) {
Set<Provider.Service> services = provider.getServices();
//Synthetic comment -- @@ -371,34 +397,63 @@
if (!type.equals("Cipher")) {
continue;
}

String algorithm = service.getAlgorithm();
try {
                    test_Cipher_Algorithm(provider, algorithm);
} catch (Throwable e) {
                    out.append("Error encountered checking " + algorithm + "\n");
                    e.printStackTrace(out);
                }

                Set<String> modes = StandardNames.getModesForCipher(algorithm);
                if (modes != null) {
                    for (String mode : modes) {
                        Set<String> paddings = StandardNames.getPaddingsForCipher(algorithm);
                        if (paddings != null) {
                            for (String padding : paddings) {
                                final String algorithmName = algorithm + "/" + mode + "/" + padding;
                                try {
                                    test_Cipher_Algorithm(provider, algorithmName);
                                } catch (Throwable e) {
                                    out.append("Error encountered checking " + algorithmName + "\n");
                                    e.printStackTrace(out);
                                }
                            }
                        }
                    }
}
}
}

        out.flush();
        if (errBuffer.size() > 0) {
            throw new Exception("Errors encountered:\n\n" + errBuffer.toString() + "\n\n");
        }
    }

    private void test_Cipher_Algorithm(Provider provider, String algorithm) throws Exception {
        // Cipher.getInstance(String)
        Cipher c1 = Cipher.getInstance(algorithm);
        assertEquals(algorithm, c1.getAlgorithm());
        test_Cipher(c1);

        // Cipher.getInstance(String, Provider)
        Cipher c2 = Cipher.getInstance(algorithm, provider);
        assertEquals(algorithm, c2.getAlgorithm());
        assertEquals(provider, c2.getProvider());
        test_Cipher(c2);

        // KeyGenerator.getInstance(String, String)
        Cipher c3 = Cipher.getInstance(algorithm, provider.getName());
        assertEquals(algorithm, c3.getAlgorithm());
        assertEquals(provider, c3.getProvider());
        test_Cipher(c3);
}

private void test_Cipher(Cipher c) throws Exception {
// TODO: test all supported modes and padding for a given algorithm
        String algorithm = c.getAlgorithm().toUpperCase(Locale.US);
if (isUnsupported(algorithm)) {
return;
}
//Synthetic comment -- @@ -408,13 +463,13 @@
} catch (IllegalStateException expected) {
}

        assertEquals("getBlockSize()", getExpectedBlockSize(algorithm), c.getBlockSize());

// TODO: test keys from different factories (e.g. OpenSSLRSAPrivateKey vs JCERSAPrivateKey)
Key encryptKey = getEncryptKey(algorithm);
c.init(getEncryptMode(algorithm), encryptKey);

        assertEquals("getOutputSize(0)", getExpectedOutputSize(algorithm), c.getOutputSize(0));

// TODO: test Cipher.getIV()

//Synthetic comment -- @@ -1353,4 +1408,347 @@
Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");
assertNull("Parameters should be null", c.getParameters());
}

    /*
     * Test vector generation:
     * openssl rand -hex 16
     * echo '3d4f8970b1f27537f40a39298a41555f' | sed 's/\(..\)/(byte) 0x\1, /g'
     */
    private static final byte[] AES_128_KEY = new byte[] {
            (byte) 0x3d, (byte) 0x4f, (byte) 0x89, (byte) 0x70, (byte) 0xb1, (byte) 0xf2,
            (byte) 0x75, (byte) 0x37, (byte) 0xf4, (byte) 0x0a, (byte) 0x39, (byte) 0x29,
            (byte) 0x8a, (byte) 0x41, (byte) 0x55, (byte) 0x5f,
    };

    /*
     * Test key generation:
     * openssl rand -hex 24
     * echo '5a7a3d7e40b64ed996f7afa15f97fd595e27db6af428e342' | sed 's/\(..\)/(byte) 0x\1, /g'
     */
    private static final byte[] AES_192_KEY = new byte[] {
            (byte) 0x5a, (byte) 0x7a, (byte) 0x3d, (byte) 0x7e, (byte) 0x40, (byte) 0xb6,
            (byte) 0x4e, (byte) 0xd9, (byte) 0x96, (byte) 0xf7, (byte) 0xaf, (byte) 0xa1,
            (byte) 0x5f, (byte) 0x97, (byte) 0xfd, (byte) 0x59, (byte) 0x5e, (byte) 0x27,
            (byte) 0xdb, (byte) 0x6a, (byte) 0xf4, (byte) 0x28, (byte) 0xe3, (byte) 0x42,
    };

    /*
     * Test key generation:
     * openssl rand -hex 32
     * echo 'ec53c6d51d2c4973585fb0b8e51cd2e39915ff07a1837872715d6121bf861935' | sed 's/\(..\)/(byte) 0x\1, /g'
     */
    private static final byte[] AES_256_KEY = new byte[] {
            (byte) 0xec, (byte) 0x53, (byte) 0xc6, (byte) 0xd5, (byte) 0x1d, (byte) 0x2c,
            (byte) 0x49, (byte) 0x73, (byte) 0x58, (byte) 0x5f, (byte) 0xb0, (byte) 0xb8,
            (byte) 0xe5, (byte) 0x1c, (byte) 0xd2, (byte) 0xe3, (byte) 0x99, (byte) 0x15,
            (byte) 0xff, (byte) 0x07, (byte) 0xa1, (byte) 0x83, (byte) 0x78, (byte) 0x72,
            (byte) 0x71, (byte) 0x5d, (byte) 0x61, (byte) 0x21, (byte) 0xbf, (byte) 0x86,
            (byte) 0x19, (byte) 0x35,
    };

    static {

    }
    private static final byte[][] AES_KEYS = new byte[][] {
            AES_128_KEY, AES_192_KEY, AES_256_KEY,
    };

    private static final String[] AES_MODES = new String[] {
            "AES/ECB",
            "AES/CBC",
            "AES/CFB",
            "AES/CTR",
            "AES/OFB",
    };

    /*
     * Test vector creation:
     * echo -n 'Hello, world!' | recode ../x1 | sed 's/0x/(byte) 0x/g'
     */
    private static final byte[] AES_128_ECB_PKCS5Padding_TestVector_1_Plaintext = new byte[] {
            (byte) 0x48, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F, (byte) 0x2C,
            (byte) 0x20, (byte) 0x77, (byte) 0x6F, (byte) 0x72, (byte) 0x6C, (byte) 0x64,
            (byte) 0x21,
    };

    /*
     * Test vector creation:
     * openssl enc -aes-128-ecb -K 3d4f8970b1f27537f40a39298a41555f -in blah|openssl enc -aes-128-ecb -K 3d4f8970b1f27537f40a39298a41555f -nopad -d|recode ../x1 | sed 's/0x/(byte) 0x/g'
     */
    private static final byte[] AES_128_ECB_PKCS5Padding_TestVector_1_Plaintext_Padded = new byte[] {
            (byte) 0x48, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F, (byte) 0x2C,
            (byte) 0x20, (byte) 0x77, (byte) 0x6F, (byte) 0x72, (byte) 0x6C, (byte) 0x64,
            (byte) 0x21, (byte) 0x03, (byte) 0x03, (byte) 0x03
    };

    /*
     * Test vector generation:
     * openssl enc -aes-128-ecb -K 3d4f8970b1f27537f40a39298a41555f -in blah|recode ../x1 | sed 's/0x/(byte) 0x/g'
     */
    private static final byte[] AES_128_ECB_PKCS5Padding_TestVector_1_Encrypted = new byte[] {
            (byte) 0x65, (byte) 0x3E, (byte) 0x86, (byte) 0xFB, (byte) 0x05, (byte) 0x5A,
            (byte) 0x52, (byte) 0xEA, (byte) 0xDD, (byte) 0x08, (byte) 0xE7, (byte) 0x48,
            (byte) 0x33, (byte) 0x01, (byte) 0xFC, (byte) 0x5A,
    };

    /*
     * Test key generation:
     * openssl rand -hex 16
     * echo 'ceaa31952dfd3d0f5af4b2042ba06094' | sed 's/\(..\)/(byte) 0x\1, /g'
     */
    private static final byte[] AES_256_CBC_PKCS5Padding_TestVector_1_IV = new byte[] {
            (byte) 0xce, (byte) 0xaa, (byte) 0x31, (byte) 0x95, (byte) 0x2d, (byte) 0xfd,
            (byte) 0x3d, (byte) 0x0f, (byte) 0x5a, (byte) 0xf4, (byte) 0xb2, (byte) 0x04,
            (byte) 0x2b, (byte) 0xa0, (byte) 0x60, (byte) 0x94,
    };

    /*
     * Test vector generation:
     * echo -n 'I only regret that I have but one test to write.' | recode ../x1 | sed 's/0x/(byte) 0x/g'
     */
    private static final byte[] AES_256_CBC_PKCS5Padding_TestVector_1_Plaintext = new byte[] {
            (byte) 0x49, (byte) 0x20, (byte) 0x6F, (byte) 0x6E, (byte) 0x6C, (byte) 0x79,
            (byte) 0x20, (byte) 0x72, (byte) 0x65, (byte) 0x67, (byte) 0x72, (byte) 0x65,
            (byte) 0x74, (byte) 0x20, (byte) 0x74, (byte) 0x68, (byte) 0x61, (byte) 0x74,
            (byte) 0x20, (byte) 0x49, (byte) 0x20, (byte) 0x68, (byte) 0x61, (byte) 0x76,
            (byte) 0x65, (byte) 0x20, (byte) 0x62, (byte) 0x75, (byte) 0x74, (byte) 0x20,
            (byte) 0x6F, (byte) 0x6E, (byte) 0x65, (byte) 0x20, (byte) 0x74, (byte) 0x65,
            (byte) 0x73, (byte) 0x74, (byte) 0x20, (byte) 0x74, (byte) 0x6F, (byte) 0x20,
            (byte) 0x77, (byte) 0x72, (byte) 0x69, (byte) 0x74, (byte) 0x65, (byte) 0x2E
    };

    /*
     * Test vector generation:
     * echo -n 'I only regret that I have but one test to write.' | openssl enc -aes-256-cbc -K ec53c6d51d2c4973585fb0b8e51cd2e39915ff07a1837872715d6121bf861935 -iv ceaa31952dfd3d0f5af4b2042ba06094 | openssl enc -aes-256-cbc -K ec53c6d51d2c4973585fb0b8e51cd2e39915ff07a1837872715d6121bf861935 -iv ceaa31952dfd3d0f5af4b2042ba06094 -d -nopad | recode ../x1 | sed 's/0x/(byte) 0x/g'
     */
    private static final byte[] AES_256_CBC_PKCS5Padding_TestVector_1_Plaintext_Padded = new byte[] {
            (byte) 0x49, (byte) 0x20, (byte) 0x6F, (byte) 0x6E, (byte) 0x6C, (byte) 0x79,
            (byte) 0x20, (byte) 0x72, (byte) 0x65, (byte) 0x67, (byte) 0x72, (byte) 0x65,
            (byte) 0x74, (byte) 0x20, (byte) 0x74, (byte) 0x68, (byte) 0x61, (byte) 0x74,
            (byte) 0x20, (byte) 0x49, (byte) 0x20, (byte) 0x68, (byte) 0x61, (byte) 0x76,
            (byte) 0x65, (byte) 0x20, (byte) 0x62, (byte) 0x75, (byte) 0x74, (byte) 0x20,
            (byte) 0x6F, (byte) 0x6E, (byte) 0x65, (byte) 0x20, (byte) 0x74, (byte) 0x65,
            (byte) 0x73, (byte) 0x74, (byte) 0x20, (byte) 0x74, (byte) 0x6F, (byte) 0x20,
            (byte) 0x77, (byte) 0x72, (byte) 0x69, (byte) 0x74, (byte) 0x65, (byte) 0x2E,
            (byte) 0x10, (byte) 0x10, (byte) 0x10, (byte) 0x10, (byte) 0x10, (byte) 0x10,
            (byte) 0x10, (byte) 0x10, (byte) 0x10, (byte) 0x10, (byte) 0x10, (byte) 0x10,
            (byte) 0x10, (byte) 0x10, (byte) 0x10, (byte) 0x10
    };

    /*
     * Test vector generation:
     * echo -n 'I only regret that I have but one test to write.' | openssl enc -aes-256-cbc -K ec53c6d51d2c4973585fb0b8e51cd2e39915ff07a1837872715d6121bf861935 -iv ceaa31952dfd3d0f5af4b2042ba06094 | recode ../x1 | sed 's/0x/(byte) 0x/g'
     */
    private static final byte[] AES_256_CBC_PKCS5Padding_TestVector_1_Ciphertext = new byte[] {
            (byte) 0x90, (byte) 0x65, (byte) 0xDD, (byte) 0xAF, (byte) 0x7A, (byte) 0xCE,
            (byte) 0xAE, (byte) 0xBF, (byte) 0xE8, (byte) 0xF6, (byte) 0x9E, (byte) 0xDB,
            (byte) 0xEA, (byte) 0x65, (byte) 0x28, (byte) 0xC4, (byte) 0x9A, (byte) 0x28,
            (byte) 0xEA, (byte) 0xA3, (byte) 0x95, (byte) 0x2E, (byte) 0xFF, (byte) 0xF1,
            (byte) 0xA0, (byte) 0xCA, (byte) 0xC2, (byte) 0xA4, (byte) 0x65, (byte) 0xCD,
            (byte) 0xBF, (byte) 0xCE, (byte) 0x9E, (byte) 0xF1, (byte) 0x57, (byte) 0xF6,
            (byte) 0x32, (byte) 0x2E, (byte) 0x8F, (byte) 0x93, (byte) 0x2E, (byte) 0xAE,
            (byte) 0x41, (byte) 0x33, (byte) 0x54, (byte) 0xD0, (byte) 0xEF, (byte) 0x8C,
            (byte) 0x52, (byte) 0x14, (byte) 0xAC, (byte) 0x2D, (byte) 0xD5, (byte) 0xA4,
            (byte) 0xF9, (byte) 0x20, (byte) 0x77, (byte) 0x25, (byte) 0x91, (byte) 0x3F,
            (byte) 0xD1, (byte) 0xB9, (byte) 0x00, (byte) 0x3E
    };

    private static class CipherTestParam {
        public final String mode;

        public final byte[] key;

        public final byte[] iv;

        public final byte[] plaintext;

        public final byte[] ciphertext;

        public final byte[] plaintextPadded;

        public CipherTestParam(String mode, byte[] key, byte[] iv, byte[] plaintext,
                byte[] plaintextPadded, byte[] ciphertext) {
            this.mode = mode;
            this.key = key;
            this.iv = iv;
            this.plaintext = plaintext;
            this.plaintextPadded = plaintextPadded;
            this.ciphertext = ciphertext;
        }
    }

    private static List<CipherTestParam> CIPHER_TEST_PARAMS = new ArrayList<CipherTestParam>();
    static {
        CIPHER_TEST_PARAMS.add(new CipherTestParam("AES/ECB", AES_128_KEY,
                null,
                AES_128_ECB_PKCS5Padding_TestVector_1_Plaintext,
                AES_128_ECB_PKCS5Padding_TestVector_1_Plaintext_Padded,
                AES_128_ECB_PKCS5Padding_TestVector_1_Encrypted));
        CIPHER_TEST_PARAMS.add(new CipherTestParam("AES/CBC", AES_256_KEY,
                AES_256_CBC_PKCS5Padding_TestVector_1_IV,
                AES_256_CBC_PKCS5Padding_TestVector_1_Plaintext,
                AES_256_CBC_PKCS5Padding_TestVector_1_Plaintext_Padded,
                AES_256_CBC_PKCS5Padding_TestVector_1_Ciphertext));
    }

    public void testCipher_Success() throws Exception {
        final ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(errBuffer);
        for (CipherTestParam p : CIPHER_TEST_PARAMS) {
            try {
                checkCipher(p);
            } catch (Exception e) {
                out.append("Error encountered checking " + p.mode + ", keySize="
                        + (p.key.length * 8) + "\n");
                e.printStackTrace(out);
            }
        }
        out.flush();
        if (errBuffer.size() > 0) {
            throw new Exception("Errors encountered:\n\n" + errBuffer.toString() + "\n\n");
        }
    }

    private void checkCipher(CipherTestParam p) throws Exception {
        SecretKey key = new SecretKeySpec(p.key, "AES");
        Cipher c = Cipher.getInstance(p.mode + "/PKCS5Padding");
        AlgorithmParameterSpec spec = null;
        if (p.iv != null) {
            spec = new IvParameterSpec(p.iv);
        }
        c.init(Cipher.ENCRYPT_MODE, key, spec);

        final byte[] actualCiphertext = c.doFinal(p.plaintext);
        assertTrue(Arrays.equals(p.ciphertext, actualCiphertext));

        c.init(Cipher.DECRYPT_MODE, key, spec);

        final byte[] actualPlaintext = c.doFinal(p.ciphertext);
        assertTrue(Arrays.equals(p.plaintext, actualPlaintext));

        Cipher cNoPad = Cipher.getInstance(p.mode + "/NoPadding");
        cNoPad.init(Cipher.DECRYPT_MODE, key, spec);

        final byte[] actualPlaintextPadded = cNoPad.doFinal(p.ciphertext);
        assertTrue(Arrays.equals(p.plaintextPadded, actualPlaintextPadded));
    }

    public void testCipher_ShortBlock_Failure() throws Exception {
        final ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(errBuffer);
        for (CipherTestParam p : CIPHER_TEST_PARAMS) {
            try {
                checkCipher_ShortBlock_Failure(p);
            } catch (Exception e) {
                out.append("Error encountered checking " + p.mode + ", keySize="
                        + (p.key.length * 8) + "\n");
                e.printStackTrace(out);
            }
        }
        out.flush();
        if (errBuffer.size() > 0) {
            throw new Exception("Errors encountered:\n\n" + errBuffer.toString() + "\n\n");
        }
    }

    private void checkCipher_ShortBlock_Failure(CipherTestParam p) throws Exception {
        SecretKey key = new SecretKeySpec(p.key, "AES");
        Cipher c = Cipher.getInstance(p.mode + "/NoPadding");
        if (c.getBlockSize() == 0) {
            return;
        }

        c.init(Cipher.ENCRYPT_MODE, key);
        try {
            c.doFinal(new byte[] { 0x01, 0x02, 0x03 });
            fail("Should throw IllegalBlockSizeException on wrong-sized block");
        } catch (IllegalBlockSizeException expected) {
        }
    }

    public void testAES_ECB_PKCS5Padding_ShortBuffer_Failure() throws Exception {
        SecretKey key = new SecretKeySpec(AES_128_KEY, "AES");
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, key);

        final byte[] fragmentOutput = c.update(AES_128_ECB_PKCS5Padding_TestVector_1_Plaintext);
        if (fragmentOutput != null) {
            assertEquals(0, fragmentOutput.length);
        }

        // Provide null buffer.
        {
            try {
                c.doFinal(null, 0);
                fail("Should throw NullPointerException on null output buffer");
            } catch (NullPointerException expected) {
            } catch (IllegalArgumentException expected) {
            }
        }

        // Provide short buffer.
        {
            final byte[] output = new byte[c.getBlockSize() - 1];
            try {
                c.doFinal(output, 0);
                fail("Should throw ShortBufferException on short output buffer");
            } catch (ShortBufferException expected) {
            }
        }

        // Start 1 byte into output buffer.
        {
            final byte[] output = new byte[c.getBlockSize()];
            try {
                c.doFinal(output, 1);
                fail("Should throw ShortBufferException on short output buffer");
            } catch (ShortBufferException expected) {
            }
        }

        // Should keep data for real output buffer
        {
            final byte[] output = new byte[c.getBlockSize()];
            assertEquals(AES_128_ECB_PKCS5Padding_TestVector_1_Encrypted.length, c.doFinal(output, 0));
            assertTrue(Arrays.equals(AES_128_ECB_PKCS5Padding_TestVector_1_Encrypted, output));
        }
    }

    public void testAES_ECB_NoPadding_IncrementalUpdate_Success() throws Exception {
        SecretKey key = new SecretKeySpec(AES_128_KEY, "AES");
        Cipher c = Cipher.getInstance("AES/ECB/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, key);

        for (int i = 0; i < AES_128_ECB_PKCS5Padding_TestVector_1_Plaintext_Padded.length - 1; i++) {
            final byte[] outputFragment = c.update(AES_128_ECB_PKCS5Padding_TestVector_1_Plaintext_Padded, i, 1);
            if (outputFragment != null) {
                assertEquals(0, outputFragment.length);
            }
        }

        final byte[] output = c.doFinal(AES_128_ECB_PKCS5Padding_TestVector_1_Plaintext_Padded,
                AES_128_ECB_PKCS5Padding_TestVector_1_Plaintext_Padded.length - 1, 1);
        assertNotNull(output);
        assertEquals(AES_128_ECB_PKCS5Padding_TestVector_1_Plaintext_Padded.length, output.length);

        assertTrue(Arrays.equals(AES_128_ECB_PKCS5Padding_TestVector_1_Encrypted, output));
    }

    private static final byte[] AES_IV_ZEROES = new byte[] {
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
    };

    public void testAES_ECB_NoPadding_IvParameters_Failure() throws Exception {
        SecretKey key = new SecretKeySpec(AES_128_KEY, "AES");
        Cipher c = Cipher.getInstance("AES/ECB/NoPadding");

        AlgorithmParameterSpec spec = new IvParameterSpec(AES_IV_ZEROES);
        try {
            c.init(Cipher.ENCRYPT_MODE, key, spec);
            fail("Should not accept an IV in ECB mode");
        } catch (InvalidAlgorithmParameterException expected) {
        }
    }
}








//Synthetic comment -- diff --git a/support/src/test/java/libcore/java/security/StandardNames.java b/support/src/test/java/libcore/java/security/StandardNames.java
//Synthetic comment -- index eb53ba5..335cdab 100644

//Synthetic comment -- @@ -85,6 +85,13 @@
*/
public static final Map<String,Set<String>> PROVIDER_ALGORITHMS
= new HashMap<String,Set<String>>();
    
    public static final Map<String,Set<String>> CIPHER_MODES
            = new HashMap<String,Set<String>>();

    public static final Map<String,Set<String>> CIPHER_PADDINGS
            = new HashMap<String,Set<String>>();

private static void provide(String type, String algorithm) {
Set<String> algorithms = PROVIDER_ALGORITHMS.get(type);
if (algorithms == null) {
//Synthetic comment -- @@ -102,6 +109,22 @@
assertNotNull(PROVIDER_ALGORITHMS.remove(type));
}
}
    private static void provideCipherModes(String algorithm, String newModes[]) {
        Set<String> modes = CIPHER_MODES.get(algorithm);
        if (modes == null) {
            modes = new HashSet<String>();
            CIPHER_MODES.put(algorithm, modes);
        }
        modes.addAll(Arrays.asList(newModes));
    }
    private static void provideCipherPaddings(String algorithm, String newPaddings[]) {
        Set<String> paddings = CIPHER_MODES.get(algorithm);
        if (paddings == null) {
            paddings = new HashSet<String>();
            CIPHER_MODES.put(algorithm, paddings);
        }
        paddings.addAll(Arrays.asList(newPaddings));
    }
static {
provide("AlgorithmParameterGenerator", "DSA");
provide("AlgorithmParameterGenerator", "DiffieHellman");
//Synthetic comment -- @@ -123,6 +146,8 @@
provide("CertStore", "LDAP");
provide("CertificateFactory", "X.509");
provide("Cipher", "AES");
        provideCipherModes("AES", new String[] { "CBC", "CFB", "CTR", "CTS", "ECB", "OFB" });
        provideCipherPaddings("AES", new String[] { "NoPadding", "PKCS5Padding" });
provide("Cipher", "AESWrap");
provide("Cipher", "ARCFOUR");
provide("Cipher", "Blowfish");
//Synthetic comment -- @@ -850,4 +875,18 @@
assertValidCipherSuites(CIPHER_SUITES, cipherSuites);
assertEquals(CIPHER_SUITES_DEFAULT, Arrays.asList(cipherSuites));
}

    /**
     * Get all supported mode names for the given cipher.
     */
    public static Set<String> getModesForCipher(String cipher) {
        return CIPHER_MODES.get(cipher);
    }

    /**
     * Get all supported padding names for the given cipher.
     */
    public static Set<String> getPaddingsForCipher(String cipher) {
        return CIPHER_PADDINGS.get(cipher);
    }
}







