/*WIP -- NOT FOR Tools r20

Reorganize the installation procedure of the SDK Manager
so that it can do all downloads first, then install packages.

Also provide a hook so that the download/install part can
happen in ADT via an Eclipse progress job.

Change-Id:I7e1316de333c5dcb3e6fcc95e5ab2d040caacf73*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/SdkManagerAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/SdkManagerAction.java
//Synthetic comment -- index ce9030e..71605bb 100755

//Synthetic comment -- @@ -30,15 +30,27 @@
import com.android.sdkuilib.repository.SdkUpdaterWindow;
import com.android.sdkuilib.repository.SdkUpdaterWindow.SdkInvocationContext;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import java.io.File;

/**
* Delegate for the toolbar/menu action "Android SDK Manager".
* It displays the Android SDK Manager.
//Synthetic comment -- @@ -56,12 +68,75 @@
}

@Override
public void run(IAction action) {
if (!openAdtSdkManager()) {
AdtPlugin.displayError(
"Android SDK",
"Location of the Android SDK has not been setup in the preferences.");
}
}

/**
//Synthetic comment -- @@ -213,14 +288,4 @@

return true;
}

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        // nothing related to the current selection.
    }

    @Override
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        // nothing to do.
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ITaskMonitor.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ITaskMonitor.java
//Synthetic comment -- index dcb1f22..e8867fd 100755

//Synthetic comment -- @@ -100,6 +100,14 @@
public void incProgress(int delta);

/**
* Returns the current value of the progress bar,
* between 0 and up to {@link #setProgressMax(int)} - 1.
*








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/NullTaskMonitor.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/NullTaskMonitor.java
//Synthetic comment -- index ac40f57..f92578a 100755

//Synthetic comment -- @@ -80,6 +80,11 @@
// pass
}

/** Always return 1. */
@Override
public int getProgress() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/archives/ArchiveInstaller.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/archives/ArchiveInstaller.java
//Synthetic comment -- index c2e11cd..1ca751d 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib.internal.repository.archives;

import com.android.annotations.Nullable;
import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
//Synthetic comment -- @@ -143,6 +144,27 @@
}

