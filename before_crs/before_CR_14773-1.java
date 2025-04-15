/*Console-based "android update sdk"

SDK Bug 2404640

Change-Id:If0512558f048e88f2b216e34ed33aa550ff2610c*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index f0baa31..4d92467 100644

//Synthetic comment -- @@ -31,9 +31,11 @@
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectCreator.OutputLevel;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.xml.AndroidXPathFactory;
import com.android.sdkmanager.internal.repository.AboutPage;
import com.android.sdkmanager.internal.repository.SettingsPage;
import com.android.sdkuilib.internal.repository.LocalPackagesPage;
import com.android.sdkuilib.internal.widgets.MessageBoxLog;
import com.android.sdkuilib.repository.UpdaterWindow;
//Synthetic comment -- @@ -45,6 +47,8 @@
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//Synthetic comment -- @@ -206,8 +210,10 @@
// list action.
if (SdkCommandLine.OBJECT_TARGET.equals(directObject)) {
displayTargetList();
} else if (SdkCommandLine.OBJECT_AVD.equals(directObject)) {
displayAvdList();
} else {
displayTargetList();
displayAvdList();
//Synthetic comment -- @@ -216,24 +222,35 @@
} else if (SdkCommandLine.VERB_CREATE.equals(verb)) {
if (SdkCommandLine.OBJECT_AVD.equals(directObject)) {
createAvd();
} else if (SdkCommandLine.OBJECT_PROJECT.equals(directObject)) {
createProject(false /*library*/);
} else if (SdkCommandLine.OBJECT_TEST_PROJECT.equals(directObject)) {
createTestProject();
} else if (SdkCommandLine.OBJECT_LIB_PROJECT.equals(directObject)) {
createProject(true /*library*/);
}
} else if (SdkCommandLine.VERB_UPDATE.equals(verb)) {
if (SdkCommandLine.OBJECT_AVD.equals(directObject)) {
updateAvd();
} else if (SdkCommandLine.OBJECT_PROJECT.equals(directObject)) {
updateProject(false /*library*/);
} else if (SdkCommandLine.OBJECT_TEST_PROJECT.equals(directObject)) {
updateTestProject();
} else if (SdkCommandLine.OBJECT_LIB_PROJECT.equals(directObject)) {
updateProject(true /*library*/);
} else if (SdkCommandLine.OBJECT_SDK.equals(directObject)) {
                showMainWindow(true /*autoUpdate*/);
} else if (SdkCommandLine.OBJECT_ADB.equals(directObject)) {
updateAdb();
}
//Synthetic comment -- @@ -288,6 +305,47 @@
}

/**
* Creates a new Android project based on command-line parameters
*/
private void createProject(boolean library) {








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java b/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java
//Synthetic comment -- index 9b1425e..fb27758 100644

//Synthetic comment -- @@ -18,6 +18,9 @@

import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkManager;


/**
//Synthetic comment -- @@ -68,6 +71,10 @@
public static final String KEY_RENAME       = "rename";
public static final String KEY_SUBPROJECTS  = "subprojects";
public static final String KEY_MAIN_PROJECT = "main";

/**
* Action definitions for SdkManager command line.
//Synthetic comment -- @@ -175,6 +182,36 @@
VERB_UPDATE, OBJECT_AVD, "n", KEY_NAME,
"Name of the AVD to update", null);

// --- create project ---

/* Disabled for ADT 0.9 / Cupcake SDK 1.5_r1 release. [bug #1795718].
//Synthetic comment -- @@ -363,4 +400,32 @@
public String getParamTestProjectMain() {
return ((String) getValue(null, null, KEY_MAIN_PROJECT));
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Archive.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Archive.java
//Synthetic comment -- index 285262d..21cae7a 100755

//Synthetic comment -- @@ -42,7 +42,7 @@
* A package has some attributes (revision, description) and a list of archives
* which represent the downloadable bits.
* <p/>
 * Packages are contained in offered by a {@link RepoSource} (a download site).
*/
public class Archive implements IDescription {









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkRepository.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkRepository.java
//Synthetic comment -- index d59fc9e..49e2b2e 100755

//Synthetic comment -- @@ -61,6 +61,17 @@
/** An extra package. */
public static final String NODE_EXTRA    = "extra";                         //$NON-NLS-1$

/** The license definition. */
public static final String NODE_LICENSE       = "license";                  //$NON-NLS-1$
/** The optional uses-license for all packages or for a lib. */








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/LocalPackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/LocalPackagesPage.java
//Synthetic comment -- index c482042..f8a23bb 100755

//Synthetic comment -- @@ -256,7 +256,7 @@
}

private void onUpdateSelected() {
        mUpdaterData.updateOrInstallAll(null /*selectedArchives*/);
}

private void onDeleteSelected() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RemotePackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RemotePackagesPage.java
//Synthetic comment -- index b687f5c..defcd77 100755

//Synthetic comment -- @@ -320,7 +320,7 @@
}

