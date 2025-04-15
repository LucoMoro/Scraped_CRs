/*Asserting mProvider was not null was kind of pointless since

1) According to the docs, Class.newInstance() does not return null
2) a function in mProvider was being used before the assertion, so the assertion will always pass*/
//Synthetic comment -- diff --git a/test-runner/android/test/ProviderTestCase2.java b/test-runner/android/test/ProviderTestCase2.java
//Synthetic comment -- index a923d2a..958aa3c 100644

//Synthetic comment -- @@ -63,7 +63,6 @@

mProvider = mProviderClass.newInstance();
mProvider.attachInfo(mProviderContext, null);
        assertNotNull(mProvider);
mResolver.addProvider(mProviderAuthority, getProvider());
}








