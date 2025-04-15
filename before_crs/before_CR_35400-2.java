/*SDK: Open AVD Manager from from AVD Selector > Manager.

SDK Bug: 29026

Change-Id:I036bcd7e23210ebf3991b56211c38c077ef3d308*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AvdManagerWindowImpl1.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AvdManagerWindowImpl1.java
//Synthetic comment -- index 8277ce2..74c061f 100755

//Synthetic comment -- @@ -58,6 +58,8 @@
private final AvdInvocationContext mContext;
/** Internal data shared between the window and its pages. */
private final UpdaterData mUpdaterData;

// --- UI members ---

//Synthetic comment -- @@ -82,6 +84,7 @@
mParentShell = parentShell;
mContext = context;
mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);
}

/**
//Synthetic comment -- @@ -102,6 +105,7 @@
mParentShell = parentShell;
mContext = context;
mUpdaterData = updaterData;
}

/**
//Synthetic comment -- @@ -146,13 +150,8 @@
mShell.addDisposeListener(new DisposeListener() {
@Override
public void widgetDisposed(DisposeEvent e) {
                ShellSizeAndPos.saveSizeAndPos(mShell, SIZE_POS_PREFIX);

                if (mContext != AvdInvocationContext.SDK_MANAGER) {
                    // When invoked from the sdk manager, don't dispose
                    // resources that the sdk manager still needs.
                    onAndroidSdkUpdaterDispose();    //$hide$ (hide from SWT designer)
                }
}
});

//Synthetic comment -- @@ -322,7 +321,7 @@
* Callback called when the window shell is disposed.
*/
private void onAndroidSdkUpdaterDispose() {
        if (mUpdaterData != null) {
ImageFactory imgFactory = mUpdaterData.getImageFactory();
if (imgFactory != null) {
imgFactory.dispose();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/SdkUpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/SdkUpdaterWindowImpl2.java
//Synthetic comment -- index ab0934a..febbf49 100755

//Synthetic comment -- @@ -566,7 +566,7 @@
AvdManagerWindowImpl1 win = new AvdManagerWindowImpl1(
mShell,
mUpdaterData,
                    AvdInvocationContext.SDK_MANAGER);

win.open();
} catch (Exception e) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 8c0057b..daacf10 100644

//Synthetic comment -- @@ -31,9 +31,9 @@
import com.android.sdklib.util.GrabProcessOutput.Wait;
import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressTask;
import com.android.sdkuilib.repository.SdkUpdaterWindow;
import com.android.sdkuilib.repository.SdkUpdaterWindow.SdkInvocationContext;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
//Synthetic comment -- @@ -345,7 +345,7 @@
mManagerButton.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                    onManager();
}
});
} else {
//Synthetic comment -- @@ -1007,7 +1007,7 @@
}
}

    private void onManager() {

// get the current Display
Display display = mTable.getDisplay();
//Synthetic comment -- @@ -1020,12 +1020,16 @@
log = new MessageBoxLog("Result of SDK Manager", display, true /*logErrorsOnly*/);
}

        SdkUpdaterWindow window = new SdkUpdaterWindow(
                mTable.getShell(),
                log,
                mAvdManager.getSdkManager().getLocation(),
                SdkInvocationContext.AVD_SELECTOR);
        window.open();
refresh(true /*reload*/); // UpdaterWindow uses its own AVD manager so this one must reload.

if (log instanceof MessageBoxLog) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/AvdManagerWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/AvdManagerWindow.java
//Synthetic comment -- index 5c601e6..5ad5fc2 100755

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.sdklib.ISdkLog;
import com.android.sdkuilib.internal.repository.sdkman2.AvdManagerWindowImpl1;

import org.eclipse.swt.widgets.Shell;

//Synthetic comment -- @@ -47,13 +48,14 @@
STANDALONE,

/**
         * The AVD Manager is invoked from the SDK Manager.
* This is similar to the {@link #STANDALONE} mode except we don't need
* to display a menu bar at all since we don't want a menu item linking
* back to the SDK Manager and we don't need to redisplay the options
* and about which are already on the root window.
*/
        SDK_MANAGER,

/**
* The AVD Manager is invoked from an IDE.







