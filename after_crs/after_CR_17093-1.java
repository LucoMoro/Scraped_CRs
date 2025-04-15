/*Fix TouchTest#testOnTouchEvent Test String

Bug 2437360

Fix this test to work with any display width by creating
a test string that is wider than the screen rather than
using a fixed size and expecting the TextView to only scroll
as much as there is left to scroll.

Change-Id:I578c9911b595397d44c222bdbc444382926b4acd*/




//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/TouchTest.java b/tests/tests/text/src/android/text/method/cts/TouchTest.java
//Synthetic comment -- index bc62bfe..e5275af 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.Touch;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.TextView;

//Synthetic comment -- @@ -128,24 +129,37 @@
@ToBeFixed(bug = "1695243", explanation = "Android API javadocs are incomplete, " +
"should add @throws clause into javadoc.")
public void testOnTouchEvent() throws Throwable {
final TextView tv = new TextView(mActivity);

        // Create a string that is wider than the screen.
        DisplayMetrics metrics = mActivity.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        TextPaint paint = tv.getPaint();
        String text = LONG_TEXT;
        int textWidth = Math.round(paint.measureText(text));
        while (textWidth < screenWidth) {
            text += LONG_TEXT;
            textWidth = Math.round(paint.measureText(text));
        }

        // Drag the difference between the text width and the screen width.
        int dragAmount = Math.min(screenWidth, textWidth - screenWidth);
        assertTrue(dragAmount > 0);
        final String finalText = text;
        final SpannableString spannable = new SpannableString(finalText);
runTestOnUiThread(new Runnable() {
public void run() {
mActivity.setContentView(tv);
tv.setSingleLine(true);
                tv.setText(finalText);
}
});
getInstrumentation().waitForIdleSync();

long downTime = SystemClock.uptimeMillis();
long eventTime = SystemClock.uptimeMillis();
final MotionEvent event1 = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, dragAmount, 0, 0);
final MotionEvent event2 = MotionEvent.obtain(downTime, eventTime,
MotionEvent.ACTION_MOVE, 0, 0, 0);
final MotionEvent event3 = MotionEvent.obtain(downTime, eventTime,
//Synthetic comment -- @@ -175,7 +189,7 @@
getInstrumentation().waitForIdleSync();
assertTrue(mReturnFromTouchEvent);
// TextView has been scrolled.
        assertEquals(dragAmount, tv.getScrollX());
assertEquals(0, tv.getScrollY());
assertEquals(0, Touch.getInitialScrollX(tv, spannable));
assertEquals(0, Touch.getInitialScrollY(tv, spannable));
//Synthetic comment -- @@ -189,7 +203,7 @@
getInstrumentation().waitForIdleSync();
assertTrue(mReturnFromTouchEvent);
// TextView has not been scrolled.
        assertEquals(dragAmount, tv.getScrollX());
assertEquals(0, tv.getScrollY());
assertEquals(-1, Touch.getInitialScrollX(tv, spannable));
assertEquals(-1, Touch.getInitialScrollY(tv, spannable));







