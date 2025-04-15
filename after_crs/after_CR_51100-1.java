/*AndroidKeyStore: fix tests

Change-Id:I65fd8ba27af57ea8fd27c8e08c9c1201f32c494d*/




//Synthetic comment -- diff --git a/keystore/java/android/security/AndroidKeyStore.java b/keystore/java/android/security/AndroidKeyStore.java
//Synthetic comment -- index 65d7b8f..ae00c4d 100644

//Synthetic comment -- @@ -163,12 +163,12 @@
}

private Date getModificationDate(String alias) {
        final long epochSeconds = mKeyStore.getmtime(alias);
        if (epochSeconds == -1L) {
return null;
}

        return new Date(epochSeconds * 1000L);
}

@Override








//Synthetic comment -- diff --git a/keystore/tests/src/android/security/AndroidKeyStoreTest.java b/keystore/tests/src/android/security/AndroidKeyStoreTest.java
//Synthetic comment -- index 49e2f12..056e681 100644

//Synthetic comment -- @@ -577,17 +577,14 @@
assertAliases(new String[] { });
}

    public void testKeyStore_DeleteEntry_EmptyStore_Success() throws Exception {
mKeyStore.load(null, null);

        // Should not throw when a non-existent entry is requested for delete.
        mKeyStore.deleteEntry(TEST_ALIAS_1);
}

    public void testKeyStore_DeleteEntry_NonExistent_Success() throws Exception {
mKeyStore.load(null, null);

// TEST_ALIAS_1
//Synthetic comment -- @@ -596,11 +593,8 @@
assertTrue(mAndroidKeyStore.put(Credentials.USER_CERTIFICATE + TEST_ALIAS_1, FAKE_USER_1));
assertTrue(mAndroidKeyStore.put(Credentials.CA_CERTIFICATE + TEST_ALIAS_1, FAKE_CA_1));

        // Should not throw when a non-existent entry is requested for delete.
        mKeyStore.deleteEntry(TEST_ALIAS_2);
}

public void testKeyStore_GetCertificate_Single_Success() throws Exception {







