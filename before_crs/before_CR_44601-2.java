/*Fix a typo where BufferedReader refers to chars as bytes.

Change-Id:I4be1fe88f97c10bc5142df05e2ffc9a3b4094d81*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/io/BufferedReader.java b/luni/src/main/java/java/io/BufferedReader.java
//Synthetic comment -- index 03ecf9f..5f2484b 100644

//Synthetic comment -- @@ -119,7 +119,7 @@
* Populates the buffer with data. It is an error to call this method when
* the buffer still contains data; ie. if {@code pos < end}.
*
     * @return the number of bytes read into the buffer, or -1 if the end of the
*      source stream has been reached.
*/
private int fillBuf() throws IOException {
//Synthetic comment -- @@ -254,7 +254,7 @@
* @param buffer
*            the character array to store the characters read.
* @param offset
     *            the initial position in {@code buffer} to store the bytes read
*            from this reader.
* @param length
*            the maximum number of characters to read, must be
//Synthetic comment -- @@ -277,7 +277,7 @@
while (outstanding > 0) {

/*
                 * If there are bytes in the buffer, grab those first.
*/
int available = end - pos;
if (available > 0) {
//Synthetic comment -- @@ -291,7 +291,7 @@
/*
* Before attempting to read from the underlying stream, make
* sure we really, really want to. We won't bother if we're
                 * done, or if we've already got some bytes and reading from the
* underlying stream would block.
*/
if (outstanding == 0 || (outstanding < length && !in.ready())) {
//Synthetic comment -- @@ -302,7 +302,7 @@

/*
* If we're unmarked and the requested size is greater than our
                 * buffer, read the bytes directly into the caller's buffer. We
* don't read into smaller buffers because that could result in
* a many reads.
*/
//Synthetic comment -- @@ -464,17 +464,16 @@
}

/**
     * Skips {@code byteCount} bytes in this stream. Subsequent calls to
     * {@code read} will not return these bytes unless {@code reset} is
* used.
     * Skipping characters may invalidate a mark if {@code markLimit}
* is surpassed.
*
     * @param byteCount
     *            the maximum number of characters to skip.
* @return the number of characters actually skipped.
     * @throws IllegalArgumentException
     *             if {@code byteCount < 0}.
* @throws IOException
*             if this reader is closed or some other I/O error occurs.
* @see #mark(int)
//Synthetic comment -- @@ -482,35 +481,35 @@
* @see #reset()
*/
@Override
    public long skip(long byteCount) throws IOException {
        if (byteCount < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + byteCount);
}
synchronized (lock) {
checkNotClosed();
            if (byteCount < 1) {
return 0;
}
            if (end - pos >= byteCount) {
                pos += byteCount;
                return byteCount;
}

long read = end - pos;
pos = end;
            while (read < byteCount) {
if (fillBuf() == -1) {
return read;
}
                if (end - pos >= byteCount - read) {
                    pos += byteCount - read;
                    return byteCount;
}
// Couldn't get all the characters, skip what we read
read += (end - pos);
pos = end;
}
            return byteCount;
}
}
}







