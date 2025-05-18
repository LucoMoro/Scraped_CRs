//<Beginning of snippet n. 0>
public InputStream openWithHttpClient(String urlString, ITaskMonitor monitor, String[] headers) 
        throws IOException, CanceledByUserException {
    // ... implementation
    InputStream inputStream = result.getFirst();
    if (inputStream == null) {
        throw new IOException("InputStream is null for URL: " + urlString);
    }
    return inputStream;
}

/**
 * @param monitor {@link ITaskMonitor} which is related to this URL
 *            fetching.
 * @return Returns an {@link InputStream} holding the URL content.
 *         Returns null if the document is not cached and strategy is {@link Strategy#ONLY_CACHE}.
 * @throws IOException Exception thrown when there are problems retrieving
 *             the URL or its content.
 */
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public Pair<InputStream, HttpResponse> openWithUrl(String url, String[] headers) 
        throws IOException {
    // ... implementation
    if (result == null) {
        HttpResponse outResponse = new BasicHttpResponse(
            new ProtocolVersion("HTTP", 1, 0), 
            424, "");
        throw new IOException("Failed to open URL with both HttpClient and Url");
    }

    InputStream inputStream = result.getFirst();
    if (inputStream == null) {
        throw new IOException("InputStream is null for URL: " + url);
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
        throw new IOException("InputStream is null for URL: " + urlString);
    }
    os = new FileOutputStream(tmpFile);

    MessageDigest digester = archive.getChecksumType().getMessageDigest();
    total += n;
    if (total >= next_inc) {
        monitor.incProgress(1);
        next_inc += inc;
    }

} catch (IOException e) {
    monitor.logError("IOException occurred: %1$s", e.getMessage());
    throw new IOException("IOException occurred while processing URL: " + urlString, e);
} finally {
    if (os != null) {
        try {
            os.close();
        } catch (IOException e) {
            monitor.logError("Failed to close file output stream: %1$s", e.getMessage());
        }
    }
    if (is != null) {
        try {
            is.close();
        } catch (IOException e) {
            monitor.logError("Failed to close input stream: %1$s", e.getMessage());
        }
    }
}

return false;
//<End of snippet n. 2>