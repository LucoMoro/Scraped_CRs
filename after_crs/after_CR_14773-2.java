/*Console-based "android update sdk"

Known limitations:
- This simulate exactly what the "update sdk" GUI does,
  namely it suggests the latest platform to install and
  updates older packages.
- It respects the default source URLs (e.g. the getenv stuff)
- there are a few options to configure behavior such as
  selecting obsolete packages and a dry-mode.
- As such, this is mostly a one-shot update step and probably
  lacks configuration that a script-based updater would want.

Non-goals:
- No way to specifically request a given package to be installed.
- No way to configure the output (e.g. the install progress might
  be annoying if the output is piped for a script)

SDK Bug 2404640

Change-Id:If0512558f048e88f2b216e34ed33aa550ff2610c*/




//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index f0baa31..4d92467 100644

//Synthetic comment -- @@ -31,9 +31,11 @@
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectCreator.OutputLevel;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.repository.SdkRepository;
import com.android.sdklib.xml.AndroidXPathFactory;
import com.android.sdkmanager.internal.repository.AboutPage;
import com.android.sdkmanager.internal.repository.SettingsPage;
import com.android.sdkuilib.internal.repository.UpdateNoWindow;
import com.android.sdkuilib.internal.repository.LocalPackagesPage;
import com.android.sdkuilib.internal.widgets.MessageBoxLog;
import com.android.sdkuilib.repository.UpdaterWindow;
//Synthetic comment -- @@ -45,6 +47,8 @@
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
                if (mSdkCommandLine.getFlagNoUI()) {
                    updateSdkNoUI();
                } else {
                    showMainWindow(true /*autoUpdate*/);
                }
} else if (SdkCommandLine.OBJECT_ADB.equals(directObject)) {
updateAdb();
}
//Synthetic comment -- @@ -288,6 +305,47 @@
}

