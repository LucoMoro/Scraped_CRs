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
*/
public static final String[] NODES = {
NODE_PLATFORM,








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index af81977..dd98b00 100755

//Synthetic comment -- @@ -23,18 +23,13 @@
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.repository.AddonPackage;
import com.android.sdklib.internal.repository.Archive;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.LocalSdkParser;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.RepoSource;
import com.android.sdklib.internal.repository.RepoSources;
import com.android.sdklib.internal.repository.ToolPackage;
import com.android.sdklib.repository.SdkRepository;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
//Synthetic comment -- @@ -639,6 +634,7 @@
* @param dryMode True to check what would be updated/installed but do not actually
*   download or install anything.
*/
    @SuppressWarnings("unchecked")
public void updateOrInstallAll_NoGUI(
Collection<String> pkgFilter,
boolean includeObsoletes,
//Synthetic comment -- @@ -664,19 +660,52 @@
// Map filter types to an SdkRepository Package type.
HashMap<String, Class<? extends Package>> pkgMap =
new HashMap<String, Class<? extends Package>>();

            // Automatically find the classes matching the node names
            ClassLoader classLoader = getClass().getClassLoader();
            String basePackage = Package.class.getPackage().getName();
            for (String node : SdkRepository.NODES) {
                // Capitalize the name
                String name = node.substring(0, 1).toUpperCase() + node.substring(1);

                // We can have one dash at most in a name. If it's present, we'll try
                // with the dash or with the next letter capitalized.
                int dash = name.indexOf('-');
                if (dash > 0) {
                    name = name.replaceFirst("-", "");
                }

                for (int alternatives = 0; alternatives < 2; alternatives++) {

                    String fqcn = basePackage + "." + name + "Package";  //$NON-NLS-1$ //$NON-NLS-2$
                    try {
                        Class<? extends Package> clazz =
                            (Class<? extends Package>) classLoader.loadClass(fqcn);
                        if (clazz != null) {
                            pkgMap.put(node, clazz);
                            continue;
                        }
                    } catch (Throwable ignore) {
                    }

                    if (alternatives == 0 && dash > 0) {
                        // Try an alternative where the next letter after the dash
                        // is converted to an upper case.
                        name = name.substring(0, dash) +
                               name.substring(dash, dash + 1).toUpperCase() +
                               name.substring(dash + 1);
                    } else {
                        break;
                    }
                }
            }

if (SdkRepository.NODES.length != pkgMap.size()) {
                // Sanity check in case we forget to update this node array.
// We don't cancel the operation though.
                mSdkLog.printf(
                    "*** Filter Mismatch! ***\n" +
                    "*** The package filter list has changed. Please report this.");
}

// Now make a set of the types that are allowed by the filter.







