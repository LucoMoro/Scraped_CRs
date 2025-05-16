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

public final class CipherTest extends TestCase {
    private static final Map<String, Integer> EXPECTED_BLOCK_SIZE = new HashMap<>();
    private static final Map<String, Integer> EXPECTED_OUTPUT_SIZE = new HashMap<>();

    static {
        EXPECTED_BLOCK_SIZE.put("DESEDEWRAP", 0);
        EXPECTED_BLOCK_SIZE.put("RSA", 256);
        EXPECTED_BLOCK_SIZE.put("RSA/CBC/PKCS5Padding", 256); // Changed to a secure mode
        EXPECTED_BLOCK_SIZE.put("RSA/GCM/NoPadding", 256); // Added secure alternative
        
        EXPECTED_OUTPUT_SIZE.put("DESEDEWRAP", -1);
        EXPECTED_OUTPUT_SIZE.put("RSA", 256);
        EXPECTED_OUTPUT_SIZE.put("RSA/CBC/PKCS5Padding", 256); // Changed to a secure mode
        EXPECTED_OUTPUT_SIZE.put("RSA/GCM/NoPadding", 256); // Added secure alternative
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
        if (algorithm.equals("RSA")) {
            return "RSA";
        }
        if (algorithm.equals("RSA/CBC/PKCS5Padding")) { // Changed to a secure mode
            return "RSA";
        }
        if (algorithm.equals("RSA/GCM/NoPadding")) { // Added secure alternative
            return "RSA";
        }
        return null;
    }

    private static boolean isAsymmetric(String algorithm) {
        return getBaseAlgorithm(algorithm).equals("RSA");
    }

    private static boolean isWrap(String algorithm) {
        // Key wrapping logic here, which may need a valid key context
        if (key != null) {
            return key;
        }
        algorithm = getBaseAlgorithm(algorithm);
        if (algorithm.equals("RSA")) {
            // Wrap logic can be added here if needed
            return true;
        }
        return false;
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
        assertNull("Parameters should be null", c.getParameters());
    }
}

//<End of snippet n. 0>