//<Beginning of snippet n. 0>

* loadDataWithBaseURL()} with an appropriate base URL.
* <p>
* If the value of the encoding parameter is 'base64', then the data must
     * be encoded as base64. Otherwise, the data must use the specified encoding 
* for octets inside the range of safe URL characters and use the standard %xx
* hex encoding of URLs for octets outside that range. For example,
* '#', '%', '\', '?' should be replaced by %23, %25, %27, %3f respectively.
     * <p>
* The 'data' scheme URL formed by this method uses the specified charset 
     * charset. If you need to set a different charset, you should form a
     * 'data' scheme URL which explicitly specifies a charset parameter in the
     * mediatype portion of the URL and call {@link #loadUrl(String)} instead.
* Note that the charset obtained from the mediatype portion of a data URL
* always overrides that specified in the HTML or XML document itself.
*

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

dataUrl.append(mimeType);
if ("base64".equals(encoding)) {
    dataUrl.append(";base64");
} else {
    dataUrl.append(";charset=").append(encoding);
}
dataUrl.append(",");
dataUrl.append(encodeData(data, encoding));

//<End of snippet n. 1>

// Helper method for encoding data according to the charset
private String encodeData(String data, String encoding) {
    try {
        byte[] bytes = data.getBytes(encoding);
        StringBuilder encodedData = new StringBuilder();
        for (byte b : bytes) {
            if (isSafeUrlCharacter(b)) {
                encodedData.append((char) b);
            } else {
                encodedData.append(String.format("%%%02X", b));
            }
        }
        return encodedData.toString();
    } catch (UnsupportedEncodingException e) {
        throw new RuntimeException("Unsupported encoding: " + encoding);
    }
}

// Check if the character is a safe URL character
private boolean isSafeUrlCharacter(byte b) {
    return (b >= 65 && b <= 90) || // A-Z
           (b >= 97 && b <= 122) || // a-z
           (b >= 48 && b <= 57) || // 0-9
           (b == 45 || b == 46 || b == underscore || b == 126); // -, ., _, ~
}

//<End of snippet n. 1>