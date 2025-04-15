/*Added hack to reload AVDs on GLE open

Change-Id:Ia48861b743b7dbc19a679207245407b272d4332b*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index b4e052f..c8ea728 100644

//Synthetic comment -- @@ -55,6 +55,7 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.Density;
import com.android.resources.NightMode;
import com.android.resources.ResourceFolderType;
//Synthetic comment -- @@ -2587,10 +2588,14 @@
Sdk sdk = Sdk.getCurrent();
if (sdk != null) {
mDeviceList = sdk.getDevices();
} else {
mDeviceList = new ArrayList<Device>();
}

// fill with the devices
if (!mDeviceList.isEmpty()) {
Device first = mDeviceList.get(0);







