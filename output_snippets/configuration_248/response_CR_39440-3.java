//<Beginning of snippet n. 0>

public class KeyStoreTest extends ActivityUnitTestCase<Activity> {
    private static final String TEST_PASSWD = "12345678";
    private static final String TEST_PASSWD2 = "87654321";
    private static final String MAIN_TEST_KEYNAME = "mainTestKey";
    private static final String SECONDARY_TEST_KEYNAME = "secondaryTestKey";
    private static final byte[] TEST_KEYVALUE = "test value".getBytes(Charsets.UTF_8);

    // "Hello, World" in Chinese
    private static final byte[] TEST_I18N_VALUE = "你好，世界".getBytes(Charsets.UTF_8);

    // Test vector data for signatures
    private static final byte[] TEST_DATA = {
        (byte) 0x00, (byte) 0xA0, (byte) 0xFF, (byte) 0x0A, (byte) 0x00, (byte) 0xFF,
        (byte) 0xAA, (byte) 0x55, (byte) 0x05, (byte) 0x5A,
    };

    private KeyStore mKeyStore = null;

    public void testDelete() throws Exception {
        assertTrue(mKeyStore.delete(MAIN_TEST_KEYNAME));
        mKeyStore.password(TEST_PASSWD);
        assertTrue(mKeyStore.delete(MAIN_TEST_KEYNAME));

        mKeyStore.put(MAIN_TEST_KEYNAME, TEST_KEYVALUE);
        assertTrue(Arrays.equals(TEST_KEYVALUE, mKeyStore.get(MAIN_TEST_KEYNAME)));
    }

    public void testPutAndGetNonExistentKey() throws Exception {
        assertNull(mKeyStore.get(SECONDARY_TEST_KEYNAME));
        mKeyStore.put(SECONDARY_TEST_KEYNAME, TEST_KEYVALUE);
        assertTrue(Arrays.equals(TEST_KEYVALUE, mKeyStore.get(SECONDARY_TEST_KEYNAME)));
    }

    public void testDeleteNonExistentKey() throws Exception {
        assertFalse(mKeyStore.delete(SECONDARY_TEST_KEYNAME));
    }
    
}

//<End of snippet n. 0>