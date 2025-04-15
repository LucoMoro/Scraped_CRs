/*Replaced raw string arguments for Context.getSystemService() with final
Context variables

Change-Id:I871dbcae901d5a3bf51e6853f8a0b3a329eaf271*/
//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/ContextWrapperTest.java b/tests/tests/content/src/android/content/cts/ContextWrapperTest.java
//Synthetic comment -- index 1d9affa5..5e06d36 100644

//Synthetic comment -- @@ -1203,7 +1203,7 @@
assertNull(mContextWrapper.getSystemService("invalid"));

// Test valid service name
        assertNotNull(mContextWrapper.getSystemService("window"));
}

@TestTargetNew(







