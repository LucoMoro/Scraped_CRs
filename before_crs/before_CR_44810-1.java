/*More "charsetName" consistency.

A follow-on to 9b9e9145bc55a47be42a9d3f7ecc9f6b533739b2, after URLDecoder
caused some confusion.

Change-Id:I727dc375593596272d06e177c5dd8002d3e7321c*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/net/URLDecoder.java b/luni/src/main/java/java/net/URLDecoder.java
//Synthetic comment -- index 175175d..f21de7d 100644

//Synthetic comment -- @@ -47,23 +47,16 @@

/**
* Decodes the argument which is assumed to be encoded in the {@code
     * x-www-form-urlencoded} MIME content type using the specified encoding
     * scheme.
     * <p>
     *'+' will be converted to space, '%' and two following hex digit
* characters are converted to the equivalent byte value. All other
* characters are passed through unmodified. For example "A+B+C %24%25" ->
* "A B C $%".
*
     * @param s
     *            the encoded string.
     * @param encoding
     *            the encoding scheme to be used.
     * @return the decoded clear-text representation of the given string.
     * @throws UnsupportedEncodingException
     *             if the specified encoding scheme is invalid.
*/
    public static String decode(String s, String encoding) throws UnsupportedEncodingException {
        return UriCodec.decode(s, true, Charset.forName(encoding), true);
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/cert/CertPath.java b/luni/src/main/java/java/security/cert/CertPath.java
//Synthetic comment -- index d247d30..f9154b6 100644

//Synthetic comment -- @@ -145,16 +145,12 @@
throws CertificateEncodingException;

/**
     * Returns an encoding of the {@code CertPath} using the specified encoding.
*
     * @param encoding
     *            encoding that should be generated.
     * @return default encoding of the {@code CertPath}.
* @throws CertificateEncodingException
*             if the encoding fails.
*/
    public abstract byte[] getEncoded(String encoding)
        throws CertificateEncodingException;

/**
* Returns an {@code Iterator} over the supported encodings for a








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/cert/CertificateFactory.java b/luni/src/main/java/java/security/cert/CertificateFactory.java
//Synthetic comment -- index 83d40d3..31456a2 100644

//Synthetic comment -- @@ -225,23 +225,17 @@
}

/**
     * Generates a {@code CertPath} (a certificate chain) from the provided
     * {@code InputStream} and the specified encoding scheme.
*
     * @param inStream
     *            {@code InputStream} containing certificate path data in
     *            specified encoding.
     * @param encoding
     *            encoding of the data in the input stream.
     * @return a {@code CertPath} initialized from the provided data.
* @throws CertificateException
*             if parsing problems are detected.
* @throws UnsupportedOperationException
*             if the provider does not implement this method.
*/
    public final CertPath generateCertPath(InputStream inStream, String encoding)
throws CertificateException {
        return spiImpl.engineGenerateCertPath(inStream, encoding);
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/cert/CertificateFactorySpi.java b/luni/src/main/java/java/security/cert/CertificateFactorySpi.java
//Synthetic comment -- index 321afa7..95c66e3 100644

//Synthetic comment -- @@ -105,21 +105,15 @@
}

/**
     * Generates a {@code CertPath} from the provided {@code
     * InputStream} in the specified encoding.
*
     * @param inStream
     *            an input stream containing certificate path data in specified
     *            encoding.
     * @param encoding
     *            the encoding of the data in the input stream.
     * @return a {@code CertPath} initialized from the provided data
* @throws CertificateException
*             if parsing problems are detected.
* @throws UnsupportedOperationException
*             if the provider does not implement this method.
*/
    public CertPath engineGenerateCertPath(InputStream inStream, String encoding)
throws CertificateException {
throw new UnsupportedOperationException();
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/logging/Handler.java b/luni/src/main/java/java/util/logging/Handler.java
//Synthetic comment -- index 13dbdd5..ae7288d 100644

//Synthetic comment -- @@ -281,14 +281,11 @@
* Sets the character encoding used by this handler, {@code null} indicates
* a default encoding.
*
     * @param encoding
     *            the character encoding to set.
     * @throws UnsupportedEncodingException
     *             if the specified encoding is not supported by the runtime.
*/
    public void setEncoding(String encoding) throws UnsupportedEncodingException {
LogManager.getLogManager().checkAccess();
        internalSetEncoding(encoding);
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/logging/StreamHandler.java b/luni/src/main/java/java/util/logging/StreamHandler.java
//Synthetic comment -- index 60b4321..4785f13 100644

//Synthetic comment -- @@ -180,16 +180,13 @@
* Sets the character encoding used by this handler. A {@code null} value
* indicates that the default encoding should be used.
*
     * @param encoding
     *            the character encoding to set.
     * @throws UnsupportedEncodingException
     *             if the specified encoding is not supported by the runtime.
*/
@Override
    public void setEncoding(String encoding) throws UnsupportedEncodingException {
        // flush first before set new encoding
this.flush();
        super.setEncoding(encoding);
// renew writer only if the writer exists
if (this.writer != null) {
if (getEncoding() == null) {








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/icu/NativeConverter.java b/luni/src/main/java/libcore/icu/NativeConverter.java
//Synthetic comment -- index e18f483..1b8a7e0 100644

//Synthetic comment -- @@ -21,7 +21,7 @@
public static native int encode(long converterHandle, char[] input, int inEnd,
byte[] output, int outEnd, int[] data, boolean flush);

    public static native long openConverter(String encoding);
public static native void closeConverter(long converterHandle);

public static native void resetByteToChar(long converterHandle);








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xml/ExpatReader.java b/luni/src/main/java/org/apache/harmony/xml/ExpatReader.java
//Synthetic comment -- index 90fa181..e0542b1 100644

//Synthetic comment -- @@ -309,15 +309,10 @@
parser.parseDocument(in);
}

    private void parse(InputStream in, String encoding, String publicId,
            String systemId) throws IOException, SAXException {
        ExpatParser parser = new ExpatParser(
                encoding,
                this,
                processNamespaces,
                publicId,
                systemId
        );
parser.parseDocument(in);
}








