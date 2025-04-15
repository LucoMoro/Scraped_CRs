/*Use the middle of screen for InstrumentationTest

use the middle of the screen to avoid to click with MotionEvent on statusbar during the InstrumentationTest

Change-Id:I67da35e2f39d3eb24580df3b14fa1695de275742Signed-off-by: Fabien DUVOUX <fabien.duvoux@gmail.com>*/
//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/InstrumentationTest.java b/tests/tests/app/src/android/app/cts/InstrumentationTest.java
//Synthetic comment -- index e3eb46d..fd4dbd6 100644

//Synthetic comment -- @@ -45,6 +45,7 @@
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;

import com.android.cts.stub.R;

//Synthetic comment -- @@ -71,10 +72,11 @@
mContext = mInstrumentation.getTargetContext();
final long downTime = SystemClock.uptimeMillis();
final long eventTime = SystemClock.uptimeMillis();
        // use coordinates for MotionEvent that do not include the status bar
        // TODO: is there a more deterministic way to get these values
        final long x = 55;
        final long y = 55;
final int metaState = 0;
mMotionEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y,
metaState);







