//<Beginning of snippet n. 0>
/**
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
    result = openWithHttpClient(urlString, monitor);
    if (result == null) {
        throw new IOException("InputStream is null after fetching URL with HttpClient.");
    }
} catch (IOException e) {
    monitor.logError("Error fetching URL with HttpClient: " + e.getMessage());
    throw e; // Re-throw IOException
} catch (CanceledByUserException e) {
    throw e; // Propagate user cancellation
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
 */
try {
    result = openWithHttpClient(url, monitor, headers);
    if (result.getFirst() == null) {
        throw new IOException("InputStream is null after HTTP request.");
    }
} catch (Exception e) {
    monitor.logError("Error fetching with HttpClient, attempting alternative: " + e.getMessage());
    try {
        result = openWithUrl(url, headers);
        if (result.getFirst() == null) {
            throw new IOException("InputStream is null after fetching URL with Url.");
        }
    } catch (Exception e2) {
        monitor.logError("Error fetching with Url: " + e2.getMessage());
        throw new IOException("Failed to retrieve content from both HttpClient and Url.", e2);
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
    if (is == null) {
        throw new IOException("InputStream is null after directly opening URL.");
    }
    os = new FileOutputStream(tmpFile);

    MessageDigest digester = archive.getChecksumType().getMessageDigest();
    total += n;
    if (total >= next_inc) {
        monitor.incProgress(1);
        next_inc += inc;
    }

} catch (IOException e) {
    monitor.logError("File not found: " + e.getMessage());
    throw e; // Re-throw IOException
} catch (Exception e) {
    monitor.logError("An error occurred: " + e.getMessage());
    throw e; // Propagate other exceptions
} finally {
    if (os != null) {
        try {
            os.close();
        } catch (IOException e) {
            monitor.logError("Error closing output stream: " + e.getMessage());
        }
    }
    if (is != null) {
        try {
            is.close();
        } catch (IOException e) {
            monitor.logError("Error closing input stream: " + e.getMessage());
        }
    }
}

return false;
//<End of snippet n. 2>