//<Beginning of snippet n. 0>
public void loadData(String data, String mimeType, String encoding) {
    StringBuilder dataUrl = new StringBuilder();
    dataUrl.append("data:");
    dataUrl.append(mimeType);
    if ("base64".equals(encoding)) {
        dataUrl.append(";base64");
        dataUrl.append(Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8)));
    } else {
        String encodedData = URLEncoder.encode(data, StandardCharsets.UTF_8);
        dataUrl.append(encodedData);
    }
    loadUrl(dataUrl.toString());
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
dataUrl.append(mimeType);
if ("base64".equals(encoding)) {
    dataUrl.append(";base64");
    dataUrl.append(Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8)));
} else {
    dataUrl.append(",");
    dataUrl.append(URLEncoder.encode(data, StandardCharsets.UTF_8));
}
//<End of snippet n. 1>