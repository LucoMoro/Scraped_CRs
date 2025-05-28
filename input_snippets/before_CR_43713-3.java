
//<Beginning of snippet n. 0>


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
return Cipher.DECRYPT_MODE;
}

    private static String getBaseAlgoritm(String algorithm) {
if (algorithm.equals("AESWRAP")) {
return "AES";
}
if (algorithm.equals("PBEWITHMD5AND128BITAES-CBC-OPENSSL")) {
return "AES";
}
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
}

private static boolean isAsymmetric(String algorithm) {
        return getBaseAlgoritm(algorithm).equals("RSA");
}

private static boolean isWrap(String algorithm) {
if (key != null) {
return key;
}
        algorithm = getBaseAlgoritm(algorithm);
        if (algorithm.equals("RSA")) {
KeyFactory kf = KeyFactory.getInstance("RSA");
RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(RSA_2048_modulus,
RSA_2048_privateExponent);
if (key != null) {
return key;
}
        algorithm = getBaseAlgoritm(algorithm);
        if (algorithm.equals("RSA")) {
KeyFactory kf = KeyFactory.getInstance("RSA");
RSAPublicKeySpec keySpec = new RSAPublicKeySpec(RSA_2048_modulus,
RSA_2048_publicExponent);
EXPECTED_BLOCK_SIZE.put("PBEWITHSHAAND192BITAES-CBC-BC", 16);
EXPECTED_BLOCK_SIZE.put("PBEWITHSHAAND256BITAES-CBC-BC", 16);

        EXPECTED_BLOCK_SIZE.put("AESWRAP", 0);

EXPECTED_BLOCK_SIZE.put("ARC4", 0);
EXPECTED_BLOCK_SIZE.put("PBEWITHSHAAND40BITRC4", 0);
EXPECTED_BLOCK_SIZE.put("PBEWITHSHAAND128BITRC4", 0);

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
EXPECTED_OUTPUT_SIZE.put("PBEWITHSHAAND192BITAES-CBC-BC", 16);
EXPECTED_OUTPUT_SIZE.put("PBEWITHSHAAND256BITAES-CBC-BC", 16);

        EXPECTED_OUTPUT_SIZE.put("AESWRAP", -1);

EXPECTED_OUTPUT_SIZE.put("ARC4", 0);
EXPECTED_OUTPUT_SIZE.put("PBEWITHSHAAND40BITRC4", 0);
EXPECTED_OUTPUT_SIZE.put("PBEWITHSHAAND128BITRC4", 0);

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

Key encryptKey = getEncryptKey(algorithm);
c.init(getEncryptMode(algorithm), encryptKey);

        assertEquals(getExpectedBlockSize(algorithm), c.getBlockSize());

        assertEquals(getExpectedOutputSize(algorithm), c.getOutputSize(0));

// TODO: test Cipher.getIV()

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
Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");
assertNull("Parameters should be null", c.getParameters());
}
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


*/
public static final Map<String,Set<String>> PROVIDER_ALGORITHMS
= new HashMap<String,Set<String>>();
private static void provide(String type, String algorithm) {
Set<String> algorithms = PROVIDER_ALGORITHMS.get(type);
if (algorithms == null) {
assertNotNull(PROVIDER_ALGORITHMS.remove(type));
}
}
static {
provide("AlgorithmParameterGenerator", "DSA");
provide("AlgorithmParameterGenerator", "DiffieHellman");
provide("CertStore", "LDAP");
provide("CertificateFactory", "X.509");
provide("Cipher", "AES");
provide("Cipher", "AESWrap");
provide("Cipher", "ARCFOUR");
provide("Cipher", "Blowfish");
assertValidCipherSuites(CIPHER_SUITES, cipherSuites);
assertEquals(CIPHER_SUITES_DEFAULT, Arrays.asList(cipherSuites));
}
}

//<End of snippet n. 1>








