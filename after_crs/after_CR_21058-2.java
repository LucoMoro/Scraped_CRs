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
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
* Data shared between {@link UpdaterWindowImpl} and its pages.
//Synthetic comment -- @@ -383,13 +385,16 @@
* @param archives The archives to install. Incompatible ones will be skipped.
*/
@VisibleForTesting(visibility=Visibility.PRIVATE)
    protected void installArchives(final List<ArchiveInfo> archives) {
if (mTaskFactory == null) {
throw new IllegalArgumentException("Task Factory is null");
}

final boolean forceHttp = getSettingsController().getForceHttp();

        // sort all archives based on their dependency level.
        Collections.sort(archives, new InstallOrderComparator());

mTaskFactory.start("Installing Archives", new ITask() {
public void run(ITaskMonitor monitor) {

//Synthetic comment -- @@ -549,6 +554,53 @@
}

/**
     * A comparator to sort all the {@link ArchiveInfo} based on their
     * dependency level. This forces the installer to install first all packages
     * with no dependency, then those with one level of dependency, etc.
     */
    private static class InstallOrderComparator implements Comparator<ArchiveInfo> {

        private final Map<ArchiveInfo, Integer> mOrders = new HashMap<ArchiveInfo, Integer>();

        public int compare(ArchiveInfo o1, ArchiveInfo o2) {
            int n1 = getDependencyOrder(o1);
            int n2 = getDependencyOrder(o2);

            return n1 - n2;
        }

        private int getDependencyOrder(ArchiveInfo ai) {
            if (ai == null) {
                return 0;
            }

            // reuse cached value, if any
            Integer cached = mOrders.get(ai);
            if (cached != null) {
                return cached.intValue();
            }

            ArchiveInfo[] deps = ai.getDependsOn();
            if (deps == null) {
                return 0;
            }

            // compute dependencies, recursively
            int n = deps.length;

            for (ArchiveInfo dep : deps) {
                n += getDependencyOrder(dep);
            }

            // cache it
            mOrders.put(ai, Integer.valueOf(n));

            return n;
        }

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
import com.android.sdklib.internal.repository.Archive.Arch;
import com.android.sdklib.internal.repository.Archive.Os;
import com.android.sdklib.mock.MockLog;
//Synthetic comment -- @@ -103,7 +102,7 @@

m._installArchives(archives);
// TODO fix bug 14393: a2 is not installed because a1 has not been installed yet.
        assertEquals("[a1, a2]", Arrays.toString(m.getInstalled()));
}

// ---
//Synthetic comment -- @@ -120,8 +119,8 @@
setTaskFactory(new MockTaskFactory());
}

        /** Gives access to the internal {@link #installArchives(List)}. */
        public void _installArchives(List<ArchiveInfo> result) {
installArchives(result);
}








