/*Removed autosetting of SD card size

Change-Id:I960496610b3c43e16ef58c949a688241943a7153*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 6be646d..39a2710 100644

//Synthetic comment -- @@ -27,7 +27,6 @@
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.Hardware;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.AvdManager.AvdConflict;
//Synthetic comment -- @@ -930,31 +929,6 @@
}
}

// Set the screen resolution
mSkinListRadio.setSelection(false);
mSkinSizeRadio.setSelection(true);







