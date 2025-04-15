/*Test Sensor Feature Reporting

Bug 2817004

Check that the sensor features reported by PackageManager have
sensors reported by SensorManager. Also check that sensor
features not reported by PackageManager don't have any sensors.

Change-Id:I7df2dfd055df51f5212ef0166cfeb85cdcc4ab6f*/
//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/SensorManagerTest.java b/tests/tests/hardware/src/android/hardware/cts/SensorManagerTest.java
//Synthetic comment -- index 87ed3085..73c32af 100644

//Synthetic comment -- @@ -15,9 +15,15 @@
*/
package android.hardware.cts;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
//Synthetic comment -- @@ -26,12 +32,13 @@
import android.test.AndroidTestCase;
import android.util.Log;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;
import dalvik.annotation.ToBeFixed;

@SuppressWarnings("deprecation")
@TestTargetClass(SensorManager.class)
//Synthetic comment -- @@ -304,4 +311,75 @@
assertTrue(Math.abs(orientation[2]) <= TWO_PI);
}
}
}







