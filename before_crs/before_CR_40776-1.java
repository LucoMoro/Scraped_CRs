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
        System.arraycopy(output, 0, b, outputOffset, b.length);
return b.length;
}
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/crypto/CipherTest.java b/luni/src/test/java/libcore/javax/crypto/CipherTest.java
//Synthetic comment -- index a94a9d0..0a5ed36 100644

//Synthetic comment -- @@ -567,6 +567,37 @@
Arrays.equals(RSA_Vector1_Encrypt_Private, encrypted));
}

public void testRSA_ECB_NoPadding_Public_OnlyDoFinal_Success() throws Exception {
KeyFactory kf = KeyFactory.getInstance("RSA");
RSAPublicKeySpec keySpec = new RSAPublicKeySpec(RSA_2048_modulus, RSA_2048_publicExponent);
//Synthetic comment -- @@ -591,6 +622,33 @@
Arrays.equals(RSA_2048_Vector1, encrypted));
}

public void testRSA_ECB_NoPadding_Public_UpdateThenEmptyDoFinal_Success() throws Exception {
KeyFactory kf = KeyFactory.getInstance("RSA");
RSAPublicKeySpec keySpec = new RSAPublicKeySpec(RSA_2048_modulus, RSA_2048_publicExponent);








//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/crypto/tests/javax/crypto/func/CipherRSATest.java b/luni/src/test/java/org/apache/harmony/crypto/tests/javax/crypto/func/CipherRSATest.java
//Synthetic comment -- index d25d958..0e070f6 100644

//Synthetic comment -- @@ -15,8 +15,6 @@
*/
package org.apache.harmony.crypto.tests.javax.crypto.func;

import dalvik.annotation.AndroidOnly;

import junit.framework.TestCase;

public class CipherRSATest extends TestCase {
//Synthetic comment -- @@ -55,7 +53,6 @@
assertEquals(rsa.getFailureMessages(), 0, rsa.getTotalFailuresNumber());
}

    @AndroidOnly("Fails on RI but succeeds on Android.")
public void test_RSANoPadding() {
CipherRSAThread rsa = new CipherRSAThread("RSA", new int[] {1024},
new String[] {"ECB"}, new String[] {"NOPADDING"});








//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/crypto/tests/javax/crypto/func/CipherRSAThread.java b/luni/src/test/java/org/apache/harmony/crypto/tests/javax/crypto/func/CipherRSAThread.java
//Synthetic comment -- index f7445b1..31e1075 100644

//Synthetic comment -- @@ -45,6 +45,10 @@
cip.init(Cipher.DECRYPT_MODE, kp.getPrivate());
cip.doFinal(output, 0, outputSize, decrypted);

        checkEncodedData(input, decrypted);
}
}








//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/crypto/tests/javax/crypto/func/CipherThread.java b/luni/src/test/java/org/apache/harmony/crypto/tests/javax/crypto/func/CipherThread.java
//Synthetic comment -- index 4dac176..2fd388b 100644

//Synthetic comment -- @@ -57,6 +57,20 @@
}
}

public void launcher() {
Thread thread = null;








