/*Improve ObjectInputStream#read(byte[], int, int) documentation

Count is not a parameter for this method.  This method just calls
InputStream#read(byte[], int, int), so update the documentation to roughly
match.  Point users to readFully(), which is likely what they want.

Change-Id:I5832a4f5303f912d868a8bdb8b5b1f485a13add7Signed-off-by: Brad Larson <bklarson@gmail.com>*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/io/ObjectInputStream.java b/luni/src/main/java/java/io/ObjectInputStream.java
//Synthetic comment -- index d3af278..b6f1636 100644

//Synthetic comment -- @@ -533,9 +533,8 @@

/**
* Reads at most {@code length} bytes from the source stream and stores them
     * in byte array {@code buffer} starting at {@code offset}. In mose cases,
     * @link(#readFully(byte[], int, int) is a more appropriate method to call.
*
* @param buffer
*            the array in which to store the bytes read.
//Synthetic comment -- @@ -554,6 +553,8 @@
*             if an error occurs while reading from this stream.
* @throws NullPointerException
*             if {@code buffer} is {@code null}.
     *
     * @see #readFully(byte[], int, int)
*/
@Override
public int read(byte[] buffer, int offset, int length) throws IOException {







