/*Fix image conversion

Fix the image utils methods such that they can correctly crop/scale
images where the image.getType() == 0.  Before this fix, you'd get an
exception from BufferedImage if you instantiate the Custom View
wizard, open the sample layout and select the custom view with the
property sheet visible.

According to the docs, getType() == 0 (== BufferedImage.TYPE_CUSTOM)
"This type is only used as a return value for the getType() method".

(cherry picked from commit 5d22d1c55254b31d60009f79e481f6cc0a3715da)

Change-Id:I8fdf98ee5a687634606bb6bd1b2b549ea7569eec*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java
//Synthetic comment -- index 30a2e78..5e9ec51 100644

//Synthetic comment -- @@ -310,8 +310,13 @@
int height = y2 - y1;

// Now extract the sub-image
        if (imageType == -1) {
            imageType = image.getType();
        }
        if (imageType == BufferedImage.TYPE_CUSTOM) {
            imageType = BufferedImage.TYPE_INT_ARGB;
        }
        BufferedImage cropped = new BufferedImage(width, height, imageType);
Graphics g = cropped.getGraphics();
g.drawImage(image, 0, 0, width, height, x1, y1, x2, y2, null);

//Synthetic comment -- @@ -467,7 +472,11 @@
public static BufferedImage subImage(BufferedImage source, int x1, int y1, int x2, int y2) {
int width = x2 - x1;
int height = y2 - y1;
        int imageType = source.getType();
        if (imageType == BufferedImage.TYPE_CUSTOM) {
            imageType = BufferedImage.TYPE_INT_ARGB;
        }
        BufferedImage sub = new BufferedImage(width, height, imageType);
Graphics g = sub.getGraphics();
g.drawImage(source, 0, 0, width, height, x1, y1, x2, y2, null);
g.dispose();
//Synthetic comment -- @@ -541,7 +550,11 @@
int sourceHeight = source.getHeight();
int destWidth = Math.max(1, (int) (xScale * sourceWidth));
int destHeight = Math.max(1, (int) (yScale * sourceHeight));
        int imageType = source.getType();
        if (imageType == BufferedImage.TYPE_CUSTOM) {
            imageType = BufferedImage.TYPE_INT_ARGB;
        }
        BufferedImage scaled = new BufferedImage(destWidth, destHeight, imageType);
Graphics2D g2 = scaled.createGraphics();
g2.setComposite(AlphaComposite.Src);
g2.setColor(new Color(0, true));







