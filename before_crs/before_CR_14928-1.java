/*Replaced raw string arguments for Context.getSystemService() with final
Context variables

Change-Id:I97bb970fdb81f8c1329d112d60d644f6225f04f1*/
//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/ContextWrapperTest.java b/tests/tests/content/src/android/content/cts/ContextWrapperTest.java
//Synthetic comment -- index 1d9affa5..5e06d36 100644

//Synthetic comment -- @@ -1203,7 +1203,7 @@
assertNull(mContextWrapper.getSystemService("invalid"));

// Test valid service name
        assertNotNull(mContextWrapper.getSystemService("window"));
}

@TestTargetNew(