/**
     * Updates the whole SDK without any UI, just using console output.
     */
    private void updateSdkNoUI() {
        boolean force = mSdkCommandLine.getFlagForce();
        boolean useHttp = mSdkCommandLine.getFlagNoHttps();
        boolean dryMode = mSdkCommandLine.getFlagDryMode();
        boolean obsolete = mSdkCommandLine.getFlagObsolete();

        // Check filter types.
        ArrayList<String> pkgFilter = new ArrayList<String>();
        String filter = mSdkCommandLine.getParamFilter();
        if (filter != null && filter.length() > 0) {
            for (String t : filter.split(",")) {    //$NON-NLS-1$
                if (t != null) {
                    t = t.trim();
                    if (t.length() > 0) {
                        boolean found = false;
                        for (String t2 : SdkRepository.NODES) {
                            if (t2.equals(t)) {
                                pkgFilter.add(t2);
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            errorAndExit(
                                "Unknown package filter type '%1$s'.\nAccepted values are: %2$s",
                                t,
                                Arrays.toString(SdkRepository.NODES));
                            return;
                        }
                    }
                }
            }
        }

        UpdateNoWindow upd = new UpdateNoWindow(mOsSdkFolder, mSdkManager, mSdkLog, force, useHttp);
        upd.updateAll(pkgFilter, obsolete, dryMode);
    }

    /**
* Creates a new Android project based on command-line parameters
*/
private void createProject(boolean library) {








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java b/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java
//Synthetic comment -- index 9b1425e..d4da959 100644

//Synthetic comment -- @@ -18,6 +18,9 @@

import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkManager;
import com.android.sdklib.repository.SdkRepository;

import java.util.Arrays;


/**
//Synthetic comment -- @@ -68,6 +71,10 @@
public static final String KEY_RENAME       = "rename";
public static final String KEY_SUBPROJECTS  = "subprojects";
public static final String KEY_MAIN_PROJECT = "main";
    public static final String KEY_NO_UI        = "no-ui";
    public static final String KEY_NO_HTTPS     = "no-https";
    public static final String KEY_DRY_MODE     = "dry-mode";
    public static final String KEY_OBSOLETE     = "obsolete";

/**
* Action definitions for SdkManager command line.
//Synthetic comment -- @@ -175,6 +182,36 @@
VERB_UPDATE, OBJECT_AVD, "n", KEY_NAME,
"Name of the AVD to update", null);

        // --- update sdk ---

        define(Mode.BOOLEAN, false,
                VERB_UPDATE, OBJECT_SDK, "u", KEY_NO_UI,
                "Update from command-line, without any UI", false);

        define(Mode.BOOLEAN, false,
                VERB_UPDATE, OBJECT_SDK, "s", KEY_NO_HTTPS,
                "Use HTTP instead of the default HTTPS for downloads", false);

        define(Mode.BOOLEAN, false,
                VERB_UPDATE, OBJECT_SDK, "f", KEY_FORCE,
                "Force replacing things that have been modified (samples, adb)", false);

        define(Mode.STRING, false,
                VERB_UPDATE, OBJECT_SDK, "t", KEY_FILTER,
                "A coma-separated list of " + Arrays.toString(SdkRepository.NODES) +
                " to limit update to specified types of packages",
                null);

        define(Mode.BOOLEAN, false,
                VERB_UPDATE, OBJECT_SDK, "o", KEY_OBSOLETE,
                "Install obsolete packages",
                false);

        define(Mode.BOOLEAN, false,
                VERB_UPDATE, OBJECT_SDK, "n", KEY_DRY_MODE,
                "Only simulates what would be updated but does not download/install anything",
                false);

// --- create project ---

/* Disabled for ADT 0.9 / Cupcake SDK 1.5_r1 release. [bug #1795718].
//Synthetic comment -- @@ -363,4 +400,32 @@
public String getParamTestProjectMain() {
return ((String) getValue(null, null, KEY_MAIN_PROJECT));
}


    // -- some helpers for update sdk flags

    /** Helper to retrieve the --force flag. */
    public boolean getFlagNoUI() {
        return ((Boolean) getValue(null, null, KEY_NO_UI)).booleanValue();
    }

    /** Helper to retrieve the --no-https flag. */
    public boolean getFlagNoHttps() {
        return ((Boolean) getValue(null, null, KEY_NO_HTTPS)).booleanValue();
    }

    /** Helper to retrieve the --dry-mode flag. */
    public boolean getFlagDryMode() {
        return ((Boolean) getValue(null, null, KEY_DRY_MODE)).booleanValue();
    }

    /** Helper to retrieve the --obsolete flag. */
    public boolean getFlagObsolete() {
        return ((Boolean) getValue(null, null, KEY_OBSOLETE)).booleanValue();
    }

    /** Helper to retrieve the --filter value. */
    public String getParamFilter() {
        return ((String) getValue(null, null, KEY_FILTER));
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Archive.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Archive.java
//Synthetic comment -- index 285262d..21cae7a 100755

//Synthetic comment -- @@ -42,7 +42,7 @@
* A package has some attributes (revision, description) and a list of archives
* which represent the downloadable bits.
* <p/>
 * Packages are offered by a {@link RepoSource} (a download site).
*/
public class Archive implements IDescription {









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkRepository.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkRepository.java
//Synthetic comment -- index d59fc9e..49e2b2e 100755

//Synthetic comment -- @@ -61,6 +61,17 @@
/** An extra package. */
public static final String NODE_EXTRA    = "extra";                         //$NON-NLS-1$

    // Warning: if you edit this list, please also update the package-to-class map
    // com.android.sdkuilib.internal.repository.UpdaterData.updateOrInstallAll_NoGUI().
    public static final String[] NODES = {
        NODE_PLATFORM,
        NODE_ADD_ON,
        NODE_TOOL,
        NODE_DOC,
        NODE_SAMPLE,
        NODE_EXTRA
    };

/** The license definition. */
public static final String NODE_LICENSE       = "license";                  //$NON-NLS-1$
/** The optional uses-license for all packages or for a lib. */








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/LocalPackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/LocalPackagesPage.java
//Synthetic comment -- index c482042..f8a23bb 100755

//Synthetic comment -- @@ -256,7 +256,7 @@
}

private void onUpdateSelected() {
        mUpdaterData.updateOrInstallAll_WithGUI(null /*selectedArchives*/);
}

private void onDeleteSelected() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RemotePackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RemotePackagesPage.java
//Synthetic comment -- index b687f5c..defcd77 100755

//Synthetic comment -- @@ -320,7 +320,7 @@
}

if (mUpdaterData != null) {
            mUpdaterData.updateOrInstallAll_WithGUI(archives);
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsController.java
//Synthetic comment -- index 87f0544..e2e503e 100755

//Synthetic comment -- @@ -122,7 +122,7 @@
/**
* Internal helper to set a boolean setting.
*/
    void setSetting(String key, boolean value) {
mProperties.setProperty(key, Boolean.toString(value));
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateNoWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdateNoWindow.java
new file mode 100755
//Synthetic comment -- index 0000000..be8f841

//Synthetic comment -- @@ -0,0 +1,338 @@
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

package com.android.sdkuilib.internal.repository;

import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.repository.SdkRepository;

import java.util.ArrayList;

/**
 * Performs an update using only a non-interactive console output with no GUI.
 * <p/>
 * TODO: It may be useful in the future to let the filter specify packages names
 * rather than package types, typically to let the user upgrade to a new platform.
 * This can be achieved easily by simply allowing package names in the pkgFilter
 * argument.
 */
public class UpdateNoWindow {

    /** The {@link UpdaterData} to use. */
    private final UpdaterData mUpdaterData;
    /** The {@link ISdkLog} logger to use. */
    private final ISdkLog mSdkLog;
    /** The reply to any question asked by the update process. Currently this will
     *   be yes/no for ability to replace modified samples or restart ADB. */
    private final boolean mForce;

    /**
     * Creates an UpdateNoWindow object that will update using the given SDK root
     * and outputs to the given SDK logger.
     *
     * @param osSdkRoot The OS path of the SDK folder to update.
     * @param sdkManager An existing SDK manager to list current platforms and addons.
     * @param sdkLog A logger object, that should ideally output to a write-only console.
     * @param force The reply to any question asked by the update process. Currently this will
     *   be yes/no for ability to replace modified samples or restart ADB.
     * @param useHttp True to force using HTTP instead of HTTPS for downloads.
     */
    public UpdateNoWindow(String osSdkRoot,
            SdkManager sdkManager,
            ISdkLog sdkLog,
            boolean force,
            boolean useHttp) {
        mSdkLog = sdkLog;
        mForce = force;
        mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);

        // Change the in-memory settings to force the http/https mode
        mUpdaterData.getSettingsController().setSetting(ISettingsPage.KEY_FORCE_HTTP, useHttp);

        // Use a factory that only outputs to the given ISdkLog.
        mUpdaterData.setTaskFactory(new ConsoleTaskFactory());

        // Check that the AVD Manager has been correctly initialized. This is done separately
        // from the constructor in the GUI-based UpdaterWindowImpl to give time to the UI to
        // initialize before displaying a message box. Since we don't have any GUI here
        // we can call it whenever we want.
        if (mUpdaterData.checkIfInitFailed()) {
            return;
        }

        // Setup the default sources including the getenv overrides.
        mUpdaterData.setupDefaultSources();

        mUpdaterData.getLocalSdkParser().parseSdk(osSdkRoot, sdkManager, sdkLog);

    }

    /**
     * Performs the actual update.
     *
     * @param pkgFilter A list of {@link SdkRepository#NODES} to limit the type of packages
     *   we can update. A null or empty list means to update everything possible.
     * @param includeObsoletes True to also list and install obsolete packages.
     * @param dryMode True to check what would be updated/installed but do not actually
     *   download or install anything.
     */
    public void updateAll(
            ArrayList<String> pkgFilter,
            boolean includeObsoletes,
            boolean dryMode) {
        mUpdaterData.updateOrInstallAll_NoGUI(pkgFilter, includeObsoletes, dryMode);
    }

    /**
     * A custom implementation of {@link ITaskFactory} that provides {@link ConsoleTask} objects.
     */
    private class ConsoleTaskFactory implements ITaskFactory {
        public void start(String title, ITask task) {
            new ConsoleTask(title, task);
        }
    }

    /**
     * A custom implementation of {@link ITaskMonitor} that defers all output to the
     * super {@link UpdateNoWindow#mSdkLog}.
     */
    private class ConsoleTask implements ITaskMonitor {

        private static final double MAX_COUNT = 10000.0;
        private double mIncCoef = 0;
        private double mValue = 0;
        private String mLastDesc = null;
        private String mLastProgressBase = null;

        /**
         * Creates a new {@link ConsoleTask} with the given title.
         */
        public ConsoleTask(String title, ITask task) {
            mSdkLog.printf("%s:\n", title);
            task.run(this);
        }

        /**
         * Sets the description in the current task dialog.
         */
        public void setDescription(String descriptionFormat, Object...args) {

            String last = mLastDesc;
            String line = String.format("  " + descriptionFormat, args);

            // If the description contains a %, it generally indicates a recurring
            // progress so we want a \r at the end.
            if (line.indexOf('%') > -1) {
                if (mLastProgressBase != null && line.startsWith(mLastProgressBase)) {
                    line = "    " + line.substring(mLastProgressBase.length());
                }
                line += "\r";
            } else {
                mLastProgressBase = line;
                line += "\n";
            }

            // Skip line if it's the same as the last one.
            if (last != null && last.equals(line)) {
                return;
            }
            mLastDesc = line;

            // If the last line terminated with a \r but the new one doesn't, we need to
            // insert a \n to avoid erasing the previous line.
            if (last != null &&
                    last.endsWith("\r") &&
                    !line.endsWith("\r")) {
                line = "\n" + line;
            }

            mSdkLog.printf("%s", line);
        }

        /**
         * Sets the description in the current task dialog.
         */
        public void setResult(String resultFormat, Object...args) {
            setDescription(resultFormat, args);
        }

        /**
         * Sets the max value of the progress bar.
         *
         * Weird things will happen if setProgressMax is called multiple times
         * *after* {@link #incProgress(int)}: we don't try to adjust it on the
         * fly.
         */
        public void setProgressMax(int max) {
            assert max > 0;
            // Always set the dialog's progress max to 10k since it only handles
            // integers and we want to have a better inner granularity. Instead
            // we use the max to compute a coefficient for inc deltas.
            mIncCoef = max > 0 ? MAX_COUNT / max : 0;
            assert mIncCoef > 0;
        }

        /**
         * Increments the current value of the progress bar.
         */
        public void incProgress(int delta) {
            assert mIncCoef > 0;
            assert delta > 0;
            internalIncProgress(delta * mIncCoef);
        }

        private void internalIncProgress(double realDelta) {
            mValue += realDelta;
            // max value is 10k, so 10k/100 == 100%.
            // Experimentation shows that it is not really useful to display this
            // progression since during download the description line will change.
            // mSdkLog.printf("    [%3d%%]\r", ((int)mValue) / 100);
        }

        /**
         * Returns the current value of the progress bar,
         * between 0 and up to {@link #setProgressMax(int)} - 1.
         */
        public int getProgress() {
            assert mIncCoef > 0;
            return mIncCoef > 0 ? (int)(mValue / mIncCoef) : 0;
        }

        /**
         * Returns true if the "Cancel" button was selected.
         */
        public boolean isCancelRequested() {
            return false;
        }

        /**
         * Display a yes/no question dialog box.
         *
         * This implementation allow this to be called from any thread, it
         * makes sure the dialog is opened synchronously in the ui thread.
         *
         * @param title The title of the dialog box
         * @param message The error message
         * @return true if YES was clicked.
         */
        public boolean displayPrompt(final String title, final String message) {
            // TODO Make it interactive if mForce==false
            mSdkLog.printf("\n%s\n%s\n[y/n] => %s\n",
                    title,
                    message,
                    mForce ? "yes" : "no (use --force to override)");
            return mForce;
        }

        /**
         * Creates a sub-monitor that will use up to tickCount on the progress bar.
         * tickCount must be 1 or more.
         */
        public ITaskMonitor createSubMonitor(int tickCount) {
            assert mIncCoef > 0;
            assert tickCount > 0;
            return new ConsoleSubTaskMonitor(this, null, mValue, tickCount * mIncCoef);
        }
    }

    private interface IConsoleSubTaskMonitor extends ITaskMonitor {
        public void subIncProgress(double realDelta);
    }

    private static class ConsoleSubTaskMonitor implements IConsoleSubTaskMonitor {

        private final ConsoleTask mRoot;
        private final IConsoleSubTaskMonitor mParent;
        private final double mStart;
        private final double mSpan;
        private double mSubValue;
        private double mSubCoef;

        /**
         * Creates a new sub task monitor which will work for the given range [start, start+span]
         * in its parent.
         *
         * @param root The ProgressTask root
         * @param parent The immediate parent. Can be the null or another sub task monitor.
         * @param start The start value in the root's coordinates
         * @param span The span value in the root's coordinates
         */
        public ConsoleSubTaskMonitor(ConsoleTask root,
                IConsoleSubTaskMonitor parent,
                double start,
                double span) {
            mRoot = root;
            mParent = parent;
            mStart = start;
            mSpan = span;
            mSubValue = start;
        }

        public boolean isCancelRequested() {
            return mRoot.isCancelRequested();
        }

        public void setDescription(String descriptionFormat, Object... args) {
            mRoot.setDescription(descriptionFormat, args);
        }

        public void setResult(String resultFormat, Object... args) {
            mRoot.setResult(resultFormat, args);
        }

        public void setProgressMax(int max) {
            assert max > 0;
            mSubCoef = max > 0 ? mSpan / max : 0;
            assert mSubCoef > 0;
        }

        public int getProgress() {
            assert mSubCoef > 0;
            return mSubCoef > 0 ? (int)((mSubValue - mStart) / mSubCoef) : 0;
        }

        public void incProgress(int delta) {
            assert mSubCoef > 0;
            subIncProgress(delta * mSubCoef);
        }

        public void subIncProgress(double realDelta) {
            mSubValue += realDelta;
            if (mParent != null) {
                mParent.subIncProgress(realDelta);
            } else {
                mRoot.internalIncProgress(realDelta);
            }
        }

        public boolean displayPrompt(String title, String message) {
            return mRoot.displayPrompt(title, message);
        }

        public ITaskMonitor createSubMonitor(int tickCount) {
            assert mSubCoef > 0;
            assert tickCount > 0;
            return new ConsoleSubTaskMonitor(mRoot,
                    this,
                    mSubValue,
                    tickCount * mSubCoef);
        }
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index c14be7e..a45896f 100755

//Synthetic comment -- @@ -23,14 +23,19 @@
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
import com.android.sdklib.internal.repository.RepoSource;
import com.android.sdklib.internal.repository.RepoSources;
import com.android.sdklib.internal.repository.SamplePackage;
import com.android.sdklib.internal.repository.ToolPackage;
import com.android.sdklib.repository.SdkRepository;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.repository.UpdaterWindow.ISdkListener;

//Synthetic comment -- @@ -42,7 +47,9 @@
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
* Data shared between {@link UpdaterWindowImpl} and its pages.
//Synthetic comment -- @@ -185,15 +192,22 @@
example = "~";                 //$NON-NLS-1$
}

            String error = String.format(
                "The AVD manager normally uses the user's profile directory to store " +
                "AVD files. However it failed to find the default profile directory. " +
                "\n" +
                "To fix this, please set the environment variable ANDROID_SDK_HOME to " +
                "a valid path such as \"%s\".",
                example);

            // We may not have any UI. Only display a dialog if there's a window shell available.
            if (mWindowShell != null) {
                MessageDialog.openError(mWindowShell,
                    "Android Virtual Devices Manager",
                    error);
            } else {
                mSdkLog.error(null /* Throwable */, "%s", error);  //$NON-NLS-1$
            }

return true;
}
//Synthetic comment -- @@ -222,17 +236,16 @@
mAvdManagerInitError = e;
}

// notify listeners.
notifyListeners(false /*init*/);
}

/**
* Reloads the SDK content (targets).
     * <p/>
     * This also reloads the AVDs in case their status changed.
     * <p/>
     * This does not notify the listeners ({@link ISdkListener}).
*/
public void reloadSdk() {
// reload SDK
//Synthetic comment -- @@ -257,7 +270,8 @@

/**
* Reloads the AVDs.
     * <p/>
     * This does not notify the listeners.
*/
public void reloadAvds() {
// reload AVDs
//Synthetic comment -- @@ -271,6 +285,52 @@
}

/**
     * Sets up the default sources: <br/>
     * - the default google SDK repository, <br/>
     * - the extra repo URLs from the environment, <br/>
     * - the user sources from prefs <br/>
     * - and finally the extra user repo URLs from the environment.
     */
    public void setupDefaultSources() {
        RepoSources sources = getSources();
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
        sources.loadUserSources(getSdkLog());

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
    }

    /**
* Returns the list of installed packages, parsing them if this has not yet been done.
*/
public Package[] getInstalledPackage() {
//Synthetic comment -- @@ -288,7 +348,9 @@

/**
* Notify the listeners ({@link ISdkListener}) that the SDK was reloaded.
     * <p/>
     * This can be called from any thread.
     *
* @param init whether the SDK loaded for the first time.
*/
public void notifyListeners(final boolean init) {
//Synthetic comment -- @@ -458,22 +520,23 @@
}

/**
     * Attempts to restart ADB.
     * <p/>
* If the "ask before restart" setting is set (the default), prompt the user whether
* now is a good time to restart ADB.
     *
* @param monitor
*/
private void askForAdbRestart(ITaskMonitor monitor) {
final boolean[] canRestart = new boolean[] { true };

        if (getWindowShell() != null && getSettingsController().getAskBeforeAdbRestart()) {
// need to ask for permission first
            Display display = getWindowShell().getDisplay();

display.syncExec(new Runnable() {
public void run() {
                    canRestart[0] = MessageDialog.openQuestion(getWindowShell(),
"ADB Restart",
"A package that depends on ADB has been updated. It is recommended " +
"to restart ADB. Is it OK to do it now? If not, you can restart it " +
//Synthetic comment -- @@ -490,11 +553,16 @@
}

private void notifyToolsNeedsToBeRestarted() {
        if (getWindowShell() == null) {
            // We don't need to print anything if this is a standalone console update.
            return;
        }

        Display display = getWindowShell().getDisplay();

display.syncExec(new Runnable() {
public void run() {
                MessageDialog.openInformation(getWindowShell(),
"Android Tools Updated",
"The Android SDK and AVD Manager that you are currently using has been updated. " +
"It is recommended that you now close the manager window and re-open it. " +
//Synthetic comment -- @@ -507,6 +575,7 @@

/**
* Tries to update all the *existing* local packages.
     * This version *requires* to be run with a GUI.
* <p/>
* There are two modes of operation:
* <ul>
//Synthetic comment -- @@ -517,11 +586,11 @@
* the user wants to install or update, so just process these.
* </ul>
*
     * @param selectedArchives The list of remote archives to consider for the update.
*  This can be null, in which case a list of remote archive is fetched from all
*  available sources.
*/
    public void updateOrInstallAll_WithGUI(Collection<Archive> selectedArchives) {
if (selectedArchives == null) {
refreshSources(true);
}
//Synthetic comment -- @@ -536,7 +605,7 @@
ul.addNewPlatforms(archives, getSources(), getLocalSdkParser().getPackages());
}

        // TODO if selectedArchives is null and archives.len==0, find if there are
// any new platform we can suggest to install instead.

UpdateChooserDialog dialog = new UpdateChooserDialog(getWindowShell(), this, archives);
//Synthetic comment -- @@ -547,6 +616,140 @@
installArchives(result);
}
}

    /**
     * Tries to update all the *existing* local packages.
     * This version is intended to run without a GUI and
     * only outputs to the current {@link ISdkLog}.
     *
     * @param pkgFilter A list of {@link SdkRepository#NODES} to limit the type of packages
     *   we can update. A null or empty list means to update everything possible.
     * @param includeObsoletes True to also list and install obsolete packages.
     * @param dryMode True to check what would be updated/installed but do not actually
     *   download or install anything.
     */
    public void updateOrInstallAll_NoGUI(
            Collection<String> pkgFilter,
            boolean includeObsoletes,
            boolean dryMode) {

        refreshSources(true);

        UpdaterLogic ul = new UpdaterLogic();
        ArrayList<ArchiveInfo> archives = ul.computeUpdates(
                null /*selectedArchives*/,
                getSources(),
                getLocalSdkParser().getPackages());

        ul.addNewPlatforms(archives, getSources(), getLocalSdkParser().getPackages());

        // Filter the selected archives to only keep the ones matching the filter
        if (pkgFilter != null && pkgFilter.size() > 0 && archives != null && archives.size() > 0) {
            // Map filter types to an SdkRepository Package type.
            HashMap<String, Class<? extends Package>> pkgMap =
                new HashMap<String, Class<? extends Package>>();
            pkgMap.put(SdkRepository.NODE_PLATFORM, PlatformPackage.class);
            pkgMap.put(SdkRepository.NODE_ADD_ON,   AddonPackage.class);
            pkgMap.put(SdkRepository.NODE_TOOL,     ToolPackage.class);
            pkgMap.put(SdkRepository.NODE_DOC,      DocPackage.class);
            pkgMap.put(SdkRepository.NODE_SAMPLE,   SamplePackage.class);
            pkgMap.put(SdkRepository.NODE_EXTRA,    ExtraPackage.class);

            if (SdkRepository.NODES.length != pkgMap.size()) {
                // Sanity check in case we forget to update this package map.
                // We don't cancel the operation though.
                mSdkLog.error(null,
                    "Filter Mismatch!\nThe package filter list has changed. Please report this.");
            }

            // Now make a set of the types that are allowed by the filter.
            HashSet<Class<? extends Package>> allowedPkgSet =
                new HashSet<Class<? extends Package>>();
            for (String type : pkgFilter) {
                if (pkgMap.containsKey(type)) {
                    allowedPkgSet.add(pkgMap.get(type));
                } else {
                    // This should not happen unless there's a mismatch in the package map.
                    mSdkLog.error(null, "Ignoring unknown package filter '%1$s'", type);
                }
            }

            // we don't need the map anymore
            pkgMap = null;

            Iterator<ArchiveInfo> it = archives.iterator();
            while (it.hasNext()) {
                boolean keep = false;
                ArchiveInfo ai = it.next();
                Archive a = ai.getNewArchive();
                if (a != null) {
                    Package p = a.getParentPackage();
                    if (p != null && allowedPkgSet.contains(p.getClass())) {
                        keep = true;
                    }
                }

                if (!keep) {
                    it.remove();
                }
            }

            if (archives.size() == 0) {
                mSdkLog.warning("The package filter removed all packages. There is nothing to install.\n" +
                        "Please consider trying updating again without a package filter.");
                return;
            }
        }

        if (!includeObsoletes && archives != null && archives.size() > 0) {
            // Filter obsolete packages out
            Iterator<ArchiveInfo> it = archives.iterator();
            while (it.hasNext()) {
                boolean keep = false;
                ArchiveInfo ai = it.next();
                Archive a = ai.getNewArchive();
                if (a != null) {
                    Package p = a.getParentPackage();
                    if (p != null && !p.isObsolete()) {
                        keep = true;
                    }
                }

                if (!keep) {
                    it.remove();
                }
            }

            if (archives.size() == 0) {
                mSdkLog.warning("All candidate packages were obsolete. Nothing to install.");
                return;
            }
        }

        // TODO if selectedArchives is null and archives.len==0, find if there are
        // any new platform we can suggest to install instead.

        if (archives != null && archives.size() > 0) {
            if (dryMode) {
                mSdkLog.printf("Packages selected for install:\n");
                for (ArchiveInfo ai : archives) {
                    Archive a = ai.getNewArchive();
                    if (a != null) {
                        Package p = a.getParentPackage();
                        if (p != null) {
                            mSdkLog.printf("- %1$s\n", p.getShortDescription());
                        }
                    }
                }
                mSdkLog.printf("\nDry mode is on so nothing will actually be installed.\n");
            } else {
                installArchives(archives);
            }
        } else {
            mSdkLog.printf("There is nothing to install or update.\n");
        }
    }

/**
* Refresh all sources. This is invoked either internally (reusing an existing monitor)
* or as a UI callback on the remote page "Refresh" button (in which case the monitor is
//Synthetic comment -- @@ -560,7 +763,7 @@

final boolean forceHttp = getSettingsController().getForceHttp();

        mTaskFactory.start("Refresh Sources", new ITask() {
public void run(ITaskMonitor monitor) {
RepoSource[] sources = mSources.getSources();
monitor.setProgressMax(sources.length);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java
//Synthetic comment -- index eda46e5..f7c9a96 100755

//Synthetic comment -- @@ -19,9 +19,6 @@

import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressTaskFactory;
import com.android.sdkuilib.repository.UpdaterWindow.ISdkListener;
//Synthetic comment -- @@ -295,7 +292,7 @@
mUpdaterData.notifyListeners(true /*init*/);

if (mRequestAutoUpdate) {
            mUpdaterData.updateOrInstallAll_WithGUI(null /*selectedArchives*/);
}

return true;
//Synthetic comment -- @@ -395,43 +392,7 @@
* Used to initialize the sources.
*/
private void setupSources() {
        mUpdaterData.setupDefaultSources();
mRemotePackagesPage.onSdkChange(false /*init*/);
}








