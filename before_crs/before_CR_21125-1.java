/*SDK Manager: Update All doesn't pick highest revisions

SDK Bug 14128

Change-Id:I4a9d1a10c99e32226eb0c2a4bc6bdddedfa26f66*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java
//Synthetic comment -- index 77eb981..58bf314 100755

//Synthetic comment -- @@ -170,12 +170,22 @@
return mPackages;
}

/**
* Clear the internal packages list. After this call, {@link #getPackages()} will return
* null till load() is called.
*/
public void clearPackages() {
        mPackages = null;
}

/**
//Synthetic comment -- @@ -740,10 +750,7 @@
}
}

            mPackages = packages.toArray(new Package[packages.size()]);

            // Order the packages.
            Arrays.sort(mPackages, null);

return true;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java
//Synthetic comment -- index 824a89f..42f92dd 100755

//Synthetic comment -- @@ -302,8 +302,25 @@
// Found a suitable update. Only accept the remote package
// if it provides at least one compatible archive

for (Archive a : remotePkg.getArchives()) {
if (a.isCompatible()) {
updates.add(a);
break;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java
//Synthetic comment -- index fb956f2..486a837 100755

//Synthetic comment -- @@ -21,13 +21,17 @@
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.repository.Archive;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.MockAddonPackage;
import com.android.sdklib.internal.repository.MockBrokenPackage;
import com.android.sdklib.internal.repository.MockPlatformPackage;
import com.android.sdklib.internal.repository.MockPlatformToolPackage;
import com.android.sdklib.internal.repository.MockToolPackage;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;

import org.eclipse.swt.widgets.Shell;
//Synthetic comment -- @@ -35,6 +39,7 @@
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import junit.framework.TestCase;

//Synthetic comment -- @@ -266,4 +271,58 @@
assertEquals(1, out.size());
assertSame(t2_archive, out.get(0).getNewArchive());
}
}







