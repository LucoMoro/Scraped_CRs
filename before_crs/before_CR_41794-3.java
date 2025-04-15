/*SDK Manager: fix "null" error some people are getting.

Some users reported in some cases they see a line in
the sdk manager log that just says "null". Turns out
to be an NPE when trying to read the null InputStream
returned by UrlOpener in case of a failed URL fetch.

UrlOpener used to throw an IOException when a resource
couldn't be fetched, but then I added an alternate
download path (to handle file:// resources) and that
made it fail with a null input stream instead. So now
the code throws an exception like it used to before.

Change-Id:I92ef53992290c5e6fde8b0400274bbee822f2a61*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/DownloadCache.java
//Synthetic comment -- index 91b054f..e449297 100755

//Synthetic comment -- @@ -286,10 +286,11 @@
*
* @param urlString the URL string to be opened.
* @param monitor {@link ITaskMonitor} which is related to this URL
     *            fetching.
     * @return Returns an {@link InputStream} holding the URL content.
* @throws IOException Exception thrown when there are problems retrieving
     *             the URL or its content.
* @throws CanceledByUserException Exception thrown if the user cancels the
*              authentication dialog.
*/
//Synthetic comment -- @@ -303,7 +304,16 @@
false /*needsMarkResetSupport*/,
monitor,
null /*headers*/);
        return result.getFirst();
}

/**
//Synthetic comment -- @@ -323,6 +333,7 @@
* @param monitor {@link ITaskMonitor} which is related to this URL
*            fetching.
* @return Returns an {@link InputStream} holding the URL content.
*   Returns null if the document is not cached and strategy is {@link Strategy#ONLY_CACHE}.
* @throws IOException Exception thrown when there are problems retrieving
*             the URL or its content.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java
//Synthetic comment -- index e32bda0..c157982 100644

//Synthetic comment -- @@ -52,6 +52,7 @@
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
//Synthetic comment -- @@ -132,10 +133,10 @@
* @param headers An optional array of HTTP headers to use in the GET request.
* @return Returns a {@link Pair} with {@code first} holding an {@link InputStream}
*      and {@code second} holding an {@link HttpResponse}.
     *      The input stream can be null. The response is never null and contains
*      at least a code; for http requests that provide them the response
*      also contains locale, headers and an status line.
     *      The returned pair is never null.
*      The caller must only accept the stream if the response code is 200 or similar.
* @throws IOException Exception thrown when there are problems retrieving
*             the URL or its content.
//Synthetic comment -- @@ -154,13 +155,30 @@
try {
result = openWithHttpClient(url, monitor, headers);

} catch (Exception e) {
// If the protocol is not supported by HttpClient (e.g. file:///),
// revert to the standard java.net.Url.open.

try {
result = openWithUrl(url, headers);
} catch (Exception e2) {
}
}

//Synthetic comment -- @@ -190,9 +208,10 @@
}

if (result == null) {
HttpResponse outResponse = new BasicHttpResponse(
new ProtocolVersion("HTTP", 1, 0),  //$NON-NLS-1$
                    424, "");                           //$NON-NLS-1$;  // 424=Method Failure
result = Pair.of(null, outResponse);
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/archives/ArchiveInstaller.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/archives/ArchiveInstaller.java
//Synthetic comment -- index 89a122e..5bac5f3 100755

//Synthetic comment -- @@ -315,8 +315,13 @@

FileOutputStream os = null;
InputStream is = null;
try {
is = cache.openDirectUrl(urlString, monitor);
os = new FileOutputStream(tmpFile);

MessageDigest digester = archive.getChecksumType().getMessageDigest();
//Synthetic comment -- @@ -343,6 +348,7 @@
total += n;
if (total >= next_inc) {
monitor.incProgress(1);
next_inc += inc;
}

//Synthetic comment -- @@ -404,7 +410,7 @@
monitor.logError("File not found: %1$s", e.getMessage());

} catch (Exception e) {
            monitor.logError("%1$s", e.getMessage());   //$NON-NLS-1$

} finally {
if (os != null) {
//Synthetic comment -- @@ -422,6 +428,9 @@
// pass
}
}
}

return false;







