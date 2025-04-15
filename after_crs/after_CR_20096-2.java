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
     * Removes any reference to the dex library.
     * <p/>
     * {@link #loadDex(String)} must be called on the wrapper
     * before {@link #run(String, String[], boolean, PrintStream, PrintStream)} can
     * be used again.
     */
    public synchronized void unload() {
        mRunMethod = null;
        mArgConstructor = null;
        mArgOutName = null;
        mArgJarOutput = null;
        mArgFileNames = null;
        mArgVerbose = null;
        mConsoleOut = null;
        mConsoleErr = null;
        System.gc();
    }

    /**
* Runs the dex command.
     * The wrapper must have been initialized via {@link #loadDex(String)} first.
     *
* @param osOutFilePath the OS path to the outputfile (classes.dex
* @param osFilenames list of input source files (.class and .jar files)
* @param verbose verbose mode.
//Synthetic comment -- @@ -126,6 +147,22 @@
public synchronized int run(String osOutFilePath, String[] osFilenames,
boolean verbose, PrintStream outStream, PrintStream errStream) throws CoreException {

        assert mRunMethod != null;
        assert mArgConstructor != null;
        assert mArgOutName != null;
        assert mArgJarOutput != null;
        assert mArgFileNames != null;
        assert mArgVerbose != null;
        assert mConsoleOut != null;
        assert mConsoleErr != null;

        if (mRunMethod == null) {
            throw new CoreException(createErrorStatus(
                    String.format(Messages.DexWrapper_Unable_To_Execute_Dex_s,
                            "wrapper was not properly loaded first"),
                    null /*exception*/));
        }

try {
// set the stream
mConsoleErr.set(null /* obj: static field */, errStream);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 61e3176..03083b8 100644

//Synthetic comment -- @@ -1279,7 +1279,7 @@
String validName = name.replaceAll("[^0-9a-zA-Z]+", "_"); //$NON-NLS-1$ //$NON-NLS-2$

//ensure the valid is not negative as - is not a valid char
        long hash = name.hashCode() & 0x00000000ffffffffL;
return "_android_" + validName + "_" + Long.toString(hash, 16) ;  //$NON-NLS-1$ //$NON-NLS-2$
}

//Synthetic comment -- @@ -1685,7 +1685,7 @@

/**
* Computes a new IPath targeting a given target, but relative to a given base.
     * <p/>{@link IPath#makeRelativeTo(IPath)} is only available in 3.5 and later.
* <p/>This is based on the implementation {@link Path#makeRelativeTo(IPath)}.
* @param target the target of the IPath
* @param base the IPath to base the relative path on.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/actions/AvdManagerAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/actions/AvdManagerAction.java
//Synthetic comment -- index e0de79a..42739e2 100755

//Synthetic comment -- @@ -17,8 +17,10 @@
package com.android.ide.eclipse.adt.internal.wizards.actions;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.DexWrapper;
import com.android.ide.eclipse.adt.internal.sdk.AdtConsoleSdkLog;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.UpdaterWindow;

import org.eclipse.jface.action.IAction;
//Synthetic comment -- @@ -52,13 +54,66 @@
AdtPlugin.getDisplay().getActiveShell(),
new AdtConsoleSdkLog(),
sdk.getSdkLocation());

            ISdkChangeListener listener = new ISdkChangeListener() {
                public void onSdkLoaded() {
                    // Ignore initial load of the SDK.
                }

                /**
                 * Unload all we can from the SDK before new packages are installed.
                 * Typically we need to get rid of references to dx from platform-tools
                 * and to any platform resource data.
                 * <p/>
                 * {@inheritDoc}
                 */
                public void preInstallHook() {

                    // TODO we need to unload as much of as SDK as possible. Otherwise
                    // on Windows we end up with Eclipse locking some files and we can't
                    // replace them.
                    //
                    // At this point, we know what the user wants to install so it would be
                    // possible to pass in flags to know what needs to be unloaded. Typically
                    // we need to:
                    // - unload dex if platform-tools is going to be updated. There's a vague
                    //   attempt below at removing any references to dex and GCing. Seems
                    //   to do the trick.
                    // - unload any target that is going to be updated since it may have
                    //   resource data used by a current layout editor (e.g. data/*.ttf
                    //   and various data/res/*.xml).
                    //
                    // Most important we need to make sure there isn't a build going on
                    // and if there is one, either abort it or wait for it to complete and
                    // then we want to make sure we don't get any attempt to use the SDK
                    // before the postInstallHook is called.

                    Sdk sdk = Sdk.getCurrent();
                    if (sdk != null) {
                        DexWrapper dx = sdk.getDexWrapper();
                        dx.unload();
}
}

                /**
                 * Nothing to do. We'll reparse the SDK later in onSdkReload.
                 * <p/>
                 * {@inheritDoc}
                 */
                public void postInstallHook() {
                }

                /**
                 * Reparse the SDK in case anything was add/removed.
                 * <p/>
                 * {@inheritDoc}
                 */
                public void onSdkReload() {
                    AdtPlugin.getDefault().reparseSdk();
                }
            };

            window.addListener(listener);
