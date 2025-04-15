/*Fix WebView.loadData ignores encoding on Android 3.x and after

In WebView.loadData encoding parameter was ignored (expect if the value was 'base64').
Now, charset encoding is used to display the data.

The following call should display well.
webView.loadData("éàçèù", "text/plain", "utf-8");

This fix a bug that appears in the 3.x android version.
Fix issues:
19747http://code.google.com/p/android/issues/detail?id=1974719306http://code.google.com/p/android/issues/detail?id=1930617163http://code.google.com/p/android/issues/detail?id=17163Sources:
-http://en.wikipedia.org/wiki/Data_URI_scheme-http://tools.ietf.org/html/rfc2397Change-Id:I518d916204423f3fc78f76e4e868e5d5377c1ceaSigned-off-by: Guillaume Châtelet <guillaume.chatelet@orange.com>*/




//Synthetic comment -- diff --git a/core/java/android/webkit/WebView.java b/core/java/android/webkit/WebView.java
//Synthetic comment -- index 24eebd7..eb10fcd 100644

//Synthetic comment -- @@ -2128,15 +2128,20 @@
* loadDataWithBaseURL()} with an appropriate base URL.
* <p>
* If the value of the encoding parameter is 'base64', then the data must
     * be encoded as base64 US-ASCII data.
     * If the value of the encoding parameter is a {@code charset} (for example
     * 'utf-8'), then the data must be encoded as {@code charset} data.
     * If the value of the encoding parameter is 'charset;base64', then the
     * data must be encoded as base64 {@code charset} data.
     * <p>
     * If the value of encoding parameter is null or 'base64', then the data
     * must use US-ASCII encoding for
* octets inside the range of safe URL characters and use the standard %xx
* hex encoding of URLs for octets outside that range. For example,
* '#', '%', '\', '?' should be replaced by %23, %25, %27, %3f respectively.
* The 'data' scheme URL formed by this method uses the default US-ASCII
     * charset.
     * <p>
* Note that the charset obtained from the mediatype portion of a data URL
* always overrides that specified in the HTML or XML document itself.
* @param data A String of data in the given encoding.
//Synthetic comment -- @@ -2153,6 +2158,8 @@
dataUrl.append(mimeType);
if ("base64".equals(encoding)) {
dataUrl.append(";base64");
        } else if (encoding != null) {
            dataUrl.append(";charset="+encoding);
}
dataUrl.append(",");
dataUrl.append(data);