if (mUpdaterData != null) {
            mUpdaterData.updateOrInstallAll(archives);
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java
//Synthetic comment -- index 87f0544..e2e503e 100755

//Synthetic comment -- @@ -122,7 +122,7 @@
/**
* Internal helper to set a boolean setting.
*/
    private void setSetting(String key, boolean value) {
mProperties.setProperty(key, Boolean.toString(value));
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateNoWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateNoWindow.java
new file mode 100755
//Synthetic comment -- index 0000000..be8f841

//Synthetic comment -- @@ -0,0 +1,338 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index c14be7e..a45896f 100755

//Synthetic comment -- @@ -23,14 +23,19 @@
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
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.repository.UpdaterWindow.ISdkListener;

//Synthetic comment -- @@ -42,7 +47,9 @@
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
* Data shared between {@link UpdaterWindowImpl} and its pages.
//Synthetic comment -- @@ -185,15 +192,22 @@
example = "~";                 //$NON-NLS-1$
}

            MessageDialog.openError(mWindowShell,
                "Android Virtual Devices Manager",
                String.format(
                    "The AVD manager normally uses the user's profile directory to store " +
                    "AVD files. However it failed to find the default profile directory. " +
                    "\n" +
                    "To fix this, please set the environment variable ANDROID_SDK_HOME to " +
                    "a valid path such as \"%s\".",
                    example));

return true;
}
//Synthetic comment -- @@ -222,17 +236,16 @@
mAvdManagerInitError = e;
}

        // notify adapters/parsers
        // TODO

// notify listeners.
notifyListeners(false /*init*/);
}

