/*Move the ninepatch info into its own class.

The chunk is what is used separately by the default NinePatch class,
through serialization to make a byte[] out of it.

Since we are moving away from replacement classes to delegate, splitting
the chunk info away from the image allows us to use the normal
NinePatch(Drawable).

Change-Id:Ifd86dc2aa9b485d0e97a2d4a248621cfcddda9ab*/
//Synthetic comment -- diff --git a/ninepatch/src/com/android/ninepatch/NinePatch.java b/ninepatch/src/com/android/ninepatch/NinePatch.java
//Synthetic comment -- index 7484fd8..d70eff2 100644

//Synthetic comment -- @@ -17,49 +17,43 @@
package com.android.ninepatch;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
* Represents a 9-Patch bitmap.
*/
public class NinePatch {
public static final String EXTENSION_9PATCH = ".9.png";

private BufferedImage mImage;

    private int mMinWidth;
    private int mMinHeight;

    private int[] row;
    private int[] column;

    private boolean mVerticalStartWithPatch;
    private boolean mHorizontalStartWithPatch;

    private List<Rectangle> mFixed;
    private List<Rectangle> mPatches;
    private List<Rectangle> mHorizontalPatches;
    private List<Rectangle> mVerticalPatches;

    private Pair<Integer> mHorizontalPadding;
    private Pair<Integer> mVerticalPadding;

    private float mHorizontalPatchesSum;
    private float mVerticalPatchesSum;

    private int mRemainderHorizontal;

    private int mRemainderVertical;

/**
* Loads a 9 patch or regular bitmap.
* @param fileUrl the URL of the file to load.
* @param convert if <code>true</code>, non 9-patch bitmap will be converted into a 9 patch.
//Synthetic comment -- @@ -83,6 +77,9 @@
}

/**
* Loads a 9 patch or regular bitmap.
* @param stream the {@link InputStream} of the file to load.
* @param is9Patch whether the file represents a 9-patch
//Synthetic comment -- @@ -106,6 +103,9 @@
}

/**
* Loads a 9 patch or regular bitmap.
* @param image the source {@link BufferedImage}.
* @param is9Patch whether the file represents a 9-patch
//Synthetic comment -- @@ -129,336 +129,56 @@
return new NinePatch(image);
}

public int getWidth() {
return mImage.getWidth() - 2;
}

public int getHeight() {
return mImage.getHeight() - 2;
}

/**
*
* @param padding array of left, top, right, bottom padding
* @return
*/
public boolean getPadding(int[] padding) {
        padding[0] = mHorizontalPadding.mFirst; // left
        padding[2] = mHorizontalPadding.mSecond; // right
        padding[1] = mVerticalPadding.mFirst; // top
        padding[3] = mVerticalPadding.mSecond; // bottom
return true;
}


public void draw(Graphics2D graphics2D, int x, int y, int scaledWidth, int scaledHeight) {
        if (scaledWidth <= 1 || scaledHeight <= 1) {
            return;
        }

        Graphics2D g = (Graphics2D)graphics2D.create();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);


