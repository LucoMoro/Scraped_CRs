/*Use LinkedHashSet to obtain stable ordering of elements.

The hashCode() routine relies on the items being retrieved in a
stable sequence.

(cherry picked from commit b90199181261d60b33faf68ea80b60c167e3c396)

Change-Id:I62398af58b7af2b3f611abe4fe8cce1d22bb1432*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Device.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Device.java
//Synthetic comment -- index cb712f0..762597e 100644

//Synthetic comment -- @@ -275,11 +275,11 @@
/** A hash that's stable across JVM instances */
public int hashCode() {
int hash = 17;
        if (mName != null) {
            hash = 31 * hash + mName.hashCode();
}
        if (mManufacturer != null) {
            hash = 31 * hash + mManufacturer.hashCode();
}
hash = 31 * hash + mSoftware.hashCode();
hash = 31 * hash + mState.hashCode();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Meta.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Meta.java
//Synthetic comment -- index 4c19f3f..e69d2c3 100644

//Synthetic comment -- @@ -143,19 +143,16 @@
public int hashCode() {
int hash = 17;
if(mIconSixteen != null){
            String path = mIconSixteen.getAbsolutePath();
            hash = 31 * hash + path.hashCode();
}
if(mIconSixtyFour != null){
            String path = mIconSixtyFour.getAbsolutePath();
            hash = 31 * hash + path.hashCode();
}
if(mFrame != null){
            String path = mFrame.getAbsolutePath();
            hash = 31 * hash + path.hashCode();
}
if(mFrameOffsetLandscape != null){
hash = 31 * hash + mFrameOffsetLandscape.x;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Software.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Software.java
//Synthetic comment -- index 58f13b0..ac66a73 100644

//Synthetic comment -- @@ -17,16 +17,16 @@
package com.android.sdklib.devices;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class Software {
private int mMinSdkLevel = 0;
private int mMaxSdkLevel = Integer.MAX_VALUE;
private boolean mLiveWallpaperSupport;
    private Set<BluetoothProfile> mBluetoothProfiles = new LinkedHashSet<BluetoothProfile>();
private String mGlVersion;
    private Set<String> mGlExtensions = new LinkedHashSet<String>();
private boolean mStatusBar;

public int getMinSdkLevel() {
//Synthetic comment -- @@ -134,12 +134,12 @@
for (BluetoothProfile bp : mBluetoothProfiles) {
hash = 31 * hash + bp.ordinal();
}
        if (mGlVersion != null) {
            hash = 31 * hash + mGlVersion.hashCode();
}
for (String glExtension : mGlExtensions) {
            if (glExtension != null) {
                hash = 31 * hash + glExtension.hashCode();
}
}
hash = 31 * hash + (mStatusBar ? 1 : 0);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/State.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/State.java
//Synthetic comment -- index 27e5448..42c43e7 100644

//Synthetic comment -- @@ -125,11 +125,11 @@
public int hashCode() {
int hash = 17;
hash = 31 * hash + (mDefaultState ? 1 : 0);
        if (mName != null) {
            hash = 31 * hash + mName.hashCode();
}
        if (mDescription != null) {
            hash = 31 * hash + mDescription.hashCode();
}
hash = 31 * hash + mOrientation.ordinal();
hash = 31 * hash + mKeyState.ordinal();







