/*Take the Buffer's position into account when accessing the array

Prior to this patch, our APIs would take into account a Buffer's position
if it was backed by native memory. The position would not be taken into
account for buffers backed by a Dalvik array.

(cherry-pick of 177714f960fc0c586930e23b4c98341b9b4eef56.)

Bug:http://code.google.com/p/android/issues/detail?id=32588Change-Id:Id27bc73e4cf44caa4e304fc17a52c4732d2cd97c*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/NIOAccess.java b/luni/src/main/java/java/nio/NIOAccess.java
//Synthetic comment -- index 361a37f..36c41cf 100644

//Synthetic comment -- @@ -28,7 +28,7 @@
* different than what the Harmony implementation calls a "base
* address."
*
     * @param b the Buffer to be queried
* @return the native pointer to the Buffer's data at its current
* position, or 0 if there is none
*/
//Synthetic comment -- @@ -44,7 +44,7 @@
* Returns the underlying Java array containing the data of the
* given Buffer, or null if the Buffer is not backed by a Java array.
*
     * @param b the Buffer to be queried
* @return the Java array containing the Buffer's data, or null if
* there is none
*/
//Synthetic comment -- @@ -55,13 +55,14 @@
/**
* Returns the offset in bytes from the start of the underlying
* Java array object containing the data of the given Buffer to
     * the actual start of the data. The start of the data takes into
     * account the Buffer's current position. This method is only
     * meaningful if getBaseArray() returns non-null.
*
     * @param b the Buffer to be queried
* @return the data offset in bytes to the start of this Buffer's data
*/
static int getBaseArrayOffset(Buffer b) {
        return b.hasArray() ? ((b.arrayOffset() + b.position) << b._elementSizeShift) : 0;
}
}







