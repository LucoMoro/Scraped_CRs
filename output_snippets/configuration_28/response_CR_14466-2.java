//<Beginning of snippet n. 0>

public static final String MMS_GENERIC       = "application/vnd.wap.mms-generic";
public static final String MULTIPART_MIXED   = "application/vnd.wap.multipart.mixed";
public static final String MULTIPART_RELATED = "application/vnd.wap.multipart.related";
public static final String MULTIPART_ALTERNATIVE = "multipart/vnd.wap.multipart.alternative";

public static final String TEXT_PLAIN        = "text/plain";
public static final String TEXT_HTML         = "text/html";

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

public static byte[] getPartData(PduPart part, InputStream pduDataStream) throws IOException {
    int dataLength = part.getDataLength();
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
            } else if (!encoding.equalsIgnoreCase("binary")) {
                log("Unsupported encoding: " + encoding);
                return null;
            }
        }

        if (partData == null || partData.length == 0) {
            log("Decode part data error!");
            return null;
        }
        
        part.setData(partData);
        return partData;
    }
    log("Data length is zero!");
    return null;
}

// Add functionality to parse "multipart/vnd.wap.multipart.alternative"
public static void parseMultipartAlternative(InputStream inputStream) {
    // Placeholder for multipart parsing logic
    try {
        // Implement multipart parsing logic here
        // Extract first part as parent part data
    } catch (Exception e) {
        log("Error while parsing multipart: " + e.getMessage());
    }
}

//<End of snippet n. 1>