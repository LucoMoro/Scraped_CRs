/*Add menu item for configuring and creating devices

Change-Id:I39e4747b818d0163d8f51c29d37424078580f247*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AvdManagerWindowImpl1.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AvdManagerWindowImpl1.java
//Synthetic comment -- index 5fa4020..c84506b 100755

//Synthetic comment -- @@ -19,6 +19,8 @@

import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdkuilib.internal.repository.AboutDialog;
import com.android.sdkuilib.internal.repository.MenuBarWrapper;
//Synthetic comment -- @@ -27,10 +29,12 @@
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.repository.sdkman1.AvdManagerPage;
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.SdkUpdaterWindow;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
//Synthetic comment -- @@ -44,6 +48,11 @@
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
* This is an intermediate version of the {@link AvdManagerPage}
* wrapped in its own standalone window for use from the SDK Manager 2.
//Synthetic comment -- @@ -175,61 +184,64 @@
mAvdPage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
}

    @SuppressWarnings("unused") // MenuBarWrapper works using side effects
private void createMenuBar() {

        // Only create the menu when running as standalone.
        // We don't need the menu when invoked from the IDE, or the SDK Manager
        // or from the AVD Chooser dialog. The only point of the menu is to
        // get the about box, and invoke Tools > SDK Manager, which we don't
        // need to do in these cases.
        if (mContext != AvdInvocationContext.STANDALONE) {
            return;
        }

Menu menuBar = new Menu(mShell, SWT.BAR);
mShell.setMenuBar(menuBar);

        MenuItem menuBarTools = new MenuItem(menuBar, SWT.CASCADE);
        menuBarTools.setText("Tools");

        Menu menuTools = new Menu(menuBarTools);
        menuBarTools.setMenu(menuTools);

        MenuItem manageSdk = new MenuItem(menuTools, SWT.NONE);
        manageSdk.setText("Manage SDK...");
        manageSdk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                onSdkManager();
            }
        });

        try {
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


//Synthetic comment -- @@ -334,6 +346,62 @@
}

/**
* Initializes settings.
* This must be called after addExtraPages(), which created a settings page.
* Iterate through all the pages to find the first (and supposedly unique) setting page,








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/DeviceCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/DeviceCreationDialog.java
new file mode 100644
//Synthetic comment -- index 0000000..ad9997c

//Synthetic comment -- @@ -0,0 +1,993 @@







