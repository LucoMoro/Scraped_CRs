/*(Originally K-9 commit #100 by jessev@gmail.com)

* Switch from using the MIME4J charset list to the Java 1.5 java.nio.charset
 Charset class.

  While profiling K-9's message loading performance, I discovered a
  significant amount of time was spent instantiating the MIME4J Charset
  converter.  MIME4J's routines are an emulation layer to be Java 1.3
  compatible. Switching to Java 1.5's built in charset code resulted in
  a significant performance boost for message loading.

Based on the following comment in MIME4J, this seems like a win:

 * Utility class for working with character sets. It is somewhat similar to
 * the Java 1.4 <code>java.nio.charset.Charset</code> class but knows many
 * more aliases and is compatible with Java 1.3. It will use a simple detection
 * mechanism to detect what character sets the current VM supports. This will
 * be a sub-set of the character sets listed in the
 * <a href="http://java.sun.com/j2se/1.5.0/docs/guide/intl/encoding.doc.html">
 * Java 1.5 (J2SE5.0) Supported Encodings</a> document.
 * <p>
 * The <a href="http://www.iana.org/assignments/character-sets">
 * IANA Character Sets</a> document has been used to determine the preferred
 * MIME character set names and to get a list of known aliases.
 * <p>*/
//Synthetic comment -- diff --git a/src/com/android/email/mail/internet/MimeUtility.java b/src/com/android/email/mail/internet/MimeUtility.java
//Synthetic comment -- index 66b2a7e..5cbb71b 100644

//Synthetic comment -- @@ -5,6 +5,7 @@
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -12,7 +13,6 @@
import org.apache.james.mime4j.decoder.Base64InputStream;
import org.apache.james.mime4j.decoder.DecoderUtil;
import org.apache.james.mime4j.decoder.QuotedPrintableInputStream;
import org.apache.james.mime4j.util.CharsetUtil;

import android.util.Log;

//Synthetic comment -- @@ -25,6 +25,8 @@
import com.android.email.mail.Part;

public class MimeUtility {
public static String unfold(String s) {
if (s == null) {
return null;
//Synthetic comment -- @@ -130,6 +132,8 @@
* @throws IOException
*/
public static String getTextFromPart(Part part) {
try {
if (part != null && part.getBody() != null) {
InputStream in = part.getBody().getInputStream();
//Synthetic comment -- @@ -154,7 +158,8 @@
/*
* See if there is conversion from the MIME charset to the Java one.
*/
                        charset = CharsetUtil.toJavaCharset(charset);
}
if (charset != null) {
/*







