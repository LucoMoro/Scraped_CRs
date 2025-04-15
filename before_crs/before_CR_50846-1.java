/*Refactor sdkuilib UpdaterData to isolate part that require SWT.

This splits UpdaterData into 2 classes:
- UpdaterData: main class that does not require SWT and that will
  eventually move to sdklib.
- SwtUpdaterData: a derived implementation that provides an SWT context.

Same split for the IUpdaterData interface and the PackageLoader.

Change-Id:Iab1a697d1ed5592259534ca7288c87e3bb7ee83c*/
//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/AboutDialog.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/AboutDialog.java
//Synthetic comment -- index 717890b..3684108 100755

//Synthetic comment -- @@ -39,9 +39,9 @@

public class AboutDialog extends UpdaterBaseDialog {

    public AboutDialog(Shell parentShell, UpdaterData updaterData) {
        super(parentShell, updaterData, "About" /*title*/);
        assert updaterData != null;
}

@Override
//Synthetic comment -- @@ -54,7 +54,7 @@
GridLayoutBuilder.create(shell).columns(3);

Label logo = new Label(shell, SWT.NONE);
        ImageFactory imgf = getUpdaterData() == null ? null : getUpdaterData().getImageFactory();
Image image = imgf == null ? null : imgf.getImageByName("sdkman_logo_128.png");
if (image != null) logo.setImage(image);

//Synthetic comment -- @@ -92,7 +92,7 @@
private String getRevision() {
Properties p = new Properties();
try{
            File sourceProp = FileOp.append(getUpdaterData().getOsSdkRoot(),
SdkConstants.FD_TOOLS,
SdkConstants.FN_SOURCE_PROP);
FileInputStream fis = null;








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ISwtUpdaterData.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ISwtUpdaterData.java
new file mode 100755
//Synthetic comment -- index 0000000..6f3cb02

//Synthetic comment -- @@ -0,0 +1,34 @@








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/IUpdaterData.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/IUpdaterData.java
//Synthetic comment -- index acc3990..52fdb1d 100755

//Synthetic comment -- @@ -20,11 +20,8 @@
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.utils.ILogger;

import org.eclipse.swt.widgets.Shell;


/**
* Interface used to retrieve some parameters from an {@link UpdaterData} instance.
//Synthetic comment -- @@ -38,14 +35,10 @@

public abstract DownloadCache getDownloadCache();

    public abstract ImageFactory getImageFactory();

public abstract SdkManager getSdkManager();

public abstract AvdManager getAvdManager();

public abstract SettingsController getSettingsController();

    public abstract Shell getWindowShell();

}








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/SdkUpdaterChooserDialog.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/SdkUpdaterChooserDialog.java
//Synthetic comment -- index 5aebc88..695f05f 100755

//Synthetic comment -- @@ -91,7 +91,7 @@
private Button mLicenseRadioReject;
private Button mLicenseRadioAcceptLicense;
private Group mPackageTextGroup;
    private final UpdaterData mUpdaterData;
private Group mTableGroup;
private Label mErrorLabel;

//Synthetic comment -- @@ -113,14 +113,14 @@
* Create the dialog.
*
* @param parentShell The shell to use, typically updaterData.getWindowShell()
     * @param updaterData The updater data
* @param archives The archives to be installed
*/
public SdkUpdaterChooserDialog(Shell parentShell,
            UpdaterData updaterData,
Collection<ArchiveInfo> archives) {
super(parentShell, 3, false/*makeColumnsEqual*/);
        mUpdaterData = updaterData;
mArchives = archives;
}

//Synthetic comment -- @@ -348,8 +348,8 @@
imageName = "android_icon_128.png";   //$NON-NLS-1$
}

        if (mUpdaterData != null) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();
if (imgFactory != null) {
getShell().setImage(imgFactory.getImageByName(imageName));
}
//Synthetic comment -- @@ -848,7 +848,7 @@
// Archive icon: accepted (green), rejected (red), not set yet (question mark)
ArchiveInfo ai = (ArchiveInfo) element;

                ImageFactory imgFactory = mUpdaterData.getImageFactory();
if (imgFactory != null) {
if (ai.isAccepted()) {
return imgFactory.getImageByName("accept_icon16.png");
//Synthetic comment -- @@ -862,7 +862,7 @@
} else if (element instanceof LicenseEntry) {
// License icon: green if all below are accepted, red if all rejected, otherwise
// no icon.
                ImageFactory imgFactory = mUpdaterData.getImageFactory();
if (imgFactory != null) {
boolean allAccepted = true;
boolean allRejected = true;








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/SettingsDialog.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/SettingsDialog.java
//Synthetic comment -- index f05be4c..822715a 100755

//Synthetic comment -- @@ -68,10 +68,10 @@
}
};

    public SettingsDialog(Shell parentShell, UpdaterData updaterData) {
        super(parentShell, updaterData, "Settings" /*title*/);
        assert updaterData != null;
        mSettingsController = updaterData.getSettingsController();
}

@Override








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/SwtUpdaterData.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/SwtUpdaterData.java
new file mode 100755
//Synthetic comment -- index 0000000..17282b7

//Synthetic comment -- @@ -0,0 +1,237 @@








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/UpdaterBaseDialog.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/UpdaterBaseDialog.java
//Synthetic comment -- index 02b39b4..8955667 100755

//Synthetic comment -- @@ -37,17 +37,17 @@
*/
public abstract class UpdaterBaseDialog extends SwtBaseDialog {

    private final UpdaterData mUpdaterData;

    protected UpdaterBaseDialog(Shell parentShell, UpdaterData updaterData, String title) {
super(parentShell,
SWT.APPLICATION_MODAL,
String.format("%1$s - %2$s", SdkUpdaterWindowImpl2.APP_NAME, title)); //$NON-NLS-1$
        mUpdaterData = updaterData;
}

    public UpdaterData getUpdaterData() {
        return mUpdaterData;
}

/**
//Synthetic comment -- @@ -96,8 +96,8 @@
imageName = "android_icon_128.png"; //$NON-NLS-1$
}

        if (mUpdaterData != null) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();
if (imgFactory != null) {
shell.setImage(imgFactory.getImageByName(imageName));
}








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 7fc1b1e..8dd200b 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdkuilib.internal.repository;

import com.android.SdkConstants;
import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.prefs.AndroidLocation.AndroidLocationException;
//Synthetic comment -- @@ -45,14 +46,10 @@
import com.android.sdklib.util.SparseIntArray;
import com.android.sdkuilib.internal.repository.SettingsController.OnChangedListener;
import com.android.sdkuilib.internal.repository.core.PackageLoader;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.repository.ui.SdkUpdaterWindowImpl2;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.utils.ILogger;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
//Synthetic comment -- @@ -87,7 +84,7 @@
private final ArrayList<ISdkChangeListener> mListeners = new ArrayList<ISdkChangeListener>();
private final ILogger mSdkLog;
private ITaskFactory mTaskFactory;
    private Shell mWindowShell;
private SdkManager mSdkManager;
private AvdManager mAvdManager;
/**
//Synthetic comment -- @@ -100,12 +97,6 @@
* Lazily created in {@link #getDownloadCache()}.
*/
private DownloadCache mDownloadCache;
    /**
     * The current {@link ImageFactory}.
     * Set via {@link #setImageFactory(ImageFactory)} by the window implementation.
     * It is null when invoked using the command-line interface.
     */
    private ImageFactory mImageFactory;
private AndroidLocationException mAvdManagerInitError;

/**
//Synthetic comment -- @@ -118,7 +109,6 @@
mOsSdkRoot = osSdkRoot;
mSdkLog = sdkLog;


mSettingsController = initSettingsController();
initSdk();
}
//Synthetic comment -- @@ -162,15 +152,6 @@
return mSdkLog;
}

    public void setImageFactory(ImageFactory imageFactory) {
        mImageFactory = imageFactory;
    }

    @Override
    public ImageFactory getImageFactory() {
        return mImageFactory;
    }

@Override
public SdkManager getSdkManager() {
return mSdkManager;
//Synthetic comment -- @@ -198,15 +179,6 @@
mListeners.remove(listener);
}

    public void setWindowShell(Shell windowShell) {
        mWindowShell = windowShell;
    }

    @Override
    public Shell getWindowShell() {
        return mWindowShell;
    }

public PackageLoader getPackageLoader() {
// The package loader is lazily initialized here.
if (mPackageLoader == null) {
//Synthetic comment -- @@ -238,23 +210,30 @@
"a valid path such as \"%s\".",
example);

            // We may not have any UI. Only display a dialog if there's a window shell available.
            if (mWindowShell != null && !mWindowShell.isDisposed()) {
                MessageDialog.openError(mWindowShell,
                    "Android Virtual Devices Manager",
                    error);
            } else {
                mSdkLog.error(null /* Throwable */, "%s", error);  //$NON-NLS-1$
            }

return true;
}
return false;
}

