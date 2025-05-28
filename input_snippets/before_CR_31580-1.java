
//<Beginning of snippet n. 0>


private static final int DEFAULT_BUFFER_SIZE = 1024;

protected final Deflater def;
protected final byte[] buf;

private boolean closed = false;
*/
public DeflaterInputStream(InputStream in) {
this(in, new Deflater(), DEFAULT_BUFFER_SIZE);
}

/**
@Override
public void close() throws IOException {
closed = true;
        def.end();
in.close();
}


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


*/
protected Deflater def;

boolean done = false;

private final boolean syncFlush;
*/
public DeflaterOutputStream(OutputStream os) {
this(os, new Deflater(), BUF_SIZE, false);
}

/**
*/
public DeflaterOutputStream(OutputStream os, boolean syncFlush) {
this(os, new Deflater(), BUF_SIZE, syncFlush);
}

/**
if (!def.finished()) {
finish();
}
        def.end();
out.close();
}


//<End of snippet n. 1>










//<Beginning of snippet n. 2>


*/
protected Inflater inf;

/**
* The input buffer used for decompression.
*/
*/
public InflaterInputStream(InputStream is) {
this(is, new Inflater(), BUF_SIZE);
}

/**
@Override
public void close() throws IOException {
if (!closed) {
            inf.end();
closed = true;
eof = true;
super.close();

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


private static final int DEFAULT_BUFFER_SIZE = 1024;

protected final Inflater inf;
protected final byte[] buf;

private boolean closed = false;
*/
public InflaterOutputStream(OutputStream out) {
this(out, new Inflater());
}

/**
@Override
public void close() throws IOException {
if (!closed) {
            finish();
            inf.end();
out.close();
closed = true;
}

//<End of snippet n. 3>










//<Beginning of snippet n. 4>

new file mode 100644


//<End of snippet n. 4>








