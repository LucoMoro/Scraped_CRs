//<Beginning of snippet n. 0>
public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding) {
    StringBuilder dataUrl = new StringBuilder("data:");
    if (mimeType == null || mimeType.isEmpty()) {
        mimeType = "text/plain";
    }
    dataUrl.append(mimeType);
    if ("base64".equals(encoding)) {
        dataUrl.append(";base64");
        byte[] decodedData = Base64.getEncoder().encode(data.getBytes(StandardCharsets.UTF_8));
        dataUrl.append(new String(decodedData, StandardCharsets.UTF_8));
    } else if (isSupportedEncoding(encoding)) {
        String encodedData = URLEncoder.encode(data, Charset.forName(encoding));
        dataUrl.append(";charset=").append(encoding).append(",");
        dataUrl.append(encodedData);
    } else {
        encoding = StandardCharsets.UTF_8.name();
        String encodedData = URLEncoder.encode(data, Charset.forName(encoding));
        dataUrl.append(";charset=").append(encoding).append(",");
        dataUrl.append(encodedData);
    }
    dataUrl.append(",");
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
private boolean isSupportedEncoding(String encoding) {
    return StandardCharsets.UTF_8.name().equalsIgnoreCase(encoding) ||
           "ISO-8859-1".equalsIgnoreCase(encoding) ||
           "UTF-16".equalsIgnoreCase(encoding);
}
//<End of snippet n. 1>