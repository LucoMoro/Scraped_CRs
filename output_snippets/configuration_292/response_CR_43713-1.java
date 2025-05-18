//<Beginning of snippet n. 0>

package libcore.javax.crypto;

import java.security.Key;
import java.security.Provider;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.crypto.Cipher;
import junit.framework.TestCase;

public final class CipherTest extends TestCase {

    private static final Map<String, Integer> EXPECTED_BLOCK_SIZE = new HashMap<>();
    private static final Map<String, Integer> EXPECTED_OUTPUT_SIZE = new HashMap<>();

    static {
        EXPECTED_BLOCK_SIZE.put("AES", 16);
        EXPECTED_BLOCK_SIZE.put("AES/CBC/PKCS5Padding", 16);
        EXPECTED_BLOCK_SIZE.put("AES/GCM/NoPadding", 16);
        EXPECTED_BLOCK_SIZE.put("PBEWITHSHAAND3-KEYTRIPLEDES-CBC", 24);
        EXPECTED_BLOCK_SIZE.put("DESEDE", 8);
        
        EXPECTED_OUTPUT_SIZE.put("AES", 16);
        EXPECTED_OUTPUT_SIZE.put("AES/CBC/PKCS5Padding", -1);
        EXPECTED_OUTPUT_SIZE.put("AES/GCM/NoPadding", -1);
        EXPECTED_OUTPUT_SIZE.put("PBEWITHSHAAND3-KEYTRIPLEDES-CBC", -1);
        EXPECTED_OUTPUT_SIZE.put("DESEDE", -1);
    }

    private static String getBaseAlgorithm(String algorithm) {
        if (algorithm.equals("AESWRAP") || algorithm.equals("AES/CBC/PKCS5Padding") || algorithm.equals("AES/GCM/NoPadding")) {
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
        assertNotNull(algorithm, expected);
        return expected;
    }

    private static int getExpectedOutputSize(String algorithm) {
        Integer expected = EXPECTED_OUTPUT_SIZE.get(algorithm);
        assertNotNull(algorithm, expected);
        return expected;
    }

    public void test_getInstance() throws Exception {
        Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            Set<Provider.Service> services = provider.getServices();
            for (Provider.Service service : services) {
                String type = service.getType();
                if (!type.equals("Cipher")) {
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
            throw new Exception("Unsupported algorithm: " + algorithm);
        }

        Key encryptKey = getEncryptKey(algorithm);
        c.init(Cipher.ENCRYPT_MODE, encryptKey);
        
        assertEquals(getExpectedBlockSize(algorithm), c.getBlockSize());
        assertEquals(getExpectedOutputSize(algorithm), c.getOutputSize(0));
        
        if (isAsymmetric(algorithm)) {
            // Specific handling for asymmetric algorithms.
            // Further implementation goes here as needed.
        }
    }

    private boolean isUnsupported(String algorithm) {
        return algorithm.endsWith("/ECB") || algorithm.equals("AES/ECB/NoPadding");
    }
}

//<End of snippet n. 0>