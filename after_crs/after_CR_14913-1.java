/*Replaced raw string arguments for Context.getSystemService() with final Context variables

Change-Id:Idff6f0e4495f72405f7d9082ff1239af25f58a4b*/




//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/ContextWrapperTest.java b/tests/tests/content/src/android/content/cts/ContextWrapperTest.java
//Synthetic comment -- index 1d9affa5..5e06d36 100644

//Synthetic comment -- @@ -1203,7 +1203,7 @@
assertNull(mContextWrapper.getSystemService("invalid"));

// Test valid service name
        assertNotNull(mContextWrapper.getSystemService(Context.WINDOW_SERVICE));
}

@TestTargetNew(







