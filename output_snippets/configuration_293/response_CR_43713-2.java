//<Beginning of snippet n. 0>

package libcore.javax.crypto;

import com.android.org.bouncycastle.asn1.x509.KeyUsage;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
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
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class CipherTest extends TestCase {

    private static final Map<String, Integer> EXPECTED_BLOCK_SIZE = new HashMap<>();
    private static final Map<String, Integer> EXPECTED_OUTPUT_SIZE = new HashMap<>();

    static {
        EXPECTED_BLOCK_SIZE.put("DESEDEWRAP", 0);
        EXPECTED_BLOCK_SIZE.put("RSA", 255);
        EXPECTED_BLOCK_SIZE.put("AES/CBC/PKCS5Padding", 16);
        EXPECTED_BLOCK_SIZE.put("AES/GCM/NoPadding", 12);
        
        EXPECTED_OUTPUT_SIZE.put("DESEDEWRAP", -1);
        EXPECTED_OUTPUT_SIZE.put("RSA", 256);
        EXPECTED_OUTPUT_SIZE.put("AES/CBC/PKCS5Padding", -1);
        EXPECTED_OUTPUT_SIZE.put("AES/GCM/NoPadding", -1);
    }

    private static String getBaseAlgorithm(String algorithm) {
        if (algorithm.equals("AESWRAP")) {
            return "AES";
        }
        if (algorithm.equals("PBEWITHMD5AND128BITAES-CBC-OPENSSL")) {
            return "AES";
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
        return "DES";
    }

    private static boolean isUnsupported(String algorithm) {
        return algorithm.startsWith("RSA/ECB/") || algorithm.contains("/ECB/");
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
        Key encryptKey = getEncryptKey(algorithm);
        c.init(Cipher.ENCRYPT_MODE, encryptKey);
        byte[] plaintext = new byte[16]; // Test with maximum block size
        byte[] ciphertext = c.doFinal(plaintext);

        assertEquals(getExpectedBlockSize(algorithm), c.getBlockSize());
        assertEquals(getExpectedOutputSize(algorithm), c.getOutputSize(plaintext.length));

        // Generate HMAC for integrity check
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec hmacKey = new SecretKeySpec(encryptKey.getEncoded(), "HmacSHA256");
        hmac.init(hmacKey);
        byte[] hmacResult = hmac.doFinal(ciphertext);

        assertNotNull(hmacResult);

        // Ensure decryption returns the original plaintext
        c.init(Cipher.DECRYPT_MODE, encryptKey);
        byte[] decrypted = c.doFinal(ciphertext);
        assertTrue(Arrays.equals(plaintext, decrypted));
    }

    private Key getEncryptKey(String algorithm) {
        // Provide your implementation to retrieve the correct encryption key for the algorithm
        return null;
    }
}

//<End of snippet n. 0>