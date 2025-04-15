/*SDK Manager: fix link to AVD Manager.

The AVD Manager still had remnants of the obsolete
"pages" mechanism removed in changeI7f4f3941.
That was making it crash with an NPE when started
from the SDK Manager.

SDK Bug: 6272923

Change-Id:I1f010c016e1db0e884aea7beda23ead6f47df70e*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AboutDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AboutDialog.java
//Synthetic comment -- index ba87300..35e3420 100755

//Synthetic comment -- @@ -70,7 +70,6 @@
SdkAddonConstants.NS_LATEST_VERSION,
SdkRepoConstants.NS_LATEST_VERSION));

Label filler = new Label(shell, SWT.NONE);
GridDataBuilder.create(filler).fill().grab().hSpan(2);









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ISdkUpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ISdkUpdaterWindow.java
//Synthetic comment -- index 6bf8684..e5f2521 100755

//Synthetic comment -- @@ -18,43 +18,12 @@

import com.android.sdkuilib.repository.ISdkChangeListener;

/**
* Interface for the actual implementation of the Update Window.
*/
public interface ISdkUpdaterWindow {

/**
* Adds a new listener to be notified when a change is made to the content of the SDK.
*/
public abstract void addListener(ISdkChangeListener listener);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsDialog.java
//Synthetic comment -- index f145453..a90002c 100755

//Synthetic comment -- @@ -122,6 +122,9 @@
"to restart ADB after updating an addon-on package or a tool package.");
mAskAdbRestartCheck.addSelectionListener(mApplyOnSelected);

        Label filler = new Label(shell, SWT.NONE);
        GridDataBuilder.create(filler).hFill().hGrab();

createCloseButton();
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AvdManagerWindowImpl1.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AvdManagerWindowImpl1.java
//Synthetic comment -- index 23fde3f..8277ce2 100755

//Synthetic comment -- @@ -20,21 +20,16 @@
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdkuilib.internal.repository.AboutDialog;
import com.android.sdkuilib.internal.repository.MenuBarWrapper;
import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.internal.repository.SettingsDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.repository.sdkman1.AvdManagerPage;
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.SdkUpdaterWindow;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
//Synthetic comment -- @@ -44,16 +39,11 @@
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
* This is an intermediate version of the {@link AvdManagerPage}
* wrapped in its own standalone window for use from the SDK Manager 2.
//Synthetic comment -- @@ -68,11 +58,6 @@
private final AvdInvocationContext mContext;
/** Internal data shared between the window and its pages. */
private final UpdaterData mUpdaterData;

// --- UI members ---

//Synthetic comment -- @@ -227,12 +212,14 @@
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
//Synthetic comment -- @@ -256,46 +243,6 @@

// --- Public API -----------

/**
* Adds a new listener to be notified when a change is made to the content of the SDK.
*/
//Synthetic comment -- @@ -342,15 +289,6 @@

mUpdaterData.broadcastOnSdkLoaded();

return true;
}

//Synthetic comment -- @@ -411,26 +349,6 @@
mSettingsController.applySettings();
}

private void onSdkManager() {
ITaskFactory oldFactory = mUpdaterData.getTaskFactory();

//Synthetic comment -- @@ -440,10 +358,6 @@
mUpdaterData,
SdkUpdaterWindow.SdkInvocationContext.AVD_MANAGER);

win.open();
} catch (Exception e) {
mUpdaterData.getSdkLog().error(e, "SDK Manager window error");
//Synthetic comment -- @@ -451,80 +365,4 @@
mUpdaterData.setTaskFactory(oldFactory);
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/SdkUpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/SdkUpdaterWindowImpl2.java
//Synthetic comment -- index 135c6bd..e5f26cd 100755

//Synthetic comment -- @@ -26,8 +26,6 @@
import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.internal.repository.SettingsDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.repository.sdkman2.PackagesPage.MenuAction;
import com.android.sdkuilib.internal.tasks.ILogUiProvider;
//Synthetic comment -- @@ -38,7 +36,6 @@
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.SdkUpdaterWindow.SdkInvocationContext;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
//Synthetic comment -- @@ -59,8 +56,6 @@
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

/**
* This is the private implementation of the UpdateWindow
* for the second version of the SDK Manager.
//Synthetic comment -- @@ -76,11 +71,6 @@
private final SdkInvocationContext mContext;
/** Internal data shared between the window and its pages. */
private final UpdaterData mUpdaterData;

// --- UI members ---

//Synthetic comment -- @@ -391,49 +381,6 @@

// --- Public API -----------

/**
* Adds a new listener to be notified when a change is made to the content of the SDK.
*/
//Synthetic comment -- @@ -532,15 +479,6 @@

mUpdaterData.broadcastOnSdkLoaded();

// Tell the one page its the selected one
mPkgPage.onPageSelected();

//Synthetic comment -- @@ -626,10 +564,6 @@
mUpdaterData,
AvdInvocationContext.SDK_MANAGER);

win.open();
} catch (Exception e) {
mUpdaterData.getSdkLog().error(e, "AVD Manager window error");








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/AvdManagerWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/AvdManagerWindow.java
//Synthetic comment -- index 73be6a2..5c601e6 100755

//Synthetic comment -- @@ -17,10 +17,8 @@
package com.android.sdkuilib.repository;

import com.android.sdklib.ISdkLog;
import com.android.sdkuilib.internal.repository.sdkman2.AvdManagerWindowImpl1;

import org.eclipse.swt.widgets.Shell;

/**
//Synthetic comment -- @@ -88,22 +86,6 @@
}

/**
* Opens the window.
*/
public void open() {







