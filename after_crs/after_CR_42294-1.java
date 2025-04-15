/*Add test for bogus ProtectionParameter

KeyStoreTest didn't test bogus ProtectionParameter arguments.

Also fix to run under RI.

Change-Id:I4bc742f5a543facf145a29e0d4fd0e4c9d3d5a35*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/KeyStoreTest.java b/luni/src/test/java/libcore/java/security/KeyStoreTest.java
//Synthetic comment -- index 14d0987..8987569 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
//Synthetic comment -- @@ -50,7 +51,6 @@
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import junit.framework.TestCase;

public class KeyStoreTest extends TestCase {

//Synthetic comment -- @@ -1755,6 +1755,9 @@
}
}

    public static class FakeProtectionParameter implements ProtectionParameter {
    }

public void test_KeyStore_setEntry() throws Exception {
for (KeyStore keyStore : keyStores()) {
keyStore.load(null, null);
//Synthetic comment -- @@ -1768,6 +1771,16 @@
for (KeyStore keyStore : keyStores()) {
keyStore.load(null, null);

            try {
                keyStore.setEntry(ALIAS_PRIVATE, getPrivateKey(), new FakeProtectionParameter());
                fail("Should not accept unknown ProtectionParameter");
            } catch (KeyStoreException success) {
            }
        }

        for (KeyStore keyStore : keyStores()) {
            keyStore.load(null, null);

// test odd inputs
try {
keyStore.setEntry(null, null, null);
//Synthetic comment -- @@ -2160,7 +2173,12 @@
assertEquals(PARAM_STORE, builder.getProtectionParameter(""));
assertEqualsKeyStores(file, PASSWORD_STORE, keyStore);
} finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException ignore) {
                }
file.delete();
}
}
//Synthetic comment -- @@ -2215,7 +2233,11 @@
// http://b/857840: want JKS key store
public void testDefaultKeystore() {
String type = KeyStore.getDefaultType();
        if (StandardNames.IS_RI) {
            assertEquals("Default keystore type must be JKS", "jks", type);
        } else {
            assertEquals("Default keystore type must be Bouncy Castle", "BKS", type);
        }

try {
KeyStore store = KeyStore.getInstance(KeyStore.getDefaultType());
//Synthetic comment -- @@ -2225,7 +2247,12 @@
}

try {
            final KeyStore store;
            if (StandardNames.IS_RI) {
                store = KeyStore.getInstance("JKS");
            } else {
                store = KeyStore.getInstance("BKS");
            }
assertNotNull("Keystore must not be null", store);
} catch (Exception ex) {
throw new RuntimeException(ex);