/**
* Reloads the SDK content (targets).
     * <p/> This also reloads the AVDs in case their status changed.
     * <p/>This does not notify the listeners ({@link ISdkListener}).
*/
public void reloadSdk() {
// reload SDK
//Synthetic comment -- @@ -257,7 +270,8 @@

/**
* Reloads the AVDs.
     * <p/>This does not notify the listeners.
*/
public void reloadAvds() {
// reload AVDs
//Synthetic comment -- @@ -271,6 +285,52 @@
}

/**
* Returns the list of installed packages, parsing them if this has not yet been done.
*/
public Package[] getInstalledPackage() {
//Synthetic comment -- @@ -288,7 +348,9 @@

/**
* Notify the listeners ({@link ISdkListener}) that the SDK was reloaded.
     * <p/>This can be called from any thread.
* @param init whether the SDK loaded for the first time.
*/
public void notifyListeners(final boolean init) {
//Synthetic comment -- @@ -458,22 +520,23 @@
}

/**
     * Attemps to restart ADB.
     *
* If the "ask before restart" setting is set (the default), prompt the user whether
* now is a good time to restart ADB.
* @param monitor
*/
private void askForAdbRestart(ITaskMonitor monitor) {
final boolean[] canRestart = new boolean[] { true };

        if (getSettingsController().getAskBeforeAdbRestart()) {
// need to ask for permission first
            Display display = mWindowShell.getDisplay();

display.syncExec(new Runnable() {
public void run() {
                    canRestart[0] = MessageDialog.openQuestion(mWindowShell,
"ADB Restart",
"A package that depends on ADB has been updated. It is recommended " +
"to restart ADB. Is it OK to do it now? If not, you can restart it " +
//Synthetic comment -- @@ -490,11 +553,16 @@
}

private void notifyToolsNeedsToBeRestarted() {
        Display display = mWindowShell.getDisplay();

display.syncExec(new Runnable() {
public void run() {
                MessageDialog.openInformation(mWindowShell,
"Android Tools Updated",
"The Android SDK and AVD Manager that you are currently using has been updated. " +
"It is recommended that you now close the manager window and re-open it. " +
//Synthetic comment -- @@ -507,6 +575,7 @@

/**
* Tries to update all the *existing* local packages.
* <p/>
* There are two modes of operation:
* <ul>
//Synthetic comment -- @@ -517,11 +586,11 @@
* the user wants to install or update, so just process these.
* </ul>
*
     * @param selectedArchives The list of remote archive to consider for the update.
*  This can be null, in which case a list of remote archive is fetched from all
*  available sources.
*/
    public void updateOrInstallAll(Collection<Archive> selectedArchives) {
if (selectedArchives == null) {
refreshSources(true);
}
//Synthetic comment -- @@ -536,7 +605,7 @@
ul.addNewPlatforms(archives, getSources(), getLocalSdkParser().getPackages());
}

        // TODO if selectedArchives is null and archives.len==0, find if there's
// any new platform we can suggest to install instead.

UpdateChooserDialog dialog = new UpdateChooserDialog(getWindowShell(), this, archives);
//Synthetic comment -- @@ -547,6 +616,140 @@
installArchives(result);
}
}
/**
* Refresh all sources. This is invoked either internally (reusing an existing monitor)
* or as a UI callback on the remote page "Refresh" button (in which case the monitor is
//Synthetic comment -- @@ -560,7 +763,7 @@

final boolean forceHttp = getSettingsController().getForceHttp();

        mTaskFactory.start("Refresh Sources",new ITask() {
public void run(ITaskMonitor monitor) {
RepoSource[] sources = mSources.getSources();
monitor.setProgressMax(sources.length);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java
//Synthetic comment -- index eda46e5..f7c9a96 100755

//Synthetic comment -- @@ -19,9 +19,6 @@

import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.RepoSource;
import com.android.sdklib.internal.repository.RepoSources;
import com.android.sdklib.repository.SdkRepository;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressTaskFactory;
import com.android.sdkuilib.repository.UpdaterWindow.ISdkListener;
//Synthetic comment -- @@ -295,7 +292,7 @@
mUpdaterData.notifyListeners(true /*init*/);

if (mRequestAutoUpdate) {
            mUpdaterData.updateOrInstallAll(null /*selectedArchives*/);
}

return true;
//Synthetic comment -- @@ -395,43 +392,7 @@
* Used to initialize the sources.
*/
private void setupSources() {
        RepoSources sources = mUpdaterData.getSources();
        sources.add(new RepoSource(SdkRepository.URL_GOOGLE_SDK_REPO_SITE, false /*userSource*/));

        // SDK_UPDATER_URLS is a semicolon-separated list of URLs that can be used to
        // seed the SDK Updater list for full repositories.
        String str = System.getenv("SDK_UPDATER_URLS");
        if (str != null) {
            String[] urls = str.split(";");
            for (String url : urls) {
                if (url != null && url.length() > 0) {
                    RepoSource s = new RepoSource(url, false /*userSource*/);
                    if (!sources.hasSource(s)) {
                        sources.add(s);
                    }
                }
            }
        }

        // Load user sources
        sources.loadUserSources(mUpdaterData.getSdkLog());

        // SDK_UPDATER_USER_URLS is a semicolon-separated list of URLs that can be used to
        // seed the SDK Updater list for user-only repositories. User sources can only provide
        // add-ons and extra packages.
        str = System.getenv("SDK_UPDATER_USER_URLS");
        if (str != null) {
            String[] urls = str.split(";");
            for (String url : urls) {
                if (url != null && url.length() > 0) {
                    RepoSource s = new RepoSource(url, true /*userSource*/);
                    if (!sources.hasSource(s)) {
                        sources.add(s);
                    }
                }
            }
        }

mRemotePackagesPage.onSdkChange(false /*init*/);
}








