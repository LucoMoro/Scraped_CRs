/*Add Bluetooth Feature Test

Bug 2908388

Change-Id:I08026f07b5d32b08e6a36fcd5bf3792be68171fc*/
//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/SystemFeaturesTest.java b/tests/tests/app/src/android/app/cts/SystemFeaturesTest.java
//Synthetic comment -- index 58575ee..b865c75 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
//Synthetic comment -- @@ -95,6 +96,14 @@
}
}

public void testCameraFeatures() {
Camera camera = null;
try {







