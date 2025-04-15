/*Get the text in title tag of loaderror.html as expected value to prevent locale issue.

Change-Id:I5be1a4b04a09e7bc974a421bcb40e240ebb91a45*/
//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/TestHtmlConstants.java b/tests/tests/webkit/src/android/webkit/cts/TestHtmlConstants.java
//Synthetic comment -- index 63354d4..dfdcf2b 100644

//Synthetic comment -- @@ -16,6 +16,13 @@

package android.webkit.cts;

/**
* This class defines constants for webkit test assets.
*/
//Synthetic comment -- @@ -73,4 +80,24 @@
}
return "file:///android_asset/" + assetName;
}
}








//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebSettingsTest.java b/tests/tests/webkit/src/android/webkit/cts/WebSettingsTest.java
//Synthetic comment -- index 1bc8a4f..122bdff 100644

//Synthetic comment -- @@ -218,7 +218,7 @@

// Files on the file system should not be loaded.
mOnUiThread.loadUrlAndWaitForCompletion(TestHtmlConstants.LOCAL_FILESYSTEM_URL);
        assertEquals(TestHtmlConstants.WEBPAGE_NOT_AVAILABLE_TITLE, mOnUiThread.getTitle());
}

public void testAccessCacheMode() throws Throwable {







