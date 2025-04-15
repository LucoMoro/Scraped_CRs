/*AndroidKeyStore: add key wrapping test

Change-Id:Ib21ab37d22689dd87f014eaa1f7919a575367cdd*/




//Synthetic comment -- diff --git a/keystore/tests/src/android/security/AndroidKeyStoreTest.java b/keystore/tests/src/android/security/AndroidKeyStoreTest.java
//Synthetic comment -- index 056e681..c376f3d 100644

//Synthetic comment -- @@ -51,6 +51,9 @@
import java.util.Iterator;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;

public class AndroidKeyStoreTest extends AndroidTestCase {
//Synthetic comment -- @@ -1545,4 +1548,49 @@
} catch (UnsupportedOperationException success) {
}
}

    private void setupKey() throws Exception {
        final String privateKeyAlias = Credentials.USER_PRIVATE_KEY + TEST_ALIAS_1;
        assertTrue(mAndroidKeyStore.generate(privateKeyAlias));

        X509Certificate cert = generateCertificate(mAndroidKeyStore, TEST_ALIAS_1, TEST_SERIAL_1,
                TEST_DN_1, NOW, NOW_PLUS_10_YEARS);

        assertTrue(mAndroidKeyStore.put(Credentials.USER_CERTIFICATE + TEST_ALIAS_1,
                cert.getEncoded()));
    }

    public void testKeyStore_KeyOperations_Wrap_Success() throws Exception {
        mKeyStore.load(null, null);

        setupKey();

        // Test key usage
        Entry e = mKeyStore.getEntry(TEST_ALIAS_1, null);
        assertNotNull(e);
        assertTrue(e instanceof PrivateKeyEntry);

        PrivateKeyEntry privEntry = (PrivateKeyEntry) e;
        PrivateKey privKey = privEntry.getPrivateKey();
        assertNotNull(privKey);

        PublicKey pubKey = privEntry.getCertificate().getPublicKey();

        Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        c.init(Cipher.WRAP_MODE, pubKey);

        byte[] expectedKey = new byte[] {
                0x00, 0x05, (byte) 0xAA, (byte) 0x0A5, (byte) 0xFF, 0x55, 0x0A
        };

        SecretKey expectedSecret = new SecretKeySpec(expectedKey, "AES");

        byte[] wrappedExpected = c.wrap(expectedSecret);

        c.init(Cipher.UNWRAP_MODE, privKey);
        SecretKey actualSecret = (SecretKey) c.unwrap(wrappedExpected, "AES", Cipher.SECRET_KEY);

        assertEquals(Arrays.toString(expectedSecret.getEncoded()),
                Arrays.toString(actualSecret.getEncoded()));
    }
}







