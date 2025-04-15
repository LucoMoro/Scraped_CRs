/*Fix AvdManager to correctly find emulator in AOSP.

This is a temporary workaround on the lack of a main
"emulator" binary in AOSP, which will be fixed later.

Change-Id:I61f03ed8a54a410469f95a1100ef38eef7ab4bf6*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 038a59e..627bdb9 100644

//Synthetic comment -- @@ -308,12 +308,28 @@

// Start with base name of the emulator
path = path + SdkConstants.FN_EMULATOR;
// If not using ARM, add processor type to emulator command line
            if (!getAbiType().equalsIgnoreCase(SdkConstants.ABI_ARMEABI)) {
path = path + "-" + getAbiType();
}
// Add OS appropriate emulator extension (e.g., .exe on windows)
path = path + SdkConstants.FN_EMULATOR_EXTENSION;
return path;
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 4cf35ec..6309d5d 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.ISdkLog;
import com.android.sdklib.NullSdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.AvdManager.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager.AvdInfo.AvdStatus;







