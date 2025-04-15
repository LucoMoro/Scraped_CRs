/*Applying charset detection and transformation to particular charset
Signed-off-by: Sang-Jun Park <sj2202.park@samsung.com>

Change-Id:I609134d78d58db733204b01912350a2ee69ad65b*/
//Synthetic comment -- diff --git a/src/com/android/email/activity/MessageCompose.java b/src/com/android/email/activity/MessageCompose.java
old mode 100644
new mode 100755
//Synthetic comment -- index 063e63e..759d147

//Synthetic comment -- @@ -742,12 +742,24 @@
}

private String getPackedAddresses(TextView view) {
        Address[] addresses = Address.parse(view.getText().toString().trim());
return Address.pack(addresses);
}

private Address[] getAddresses(TextView view) {
        Address[] addresses = Address.parse(view.getText().toString().trim());
return addresses;
}









//Synthetic comment -- diff --git a/src/com/android/email/mail/Address.java b/src/com/android/email/mail/Address.java
old mode 100644
new mode 100755
//Synthetic comment -- index 4fb1c32..bb31650

//Synthetic comment -- @@ -102,6 +102,13 @@
if (personal != null) {
personal = REMOVE_OPTIONAL_DQUOTE.matcher(personal).replaceAll("$1");
personal = UNQUOTE.matcher(personal).replaceAll("$1");
personal = DecoderUtil.decodeEncodedWords(personal);
if (personal.length() == 0) {
personal = null;
//Synthetic comment -- @@ -152,6 +159,53 @@
if (TextUtils.isEmpty(name)) {
name = null;
}
addresses.add(new Address(address, name));
}
}








//Synthetic comment -- diff --git a/src/com/android/email/mail/internet/MimeUtility.java b/src/com/android/email/mail/internet/MimeUtility.java
old mode 100644
new mode 100755
//Synthetic comment -- index 008328a..3dc0712

//Synthetic comment -- @@ -26,13 +26,16 @@

import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.codec.EncoderUtil;
import org.apache.james.mime4j.decoder.Base64InputStream;
import org.apache.james.mime4j.decoder.DecoderUtil;
import org.apache.james.mime4j.decoder.QuotedPrintableInputStream;
import org.apache.james.mime4j.util.CharsetUtil;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
//Synthetic comment -- @@ -66,7 +69,22 @@
if (s == null) {
return null;
}
        return DecoderUtil.decodeEncodedWords(s);
}

public static String unfoldAndDecode(String s) {
//Synthetic comment -- @@ -284,7 +302,15 @@
* No encoding, so use us-ascii, which is the standard.
*/
if (charset == null) {
                        charset = "ASCII";
}
/*
* Convert and return as new String
//Synthetic comment -- @@ -360,7 +386,7 @@
in = new QuotedPrintableInputStream(in);
}
else if ("base64".equalsIgnoreCase(contentTransferEncoding)) {
                in = new Base64InputStream(in);
}
}









//Synthetic comment -- diff --git a/src/com/android/email/mail/transport/Rfc822Output.java b/src/com/android/email/mail/transport/Rfc822Output.java
old mode 100644
new mode 100755
//Synthetic comment -- index aa95d89..519bf1e

//Synthetic comment -- @@ -47,6 +47,17 @@
import java.util.regex.Pattern;

/**
* Utility class to output RFC 822 messages from provider email messages
*/
public class Rfc822Output {
//Synthetic comment -- @@ -217,17 +228,55 @@
*/
private static void writeOneAttachment(Context context, Writer writer, OutputStream out,
Attachment attachment) throws IOException, MessagingException {
writeHeader(writer, "Content-Type",
                attachment.mMimeType + ";\n name=\"" + attachment.mFileName + "\"");
writeHeader(writer, "Content-Transfer-Encoding", "base64");
        // Most attachments (real files) will send Content-Disposition.  The suppression option
        // is used when sending calendar invites.
        if ((attachment.mFlags & Attachment.FLAG_ICS_ALTERNATIVE_PART) == 0) {
writeHeader(writer, "Content-Disposition",
"attachment;"
+ "\n filename=\"" + attachment.mFileName + "\";"
+ "\n size=" + Long.toString(attachment.mSize));
}
writeHeader(writer, "Content-ID", attachment.mContentId);
writer.append("\r\n");









//Synthetic comment -- diff --git a/src/org/apache/james/mime4j/decoder/DecoderUtil.java b/src/org/apache/james/mime4j/decoder/DecoderUtil.java
old mode 100644
new mode 100755
//Synthetic comment -- index 551be0f..67f74fe

//Synthetic comment -- @@ -31,6 +31,19 @@
import java.io.UnsupportedEncodingException;

/**
* Static methods for decoding strings, byte arrays and encoded words.
*
* 
//Synthetic comment -- @@ -40,6 +53,127 @@
private static Log log = LogFactory.getLog(DecoderUtil.class);

/**
* Decodes a string containing quoted-printable encoded data. 
* 
* @param s the string to decode.
//Synthetic comment -- @@ -47,7 +181,7 @@
*/
public static byte[] decodeBaseQuotedPrintable(String s) {
ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
try {
byte[] bytes = s.getBytes("US-ASCII");

//Synthetic comment -- @@ -67,7 +201,7 @@

return baos.toByteArray();
}
    
/**
* Decodes a string containing base64 encoded data. 
* 








//Synthetic comment -- diff --git a/src/org/apache/james/mime4j/util/CharsetUtil.java b/src/org/apache/james/mime4j/util/CharsetUtil.java
old mode 100644
new mode 100755
//Synthetic comment -- index d2074bb..4b8167f

//Synthetic comment -- @@ -956,7 +956,16 @@
new Charset("Cp943", null, new String[] {}),
new Charset("Cp943C", null, new String[] {}),
new Charset("Cp948", null, new String[] {}),
        new Charset("Cp949", null, new String[] {}),
new Charset("Cp949C", null, new String[] {}),
new Charset("Cp950", null, new String[] {}),
new Charset("Cp964", null, new String[] {}),







