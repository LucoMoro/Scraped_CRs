/*Fix Broken LauncherActivityTest

Bug 3188260

Hit the down key a couple times before trying to select an item.

Change-Id:I8dc491730d3524f2e6a97e853093f9ed6c3398db*/




//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/LauncherActivityTest.java b/tests/tests/app/src/android/app/cts/LauncherActivityTest.java
//Synthetic comment -- index b8129e6..4e5e7d4 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package android.app.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -84,7 +83,6 @@
args = {int.class}
)
})
public void testLaunchActivity() {
// Constructor of LaunchActivity can't be invoked directly.
new LauncherActivityStub();
//Synthetic comment -- @@ -109,6 +107,9 @@
// There should be an activity(but with uncertain content) in position 0.
assertNotNull(mActivity.intentForPosition(0));
// Test onListItemClick
        sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
        sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
        sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
assertTrue(mActivity.isOnListItemClick);
}







