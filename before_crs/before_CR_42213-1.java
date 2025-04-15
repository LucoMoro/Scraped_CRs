/*Turkish characters show problem in addressList and subject

Set Locale.US as parameter for toLowerCase() to make sure
that the string is changed to lowercase correctly, e.g.
'ISO-8859-9' to 'iso-8859-9'. The email code supports the
decoding of charset 'iso-8859-9', so that the encoded address's
name and subject, will be decoded and display the correct
content in the phone.

Change-Id:Id1a7cda0bbf83a7b584ab12336ff66195835a2fc*/
//Synthetic comment -- diff --git a/src/org/apache/james/mime4j/util/CharsetUtil.java b/src/org/apache/james/mime4j/util/CharsetUtil.java
//Synthetic comment -- index 0fc267d..f26b44f 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.TreeSet;

//BEGIN android-changed: Stubbing out logging
//Synthetic comment -- @@ -1023,13 +1024,13 @@
for (int i = 0; i < JAVA_CHARSETS.length; i++) {
try {
String s = new String(dummy, JAVA_CHARSETS[i].canonical);
                decodingSupported.add(JAVA_CHARSETS[i].canonical.toLowerCase());
} catch (UnsupportedOperationException e) {
} catch (UnsupportedEncodingException e) {
}
try {
"dummy".getBytes(JAVA_CHARSETS[i].canonical);
                encodingSupported.add(JAVA_CHARSETS[i].canonical.toLowerCase());
} catch (UnsupportedOperationException e) {
} catch (UnsupportedEncodingException e) {
}
//Synthetic comment -- @@ -1038,13 +1039,13 @@
charsetMap = new HashMap<String, Charset>();
for (int i = 0; i < JAVA_CHARSETS.length; i++) {
Charset c = JAVA_CHARSETS[i];
            charsetMap.put(c.canonical.toLowerCase(), c);
if (c.mime != null) {
                charsetMap.put(c.mime.toLowerCase(), c);
}
if (c.aliases != null) {
for (int j = 0; j < c.aliases.length; j++) {
                    charsetMap.put(c.aliases[j].toLowerCase(), c);
}
}
}
//Synthetic comment -- @@ -1136,7 +1137,7 @@
*         otherwise.
*/
public static boolean isEncodingSupported(String charsetName) {
        return encodingSupported.contains(charsetName.toLowerCase());
}

/**
//Synthetic comment -- @@ -1151,7 +1152,7 @@
*         otherwise.
*/
public static boolean isDecodingSupported(String charsetName) {
        return decodingSupported.contains(charsetName.toLowerCase());
}

/**
//Synthetic comment -- @@ -1162,7 +1163,7 @@
* @return the MIME preferred name or <code>null</code> if not known.
*/
public static String toMimeCharset(String charsetName) {
        Charset c = charsetMap.get(charsetName.toLowerCase());
if (c != null) {
return c.mime;
}
//Synthetic comment -- @@ -1181,7 +1182,7 @@
* @return the canonical Java name or <code>null</code> if not known.
*/
public static String toJavaCharset(String charsetName) {
        Charset c = charsetMap.get(charsetName.toLowerCase());
if (c != null) {
return c.canonical;
}







