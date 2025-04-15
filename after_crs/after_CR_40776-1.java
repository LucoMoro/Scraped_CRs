/*Fix OpenSSLCipherRawRSA doFinal array copy

System.arraycopy was pointing the wrong way making calls to doFinal()
with offset markers get zeroed output instead of the actual output.

Also fix tests that checked RSA cipher behavior to match RI.

Bug: 6951038
Change-Id:Ife84c177a2c06a2c27b98df9960cbd3c4b62d984*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLCipherRawRSA.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLCipherRawRSA.java
//Synthetic comment -- index 8312013..d0368fe 100644

//Synthetic comment -- @@ -221,7 +221,14 @@
int outputOffset) throws ShortBufferException, IllegalBlockSizeException,
BadPaddingException {
byte[] b = engineDoFinal(input, inputOffset, inputLen);

        final int lastOffset = outputOffset + b.length;
        if (lastOffset > output.length) {
            throw new ShortBufferException("output buffer is too small " + output.length + " < "
                    + lastOffset);
        }

        System.arraycopy(b, 0, output, outputOffset, b.length);
return b.length;
}
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/crypto/CipherTest.java b/luni/src/test/java/libcore/javax/crypto/CipherTest.java
//Synthetic comment -- index a94a9d0..0a5ed36 100644

//Synthetic comment -- @@ -567,6 +567,37 @@
Arrays.equals(RSA_Vector1_Encrypt_Private, encrypted));
}

    public void testRSA_ECB_NoPadding_Private_OnlyDoFinalWithOffset_Success() throws Exception {
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
        byte[] encrypted = new byte[RSA_Vector1_Encrypt_Private.length];
        final int encryptLen = c
                .doFinal(RSA_2048_Vector1, 0, RSA_2048_Vector1.length, encrypted, 0);
        assertEquals("Encrypted size should match expected", RSA_Vector1_Encrypt_Private.length,
                encryptLen);
        assertTrue("Encrypted should match expected",
                Arrays.equals(RSA_Vector1_Encrypt_Private, encrypted));

        c.init(Cipher.DECRYPT_MODE, privKey);
        final int decryptLen = c
                .doFinal(RSA_2048_Vector1, 0, RSA_2048_Vector1.length, encrypted, 0);
        assertEquals("Encrypted size should match expected", RSA_Vector1_Encrypt_Private.length,
                decryptLen);
        assertTrue("Encrypted should match expected",
                Arrays.equals(RSA_Vector1_Encrypt_Private, encrypted));
    }

public void testRSA_ECB_NoPadding_Public_OnlyDoFinal_Success() throws Exception {
KeyFactory kf = KeyFactory.getInstance("RSA");
RSAPublicKeySpec keySpec = new RSAPublicKeySpec(RSA_2048_modulus, RSA_2048_publicExponent);
//Synthetic comment -- @@ -591,6 +622,33 @@
Arrays.equals(RSA_2048_Vector1, encrypted));
}

    public void testRSA_ECB_NoPadding_Public_OnlyDoFinalWithOffset_Success() throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(RSA_2048_modulus, RSA_2048_publicExponent);

        final PublicKey pubKey = kf.generatePublic(keySpec);

        Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");

        /*
         * You're actually encrypting with public keys, but there is no
         * distinction made here. It's all keyed off of what kind of key you're
         * using. ENCRYPT_MODE and DECRYPT_MODE are the same.
         */
        c.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] encrypted = new byte[RSA_2048_Vector1.length];
        final int encryptLen = c.doFinal(RSA_Vector1_Encrypt_Private, 0,
                RSA_Vector1_Encrypt_Private.length, encrypted, 0);
        assertEquals("Encrypted size should match expected", RSA_2048_Vector1.length, encryptLen);
        assertTrue("Encrypted should match expected", Arrays.equals(RSA_2048_Vector1, encrypted));

        c.init(Cipher.DECRYPT_MODE, pubKey);
        final int decryptLen = c.doFinal(RSA_Vector1_Encrypt_Private, 0,
                RSA_Vector1_Encrypt_Private.length, encrypted, 0);
        assertEquals("Encrypted size should match expected", RSA_2048_Vector1.length, decryptLen);
        assertTrue("Encrypted should match expected", Arrays.equals(RSA_2048_Vector1, encrypted));
    }

public void testRSA_ECB_NoPadding_Public_UpdateThenEmptyDoFinal_Success() throws Exception {
KeyFactory kf = KeyFactory.getInstance("RSA");
RSAPublicKeySpec keySpec = new RSAPublicKeySpec(RSA_2048_modulus, RSA_2048_publicExponent);








//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/crypto/tests/javax/crypto/func/CipherRSATest.java b/luni/src/test/java/org/apache/harmony/crypto/tests/javax/crypto/func/CipherRSATest.java
//Synthetic comment -- index d25d958..0e070f6 100644

//Synthetic comment -- @@ -15,8 +15,6 @@
*/
package org.apache.harmony.crypto.tests.javax.crypto.func;

import junit.framework.TestCase;

public class CipherRSATest extends TestCase {
//Synthetic comment -- @@ -55,7 +53,6 @@
assertEquals(rsa.getFailureMessages(), 0, rsa.getTotalFailuresNumber());
}

public void test_RSANoPadding() {
CipherRSAThread rsa = new CipherRSAThread("RSA", new int[] {1024},
new String[] {"ECB"}, new String[] {"NOPADDING"});








//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/crypto/tests/javax/crypto/func/CipherRSAThread.java b/luni/src/test/java/org/apache/harmony/crypto/tests/javax/crypto/func/CipherRSAThread.java
//Synthetic comment -- index f7445b1..31e1075 100644

//Synthetic comment -- @@ -45,6 +45,10 @@
cip.init(Cipher.DECRYPT_MODE, kp.getPrivate());
cip.doFinal(output, 0, outputSize, decrypted);

        if ("NOPADDING".equals(getPadding())) {
            checkPaddedEncodedData(input, decrypted, outputSize - input.length);
        } else {
            checkEncodedData(input, decrypted);
        }
}
}








//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/crypto/tests/javax/crypto/func/CipherThread.java b/luni/src/test/java/org/apache/harmony/crypto/tests/javax/crypto/func/CipherThread.java
//Synthetic comment -- index 4dac176..2fd388b 100644

//Synthetic comment -- @@ -57,6 +57,20 @@
}
}

    public void checkPaddedEncodedData(byte[] original, byte[] encoded, int offset)
            throws Exception {
        for (int i = 0; i < offset; i++) {
            if (encoded[i] != 0) {
                throw new Exception("Encoded data is not properly padded at offset " + i);
            }
        }
        for (int i = 0; i < original.length; i++) {
            if (original[i] != encoded[i + offset]) {
                throw new Exception("Source and encoded data not match " + getCipherParameters());
            }
        }
    }

public void launcher() {
Thread thread = null;








