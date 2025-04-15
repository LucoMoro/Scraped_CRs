/*Fix overlapping text in EditText widget

- invalidate display list when the number of line is changed. (deltaLines != 0)
- execute the for loop only when deltaLines is not zero.

Display list should be invalidated when the number of line is changed.
If not it causes overlapping text issue in EditText widget.
This patch will fix the following issue:http://code.google.com/p/android/issues/detail?id=38770Change-Id:I5b0b200fce2847ee157e3f21c5f9582230a8b2d1Signed-off-by: Sangkyu Lee <geteuid@gmail.com>*/
//Synthetic comment -- diff --git a/core/java/android/text/DynamicLayout.java b/core/java/android/text/DynamicLayout.java
//Synthetic comment -- index d909362..f421f45 100644

//Synthetic comment -- @@ -503,8 +503,11 @@

mNumberOfBlocks = newNumberOfBlocks;
final int deltaLines = newLineCount - (endLine - startLine + 1);
        for (int i = firstBlock + numAddedBlocks; i < mNumberOfBlocks; i++) {
            mBlockEndLines[i] += deltaLines;
}

int blockIndex = firstBlock;







