
//<Beginning of snippet n. 0>


public class KeyStoreTest extends ActivityUnitTestCase<Activity> {
private static final String TEST_PASSWD = "12345678";
private static final String TEST_PASSWD2 = "87654321";
    private static final String TEST_KEYNAME = "test-key";
    private static final String TEST_KEYNAME1 = "test-key.1";
    private static final String TEST_KEYNAME2 = "test-key.2";
private static final byte[] TEST_KEYVALUE = "test value".getBytes(Charsets.UTF_8);

// "Hello, World" in Chinese
private static final byte[] TEST_I18N_VALUE = TEST_I18N_KEY.getBytes(Charsets.UTF_8);

// Test vector data for signatures
    private static final byte[] TEST_DATA =  new byte[256];
    static {
        for (int i = 0; i < TEST_DATA.length; i++) {
            TEST_DATA[i] = (byte) i;
        }
    }

private KeyStore mKeyStore = null;

}

public void testDelete() throws Exception {
        assertFalse(mKeyStore.delete(TEST_KEYNAME));
mKeyStore.password(TEST_PASSWD);
        assertFalse(mKeyStore.delete(TEST_KEYNAME));

mKeyStore.put(TEST_KEYNAME, TEST_KEYVALUE);
assertTrue(Arrays.equals(TEST_KEYVALUE, mKeyStore.get(TEST_KEYNAME)));

//<End of snippet n. 0>








