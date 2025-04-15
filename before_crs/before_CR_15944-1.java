/*Add Another Delayed Check to WebSettingsTest

Bug 2814482

Fix some more flakiness with another delayed check.

Change-Id:I84077f84c67be0de4fee415f445191b39eea1065*/
//Synthetic comment -- diff --git a/tests/tests/webkit/src/android/webkit/cts/WebSettingsTest.java b/tests/tests/webkit/src/android/webkit/cts/WebSettingsTest.java
//Synthetic comment -- index 5e7572d..16c3e04 100644

//Synthetic comment -- @@ -240,13 +240,18 @@
@ToBeFixed(explanation = "Implementation does not work as expected.")
public void testAccessBlockNetworkImage() throws Exception {
String url = TestHtmlConstants.EMBEDDED_IMG_URL;
        String ext = MimeTypeMap.getFileExtensionFromUrl(url);

mWebView.clearCache(true);
assertFalse(mSettings.getBlockNetworkImage());
assertTrue(mSettings.getLoadsImagesAutomatically());
loadAssetUrl(url);
        assertFalse(mWebServer.getLastRequestUrl().endsWith(ext));

/* ToBeFixed: Uncomment after fixing the framework
mWebView.clearCache(true);







