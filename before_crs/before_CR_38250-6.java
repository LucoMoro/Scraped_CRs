/*Fix WebView.loadData ignores encoding on Android 3.x and after

In WebView.loadData encoding parameter was ignored (expect if the value was 'base64').
Now, charset encoding is used to display the data.

The following call should display well.
webView.loadData("éàçèù", "text/plain", "utf-8") will display 'éàçèù' where it display garbage in version > 3.x.
webview.loadData("aGVsb68=", "text/plain", "base64") will still display 'hello'.
webview.loadData("6eDn6Pk=", "text/plain", "utf-8,base64") will show 'éàçèù' where it display garbage in the current version.
webview.loadDataWithBaseURL(url, "éàçèù", "text/plain", "utf-8", null) will still display 'éàçèù' if (url is null or different of "data:").
webView.loadDataWithBaseURL("data:", "éàçèù", "text/plain", "utf-8", null) will display 'éàçèù' where it display garbage in version > 3.x

This assure downward compatibility with the change made in 3.x

This fix a bug that appears in the 3.x android version.
Fix issues:
19747http://code.google.com/p/android/issues/detail?id=1974719306http://code.google.com/p/android/issues/detail?id=1930617163http://code.google.com/p/android/issues/detail?id=17163Sources:
-http://en.wikipedia.org/wiki/Data_URI_scheme-http://tools.ietf.org/html/rfc2397Amends:
remove + string concat
remove "or base64" from the url encoded case
integrate jb remastering
integrate new comments style

Change-Id:I518d916204423f3fc78f76e4e868e5d5377c1ceaSigned-off-by: Guillaume Châtelet <guillaume.chatelet@orange.com>*/
//Synthetic comment -- diff --git a/core/java/android/webkit/WebView.java b/core/java/android/webkit/WebView.java
//Synthetic comment -- index 6df7820..9d4ee35 100644

//Synthetic comment -- @@ -807,18 +807,24 @@
* #loadDataWithBaseURL(String,String,String,String,String)
* loadDataWithBaseURL()} with an appropriate base URL.
* <p>
     * The encoding parameter specifies whether the data is base64 or URL
     * encoded. If the data is base64 encoded, the value of the encoding
     * parameter must be 'base64'. For all other values of the parameter,
     * including null, it is assumed that the data uses ASCII encoding for
* octets inside the range of safe URL characters and use the standard %xx
* hex encoding of URLs for octets outside that range. For example, '#',
* '%', '\', '?' should be replaced by %23, %25, %27, %3f respectively.
     * <p>
* The 'data' scheme URL formed by this method uses the default US-ASCII
     * charset. If you need need to set a different charset, you should form a
     * 'data' scheme URL which explicitly specifies a charset parameter in the
     * mediatype portion of the URL and call {@link #loadUrl(String)} instead.
* Note that the charset obtained from the mediatype portion of a data URL
* always overrides that specified in the HTML or XML document itself.
*








//Synthetic comment -- diff --git a/core/java/android/webkit/WebViewClassic.java b/core/java/android/webkit/WebViewClassic.java
//Synthetic comment -- index ae56e6b..ccc4365 100644

//Synthetic comment -- @@ -2571,6 +2571,9 @@
dataUrl.append(mimeType);
if ("base64".equals(encoding)) {
dataUrl.append(";base64");
}
dataUrl.append(",");
dataUrl.append(data);







