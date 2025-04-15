/*Fix a typo where BufferedReader refers to chars as bytes.

Change-Id:I4be1fe88f97c10bc5142df05e2ffc9a3b4094d81*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/io/BufferedReader.java b/luni/src/main/java/java/io/BufferedReader.java
//Synthetic comment -- index 03ecf9f..ad35844 100644

//Synthetic comment -- @@ -464,17 +464,16 @@
}

/**
     * Skips {@code charCount} bytes in this stream. Subsequent calls to
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







