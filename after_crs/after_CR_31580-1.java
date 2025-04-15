/*Don't end() user-supplied [Inf|Def]laters on closing stream

Calling close() on the following compression stream classes
*should not* invoke end() on the underlying Inflater/Deflater if it
was supplied in the stream class constructor:

    InflaterInputStream
    InflaterOutputStream
    DeflaterInputStream
    DeflaterOutputStream

This fix updates the behaviour of the close() method on those classes
to match the RI, allowing reuse of Inflater & Deflaters supplied to
stream class constructors.

This issue was initially observed using JGit on Android- JGit uses an
InflaterCache to recycle Inflaters, but in the cases where that Inflater
was used on a stream it would be returned to the cache in an invalidated
state. Seemingly-random exceptions would occur as the invalid Inflaters
were re-used in other contexts :-)https://github.com/rtyley/agit/issues/47#issuecomment-3496045Change-Id:Ia7b4b154820ba19ab08696978025c90b6fe2b1e5*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/DeflaterInputStream.java b/luni/src/main/java/java/util/zip/DeflaterInputStream.java
//Synthetic comment -- index c6e95f2..86204bb 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
private static final int DEFAULT_BUFFER_SIZE = 1024;

protected final Deflater def;
    private boolean usesPrivateDeflater = false;
protected final byte[] buf;

private boolean closed = false;
//Synthetic comment -- @@ -47,6 +48,7 @@
*/
public DeflaterInputStream(InputStream in) {
this(in, new Deflater(), DEFAULT_BUFFER_SIZE);
        usesPrivateDeflater = true;
}

/**
//Synthetic comment -- @@ -89,7 +91,8 @@
@Override
public void close() throws IOException {
closed = true;
        if (usesPrivateDeflater)
            def.end();
in.close();
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/DeflaterOutputStream.java b/luni/src/main/java/java/util/zip/DeflaterOutputStream.java
//Synthetic comment -- index b0bcb99..d05bdb8 100644

//Synthetic comment -- @@ -43,6 +43,8 @@
*/
protected Deflater def;

    private boolean usesPrivateDeflater = false;

boolean done = false;

private final boolean syncFlush;
//Synthetic comment -- @@ -74,6 +76,7 @@
*/
public DeflaterOutputStream(OutputStream os) {
this(os, new Deflater(), BUF_SIZE, false);
        usesPrivateDeflater = true;
}

/**
//Synthetic comment -- @@ -99,6 +102,7 @@
*/
public DeflaterOutputStream(OutputStream os, boolean syncFlush) {
this(os, new Deflater(), BUF_SIZE, syncFlush);
        usesPrivateDeflater = true;
}

/**
//Synthetic comment -- @@ -155,7 +159,8 @@
if (!def.finished()) {
finish();
}
        if (usesPrivateDeflater)
            def.end();
out.close();
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/InflaterInputStream.java b/luni/src/main/java/java/util/zip/InflaterInputStream.java
//Synthetic comment -- index 580d605..7575472 100644

//Synthetic comment -- @@ -41,6 +41,8 @@
*/
protected Inflater inf;

    private boolean usesPrivateInflater = false;

/**
* The input buffer used for decompression.
*/
//Synthetic comment -- @@ -75,6 +77,7 @@
*/
public InflaterInputStream(InputStream is) {
this(is, new Inflater(), BUF_SIZE);
        usesPrivateInflater = true;
}

/**
//Synthetic comment -- @@ -250,7 +253,8 @@
@Override
public void close() throws IOException {
if (!closed) {
            if (usesPrivateInflater)
                inf.end();
closed = true;
eof = true;
super.close();








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/InflaterOutputStream.java b/luni/src/main/java/java/util/zip/InflaterOutputStream.java
//Synthetic comment -- index c9687b6..222ac97 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
private static final int DEFAULT_BUFFER_SIZE = 1024;

protected final Inflater inf;
    private boolean usesPrivateInflater = false;
protected final byte[] buf;

private boolean closed = false;
//Synthetic comment -- @@ -45,6 +46,7 @@
*/
public InflaterOutputStream(OutputStream out) {
this(out, new Inflater());
        usesPrivateInflater = true;
}

