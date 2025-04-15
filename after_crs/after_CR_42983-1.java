/*Consistently use "charsetName" for charset names.

Change-Id:I147217077790c1ddfb815e870409abade31839c6*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/io/ByteArrayOutputStream.java b/luni/src/main/java/java/io/ByteArrayOutputStream.java
//Synthetic comment -- index 3ab2c20..ff9c7df 100644

//Synthetic comment -- @@ -162,17 +162,17 @@

/**
* Returns the contents of this ByteArrayOutputStream as a string converted
     * according to the encoding declared in {@code charsetName}.
*
     * @param charsetName
*            a string representing the encoding to use when translating
*            this stream to a string.
* @return this stream's current contents as an encoded string.
* @throws UnsupportedEncodingException
*             if the provided encoding is not supported.
*/
    public String toString(String charsetName) throws UnsupportedEncodingException {
        return new String(buf, 0, count, charsetName);
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/InputStreamReader.java b/luni/src/main/java/java/io/InputStreamReader.java
//Synthetic comment -- index 2e9b381..d3650dc 100644

//Synthetic comment -- @@ -62,32 +62,32 @@
/**
* Constructs a new InputStreamReader on the InputStream {@code in}. The
* character converter that is used to decode bytes into characters is
     * identified by name by {@code charsetName}. If the encoding cannot be found, an
* UnsupportedEncodingException error is thrown.
*
* @param in
*            the InputStream from which to read characters.
     * @param charsetName
*            identifies the character converter to use.
* @throws NullPointerException
     *             if {@code charsetName} is {@code null}.
* @throws UnsupportedEncodingException
     *             if the encoding specified by {@code charsetName} cannot be found.
*/
    public InputStreamReader(InputStream in, final String charsetName)
throws UnsupportedEncodingException {
super(in);
        if (charsetName == null) {
            throw new NullPointerException("charsetName == null");
}
this.in = in;
try {
            decoder = Charset.forName(charsetName).newDecoder().onMalformedInput(
CodingErrorAction.REPLACE).onUnmappableCharacter(
CodingErrorAction.REPLACE);
} catch (IllegalArgumentException e) {
throw (UnsupportedEncodingException)
                    new UnsupportedEncodingException(charsetName).initCause(e);
}
bytes.limit(0);
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/OutputStreamWriter.java b/luni/src/main/java/java/io/OutputStreamWriter.java
//Synthetic comment -- index c09c9e3..5dffdfe 100644

//Synthetic comment -- @@ -57,30 +57,30 @@

/**
* Constructs a new OutputStreamWriter using {@code out} as the target
     * stream to write converted characters to and {@code charsetName} as the character
* encoding. If the encoding cannot be found, an
* UnsupportedEncodingException error is thrown.
*
* @param out
*            the target stream to write converted bytes to.
     * @param charsetName
*            the string describing the desired character encoding.
* @throws NullPointerException
     *             if {@code charsetName} is {@code null}.
* @throws UnsupportedEncodingException
     *             if the encoding specified by {@code charsetName} cannot be found.
*/
    public OutputStreamWriter(OutputStream out, final String charsetName)
throws UnsupportedEncodingException {
super(out);
        if (charsetName == null) {
            throw new NullPointerException("charsetName == null");
}
this.out = out;
try {
            encoder = Charset.forName(charsetName).newEncoder();
} catch (Exception e) {
            throw new UnsupportedEncodingException(charsetName);
}
encoder.onMalformedInput(CodingErrorAction.REPLACE);
encoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
//Synthetic comment -- @@ -106,19 +106,19 @@

/**
* Constructs a new OutputStreamWriter using {@code out} as the target
     * stream to write converted characters to and {@code charsetEncoder} as the character
* encoder.
*
* @param out
*            the target stream to write converted bytes to.
     * @param charsetEncoder
*            the character encoder used for character conversion.
*/
    public OutputStreamWriter(OutputStream out, CharsetEncoder charsetEncoder) {
super(out);
        charsetEncoder.charset();
this.out = out;
        encoder = charsetEncoder;
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/PrintStream.java b/luni/src/main/java/java/io/PrintStream.java
//Synthetic comment -- index ea67d46..18f2310 100644

//Synthetic comment -- @@ -87,7 +87,7 @@

/**
* Constructs a new {@code PrintStream} with {@code out} as its target
     * stream and using the character encoding {@code charsetName} while writing. The
* parameter {@code autoFlush} determines if the print stream automatically
* flushes its contents to the target stream when a newline is encountered.
*
//Synthetic comment -- @@ -96,30 +96,30 @@
* @param autoFlush
*            indicates whether or not to flush contents upon encountering a
*            newline sequence.
     * @param charsetName
*            the non-null string describing the desired character encoding.
* @throws NullPointerException
     *             if {@code out} or {@code charsetName} are {@code null}.
* @throws UnsupportedEncodingException
     *             if the encoding specified by {@code charsetName} is not supported.
*/
    public PrintStream(OutputStream out, boolean autoFlush, String charsetName)
throws UnsupportedEncodingException {
super(out);
if (out == null) {
throw new NullPointerException("out == null");
        } else if (charsetName == null) {
            throw new NullPointerException("charsetName == null");
}
this.autoFlush = autoFlush;
try {
            if (!Charset.isSupported(charsetName)) {
                throw new UnsupportedEncodingException(charsetName);
}
} catch (IllegalCharsetNameException e) {
            throw new UnsupportedEncodingException(charsetName);
}
        encoding = charsetName;
}

/**
//Synthetic comment -- @@ -138,30 +138,30 @@

/**
* Constructs a new {@code PrintStream} with {@code file} as its target. The
     * character set named {@code charsetName} is used for character encoding.
*
* @param file
*            the target file. If the file already exists, its contents are
*            removed, otherwise a new file is created.
     * @param charsetName
*            the name of the character set used for character encoding.
* @throws FileNotFoundException
*             if an error occurs while opening or creating the target file.
* @throws NullPointerException
     *             if {@code charsetName} is {@code null}.
* @throws UnsupportedEncodingException
     *             if the encoding specified by {@code charsetName} is not supported.
*/
    public PrintStream(File file, String charsetName) throws FileNotFoundException,
UnsupportedEncodingException {
super(new FileOutputStream(file));
        if (charsetName == null) {
            throw new NullPointerException("charsetName == null");
}
        if (!Charset.isSupported(charsetName)) {
            throw new UnsupportedEncodingException(charsetName);
}
        encoding = charsetName;
}

/**
//Synthetic comment -- @@ -181,24 +181,24 @@

/**
* Constructs a new {@code PrintStream} with the file identified by
     * {@code fileName} as its target. The character set named {@code charsetName} is
* used for character encoding.
*
* @param fileName
*            the target file's name. If the file already exists, its
*            contents are removed, otherwise a new file is created.
     * @param charsetName
*            the name of the character set used for character encoding.
* @throws FileNotFoundException
*             if an error occurs while opening or creating the target file.
* @throws NullPointerException
     *             if {@code charsetName} is {@code null}.
* @throws UnsupportedEncodingException
     *             if the encoding specified by {@code charsetName} is not supported.
*/
    public PrintStream(String fileName, String charsetName)
throws FileNotFoundException, UnsupportedEncodingException {
        this(new File(fileName), charsetName);
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/x509/AuthorityKeyIdentifier.java b/luni/src/main/java/org/apache/harmony/security/x509/AuthorityKeyIdentifier.java
//Synthetic comment -- index 4afbb04..4985aff 100644

//Synthetic comment -- @@ -138,10 +138,10 @@
@Override protected Object getDecodedObject(BerInputStream in) throws IOException {
Object[] values = (Object[]) in.content;

            byte[] bytes = (byte[]) values[2];
BigInteger authorityCertSerialNumber = null;
            if (bytes != null) {
                authorityCertSerialNumber = new BigInteger(bytes);
}

return new AuthorityKeyIdentifier((byte[]) values[0],