// -----

/**
* Initializes the {@link SdkManager} and the {@link AvdManager}.
* Extracted so that we can override this in unit tests.
*/
//Synthetic comment -- @@ -641,39 +620,17 @@
* <p/>
* If the "ask before restart" setting is set (the default), prompt the user whether
* now is a good time to restart ADB.
     *
     * @param monitor
*/
    private void askForAdbRestart(ITaskMonitor monitor) {
        final boolean[] canRestart = new boolean[] { true };

        if (getWindowShell() != null &&
                getSettingsController().getSettings().getAskBeforeAdbRestart()) {
            // need to ask for permission first
            final Shell shell = getWindowShell();
            if (shell != null && !shell.isDisposed()) {
                shell.getDisplay().syncExec(new Runnable() {
                    @Override
                    public void run() {
                        if (!shell.isDisposed()) {
                            canRestart[0] = MessageDialog.openQuestion(shell,
                                    "ADB Restart",
                                    "A package that depends on ADB has been updated. \n" +
                                    "Do you want to restart ADB now?");
                        }
                    }
                });
            }
        }

        if (canRestart[0]) {
AdbWrapper adb = new AdbWrapper(getOsSdkRoot(), monitor);
adb.stopAdb();
adb.startAdb();
}
}

    private void notifyToolsNeedsToBeRestarted(int flags) {

String msg = null;
if ((flags & TOOLS_MSG_UPDATED_FROM_ADT) != 0) {
//Synthetic comment -- @@ -690,84 +647,7 @@
"plug-in needs to be updated.";
}

        final String msg2 = msg;

        final Shell shell = getWindowShell();
        if (msg2 != null && shell != null && !shell.isDisposed()) {
            shell.getDisplay().syncExec(new Runnable() {
                @Override
                public void run() {
                    if (!shell.isDisposed()) {
                        MessageDialog.openInformation(shell,
                                "Android Tools Updated",
                                msg2);
                    }
                }
            });
        }
    }


    /**
     * Tries to update all the *existing* local packages.
     * This version *requires* to be run with a GUI.
     * <p/>
     * There are two modes of operation:
     * <ul>
     * <li>If selectedArchives is null, refreshes all sources, compares the available remote
     * packages with the current local ones and suggest updates to be done to the user (including
     * new platforms that the users doesn't have yet).
     * <li>If selectedArchives is not null, this represents a list of archives/packages that
     * the user wants to install or update, so just process these.
     * </ul>
     *
     * @param selectedArchives The list of remote archives to consider for the update.
     *  This can be null, in which case a list of remote archive is fetched from all
     *  available sources.
     * @param includeObsoletes True if obsolete packages should be used when resolving what
     *  to update.
     * @param flags Optional flags for the installer, such as {@link #NO_TOOLS_MSG}.
     * @return A list of archives that have been installed. Can be null if nothing was done.
     */
    public List<Archive> updateOrInstallAll_WithGUI(
            Collection<Archive> selectedArchives,
            boolean includeObsoletes,
            int flags) {

        // Note: we no longer call refreshSources(true) here. This will be done
        // automatically by computeUpdates() iif it needs to access sources to
        // resolve missing dependencies.

        SdkUpdaterLogic ul = new SdkUpdaterLogic(this);
        List<ArchiveInfo> archives = ul.computeUpdates(
                selectedArchives,
                getSources(),
                getLocalSdkParser().getPackages(),
                includeObsoletes);

        if (selectedArchives == null) {
            getPackageLoader().loadRemoteAddonsList(new NullTaskMonitor(getSdkLog()));
            ul.addNewPlatforms(
                    archives,
                    getSources(),
                    getLocalSdkParser().getPackages(),
                    includeObsoletes);
        }

        // TODO if selectedArchives is null and archives.len==0, find if there are
        // any new platform we can suggest to install instead.

        Collections.sort(archives);

        SdkUpdaterChooserDialog dialog =
            new SdkUpdaterChooserDialog(getWindowShell(), this, archives);
        dialog.open();

        ArrayList<ArchiveInfo> result = dialog.getResult();
        if (result != null && result.size() > 0) {
            return installArchives(result, flags);
        }
        return null;
}

