/*Server thread may not update the current value immediately after request url on testAccessLoadsImagesAutomatically case.
Solution: Add one second delay before assert function.

Change-Id:I8aaf8b2f8436732ddcaf6181f8e1c5007b267269*/
//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebSettingsTest.java b/tests/tests/webkit/src/android/webkit/cts/WebSettingsTest.java
//Synthetic comment -- index 6b9f9dcf..18301e7 100644

//Synthetic comment -- @@ -665,12 +665,14 @@
String url = TestHtmlConstants.EMBEDDED_IMG_URL;
String ext = MimeTypeMap.getFileExtensionFromUrl(url);
loadAssetUrl(url);
assertFalse(mWebServer.getLastRequestUrl().endsWith(ext));

mWebView.clearCache(true);
mSettings.setLoadsImagesAutomatically(false);
assertFalse(mSettings.getLoadsImagesAutomatically());
loadAssetUrl(url);
assertTrue(mWebServer.getLastRequestUrl().endsWith(ext));
}








