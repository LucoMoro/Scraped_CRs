/*Add condition to assert different expected value when locale is en_GB.

Change-Id:I5be1a4b04a09e7bc974a421bcb40e240ebb91a45*/
//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebSettingsTest.java b/tests/tests/webkit/src/android/webkit/cts/WebSettingsTest.java
//Synthetic comment -- index 2af4482..1cee975 100644

//Synthetic comment -- @@ -237,7 +237,12 @@

// Files on the file system should not be loaded.
loadUrl(TestHtmlConstants.LOCAL_FILESYSTEM_URL);
        assertEquals(TestHtmlConstants.WEBPAGE_NOT_AVAILABLE_TITLE, mWebView.getTitle());
}

@TestTargets({







