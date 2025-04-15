/*SDK Manager: Fix dependencies when installing selected packages.

In the SDK Manager, when you select a specific package from an
available repository to install it, it used to not notice if there
was a broken depedency. This fixes it. It means we have to force
loading all known available repo sources to find them if necessary.

This CL fixes a minor issue: trim all URLs before trying to use
them. The CL is voluntarily a bit defensive for that. This can
happen when we load URLs from external addon sites which we do
not control.

Change-Id:I652e1fa0e74b4384d0e95819970a59519fafa05a*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/AndroidVersion.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/AndroidVersion.java
//Synthetic comment -- index 7674e76..33b8fd2 100644

//Synthetic comment -- @@ -239,6 +239,20 @@
}

/**
     * Returns a string with the API Level and optional codename.
     * Useful for debugging.
     * For display purpose, please use {@link #getApiString()} instead.
     */
    @Override
    public String toString() {
        String s = String.format("API %1$d", mApiLevel);        //$NON-NLS-1$
        if (isPreview()) {
            s += String.format(", %1$s preview", mCodename);    //$NON-NLS-1$
        }
        return s;
    }

    /**
* Compares this object with the specified object for order. Returns a
* negative integer, zero, or a positive integer as this object is less
* than, equal to, or greater than the specified object.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonsListFetcher.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonsListFetcher.java
//Synthetic comment -- index c40c7e4..fcef972 100755

//Synthetic comment -- @@ -64,7 +64,7 @@
private final String mUiName;

private Site(String url, String uiName) {
            mUrl = url.trim();
mUiName = uiName;
}

//Synthetic comment -- @@ -87,6 +87,9 @@
* @return An array of {@link Site} on success (possibly empty), or null on error.
*/
public Site[] fetch(ITaskMonitor monitor, String url) {

        url = url == null ? "" : url.trim();

monitor.setProgressMax(4);
monitor.setDescription("Fetching %1$s", url);
monitor.incProgress(1);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Archive.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Archive.java
//Synthetic comment -- index 3f2f966..8b61637 100755

//Synthetic comment -- @@ -202,7 +202,7 @@
mPackage = pkg;
mOs = os;
mArch = arch;
        mUrl = url == null ? null : url.trim();
mLocalOsPath = null;
mSize = size;
mChecksum = checksum;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSource.java
//Synthetic comment -- index db6067b..034854a 100755

//Synthetic comment -- @@ -73,6 +73,12 @@
*/
public SdkSource(String url, String uiName) {

        // URLs should not be null and should not have whitespace.
        if (url == null) {
            url = "";
        }
        url = url.trim();

// if the URL ends with a /, it must be "directory" resource,
// in which case we automatically add the default file that will
// looked for. This way it will be obvious to the user which








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/IUpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/IUpdaterData.java
new file mode 100755
//Synthetic comment -- index 0000000..8ec6596

//Synthetic comment -- @@ -0,0 +1,48 @@
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
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;

import org.eclipse.swt.widgets.Shell;


/**
 * Interface used to retrieve some parameters from an {@link UpdaterData} instance.
 * Useful mostly for unit tests purposes.
 */
interface IUpdaterData {

    public abstract ITaskFactory getTaskFactory();

    public abstract ISdkLog getSdkLog();

    public abstract ImageFactory getImageFactory();

    public abstract SdkManager getSdkManager();

    public abstract AvdManager getAvdManager();

    public abstract SettingsController getSettingsController();

    public abstract Shell getWindowShell();

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RemotePackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RemotePackagesPage.java
//Synthetic comment -- index d1f8a09..749365f 100755

//Synthetic comment -- @@ -365,6 +365,8 @@
InputDialog dlg = new InputDialog(getShell(), title, msg, null, new IInputValidator() {
public String isValid(String newText) {

                newText = newText == null ? null : newText.trim();

if (newText == null || newText.length() == 0) {
return "Error: URL field is empty. Please enter a URL.";
}
//Synthetic comment -- @@ -391,7 +393,7 @@
});

if (dlg.open() == Window.OK) {
            String url = dlg.getValue().trim();
mUpdaterData.getSources().add(
SdkSourceCategory.USER_ADDONS,
new SdkAddonSource(url, null/*uiName*/));








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 744bb8d..8d6a253 100755

//Synthetic comment -- @@ -57,7 +57,7 @@
/**
* Data shared between {@link UpdaterWindowImpl} and its pages.
*/
class UpdaterData implements IUpdaterData {
private String mOsSdkRoot;

private final ISdkLog mSdkLog;
//Synthetic comment -- @@ -617,7 +617,7 @@
refreshSources(true);
}

        UpdaterLogic ul = new UpdaterLogic(this);
ArrayList<ArchiveInfo> archives = ul.computeUpdates(
selectedArchives,
getSources(),
//Synthetic comment -- @@ -663,7 +663,7 @@

refreshSources(true);

        UpdaterLogic ul = new UpdaterLogic(this);
ArrayList<ArchiveInfo> archives = ul.computeUpdates(
null /*selectedArchives*/,
getSources(),








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java
//Synthetic comment -- index 56847bc..db5c99c 100755

//Synthetic comment -- @@ -26,6 +26,8 @@
import com.android.sdklib.internal.repository.IMinToolsDependency;
import com.android.sdklib.internal.repository.IPackageVersion;
import com.android.sdklib.internal.repository.IPlatformDependency;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.MinToolsPackage;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.PlatformPackage;
//Synthetic comment -- @@ -49,6 +51,12 @@
*/
class UpdaterLogic {

    private final IUpdaterData mUpdaterData;

    public UpdaterLogic(IUpdaterData updaterData) {
        mUpdaterData = updaterData;
    }

/**
* Compute which packages to install by taking the user selection
* and adding required packages as needed.
//Synthetic comment -- @@ -951,8 +959,21 @@
return;
}

        for (final SdkSource remoteSrc : remoteSources) {
Package[] pkgs = remoteSrc.getPackages();

            if (pkgs == null) {
                final boolean forceHttp = mUpdaterData.getSettingsController().getForceHttp();

                mUpdaterData.getTaskFactory().start("Loading Source", new ITask() {
                    public void run(ITaskMonitor monitor) {
                        remoteSrc.load(monitor, forceHttp);
                    }
                });

                pkgs = remoteSrc.getPackages();
            }

if (pkgs != null) {
nextPackage: for (Package pkg : pkgs) {
for (Archive a : pkg.getArchives()) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java
//Synthetic comment -- index 485619a..94fac06 100755

//Synthetic comment -- @@ -16,13 +16,20 @@

package com.android.sdkuilib.internal.repository;

import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.repository.Archive;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.MockAddonPackage;
import com.android.sdklib.internal.repository.MockPlatformPackage;
import com.android.sdklib.internal.repository.MockPlatformToolPackage;
import com.android.sdklib.internal.repository.MockToolPackage;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;

import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;
import java.util.Arrays;
//Synthetic comment -- @@ -31,10 +38,43 @@

public class UpdaterLogicTest extends TestCase {

    private static class NullUpdaterData implements IUpdaterData {

        public AvdManager getAvdManager() {
            return null;
        }

        public ImageFactory getImageFactory() {
            return null;
        }

        public ISdkLog getSdkLog() {
            return null;
        }

        public SdkManager getSdkManager() {
            return null;
        }

        public SettingsController getSettingsController() {
            return null;
        }

        public ITaskFactory getTaskFactory() {
            return null;
        }

        public Shell getWindowShell() {
            return null;
        }

    }

private static class MockUpdaterLogic extends UpdaterLogic {
private final Package[] mRemotePackages;

        public MockUpdaterLogic(IUpdaterData updaterData, Package[] remotePackages) {
            super(updaterData);
mRemotePackages = remotePackages;
}

//Synthetic comment -- @@ -55,7 +95,7 @@
* can find the base platform for a given addon.
*/
public void testFindAddonDependency() {
        MockUpdaterLogic mul = new MockUpdaterLogic(new NullUpdaterData(), null);

MockPlatformPackage p1 = new MockPlatformPackage(1, 1);
MockPlatformPackage p2 = new MockPlatformPackage(2, 1);
//Synthetic comment -- @@ -97,7 +137,7 @@
* tool package for a given platform package.
*/
public void testFindPlatformDependency() {
        MockUpdaterLogic mul = new MockUpdaterLogic(new NullUpdaterData(), null);

MockPlatformToolPackage pt1 = new MockPlatformToolPackage(1);

//Synthetic comment -- @@ -140,7 +180,7 @@
* platform-tool package for a given tool package.
*/
public void testFindPlatformToolDependency() {
        MockUpdaterLogic mul = new MockUpdaterLogic(new NullUpdaterData(), null);

MockPlatformToolPackage t1 = new MockPlatformToolPackage(1);
MockPlatformToolPackage t2 = new MockPlatformToolPackage(2);







