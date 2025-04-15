/*Signed-off-by: Woo-seok Jang <usok.jang@gmail.com>

added a ScreenConfiguration to SUPPORTED_SCREEN_CONFIGS
and fixed GridViewTest#testScroll to handle above changes

added a 1024x600, NORMAL screen layout size to SUPPORTED_SCREEN_CONFIGS
and enlarged testing instance of MockGridView's size to big enough to handle 1024-NORMAL layout

Change-Id:Iad6fe06506905b4bd27dced0912cc1f7ea41196d*/
//Synthetic comment -- diff --git a/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java b/tests/tests/dpi/src/android/dpi/cts/ConfigurationTest.java
//Synthetic comment -- index a014ac6..710c44f 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import android.view.Display;
import android.view.WindowManager;
import android.util.DisplayMetrics;

import java.lang.Integer;
import java.util.EnumSet;
//Synthetic comment -- @@ -239,6 +240,9 @@
assertFalse(Density.INVALID_LOW.equals(currentScreenConfig.getDensity()));
assertFalse(Density.INVALID_HIGH.equals(currentScreenConfig.getDensity()));

// Look up the ScreenConfig in the supported table and make
// sure we find a match.
for (ScreenConfiguration screenConfig: SUPPORTED_SCREEN_CONFIGS) {








//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/GridViewTest.java b/tests/tests/widget/src/android/widget/cts/GridViewTest.java
//Synthetic comment -- index 65045d8..74b8ed0 100644

//Synthetic comment -- @@ -52,6 +52,8 @@
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.ToBeFixed;

/**
* Test {@link GridView}.
*/
//Synthetic comment -- @@ -673,8 +675,13 @@
int oldExtent = mockGridView.computeVerticalScrollExtent();
int oldOffset = mockGridView.computeVerticalScrollOffset();

TouchUtils.scrollToBottom(this, mActivity, mockGridView);
        assertEquals(oldRange, mockGridView.computeVerticalScrollRange());
assertEquals(oldExtent, mockGridView.computeVerticalScrollExtent());
assertTrue(oldOffset < mockGridView.computeVerticalScrollOffset());
}
//Synthetic comment -- @@ -892,6 +899,12 @@
R.drawable.animated, R.drawable.black,
R.drawable.blue, R.drawable.failed,
R.drawable.pass, R.drawable.red,
};

private final DataSetObservable mDataSetObservable = new DataSetObservable();








//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/TestDevice.java b/tools/host/src/com/android/cts/TestDevice.java
//Synthetic comment -- index 987fd96..17b1370 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.ddmlib.Client;
import com.android.ddmlib.ClientData;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.IShellOutputReceiver;
import com.android.ddmlib.MultiLineReceiver;
//Synthetic comment -- @@ -143,6 +144,7 @@
mDeviceInfo = new DeviceParameterCollector();
mPackageActionTimer = new PackageActionTimer();
mObjectSync = new ObjectSync();
}

/**







