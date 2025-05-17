//<Beginning of snippet n. 0>
public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding) {
    if (baseUrl == null || baseUrl.isEmpty() || !baseUrl.startsWith("data:")) {
        throw new IllegalArgumentException("Invalid base URL");
    }
    
    List<String> supportedEncodings = Arrays.asList("UTF-8", "ISO-8859-1", "UTF-16", "base64");
    if (!supportedEncodings.contains(encoding)) {
        throw new UnsupportedOperationException("Unsupported encoding: " + encoding);
    }

    StringBuilder dataUrl = new StringBuilder("data:");
    dataUrl.append(mimeType);
    
    if ("base64".equals(encoding)) {
        dataUrl.append(";base64");
    } else {
        dataUrl.append(";charset=").append(URLEncoder.encode(encoding, StandardCharsets.UTF_8));
    }

    try {
        dataUrl.append(",").append(URLEncoder.encode(data, StandardCharsets.UTF_8.toString()));
    } catch (UnsupportedEncodingException e) {
        throw new RuntimeException("Encoding error: " + e.getMessage(), e);
    }

    loadUrl(dataUrl.toString());
}
//<End of snippet n. 0>