/*Asserting mProvider was not null was kind of pointless since

1) According to the docs, Class.newInstance() does not return null
2) a function in mProvider was being used before the assertion, so the assertion will always pass*/




//Synthetic comment -- diff --git a/test-runner/android/test/ProviderTestCase2.java b/test-runner/android/test/ProviderTestCase2.java
//Synthetic comment -- index a923d2a..66657ad 100644

//Synthetic comment -- @@ -62,8 +62,8 @@
mProviderContext = new IsolatedContext(mResolver, targetContextWrapper);

mProvider = mProviderClass.newInstance();
	assertNotNull(mProvider);
mProvider.attachInfo(mProviderContext, null);
mResolver.addProvider(mProviderAuthority, getProvider());
}








