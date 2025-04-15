/*Add condition to assert different expected value when locale is en_GB.

Change-Id:I42808647b0b83037f8331fa65b64023462e9ea7b*/
//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebSettingsTest.java b/tests/tests/webkit/src/android/webkit/cts/WebSettingsTest.java
//Synthetic comment -- index 1bc8a4f..c5e6a13 100644

//Synthetic comment -- @@ -218,7 +218,12 @@

// Files on the file system should not be loaded.
mOnUiThread.loadUrlAndWaitForCompletion(TestHtmlConstants.LOCAL_FILESYSTEM_URL);
        assertEquals(TestHtmlConstants.WEBPAGE_NOT_AVAILABLE_TITLE, mOnUiThread.getTitle());
}

public void testAccessCacheMode() throws Throwable {







