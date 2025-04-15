/*Improve test key names to reproduce public issue

Also fixes other unrelated test failures.

Bug:http://code.google.com/p/android/issues/detail?id=34577Bug: 6837950

(cherry-picked from f4019af04a1fc4b16aa5972cbcbba703caa5d78d)

Change-Id:I5b32b5ccac80f04a4d0fd6b21b8caa11e42995a7*/




//Synthetic comment -- diff --git a/keystore/tests/src/android/security/KeyStoreTest.java b/keystore/tests/src/android/security/KeyStoreTest.java
//Synthetic comment -- index 008d682..91c56d6 100755

//Synthetic comment -- @@ -35,9 +35,9 @@
public class KeyStoreTest extends ActivityUnitTestCase<Activity> {
private static final String TEST_PASSWD = "12345678";
private static final String TEST_PASSWD2 = "87654321";
    private static final String TEST_KEYNAME = "test-key";
    private static final String TEST_KEYNAME1 = "test-key.1";
    private static final String TEST_KEYNAME2 = "test-key.2";
private static final byte[] TEST_KEYVALUE = "test value".getBytes(Charsets.UTF_8);

// "Hello, World" in Chinese
//Synthetic comment -- @@ -45,10 +45,12 @@
private static final byte[] TEST_I18N_VALUE = TEST_I18N_KEY.getBytes(Charsets.UTF_8);

// Test vector data for signatures
    private static final byte[] TEST_DATA =  new byte[256];
    static {
        for (int i = 0; i < TEST_DATA.length; i++) {
            TEST_DATA[i] = (byte) i;
        }
    }

private KeyStore mKeyStore = null;

//Synthetic comment -- @@ -155,9 +157,9 @@
}

public void testDelete() throws Exception {
        assertFalse(mKeyStore.delete(TEST_KEYNAME));
mKeyStore.password(TEST_PASSWD);
        assertFalse(mKeyStore.delete(TEST_KEYNAME));

mKeyStore.put(TEST_KEYNAME, TEST_KEYVALUE);
assertTrue(Arrays.equals(TEST_KEYVALUE, mKeyStore.get(TEST_KEYNAME)));







