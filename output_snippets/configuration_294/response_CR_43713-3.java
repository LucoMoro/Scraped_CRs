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

    private static final Map<String, Integer> EXPECTED_BLOCK_SIZE = new HashMap<>();
    private static final Map<String, Integer> EXPECTED_OUTPUT_SIZE = new HashMap<>();
    private static final byte[] ORIGINAL_PLAIN_TEXT = "Hello".getBytes();
    private static final byte[] PKCS1_BLOCK_TYPE_00_PADDED_PLAIN_TEXT = "Padded".getBytes(); // Example value
    private static final byte[] RSA_2048_modulus = new byte[256]; // Example RSA modulus
    private static final byte[] RSA_2048_privateExponent = new byte[256]; // Example RSA private exponent
    private static final byte[] RSA_2048_publicExponent = new byte[256]; // Example RSA public exponent

    static {
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

    private static boolean isUnsupported(String algorithm) {
        return algorithm.matches("PBEWITHMD5ANDRC2|PBEWITHSHA1ANDRC2|PBEWITHSHAAND40BITRC2-CBC");
    }

    private static String getBaseAlgoritm(String algorithm) {
        switch (algorithm) {
            case "AESWRAP":
            case "PBEWITHMD5AND128BITAES-CBC-OPENSSL":
                return "AES";
            case "DESEDEWRAP":
            case "PBEWITHSHAAND2-KEYTRIPLEDES-CBC":
            case "PBEWITHSHAAND3-KEYTRIPLEDES-CBC":
                return "DESede";
            case "RSA/ECB/NoPadding":
            case "RSA/ECB/PKCS1Padding":
                return "RSA";
            default:
                return "DES";
        }
    }

    private static boolean isAsymmetric(String algorithm) {
        return getBaseAlgoritm(algorithm).equals("RSA");
    }

    private static boolean isWrap(String algorithm) {
        String baseAlgorithm = getBaseAlgoritm(algorithm);
        return baseAlgorithm.equals("RSA");
    }

    private static int getExpectedBlockSize(String algorithm) {
        Integer expected = EXPECTED_BLOCK_SIZE.get(algorithm);
        assertNotNull(algorithm, expected);
        return expected;
    }

    private static int getExpectedOutputSize(String algorithm) {
        Integer expected = EXPECTED_OUTPUT_SIZE.get(algorithm);
        assertNotNull(algorithm, expected);
        return expected;
    }

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
            for (Service service : services) {
                if (!service.getType().equals("Cipher")) {
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
        String algorithm = c.getAlgorithm();
        if (isUnsupported(algorithm)) {
            return;
        }

        Key encryptKey = getEncryptKey(algorithm);
        c.init(Cipher.ENCRYPT_MODE, encryptKey);

        assertEquals(getExpectedBlockSize(algorithm), c.getBlockSize());
        assertEquals(getExpectedOutputSize(algorithm), c.getOutputSize(0));

        // Testing Cipher.getIV()
        // Add randomized data tests
        byte[] randomData = new byte[128]; // Example random data
        new java.util.Random().nextBytes(randomData);
        c.init(Cipher.ENCRYPT_MODE, encryptKey);
        byte[] cipherText = c.doFinal(randomData);
        Cipher decryptCipher = Cipher.getInstance(algorithm);
        Key decryptKey = getDecryptKey(algorithm);
        decryptCipher.init(Cipher.DECRYPT_MODE, decryptKey);
        byte[] plainText = decryptCipher.doFinal(cipherText);
        assertTrue(Arrays.equals(randomData, plainText)); // Validate data integrity
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