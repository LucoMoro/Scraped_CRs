/*Parse "multipart/vnd.wap.multipart.alternative" which is a part of multipart body (nested multipart).

And take the first part of parsed as a parent part data.

Change-Id:I2752654f41d642524061802772e2a7eaa10a4e2d*/
//Synthetic comment -- diff --git a/core/java/com/google/android/mms/ContentType.java b/core/java/com/google/android/mms/ContentType.java
//Synthetic comment -- index 94bc9fd..b066fad 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
public static final String MMS_GENERIC       = "application/vnd.wap.mms-generic";
public static final String MULTIPART_MIXED   = "application/vnd.wap.multipart.mixed";
public static final String MULTIPART_RELATED = "application/vnd.wap.multipart.related";

public static final String TEXT_PLAIN        = "text/plain";
public static final String TEXT_HTML         = "text/html";








//Synthetic comment -- diff --git a/core/java/com/google/android/mms/pdu/PduParser.java b/core/java/com/google/android/mms/pdu/PduParser.java
//Synthetic comment -- index d465c5a..60b35cf 100644

//Synthetic comment -- @@ -778,26 +778,34 @@
/* get part's data */
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
                    } else {
                        // "binary" is the default encoding.
}
}
                if (null == partData) {
                    log("Decode part data error!");
                    return null;
                }
                part.setData(partData);
}

/* add this part to body */







