/*Fix a typo where BufferedReader refers to chars as bytes.

Change-Id:I4be1fe88f97c10bc5142df05e2ffc9a3b4094d81*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/io/BufferedReader.java b/luni/src/main/java/java/io/BufferedReader.java
//Synthetic comment -- index 03ecf9f..5f2484b 100644

//Synthetic comment -- @@ -119,7 +119,7 @@
* Populates the buffer with data. It is an error to call this method when
* the buffer still contains data; ie. if {@code pos < end}.
*
     * @return the number of chars read into the buffer, or -1 if the end of the
*      source stream has been reached.
*/
private int fillBuf() throws IOException {
//Synthetic comment -- @@ -254,7 +254,7 @@
* @param buffer
*            the character array to store the characters read.
* @param offset
     *            the initial position in {@code buffer} to store the chars read
*            from this reader.
* @param length
*            the maximum number of characters to read, must be
//Synthetic comment -- @@ -277,7 +277,7 @@
while (outstanding > 0) {

/*
                 * If there are chars in the buffer, grab those first.
*/
int available = end - pos;
if (available > 0) {
//Synthetic comment -- @@ -291,7 +291,7 @@
/*
* Before attempting to read from the underlying stream, make
* sure we really, really want to. We won't bother if we're
                 * done, or if we've already got some chars and reading from the
* underlying stream would block.
*/
if (outstanding == 0 || (outstanding < length && !in.ready())) {
//Synthetic comment -- @@ -302,7 +302,7 @@

/*
* If we're unmarked and the requested size is greater than our
                 * buffer, read the chars directly into the caller's buffer. We
* don't read into smaller buffers because that could result in
* a many reads.
*/
//Synthetic comment -- @@ -464,17 +464,16 @@
}

/**
     * Skips {@code charCount} chars in this stream. Subsequent calls to
     * {@code read} will not return these chars unless {@code reset} is
* used.
     *
     * <p>Skipping characters may invalidate a mark if {@code markLimit}
* is surpassed.
*
     * @param charCount the maximum number of characters to skip.
* @return the number of characters actually skipped.
     * @throws IllegalArgumentException if {@code charCount < 0}.
* @throws IOException
*             if this reader is closed or some other I/O error occurs.
* @see #mark(int)
//Synthetic comment -- @@ -482,35 +481,35 @@
* @see #reset()
*/
@Override
    public long skip(long charCount) throws IOException {
        if (charCount < 0) {
            throw new IllegalArgumentException("charCount < 0: " + charCount);
}
synchronized (lock) {
checkNotClosed();
            if (charCount < 1) {
return 0;
}
            if (end - pos >= charCount) {
                pos += charCount;
                return charCount;
}

long read = end - pos;
pos = end;
            while (read < charCount) {
if (fillBuf() == -1) {
return read;
}
                if (end - pos >= charCount - read) {
                    pos += charCount - read;
                    return charCount;
}
// Couldn't get all the characters, skip what we read
read += (end - pos);
pos = end;
}
            return charCount;
}
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/BufferedWriter.java b/luni/src/main/java/java/io/BufferedWriter.java
//Synthetic comment -- index 55ae121..bbb18f7 100644

//Synthetic comment -- @@ -46,7 +46,7 @@

/**
* Constructs a new {@code BufferedWriter}, providing {@code out} with a buffer
     * of 8192 chars.
*
* @param out the {@code Writer} the buffer writes to.
*/
//Synthetic comment -- @@ -55,11 +55,11 @@
}

/**
     * Constructs a new {@code BufferedWriter}, providing {@code out} with {@code size} chars
* of buffer.
*
* @param out the {@code OutputStream} the buffer writes to.
     * @param size the size of buffer in chars.
* @throws IllegalArgumentException if {@code size <= 0}.
*/
public BufferedWriter(Writer out, int size) {







