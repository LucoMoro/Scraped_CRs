/*SDK: Open AVD Manager from from AVD Selector > Manager.

SDK Bug: 29026

Change-Id:I036bcd7e23210ebf3991b56211c38c077ef3d308*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 8c0057b..50c4bc0 100644

//Synthetic comment -- @@ -30,10 +30,11 @@
import com.android.sdklib.util.GrabProcessOutput.IProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.Wait;
import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressTask;
import com.android.sdkuilib.repository.SdkUpdaterWindow;
import com.android.sdkuilib.repository.SdkUpdaterWindow.SdkInvocationContext;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
//Synthetic comment -- @@ -345,7 +346,7 @@
mManagerButton.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                    onManager();
}
});
} else {
//Synthetic comment -- @@ -1007,7 +1008,7 @@
}
}

    private void onManager() {

// get the current Display
Display display = mTable.getDisplay();
//Synthetic comment -- @@ -1020,12 +1021,16 @@
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







