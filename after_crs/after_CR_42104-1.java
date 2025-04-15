/*framework: system-property'fy orientation parameters

Some window orientation parameters need board specific
tweaking. Do this via system properties.

Change-Id:Id7c585ff3edbfc81086bdba4dea4279b2fe858e6Signed-off-by: Dragos Tatulea <dragos.tatulea@intel.com>
Signed-off-by: Andrew Boie <andrew.p.boie@intel.com>*/




//Synthetic comment -- diff --git a/core/java/android/view/WindowOrientationListener.java b/core/java/android/view/WindowOrientationListener.java
//Synthetic comment -- index 4c34dd4..3014da9 100755

//Synthetic comment -- @@ -25,6 +25,7 @@
import android.util.FloatMath;
import android.util.Log;
import android.util.Slog;
import android.os.SystemProperties;

/**
* A special helper class used by the WindowManager
//Synthetic comment -- @@ -320,10 +321,26 @@
// The ideal tilt angle is 0 (when the device is vertical) so the limits establish
// how close to vertical the device must be in order to change orientation.
private static final int[][] TILT_TOLERANCE = new int[][] {
            /* ROTATION_0   */
            {
                SystemProperties.getInt("ro.rotation.tilt.0.down", -25),
                SystemProperties.getInt("ro.rotation.tilt.0.up", 70)
            },
            /* ROTATION_90   */
            {
                SystemProperties.getInt("ro.rotation.tilt.90.down", -25),
                SystemProperties.getInt("ro.rotation.tilt.90.up", 65)
            },
            /* ROTATION_180   */
            {
                SystemProperties.getInt("ro.rotation.tilt.180.down", -25),
                SystemProperties.getInt("ro.rotation.tilt.180.up", 60)
            },
            /* ROTATION_270   */
            {
                SystemProperties.getInt("ro.rotation.tilt.270.down", -25),
                SystemProperties.getInt("ro.rotation.tilt.270.up", 65)
            }
};

// The gap angle in degrees between adjacent orientation angles for hysteresis.







