/*SdkMan2: fix Android platform name display.

Change-Id:Ifb280a90fba3a7ecd5eb41ef8b4dd152a480d12a*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index f25927a..53cbf73 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdkuilib.internal.repository;

import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.internal.repository.Archive;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.ITask;
//Synthetic comment -- @@ -599,10 +600,28 @@

if (mapEntry == null) {
// This is a new category. Create it and add it to the map.

                // We need a label for the category.
                // If we have an API level, try to get the info from the SDK Manager.
                // If we don't (e.g. when installing a new platform that isn't yet available
                // locally in the SDK Manager), it's OK we'll try to find the first platform
                // package available.
                String label = null;
                if (apiKey != -1) {
                    for (IAndroidTarget target : mUpdaterData.getSdkManager().getTargets()) {
                        if (target.isPlatform() && target.getVersion().getApiLevel() == apiKey) {
                            label = target.getVersionName();
                            if (label != null) {
                                label = String.format("Android %1$s (API %2$d)", label, apiKey);
                                break;
                            }
                        }
                    }
                }

PkgCategory cat = new PkgCategory(
apiKey,
                        label,
imgFactory.getImageByName(ICON_CAT_PLATFORM));
mapEntry = Pair.of(cat, new HashSet<PkgItem>());
unusedItemsMap.put(apiKey, mapEntry);







