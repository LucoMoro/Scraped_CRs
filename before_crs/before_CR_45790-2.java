/*Use LinkedHashSet to obtain stable ordering of elements.

The hashCode() routine relies on the items being retrieved in a
stable sequence.

Change-Id:I9a3ffb79bfaa0eb52416f6b06343de7dc89193b2*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Software.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Software.java
//Synthetic comment -- index 58f13b0..6fae041 100644

//Synthetic comment -- @@ -17,16 +17,16 @@
package com.android.sdklib.devices;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Software {
private int mMinSdkLevel = 0;
private int mMaxSdkLevel = Integer.MAX_VALUE;
private boolean mLiveWallpaperSupport;
    private Set<BluetoothProfile> mBluetoothProfiles = new HashSet<BluetoothProfile>();
private String mGlVersion;
    private Set<String> mGlExtensions = new HashSet<String>();
private boolean mStatusBar;

public int getMinSdkLevel() {
//Synthetic comment -- @@ -134,12 +134,12 @@
for (BluetoothProfile bp : mBluetoothProfiles) {
hash = 31 * hash + bp.ordinal();
}
        for (Character c : mGlVersion.toCharArray()) {
            hash = 31 * hash + c;
}
for (String glExtension : mGlExtensions) {
            for (Character c : glExtension.toCharArray()) {
                hash = 31 * hash + c;
}
}
hash = 31 * hash + (mStatusBar ? 1 : 0);