/**
* Downloads an archive and returns the temp file with it.
* Caller is responsible with deleting the temp file when done.
*/
//Synthetic comment -- @@ -304,7 +326,7 @@
* Success is defined as downloading as many bytes as was expected and having the same
* SHA1 as expected. Returns true on success or false if any of those checks fail.
* <p/>
     * Increments the monitor by {@link #NUM_MONITOR_INC}.
*/
private boolean fetchUrl(Archive archive,
File tmpFile,
//Synthetic comment -- @@ -313,6 +335,8 @@
DownloadCache cache,
ITaskMonitor monitor) {

FileOutputStream os = null;
InputStream is = null;
try {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockMonitor.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/MockMonitor.java
//Synthetic comment -- index 42ba916..be0df44 100755

//Synthetic comment -- @@ -83,6 +83,10 @@
}

@Override
public int getProgress() {
return 0;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ISdkUpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ISdkUpdaterWindow.java
deleted file mode 100755
//Synthetic comment -- index e5f2521..0000000

//Synthetic comment -- @@ -1,42 +0,0 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
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

package com.android.sdkuilib.internal.repository;

import com.android.sdkuilib.repository.ISdkChangeListener;

/**
 * Interface for the actual implementation of the Update Window.
 */
public interface ISdkUpdaterWindow {

    /**
     * Adds a new listener to be notified when a change is made to the content of the SDK.
     */
    public abstract void addListener(ISdkChangeListener listener);

    /**
     * Removes a new listener to be notified anymore when a change is made to the content of
     * the SDK.
     */
    public abstract void removeListener(ISdkChangeListener listener);

    /**
     * Opens the window.
     */
    public abstract void open();

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/InstallWorkerImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/InstallWorkerImpl.java
new file mode 100755
//Synthetic comment -- index 0000000..4221136

//Synthetic comment -- @@ -0,0 +1,342 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterNoWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterNoWindow.java
//Synthetic comment -- index 89f084c..e1d610d 100755

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.sdklib.internal.repository.NullTaskMonitor;
import com.android.sdklib.internal.repository.UserCredentials;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.util.Pair;

import java.io.IOException;
//Synthetic comment -- @@ -65,7 +66,7 @@
String proxyPort) {
mSdkLog = sdkLog;
mForce = force;
        mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);

// Read and apply settings from settings file, so that http/https proxy is set
// and let the command line args override them as necessary.
//Synthetic comment -- @@ -316,6 +317,17 @@
// mSdkLog.printf("    [%3d%%]\r", ((int)mValue) / 100);
}

/**
* Returns the current value of the progress bar,
* between 0 and up to {@link #setProgressMax(int)} - 1.
//Synthetic comment -- @@ -574,6 +586,17 @@
}

@Override
public boolean displayPrompt(String title, String message) {
return mRoot.displayPrompt(title, message);
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index cdf5e7b..7a7beea 100755

//Synthetic comment -- @@ -48,7 +48,9 @@
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.repository.sdkman2.PackageLoader;
import com.android.sdkuilib.internal.repository.sdkman2.SdkUpdaterWindowImpl2;
import com.android.sdkuilib.repository.ISdkChangeListener;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
//Synthetic comment -- @@ -58,12 +60,10 @@
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
//Synthetic comment -- @@ -77,6 +77,7 @@

private String mOsSdkRoot;

private final LocalSdkParser mLocalSdkParser = new LocalSdkParser();
private final SdkSources mSources = new SdkSources();
private final SettingsController mSettingsController;
//Synthetic comment -- @@ -103,17 +104,19 @@
*/
private ImageFactory mImageFactory;
private AndroidLocationException mAvdManagerInitError;

/**
* Creates a new updater data.
*
* @param sdkLog Logger. Cannot be null.
* @param osSdkRoot The OS path to the SDK root.
*/
    public UpdaterData(String osSdkRoot, ISdkLog sdkLog) {
mOsSdkRoot = osSdkRoot;
mSdkLog = sdkLog;


mSettingsController = initSettingsController();
initSdk();
//Synthetic comment -- @@ -136,6 +139,10 @@
return mDownloadCache;
}

public void setTaskFactory(ITaskFactory taskFactory) {
mTaskFactory = taskFactory;
}
//Synthetic comment -- @@ -585,54 +592,6 @@
}

/**
     * A comparator to sort all the {@link ArchiveInfo} based on their
     * dependency level. This forces the installer to install first all packages
     * with no dependency, then those with one level of dependency, etc.
     */
    private static class InstallOrderComparator implements Comparator<ArchiveInfo> {

        private final Map<ArchiveInfo, Integer> mOrders = new HashMap<ArchiveInfo, Integer>();

        @Override
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








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/SdkUpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/SdkUpdaterWindowImpl2.java
//Synthetic comment -- index 4376ec8..efa6d7c 100755

//Synthetic comment -- @@ -22,7 +22,6 @@
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.sources.SdkSourceProperties;
import com.android.sdkuilib.internal.repository.AboutDialog;
import com.android.sdkuilib.internal.repository.ISdkUpdaterWindow;
import com.android.sdkuilib.internal.repository.MenuBarWrapper;
import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.internal.repository.SettingsController.Settings;
//Synthetic comment -- @@ -36,6 +35,7 @@
import com.android.sdkuilib.internal.widgets.ImgDisabledButton;
import com.android.sdkuilib.internal.widgets.ToggleButton;
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.SdkUpdaterWindow.SdkInvocationContext;

//Synthetic comment -- @@ -64,7 +64,7 @@
* <p/>
* This window features only one embedded page, the combined installed+available package list.
*/
public class SdkUpdaterWindowImpl2 implements ISdkUpdaterWindow {

public static final String APP_NAME = "Android SDK Manager";
private static final String SIZE_POS_PREFIX = "sdkman2"; //$NON-NLS-1$
//Synthetic comment -- @@ -128,8 +128,7 @@
* Opens the window.
* @wbp.parser.entryPoint
*/
    @Override
    public void open() {
if (mParentShell == null) {
Display.setAppName(APP_NAME); //$hide$ (hide from SWT designer)
}
//Synthetic comment -- @@ -155,6 +154,7 @@
p.save();

dispose();  //$hide$
}

private void createShell() {
//Synthetic comment -- @@ -397,7 +397,6 @@
/**
* Adds a new listener to be notified when a change is made to the content of the SDK.
*/
    @Override
public void addListener(ISdkChangeListener listener) {
mUpdaterData.addListeners(listener);
}
//Synthetic comment -- @@ -406,7 +405,6 @@
* Removes a new listener to be notified anymore when a change is made to the content of
* the SDK.
*/
    @Override
public void removeListener(ISdkChangeListener listener) {
mUpdaterData.removeListener(listener);
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/TaskMonitorImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/tasks/TaskMonitorImpl.java
//Synthetic comment -- index 9a796b7..00054c4 100755

//Synthetic comment -- @@ -140,6 +140,17 @@
mUi.setProgress((int)mValue);
}

/**
* Returns the current value of the progress bar,
* between 0 and up to {@link #setProgressMax(int)} - 1.
//Synthetic comment -- @@ -319,6 +330,17 @@
}

@Override
public boolean displayPrompt(String title, String message) {
return mRoot.displayPrompt(title, message);
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/IInstallWorker.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/IInstallWorker.java
new file mode 100755
//Synthetic comment -- index 0000000..3838cdc

//Synthetic comment -- @@ -0,0 +1,24 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/SdkUpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/SdkUpdaterWindow.java
//Synthetic comment -- index 06fc387..b89a1fa 100755

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.android.sdkuilib.repository;

import com.android.sdklib.ISdkLog;
import com.android.sdkuilib.internal.repository.ISdkUpdaterWindow;
import com.android.sdkuilib.internal.repository.sdkman2.SdkUpdaterWindowImpl2;

import org.eclipse.swt.widgets.Shell;
//Synthetic comment -- @@ -30,7 +29,7 @@
public class SdkUpdaterWindow {

/** The actual window implementation to which this class delegates. */
    private ISdkUpdaterWindow mWindow;

/**
* Enum giving some indication of what is invoking this window.
//Synthetic comment -- @@ -106,7 +105,7 @@
/**
* Opens the window.
*/
    public void open() {
        mWindow.open();
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/MockUpdaterData.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/MockUpdaterData.java
//Synthetic comment -- index 0fc5b55..acd70be 100755

//Synthetic comment -- @@ -30,6 +30,7 @@
import com.android.sdklib.mock.MockLog;
import com.android.sdkuilib.internal.repository.SettingsController.Settings;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;

import org.eclipse.swt.graphics.Image;

//Synthetic comment -- @@ -48,7 +49,7 @@
private DownloadCache mMockDownloadCache;

public MockUpdaterData() {
        super(SDK_PATH, new MockLog());

setTaskFactory(new MockTaskFactory());
setImageFactory(new NullImageFactory());







