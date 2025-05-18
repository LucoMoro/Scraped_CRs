//<Beginning of snippet n. 0>


package libcore.javax.crypto;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Provider.Service;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.IllegalBlockSizeException;
import junit.framework.TestCase;

public final class CipherTest extends TestCase {

private static boolean isUnsupported(String algorithm) {
    return algorithm.equals("PBEWITHMD5ANDRC2") || 
           algorithm.equals("PBEWITHSHA1ANDRC2") || 
           algorithm.equals("PBEWITHSHAAND40BITRC2-CBC") ||
           algorithm.contains("ECB");
}

private static String getBaseAlgorithm(String algorithm) {
    switch (algorithm) {
        case "AESWRAP":
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
        for (Provider.Service service : provider.getServices()) {
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
    
    byte[] plainText = "Test message".getBytes();
    byte[] cipherText = c.doFinal(plainText);
    
    assertEquals(getExpectedBlockSize(algorithm), c.getBlockSize());
    assertEquals(getExpectedOutputSize(algorithm), c.getOutputSize(0));
    
    Cipher decryptCipher = Cipher.getInstance(algorithm);
    decryptCipher.init(Cipher.DECRYPT_MODE, encryptKey);
    byte[] decryptedText = decryptCipher.doFinal(cipherText);
    
    assertNotNull(decryptedText);
    assertEquals(new String(plainText), new String(decryptedText));
    
    assertNotEquals(cipherText, decryptedText);
}

private void assertPadding(byte expectedBlockType, byte[] expectedData, byte[] actualDataWithPadding) {
    assertNotNull(actualDataWithPadding);
    assertEquals(getExpectedOutputSize("RSA"), actualDataWithPadding.length);
    assertEquals(0, actualDataWithPadding[0]);
    byte actualBlockType = actualDataWithPadding[1];
    assertEquals(expectedBlockType, actualBlockType);
    Cipher c = Cipher.getInstance("RSA");
    assertNull("Parameters should be null", c.getParameters());
}

//<End of snippet n. 0>