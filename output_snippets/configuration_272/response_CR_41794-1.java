//<Beginning of snippet n. 0>
/**
 * Opens a URL and returns an InputStream.
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
InputStream result = null;
try {
    result = openWithHttpClient(url, monitor, headers);
    if (result == null) {
        throw new IOException("InputStream was null from openWithHttpClient.");
    }
} catch (IOException | MalformedURLException e) {
    // Enhanced logging for context
    monitor.logError("Failed to open URL with HTTP client: %s", e.getMessage());
    try {
        result = openWithUrl(url, headers);
        if (result == null) {
            throw new IOException("InputStream was null from openWithUrl.");
        }
    } catch (IOException | MalformedURLException e2) {
        monitor.logError("Failed to open URL with fallback method: %s", e2.getMessage());
        HttpResponse outResponse = new BasicHttpResponse(
                new ProtocolVersion("HTTP", 1, 0),
                424, ""); 
        return Pair.of(null, outResponse);
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Fetches the URL content and returns a pair containing an InputStream and HttpResponse.
 *
 * @param url the URL to fetch.
 * @param monitor {@link ITaskMonitor} which is related to the URL fetching.
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
Pair<InputStream, HttpResponse> fetchUrl(String url, ITaskMonitor monitor, String[] headers) throws IOException {
    Pair<InputStream, HttpResponse> result;

    try {
        result = openWithHttpClient(url, monitor, headers);
        if (result.getFirst() == null) {
            throw new IOException("InputStream is null from openWithHttpClient.");
        }
    } catch (IOException | MalformedURLException e) {
        monitor.logError("Failed to open URL with HTTP client: %s", e.getMessage());
        try {
            result = openWithUrl(url, headers);
            if (result.getFirst() == null) {
                throw new IOException("InputStream is null from openWithUrl.");
            }
        } catch (IOException | MalformedURLException e2) {
            monitor.logError("Failed to open URL with fallback method: %s", e2.getMessage());
            HttpResponse outResponse = new BasicHttpResponse(
                    new ProtocolVersion("HTTP", 1, 0),
                    424, ""); 
            return Pair.of(null, outResponse);
        }
    }

    return result;
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
FileOutputStream os = null;
InputStream is = null;
try {
    is = cache.openDirectUrl(urlString, monitor);
    if (is == null) {
        throw new IOException("InputStream is null from cache.");
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
} finally {
    try {
        if (is != null) is.close();
    } catch (IOException e) {
        monitor.logError("Error closing InputStream: %1$s", e.getMessage());
    }
    try {
        if (os != null) os.close();
    } catch (IOException e) {
        monitor.logError("Error closing FileOutputStream: %1$s", e.getMessage());
    }
}
return false;
//<End of snippet n. 2>