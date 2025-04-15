/*Monkey Crash: NullPointerException in WebTextView

getLayout() may return null if text or width has recently changed.
This fixeshttp://code.google.com/p/android/issues/detail?id=8055Change-Id:Ibdf1226b3fec55cf3b9349741c228b9953a61585*/




//Synthetic comment -- diff --git a/core/java/android/webkit/WebTextView.java b/core/java/android/webkit/WebTextView.java
//Synthetic comment -- index 19abec1..33e52905 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextPaint;
//Synthetic comment -- @@ -482,8 +483,12 @@
int smallerSlop = slop/2;
if (dx > smallerSlop || dy > smallerSlop) {
if (mWebView != null) {
                    final Layout layout = getLayout();
                    float maxScrollX = 0;
                    if (layout != null) {
                        maxScrollX = (float) Touch.getMaxScrollX(this,
                                layout, mScrollY);
                    }
if (DebugFlags.WEB_TEXT_VIEW) {
Log.v(LOGTAG, "onTouchEvent x=" + mScrollX + " y="
+ mScrollY + " maxX=" + maxScrollX);







