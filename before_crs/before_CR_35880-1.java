/*Test fix: The image parameter is mandatory

Change-Id:I9b8ca8e0384f1cb2343d13e8e43b268c63ed4dcb*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java
//Synthetic comment -- index e912f53..30a2e78 100644

//Synthetic comment -- @@ -22,6 +22,8 @@
import static com.android.ide.eclipse.adt.AdtConstants.DOT_PNG;
import static com.android.ide.eclipse.adt.AdtUtils.endsWithIgnoreCase;

import com.android.ide.common.api.Rect;

import org.eclipse.swt.graphics.RGB;
//Synthetic comment -- @@ -124,7 +126,10 @@
* @return a cropped version of the source image, or null if the whole image was blank
*         and cropping completely removed everything
*/
    public static BufferedImage cropBlank(BufferedImage image, Rect initialCrop) {
return cropBlank(image, initialCrop, image.getType());
}

//Synthetic comment -- @@ -167,8 +172,11 @@
* @return a cropped version of the source image, or null if the whole image was blank
*         and cropping completely removed everything
*/
    public static BufferedImage cropColor(BufferedImage image,
            final int blankArgb, Rect initialCrop) {
return cropColor(image, blankArgb, initialCrop, image.getType());
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtilsTest.java
//Synthetic comment -- index 4bd0bba..9e9c734 100644

//Synthetic comment -- @@ -157,12 +157,6 @@
assertEquals(0xFFFF0000, crop.getRGB(49, 49));
}

    public void testNullOk() throws Exception {
        ImageUtils.cropBlank(null, null);
        ImageUtils.cropColor(null, 0, null);
    }


public void testNothingTodo() throws Exception {
BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB_PRE);
Graphics g = image.getGraphics();







