/*Set width of spinner's dropdown to fit on device

Commit for issue athttp://code.google.com/p/android/issues/detail?id=25916&colspec=ID%20Type%20Status%20Owner%20Summary%20Stars&start=100[Problem]
Dropdown popup of Spinner become wider than device's width if selected
item's content's width is larger than device's screen.

[Cause]
Spinner just display dropdown popup with measured content's width
although measured content's width is wider than device's screen.

[Solution]
If calculated content's width is wider than device's screen, set width
of spinner's dropdown popup to fit on device.

Change-Id:I3276e5ff745c6ba1437c07fc55645d6b53fef89aSigned-off-by: SeongJae Park <sj38.park@gmail.com>*/
//Synthetic comment -- diff --git a/core/java/android/widget/Spinner.java b/core/java/android/widget/Spinner.java
//Synthetic comment -- index ec3790e..ae11a16 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
//Synthetic comment -- @@ -38,13 +39,13 @@
*
* <p>See the <a href="{@docRoot}resources/tutorials/views/hello-spinner.html">Spinner
* tutorial</a>.</p>
 * 
* @attr ref android.R.styleable#Spinner_prompt
*/
@Widget
public class Spinner extends AbsSpinner implements OnClickListener {
private static final String TAG = "Spinner";
    
// Only measure this many items to get a decent max width.
private static final int MAX_ITEMS_MEASURED = 15;

//Synthetic comment -- @@ -52,7 +53,7 @@
* Use a dialog window for selecting spinner options.
*/
public static final int MODE_DIALOG = 0;
    
/**
* Use a dropdown anchored to the Spinner for selecting spinner options.
*/
//Synthetic comment -- @@ -732,13 +733,28 @@

@Override
public void show() {
final int spinnerPaddingLeft = Spinner.this.getPaddingLeft();
if (mDropDownWidth == WRAP_CONTENT) {
final int spinnerWidth = Spinner.this.getWidth();
final int spinnerPaddingRight = Spinner.this.getPaddingRight();
setContentWidth(Math.max(
                        measureContentWidth((SpinnerAdapter) mAdapter, getBackground()),
                        spinnerWidth - spinnerPaddingLeft - spinnerPaddingRight));
} else if (mDropDownWidth == MATCH_PARENT) {
final int spinnerWidth = Spinner.this.getWidth();
final int spinnerPaddingRight = Spinner.this.getPaddingRight();
//Synthetic comment -- @@ -746,12 +762,6 @@
} else {
setContentWidth(mDropDownWidth);
}
            final Drawable background = getBackground();
            int bgOffset = 0;
            if (background != null) {
                background.getPadding(mTempRect);
                bgOffset = -mTempRect.left;
            }
setHorizontalOffset(bgOffset + spinnerPaddingLeft);
setInputMethodMode(ListPopupWindow.INPUT_METHOD_NOT_NEEDED);
super.show();







