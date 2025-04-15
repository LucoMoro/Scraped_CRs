/*When application menu key is pressed, the applications are not visible, although isVisible() returns true. We force the mZoom to zero, if RenderScript fails to do so.

Change-Id:I8d136b3a6a3cd0286aa9f6f5dcb096e0db20297fSigned-off-by: Madan Ankapura <mankapur@sta.samsung.com>*/
//Synthetic comment -- diff --git a/src/com/android/launcher2/AllApps3D.java b/src/com/android/launcher2/AllApps3D.java
//Synthetic comment -- index 308ad28..4fad656 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.ProgramFragment;
//Synthetic comment -- @@ -131,7 +132,10 @@
private boolean mSurrendered;

private int mRestoreFocusIndex = -1;
    
@SuppressWarnings({"UnusedDeclaration"})
static class Defines {
public static final int ALLOC_PARAMS = 0;
//Synthetic comment -- @@ -822,6 +826,10 @@
* sRollo being null.
*/
public boolean isVisible() {
return sRollo != null && mZoom > 0.001f;
}