        try {
            if (mPatches.size() == 0) {
                g.drawImage(mImage, x, y, scaledWidth, scaledHeight, null);
                return;
            }

            g.translate(x, y);
            x = y = 0;

            computePatches(scaledWidth, scaledHeight);

            int fixedIndex = 0;
            int horizontalIndex = 0;
            int verticalIndex = 0;
            int patchIndex = 0;

            boolean hStretch;
            boolean vStretch;

            float vWeightSum = 1.0f;
            float vRemainder = mRemainderVertical;

            vStretch = mVerticalStartWithPatch;
            while (y < scaledHeight - 1) {
                hStretch = mHorizontalStartWithPatch;

                int height = 0;
                float vExtra = 0.0f;

                float hWeightSum = 1.0f;
                float hRemainder = mRemainderHorizontal;

                while (x < scaledWidth - 1) {
                    Rectangle r;
                    if (!vStretch) {
                        if (hStretch) {
                            r = mHorizontalPatches.get(horizontalIndex++);
                            float extra = r.width / mHorizontalPatchesSum;
                            int width = (int) (extra * hRemainder / hWeightSum);
                            hWeightSum -= extra;
                            hRemainder -= width;
                            g.drawImage(mImage, x, y, x + width, y + r.height, r.x, r.y,
                                    r.x + r.width, r.y + r.height, null);
                            x += width;
                        } else {
                            r = mFixed.get(fixedIndex++);
                            g.drawImage(mImage, x, y, x + r.width, y + r.height, r.x, r.y,
                                    r.x + r.width, r.y + r.height, null);
                            x += r.width;
                        }
                        height = r.height;
                    } else {
                        if (hStretch) {
                            r = mPatches.get(patchIndex++);
                            vExtra = r.height / mVerticalPatchesSum;
                            height = (int) (vExtra * vRemainder / vWeightSum);
                            float extra = r.width / mHorizontalPatchesSum;
                            int width = (int) (extra * hRemainder / hWeightSum);
                            hWeightSum -= extra;
                            hRemainder -= width;
                            g.drawImage(mImage, x, y, x + width, y + height, r.x, r.y,
                                    r.x + r.width, r.y + r.height, null);
                            x += width;
                        } else {
                            r = mVerticalPatches.get(verticalIndex++);
                            vExtra = r.height / mVerticalPatchesSum;
                            height = (int) (vExtra * vRemainder / vWeightSum);
                            g.drawImage(mImage, x, y, x + r.width, y + height, r.x, r.y,
                                    r.x + r.width, r.y + r.height, null);
                            x += r.width;
                        }

                    }
                    hStretch = !hStretch;
                }
                x = 0;
                y += height;
                if (vStretch) {
                    vWeightSum -= vExtra;
                    vRemainder -= height;
                }
                vStretch = !vStretch;
            }

        } finally {
            g.dispose();
        }
}

    void computePatches(int scaledWidth, int scaledHeight) {
        boolean measuredWidth = false;
        boolean endRow = true;

        int remainderHorizontal = 0;
        int remainderVertical = 0;

        if (mFixed.size() > 0) {
            int start = mFixed.get(0).y;
            for (Rectangle rect : mFixed) {
                if (rect.y > start) {
                    endRow = true;
                    measuredWidth = true;
                }
                if (!measuredWidth) {
                    remainderHorizontal += rect.width;
                }
                if (endRow) {
                    remainderVertical += rect.height;
                    endRow = false;
                    start = rect.y;
                }
            }
        }

        mRemainderHorizontal = scaledWidth - remainderHorizontal;

        mRemainderVertical = scaledHeight - remainderVertical;

        mHorizontalPatchesSum = 0;
        if (mHorizontalPatches.size() > 0) {
            int start = -1;
            for (Rectangle rect : mHorizontalPatches) {
                if (rect.x > start) {
                    mHorizontalPatchesSum += rect.width;
                    start = rect.x;
                }
            }
        } else {
            int start = -1;
            for (Rectangle rect : mPatches) {
                if (rect.x > start) {
                    mHorizontalPatchesSum += rect.width;
                    start = rect.x;
                }
            }
        }

        mVerticalPatchesSum = 0;
        if (mVerticalPatches.size() > 0) {
            int start = -1;
            for (Rectangle rect : mVerticalPatches) {
                if (rect.y > start) {
                    mVerticalPatchesSum += rect.height;
                    start = rect.y;
                }
            }
        } else {
            int start = -1;
            for (Rectangle rect : mPatches) {
                if (rect.y > start) {
                    mVerticalPatchesSum += rect.height;
                    start = rect.y;
                }
            }
        }
    }


