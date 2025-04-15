/*SDK Manager: Remove static package class map.

When using the manager in --no-ui mode, one can use
--filter to select a list of package types to install.
This list is based on the array in SdkRepository constants
and it used need a static map to the actual classes to do
the filtering. The static map is now computed at runtime
so that we don't risk forgetting to update it.

Change-Id:I7b091c36942d3b9358595f2194f2edc76c517245*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkRepository.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkRepository.java
//Synthetic comment -- index 723688c..0a4452a 100755

//Synthetic comment -- @@ -66,9 +66,6 @@
/**
* List of possible nodes in a repository XML. Used to populate options automatically
* in the no-GUI mode.
     * <p/>
     * **IMPORTANT**: if you edit this list, please also update the package-to-class map
     * com.android.sdkuilib.internal.repository.UpdaterData.updateOrInstallAll_NoGUI().
*/
public static final String[] NODES = {
NODE_PLATFORM,








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index af81977..dd98b00 100755

//Synthetic comment -- @@ -23,18 +23,13 @@
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.repository.AddonPackage;
import com.android.sdklib.internal.repository.Archive;
import com.android.sdklib.internal.repository.DocPackage;
import com.android.sdklib.internal.repository.ExtraPackage;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.LocalSdkParser;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.PlatformPackage;
import com.android.sdklib.internal.repository.PlatformToolPackage;
import com.android.sdklib.internal.repository.RepoSource;
import com.android.sdklib.internal.repository.RepoSources;
import com.android.sdklib.internal.repository.SamplePackage;
import com.android.sdklib.internal.repository.ToolPackage;
import com.android.sdklib.repository.SdkRepository;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
//Synthetic comment -- @@ -639,6 +634,7 @@
* @param dryMode True to check what would be updated/installed but do not actually
*   download or install anything.
*/
public void updateOrInstallAll_NoGUI(
Collection<String> pkgFilter,
boolean includeObsoletes,
//Synthetic comment -- @@ -664,19 +660,52 @@
// Map filter types to an SdkRepository Package type.
HashMap<String, Class<? extends Package>> pkgMap =
new HashMap<String, Class<? extends Package>>();
            pkgMap.put(SdkRepository.NODE_PLATFORM,         PlatformPackage.class);
            pkgMap.put(SdkRepository.NODE_ADD_ON,           AddonPackage.class);
            pkgMap.put(SdkRepository.NODE_TOOL,             ToolPackage.class);
            pkgMap.put(SdkRepository.NODE_PLATFORM_TOOL,    PlatformToolPackage.class);
            pkgMap.put(SdkRepository.NODE_DOC,              DocPackage.class);
            pkgMap.put(SdkRepository.NODE_SAMPLE,           SamplePackage.class);
            pkgMap.put(SdkRepository.NODE_EXTRA,            ExtraPackage.class);

if (SdkRepository.NODES.length != pkgMap.size()) {
                // Sanity check in case we forget to update this package map.
// We don't cancel the operation though.
                mSdkLog.error(null,
                    "Filter Mismatch!\nThe package filter list has changed. Please report this.");
}

// Now make a set of the types that are allowed by the filter.







