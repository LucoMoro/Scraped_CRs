//<Beginning of snippet n. 0>


package libcore.javax.crypto;

import java.security.Provider;
import java.security.Security;
import java.security.Key;
import javax.crypto.Cipher;
import junit.framework.TestCase;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class CipherTest extends TestCase {
    private static final Map<String, Integer> EXPECTED_BLOCK_SIZE = new HashMap<>();
    private static final Map<String, Integer> EXPECTED_OUTPUT_SIZE = new HashMap<>();
    
    static {
        EXPECTED_BLOCK_SIZE.put("AES", 128);
        EXPECTED_BLOCK_SIZE.put("RSA", 256);
        
        EXPECTED_OUTPUT_SIZE.put("AES", 128);
        EXPECTED_OUTPUT_SIZE.put("RSA", 256);
    }

    private static String getBaseAlgorithm(String algorithm) {
        if (algorithm.equals("AES/CBC/PKCS5Padding") || algorithm.equals("AES/GCM/NoPadding")) {
            return "AES";
        }
        return "DES";
    }

    private static boolean isAsymmetric(String algorithm) {
        return getBaseAlgorithm(algorithm).equals("RSA");
    }

    private static boolean isUnsupported(String algorithm) {
        return algorithm.contains("ECB") || 
               algorithm.equals("RSA/NoPadding") || 
               algorithm.equals("RSA/PKCS1Padding");
    }

    private static int getExpectedBlockSize(String algorithm) {
        Integer expected = EXPECTED_BLOCK_SIZE.get(algorithm);
        assertNotNull("Algorithm not supported", expected);
        return expected;
    }

    private static int getExpectedOutputSize(String algorithm) {
        Integer expected = EXPECTED_OUTPUT_SIZE.get(algorithm);
        assertNotNull("Algorithm not supported", expected);
        return expected;
    }

    private static byte[] getExpectedPlainText(String algorithm) {
        return "RSA/NoPadding".equals(algorithm) ? 
            PKCS1_BLOCK_TYPE_00_PADDED_PLAIN_TEXT : ORIGINAL_PLAIN_TEXT;
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
        if (isUnsupported(algorithm)) {
            fail("Unsupported algorithm: " + algorithm);
        }
        try {
            Key encryptKey = getEncryptKey(algorithm);
            c.init(Cipher.ENCRYPT_MODE, encryptKey);

            assertEquals(getExpectedBlockSize(algorithm), c.getBlockSize());
            assertEquals(getExpectedOutputSize(algorithm), c.getOutputSize(0));

            if (algorithm.contains("CBC")) {
                assertNotNull("IV should not be null for CBC mode", c.getIV());
            }

            byte[] encryptedData = c.doFinal(getExpectedPlainText(algorithm));
            assertTrue(validateIntegrity(encryptedData));

            if (algorithm.contains("ECB")) {
                fail("ECB mode should not be used, identified vulnerability.");
            }
        } catch (IllegalStateException expected) {
            fail("IllegalStateException encountered, potentially unsupported mode.");
        } catch (Exception e) {
            fail("Unexpected error during cipher test: " + e.getMessage());
        }
    }
}

//<End of snippet n. 0>
