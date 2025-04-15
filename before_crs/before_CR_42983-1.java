/*Consistently use "charsetName" for charset names.

Change-Id:I147217077790c1ddfb815e870409abade31839c6*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/io/ByteArrayOutputStream.java b/luni/src/main/java/java/io/ByteArrayOutputStream.java
//Synthetic comment -- index 3ab2c20..ff9c7df 100644

//Synthetic comment -- @@ -162,17 +162,17 @@

/**
* Returns the contents of this ByteArrayOutputStream as a string converted
     * according to the encoding declared in {@code enc}.
*
     * @param enc
*            a string representing the encoding to use when translating
*            this stream to a string.
* @return this stream's current contents as an encoded string.
* @throws UnsupportedEncodingException
*             if the provided encoding is not supported.
*/
    public String toString(String enc) throws UnsupportedEncodingException {
        return new String(buf, 0, count, enc);
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/InputStreamReader.java b/luni/src/main/java/java/io/InputStreamReader.java
//Synthetic comment -- index 2e9b381..d3650dc 100644

//Synthetic comment -- @@ -62,32 +62,32 @@
/**
* Constructs a new InputStreamReader on the InputStream {@code in}. The
* character converter that is used to decode bytes into characters is
     * identified by name by {@code enc}. If the encoding cannot be found, an
* UnsupportedEncodingException error is thrown.
*
* @param in
*            the InputStream from which to read characters.
     * @param enc
*            identifies the character converter to use.
* @throws NullPointerException
     *             if {@code enc} is {@code null}.
* @throws UnsupportedEncodingException
     *             if the encoding specified by {@code enc} cannot be found.
*/
    public InputStreamReader(InputStream in, final String enc)
throws UnsupportedEncodingException {
super(in);
        if (enc == null) {
            throw new NullPointerException("enc == null");
}
this.in = in;
try {
            decoder = Charset.forName(enc).newDecoder().onMalformedInput(
CodingErrorAction.REPLACE).onUnmappableCharacter(
CodingErrorAction.REPLACE);
} catch (IllegalArgumentException e) {
throw (UnsupportedEncodingException)
                    new UnsupportedEncodingException(enc).initCause(e);
}
bytes.limit(0);
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/OutputStreamWriter.java b/luni/src/main/java/java/io/OutputStreamWriter.java
//Synthetic comment -- index c09c9e3..5dffdfe 100644

//Synthetic comment -- @@ -57,30 +57,30 @@

/**
* Constructs a new OutputStreamWriter using {@code out} as the target
     * stream to write converted characters to and {@code enc} as the character
* encoding. If the encoding cannot be found, an
* UnsupportedEncodingException error is thrown.
*
* @param out
*            the target stream to write converted bytes to.
     * @param enc
*            the string describing the desired character encoding.
* @throws NullPointerException
     *             if {@code enc} is {@code null}.
* @throws UnsupportedEncodingException
     *             if the encoding specified by {@code enc} cannot be found.
*/
    public OutputStreamWriter(OutputStream out, final String enc)
throws UnsupportedEncodingException {
super(out);
        if (enc == null) {
            throw new NullPointerException("enc == null");
}
this.out = out;
try {
            encoder = Charset.forName(enc).newEncoder();
} catch (Exception e) {
            throw new UnsupportedEncodingException(enc);
}
encoder.onMalformedInput(CodingErrorAction.REPLACE);
encoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
//Synthetic comment -- @@ -106,19 +106,19 @@

/**
* Constructs a new OutputStreamWriter using {@code out} as the target
     * stream to write converted characters to and {@code enc} as the character
* encoder.
*
* @param out
*            the target stream to write converted bytes to.
     * @param enc
*            the character encoder used for character conversion.
*/
    public OutputStreamWriter(OutputStream out, CharsetEncoder enc) {
super(out);
        enc.charset();
this.out = out;
        encoder = enc;
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/PrintStream.java b/luni/src/main/java/java/io/PrintStream.java
//Synthetic comment -- index ea67d46..18f2310 100644

//Synthetic comment -- @@ -87,7 +87,7 @@

/**
* Constructs a new {@code PrintStream} with {@code out} as its target
     * stream and using the character encoding {@code enc} while writing. The
* parameter {@code autoFlush} determines if the print stream automatically
* flushes its contents to the target stream when a newline is encountered.
*
//Synthetic comment -- @@ -96,30 +96,30 @@
* @param autoFlush
*            indicates whether or not to flush contents upon encountering a
*            newline sequence.
     * @param enc
*            the non-null string describing the desired character encoding.
* @throws NullPointerException
     *             if {@code out} or {@code enc} are {@code null}.
* @throws UnsupportedEncodingException
     *             if the encoding specified by {@code enc} is not supported.
*/
    public PrintStream(OutputStream out, boolean autoFlush, String enc)
throws UnsupportedEncodingException {
super(out);
if (out == null) {
throw new NullPointerException("out == null");
        } else if (enc == null) {
            throw new NullPointerException("enc == null");
}
this.autoFlush = autoFlush;
try {
            if (!Charset.isSupported(enc)) {
                throw new UnsupportedEncodingException(enc);
}
} catch (IllegalCharsetNameException e) {
            throw new UnsupportedEncodingException(enc);
}
        encoding = enc;
}

/**
//Synthetic comment -- @@ -138,30 +138,30 @@

/**
* Constructs a new {@code PrintStream} with {@code file} as its target. The
     * character set named {@code charSetName} is used for character encoding.
*
* @param file
*            the target file. If the file already exists, its contents are
*            removed, otherwise a new file is created.
     * @param charSetName
*            the name of the character set used for character encoding.
* @throws FileNotFoundException
*             if an error occurs while opening or creating the target file.
* @throws NullPointerException
     *             if {@code charSetName} is {@code null}.
* @throws UnsupportedEncodingException
     *             if the encoding specified by {@code charSetName} is not supported.
*/
    public PrintStream(File file, String charSetName) throws FileNotFoundException,
UnsupportedEncodingException {
super(new FileOutputStream(file));
        if (charSetName == null) {
            throw new NullPointerException("charSetName == null");
}
        if (!Charset.isSupported(charSetName)) {
            throw new UnsupportedEncodingException(charSetName);
}
        encoding = charSetName;
}

/**
//Synthetic comment -- @@ -181,24 +181,24 @@

/**
* Constructs a new {@code PrintStream} with the file identified by
     * {@code fileName} as its target. The character set named {@code charSetName} is
* used for character encoding.
*
* @param fileName
*            the target file's name. If the file already exists, its
*            contents are removed, otherwise a new file is created.
     * @param charSetName
*            the name of the character set used for character encoding.
* @throws FileNotFoundException
*             if an error occurs while opening or creating the target file.
* @throws NullPointerException
     *             if {@code charSetName} is {@code null}.
* @throws UnsupportedEncodingException
     *             if the encoding specified by {@code charSetName} is not supported.
*/
    public PrintStream(String fileName, String charSetName)
throws FileNotFoundException, UnsupportedEncodingException {
        this(new File(fileName), charSetName);
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/x509/AuthorityKeyIdentifier.java b/luni/src/main/java/org/apache/harmony/security/x509/AuthorityKeyIdentifier.java
//Synthetic comment -- index 4afbb04..4985aff 100644

//Synthetic comment -- @@ -138,10 +138,10 @@
@Override protected Object getDecodedObject(BerInputStream in) throws IOException {
Object[] values = (Object[]) in.content;

            byte[] enc = (byte[]) values[2];
BigInteger authorityCertSerialNumber = null;
            if (enc != null) {
                authorityCertSerialNumber = new BigInteger(enc);
}

return new AuthorityKeyIdentifier((byte[]) values[0],







