/*dded a ScreenConfiguration to SUPPORTED_SCREEN_CONFIGS
and fixed GridViewTest#testScroll to handle above changes

added a 1024x600, NORMAL screen layout size to SUPPORTED_SCREEN_CONFIGS
and enlarged testing instance of MockGridView's size to big enough to handle 1024-NORMAL layout

Change-Id:Ib6655b0685af1eba80ff7cc7f0edb1a841c79820*/
//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/GridViewTest.java b/tests/tests/widget/src/android/widget/cts/GridViewTest.java
//Synthetic comment -- index 74b8ed0..64e72ed 100644

//Synthetic comment -- @@ -52,8 +52,6 @@
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.ToBeFixed;

import android.util.Log;

/**
* Test {@link GridView}.
*/
//Synthetic comment -- @@ -675,11 +673,7 @@
int oldExtent = mockGridView.computeVerticalScrollExtent();
int oldOffset = mockGridView.computeVerticalScrollOffset();

		Log.d("testScroll", "old values are "+oldRange+","+oldExtent+","+oldOffset+" and MATCH_PARENT is "+ViewGroup.LayoutParams.MATCH_PARENT);

TouchUtils.scrollToBottom(this, mActivity, mockGridView);
		
		Log.d("testScroll", "and, comparities are "+mockGridView.computeVerticalScrollRange()+","+mockGridView.computeVerticalScrollExtent()+","+mockGridView.computeVerticalScrollOffset());

		assertEquals(oldRange, mockGridView.computeVerticalScrollRange());
assertEquals(oldExtent, mockGridView.computeVerticalScrollExtent());








//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/TestDevice.java b/tools/host/src/com/android/cts/TestDevice.java
//Synthetic comment -- index 17b1370..987fd96 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import com.android.ddmlib.Client;
import com.android.ddmlib.ClientData;
import com.android.ddmlib.DdmPreferences;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.IShellOutputReceiver;
import com.android.ddmlib.MultiLineReceiver;
//Synthetic comment -- @@ -144,7 +143,6 @@
mDeviceInfo = new DeviceParameterCollector();
mPackageActionTimer = new PackageActionTimer();
mObjectSync = new ObjectSync();
		DdmPreferences.setTimeOut(300000);
}

/**








//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/TestDevice.java.modified b/tools/host/src/com/android/cts/TestDevice.java.modified
new file mode 100644
//Synthetic comment -- index 0000000..17b1370

//Synthetic comment -- @@ -0,0 +1,1945 @@







