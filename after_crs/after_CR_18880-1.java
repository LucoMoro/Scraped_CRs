/*InputDevice: adjust orientation according to ro.sf.hwrotation

The touchscreen events are not rotated by ro.sf.hwrotation.
The patch fixes this issue.*/




//Synthetic comment -- diff --git a/services/java/com/android/server/InputDevice.java b/services/java/com/android/server/InputDevice.java
//Synthetic comment -- index 414b69f..8ac0e96 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import android.view.MotionEvent;
import android.view.Surface;
import android.view.WindowManagerPolicy;
import android.os.SystemProperties;

import java.io.PrintWriter;

//Synthetic comment -- @@ -43,6 +44,7 @@
/** Number of jumpy points to drop for touchscreens that need it. */
private static final int JUMPY_TRANSITION_DROPS = 3;
private static final int JUMPY_DROP_LIMIT = 3;
    private static final int HWROTATION = SystemProperties.getInt("ro.sf.hwrotation", 0) / 90;

final int id;
final int classes;
//Synthetic comment -- @@ -744,7 +746,7 @@
MotionEvent generateAbsMotion(InputDevice device, long curTime,
long curTimeNano, Display display, int orientation,
int metaState) {
            orientation = (orientation + HWROTATION) % 4;
if (mSkipLastPointers) {
mSkipLastPointers = false;
mLastNumPointers = 0;







