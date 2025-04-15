/*AVD Manager: display and select newly created AVD.

When using Device page > Create AVD, this will switch
the AVD Manager to the AVD page, refresh the AVD list
and select the new AVD.

SDK Bug: 38785

Change-Id:I7cff8f434b16d90cc9e46e938631e6b67bb69af7*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AvdManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AvdManagerPage.java
//Synthetic comment -- index 1074dfa..c8e0612 100755

//Synthetic comment -- @@ -19,6 +19,8 @@
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.DeviceManager.DevicesChangeListener;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.widgets.AvdSelector;
import com.android.sdkuilib.internal.widgets.AvdSelector.DisplayMode;
//Synthetic comment -- @@ -112,6 +114,19 @@
// Disable the check that prevents subclassing of SWT components
}

// -- Start of internal part ----------
// Hide everything down-below from SWT designer
//$hide>>$








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AvdManagerWindowImpl1.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AvdManagerWindowImpl1.java
//Synthetic comment -- index 36e01ba..f4dc212 100755

//Synthetic comment -- @@ -19,6 +19,7 @@

import com.android.SdkConstants;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdkuilib.internal.repository.AboutDialog;
import com.android.sdkuilib.internal.repository.MenuBarWrapper;
//Synthetic comment -- @@ -26,6 +27,7 @@
import com.android.sdkuilib.internal.repository.SettingsDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.SdkUpdaterWindow;
//Synthetic comment -- @@ -72,6 +74,7 @@
protected Shell mShell;
private AvdManagerPage mAvdPage;
private SettingsController mSettingsController;

/**
* Creates a new window. Caller must call open(), which will block.
//Synthetic comment -- @@ -176,18 +179,18 @@

private void createContents() {

        TabFolder tabFolder = new TabFolder(mShell, SWT.NONE);
        GridDataBuilder.create(tabFolder).fill().grab().hSpan(2);

// avd tab
        TabItem avdTabItem = new TabItem(tabFolder, SWT.NONE);
avdTabItem.setText("Android Virtual Devices");
        createAvdTab(tabFolder, avdTabItem);

// device tab
        TabItem devTabItem = new TabItem(tabFolder, SWT.NONE);
devTabItem.setText("Device Definitions");
        createDeviceTab(tabFolder, devTabItem);
}

private void createAvdTab(TabFolder tabFolder, TabItem avdTabItem) {
//Synthetic comment -- @@ -204,8 +207,19 @@
devTabItem.setControl(root);
GridLayoutBuilder.create(root).columns(1);

        DeviceManagerPage container = new DeviceManagerPage(root, SWT.NONE, mUpdaterData, mDeviceManager);
        GridDataBuilder.create(container).fill().grab();
}

@SuppressWarnings("unused")








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java
//Synthetic comment -- index 62f8bd5..2f1193b 100755

//Synthetic comment -- @@ -90,6 +90,10 @@
public class DeviceManagerPage extends Composite
implements ISdkChangeListener, DevicesChangeListener, DisposeListener {

private final UpdaterData mUpdaterData;
private final DeviceManager mDeviceManager;
private Table mTable;
//Synthetic comment -- @@ -104,6 +108,8 @@
private Image mOtherImage;
private int mImageWidth;
private boolean mDisableRefresh;
/**
* Create the composite.
* @param parent The parent of the composite.
//Synthetic comment -- @@ -125,6 +131,10 @@
postCreate();  //$hide$
}

private void createContents(Composite parent) {

// get some bitmaps.
//Synthetic comment -- @@ -201,7 +211,7 @@
mNewAvdButton.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent arg0) {
                onNewAvd();
}
});

//Synthetic comment -- @@ -702,13 +712,13 @@
}
}

    private void onNewAvd() {
CellInfo ci = getTableSelection();
if (ci == null || ci.mDevice == null) {
return;
}

        AvdCreationDialog dlg = new AvdCreationDialog(mTable.getShell(),
mUpdaterData.getAvdManager(),
mImageFactory,
mUpdaterData.getSdkLog(),
//Synthetic comment -- @@ -717,6 +727,10 @@

if (dlg.open() == Window.OK) {
onRefresh();
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index e3bd5f7..b97881e 100644

//Synthetic comment -- @@ -116,6 +116,9 @@
private Label mStatusIcon;
private Label mStatusLabel;

/**
* {@link VerifyListener} for {@link Text} widgets that should only contains
* numbers.
//Synthetic comment -- @@ -133,7 +136,6 @@
}
}
};
    private Device mInitWithDevice;

public AvdCreationDialog(Shell shell,
AvdManager avdManager,
//Synthetic comment -- @@ -165,6 +167,11 @@
}
}

@Override
protected Control createContents(Composite parent) {
Control control = super.createContents(parent);
//Synthetic comment -- @@ -994,6 +1001,7 @@
mAvdInfo != null, // edit existing
log);

boolean success = avdInfo != null;

if (log instanceof MessageBoxLog) {







