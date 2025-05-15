//<Beginning of snippet n. 0>


public class KeyStoreTest extends ActivityUnitTestCase<Activity> {
    private static final String TEST_PASSWD = "12345678";
    private static final String TEST_PASSWD2 = "87654321";
    private static final String PRIMARY_TEST_KEY = "primaryTestKey";
    private static final String SECONDARY_TEST_KEY = "secondaryTestKey";
    private static final String TERTIARY_TEST_KEY = "tertiaryTestKey";
    private static final byte[] PRIMARY_TEST_KEY_VALUE = "primary test value".getBytes(Charsets.UTF_8);
    private static final byte[] I18N_TEST_VALUE = "你好，世界".getBytes(Charsets.UTF_8);
    private static final byte[] TEST_DATA = {
            (byte) 0x00, (byte) 0xA0, (byte) 0xFF, (byte) 0x0A, (byte) 0x00, (byte) 0xFF,
            (byte) 0xAA, (byte) 0x55, (byte) 0x05, (byte) 0x5A,
    };

    private KeyStore mKeyStore = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mKeyStore = new KeyStore();
        mKeyStore.password(TEST_PASSWD);
    }

    @Override
    protected void tearDown() throws Exception {
        mKeyStore.clear();
        super.tearDown();
    }

    public void testDelete() throws Exception {
        assertTrue(mKeyStore.delete(PRIMARY_TEST_KEY));
        mKeyStore.put(PRIMARY_TEST_KEY, PRIMARY_TEST_KEY_VALUE);
        assertTrue(Arrays.equals(PRIMARY_TEST_KEY_VALUE, mKeyStore.get(PRIMARY_TEST_KEY)));
        assertTrue(mKeyStore.delete(PRIMARY_TEST_KEY));
    }

    public void testDeleteNonExistentKey() throws Exception {
        assertFalse(mKeyStore.delete("nonExistentKey"));
    }
    
    public void testKeyManagementEdgeCases() throws Exception {
        mKeyStore.put(PRIMARY_TEST_KEY, PRIMARY_TEST_KEY_VALUE);
        assertEquals(PRIMARY_TEST_KEY_VALUE, mKeyStore.get(PRIMARY_TEST_KEY));
        mKeyStore.delete(PRIMARY_TEST_KEY);
        assertNull(mKeyStore.get(PRIMARY_TEST_KEY));
    }
}

//<End of snippet n. 0>