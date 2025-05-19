//<Beginning of snippet n. 0>
InputStream result = null;
try {
    result = openWithHttpClient(url, monitor, headers);
    if (result == null) {
        throw new IOException("InputStream was null from openWithHttpClient.");
    }
} catch (IOException | MalformedURLException e) {
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
        throw new IOException("Failed to fetch URL, fallback method failed.", e2);
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
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
            throw new IOException("Failed to fetch URL using both methods.", e2);
        }
    }

    return result;
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
try (InputStream is = cache.openDirectUrl(urlString, monitor);
     FileOutputStream os = new FileOutputStream(tmpFile)) {
    if (is == null) {
        throw new IOException("InputStream is null from cache.");
    }
    MessageDigest digester = archive.getChecksumType().getMessageDigest();
    total += n;
    if (total >= next_inc) {
        monitor.incProgress(1);
        next_inc += inc;
    }
} catch (IOException e) {
    monitor.logError("File not found: %1$s", e.getMessage());
}
return false;
//<End of snippet n. 2>