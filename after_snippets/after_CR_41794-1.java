
//<Beginning of snippet n. 0>


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
* @param monitor {@link ITaskMonitor} which is related to this URL
*            fetching.
* @return Returns an {@link InputStream} holding the URL content.
     *   Returns null if there's no content (e.g. resource not found.)
*   Returns null if the document is not cached and strategy is {@link Strategy#ONLY_CACHE}.
* @throws IOException Exception thrown when there are problems retrieving
*             the URL or its content.

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

}

if (result == null) {
            // Make up an error code if we don't have one already.
HttpResponse outResponse = new BasicHttpResponse(
new ProtocolVersion("HTTP", 1, 0),  //$NON-NLS-1$
                    HttpStatus.SC_METHOD_FAILURE, "");  //$NON-NLS-1$;  // 420=Method Failure
result = Pair.of(null, outResponse);
}


//<End of snippet n. 1>










//<Beginning of snippet n. 2>



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
total += n;
if (total >= next_inc) {
monitor.incProgress(1);
                    inc_remain--;
next_inc += inc;
}

monitor.logError("File not found: %1$s", e.getMessage());

} catch (Exception e) {
            monitor.logError("Download failed: %1$s", e.getMessage());   //$NON-NLS-1$

} finally {
if (os != null) {
// pass
}
}
            if (inc_remain > 0) {
                monitor.incProgress(inc_remain);
            }
}

return false;

//<End of snippet n. 2>








