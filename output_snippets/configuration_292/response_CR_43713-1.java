//<Beginning of snippet n. 0>


package libcore.javax.crypto;

import com.android.org.bouncycastle.asn1.x509.KeyUsage;
import java.math.BigInteger;
import java.security.*;
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

    static {
        EXPECTED_BLOCK_SIZE.put("DESEDEWRAP", 0);
        EXPECTED_BLOCK_SIZE.put("RSA", 255);
        EXPECTED_BLOCK_SIZE.put("RSA/ECB/NoPadding", 0);
        EXPECTED_BLOCK_SIZE.put("RSA/ECB/PKCS1Padding", 0);
        
        EXPECTED_OUTPUT_SIZE.put("DESEDEWRAP", -1);
        EXPECTED_OUTPUT_SIZE.put("RSA", 256);
        EXPECTED_OUTPUT_SIZE.put("RSA/ECB/NoPadding", 256);
        EXPECTED_OUTPUT_SIZE.put("RSA/ECB/PKCS1Padding", 256);
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

        Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
        assertNull("Parameters should be null", cipher.getParameters());
    }

    private static String getBaseAlgorithm(String algorithm) {
        if (algorithm.equals("AESWRAP") ||
            algorithm.equals("PBEWITHMD5AND128BITAES-CBC-OPENSSL")) {
            return "AES";
        }
        if (algorithm.equals("DESEDEWRAP") ||
            algorithm.equals("PBEWITHSHAAND2-KEYTRIPLEDES-CBC") ||
            algorithm.equals("PBEWITHSHAAND3-KEYTRIPLEDES-CBC")) {
            return "DESede";
        }
        if (algorithm.equals("RSA/ECB/NoPadding") ||
            algorithm.equals("RSA/ECB/PKCS1Padding")) {
            return "RSA";
        }
        return "DES";
    }

    private static boolean isAsymmetric(String algorithm) {
        return getBaseAlgorithm(algorithm).equals("RSA");
    }

    private static boolean isUnsupported(String algorithm) {
        return !EXPECTED_BLOCK_SIZE.containsKey(algorithm);
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
}

//<End of snippet n. 0>