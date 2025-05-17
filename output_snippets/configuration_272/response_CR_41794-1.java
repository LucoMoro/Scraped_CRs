//<Beginning of snippet n. 0>

InputStream inputStream = result.getFirst();
if (inputStream == null) {
    throw new IOException("Failed to fetch URL content; InputStream is null.");
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

try {
    result = openWithHttpClient(url, monitor, headers);
    if (result == null) {
        throw new IOException("Result from openWithHttpClient is null.");
    }
} catch (IOException e) {
    throw new IOException("Failed to open URL with HttpClient; caused by: " + e.getMessage(), e);
} catch (Exception e) {
    try {
        result = openWithUrl(url, headers);
        if (result == null) {
            throw new IOException("Result from openWithUrl is null.");
        }
    } catch (IOException e2) {
        throw new IOException("Failed to open URL with both HttpClient and URL; caused by: " + e2.getMessage(), e2);
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
        throw new IOException("InputStream is null; failed to retrieve content from cache.");
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
    throw e; // Rethrow IOException for further handling
} catch (Exception e) {
    monitor.logError("Unexpected error: %1$s", e.getMessage());
    throw new IOException("Failed to process the URL; caused by: " + e.getMessage(), e);
} finally {
    if (os != null) {
        try {
            os.close();
        } catch (IOException e) {
            monitor.logError("Failed to close FileOutputStream: %1$s", e.getMessage());
        }
    }
    if (is != null) {
        try {
            is.close();
        } catch (IOException e) {
            monitor.logError("Failed to close InputStream: %1$s", e.getMessage());
        }
    }
}

return false;

//<End of snippet n. 2>