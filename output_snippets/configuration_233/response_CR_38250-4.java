//<Beginning of snippet n. 0>
String encodeData(String data) {
    StringBuilder encodedData = new StringBuilder();
    for (char c : data.toCharArray()) {
        if (isSafeUrlChar(c)) {
            encodedData.append(c);
        } else {
            encodedData.append(String.format("%%%02X", (int) c));
        }
    }
    return encodedData.toString();
}

boolean isSafeUrlChar(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') ||
           (c >= '0' && c <= '9') || "-_.~".indexOf(c) != -1;
}

public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding) {
    StringBuilder dataUrl = new StringBuilder("data:");
    dataUrl.append(mimeType);
    if ("base64".equals(encoding)) {
        dataUrl.append(";base64");
        dataUrl.append(Base64.encode(data.getBytes())); // assume Base64.encode is defined
    } else {
        dataUrl.append(";charset=UTF-8"); 
        dataUrl.append(",");
        dataUrl.append(encodeData(data));
    }
    loadUrl(dataUrl.toString());
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
dataUrl.append(mimeType);
if ("base64".equals(encoding)) {
    dataUrl.append(";base64");
    dataUrl.append(Base64.encode(data.getBytes())); // assume Base64.encode is defined
} else {
    dataUrl.append(";charset=UTF-8");
    dataUrl.append(",");
    dataUrl.append(encodeData(data));
}
//<End of snippet n. 1>