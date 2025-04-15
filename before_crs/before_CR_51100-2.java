/*AndroidKeyStore: fix tests

Change-Id:I65fd8ba27af57ea8fd27c8e08c9c1201f32c494d*/
//Synthetic comment -- diff --git a/keystore/java/android/security/KeyStore.java b/keystore/java/android/security/KeyStore.java
//Synthetic comment -- index 44be804..444dc1d 100644

//Synthetic comment -- @@ -243,7 +243,7 @@
*/
public long getmtime(String key) {
try {
            return mBinder.getmtime(key);
} catch (RemoteException e) {
Log.w(TAG, "Cannot connect to keystore", e);
return -1L;








//Synthetic comment -- diff --git a/keystore/tests/src/android/security/AndroidKeyStoreTest.java b/keystore/tests/src/android/security/AndroidKeyStoreTest.java
//Synthetic comment -- index 49e2f12..056e681 100644

//Synthetic comment -- @@ -577,17 +577,14 @@
assertAliases(new String[] { });
}

    public void testKeyStore_DeleteEntry_EmptyStore_Failure() throws Exception {
mKeyStore.load(null, null);

        try {
            mKeyStore.deleteEntry(TEST_ALIAS_1);
            fail("Should throw KeyStoreException with non-existent alias");
        } catch (KeyStoreException success) {
        }
}

    public void testKeyStore_DeleteEntry_NonExistent_Failure() throws Exception {
mKeyStore.load(null, null);

// TEST_ALIAS_1
//Synthetic comment -- @@ -596,11 +593,8 @@
assertTrue(mAndroidKeyStore.put(Credentials.USER_CERTIFICATE + TEST_ALIAS_1, FAKE_USER_1));
assertTrue(mAndroidKeyStore.put(Credentials.CA_CERTIFICATE + TEST_ALIAS_1, FAKE_CA_1));

        try {
            mKeyStore.deleteEntry(TEST_ALIAS_2);
            fail("Should throw KeyStoreException with non-existent alias");
        } catch (KeyStoreException success) {
        }
}

public void testKeyStore_GetCertificate_Single_Success() throws Exception {







