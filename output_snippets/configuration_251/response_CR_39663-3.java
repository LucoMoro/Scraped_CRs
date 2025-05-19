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
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * This is an intermediate version of the {@link AvdManagerPage}
 * wrapped in its own standalone window for use from the SDK Manager 2.
 */
private void createMenuBar() {

    // Only create the menu when running as standalone.
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

    MenuItem createDevice = new MenuItem(menuTools, SWT.NONE);
    createDevice.setText("Create Device...");
    createDevice.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent event) {
            onCreateDevice();
        }
    });

    MenuItem configureDevices = new MenuItem(menuTools, SWT.NONE);
    configureDevices.setText("Configure Devices...");
    configureDevices.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent event) {
            onConfigureDevices();
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
        // Changed logging to a more generic message
        mUpdaterData.getSdkLog().error("An unexpected error occurred");
    }
}

//<End of snippet n. 0>