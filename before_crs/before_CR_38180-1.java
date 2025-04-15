/*Fixed bugs in ScreenOrientation and UiMode device parsing

Change-Id:Icf76ae07cd03a132e12c8c7a1e1c1872f1c8d564*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceParser.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceParser.java
//Synthetic comment -- index c2ecfc7..1b98abc 100644

//Synthetic comment -- @@ -242,6 +242,9 @@
UiMode d = UiMode.getEnum(s);
if (d != null) {
uimodes.add(d);
}
}
mHardware.mUiModes = uimodes;
//Synthetic comment -- @@ -304,7 +307,12 @@
} else if (DeviceSchema.NODE_DESCRIPTION.equals(localName)) {
mState.mDescription = getString(mStringAccumulator);
} else if (DeviceSchema.NODE_SCREEN_ORIENTATION.equals(localName)) {
                mState.mOrientation = ScreenOrientation.getEnum(getString(mStringAccumulator));
} else if (DeviceSchema.NODE_KEYBOARD_STATE.equals(localName)) {
mState.mKeyState = KeyboardState.getEnum(getString(mStringAccumulator));
} else if (DeviceSchema.NODE_NAV_STATE.equals(localName)) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/devices/DeviceParserTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/devices/DeviceParserTest.java
//Synthetic comment -- index 134a5a1..329dcf1 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import com.android.resources.KeyboardState;
import com.android.resources.Navigation;
import com.android.resources.NavigationState;
import com.android.resources.ScreenRatio;
import com.android.resources.ScreenSize;
import com.android.resources.TouchScreen;
//Synthetic comment -- @@ -128,11 +129,14 @@
assertEquals("Portrait", s.getName());
assertTrue(s.isDefaultState());
assertEquals("The phone in portrait view", s.getDescription());
assertEquals(KeyboardState.SOFT, s.getKeyState());
assertEquals(NavigationState.HIDDEN, s.getNavState());
s = device.getState("Landscape");
assertEquals("Landscape", s.getName());
assertFalse(s.isDefaultState());
}

public void testApiRange() throws Exception {