/**
//Synthetic comment -- @@ -87,8 +89,8 @@
@Override
public void close() throws IOException {
if (!closed) {
            if (usesPrivateInflater)
                inf.end();
out.close();
closed = true;
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/io/InflaterDeflaterStreamClosingBehaviourTest.java b/luni/src/test/java/libcore/java/io/InflaterDeflaterStreamClosingBehaviourTest.java
new file mode 100644
//Synthetic comment -- index 0000000..3097872

//Synthetic comment -- @@ -0,0 +1,116 @@
package libcore.java.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterInputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.InflaterOutputStream;
import junit.framework.TestCase;

public final class InflaterDeflaterStreamClosingBehaviourTest extends TestCase {

    public void testClosingInflaterInputStreamDoesNotEndConstructorSuppliedInflater() throws IOException {
        Inflater inflater = new Inflater();

        assertCloseDoesNotEndInflater(new InflaterInputStream(sampleInput(), inflater), inflater);
    }

    public void testClosingInflaterOutputStreamDoesNotEndConstructorSuppliedInflater() throws IOException {
        Inflater inflater = new Inflater();

        assertCloseDoesNotEndInflater(new InflaterOutputStream(sampleOutput(), inflater), inflater);
    }

    public void testClosingDeflaterInputStreamDoesNotEndConstructorSuppliedDeflater() throws IOException {
        Deflater deflater = new Deflater();

        assertCloseDoesNotEndDeflater(new DeflaterInputStream(sampleInput(), deflater), deflater);
    }

    public void testClosingDeflaterOutputStreamDoesNotEndConstructorSuppliedDeflater() throws IOException {
        Deflater deflater = new Deflater();

        assertCloseDoesNotEndDeflater(new DeflaterOutputStream(sampleOutput(), deflater), deflater);
    }

    public void testClosingInflaterInputStreamEndsInternalInflater() throws IOException {
        new InflaterInputStream(sampleInput()) {
            public void close() throws IOException {
                super.close();
                assertInflaterEnded(inf);
            }
        }.close();
    }

    public void testClosingInflaterOutputStreamEndsInternalInflater() throws IOException {
        new InflaterOutputStream(sampleOutput()) {
            public void close() throws IOException {
                super.close();
                assertInflaterEnded(inf);
            }
        }.close();
    }

    public void testClosingDeflaterInputStreamEndsInternalDeflater() throws IOException {
        new DeflaterInputStream(sampleInput()) {
            public void close() throws IOException {
                super.close();
                assertDeflaterEnded(def);
            }
        }.close();
    }

    public void testClosingDeflaterOutputStreamEndsInternalDeflater() throws IOException {
        new DeflaterOutputStream(sampleOutput()) {
            public void close() throws IOException {
                super.close();
                assertDeflaterEnded(def);
            }
        }.close();
    }


    private void assertCloseDoesNotEndInflater(Closeable stream, Inflater inflater) throws IOException {
        stream.close(); // should not end() inflater, permitting re-use

        inflater.reset(); // breaks if inflater.end() has been invoked
    }

    private void assertCloseDoesNotEndDeflater(Closeable stream, Deflater deflater) throws IOException {
        stream.close(); // should not end() deflater, permitting re-use

        deflater.reset(); // breaks if deflater.end() has been invoked
    }

    private void assertInflaterEnded(Inflater inflater) {
        try {
            inflater.reset(); // invoke an inflater method to check that it fails
            fail();
        } catch (RuntimeException expected) {
        }
    }

    private void assertDeflaterEnded(Deflater deflater) {
        try {
            deflater.reset(); // invoke a deflater method to check that it fails
            fail();
        } catch (RuntimeException expected) {
        }
    }

    private InputStream sampleInput() {
        return new ByteArrayInputStream(new byte[0]);
    }

    private OutputStream sampleOutput() {
        return new ByteArrayOutputStream();
    }

}