/**
//Synthetic comment -- @@ -850,6 +730,58 @@

/**
* Tries to update all the *existing* local packages.
* This version is intended to run without a GUI and
* only outputs to the current {@link ILogger}.
*
//Synthetic comment -- @@ -1068,8 +1000,8 @@
* This can be called from any thread.
*/
public void broadcastOnSdkLoaded() {
        if (mWindowShell != null && !mWindowShell.isDisposed() && mListeners.size() > 0) {
            mWindowShell.getDisplay().syncExec(new Runnable() {
@Override
public void run() {
for (ISdkChangeListener listener : mListeners) {
//Synthetic comment -- @@ -1089,8 +1021,8 @@
* This can be called from any thread.
*/
private void broadcastOnSdkReload() {
        if (mWindowShell != null && !mWindowShell.isDisposed() && mListeners.size() > 0) {
            mWindowShell.getDisplay().syncExec(new Runnable() {
@Override
public void run() {
for (ISdkChangeListener listener : mListeners) {
//Synthetic comment -- @@ -1110,8 +1042,8 @@
* This can be called from any thread.
*/
private void broadcastPreInstallHook() {
        if (mWindowShell != null && !mWindowShell.isDisposed() && mListeners.size() > 0) {
            mWindowShell.getDisplay().syncExec(new Runnable() {
@Override
public void run() {
for (ISdkChangeListener listener : mListeners) {
//Synthetic comment -- @@ -1131,8 +1063,8 @@
* This can be called from any thread.
*/
private void broadcastPostInstallHook() {
        if (mWindowShell != null && !mWindowShell.isDisposed() && mListeners.size() > 0) {
            mWindowShell.getDisplay().syncExec(new Runnable() {
@Override
public void run() {
for (ISdkChangeListener listener : mListeners) {
//Synthetic comment -- @@ -1155,5 +1087,4 @@
protected ArchiveInstaller createArchiveInstaler() {
return new ArchiveInstaller();
}

}








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/PackageLoader.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/PackageLoader.java
//Synthetic comment -- index 18cccae..b659edc 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdkuilib.internal.repository.core;

import com.android.sdklib.internal.repository.AddonsListFetcher;
import com.android.sdklib.internal.repository.AddonsListFetcher.Site;
import com.android.sdklib.internal.repository.DownloadCache;
//Synthetic comment -- @@ -32,11 +33,9 @@
import com.android.sdklib.internal.repository.sources.SdkSysImgSource;
import com.android.sdklib.repository.SdkAddonsListConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdkuilib.internal.repository.UpdaterData;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
//Synthetic comment -- @@ -54,7 +53,7 @@

/**
* The {@link DownloadCache} override. Can be null, in which case the one from
     * {@link UpdaterData} is used instead.
* @see #getDownloadCache()
*/
private final DownloadCache mOverrideCache;
//Synthetic comment -- @@ -91,7 +90,7 @@
* <p/>
* <em>Important</em>: This method is called from a sub-thread, so clients which
* try to access any UI widgets must wrap their calls into
         * {@link Display#syncExec(Runnable)} or {@link Display#asyncExec(Runnable)}.
*
* @param packages All the packages loaded from the source. Never null.
* @return True if the load operation should continue, false if it should stop.
//Synthetic comment -- @@ -130,8 +129,8 @@
* The method should return true if this is a package that should be installed.
* <p/>
* <em>Important</em>: This method is called from a sub-thread, so clients who try
         * to access any UI widgets must wrap their calls into {@link Display#syncExec(Runnable)}
         * or {@link Display#asyncExec(Runnable)}.
*/
public boolean acceptPackage(Package pkg);

//Synthetic comment -- @@ -141,8 +140,8 @@
* be called with a 'true' success and the actual install paths.
* <p/>
* <em>Important</em>: This method is called from a sub-thread, so clients who try
         * to access any UI widgets must wrap their calls into {@link Display#syncExec(Runnable)}
         * or {@link Display#asyncExec(Runnable)}.
*/
public void setResult(boolean success, Map<Package, File> installPaths);

//Synthetic comment -- @@ -153,10 +152,10 @@
}

/**
     * Creates a new PackageManager associated with the given {@link UpdaterData}
     * and using the {@link UpdaterData}'s default {@link DownloadCache}.
*
     * @param updaterData The {@link UpdaterData}. Must not be null.
*/
public PackageLoader(UpdaterData updaterData) {
mUpdaterData = updaterData;
//Synthetic comment -- @@ -164,18 +163,32 @@
}

/**
     * Creates a new PackageManager associated with the given {@link UpdaterData}
* but using the specified {@link DownloadCache} instead of the one from
     * {@link UpdaterData}.
*
     * @param updaterData The {@link UpdaterData}. Must not be null.
     * @param cache The {@link DownloadCache} to use instead of the one from {@link UpdaterData}.
*/
public PackageLoader(UpdaterData updaterData, DownloadCache cache) {
mUpdaterData = updaterData;
mOverrideCache = cache;
}

/**
* Loads all packages from the remote repository.
* This runs in an {@link ITask}. The call is blocking.
//Synthetic comment -- @@ -276,15 +289,11 @@
* result of the accepted package.
* When the task is completed, {@link IAutoInstallTask#taskCompleted()} is called.
* <p/>
     * <em>Important</em>: Since some UI will be displayed to install the selected package,
     * the {@link UpdaterData} must have a window {@link Shell} associated using
     * {@link UpdaterData#setWindowShell(Shell)}.
     * <p/>
* The call is blocking. Although the name says "Task", this is not an {@link ITask}
* running in its own thread but merely a synchronous call.
*
* @param installFlags Flags for installation such as
     *  {@link UpdaterData#TOOLS_MSG_UPDATED_FROM_ADT}.
* @param installTask The task to perform.
*/
public void loadPackagesWithInstallTask(
//Synthetic comment -- @@ -361,23 +370,20 @@

final List<Archive> installedArchives = new ArrayList<Archive>();

                Shell shell = mUpdaterData.getWindowShell();
                if (shell != null && !shell.isDisposed()) {
                    shell.getDisplay().syncExec(new Runnable() {;
                        @Override
                        public void run() {
                            List<Archive> archives =
                                mUpdaterData.updateOrInstallAll_WithGUI(
                                    archivesToInstall,
                                    true /* includeObsoletes */,
                                    installFlags);

                            if (archives != null) {
                                installedArchives.addAll(archives);
                            }
}
                    });
                }

if (installedArchives.isEmpty()) {
// We failed to install anything.
//Synthetic comment -- @@ -485,7 +491,7 @@
* Returns the {@link DownloadCache} to use.
*
* @return Returns {@link #mOverrideCache} if not null; otherwise returns the
     *  one from {@link UpdaterData} is used instead.
*/
private DownloadCache getDownloadCache() {
return mOverrideCache != null ? mOverrideCache : mUpdaterData.getDownloadCache();








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/PackagesDiffLogic.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/PackagesDiffLogic.java
//Synthetic comment -- index a95c8ac..9809a4c 100755

//Synthetic comment -- @@ -30,7 +30,7 @@
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.util.SparseArray;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.core.PkgItem.PkgState;
import com.android.sdkuilib.internal.repository.ui.PackagesPageIcons;

//Synthetic comment -- @@ -49,11 +49,11 @@
* so that we can test it using head-less unit tests.
*/
public class PackagesDiffLogic {
    private final UpdaterData mUpdaterData;
private boolean mFirstLoadComplete = true;

    public PackagesDiffLogic(UpdaterData updaterData) {
        mUpdaterData = updaterData;
}

/**








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/PkgCategorySource.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/PkgCategorySource.java
//Synthetic comment -- index b73288b..e3968b6 100755

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.sdklib.internal.repository.sources.SdkRepoSource;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.ui.PackagesPageIcons;


//Synthetic comment -- @@ -37,12 +37,13 @@
* This uses {@link SdkSource#toString()} to get the source's description.
* Note that if the name of the source isn't known, the description will use its URL.
*/
    public PkgCategorySource(SdkSource source, UpdaterData updaterData) {
super(
source, // the source is the key and it can be null
source == UNKNOWN_SOURCE ? "Local Packages" : source.toString(),
source == UNKNOWN_SOURCE ?
                updaterData.getImageFactory().getImageByName(PackagesPageIcons.ICON_PKG_INSTALLED) :
source);
mSource = source;
}








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/SwtPackageLoader.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/SwtPackageLoader.java
new file mode 100755
//Synthetic comment -- index 0000000..785df01

//Synthetic comment -- @@ -0,0 +1,68 @@








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/AddonSitesDialog.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/AddonSitesDialog.java
//Synthetic comment -- index 77f82b1..b28cccd 100755

//Synthetic comment -- @@ -24,7 +24,7 @@
import com.android.sdklib.internal.repository.sources.SdkSysImgSource;
import com.android.sdklib.repository.SdkSysImgConstants;
import com.android.sdkuilib.internal.repository.UpdaterBaseDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;

//Synthetic comment -- @@ -91,7 +91,7 @@
* @param parent The parent's shell
* @wbp.parser.entryPoint
*/
    public AddonSitesDialog(Shell parent, UpdaterData updaterData) {
super(parent, updaterData, "Add-on Sites");
mSources = updaterData.getSources();
assert mSources != null;








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/AdtUpdateDialog.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/AdtUpdateDialog.java
//Synthetic comment -- index e90c3d9..932fa90 100755

//Synthetic comment -- @@ -25,7 +25,7 @@
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.core.PackageLoader;
import com.android.sdkuilib.internal.repository.core.SdkLogAdapter;
import com.android.sdkuilib.internal.repository.core.PackageLoader.IAutoInstallTask;
//Synthetic comment -- @@ -72,7 +72,7 @@
public static final int USE_MAX_REMOTE_API_LEVEL = 0;

private static final String APP_NAME = "Android SDK Manager";
    private final UpdaterData mUpdaterData;

private Boolean mResultCode = Boolean.FALSE;
private Map<Package, File> mResultPaths = null;
//Synthetic comment -- @@ -97,7 +97,7 @@
ILogger sdkLog,
String osSdkRoot) {
super(parentShell, SWT.NONE, APP_NAME);
        mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);
}

/**
//Synthetic comment -- @@ -362,7 +362,7 @@

@Override
int installFlags() {
                return UpdaterData.TOOLS_MSG_UPDATED_FROM_ADT;
}
};
}
//Synthetic comment -- @@ -381,7 +381,7 @@

@Override
int installFlags() {
                return UpdaterData.TOOLS_MSG_UPDATED_FROM_ADT;
}
};
}
//Synthetic comment -- @@ -420,7 +420,7 @@

@Override
int installFlags() {
                return UpdaterData.TOOLS_MSG_UPDATED_FROM_ADT;
}
};
}
//Synthetic comment -- @@ -479,7 +479,7 @@

@Override
int installFlags() {
                return UpdaterData.NO_TOOLS_MSG;
}
};
}








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/AvdManagerPage.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/AvdManagerPage.java
//Synthetic comment -- index a092550..a18cbbd 100755

//Synthetic comment -- @@ -21,7 +21,7 @@
import com.android.sdklib.devices.DeviceManager.DevicesChangedListener;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.widgets.AvdSelector;
import com.android.sdkuilib.internal.widgets.AvdSelector.DisplayMode;
import com.android.sdkuilib.repository.ISdkChangeListener;
//Synthetic comment -- @@ -48,21 +48,21 @@

private AvdSelector mAvdSelector;

    private final UpdaterData mUpdaterData;
private final DeviceManager mDeviceManager;
/**
* Create the composite.
* @param parent The parent of the composite.
     * @param updaterData An instance of {@link UpdaterData}.
*/
public AvdManagerPage(Composite parent,
int swtStyle,
            UpdaterData updaterData,
DeviceManager deviceManager) {
super(parent, swtStyle);

        mUpdaterData = updaterData;
        mUpdaterData.addListeners(this);

mDeviceManager = deviceManager;
mDeviceManager.registerListener(this);
//Synthetic comment -- @@ -78,10 +78,10 @@
label.setLayoutData(new GridData());

try {
            if (mUpdaterData != null && mUpdaterData.getAvdManager() != null) {
label.setText(String.format(
"List of existing Android Virtual Devices located at %s",
                        mUpdaterData.getAvdManager().getBaseAvdFolder()));
} else {
label.setText("Error: cannot find the AVD folder location.\r\n Please set the 'ANDROID_SDK_HOME' env variable.");
}
//Synthetic comment -- @@ -90,11 +90,11 @@
}

mAvdSelector = new AvdSelector(parent,
                mUpdaterData.getOsSdkRoot(),
                mUpdaterData.getAvdManager(),
DisplayMode.MANAGER,
                mUpdaterData.getSdkLog());
        mAvdSelector.setSettingsController(mUpdaterData.getSettingsController());
}

@Override
//Synthetic comment -- @@ -104,7 +104,7 @@

@Override
public void dispose() {
        mUpdaterData.removeListener(this);
mDeviceManager.unregisterListener(this);
super.dispose();
}
//Synthetic comment -- @@ -120,7 +120,7 @@

// Reloading the AVDs created new objects, so the reference to avdInfo
// will never be selected. Instead reselect it based on its unique name.
            AvdManager am = mUpdaterData.getAvdManager();
avdInfo = am.getAvd(avdInfo.getName(), false /*validAvdOnly*/);
}
mAvdSelector.setSelection(avdInfo);








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/AvdManagerWindowImpl1.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/AvdManagerWindowImpl1.java
//Synthetic comment -- index e3efca6..dd869a0 100755

//Synthetic comment -- @@ -25,7 +25,7 @@
import com.android.sdkuilib.internal.repository.MenuBarWrapper;
import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.internal.repository.SettingsDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.repository.ui.DeviceManagerPage.IAvdCreatedListener;
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;
//Synthetic comment -- @@ -63,7 +63,7 @@
private final Shell mParentShell;
private final AvdInvocationContext mContext;
/** Internal data shared between the window and its pages. */
    private final UpdaterData mUpdaterData;
/** True if this window created the UpdaterData, in which case it needs to dispose it. */
private final boolean mOwnUpdaterData;
private final DeviceManager mDeviceManager;
//Synthetic comment -- @@ -92,7 +92,7 @@
AvdInvocationContext context) {
mParentShell = parentShell;
mContext = context;
        mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);
mOwnUpdaterData = true;
mDeviceManager = DeviceManager.createInstance(osSdkRoot, sdkLog);
}
//Synthetic comment -- @@ -101,23 +101,23 @@
* Creates a new window. Caller must call open(), which will block.
* <p/>
* This is to be used when the window is opened from {@link SdkUpdaterWindowImpl2}
     * to share the same {@link UpdaterData} structure.
*
* @param parentShell Parent shell.
     * @param updaterData The parent's updater data.
* @param context The {@link AvdInvocationContext} to change the behavior depending on who's
*  opening the SDK Manager.
*/
public AvdManagerWindowImpl1(
Shell parentShell,
            UpdaterData updaterData,
AvdInvocationContext context) {
mParentShell = parentShell;
mContext = context;
        mUpdaterData = updaterData;
mOwnUpdaterData = false;
        mDeviceManager = DeviceManager.createInstance(mUpdaterData.getOsSdkRoot(),
                                                      mUpdaterData.getSdkLog());
}

/**
//Synthetic comment -- @@ -199,7 +199,7 @@
avdTabItem.setControl(root);
GridLayoutBuilder.create(root).columns(1);

        mAvdPage = new AvdManagerPage(root, SWT.NONE, mUpdaterData, mDeviceManager);
GridDataBuilder.create(mAvdPage).fill().grab();
}

//Synthetic comment -- @@ -209,7 +209,7 @@
GridLayoutBuilder.create(root).columns(1);

DeviceManagerPage devicePage =
            new DeviceManagerPage(root, SWT.NONE, mUpdaterData, mDeviceManager);
GridDataBuilder.create(devicePage).fill().grab();

devicePage.setAvdCreatedListener(new IAvdCreatedListener() {
//Synthetic comment -- @@ -255,25 +255,25 @@
new MenuBarWrapper(APP_NAME_MAC_MENU, menuTools) {
@Override
public void onPreferencesMenuSelected() {
                        SettingsDialog sd = new SettingsDialog(mShell, mUpdaterData);
sd.open();
}

@Override
public void onAboutMenuSelected() {
                        AboutDialog ad = new AboutDialog(mShell, mUpdaterData);
ad.open();
}

@Override
public void printError(String format, Object... args) {
                        if (mUpdaterData != null) {
                            mUpdaterData.getSdkLog().error(null, format, args);
}
}
};
} catch (Throwable e) {
                mUpdaterData.getSdkLog().error(e, "Failed to setup menu bar");
e.printStackTrace();
}
}
//Synthetic comment -- @@ -290,7 +290,7 @@
* Adds a new listener to be notified when a change is made to the content of the SDK.
*/
public void addListener(ISdkChangeListener listener) {
        mUpdaterData.addListeners(listener);
}

/**
//Synthetic comment -- @@ -298,7 +298,7 @@
* the SDK.
*/
public void removeListener(ISdkChangeListener listener) {
        mUpdaterData.removeListener(listener);
}

// --- Internals & UI Callbacks -----------
//Synthetic comment -- @@ -307,9 +307,9 @@
* Called before the UI is created.
*/
private void preCreateContent() {
        mUpdaterData.setWindowShell(mShell);
// We need the UI factory to create the UI
        mUpdaterData.setImageFactory(new ImageFactory(mShell.getDisplay()));
// Note: we can't create the TaskFactory yet because we need the UI
// to be created first, so this is done in postCreateContent().
}
//Synthetic comment -- @@ -326,11 +326,11 @@
setupSources();
initializeSettings();

        if (mUpdaterData.checkIfInitFailed()) {
return false;
}

        mUpdaterData.broadcastOnSdkLoaded();

return true;
}
//Synthetic comment -- @@ -346,8 +346,8 @@
imageName = "android_icon_128.png";
}

        if (mUpdaterData != null) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();
if (imgFactory != null) {
shell.setImage(imgFactory.getImageByName(imageName));
}
//Synthetic comment -- @@ -358,15 +358,15 @@
* Called by the main loop when the window has been disposed.
*/
private void dispose() {
        mUpdaterData.getSources().saveUserAddons(mUpdaterData.getSdkLog());
}

/**
* Callback called when the window shell is disposed.
*/
private void onAndroidSdkUpdaterDispose() {
        if (mOwnUpdaterData && mUpdaterData != null) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();
if (imgFactory != null) {
imgFactory.dispose();
}
//Synthetic comment -- @@ -377,7 +377,7 @@
* Used to initialize the sources.
*/
private void setupSources() {
        mUpdaterData.setupDefaultSources();
}

/**
//Synthetic comment -- @@ -387,25 +387,25 @@
* and use it to load and apply these settings.
*/
private void initializeSettings() {
        mSettingsController = mUpdaterData.getSettingsController();
mSettingsController.loadSettings();
mSettingsController.applySettings();
}

private void onSdkManager() {
        ITaskFactory oldFactory = mUpdaterData.getTaskFactory();

try {
SdkUpdaterWindowImpl2 win = new SdkUpdaterWindowImpl2(
mShell,
                    mUpdaterData,
SdkUpdaterWindow.SdkInvocationContext.AVD_MANAGER);

win.open();
} catch (Exception e) {
            mUpdaterData.getSdkLog().error(e, "SDK Manager window error");
} finally {
            mUpdaterData.setTaskFactory(oldFactory);
}
}
}








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java
//Synthetic comment -- index 7b63d39..5093991 100755

//Synthetic comment -- @@ -24,7 +24,7 @@
import com.android.sdklib.devices.Storage;
import com.android.sdklib.devices.Storage.Unit;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.widgets.AvdCreationDialog;
import com.android.sdkuilib.internal.widgets.AvdSelector;
//Synthetic comment -- @@ -94,7 +94,7 @@
public void onAvdCreated(AvdInfo createdAvdInfo);
}

    private final UpdaterData mUpdaterData;
private final DeviceManager mDeviceManager;
private Table mTable;
private Button mNewButton;
//Synthetic comment -- @@ -113,16 +113,16 @@
/**
* Create the composite.
* @param parent The parent of the composite.
     * @param updaterData An instance of {@link UpdaterData}.
*/
public DeviceManagerPage(Composite parent,
int swtStyle,
            UpdaterData updaterData,
DeviceManager deviceManager) {
super(parent, swtStyle);

        mUpdaterData = updaterData;
        mUpdaterData.addListeners(this);

mDeviceManager = deviceManager;
mDeviceManager.registerListener(this);
//Synthetic comment -- @@ -301,7 +301,7 @@

@Override
public void dispose() {
        mUpdaterData.removeListener(this);
mDeviceManager.unregisterListener(this);
super.dispose();
}
//Synthetic comment -- @@ -488,7 +488,7 @@

// Generate a list of the AVD names using these devices
Map<Device, List<String>> device2avdMap = new HashMap<Device, List<String>>();
        for (AvdInfo avd : mUpdaterData.getAvdManager().getAllAvds()) {
String n = avd.getDeviceName();
String m = avd.getDeviceManufacturer();
if (n == null || m == null || n.isEmpty() || m.isEmpty()) {
//Synthetic comment -- @@ -653,7 +653,7 @@
DeviceCreationDialog dlg = new DeviceCreationDialog(
getShell(),
mDeviceManager,
                mUpdaterData.getImageFactory(),
null /*device*/);
if (dlg.open() == Window.OK) {
onRefresh();
//Synthetic comment -- @@ -673,7 +673,7 @@
DeviceCreationDialog dlg = new DeviceCreationDialog(
getShell(),
mDeviceManager,
                mUpdaterData.getImageFactory(),
ci.mDevice);
if (dlg.open() == Window.OK) {
onRefresh();
//Synthetic comment -- @@ -719,9 +719,9 @@
}

final AvdCreationDialog dlg = new AvdCreationDialog(mTable.getShell(),
                mUpdaterData.getAvdManager(),
mImageFactory,
                mUpdaterData.getSdkLog(),
null);
dlg.selectInitialDevice(ci.mDevice);









//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/PackagesPage.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/PackagesPage.java
//Synthetic comment -- index 85a4df8..906fd26 100755

//Synthetic comment -- @@ -21,7 +21,7 @@
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.ArchiveInstaller;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.core.PkgCategory;
import com.android.sdkuilib.internal.repository.core.PkgCategoryApi;
import com.android.sdkuilib.internal.repository.core.PkgContentProvider;
//Synthetic comment -- @@ -136,10 +136,10 @@
public PackagesPage(
Composite parent,
int swtStyle,
            UpdaterData updaterData,
SdkInvocationContext context) {
super(parent, swtStyle);
        mImpl = new PackagesPageImpl(updaterData) {
@Override
protected boolean isUiDisposed() {
return mGroupPackages == null || mGroupPackages.isDisposed();
//Synthetic comment -- @@ -443,8 +443,8 @@
}

private Image getImage(String filename) {
        if (mImpl.mUpdaterData != null) {
            ImageFactory imgFactory = mImpl.mUpdaterData.getImageFactory();
if (imgFactory != null) {
return imgFactory.getImageByName(filename);
}
//Synthetic comment -- @@ -471,7 +471,7 @@
mImpl.fullReload();
break;
case SHOW_ADDON_SITES:
                    AddonSitesDialog d = new AddonSitesDialog(getShell(), mImpl.mUpdaterData);
if (d.open()) {
mImpl.loadPackages();
}
//Synthetic comment -- @@ -588,8 +588,8 @@
private void postCreate() {
mImpl.postCreate();

        if (mImpl.mUpdaterData != null) {
            mTextSdkOsPath.setText(mImpl.mUpdaterData.getOsSdkRoot());
}

((PkgContentProvider) mTreeViewer.getContentProvider()).setDisplayArchives(
//Synthetic comment -- @@ -612,7 +612,7 @@
}

private void loadPackages(boolean useLocalCache, boolean overrideExisting) {
        if (mImpl.mUpdaterData == null) {
return;
}

//Synthetic comment -- @@ -718,7 +718,7 @@
if (mTreeViewer != null && !mTreeViewer.getTree().isDisposed()) {

boolean enablePreviews =
                mImpl.mUpdaterData.getSettingsController().getSettings().getEnablePreviews();

mTreeViewer.setExpandedState(elem, true);
nextCategory: for (Object pkg :
//Synthetic comment -- @@ -1025,17 +1025,17 @@
ArrayList<Archive> archives = new ArrayList<Archive>();
getArchivesForInstall(archives);

        if (mImpl.mUpdaterData != null) {
boolean needsRefresh = false;
try {
beginOperationPending();

                List<Archive> installed = mImpl.mUpdaterData.updateOrInstallAll_WithGUI(
archives,
mCheckFilterObsolete.getSelection() /* includeObsoletes */,
mContext == SdkInvocationContext.IDE ?
                            UpdaterData.TOOLS_MSG_UPDATED_FROM_ADT :
                                UpdaterData.TOOLS_MSG_UPDATED_FROM_SDKMAN);
needsRefresh = installed != null && !installed.isEmpty();
} finally {
endOperationPending();
//Synthetic comment -- @@ -1154,7 +1154,7 @@
try {
beginOperationPending();

                    mImpl.mUpdaterData.getTaskFactory().start("Delete Package", new ITask() {
@Override
public void run(ITaskMonitor monitor) {
monitor.setProgressMax(archives.size() + 1);








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/PackagesPageImpl.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/PackagesPageImpl.java
//Synthetic comment -- index 96d9db2..e68a852 100755

//Synthetic comment -- @@ -23,7 +23,7 @@
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.core.PackageLoader;
import com.android.sdkuilib.internal.repository.core.PackageLoader.ISourceLoadedCallback;
import com.android.sdkuilib.internal.repository.core.PackagesDiffLogic;
//Synthetic comment -- @@ -54,7 +54,7 @@
*/
abstract class PackagesPageImpl {

    final UpdaterData mUpdaterData;
final PackagesDiffLogic mDiffLogic;

private ICheckboxTreeViewer mITreeViewer;
//Synthetic comment -- @@ -63,9 +63,9 @@
private ITreeViewerColumn   mIColumnRevision;
private ITreeViewerColumn   mIColumnStatus;

    PackagesPageImpl(UpdaterData updaterData) {
        mUpdaterData = updaterData;
        mDiffLogic = new PackagesDiffLogic(updaterData);
}

/**
//Synthetic comment -- @@ -142,7 +142,7 @@
*/
void fullReload() {
// Clear all source information, forcing them to be refreshed.
        mUpdaterData.getSources().clearAllPackages();
// Clear and reload all local data too.
localReload();
}
//Synthetic comment -- @@ -157,8 +157,8 @@
*/
void localReload() {
// Clear all source caches, otherwise loading will use the cached data
        mUpdaterData.getLocalSdkParser().clearPackages();
        mUpdaterData.getSdkManager().reloadSdk(mUpdaterData.getSdkLog());
loadPackages();
}

//Synthetic comment -- @@ -186,7 +186,7 @@
* Derived implementations must call this to do the actual work after setting up the UI.
*/
void loadPackagesImpl(final boolean useLocalCache, final boolean overrideExisting) {
        if (mUpdaterData == null) {
return;
}

//Synthetic comment -- @@ -260,15 +260,15 @@
* extra network access. That's {@code useLocalCache} being true.
* <p/>
* Leter it does a second pass with {@code useLocalCache} set to false
     * and actually uses the download cache specified in {@link UpdaterData}.
*
* This is extracted so that we can control this cache via unit tests.
*/
protected PackageLoader getPackageLoader(boolean useLocalCache) {
if (useLocalCache) {
            return new PackageLoader(mUpdaterData, new DownloadCache(Strategy.ONLY_CACHE));
} else {
            return mUpdaterData.getPackageLoader();
}
}

//Synthetic comment -- @@ -447,7 +447,7 @@

@Override
public Image getImage(Object element) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();

if (imgFactory != null) {
if (mColumn == mIColumnName) {








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/SdkUpdaterWindowImpl2.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/SdkUpdaterWindowImpl2.java
//Synthetic comment -- index ae1bf6b..34346df 100755

//Synthetic comment -- @@ -26,7 +26,7 @@
import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.internal.repository.SettingsController.Settings;
import com.android.sdkuilib.internal.repository.SettingsDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.repository.ui.PackagesPage.MenuAction;
import com.android.sdkuilib.internal.tasks.ILogUiProvider;
//Synthetic comment -- @@ -72,7 +72,7 @@
private final Shell mParentShell;
private final SdkInvocationContext mContext;
/** Internal data shared between the window and its pages. */
    private final UpdaterData mUpdaterData;

// --- UI members ---

//Synthetic comment -- @@ -101,27 +101,27 @@
SdkInvocationContext context) {
mParentShell = parentShell;
mContext = context;
        mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);
}

/**
* Creates a new window. Caller must call open(), which will block.
* <p/>
* This is to be used when the window is opened from {@link AvdManagerWindowImpl1}
     * to share the same {@link UpdaterData} structure.
*
* @param parentShell Parent shell.
     * @param updaterData The parent's updater data.
* @param context The {@link SdkInvocationContext} to change the behavior depending on who's
*  opening the SDK Manager.
*/
public SdkUpdaterWindowImpl2(
Shell parentShell,
            UpdaterData updaterData,
SdkInvocationContext context) {
mParentShell = parentShell;
mContext = context;
        mUpdaterData = updaterData;
}

/**
//Synthetic comment -- @@ -189,7 +189,7 @@
}

private void createContents() {
        mPkgPage = new PackagesPage(mShell, SWT.NONE, mUpdaterData, mContext);
mPkgPage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

Composite composite1 = new Composite(mShell, SWT.NONE);
//Synthetic comment -- @@ -330,7 +330,7 @@
Settings settings1 = new Settings(mSettingsController.getSettings());

// open the dialog and wait for it to close
                        SettingsDialog sd = new SettingsDialog(mShell, mUpdaterData);
sd.open();

// get the new settings
//Synthetic comment -- @@ -346,27 +346,27 @@

@Override
public void onAboutMenuSelected() {
                        AboutDialog ad = new AboutDialog(mShell, mUpdaterData);
ad.open();
}

@Override
public void printError(String format, Object... args) {
                        if (mUpdaterData != null) {
                            mUpdaterData.getSdkLog().error(null, format, args);
}
}
};
} catch (Throwable e) {
                mUpdaterData.getSdkLog().error(e, "Failed to setup menu bar");
e.printStackTrace();
}
}
}

private Image getImage(String filename) {
        if (mUpdaterData != null) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();
if (imgFactory != null) {
return imgFactory.getImageByName(filename);
}
//Synthetic comment -- @@ -383,7 +383,7 @@
*/
private void createLogWindow() {
mLogWindow = new LogWindow(mShell,
                mContext == SdkInvocationContext.IDE ? mUpdaterData.getSdkLog() : null);
mLogWindow.open();
}

//Synthetic comment -- @@ -399,7 +399,7 @@
*/
@Override
public void addListener(ISdkChangeListener listener) {
        mUpdaterData.addListeners(listener);
}

/**
//Synthetic comment -- @@ -408,7 +408,7 @@
*/
@Override
public void removeListener(ISdkChangeListener listener) {
        mUpdaterData.removeListener(listener);
}

// --- Internals & UI Callbacks -----------
//Synthetic comment -- @@ -417,9 +417,9 @@
* Called before the UI is created.
*/
private void preCreateContent() {
        mUpdaterData.setWindowShell(mShell);
// We need the UI factory to create the UI
        mUpdaterData.setImageFactory(new ImageFactory(mShell.getDisplay()));
// Note: we can't create the TaskFactory yet because we need the UI
// to be created first, so this is done in postCreateContent().
}
//Synthetic comment -- @@ -479,18 +479,18 @@

factory.setProgressView(
new ProgressView(mStatusText, mProgressBar, mButtonStop, logAdapter));
        mUpdaterData.setTaskFactory(factory);

setWindowImage(mShell);

setupSources();
initializeSettings();

        if (mUpdaterData.checkIfInitFailed()) {
return false;
}

        mUpdaterData.broadcastOnSdkLoaded();

// Tell the one page its the selected one
mPkgPage.performFirstLoad();
//Synthetic comment -- @@ -509,8 +509,8 @@
imageName = "android_icon_128.png"; //$NON-NLS-1$
}

        if (mUpdaterData != null) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();
if (imgFactory != null) {
shell.setImage(imgFactory.getImageByName(imageName));
}
//Synthetic comment -- @@ -522,15 +522,15 @@
*/
private void dispose() {
mLogWindow.close();
        mUpdaterData.getSources().saveUserAddons(mUpdaterData.getSdkLog());
}

/**
* Callback called when the window shell is disposed.
*/
private void onAndroidSdkUpdaterDispose() {
        if (mUpdaterData != null) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();
if (imgFactory != null) {
imgFactory.dispose();
}
//Synthetic comment -- @@ -541,7 +541,7 @@
* Used to initialize the sources.
*/
private void setupSources() {
        mUpdaterData.setupDefaultSources();
}

/**
//Synthetic comment -- @@ -551,7 +551,7 @@
* and use it to load and apply these settings.
*/
private void initializeSettings() {
        mSettingsController = mUpdaterData.getSettingsController();
mSettingsController.loadSettings();
mSettingsController.applySettings();
}
//Synthetic comment -- @@ -569,19 +569,19 @@
}

private void onAvdManager() {
        ITaskFactory oldFactory = mUpdaterData.getTaskFactory();

try {
AvdManagerWindowImpl1 win = new AvdManagerWindowImpl1(
mShell,
                    mUpdaterData,
AvdInvocationContext.DIALOG);

win.open();
} catch (Exception e) {
            mUpdaterData.getSdkLog().error(e, "AVD Manager window error");
} finally {
            mUpdaterData.setTaskFactory(oldFactory);
}
}









//Synthetic comment -- diff --git a/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/MockUpdaterData.java b/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/MockSwtUpdaterData.java
similarity index 95%
rename from sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/MockUpdaterData.java
rename to sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/MockSwtUpdaterData.java
//Synthetic comment -- index f19bfcc..9e60ee1 100755

//Synthetic comment -- @@ -40,7 +40,7 @@
import java.util.Properties;

/** A mock UpdaterData that simply records what would have been installed. */
public class MockUpdaterData extends UpdaterData {

public final static String SDK_PATH = "/tmp/SDK";

//Synthetic comment -- @@ -56,16 +56,16 @@
};
};

    /** Creates a {@link MockUpdaterData} using a {@link MockEmptySdkManager}. */
    public MockUpdaterData() {
super(SDK_PATH, new MockLog());

setTaskFactory(new MockTaskFactory());
setImageFactory(new NullImageFactory());
}

    /** Creates a {@link MockUpdaterData} using the given {@link SdkManager}. */
    public MockUpdaterData(SdkManager sdkManager) {
super(sdkManager.getLocation(), new MockLog());
setSdkManager(sdkManager);
setTaskFactory(new MockTaskFactory());








//Synthetic comment -- diff --git a/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/SdkUpdaterLogicTest.java b/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/SdkUpdaterLogicTest.java
//Synthetic comment -- index 6a93914..0bca0a1 100755

//Synthetic comment -- @@ -44,7 +44,7 @@

public class SdkUpdaterLogicTest extends TestCase {

    private static class NullUpdaterData implements IUpdaterData {

@Override
public AvdManager getAvdManager() {








//Synthetic comment -- diff --git a/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/UpdaterDataTest.java b/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/UpdaterDataTest.java
//Synthetic comment -- index 6b8c850..a2d12bc 100755

//Synthetic comment -- @@ -26,12 +26,12 @@

public class UpdaterDataTest extends TestCase {

    private MockUpdaterData m;

@Override
protected void setUp() throws Exception {
super.setUp();
        m = new MockUpdaterData();
assertEquals("[]", Arrays.toString(m.getInstalled()));
}









//Synthetic comment -- diff --git a/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/core/PackagesDiffLogicTest.java b/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/core/PackagesDiffLogicTest.java
//Synthetic comment -- index 1f7a27a..7427b3b 100755

//Synthetic comment -- @@ -32,7 +32,7 @@
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.repository.PkgProps;
import com.android.sdkuilib.internal.repository.ISettingsPage;
import com.android.sdkuilib.internal.repository.MockUpdaterData;
import com.android.sdkuilib.internal.repository.core.PackagesDiffLogic;
import com.android.sdkuilib.internal.repository.core.PkgCategory;
import com.android.sdkuilib.internal.repository.core.PkgItem;
//Synthetic comment -- @@ -44,13 +44,13 @@
public class PackagesDiffLogicTest extends TestCase {

private PackagesDiffLogic m;
    private MockUpdaterData u;

@Override
protected void setUp() throws Exception {
super.setUp();

        u = new MockUpdaterData();
m = new PackagesDiffLogic(u);
}









//Synthetic comment -- diff --git a/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/ui/MockPackagesPageImpl.java b/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/ui/MockPackagesPageImpl.java
//Synthetic comment -- index 04b3027..18ddd9c 100755

//Synthetic comment -- @@ -20,7 +20,7 @@
import com.android.sdklib.internal.repository.DownloadCache.Strategy;
import com.android.sdklib.util.SparseIntArray;
import com.android.sdkuilib.internal.repository.MockDownloadCache;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.core.PackageLoader;
import com.android.sdkuilib.internal.repository.core.PkgCategory;
import com.android.sdkuilib.internal.repository.core.PkgContentProvider;
//Synthetic comment -- @@ -33,8 +33,8 @@

public class MockPackagesPageImpl extends PackagesPageImpl {

    public MockPackagesPageImpl(UpdaterData updaterData) {
        super(updaterData);
}

/** UI is never disposed in the unit test. */
//Synthetic comment -- @@ -91,18 +91,18 @@

/**
* In this mock version, we use the default {@link PackageLoader} which will
     * use the {@link DownloadCache} from the {@link UpdaterData}. This should be
* the mock download cache, in which case we change the strategy at run-time
* to set it to only-cache on the first manager update.
*/
@Override
protected PackageLoader getPackageLoader(boolean useLocalCache) {
        DownloadCache dc = mUpdaterData.getDownloadCache();
assert dc instanceof MockDownloadCache;
if (dc instanceof MockDownloadCache) {
((MockDownloadCache) dc).overrideStrategy(useLocalCache ? Strategy.ONLY_CACHE : null);
}
        return mUpdaterData.getPackageLoader();
}

/**








//Synthetic comment -- diff --git a/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/ui/SdkManagerUpgradeTest.java b/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/ui/SdkManagerUpgradeTest.java
//Synthetic comment -- index 00d9684..b3e84b1 100755

//Synthetic comment -- @@ -20,7 +20,7 @@
import com.android.sdklib.SdkManagerTestCase;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdkuilib.internal.repository.MockDownloadCache;
import com.android.sdkuilib.internal.repository.MockUpdaterData;

import java.util.Arrays;

//Synthetic comment -- @@ -42,7 +42,7 @@
public void testPackagesPage1() throws Exception {
SdkManager sdkman = getSdkManager();

        MockUpdaterData updaterData = new MockUpdaterData(sdkman);
MockDownloadCache cache = (MockDownloadCache) updaterData.getDownloadCache();
updaterData.setupDefaultSources();








