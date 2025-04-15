/*Merge remote branch 'korg/eclair' into eclairmerge

Conflicts:
	tests/AndroidManifest.xml
	tests/jni/Android.mk
	tests/res/layout/focus_finder_layout.xml
	tests/res/values/strings.xml
	tests/src/android/opengl/cts/OpenGlEsVersionStubActivity.java
	tests/src/android/os/cts/CpuFeatures.java
	tests/src/android/view/cts/FocusFinderStubActivity.java
	tests/tests/app/src/android/app/cts/SystemFeaturesTest.java
	tests/tests/os/src/android/os/cts/BuildTest.java
	tests/tests/permission/src/android/permission/cts/DebuggableTest.java
	tools/device-setup/TestDeviceSetup/src/android/tests/getinfo/DeviceInfoInstrument.java
	tools/host/src/com/android/cts/TestDevice.java
	tools/host/src/com/android/cts/TestSessionLog.java
	tools/host/src/com/android/cts/Version.java
	tools/host/src/res/cts_result.xsl

Change-Id:If7cd43b18be79a062d23d5536a3ff3a91c635f0b*/




//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/ViewTest.java b/tests/tests/view/src/android/view/cts/ViewTest.java
//Synthetic comment -- index 77624e8..5c730f8 100644

//Synthetic comment -- @@ -3946,6 +3946,7 @@
fitWindowsView.requestFocus();
}
});
        getInstrumentation().waitForIdleSync();
assertTrue(mockView.isFocusableInTouchMode());
assertFalse(fitWindowsView.isFocusableInTouchMode());
assertTrue(mockView.isFocusable());
//Synthetic comment -- @@ -3963,12 +3964,14 @@
mockView.requestFocus();
}
});
        getInstrumentation().waitForIdleSync();
assertTrue(mockView.isFocused());
runTestOnUiThread(new Runnable() {
public void run() {
fitWindowsView.requestFocus();
}
});
        getInstrumentation().waitForIdleSync();
assertFalse(fitWindowsView.isFocused());
assertTrue(mockView.isInTouchMode());
assertTrue(fitWindowsView.isInTouchMode());
//Synthetic comment -- @@ -3982,6 +3985,7 @@
fitWindowsView.requestFocus();
}
});
        getInstrumentation().waitForIdleSync();
assertFalse(mockView.isFocused());
assertTrue(fitWindowsView.isFocused());
assertFalse(mockView.isInTouchMode());