window.open();
} else {
AdtPlugin.displayError("Android SDK",








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AvdManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AvdManagerPage.java
//Synthetic comment -- index b4314cb..7594f16 100755

//Synthetic comment -- @@ -20,7 +20,7 @@
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdkuilib.internal.widgets.AvdSelector;
import com.android.sdkuilib.internal.widgets.AvdSelector.DisplayMode;
import com.android.sdkuilib.repository.ISdkChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
//Synthetic comment -- @@ -28,7 +28,7 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class AvdManagerPage extends Composite implements ISdkChangeListener {

private AvdSelector mAvdSelector;

//Synthetic comment -- @@ -93,10 +93,24 @@
// nothing to be done for now.
}

    // --- Implementation of ISdkChangeListener ---

    public void onSdkLoaded() {
        onSdkReload();
    }

    public void onSdkReload() {
mAvdSelector.refresh(false /*reload*/);
}

    public void preInstallHook() {
        // nothing to be done for now.
    }

    public void postInstallHook() {
        // nothing to be done for now.
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
import com.android.sdkuilib.repository.ISdkChangeListener;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
//Synthetic comment -- @@ -42,7 +42,7 @@

import java.io.File;

public class LocalPackagesPage extends Composite implements ISdkChangeListener {

private final UpdaterData mUpdaterData;

//Synthetic comment -- @@ -276,7 +276,13 @@
updateButtonsState();
}

    // --- Implementation of ISdkChangeListener ---

    public void onSdkLoaded() {
        onSdkReload();
    }

    public void onSdkReload() {
LocalSdkAdapter localSdkAdapter = mUpdaterData.getLocalSdkAdapter();
mTableViewerPackages.setLabelProvider(  localSdkAdapter.getLabelProvider());
mTableViewerPackages.setContentProvider(localSdkAdapter.getContentProvider());
//Synthetic comment -- @@ -284,6 +290,14 @@
onTreeSelected();
}

    public void preInstallHook() {
        // nothing to be done for now.
    }

    public void postInstallHook() {
        // nothing to be done for now.
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
import com.android.sdkuilib.repository.ISdkChangeListener;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
//Synthetic comment -- @@ -53,7 +53,7 @@
import java.util.ArrayList;


public class RemotePackagesPage extends Composite implements ISdkChangeListener {

private final UpdaterData mUpdaterData;

//Synthetic comment -- @@ -190,14 +190,6 @@
// Disable the check that prevents subclassing of SWT components
}

// -- Start of internal part ----------
// Hide everything down-below from SWT designer
//$hide>>$
//Synthetic comment -- @@ -476,6 +468,28 @@
mUpdateOnlyCheckBox.setSelection(mUpdaterData.getSettingsController().getShowUpdateOnly());
}

    // --- Implementation of ISdkChangeListener ---

    public void onSdkLoaded() {
        onSdkReload();
    }

    public void onSdkReload() {
        RepoSourcesAdapter sources = mUpdaterData.getSourcesAdapter();
        mTreeViewerSources.setContentProvider(sources.getContentProvider());
        mTreeViewerSources.setLabelProvider(  sources.getLabelProvider());
        mTreeViewerSources.setInput(sources);
        onTreeSelected();
    }

    public void preInstallHook() {
        // nothing to be done for now.
    }

    public void postInstallHook() {
        // nothing to be done for now.
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
import com.android.sdkuilib.repository.ISdkChangeListener;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
//Synthetic comment -- @@ -80,7 +80,7 @@

private final SettingsController mSettingsController;

    private final ArrayList<ISdkChangeListener> mListeners = new ArrayList<ISdkChangeListener>();

private Shell mWindowShell;

//Synthetic comment -- @@ -163,15 +163,15 @@
return mSettingsController;
}

    /** Adds a listener ({@link ISdkChangeListener}) that is notified when the SDK is reloaded. */
    public void addListeners(ISdkChangeListener listener) {
if (mListeners.contains(listener) == false) {
mListeners.add(listener);
}
}

    /** Removes a listener ({@link ISdkChangeListener}) that is notified when the SDK is reloaded. */
    public void removeListener(ISdkChangeListener listener) {
mListeners.remove(listener);
}

//Synthetic comment -- @@ -243,7 +243,7 @@
}

// notify listeners.
        broadcastOnSdkReload();
}

/**
//Synthetic comment -- @@ -251,7 +251,7 @@
* <p/>
* This also reloads the AVDs in case their status changed.
* <p/>
     * This does not notify the listeners ({@link ISdkChangeListener}).
*/
public void reloadSdk() {
// reload SDK
//Synthetic comment -- @@ -266,12 +266,10 @@
}
}

mLocalSdkParser.clearPackages();

// notify listeners
        broadcastOnSdkReload();
}

/**
//Synthetic comment -- @@ -366,30 +364,6 @@

return packages;
}
/**
* Install the list of given {@link Archive}s. This is invoked by the user selecting some
* packages in the remote page and then clicking "install selected".
//Synthetic comment -- @@ -413,6 +387,7 @@
boolean installedAddon = false;
boolean installedTools = false;
boolean installedPlatformTools = false;
                boolean preInstallHookInvoked = false;

// Mark all current local archives as already installed.
HashSet<Archive> installedArchives = new HashSet<Archive>();
//Synthetic comment -- @@ -459,6 +434,11 @@
}
}

                        if (!preInstallHookInvoked) {
                            preInstallHookInvoked = true;
                            broadcastPreInstallHook();
                        }

ArchiveInstaller installer = new ArchiveInstaller();
if (installer.install(archive,
mOsSdkRoot,
//Synthetic comment -- @@ -523,6 +503,10 @@
}
}

                if (preInstallHookInvoked) {
                    broadcastPostInstallHook();
                }

if (installedAddon || installedPlatformTools) {
// We need to restart ADB. Actually since we don't know if it's even
// running, maybe we should just kill it and not start it.
//Synthetic comment -- @@ -910,4 +894,84 @@
}
}

    /**
     * Safely invoke all the registered {@link ISdkChangeListener#onSdkLoaded()}.
     * This can be called from any thread.
     */
    /*package*/ void broadcastOnSdkLoaded() {
        if (mWindowShell != null && mListeners.size() > 0) {
            mWindowShell.getDisplay().syncExec(new Runnable() {
                public void run() {
                    for (ISdkChangeListener listener : mListeners) {
                        try {
                            listener.onSdkLoaded();
                        } catch (Throwable t) {
                            mSdkLog.error(t, null);
                        }
                    }
                }
            });
        }
    }

    /**
     * Safely invoke all the registered {@link ISdkChangeListener#onSdkReload()}.
     * This can be called from any thread.
     */
    private void broadcastOnSdkReload() {
        if (mWindowShell != null && mListeners.size() > 0) {
            mWindowShell.getDisplay().syncExec(new Runnable() {
                public void run() {
                    for (ISdkChangeListener listener : mListeners) {
                        try {
                            listener.onSdkReload();
                        } catch (Throwable t) {
                            mSdkLog.error(t, null);
                        }
                    }
                }
            });
        }
    }

    /**
     * Safely invoke all the registered {@link ISdkChangeListener#preInstallHook()}.
     * This can be called from any thread.
     */
    private void broadcastPreInstallHook() {
        if (mWindowShell != null && mListeners.size() > 0) {
            mWindowShell.getDisplay().syncExec(new Runnable() {
                public void run() {
                    for (ISdkChangeListener listener : mListeners) {
                        try {
                            listener.preInstallHook();
                        } catch (Throwable t) {
                            mSdkLog.error(t, null);
                        }
                    }
                }
            });
        }
    }

    /**
     * Safely invoke all the registered {@link ISdkChangeListener#postInstallHook()}.
     * This can be called from any thread.
     */
    private void broadcastPostInstallHook() {
        if (mWindowShell != null && mListeners.size() > 0) {
            mWindowShell.getDisplay().syncExec(new Runnable() {
                public void run() {
                    for (ISdkChangeListener listener : mListeners) {
                        try {
                            listener.postInstallHook();
                        } catch (Throwable t) {
                            mSdkLog.error(t, null);
                        }
                    }
                }
            });
        }
    }

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java
//Synthetic comment -- index b5122d7..19d3916 100755

//Synthetic comment -- @@ -21,7 +21,7 @@
import com.android.sdklib.SdkConstants;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressTaskFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
//Synthetic comment -- @@ -194,7 +194,7 @@
/**
* Adds a new listener to be notified when a change is made to the content of the SDK.
*/
    public void addListener(ISdkChangeListener listener) {
mUpdaterData.addListeners(listener);
}

//Synthetic comment -- @@ -202,7 +202,7 @@
* Removes a new listener to be notified anymore when a change is made to the content of
* the SDK.
*/
    public void removeListener(ISdkChangeListener listener) {
mUpdaterData.removeListener(listener);
}

//Synthetic comment -- @@ -297,7 +297,7 @@
return false;
}

        mUpdaterData.broadcastOnSdkLoaded();

if (mRequestAutoUpdate) {
mUpdaterData.updateOrInstallAll_WithGUI(
//Synthetic comment -- @@ -403,7 +403,7 @@
*/
private void setupSources() {
mUpdaterData.setupDefaultSources();
        mRemotePackagesPage.onSdkReload();
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/ISdkChangeListener.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/ISdkChangeListener.java
new file mode 100755
//Synthetic comment -- index 0000000..9cca7aa

//Synthetic comment -- @@ -0,0 +1,54 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdkuilib.repository;


/**
 * Interface for listeners on SDK modifications by the SDK Manager UI.
 * This notifies when the SDK manager is first loading the SDK or before/after it installed
 * a package.
 */
public interface ISdkChangeListener {
    /**
     * Invoked when the content of the SDK is being loaded by the SDK Manager UI
     * for the first time.
     * This is generally followed by a call to {@link #onSdkReload()}
     * or by a call to {@link #preInstallHook()}.
     */
    void onSdkLoaded();

    /**
     * Invoked when the SDK Manager UI is about to start installing packages.
     * This will be followed by a call to {@link #postInstallHook()}.
     */
    void preInstallHook();

    /**
     * Invoked when the SDK Manager UI is done installing packages.
     * Some new packages might have been installed or the user might have cancelled the operation.
     * This is generally followed by a call to {@link #onSdkReload()}.
     */
    void postInstallHook();

    /**
     * Invoked when the content of the SDK is being reloaded by the SDK Manager UI,
     * typically after a package was installed. The SDK content might or might not
     * have changed.
     */
    void onSdkReload();
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java
//Synthetic comment -- index 4e6f548..c78b08c 100755

//Synthetic comment -- @@ -32,18 +32,6 @@
private UpdaterWindowImpl mWindow;

/**
* Creates a new window. Caller must call open(), which will block.
*
* @param parentShell Parent shell.
//Synthetic comment -- @@ -91,15 +79,15 @@
/**
* Adds a new listener to be notified when a change is made to the content of the SDK.
*/
    public void addListener(ISdkChangeListener listener) {
        mWindow.addListener(listener);
}

/**
* Removes a new listener to be notified anymore when a change is made to the content of
* the SDK.
*/
    public void removeListener(ISdkChangeListener listener) {
mWindow.removeListener(listener);
}








