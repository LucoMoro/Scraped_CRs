//<Beginning of snippet n. 0>
public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding) {
    StringBuilder dataUrl = new StringBuilder("data:");

    if ("base64".equals(encoding)) {
        dataUrl.append(mimeType).append(";base64,").append(data);
    } else {
        if (isValidEncoding(encoding)) {
            dataUrl.append(mimeType).append(";charset=").append(encoding).append(",");
        } else {
            throw new IllegalArgumentException("Unsupported encoding: " + encoding);
        }
        String encodedData = encodeData(data);
        dataUrl.append(encodedData);
    }

    loadUrl(dataUrl.toString());
}

private String encodeData(String data) {
    StringBuilder encoded = new StringBuilder();
    for (char c : data.toCharArray()) {
        if (isSafeUrlCharacter(c)) {
            encoded.append(c);
        } else {
            encoded.append(String.format("%%%02X", (int) c));
        }
    }
    return encoded.toString();
}

private boolean isSafeUrlCharacter(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') ||
           c == '-' || c == '_' || c == '.' || c == '~' || c == '!';
}

private boolean isValidEncoding(String encoding) {
    return encoding.matches("^[A-Za-z0-9-]+$"); // basic validation for encoding
}

//<End of snippet n. 0>