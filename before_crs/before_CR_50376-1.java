/*Add locale sensitive int and double parsing methods

Use these to parse ints and doubles/floats from strings
rather than Integer.valueOf or Integer.parseInt (and ditto
for Float/Double) if the string represents a localized
string (e.g. using "," instead of "." in some locales,
and so on.)

Also fix some null warnings.

Change-Id:Ie89b8cddfda96ccaff597a79e8c7ae7aae4040fa*/
//Synthetic comment -- diff --git a/common/src/main/java/com/android/utils/SdkUtils.java b/common/src/main/java/com/android/utils/SdkUtils.java
//Synthetic comment -- index 160f95d..91aa3bd 100644

//Synthetic comment -- @@ -19,6 +19,9 @@
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;

/** Miscellaneous utilities used by the Android SDK tools */
public class SdkUtils {
/**
//Synthetic comment -- @@ -213,4 +216,71 @@
return sb.toString();
}

}








//Synthetic comment -- diff --git a/common/src/test/java/com/android/utils/SdkUtilsTest.java b/common/src/test/java/com/android/utils/SdkUtilsTest.java
//Synthetic comment -- index 030e1b7..c3f7fd8 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class SdkUtilsTest extends TestCase {
public void testEndsWithIgnoreCase() {
//Synthetic comment -- @@ -129,5 +131,70 @@
wrapped);
}


}








//Synthetic comment -- diff --git a/sdklib/src/main/java/com/android/sdklib/devices/DeviceParser.java b/sdklib/src/main/java/com/android/sdklib/devices/DeviceParser.java
//Synthetic comment -- index 1a663f1..511fed6 100644

//Synthetic comment -- @@ -193,7 +193,10 @@
mHardware.addCamera(mCamera);
mCamera = null;
} else if (DeviceSchema.NODE_LOCATION.equals(localName)) {
                mCamera.setLocation(CameraLocation.getEnum(getString(mStringAccumulator)));
} else if (DeviceSchema.NODE_AUTOFOCUS.equals(localName)) {
mCamera.setFlash(getBool(mStringAccumulator));
} else if (DeviceSchema.NODE_FLASH.equals(localName)) {
//Synthetic comment -- @@ -206,7 +209,10 @@
int val = getInteger(mStringAccumulator);
mHardware.setRam(new Storage(val, mUnit));
} else if (DeviceSchema.NODE_BUTTONS.equals(localName)) {
                mHardware.setButtonType(ButtonType.getEnum(getString(mStringAccumulator)));
} else if (DeviceSchema.NODE_INTERNAL_STORAGE.equals(localName)) {
for (String s : getStringList(mStringAccumulator)) {
int val = Integer.parseInt(s);
//Synthetic comment -- @@ -238,7 +244,10 @@
}
}
} else if (DeviceSchema.NODE_POWER_TYPE.equals(localName)) {
                mHardware.setChargeType(PowerType.getEnum(getString(mStringAccumulator)));
} else if (DeviceSchema.NODE_API_LEVEL.equals(localName)) {
String val = getString(mStringAccumulator);
// Can be one of 5 forms:







