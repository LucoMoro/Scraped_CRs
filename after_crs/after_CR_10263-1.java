/*dx incorrectly writes registers as a signed leb128 in the dex debug info

This has no functional effect, because reading the signed leb128 value
as an unsignedleb128 still produces the same value. But the encoding is
1 byte longer in some cases.*/




//Synthetic comment -- diff --git a/dx/src/com/android/dx/dex/file/DebugInfoEncoder.java b/dx/src/com/android/dx/dex/file/DebugInfoEncoder.java
//Synthetic comment -- index 780e18d..3d36aed 100644

//Synthetic comment -- @@ -901,7 +901,7 @@
"Signed value where unsigned required: " + n);
}

        output.writeUnsignedLeb128(n);
}

/**







