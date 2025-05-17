//<Beginning of snippet n. 0>


public static final String MMS_GENERIC       = "application/vnd.wap.mms-generic";
public static final String MULTIPART_MIXED   = "application/vnd.wap.multipart.mixed";
public static final String MULTIPART_RELATED = "application/vnd.wap.multipart.related";
public static final String MULTIPART_ALTERNATIVE = "multipart/vnd.wap.multipart.alternative";

public static final String TEXT_PLAIN        = "text/plain";
public static final String TEXT_HTML         = "text/html";

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


/* get part's data */
if (dataLength > 0) {
    byte[] partData = new byte[dataLength];
    pduDataStream.read(partData, 0, dataLength);
    
    // Check Content-Transfer-Encoding.
    byte[] partDataEncoding = part.getContentTransferEncoding();
    if (partDataEncoding != null) {
        String encoding = new String(partDataEncoding);
        if (encoding.equalsIgnoreCase(PduPart.P_BASE64)) {
            // Decode "base64" into "binary".
            partData = Base64.decodeBase64(partData);
        } else if (encoding.equalsIgnoreCase(PduPart.P_QUOTED_PRINTABLE)) {
            // Decode "quoted-printable" into "binary".
            partData = QuotedPrintable.decodeQuotedPrintable(partData);
        }
    }
    
    if (partData == null || partData.length == 0) {
        log("Decode part data error!");
        return null;
    }
    
    part.setData(partData);
} else {
    log("Data length error! Missing part data.");
    return null;
}

/* Handle multipart/vnd.wap.multipart.alternative */
if (part.getContentType().equalsIgnoreCase(MULTIPART_ALTERNATIVE)) {
    List<PduPart> parts = parseMultipart(partData);
    if (parts == null || parts.isEmpty()) {
        log("No parts found in multipart alternative!");
        return null;
    }
    part.setData(parts.get(0).getData()); // Extract first part as parent part
    for (PduPart nestedPart : parts) {
        // Process nested parts if needed
    }
}

/* add this part to body */

//<End of snippet n. 1>