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
//Synthetic comment -- index 91b054f..b399c1b 100755

//Synthetic comment -- @@ -286,10 +286,11 @@
*
* @param urlString the URL string to be opened.
* @param monitor {@link ITaskMonitor} which is related to this URL
     *                 fetching.
     * @return Returns an {@link InputStream} holding the URL content, or null if
     *                 there's no content.
* @throws IOException Exception thrown when there are problems retrieving
     *                 the URL or its content.
* @throws CanceledByUserException Exception thrown if the user cancels the
*              authentication dialog.
*/
//Synthetic comment -- @@ -303,7 +304,16 @@
false /*needsMarkResetSupport*/,
monitor,
null /*headers*/);
        InputStream is = result.getFirst();
        HttpResponse resp = result.getSecond();
        int status = resp.getStatusLine().getStatusCode();
        // We shouldn't be using the input stream if the response code isn't 200; this
        // shoouldn't happen normally.
        if (status != 200 && is != null) {
            is.close();
            is = null;
        }
        return is;
}

/**
//Synthetic comment -- @@ -323,6 +333,7 @@
* @param monitor {@link ITaskMonitor} which is related to this URL
*            fetching.
* @return Returns an {@link InputStream} holding the URL content.
     *   Returns null if there's no content (e.g. resource not found.)
*   Returns null if the document is not cached and strategy is {@link Strategy#ONLY_CACHE}.
* @throws IOException Exception thrown when there are problems retrieving
*             the URL or its content.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/UrlOpener.java
//Synthetic comment -- index e32bda0..c157982 100644

//Synthetic comment -- @@ -52,6 +52,7 @@
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
//Synthetic comment -- @@ -132,10 +133,10 @@
* @param headers An optional array of HTTP headers to use in the GET request.
* @return Returns a {@link Pair} with {@code first} holding an {@link InputStream}
*      and {@code second} holding an {@link HttpResponse}.
     *      The returned pair is never null and contains
*      at least a code; for http requests that provide them the response
*      also contains locale, headers and an status line.
     *      The input stream can be null, especially in case of error.
*      The caller must only accept the stream if the response code is 200 or similar.
* @throws IOException Exception thrown when there are problems retrieving
*             the URL or its content.
//Synthetic comment -- @@ -154,13 +155,30 @@
try {
result = openWithHttpClient(url, monitor, headers);

        } catch (UnknownHostException e) {
            // Host in unknown. No need to even retry with the Url object,
            // if it's broken, it's broken. It's already an IOException but
            // it could use a better message.
            throw new IOException("Unknown Host " + e.getMessage(), e);

        } catch (IOException e) {
            throw e;

} catch (Exception e) {
// If the protocol is not supported by HttpClient (e.g. file:///),
// revert to the standard java.net.Url.open.
            if (DEBUG) {
                System.out.printf("[HttpClient Error] %s : %s\n", url, e.toString());
            }

try {
result = openWithUrl(url, headers);
            } catch (IOException e2) {
                throw e2;
} catch (Exception e2) {
                if (DEBUG && !e.equals(e2)) {
                    System.out.printf("[Url Error] %s : %s\n", url, e2.toString());
                }
}
}

//Synthetic comment -- @@ -190,9 +208,10 @@
}

if (result == null) {
            // Make up an error code if we don't have one already.
HttpResponse outResponse = new BasicHttpResponse(
new ProtocolVersion("HTTP", 1, 0),  //$NON-NLS-1$
                    HttpStatus.SC_METHOD_FAILURE, "");  //$NON-NLS-1$;  // 420=Method Failure
result = Pair.of(null, outResponse);
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/archives/ArchiveInstaller.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/archives/ArchiveInstaller.java
//Synthetic comment -- index 89a122e..5bac5f3 100755

//Synthetic comment -- @@ -315,8 +315,13 @@

FileOutputStream os = null;
InputStream is = null;
        int inc_remain = NUM_MONITOR_INC;
try {
is = cache.openDirectUrl(urlString, monitor);
            if (is == null) {
                monitor.logError("Download failed: no content.");
                return false;
            }
os = new FileOutputStream(tmpFile);

MessageDigest digester = archive.getChecksumType().getMessageDigest();
//Synthetic comment -- @@ -343,6 +348,7 @@
total += n;
if (total >= next_inc) {
monitor.incProgress(1);
                    inc_remain--;
next_inc += inc;
}

//Synthetic comment -- @@ -404,7 +410,7 @@
monitor.logError("File not found: %1$s", e.getMessage());

} catch (Exception e) {
            monitor.logError("Download failed: %1$s", e.getMessage());   //$NON-NLS-1$

} finally {
if (os != null) {
//Synthetic comment -- @@ -422,6 +428,9 @@
// pass
}
}
            if (inc_remain > 0) {
                monitor.incProgress(inc_remain);
            }
}

return false;







