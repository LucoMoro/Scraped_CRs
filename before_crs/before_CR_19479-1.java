/*ninepatch support for drawing at a different density.

Change-Id:I0932f8a51d30c256157fccf67e77cf165ba93b58*/
//Synthetic comment -- diff --git a/ninepatch/src/com/android/ninepatch/NinePatch.java b/ninepatch/src/com/android/ninepatch/NinePatch.java
//Synthetic comment -- index ed5ecff..2803a9e 100644

//Synthetic comment -- @@ -173,7 +173,7 @@
* @param scaledHeight
*/
public void draw(Graphics2D graphics2D, int x, int y, int scaledWidth, int scaledHeight) {
        mChunk.draw(mImage, graphics2D, x, y, scaledWidth, scaledHeight);
}

private NinePatch(BufferedImage image) {








//Synthetic comment -- diff --git a/ninepatch/src/com/android/ninepatch/NinePatchChunk.java b/ninepatch/src/com/android/ninepatch/NinePatchChunk.java
//Synthetic comment -- index dedc1d6..6dca61e 100644

//Synthetic comment -- @@ -77,6 +77,38 @@
}

public void draw(BufferedImage image, Graphics2D graphics2D, int x, int y, int scaledWidth,
int scaledHeight) {
if (scaledWidth <= 1 || scaledHeight <= 1) {
return;







