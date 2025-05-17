//<Beginning of snippet n. 0>


public class KeyStoreTest extends ActivityUnitTestCase<Activity> {
    private static final String TEST_PASSWD = "12345678";
    private static final String TEST_PASSWD2 = "87654321";
    private static final String PUBLIC_ISSUE_KEYNAME_BUG_6837950 = "public_issue_keyname_bug_6837950";
    private static final String PUBLIC_ISSUE_KEYNAME1_BUG_6837950 = "public_issue_keyname1_bug_6837950";
    private static final String PUBLIC_ISSUE_KEYNAME2_BUG_6837950 = "public_issue_keyname2_bug_6837950";
    private static final byte[] TEST_KEYVALUE = "test value".getBytes(Charsets.UTF_8);

    // "Hello, World" in Chinese
    private static final byte[] TEST_I18N_VALUE = TEST_I18N_KEY.getBytes(Charsets.UTF_8);

    // Test vector data for signatures
    private static final byte[] TEST_DATA = {
            (byte) 0x00, (byte) 0xA0, (byte) 0xFF, (byte) 0x0A, (byte) 0x00, (byte) 0xFF,
            (byte) 0xAA, (byte) 0x55, (byte) 0x05, (byte) 0x5A,
    };

    private KeyStore mKeyStore = null;

    public void testDelete() throws Exception {
        assertFalse(mKeyStore.delete("non_existent_key"));
        assertNull(mKeyStore.get("non_existent_key")); // Validate retrieval of non-existent key
        mKeyStore.password(TEST_PASSWD);
        assertTrue(mKeyStore.delete(PUBLIC_ISSUE_KEYNAME_BUG_6837950));

        mKeyStore.put(PUBLIC_ISSUE_KEYNAME_BUG_6837950, TEST_KEYVALUE);
        assertTrue(Arrays.equals(TEST_KEYVALUE, mKeyStore.get(PUBLIC_ISSUE_KEYNAME_BUG_6837950)));
        
        // Assertions for edge cases
        assertNull(mKeyStore.get(PUBLIC_ISSUE_KEYNAME_BUG_6837950)); // Check it has been deleted
        assertFalse(mKeyStore.delete(PUBLIC_ISSUE_KEYNAME_BUG_6837950)); // Attempt to delete again
    }
}

//<End of snippet n. 0>