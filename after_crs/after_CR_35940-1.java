/*Use the real StatusBar Height for InstrumentationTest

Retreive the status bar height to avoid to click with MotionEvent on statusbar during the InstrumentationTest

Signed-off-by: Fabien DUVOUX <fabien.duvoux@gmail.com>*/




//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/InstrumentationTest.java b/tests/tests/app/src/android/app/cts/InstrumentationTest.java
//Synthetic comment -- index e3eb46d..6478efe 100644

//Synthetic comment -- @@ -72,9 +72,9 @@
final long downTime = SystemClock.uptimeMillis();
final long eventTime = SystemClock.uptimeMillis();
// use coordinates for MotionEvent that do not include the status bar
        // TODO: is there a more deterministic way to get this values
final long x = 55;
        final long y = mContext.getResources().getDimensionPixelSize(com.android.internal.R.dimen.status_bar_height)+10;
final int metaState = 0;
mMotionEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y,
metaState);







