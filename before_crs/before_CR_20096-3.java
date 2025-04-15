/*SDK Manager in ADT: make it possible to unload the SDK.

One of the issues when using the SDK Manager from Eclipse
is that, at least on Windows, we can't upgrade the platform-tools
or the targets since they might be locked by ADT.
Typically dex.jar is in use and typically we have various data/res
(ttf and xml) files used by any opened layout editor.

This adds the necessary infrastructure to know in ADT when
packages are going to be installed. There's a crude attempt to
solve the dex.jar issue. However unloading the targets isn't
done yet.

Change-Id:I5c48144501c7f39ef779bfeffdfae85a48c65a29*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/DexWrapper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/DexWrapper.java
//Synthetic comment -- index 471b828..e03a150 100644

//Synthetic comment -- @@ -114,7 +114,28 @@
}

/**
* Runs the dex command.
* @param osOutFilePath the OS path to the outputfile (classes.dex
* @param osFilenames list of input source files (.class and .jar files)
* @param verbose verbose mode.
//Synthetic comment -- @@ -126,6 +147,22 @@
public synchronized int run(String osOutFilePath, String[] osFilenames,
boolean verbose, PrintStream outStream, PrintStream errStream) throws CoreException {

try {
// set the stream
mConsoleErr.set(null /* obj: static field */, errStream);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 61e3176..03083b8 100644

//Synthetic comment -- @@ -1279,7 +1279,7 @@
String validName = name.replaceAll("[^0-9a-zA-Z]+", "_"); //$NON-NLS-1$ //$NON-NLS-2$

//ensure the valid is not negative as - is not a valid char
        long hash = (long)name.hashCode() & 0x00000000ffffffffL;
return "_android_" + validName + "_" + Long.toString(hash, 16) ;  //$NON-NLS-1$ //$NON-NLS-2$
}

//Synthetic comment -- @@ -1685,7 +1685,7 @@

/**
* Computes a new IPath targeting a given target, but relative to a given base.
     * <p/>{@link IPath#makeRelativeTo(IPath, IPath)} is only available in 3.5 and later.
* <p/>This is based on the implementation {@link Path#makeRelativeTo(IPath)}.
* @param target the target of the IPath
* @param base the IPath to base the relative path on.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/actions/AvdManagerAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/actions/AvdManagerAction.java
//Synthetic comment -- index e0de79a..42739e2 100755

//Synthetic comment -- @@ -17,8 +17,10 @@
package com.android.ide.eclipse.adt.internal.wizards.actions;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.sdk.AdtConsoleSdkLog;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdkuilib.repository.UpdaterWindow;

import org.eclipse.jface.action.IAction;
//Synthetic comment -- @@ -52,13 +54,66 @@
AdtPlugin.getDisplay().getActiveShell(),
new AdtConsoleSdkLog(),
sdk.getSdkLocation());
            window.addListeners(new UpdaterWindow.ISdkListener() {
                public void onSdkChange(boolean init) {
                    if (init == false) { // ignore initial load of the SDK.
                        AdtPlugin.getDefault().reparseSdk();
}
}
            });
window.open();
} else {
AdtPlugin.displayError("Android SDK",








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AvdManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AvdManagerPage.java
//Synthetic comment -- index b4314cb..7594f16 100755

//Synthetic comment -- @@ -20,7 +20,7 @@
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdkuilib.internal.widgets.AvdSelector;
import com.android.sdkuilib.internal.widgets.AvdSelector.DisplayMode;
import com.android.sdkuilib.repository.UpdaterWindow.ISdkListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
//Synthetic comment -- @@ -28,7 +28,7 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class AvdManagerPage extends Composite implements ISdkListener {

private AvdSelector mAvdSelector;

//Synthetic comment -- @@ -93,10 +93,24 @@
// nothing to be done for now.
}

    public void onSdkChange(boolean init) {
mAvdSelector.refresh(false /*reload*/);
}

// End of hiding from SWT Designer
//$hide<<$
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/LocalPackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/LocalPackagesPage.java
//Synthetic comment -- index 88e81f2..4ca35d2 100755

