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
    int bytesRead = pduDataStream.read(partData, 0, dataLength);
    
    if (bytesRead == -1) {
        throw new IOException("Failed to read data from stream");
    }
    
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
        throw new IllegalStateException("Decoded part data is null or empty");
    }
    
    part.setData(partData);
}

// Implement multipart handling
private void parseMultipart(byte[] data) throws IOException {
    String boundary = extractBoundary(data);
    if (boundary == null) {
        throw new IllegalArgumentException("Boundary not found in multipart data");
    }
    
    String[] parts = new String(data).split("--" + boundary);
    if (parts.length <= 1) {
        throw new IllegalArgumentException("No parts found in multipart data");
    }
    
    byte[] firstPartData = extractFirstPart(parts);
    if (firstPartData != null) {
        part.setData(firstPartData);
    }
}

private String extractBoundary(byte[] data) {
    String dataString = new String(data);
    int boundaryIndex = dataString.indexOf("boundary=");
    if (boundaryIndex != -1) {
        int start = boundaryIndex + "boundary=".length();
        int end = dataString.indexOf('\n', start);
        return dataString.substring(start, end).trim();
    }
    return null;
}

private byte[] extractFirstPart(String[] parts) {
    if (parts.length > 1) {
        return parts[1].getBytes();
    }
    return null; // If no valid part found
}

/* add this part to body */

//<End of snippet n. 1>