//<Beginning of snippet n. 0>
public void loadDataWithBaseURL(String data, String mimeType, String encoding, String baseUrl) {
    if (baseUrl == null || !baseUrl.startsWith("data:")) {
        return; // Handle invalid URL
    }

    StringBuilder dataUrl = new StringBuilder();
    dataUrl.append("data:");

    // Handling encoding and special character replacement
    dataUrl.append(mimeType);
    if ("base64".equals(encoding)) {
        dataUrl.append(";base64");
        dataUrl.append(",");
        dataUrl.append(Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8)));
    } else {
        dataUrl.append(",");
        dataUrl.append(encodeURL(data));
    }

    loadUrl(dataUrl.toString());
}

// Method to encode URL with ASCII and %xx encoding for unsafe characters
private String encodeURL(String data) {
    StringBuilder encoded = new StringBuilder();
    for (char ch : data.toCharArray()) {
        if (isSafeURLCharacter(ch)) {
            encoded.append(ch);
        } else {
            encoded.append(String.format("%%%02X", (int) ch));
        }
    }
    return encoded.toString();
}

private boolean isSafeURLCharacter(char ch) {
    return (ch >= 0x21 && ch <= 0x7E && ch != '#' && ch != '%' && ch != '\\' && ch != '?'); // Define safe URL characters
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public void loadData(String data, String mimeType, String encoding) {
    loadDataWithBaseURL(data, mimeType, encoding, null);
}
//<End of snippet n. 1>