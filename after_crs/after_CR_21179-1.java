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
    /**
    * Don't have to decode, using parse2() function instead of parse()
    * @author shinwook
    * @date 2011.02.10.
    */
        Address[] addresses = Address.parse2(view.getText().toString().trim());
//        Address[] addresses = Address.parse(view.getText().toString().trim());
return Address.pack(addresses);
}

private Address[] getAddresses(TextView view) {
    /**
    * Don't have to decode, using parse2() function instead of parse()
    * @author shinwook
    * @date 2011.02.10.
    */
        Address[] addresses = Address.parse2(view.getText().toString().trim());
//        Address[] addresses = Address.parse(view.getText().toString().trim());
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
            /**
             * Comment this line out and replace this with other lines
             * Block Begin
             *
             * @author shinwook
            * @date 2011.02.10.
             */
personal = DecoderUtil.decodeEncodedWords(personal);
if (personal.length() == 0) {
personal = null;
//Synthetic comment -- @@ -152,6 +159,53 @@
if (TextUtils.isEmpty(name)) {
name = null;
}
                    /*
                     * Block End
                     */
                    /**
                     * Using new decode function
                     * Block Begin
                     * @author shinwook
                     * @date 2011.02.10.
                     */
                    name = DecoderUtil.decodeGeneric(name);
                    /*
                     * Block End
                     */

                    addresses.add(new Address(address, name));
                }
            }
        }
        return addresses.toArray(new Address[] {});
    }

    /**
     * getPackedAddress() and getAddress() uses Address.parse() and it passes string from "View"
     * So, in this case, we don't have to decode
     * This is a copy and paste/modification fucntion
     *
     * @author shinwook
     * @date 2011.02.10.
     * @param addressList
     * @return
     */
    public static Address[] parse2(String addressList) {
        if (addressList == null || addressList.length() == 0) {
            return EMPTY_ADDRESS_ARRAY;
        }
        Rfc822Token[] tokens = Rfc822Tokenizer.tokenize(addressList);
        ArrayList<Address> addresses = new ArrayList<Address>();
        for (int i = 0, length = tokens.length; i < length; ++i) {
            Rfc822Token token = tokens[i];
            String address = token.getAddress();
            if (!TextUtils.isEmpty(address)) {
                if (isValidAddress(address)) {
                    String name = token.getName();
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
import org.apache.james.mime4j.decoder.DecoderUtil;
import org.apache.james.mime4j.decoder.QuotedPrintableInputStream;
import org.apache.james.mime4j.util.CharsetUtil;

import android.util.Log;

import org.apache.james.mime4j.decoder.Base64InputStream;
//import android.util.Base64;
//import android.util.Base64InputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
//Synthetic comment -- @@ -66,7 +69,22 @@
if (s == null) {
return null;
}
        /**
         * Block old one for new one
         * Block Begin
         *
         * @author shinwook
        * @date 2011.02.10.
         */
        // return DecoderUtil.decodeEncodedWords(s);
        // Block end
        /**
         * Using new decoder
         *
        * @author shinwook
        * @date 2011.02.10.
         */
        return DecoderUtil.decodeGeneric(s);
}

public static String unfoldAndDecode(String s) {
//Synthetic comment -- @@ -284,7 +302,15 @@
* No encoding, so use us-ascii, which is the standard.
*/
if (charset == null) {
                        /**
                         * Block out this line
                         * for ASCII string, it doesn't matter wheather using charset as "EUC-KR"
                         *
                        * @author shinwook
                        * @date 2011.02.10.
                         */
                        // charset = "ASCII";
                        charset = "EUC-KR";
}
/*
* Convert and return as new String
//Synthetic comment -- @@ -360,7 +386,7 @@
in = new QuotedPrintableInputStream(in);
}
else if ("base64".equalsIgnoreCase(contentTransferEncoding)) {
                in = new Base64InputStream(in);//, Base64.DEFAULT
}
}









//Synthetic comment -- diff --git a/src/com/android/email/mail/transport/Rfc822Output.java b/src/com/android/email/mail/transport/Rfc822Output.java
old mode 100644
new mode 100755
//Synthetic comment -- index aa95d89..519bf1e

//Synthetic comment -- @@ -47,6 +47,17 @@
import java.util.regex.Pattern;

/**
 * Uses encoded word for Korean Filename
 * 
 * @author shinwook
 * @date 2011.02.10.
 */
import org.apache.james.mime4j.codec.EncoderUtil;
import org.apache.james.mime4j.codec.EncoderUtil.Usage;
import org.apache.james.mime4j.decoder.DecoderUtil;
import org.apache.james.mime4j.codec.*;

/**
* Utility class to output RFC 822 messages from provider email messages
*/
public class Rfc822Output {
//Synthetic comment -- @@ -217,17 +228,55 @@
*/
private static void writeOneAttachment(Context context, Writer writer, OutputStream out,
Attachment attachment) throws IOException, MessagingException {
        /**
        * Uses encoded word for Korean Filename
        * This is RFC822 extended method for encoded-word
        * Block Begin
        *
        * @author shinwook
        * @date 2011.02.10.
        */

        Boolean ff = EncoderUtil.hasToBeEncoded(attachment.mFileName,0);

        if(ff == true){
            String fname = (EncoderUtil.hasToBeEncoded(attachment.mFileName, 0)) ? "\n filename*=\"" : "\n filename=\"";
        String encFileName = EncoderUtil.encodeAddressDisplayName(attachment.mFileName);

writeHeader(writer, "Content-Type",
                attachment.mMimeType + ";\n name=\"" + encFileName + "\"");
writeHeader(writer, "Content-Transfer-Encoding", "base64");
        writeHeader(writer, "Content-Disposition",
                "attachment;"
              + "\n filename*=\"" + encFileName + "\";"
                    + "\n size=" + Long.toString(attachment.mSize));
        }
        else{
            writeHeader(writer, "Content-Type",
                attachment.mMimeType + ";\n name=\"" + attachment.mFileName + "\"");
            writeHeader(writer, "Content-Transfer-Encoding", "base64");
writeHeader(writer, "Content-Disposition",
"attachment;"
+ "\n filename=\"" + attachment.mFileName + "\";"
+ "\n size=" + Long.toString(attachment.mSize));
}
        // Block End

        /**
         * block this one one to new one can take effect
         * Block Begin
         * @author shinwook
        * @date 2011.02.10.
         */
//        writeHeader(writer, "Content-Type",
//                attachment.mMimeType + ";\n name=\"" + attachment.mFileName + "\"");
//        writeHeader(writer, "Content-Transfer-Encoding", "base64");
//        writeHeader(writer, "Content-Disposition",
//                "attachment;"
//                + "\n filename=\"" + attachment.mFileName + "\";"
//                + "\n size=" + Long.toString(attachment.mSize));
        // Block End

writeHeader(writer, "Content-ID", attachment.mContentId);
writer.append("\r\n");









//Synthetic comment -- diff --git a/src/org/apache/james/mime4j/decoder/DecoderUtil.java b/src/org/apache/james/mime4j/decoder/DecoderUtil.java
old mode 100644
new mode 100755
//Synthetic comment -- index 551be0f..67f74fe

//Synthetic comment -- @@ -31,6 +31,19 @@
import java.io.UnsupportedEncodingException;

/**
 * import new packages to detect charset
 * Block Begin
 * @author shinwook
 * @date 2011.02.10.
 */
import java.io.*;
import java.net.*;
import java.util.*;
import org.mozilla.intl.chardet.*;
// Block End

/**

* Static methods for decoding strings, byte arrays and encoded words.
*
* 
//Synthetic comment -- @@ -40,6 +53,127 @@
private static Log log = LogFactory.getLog(DecoderUtil.class);

/**
     * Using mozilla chardet
     *
     * @author shinwook
     * @date 2011.02.10.
     * @param s the string to detect
     * @return the charset the string might use (estimated result)
     * 			return null if it is US-ASCII (decoding is not needed)
     */
    public static String chardet(String s)  {
        if (s == null)
            return s;

        // Initalize the nsDetector() with default locale Kr (5) or all (nsPSMDetector.ALL)
        int lang = nsPSMDetector.ALL;
        nsDetector det = new nsDetector(lang) ;

        // Set an observer...
        // The Notify() will be called when a matching charset is found.
        det.Init(new nsICharsetDetectionObserver() {
            public void Notify(String charset) {
                log.info("Detected CHARSET = " + charset);
            }
        });

        String result;

        ByteArrayOutputStream imp = new ByteArrayOutputStream();

        char[] chars = s.toCharArray();
        for (char c : chars)
            imp.write(c);

	byte[] buf = imp.toByteArray();
	boolean isAscii = true;

	// Check if the stream is only ascii.
	isAscii = det.isAscii(buf, buf.length);
    // DoIt if non-ascii and not done yet.
    if (!isAscii) {
        det.DoIt(buf, buf.length, false);
    }
    det.DataEnd();

       result = null;
        if (isAscii) {
            // result = new String("US-ASCII");
            result = null;
        } else  {
            String prob[] = det.getProbableCharsets() ;

            // case Korean.
            for (String charset : prob)  {
                if (charset.equals("EUC-KR")){
                    result = String.valueOf("EUC-KR"); //new String("EUC-KR"); //sec.email tom.jung 20100818 For RSAR 3rd.
                    break;
                }
            }
        }
        return result;
    }
    /**
     * Check if it is encoded words and unless it is, decode it with euc-kr
     *
     * @author shinwook
     * @date 2011.02.10.
     * @param s the string to decode
     * @return the decoded bytes
     */
    public static String justDecode(String s, String charset)  {
        // if it is encoded words, just return as it was.
        if (s == null || s.indexOf("=?") != -1)  {
            return s;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String result;
        try {
            char[] chars = s.toCharArray();

            for (char c : chars)  {
                baos.write(c);
            }
            result = new String(baos.toByteArray(), CharsetUtil.toJavaCharset(charset.toLowerCase()));
        }
            catch(UnsupportedEncodingException e)  {
            result = s;
        }

        return result;
    }

    /**
     * Check if the string is an encoded-words, then decode it
     * if it is not an encoded-words, check if it is encoded
     * if it is, justDecode and return the result
     *
     * @author shinwook
     * @date 2011.02.10.
     * @param s the string to check and decode
     * @return the decoded string
     */
    public static String decodeGeneric(String s)  {
        if (s == null)
            return s;

        String result;
        if (s.indexOf("=?") == -1)  {
            // detect
            String charset = chardet(s);
            if (charset == null)
                result = s;
            else
                result = justDecode(s, charset);
        }
        else  {
            result = decodeEncodedWords(s);
        }

        return result;
    }
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

        //        new Charset("Cp949", null, new String[] {}),
        /**
         * add new mapping CP949 with EUC_KR
         *
         * @author shinwook
         * @date 2011.02.10.
         */
        new Charset("EUC_KR", "Cp949", new String[] {}),

new Charset("Cp949C", null, new String[] {}),
new Charset("Cp950", null, new String[] {}),
new Charset("Cp964", null, new String[] {}),







