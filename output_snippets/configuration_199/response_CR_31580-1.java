//<Beginning of snippet n. 0>
private static final int DEFAULT_BUFFER_SIZE = 1024;

protected final Deflater def;
protected final byte[] buf;
private boolean closed = false;
private final boolean internallyCreated;
private final InputStream in;

public DeflaterInputStream(InputStream in) {
    this(in, new Deflater(), DEFAULT_BUFFER_SIZE);
}

public DeflaterInputStream(InputStream in, Deflater def, int size) {
    this.in = in;
    this.def = def;
    this.buf = new byte[size];
    this.internallyCreated = (def == null);
}

@Override
public void close() throws IOException {
    if (!closed) {
        if (internallyCreated) {
            def.end();
        }
        closed = true;
        in.close();
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
protected Deflater def;
private boolean closed = false;
private final boolean syncFlush;
private final boolean internallyCreated;
private final OutputStream out;

public DeflaterOutputStream(OutputStream os) {
    this(os, new Deflater(), DEFAULT_BUFFER_SIZE, false);
}

public DeflaterOutputStream(OutputStream os, boolean syncFlush) {
    this(os, new Deflater(), DEFAULT_BUFFER_SIZE, syncFlush);
}

public DeflaterOutputStream(OutputStream os, Deflater def, int size, boolean syncFlush) {
    this.out = os;
    this.def = def;
    this.syncFlush = syncFlush;
    this.internallyCreated = (def == null);
}

@Override
public void close() throws IOException {
    if (!closed) {
        if (!def.finished()) {
            finish();
        }
        if (internallyCreated) {
            def.end();
        }
        out.close();
        closed = true;
    }
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
protected Inflater inf;
private boolean closed = false;
private final boolean internallyCreated;
private final InputStream is;
protected byte[] buf;

public InflaterInputStream(InputStream is) {
    this(is, new Inflater(), DEFAULT_BUFFER_SIZE);
}

public InflaterInputStream(InputStream is, Inflater inf, int size) {
    this.is = is;
    this.inf = inf;
    this.buf = new byte[size];
    this.internallyCreated = (inf == null);
}

@Override
public void close() throws IOException {
    if (!closed) {
        if (internallyCreated) {
            inf.end();
        }
        closed = true;
        is.close();
    }
}
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
private static final int DEFAULT_BUFFER_SIZE = 1024;

protected final Inflater inf;
protected final byte[] buf;
private boolean closed = false;
private final boolean internallyCreated;
private final OutputStream out;

public InflaterOutputStream(OutputStream out) {
    this(out, new Inflater());
}

public InflaterOutputStream(OutputStream out, Inflater inf) {
    this.out = out;
    this.inf = inf;
    this.internallyCreated = (inf == null);
}

@Override
public void close() throws IOException {
    if (!closed) {
        finish();
        if (internallyCreated) {
            inf.end();
        }
        out.close();
        closed = true;
    }
}
//<End of snippet n. 3>