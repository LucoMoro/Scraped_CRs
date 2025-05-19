//<Beginning of snippet n. 0>

public static final String MMS_GENERIC       = "application/vnd.wap.mms-generic";
public static final String MULTIPART_MIXED   = "application/vnd.wap.multipart.mixed";
public static final String MULTIPART_RELATED = "application/vnd.wap.multipart.related";
public static final String MULTIPART_ALTERNATIVE = "multipart/vnd.wap.multipart.alternative";

public static final String TEXT_PLAIN        = "text/plain";
public static final String TEXT_HTML         = "text/html";

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

if (dataLength > 0) {
    byte[] partData = new byte[dataLength];
    pduDataStream.read(partData, 0, dataLength);
    
    // Check Content-Transfer-Encoding.
    byte[] partDataEncoding = part.getContentTransferEncoding();
    if (partDataEncoding != null) {
        String encoding = new String(partDataEncoding);
        if (encoding.equalsIgnoreCase(PduPart.P_BASE64)) {
            partData = Base64.decodeBase64(partData);
        } else if (encoding.equalsIgnoreCase(PduPart.P_QUOTED_PRINTABLE)) {
            partData = QuotedPrintable.decodeQuotedPrintable(partData);
        }
    }
    
    if (partData == null || partData.length == 0) {
        log("Decode part data error: partData is null or empty!");
        return null;
    }
    
    part.setData(partData);
    
    // Handle multipart/vnd.wap.multipart.alternative
    if (part.getContentType().equals(MULTIPART_ALTERNATIVE)) {
        List<PduPart> innerParts = extractInnerParts(partData);
        if (innerParts != null && !innerParts.isEmpty()) {
            PduPart firstPart = innerParts.get(0);
            if (firstPart != null) {
                byte[] firstPartData = firstPart.getData();
                if (firstPartData != null) {
                    part.setData(firstPartData);
                } else {
                    log("First part data is null!");
                }
            } else {
                log("First part is null!");
            }
        } else {
            log("No inner parts found or innerParts is null!");
        }
    }
}

private List<PduPart> extractInnerParts(byte[] partData) {
    List<PduPart> parts = new ArrayList<>();
    String dataString = new String(partData);
    String boundary = extractBoundary(dataString);
    if (boundary.isEmpty()) {
        log("Error: Boundary is empty!");
        return parts;
    }
    String[] rawParts = dataString.split("--" + boundary);
    
    for (String rawPart : rawParts) {
        if (!rawPart.trim().isEmpty()) {
            PduPart part = new PduPart();
            byte[] partBytes = rawPart.getBytes();
            part.setData(partBytes);
            parts.add(part);
        }
    }
    
    return parts;
}

private String extractBoundary(String dataString) {
    String[] headers = dataString.split("\r\n");
    for (String header : headers) {
        if (header.startsWith("Content-Type:")) {
            String[] typeParts = header.split(";");
            for (String typePart : typeParts) {
                if (typePart.trim().startsWith("boundary=")) {
                    return typePart.trim().split("=")[1].replaceAll("\"", "");
                }
            }
        }
    }
    return "";
}

//<End of snippet n. 1>