/*Optimize drawHardwareAccelerated method in Editor class

When inserting a new line or breaking a line,
every display list was invalidated and rebuilt before.
However, we can reuse the display lists above intactly and also
reuse the display lists below with only updating drawing locations.

This patch reuses the display lists if possible.
The display lists above the inserted line are just reused
and the display lists below are reused with only updating
drawing locations not fullly rebuilt.

mIndexOfFirstChangedBlock is the index of the first block
which is moved by inserting or breaking a line.
So the display list whose index is >= mIndexOfFirstChangedBlock
only needs to update its drawing location.

Change-Id:Ica20deb0ebb5750de21356ed31fa9f86e657ff92Signed-off-by: Sangkyu Lee <sk82.lee@lge.com>*/




//Synthetic comment -- diff --git a/core/java/android/text/DynamicLayout.java b/core/java/android/text/DynamicLayout.java
//Synthetic comment -- index d909362..8c41068 100644

//Synthetic comment -- @@ -503,8 +503,15 @@

mNumberOfBlocks = newNumberOfBlocks;
final int deltaLines = newLineCount - (endLine - startLine + 1);
        if (deltaLines != 0) {
            // Display list whose index is >= mIndexFirstChangedBlock is valid
            // but it needs to update its drawing location.
            mIndexFirstChangedBlock = firstBlock + numAddedBlocks;
            for (int i = mIndexFirstChangedBlock; i < mNumberOfBlocks; i++) {
                mBlockEndLines[i] += deltaLines;
            }
        } else {
            mIndexFirstChangedBlock = mNumberOfBlocks;
}

int blockIndex = firstBlock;
//Synthetic comment -- @@ -559,6 +566,20 @@
return mNumberOfBlocks;
}

    /**
     * @hide
     */
    public int getIndexFirstChangedBlock() {
        return mIndexFirstChangedBlock;
    }

    /*
     * @hide
     */
    public void setIndexFirstChangedBlock(int i) {
        mIndexFirstChangedBlock = i;
    }

@Override
public int getLineCount() {
return mInts.size() - 1;
//Synthetic comment -- @@ -697,6 +718,8 @@
private int[] mBlockIndices;
// Number of items actually currently being used in the above 2 arrays
private int mNumberOfBlocks;
    // The first index of the blocks whose locations are changed
    private int mIndexFirstChangedBlock;

private int mTopPadding, mBottomPadding;









//Synthetic comment -- diff --git a/core/java/android/widget/Editor.java b/core/java/android/widget/Editor.java
//Synthetic comment -- index b1a44c5..8815593 100644

//Synthetic comment -- @@ -122,7 +122,6 @@
InputMethodState mInputMethodState;

DisplayList[] mTextDisplayLists;

boolean mFrozenWithFocus;
boolean mSelectionMoved;
//Synthetic comment -- @@ -1257,20 +1256,11 @@
mTextDisplayLists = new DisplayList[ArrayUtils.idealObjectArraySize(0)];
}

DynamicLayout dynamicLayout = (DynamicLayout) layout;
int[] blockEndLines = dynamicLayout.getBlockEndLines();
int[] blockIndices = dynamicLayout.getBlockIndices();
final int numberOfBlocks = dynamicLayout.getNumberOfBlocks();
            final int indexFirstChangedBlock = dynamicLayout.getIndexFirstChangedBlock();

int endOfPreviousBlock = -1;
int searchStartIndex = 0;
//Synthetic comment -- @@ -1295,7 +1285,8 @@
if (blockIsInvalid) blockDisplayList.invalidate();
}

                final boolean blockDisplayListIsInvalid = !blockDisplayList.isValid();
                if (i >= indexFirstChangedBlock || blockDisplayListIsInvalid) {
final int blockBeginLine = endOfPreviousBlock + 1;
final int top = layout.getLineTop(blockBeginLine);
final int bottom = layout.getLineBottom(blockEndLine);
//Synthetic comment -- @@ -1312,24 +1303,30 @@
right = (int) (max + 0.5f);
}

                    // Rebuild display list if it is invalid
                    if (blockDisplayListIsInvalid) {
                        final HardwareCanvas hardwareCanvas = blockDisplayList.start();
                        try {
                            // Tighten the bounds of the viewport to the actual text size
                            hardwareCanvas.setViewport(right - left, bottom - top);
                            // The dirty rect should always be null for a display list
                            hardwareCanvas.onPreDraw(null);
                            // drawText is always relative to TextView's origin, this translation brings
                            // this range of text back to the top left corner of the viewport
                            hardwareCanvas.translate(-left, -top);
                            layout.drawText(hardwareCanvas, blockBeginLine, blockEndLine);
                            // No need to untranslate, previous context is popped after drawDisplayList
                        } finally {
                            hardwareCanvas.onPostDraw();
                            blockDisplayList.end();
                            // Same as drawDisplayList below, handled by our TextView's parent
                            blockDisplayList.setClipChildren(false);
                        }
}

                    // Valid disply list whose index is >= indexFirstChangedBlock
                    // only needs to update its drawing location.
                    blockDisplayList.setLeftTopRightBottom(left, top, right, bottom);
}

((HardwareCanvas) canvas).drawDisplayList(blockDisplayList, null,
//Synthetic comment -- @@ -1337,6 +1334,8 @@

endOfPreviousBlock = blockEndLine;
}

            dynamicLayout.setIndexFirstChangedBlock(numberOfBlocks);
} else {
// Boring layout is used for empty and hint text
layout.drawText(canvas, firstLine, lastLine);








//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index 5d90400..71c8a56 100644

//Synthetic comment -- @@ -6323,7 +6323,6 @@
mDeferScroll = -1;
bringPointIntoView(Math.min(curs, mText.length()));
}
}

private boolean isShowingHint() {