//Synthetic comment -- @@ -19,7 +19,7 @@
import com.android.sdklib.internal.repository.Archive;
import com.android.sdklib.internal.repository.IDescription;
import com.android.sdklib.internal.repository.Package;
import com.android.sdkuilib.repository.UpdaterWindow.ISdkListener;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
//Synthetic comment -- @@ -42,7 +42,7 @@

import java.io.File;

public class LocalPackagesPage extends Composite implements ISdkListener {

private final UpdaterData mUpdaterData;

//Synthetic comment -- @@ -276,7 +276,13 @@
updateButtonsState();
}

    public void onSdkChange(boolean init) {
LocalSdkAdapter localSdkAdapter = mUpdaterData.getLocalSdkAdapter();
mTableViewerPackages.setLabelProvider(  localSdkAdapter.getLabelProvider());
mTableViewerPackages.setContentProvider(localSdkAdapter.getContentProvider());
//Synthetic comment -- @@ -284,6 +290,14 @@
onTreeSelected();
}

// End of hiding from SWT Designer
//$hide<<$
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RemotePackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RemotePackagesPage.java
//Synthetic comment -- index 749365f..6fbf060 100755

//Synthetic comment -- @@ -23,7 +23,7 @@
import com.android.sdklib.internal.repository.SdkAddonSource;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.SdkSourceCategory;
import com.android.sdkuilib.repository.UpdaterWindow.ISdkListener;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
//Synthetic comment -- @@ -53,7 +53,7 @@
import java.util.ArrayList;


public class RemotePackagesPage extends Composite implements ISdkListener {

private final UpdaterData mUpdaterData;

//Synthetic comment -- @@ -190,14 +190,6 @@
// Disable the check that prevents subclassing of SWT components
}

    public void onSdkChange(boolean init) {
        RepoSourcesAdapter sources = mUpdaterData.getSourcesAdapter();
        mTreeViewerSources.setContentProvider(sources.getContentProvider());
        mTreeViewerSources.setLabelProvider(  sources.getLabelProvider());
        mTreeViewerSources.setInput(sources);
        onTreeSelected();
    }

// -- Start of internal part ----------
// Hide everything down-below from SWT designer
//$hide>>$
//Synthetic comment -- @@ -476,6 +468,28 @@
mUpdateOnlyCheckBox.setSelection(mUpdaterData.getSettingsController().getShowUpdateOnly());
}

// End of hiding from SWT Designer
//$hide<<$
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index e569eba..0aa8a15 100755

//Synthetic comment -- @@ -43,7 +43,7 @@
import com.android.sdklib.repository.SdkAddonsListConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.repository.UpdaterWindow.ISdkListener;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
//Synthetic comment -- @@ -80,7 +80,7 @@

private final SettingsController mSettingsController;

    private final ArrayList<ISdkListener> mListeners = new ArrayList<ISdkListener>();

private Shell mWindowShell;

//Synthetic comment -- @@ -163,15 +163,15 @@
return mSettingsController;
}

    /** Adds a listener ({@link ISdkListener}) that is notified when the SDK is reloaded. */
    public void addListeners(ISdkListener listener) {
if (mListeners.contains(listener) == false) {
mListeners.add(listener);
}
}

    /** Removes a listener ({@link ISdkListener}) that is notified when the SDK is reloaded. */
    public void removeListener(ISdkListener listener) {
mListeners.remove(listener);
}

//Synthetic comment -- @@ -243,7 +243,7 @@
}

// notify listeners.
        notifyListeners(false /*init*/);
}

/**
//Synthetic comment -- @@ -251,7 +251,7 @@
* <p/>
* This also reloads the AVDs in case their status changed.
* <p/>
     * This does not notify the listeners ({@link ISdkListener}).
*/
public void reloadSdk() {
// reload SDK
//Synthetic comment -- @@ -266,12 +266,10 @@
}
}

        // notify adapters?
mLocalSdkParser.clearPackages();
        // TODO

// notify listeners
        notifyListeners(false /*init*/);
}

