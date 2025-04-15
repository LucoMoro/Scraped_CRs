/*Allow sys-img.xml in user-defined addon sites

Change-Id:Id7eeaa46d24385ee231c8db7a542947b5ee960b6*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSource.java
//Synthetic comment -- index 2558e71..181ed4d 100755

//Synthetic comment -- @@ -344,7 +344,7 @@
url = url.replaceAll("https://", "http://");  //$NON-NLS-1$ //$NON-NLS-2$
}

        monitor.setDescription("Fetching URL: %1$s as a %2$s", url, getRootElementName());
monitor.incProgress(1);

mFetchError = null;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AddonSitesDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AddonSitesDialog.java
//Synthetic comment -- index 3bab049..bd06b03 100755

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.sdklib.internal.repository.sources.SdkSourceCategory;
import com.android.sdklib.internal.repository.sources.SdkSourceProperties;
import com.android.sdklib.internal.repository.sources.SdkSources;
import com.android.sdklib.internal.repository.sources.SdkSysImgSource;
import com.android.sdkuilib.internal.repository.UpdaterBaseDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.ui.GridDataBuilder;
//Synthetic comment -- @@ -387,7 +388,12 @@
}

// create the source, store it and update the list
                SdkSource newSource;
                if (url.endsWith("sys-img.xml")) {
                     newSource = new SdkSysImgSource(url, null/*uiName*/);
                } else {
                     newSource = new SdkAddonSource(url, null/*uiName*/);
                }
mSources.add(SdkSourceCategory.USER_ADDONS, newSource);
setReturnValue(true);
// notify sources change listeners. This will invoke our own loadUserUrlsList().







