
//<Beginning of snippet n. 0>



import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdkuilib.internal.repository.AboutDialog;
import com.android.sdkuilib.internal.repository.MenuBarWrapper;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.repository.sdkman1.AvdManagerPage;
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.SdkUpdaterWindow;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
* This is an intermediate version of the {@link AvdManagerPage}
* wrapped in its own standalone window for use from the SDK Manager 2.
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


}

/**
* Initializes settings.
* This must be called after addExtraPages(), which created a settings page.
* Iterate through all the pages to find the first (and supposedly unique) setting page,

//<End of snippet n. 0>










//<Beginning of snippet n. 1>

new file mode 100644


//<End of snippet n. 1>








