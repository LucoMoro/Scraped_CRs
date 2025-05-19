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
        if (algorithm.equals("PBEWITHMD5ANDRC2") || algorithm.equals("PBEWITHSHA1ANDRC2") || 
            algorithm.equals("PBEWITHSHAAND40BITRC2-CBC") || 
            algorithm.startsWith("AES/ECB") || 
            algorithm.startsWith("RSA/ECB") ||
            algorithm.equals("RSA/ECB/NoPadding") ||
            algorithm.equals("RSA/ECB/PKCS1Padding") || 
            algorithm.startsWith("AES/ECB/")) {
            return true;
        }
        return false;
    }

    private static String getBaseAlgorithm(String algorithm) {
        switch (algorithm) {
            case "AESWRAP":
                return "AES";
            case "PBEWITHMD5AND128BITAES-CBC-OPENSSL":
                return "AES";
            case "DESEDEWRAP":
            case "PBEWITHSHAAND2-KEYTRIPLEDES-CBC":
            case "PBEWITHSHAAND3-KEYTRIPLEDES-CBC":
                return "DESede";
            default:
                return "DES";
        }
    }

    private static boolean isAsymmetric(String algorithm) {
        return getBaseAlgorithm(algorithm).equals("RSA");
    }

    private static boolean isWrap(String algorithm) {
        algorithm = getBaseAlgorithm(algorithm);
        return algorithm.equals("RSA");
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
        c.init(getEncryptMode(algorithm), encryptKey);

        assertEquals(getExpectedBlockSize(algorithm), c.getBlockSize());
        assertEquals(getExpectedOutputSize(algorithm), c.getOutputSize(0));
        
        if (algorithm.equals("AES/CBC/PKCS5Padding") || algorithm.equals("AES/GCM/NoPadding")) {
            byte[] cipherText = c.doFinal(ORIGINAL_PLAIN_TEXT);
            assertNotNull(cipherText);
            Cipher decryptCipher = Cipher.getInstance(algorithm);
            decryptCipher.init(Cipher.DECRYPT_MODE, getDecryptKey(algorithm));
            byte[] plainText = decryptCipher.doFinal(cipherText);
            assertNotNull(plainText);
            assertTrue(Arrays.equals(ORIGINAL_PLAIN_TEXT, plainText));
        }

        // Check for uniqueness in encryption
        Key otherEncryptKey = getEncryptKey(algorithm);
        c.init(getEncryptMode(algorithm), otherEncryptKey);
        byte[] otherCipherText = c.doFinal("AnotherPlainText".getBytes());
        assertFalse(Arrays.equals(cipherText, otherCipherText));

        // Demonstrate ECB vulnerabilities
        if (algorithm.startsWith("AES/ECB")) {
            byte[] ecbCipherText1 = c.doFinal(ORIGINAL_PLAIN_TEXT);
            byte[] ecbCipherText2 = c.doFinal(ORIGINAL_PLAIN_TEXT);
            assertTrue(Arrays.equals(ecbCipherText1, ecbCipherText2));
        }

        // Integrate integrity checks using HMAC
        byte[] hmacKey = getHMACKey(algorithm);
        byte[] hmac = computeHMAC(cipherText, hmacKey);  
        assertTrue(verifyHMAC(cipherText, hmac, hmacKey));
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