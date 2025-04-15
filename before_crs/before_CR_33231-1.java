/*Set content width of spinner to fit on device

Commit for issue athttp://code.google.com/p/android/issues/detail?id=25916&colspec=ID%20Type%20Status%20Owner%20Summary%20Stars&start=100[Problem]
Dropdown popup of Spinner become wider than device's width if selected
item's content's width is larger than device's screen.

[Cause]
Spinner just display dropdown popup with measured content's width
although measured content's width is wider than device's screen.

[Solution]
If calculated content's width is wider than device's screen, set result
to fit on device when measured.

Change-Id:I3276e5ff745c6ba1437c07fc55645d6b53fef89aSigned-off-by: SeongJae Park <sj38.park@gmail.com>*/
//Synthetic comment -- diff --git a/core/java/android/widget/Spinner.java b/core/java/android/widget/Spinner.java
//Synthetic comment -- index ec3790e..953b278 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
//Synthetic comment -- @@ -526,6 +527,22 @@
width += mTempRect.left + mTempRect.right;
}

return width;
}








