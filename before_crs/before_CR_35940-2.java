/*Use the real StatusBar Height for InstrumentationTest

Retreive the status bar height to avoid to click with MotionEvent on statusbar during the InstrumentationTest

Change-Id:I67da35e2f39d3eb24580df3b14fa1695de275742Signed-off-by: Fabien DUVOUX <fabien.duvoux@gmail.com>*/
//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/InstrumentationTest.java b/tests/tests/app/src/android/app/cts/InstrumentationTest.java
//Synthetic comment -- index e3eb46d..8aa5504 100644

//Synthetic comment -- @@ -45,6 +45,8 @@
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;

import com.android.cts.stub.R;

//Synthetic comment -- @@ -72,9 +74,14 @@
final long downTime = SystemClock.uptimeMillis();
final long eventTime = SystemClock.uptimeMillis();
// use coordinates for MotionEvent that do not include the status bar
        // TODO: is there a more deterministic way to get these values
        final long x = 55;
        final long y = 55;
final int metaState = 0;
mMotionEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y,
metaState);







