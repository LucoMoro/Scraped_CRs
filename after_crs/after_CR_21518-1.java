/*Fix cursor-handles for SOFT_INPUT_ADJUST_PAN

Avoid comparing coordinates from different coordinate systems
by using the offset from getChildVisibleRect() rather than that
from getLocationInWindow()

Change-Id:Ib33c8bf93169b97aa0993283b56ec301b88a5a12*/




//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index bdc5014..8c6bad8 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
//Synthetic comment -- @@ -197,6 +198,7 @@

final int[] mTempCoords = new int[2];
Rect mTempRect;
    Point mTempPoint = new Point(0, 0);

private ColorStateList mTextColor;
private int mCurTextColor;
//Synthetic comment -- @@ -7881,15 +7883,19 @@
clip.right = right - compoundPaddingRight;
clip.bottom = bottom - extendedPaddingBottom;

            final Point globalOffset = mTempPoint;
            globalOffset.set(0, 0);

final ViewParent parent = hostView.getParent();
            if (parent == null || !parent.getChildVisibleRect(hostView, clip, globalOffset)) {
return false;
}

            // Note: The coordinates below will be in the relative to the
            //       ViewRoot of hostView. When ADJUST_PAN is set they will
            //       differ significantly from screen coordinates.
            final int posX = globalOffset.x + mPositionX + (int) mHotspotX;
            final int posY = globalOffset.y + mPositionY + (int) mHotspotY;

return posX >= clip.left && posX <= clip.right &&
posY >= clip.top && posY <= clip.bottom;







