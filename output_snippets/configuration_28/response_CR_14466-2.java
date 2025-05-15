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
    if (null != partDataEncoding) {
        String encoding = new String(partDataEncoding);
        if (encoding.equalsIgnoreCase(PduPart.P_BASE64)) {
            // Decode "base64" into "binary".
            partData = Base64.decodeBase64(partData);
        } else if (encoding.equalsIgnoreCase(PduPart.P_QUOTED_PRINTABLE)) {
            // Decode "quoted-printable" into "binary".
            partData = QuotedPrintable.decodeQuotedPrintable(partData);
        }
    }
    
    if (null == partData) {
        log("Decode part data error!");
        return null;
    }
    part.setData(partData);
}

/* Implement multipart parsing logic */
if (part.getContentType().equalsIgnoreCase(MULTIPART_ALTERNATIVE)) {
    List<PduPart> nestedParts = parseMultipartAlternative(partData);
    if (!nestedParts.isEmpty()) {
        part.setNestedParts(nestedParts);
    } else {
        log("Nested multipart parsing error!");
    }
}

/* add this part to body */

//<End of snippet n. 1>