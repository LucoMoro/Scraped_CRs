/*SdkMan2: fix Android platform name display.

Change-Id:Ifb280a90fba3a7ecd5eb41ef8b4dd152a480d12a*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index f25927a..53cbf73 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdkuilib.internal.repository;

import com.android.sdklib.internal.repository.Archive;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.ITask;
//Synthetic comment -- @@ -599,10 +600,28 @@

if (mapEntry == null) {
// This is a new category. Create it and add it to the map.
                // We need a label for the category. Use null right now and set it later.
PkgCategory cat = new PkgCategory(
apiKey,
                        null /*label*/,
imgFactory.getImageByName(ICON_CAT_PLATFORM));
mapEntry = Pair.of(cat, new HashSet<PkgItem>());
unusedItemsMap.put(apiKey, mapEntry);