private NinePatch(BufferedImage image) {
mImage = image;

        findPatches();
    }

    private void findPatches() {
        int width = mImage.getWidth();
        int height = mImage.getHeight();

        row = GraphicsUtilities.getPixels(mImage, 0, 0, width, 1, row);
        column = GraphicsUtilities.getPixels(mImage, 0, 0, 1, height, column);

        boolean[] result = new boolean[1];
        Pair<List<Pair<Integer>>> left = getPatches(column, result);
        mVerticalStartWithPatch = result[0];

        result = new boolean[1];
        Pair<List<Pair<Integer>>> top = getPatches(row, result);
        mHorizontalStartWithPatch = result[0];

        mFixed = getRectangles(left.mFirst, top.mFirst);
        mPatches = getRectangles(left.mSecond, top.mSecond);

        if (mFixed.size() > 0) {
            mHorizontalPatches = getRectangles(left.mFirst, top.mSecond);
            mVerticalPatches = getRectangles(left.mSecond, top.mFirst);
        } else {
            if (top.mFirst.size() > 0) {
                mHorizontalPatches = new ArrayList<Rectangle>(0);
                mVerticalPatches = getVerticalRectangles(top.mFirst);
            } else if (left.mFirst.size() > 0) {
                mHorizontalPatches = getHorizontalRectangles(left.mFirst);
                mVerticalPatches = new ArrayList<Rectangle>(0);
            } else {
                mHorizontalPatches = mVerticalPatches = new ArrayList<Rectangle>(0);
            }
        }

        row = GraphicsUtilities.getPixels(mImage, 0, height - 1, width, 1, row);
        column = GraphicsUtilities.getPixels(mImage, width - 1, 0, 1, height, column);

        top = getPatches(row, result);
        mHorizontalPadding = getPadding(top.mFirst);

        left = getPatches(column, result);
        mVerticalPadding = getPadding(left.mFirst);
    }

    private List<Rectangle> getVerticalRectangles(List<Pair<Integer>> topPairs) {
        List<Rectangle> rectangles = new ArrayList<Rectangle>();
        for (Pair<Integer> top : topPairs) {
            int x = top.mFirst;
            int width = top.mSecond - top.mFirst;

            rectangles.add(new Rectangle(x, 1, width, mImage.getHeight() - 2));
        }
        return rectangles;
    }

    private List<Rectangle> getHorizontalRectangles(List<Pair<Integer>> leftPairs) {
        List<Rectangle> rectangles = new ArrayList<Rectangle>();
        for (Pair<Integer> left : leftPairs) {
            int y = left.mFirst;
            int height = left.mSecond - left.mFirst;

            rectangles.add(new Rectangle(1, y, mImage.getWidth() - 2, height));
        }
        return rectangles;
    }

    private Pair<Integer> getPadding(List<Pair<Integer>> pairs) {
        if (pairs.size() == 0) {
            return new Pair<Integer>(0, 0);
        } else if (pairs.size() == 1) {
            if (pairs.get(0).mFirst == 1) {
                return new Pair<Integer>(pairs.get(0).mSecond - pairs.get(0).mFirst, 0);
            } else {
                return new Pair<Integer>(0, pairs.get(0).mSecond - pairs.get(0).mFirst);
            }
        } else {
            int index = pairs.size() - 1;
            return new Pair<Integer>(pairs.get(0).mSecond - pairs.get(0).mFirst,
                    pairs.get(index).mSecond - pairs.get(index).mFirst);
        }
    }

    private List<Rectangle> getRectangles(List<Pair<Integer>> leftPairs,
            List<Pair<Integer>> topPairs) {
        List<Rectangle> rectangles = new ArrayList<Rectangle>();
        for (Pair<Integer> left : leftPairs) {
            int y = left.mFirst;
            int height = left.mSecond - left.mFirst;
            for (Pair<Integer> top : topPairs) {
                int x = top.mFirst;
                int width = top.mSecond - top.mFirst;

                rectangles.add(new Rectangle(x, y, width, height));
            }
        }
        return rectangles;
    }

    private Pair<List<Pair<Integer>>> getPatches(int[] pixels, boolean[] startWithPatch) {
        int lastIndex = 1;
        int lastPixel = pixels[1];
        boolean first = true;

        List<Pair<Integer>> fixed = new ArrayList<Pair<Integer>>();
        List<Pair<Integer>> patches = new ArrayList<Pair<Integer>>();

        for (int i = 1; i < pixels.length - 1; i++) {
            int pixel = pixels[i];
            if (pixel != lastPixel) {
                if (lastPixel == 0xFF000000) {
                    if (first) startWithPatch[0] = true;
                    patches.add(new Pair<Integer>(lastIndex, i));
                } else {
                    fixed.add(new Pair<Integer>(lastIndex, i));
                }
                first = false;

                lastIndex = i;
                lastPixel = pixel;
            }
        }
        if (lastPixel == 0xFF000000) {
            if (first) startWithPatch[0] = true;
            patches.add(new Pair<Integer>(lastIndex, pixels.length - 1));
        } else {
            fixed.add(new Pair<Integer>(lastIndex, pixels.length - 1));
        }

        if (patches.size() == 0) {
            patches.add(new Pair<Integer>(1, pixels.length - 1));
            startWithPatch[0] = true;
            fixed.clear();
        }

        return new Pair<List<Pair<Integer>>>(fixed, patches);
}

private static void ensure9Patch(BufferedImage image) {
//Synthetic comment -- @@ -496,19 +216,4 @@

return buffer;
}

    static class Pair<E> {
        E mFirst;
        E mSecond;

        Pair(E first, E second) {
            mFirst = first;
            mSecond = second;
        }

        @Override
        public String toString() {
            return "Pair[" + mFirst + ", " + mSecond + "]";
        }
    }
}








//Synthetic comment -- diff --git a/ninepatch/src/com/android/ninepatch/NinePatchChunk.java b/ninepatch/src/com/android/ninepatch/NinePatchChunk.java
new file mode 100644
//Synthetic comment -- index 0000000..0596871

//Synthetic comment -- @@ -0,0 +1,428 @@







