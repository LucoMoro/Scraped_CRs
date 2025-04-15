/*Explain that System.arraycopy is like memmove(3), not memcpy(3).

Change-Id:I1e581cfbddf861fdcd918833dcd85306b96ef1ac*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/System.java b/luni/src/main/java/java/lang/System.java
//Synthetic comment -- index df84c61..62845d6 100644

//Synthetic comment -- @@ -135,6 +135,11 @@
* starting at offset {@code srcPos}, into the array {@code dst},
* starting at offset {@code dstPos}.
*
     * <p>The source and destination arrays can be the same array,
     * in which case copying is performed as if the source elements
     * are first copied into a temporary array and then into the
     * destination array.
     *
* @param src
*            the source array to copy the content.
* @param srcPos







