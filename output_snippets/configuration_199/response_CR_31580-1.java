//<Beginning of snippet n. 0>
private static final int DEFAULT_BUFFER_SIZE = 1024;

protected final Deflater def;
protected final byte[] buf;
private final boolean internallyCreated;
private boolean closed = false;

public DeflaterInputStream(InputStream in) {
    this(in, new Deflater(), DEFAULT_BUFFER_SIZE, true);
}

public DeflaterInputStream(InputStream in, Deflater def, int size, boolean internallyCreated) {
    this.def = def;
    this.buf = new byte[size];
    this.internallyCreated = internallyCreated;
}

@Override
public void close() throws IOException {
    if (!closed) {
        closed = true;
        // Removed def.end() call
        out.close();
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
protected Deflater def;
private final boolean internallyCreated;
boolean done = false;
private final boolean syncFlush;

public DeflaterOutputStream(OutputStream os) {
    this(os, new Deflater(), DEFAULT_BUFFER_SIZE, true);
}

public DeflaterOutputStream(OutputStream os, boolean syncFlush) {
    this(os, new Deflater(), DEFAULT_BUFFER_SIZE, syncFlush, true);
}

public DeflaterOutputStream(OutputStream os, Deflater def, int bufSize, boolean syncFlush, boolean internallyCreated) {
    this.def = def;
    this.syncFlush = syncFlush;
    this.internallyCreated = internallyCreated;
    // initialization code here
}

@Override
public void close() throws IOException {
    if (!def.finished()) {
        finish();
    }
    // Removed def.end() call
    out.close();
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
protected Inflater inf;
private final boolean internallyCreated;
private boolean closed = false;

public InflaterInputStream(InputStream is) {
    this(is, new Inflater(), DEFAULT_BUFFER_SIZE, true);
}

public InflaterInputStream(InputStream is, Inflater inf, int size, boolean internallyCreated) {
    this.inf = inf;
    this.buf = new byte[size];
    this.internallyCreated = internallyCreated;
}

@Override
public void close() throws IOException {
    if (!closed) {
        closed = true;
        // Removed inf.end() call
        super.close();
    }
}
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
private static final int DEFAULT_BUFFER_SIZE = 1024;

protected final Inflater inf;
protected final byte[] buf;
private final boolean internallyCreated;
private boolean closed = false;

public InflaterOutputStream(OutputStream out) {
    this(out, new Inflater(), true);
}

public InflaterOutputStream(OutputStream out, Inflater inf, boolean internallyCreated) {
    this.inf = inf;
    this.buf = new byte[DEFAULT_BUFFER_SIZE];
    this.internallyCreated = internallyCreated;
}

@Override
public void close() throws IOException {
    if (!closed) {
        finish();
        closed = true;
        // Removed inf.end() call
        out.close();
    }
}
//<End of snippet n. 3>

//<Beginning of snippet n. 4>
new file mode 100644
//<End of snippet n. 4>