/*Add more CipherTest tests

Change-Id:I29f55e41335021945029e410d4e51e2c8f564285*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/crypto/CipherTest.java b/luni/src/test/java/libcore/javax/crypto/CipherTest.java
//Synthetic comment -- index d31825a..76a4176 100644

//Synthetic comment -- @@ -17,37 +17,60 @@
package libcore.javax.crypto;

import com.android.org.bouncycastle.asn1.x509.KeyUsage;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Provider.Service;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.IllegalBlockSizeException;
import junit.framework.TestCase;
import libcore.java.security.TestKeyStore;

public final class CipherTest extends TestCase {

private static boolean isUnsupported(String algorithm) {
if (algorithm.equals("PBEWITHMD5ANDRC2")) {
return true;
}
        if (algorithm.equals("PBEWITHSHA1ANDRC2")) {
return true;
}
if (algorithm.equals("PBEWITHSHAAND40BITRC2-CBC")) {
//Synthetic comment -- @@ -76,10 +99,13 @@
return Cipher.DECRYPT_MODE;
}

    private static String getBaseAlgoritm(String algorithm) {
if (algorithm.equals("AESWRAP")) {
return "AES";
}
if (algorithm.equals("PBEWITHMD5AND128BITAES-CBC-OPENSSL")) {
return "AES";
}
//Synthetic comment -- @@ -114,18 +140,24 @@
return "DES";
}
if (algorithm.equals("DESEDEWRAP")) {
            return "DESede";
}
if (algorithm.equals("PBEWITHSHAAND2-KEYTRIPLEDES-CBC")) {
            return "DESede";
}
if (algorithm.equals("PBEWITHSHAAND3-KEYTRIPLEDES-CBC")) {
            return "DESede";
}
        if (algorithm.equals("RSA/ECB/NoPadding")) {
return "RSA";
}
        if (algorithm.equals("RSA/ECB/PKCS1Padding")) {
return "RSA";
}
if (algorithm.equals("PBEWITHSHAAND40BITRC4")) {
//Synthetic comment -- @@ -138,27 +170,33 @@
}

private static boolean isAsymmetric(String algorithm) {
        return getBaseAlgoritm(algorithm).equals("RSA");
}

private static boolean isWrap(String algorithm) {
return algorithm.endsWith("WRAP");
}

private static Map<String, Key> ENCRYPT_KEYS = new HashMap<String, Key>();
private synchronized static Key getEncryptKey(String algorithm) throws Exception {
Key key = ENCRYPT_KEYS.get(algorithm);
if (key != null) {
return key;
}
        algorithm = getBaseAlgoritm(algorithm);
        if (algorithm.equals("RSA")) {
KeyFactory kf = KeyFactory.getInstance("RSA");
RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(RSA_2048_modulus,
RSA_2048_privateExponent);
key = kf.generatePrivate(keySpec);
} else {
            KeyGenerator kg = KeyGenerator.getInstance(algorithm);
key = kg.generateKey();
}
ENCRYPT_KEYS.put(algorithm, key);
//Synthetic comment -- @@ -171,8 +209,7 @@
if (key != null) {
return key;
}
        algorithm = getBaseAlgoritm(algorithm);
        if (algorithm.equals("RSA")) {
KeyFactory kf = KeyFactory.getInstance("RSA");
RSAPublicKeySpec keySpec = new RSAPublicKeySpec(RSA_2048_modulus,
RSA_2048_publicExponent);
//Synthetic comment -- @@ -198,9 +235,15 @@
EXPECTED_BLOCK_SIZE.put("PBEWITHSHAAND192BITAES-CBC-BC", 16);
EXPECTED_BLOCK_SIZE.put("PBEWITHSHAAND256BITAES-CBC-BC", 16);

        EXPECTED_BLOCK_SIZE.put("AESWRAP", 0);

EXPECTED_BLOCK_SIZE.put("ARC4", 0);
EXPECTED_BLOCK_SIZE.put("PBEWITHSHAAND40BITRC4", 0);
EXPECTED_BLOCK_SIZE.put("PBEWITHSHAAND128BITRC4", 0);

//Synthetic comment -- @@ -208,24 +251,51 @@

EXPECTED_BLOCK_SIZE.put("DES", 8);
EXPECTED_BLOCK_SIZE.put("PBEWITHMD5ANDDES", 8);
EXPECTED_BLOCK_SIZE.put("PBEWITHSHA1ANDDES", 8);

EXPECTED_BLOCK_SIZE.put("DESEDE", 8);
EXPECTED_BLOCK_SIZE.put("PBEWITHSHAAND2-KEYTRIPLEDES-CBC", 8);
EXPECTED_BLOCK_SIZE.put("PBEWITHSHAAND3-KEYTRIPLEDES-CBC", 8);

        EXPECTED_BLOCK_SIZE.put("DESEDEWRAP", 0);

        EXPECTED_BLOCK_SIZE.put("RSA", 255);
        EXPECTED_BLOCK_SIZE.put("RSA/ECB/NoPadding", 0);
        EXPECTED_BLOCK_SIZE.put("RSA/ECB/PKCS1Padding", 0);
}
    private static int getExpectedBlockSize(String algorithm) {
Integer expected = EXPECTED_BLOCK_SIZE.get(algorithm);
assertNotNull(algorithm, expected);
return expected;
}

private static Map<String, Integer> EXPECTED_OUTPUT_SIZE = new HashMap<String, Integer>();
static {
EXPECTED_OUTPUT_SIZE.put("AES", 16);
//Synthetic comment -- @@ -239,9 +309,15 @@
EXPECTED_OUTPUT_SIZE.put("PBEWITHSHAAND192BITAES-CBC-BC", 16);
EXPECTED_OUTPUT_SIZE.put("PBEWITHSHAAND256BITAES-CBC-BC", 16);

        EXPECTED_OUTPUT_SIZE.put("AESWRAP", -1);

EXPECTED_OUTPUT_SIZE.put("ARC4", 0);
EXPECTED_OUTPUT_SIZE.put("PBEWITHSHAAND40BITRC4", 0);
EXPECTED_OUTPUT_SIZE.put("PBEWITHSHAAND128BITRC4", 0);

//Synthetic comment -- @@ -249,19 +325,31 @@

EXPECTED_OUTPUT_SIZE.put("DES", 8);
EXPECTED_OUTPUT_SIZE.put("PBEWITHMD5ANDDES", 8);
EXPECTED_OUTPUT_SIZE.put("PBEWITHSHA1ANDDES", 8);

EXPECTED_OUTPUT_SIZE.put("DESEDE", 8);
EXPECTED_OUTPUT_SIZE.put("PBEWITHSHAAND2-KEYTRIPLEDES-CBC", 8);
EXPECTED_OUTPUT_SIZE.put("PBEWITHSHAAND3-KEYTRIPLEDES-CBC", 8);

        EXPECTED_OUTPUT_SIZE.put("DESEDEWRAP", -1);

        EXPECTED_OUTPUT_SIZE.put("RSA", 256);
        EXPECTED_OUTPUT_SIZE.put("RSA/ECB/NoPadding", 256);
        EXPECTED_OUTPUT_SIZE.put("RSA/ECB/PKCS1Padding", 256);
}
    private static int getExpectedOutputSize(String algorithm) {
Integer expected = EXPECTED_OUTPUT_SIZE.get(algorithm);
assertNotNull(algorithm, expected);
return expected;
//Synthetic comment -- @@ -356,13 +444,19 @@
};

private static byte[] getExpectedPlainText(String algorithm) {
        if (algorithm.equals("RSA/ECB/NoPadding")) {
return PKCS1_BLOCK_TYPE_00_PADDED_PLAIN_TEXT;
}
return ORIGINAL_PLAIN_TEXT;
}

public void test_getInstance() throws Exception {
Provider[] providers = Security.getProviders();
for (Provider provider : providers) {
Set<Provider.Service> services = provider.getServices();
//Synthetic comment -- @@ -371,34 +465,84 @@
if (!type.equals("Cipher")) {
continue;
}
String algorithm = service.getAlgorithm();
try {
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
} catch (Throwable e) {
                    throw new Exception("Problem testing Cipher." + algorithm, e);
}
}
}
}

private void test_Cipher(Cipher c) throws Exception {
        // TODO: test all supported modes and padding for a given algorithm
        String algorithm = c.getAlgorithm();
if (isUnsupported(algorithm)) {
return;
}
//Synthetic comment -- @@ -410,11 +554,21 @@

// TODO: test keys from different factories (e.g. OpenSSLRSAPrivateKey vs JCERSAPrivateKey)
Key encryptKey = getEncryptKey(algorithm);
        c.init(getEncryptMode(algorithm), encryptKey);

        assertEquals(getExpectedBlockSize(algorithm), c.getBlockSize());

        assertEquals(getExpectedOutputSize(algorithm), c.getOutputSize(0));

// TODO: test Cipher.getIV()

//Synthetic comment -- @@ -434,7 +588,7 @@
encryptKey, decryptedKey);
} else {
byte[] cipherText = c.doFinal(ORIGINAL_PLAIN_TEXT);
            c.init(getDecryptMode(algorithm), getDecryptKey(algorithm));
byte[] decryptedPlainText = c.doFinal(cipherText);
assertEquals(Arrays.toString(getExpectedPlainText(algorithm)),
Arrays.toString(decryptedPlainText));
//Synthetic comment -- @@ -479,12 +633,13 @@
Cipher decryptCipher = Cipher.getInstance("RSA/ECB/NoPadding");
decryptCipher.init(Cipher.DECRYPT_MODE, decryptKey);
byte[] plainText = decryptCipher.doFinal(cipherText);
        assertPadding(expectedBlockType, ORIGINAL_PLAIN_TEXT, plainText);
}

    private void assertPadding(byte expectedBlockType, byte[] expectedData, byte[] actualDataWithPadding) {
assertNotNull(actualDataWithPadding);
        assertEquals(getExpectedOutputSize("RSA"), actualDataWithPadding.length);
assertEquals(0, actualDataWithPadding[0]);
byte actualBlockType = actualDataWithPadding[1];
assertEquals(expectedBlockType, actualBlockType);
//Synthetic comment -- @@ -1353,4 +1508,344 @@
Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");
assertNull("Parameters should be null", c.getParameters());
}
}








//Synthetic comment -- diff --git a/support/src/test/java/libcore/java/security/StandardNames.java b/support/src/test/java/libcore/java/security/StandardNames.java
//Synthetic comment -- index eb53ba5..335cdab 100644

//Synthetic comment -- @@ -85,6 +85,13 @@
*/
public static final Map<String,Set<String>> PROVIDER_ALGORITHMS
= new HashMap<String,Set<String>>();
private static void provide(String type, String algorithm) {
Set<String> algorithms = PROVIDER_ALGORITHMS.get(type);
if (algorithms == null) {
//Synthetic comment -- @@ -102,6 +109,22 @@
assertNotNull(PROVIDER_ALGORITHMS.remove(type));
}
}
static {
provide("AlgorithmParameterGenerator", "DSA");
provide("AlgorithmParameterGenerator", "DiffieHellman");
//Synthetic comment -- @@ -123,6 +146,8 @@
provide("CertStore", "LDAP");
provide("CertificateFactory", "X.509");
provide("Cipher", "AES");
provide("Cipher", "AESWrap");
provide("Cipher", "ARCFOUR");
provide("Cipher", "Blowfish");
//Synthetic comment -- @@ -850,4 +875,18 @@
assertValidCipherSuites(CIPHER_SUITES, cipherSuites);
assertEquals(CIPHER_SUITES_DEFAULT, Arrays.asList(cipherSuites));
}
}