/**
//Synthetic comment -- @@ -366,30 +364,6 @@

return packages;
}

    /**
     * Notify the listeners ({@link ISdkListener}) that the SDK was reloaded.
     * <p/>
     * This can be called from any thread.
     *
     * @param init whether the SDK loaded for the first time.
     */
    public void notifyListeners(final boolean init) {
        if (mWindowShell != null && mListeners.size() > 0) {
            mWindowShell.getDisplay().syncExec(new Runnable() {
                public void run() {
                    for (ISdkListener listener : mListeners) {
                        try {
                            listener.onSdkChange(init);
                        } catch (Throwable t) {
                            mSdkLog.error(t, null);
                        }
                    }
                }
            });
        }
    }

/**
* Install the list of given {@link Archive}s. This is invoked by the user selecting some
* packages in the remote page and then clicking "install selected".
//Synthetic comment -- @@ -413,6 +387,7 @@
boolean installedAddon = false;
boolean installedTools = false;
boolean installedPlatformTools = false;

// Mark all current local archives as already installed.
HashSet<Archive> installedArchives = new HashSet<Archive>();
//Synthetic comment -- @@ -459,6 +434,11 @@
}
}

ArchiveInstaller installer = new ArchiveInstaller();
if (installer.install(archive,
mOsSdkRoot,
//Synthetic comment -- @@ -523,6 +503,10 @@
}
}

if (installedAddon || installedPlatformTools) {
// We need to restart ADB. Actually since we don't know if it's even
// running, maybe we should just kill it and not start it.
//Synthetic comment -- @@ -910,4 +894,84 @@
}
}

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java
//Synthetic comment -- index b5122d7..19d3916 100755

//Synthetic comment -- @@ -21,7 +21,7 @@
import com.android.sdklib.SdkConstants;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressTaskFactory;
import com.android.sdkuilib.repository.UpdaterWindow.ISdkListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
//Synthetic comment -- @@ -194,7 +194,7 @@
/**
* Adds a new listener to be notified when a change is made to the content of the SDK.
*/
    public void addListeners(ISdkListener listener) {
mUpdaterData.addListeners(listener);
}

//Synthetic comment -- @@ -202,7 +202,7 @@
* Removes a new listener to be notified anymore when a change is made to the content of
* the SDK.
*/
    public void removeListener(ISdkListener listener) {
mUpdaterData.removeListener(listener);
}

//Synthetic comment -- @@ -297,7 +297,7 @@
return false;
}

        mUpdaterData.notifyListeners(true /*init*/);

if (mRequestAutoUpdate) {
mUpdaterData.updateOrInstallAll_WithGUI(
//Synthetic comment -- @@ -403,7 +403,7 @@
*/
private void setupSources() {
mUpdaterData.setupDefaultSources();
        mRemotePackagesPage.onSdkChange(false /*init*/);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/ISdkChangeListener.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/ISdkChangeListener.java
new file mode 100755
//Synthetic comment -- index 0000000..9cca7aa

//Synthetic comment -- @@ -0,0 +1,54 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java
//Synthetic comment -- index 4e6f548..c78b08c 100755

//Synthetic comment -- @@ -32,18 +32,6 @@
private UpdaterWindowImpl mWindow;

/**
     * Interface for listeners on SDK modifications (ie new installed compoments, or deleted
     * components)
     */
    public interface ISdkListener {
        /**
         * Sent when the content of the SDK changed.
         * @param init whether this is called on the initial load of the SDK.
         */
        void onSdkChange(boolean init);
    }

    /**
* Creates a new window. Caller must call open(), which will block.
*
* @param parentShell Parent shell.
//Synthetic comment -- @@ -91,15 +79,15 @@
/**
* Adds a new listener to be notified when a change is made to the content of the SDK.
*/
    public void addListeners(ISdkListener listener) {
        mWindow.addListeners(listener);
}

/**
* Removes a new listener to be notified anymore when a change is made to the content of
* the SDK.
*/
    public void removeListener(ISdkListener listener) {
mWindow.removeListener(listener);
}








