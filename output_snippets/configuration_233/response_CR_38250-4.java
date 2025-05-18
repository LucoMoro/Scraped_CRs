//<Beginning of snippet n. 0>
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding) {
    StringBuilder dataUrl = new StringBuilder("data:");

    if ("base64".equals(encoding)) {
        dataUrl.append(mimeType).append(";base64,").append(data);
    } else {
        if (isValidEncoding(encoding)) {
            dataUrl.append(mimeType).append(";charset=").append(encoding).append(",");
        } else {
            throw new IllegalArgumentException("Unsupported encoding: " + encoding + ". Valid encodings include: utf-8, iso-8859-1.");
        }
        String encodedData = encodeData(data, encoding);
        dataUrl.append(encodedData);
    }

    loadUrl(dataUrl.toString());
}

private String encodeData(String data, String encoding) {
    Charset charset = Charset.forName(encoding);
    return new String(data.getBytes(charset), charset);
}

private boolean isValidEncoding(String encoding) {
    Set<String> validEncodings = new HashSet<>();
    validEncodings.add("utf-8");
    validEncodings.add("iso-8859-1");
    validEncodings.add("UTF-16");
    validEncodings.add("ISO-8859-15");
    return validEncodings.contains(encoding.toLowerCase());
}

// Ensure that characters are safe for URL
private String encodeUrlData(String data) {
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

//<End of snippet n. 0>