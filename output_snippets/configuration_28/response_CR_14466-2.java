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
    byte[] partDataEncoding = part.getContentTransferEncoding();
    
    if (null != partDataEncoding) {
        String encoding = new String(partDataEncoding);
        if (encoding.equalsIgnoreCase(PduPart.P_BASE64)) {
            partData = Base64.decodeBase64(partData);
        } else if (encoding.equalsIgnoreCase(PduPart.P_QUOTED_PRINTABLE)) {
            partData = QuotedPrintable.decodeQuotedPrintable(partData);
        }
    }
    if (null == partData) {
        log("Decode part data error!");
        return null;
    }
    part.setData(partData);
}

/* Parsing logic for "multipart/vnd.wap.multipart.alternative" */
if (contentType.equals(MULTIPART_ALTERNATIVE)) {
    List<PduPart> parts = parseMultipartAlternative(partData);
    if (!parts.isEmpty()) {
        PduPart parentPart = parts.get(0);
        // Handle nested multipart structures
        if (isMultipartAlternative(parentPart.getContentType())) {
            parseNestedMultipart(parentPart);
        }
        // Additional logic to process parentPart
    } else {
        log("No valid parts found in multipart/vnd.wap.multipart.alternative");
    }
}

//<End of snippet n. 1>

private List<PduPart> parseMultipartAlternative(byte[] data) {
    List<PduPart> parts = new ArrayList<>();
    // Logic to handle multipart parsing and extract parts
    return parts;
}

private void parseNestedMultipart(PduPart parentPart) {
    // Logic to parse nested multipart structures
}

private boolean isMultipartAlternative(String contentType) {
    return MULTIPART_ALTERNATIVE.equals(contentType);
}