//<Beginning of snippet n. 0>
private static final int DEFAULT_BUFFER_SIZE = 1024;

protected final Deflater def;
protected final byte[] buf;

private boolean closed = false;

public DeflaterInputStream(InputStream in) {
    this(in, new Deflater(), DEFAULT_BUFFER_SIZE);
}

@Override
public void close() throws IOException {
    if (!closed) {
        closed = true;
        in.close();
        // Do not call def.end() if def was supplied by user
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
protected Deflater def;
boolean done = false;

private final boolean syncFlush;

public DeflaterOutputStream(OutputStream os) {
    this(os, new Deflater(), BUF_SIZE, false);
}

public DeflaterOutputStream(OutputStream os, boolean syncFlush) {
    this(os, new Deflater(), BUF_SIZE, syncFlush);
}

@Override
public void close() throws IOException {
    if (!closed) {
        if (!def.finished()) {
            finish();
        }
        closed = true;
        // Do not call def.end() if def was supplied by user
        out.close();
    }
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
protected Inflater inf;

/**
* The input buffer used for decompression.
*/
public InflaterInputStream(InputStream is) {
    this(is, new Inflater(), BUF_SIZE);
}

@Override
public void close() throws IOException {
    if (!closed) {
        closed = true;
        // Do not call inf.end() if inf was supplied by user
        super.close();
    }
}
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
private static final int DEFAULT_BUFFER_SIZE = 1024;

protected final Inflater inf;
protected final byte[] buf;

private boolean closed = false;

public InflaterOutputStream(OutputStream out) {
    this(out, new Inflater());
}

@Override
public void close() throws IOException {
    if (!closed) {
        finish();
        // Do not call inf.end() if inf was supplied by user
        out.close();
        closed = true;
    }
}
//<End of snippet n. 3>

//<Beginning of snippet n. 4>
//<End of snippet n. 4>