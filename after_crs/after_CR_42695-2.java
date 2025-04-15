/*Allow sys-img.xml in user-defined addon sites

Change-Id:Id7eeaa46d24385ee231c8db7a542947b5ee960b6*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSources.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSources.java
//Synthetic comment -- index 40915f2..1902101 100755

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.repository.SdkSysImgConstants;
import com.android.utils.ILogger;

import java.io.File;
//Synthetic comment -- @@ -297,7 +298,7 @@
// make a "dynamic" source object that tries to guess its type once
// the URI has been fetched.
SdkSource s;
                            if (url.endsWith(SdkSysImgConstants.URL_DEFAULT_FILENAME)) {
s = new SdkSysImgSource(url, null/*uiName*/);
} else {
s = new SdkAddonSource(url, null/*uiName*/);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AddonSitesDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AddonSitesDialog.java
//Synthetic comment -- index 3bab049..badc3a7 100755

//Synthetic comment -- @@ -21,6 +21,8 @@
import com.android.sdklib.internal.repository.sources.SdkSourceCategory;
import com.android.sdklib.internal.repository.sources.SdkSourceProperties;
import com.android.sdklib.internal.repository.sources.SdkSources;
import com.android.sdklib.internal.repository.sources.SdkSysImgSource;
import com.android.sdklib.repository.SdkSysImgConstants;
import com.android.sdkuilib.internal.repository.UpdaterBaseDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.ui.GridDataBuilder;
//Synthetic comment -- @@ -387,7 +389,14 @@
}

// create the source, store it and update the list
                SdkSource newSource;
                // use url suffix to decide whether this is a SysImg or Addon;
                // see SdkSources.loadUserAddons() for another check like this 
                if (url.endsWith(SdkSysImgConstants.URL_DEFAULT_FILENAME)) {
                     newSource = new SdkSysImgSource(url, null/*uiName*/);
                } else {
                     newSource = new SdkAddonSource(url, null/*uiName*/);
                }
mSources.add(SdkSourceCategory.USER_ADDONS, newSource);
setReturnValue(true);
// notify sources change listeners. This will invoke our own loadUserUrlsList().







