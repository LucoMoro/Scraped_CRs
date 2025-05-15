
//<Beginning of snippet n. 0>


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
false /*needsMarkResetSupport*/,
monitor,
null /*headers*/);
        return result.getFirst();
}

/**
* @param monitor {@link ITaskMonitor} which is related to this URL
*            fetching.
* @return Returns an {@link InputStream} holding the URL content.
*   Returns null if the document is not cached and strategy is {@link Strategy#ONLY_CACHE}.
* @throws IOException Exception thrown when there are problems retrieving
*             the URL or its content.

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

}

if (result == null) {
HttpResponse outResponse = new BasicHttpResponse(
new ProtocolVersion("HTTP", 1, 0),  //$NON-NLS-1$
                    424, "");                           //$NON-NLS-1$;  // 424=Method Failure
result = Pair.of(null, outResponse);
}


//<End of snippet n. 1>










//<Beginning of snippet n. 2>



FileOutputStream os = null;
InputStream is = null;
try {
is = cache.openDirectUrl(urlString, monitor);
os = new FileOutputStream(tmpFile);

MessageDigest digester = archive.getChecksumType().getMessageDigest();
total += n;
if (total >= next_inc) {
monitor.incProgress(1);
next_inc += inc;
}

monitor.logError("File not found: %1$s", e.getMessage());

} catch (Exception e) {
            monitor.logError("%1$s", e.getMessage());   //$NON-NLS-1$

} finally {
if (os != null) {
// pass
}
}
}

return false;

//<End of snippet n. 2>








