/*InputDevice: adjust orientation according to ro.input.hwrotation

Change-Id:I8837de34c8d118f419fbd3b6028a6e7c35c08868*/
//Synthetic comment -- diff --git a/services/java/com/android/server/InputDevice.java b/services/java/com/android/server/InputDevice.java
//Synthetic comment -- index 414b69f..3b3072d 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import android.view.MotionEvent;
import android.view.Surface;
import android.view.WindowManagerPolicy;

import java.io.PrintWriter;

//Synthetic comment -- @@ -43,6 +44,7 @@
/** Number of jumpy points to drop for touchscreens that need it. */
private static final int JUMPY_TRANSITION_DROPS = 3;
private static final int JUMPY_DROP_LIMIT = 3;

final int id;
final int classes;
//Synthetic comment -- @@ -744,7 +746,7 @@
MotionEvent generateAbsMotion(InputDevice device, long curTime,
long curTimeNano, Display display, int orientation,
int metaState) {
            
if (mSkipLastPointers) {
mSkipLastPointers = false;
mLastNumPointers = 0;







