//<Beginning of snippet n. 0>


package libcore.javax.crypto;

import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Provider;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.IllegalBlockSizeException;
import junit.framework.TestCase;

public final class CipherTest extends TestCase {

    private static final Map<String, Integer> EXPECTED_BLOCK_SIZE = new HashMap<>();
    private static final Map<String, Integer> EXPECTED_OUTPUT_SIZE = new HashMap<>();
    private static final String ORIGINAL_PLAIN_TEXT = "originalPlainText";

    static {
        EXPECTED_BLOCK_SIZE.put("DESEDEWRAP", 0);
        EXPECTED_BLOCK_SIZE.put("AES", 128);      
        EXPECTED_OUTPUT_SIZE.put("DESEDEWRAP", -1);
        EXPECTED_OUTPUT_SIZE.put("AES", 128);
    }

    private static String getBaseAlgorithm(String algorithm) {
        if (algorithm.equals("AESWRAP")) {
            return "AES";
        }
        if (algorithm.equals("PBEWITHMD5AND128BITAES-CBC-OPENSSL")) {
            return "AES";
        }
        if (algorithm.equals("DESEDEWRAP") || 
            algorithm.equals("PBEWITHSHAAND2-KEYTRIPLEDES-CBC") || 
            algorithm.equals("PBEWITHSHAAND3-KEYTRIPLEDES-CBC")) {
            return "DESede";
        }
        return "DES";
    }

    private static boolean isAsymmetric(String algorithm) {
        return getBaseAlgorithm(algorithm).equals("RSA");
    }

    private static int getExpectedBlockSize(String algorithm) {
        Integer expected = EXPECTED_BLOCK_SIZE.get(algorithm);
        assertNotNull("Expected block size not found for algorithm: " + algorithm, expected);
        return expected;
    }

    private static int getExpectedOutputSize(String algorithm) {
        Integer expected = EXPECTED_OUTPUT_SIZE.get(algorithm);
        assertNotNull("Expected output size not found for algorithm: " + algorithm, expected);
        return expected;
    }

    private static byte[] getExpectedPlainText(String algorithm) {
        return ORIGINAL_PLAIN_TEXT.getBytes();
    }

    public void test_getInstance() throws Exception {
        Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            Set<Provider.Service> services = provider.getServices();
            for (Provider.Service service : services) {
                if (!service.getType().equals("Cipher")) {
                    continue;
                }
                String algorithm = service.getAlgorithm();
                try {
                    Cipher c1 = Cipher.getInstance(algorithm);
                    assertEquals(algorithm, c1.getAlgorithm());
                    test_Cipher(c1);

                    Cipher c2 = Cipher.getInstance(algorithm, provider);
                    assertEquals(algorithm, c2.getAlgorithm());
                    assertEquals(provider, c2.getProvider());
                    test_Cipher(c2);

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
        Key encryptKey = KeyGenerator.getInstance(getBaseAlgorithm(algorithm)).generateKey();
        c.init(Cipher.ENCRYPT_MODE, encryptKey);
        byte[] originalData = getExpectedPlainText(algorithm);
        byte[] encryptedData = c.doFinal(originalData);
        
        c.init(Cipher.DECRYPT_MODE, encryptKey);
        byte[] decryptedData = c.doFinal(encryptedData);

        assertEquals(getExpectedBlockSize(algorithm), c.getBlockSize());
        assertEquals(getExpectedOutputSize(algorithm), c.getOutputSize(0));
        assertNull("Parameters should be null", c.getParameters());
        assertTrue("Decrypted data does not match original", Arrays.equals(originalData, decryptedData));
        
        // Additional edge case tests
        // Test for null and invalid key scenarios
        try {
            c.init(Cipher.ENCRYPT_MODE, null);
            fail("Expected exception not thrown for null key");
        } catch (Exception expected) {
        }
    }
}

//<End of snippet n. 0>


//<Beginning of snippet n. 1>

public static final Map<String, Set<String>> PROVIDER_ALGORITHMS = new HashMap<String, Set<String>>();

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
    provide("Cipher", "Blowfish");
    assertValidCipherSuites(CIPHER_SUITES, cipherSuites);
    assertEquals(CIPHER_SUITES_DEFAULT, Arrays.asList(cipherSuites));
}

//<End of snippet n. 1>