/*SDK Manager: fix install order of packages.

This makes sure that the installer install all
packages with less or no dependencies first.

SDK Bug 14393

Change-Id:If7b2fb5dc42fb425868fc3f39edb2ca26d190a1a*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 85fa0d6..2216a29 100755

//Synthetic comment -- @@ -56,10 +56,12 @@
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
* Data shared between {@link UpdaterWindowImpl} and its pages.
//Synthetic comment -- @@ -383,13 +385,16 @@
* @param archives The archives to install. Incompatible ones will be skipped.
*/
@VisibleForTesting(visibility=Visibility.PRIVATE)
    protected void installArchives(final Collection<ArchiveInfo> archives) {
if (mTaskFactory == null) {
throw new IllegalArgumentException("Task Factory is null");
}

final boolean forceHttp = getSettingsController().getForceHttp();

mTaskFactory.start("Installing Archives", new ITask() {
public void run(ITaskMonitor monitor) {

//Synthetic comment -- @@ -549,6 +554,53 @@
}

/**
* Attempts to restart ADB.
* <p/>
* If the "ask before restart" setting is set (the default), prompt the user whether








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterDataTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterDataTest.java
//Synthetic comment -- index cf0f912..185589f 100755

//Synthetic comment -- @@ -24,7 +24,6 @@
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.MockEmptySdkManager;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.Archive.Arch;
import com.android.sdklib.internal.repository.Archive.Os;
import com.android.sdklib.mock.MockLog;
//Synthetic comment -- @@ -103,7 +102,7 @@

m._installArchives(archives);
// TODO fix bug 14393: a2 is not installed because a1 has not been installed yet.
        assertEquals("[a1]", Arrays.toString(m.getInstalled()));
}

// ---
//Synthetic comment -- @@ -120,8 +119,8 @@
setTaskFactory(new MockTaskFactory());
}

        /** Gives access to the internal {@link #installArchives(Collection)}. */
        public void _installArchives(Collection<ArchiveInfo> result) {
installArchives(result);
}








