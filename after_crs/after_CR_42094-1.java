/*Fix a bug where debugInfos' size wasn't being computed.

Previously debugInfos' size of 0 caused a malformed dex file
because debug data was present but not mentioned in the table
of contents.

Change-Id:I07171aaee12fef9f303fc505909f44ef1a714114*/




//Synthetic comment -- diff --git a/dx/src/com/android/dx/merge/DexMerger.java b/dx/src/com/android/dx/merge/DexMerger.java
//Synthetic comment -- index a6f413a..c48f436 100644

//Synthetic comment -- @@ -33,9 +33,7 @@
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
* Combine two dex files into one.
//Synthetic comment -- @@ -839,6 +837,7 @@
private static final byte DBG_SET_FILE = 0x09;

private void transformDebugInfoItem(DexBuffer.Section in, IndexMap indexMap) {
        contentsOut.debugInfos.size++;
int lineStart = in.readUleb128();
debugInfoOut.writeUleb128(lineStart);








