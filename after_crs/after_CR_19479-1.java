/*ninepatch support for drawing at a different density.

Change-Id:I0932f8a51d30c256157fccf67e77cf165ba93b58*/




//Synthetic comment -- diff --git a/ninepatch/src/com/android/ninepatch/NinePatch.java b/ninepatch/src/com/android/ninepatch/NinePatch.java
//Synthetic comment -- index ed5ecff..2803a9e 100644

//Synthetic comment -- @@ -173,7 +173,7 @@
* @param scaledHeight
*/
public void draw(Graphics2D graphics2D, int x, int y, int scaledWidth, int scaledHeight) {
        mChunk.draw(mImage, graphics2D, x, y, scaledWidth, scaledHeight, 0 , 0);
}

private NinePatch(BufferedImage image) {








//Synthetic comment -- diff --git a/ninepatch/src/com/android/ninepatch/NinePatchChunk.java b/ninepatch/src/com/android/ninepatch/NinePatchChunk.java
//Synthetic comment -- index dedc1d6..6dca61e 100644

//Synthetic comment -- @@ -77,6 +77,38 @@
}

public void draw(BufferedImage image, Graphics2D graphics2D, int x, int y, int scaledWidth,
            int scaledHeight, int destDensity, int srcDensity) {

        boolean scaling = destDensity != srcDensity && destDensity != 0 && srcDensity != 0;

        if (scaling) {
            try {
                graphics2D = (Graphics2D) graphics2D.create();

                // scale and transform
                float densityScale = (float) destDensity / srcDensity;

                // translate/rotate the canvas.
                graphics2D.translate(x, y);
                graphics2D.scale(densityScale, densityScale);

                // sets the new drawing bounds.
                scaledWidth /= densityScale;
                scaledHeight /= densityScale;
                x = y = 0;

                // draw
                draw(image, graphics2D, x, y, scaledWidth, scaledHeight);
            } finally {
                graphics2D.dispose();
            }
        } else {
            // non density-scaled rendering
            draw(image, graphics2D, x, y, scaledWidth, scaledHeight);
        }
    }

    private void draw(BufferedImage image, Graphics2D graphics2D, int x, int y, int scaledWidth,
int scaledHeight) {
if (scaledWidth <= 1 || scaledHeight <= 1) {
return;







