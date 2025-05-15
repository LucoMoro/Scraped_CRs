//<Beginning of snippet n. 0>
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
InputStream inputStream = result.getFirst();
if (inputStream == null) {
    throw new IOException("Failed to retrieve input stream from URL");
}
return inputStream;
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
/**
* @param headers An optional array of HTTP headers to use in the GET request.
* @return Returns a {@link Pair} with {@code first} holding an {@link InputStream}
*      and {@code second} holding an {@link HttpResponse}.
*      The input stream can be null. The response is never null and contains
*      at least a code; for http requests that provide them the response
*      also contains locale, headers and a status line.
*      The returned pair is never null.
*      The caller must only accept the stream if the response code is 200 or similar.
* @throws IOException Exception thrown when there are problems retrieving
*             the URL or its content.
*/
try {
    result = openWithHttpClient(url, monitor, headers);
} catch (IOException e) {
    // If the protocol is not supported by HttpClient (e.g. file:///),
    // revert to the standard java.net.Url.open.
    try {
        result = openWithUrl(url, headers);
    } catch (IOException e2) {
        throw new IOException("Failed to open URL", e2);
    }
}

if (result == null) {
    HttpResponse outResponse = new BasicHttpResponse(
        new ProtocolVersion("HTTP", 1, 0),  //$NON-NLS-1$
        424, "");                           //$NON-NLS-1$;  // 424=Method Failure
    throw new IOException("Failed to retrieve valid result from HTTP request");
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
FileOutputStream os = null;
InputStream is = null;
try {
    is = cache.openDirectUrl(urlString, monitor);
    if (is == null) {
        throw new IOException("InputStream from cache is null");
    }
    os = new FileOutputStream(tmpFile);

    MessageDigest digester = archive.getChecksumType().getMessageDigest();
    total += n;
    if (total >= next_inc) {
        monitor.incProgress(1);
        next_inc += inc;
    }
} catch (IOException e) {
    monitor.logError("File not found: %1$s", e.getMessage());
    throw new IOException("Error retrieving or processing the URL", e);
} finally {
    if (os != null) {
        try {
            os.close();
        } catch (IOException e) {
            monitor.logError("Error closing OutputStream: %1$s", e.getMessage());
        }
    }
    if (is != null) {
        try {
            is.close();
        } catch (IOException e) {
            monitor.logError("Error closing InputStream: %1$s", e.getMessage());
        }
    }
}
return false;
//<End of snippet n. 2>