/*SDK Manager: remove userCanChangeSdkRoot flag.

We've never enabled the UI for it as the SDK path
is dicated by where the manager is started from, so
it's time to clean it up.

Change-Id:I97d7ede19e869b970e649ea8a6f9b94045a71269*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/actions/AvdManagerAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/actions/AvdManagerAction.java
//Synthetic comment -- index 4bc4496..e0de79a 100755

//Synthetic comment -- @@ -51,8 +51,7 @@
UpdaterWindow window = new UpdaterWindow(
AdtPlugin.getDisplay().getActiveShell(),
new AdtConsoleSdkLog(),
                    sdk.getSdkLocation());
window.addListeners(new UpdaterWindow.ISdkListener() {
public void onSdkChange(boolean init) {
if (init == false) { // ignore initial load of the SDK.








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 0066b6c..ddb7979 100644

//Synthetic comment -- @@ -297,8 +297,7 @@
UpdaterWindow window = new UpdaterWindow(
null /* parentShell */,
errorLogger,
                    mOsSdkFolder);
window.registerPage("Settings", SettingsPage.class);
window.registerPage("About", AboutPage.class);
if (autoUpdate) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/LocalPackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/LocalPackagesPage.java
//Synthetic comment -- index d701ea5..88e81f2 100755

//Synthetic comment -- @@ -39,26 +39,14 @@
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import java.io.File;

public class LocalPackagesPage extends Composite implements ISdkListener {

private final UpdaterData mUpdaterData;

private Label mSdkLocLabel;
private TableViewer mTableViewerPackages;
private Table mTablePackages;
private TableColumn mColumnPackages;
//Synthetic comment -- @@ -89,7 +77,11 @@
private void createContents(Composite parent) {
parent.setLayout(new GridLayout(3, false));

        mSdkLocLabel = new Label(parent, SWT.NONE);
        mSdkLocLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false, 3, 1));
        mSdkLocLabel.setText("SDK Location: " +
                (mUpdaterData.getOsSdkRoot() != null ? mUpdaterData.getOsSdkRoot()
                                                     : "<unknown>"));

mTableViewerPackages = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
mTablePackages = mTableViewerPackages.getTable();
//Synthetic comment -- @@ -104,7 +96,7 @@

mColumnPackages = new TableColumn(mTablePackages, SWT.NONE);
mColumnPackages.setWidth(377);
        mColumnPackages.setText("Installed packages");

mDescriptionContainer = new Group(parent, SWT.NONE);
mDescriptionContainer.setLayout(new GridLayout(1, false));
//Synthetic comment -- @@ -155,30 +147,6 @@
});
}

@Override
public void dispose() {
mUpdaterData.removeListener(this);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RemotePackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/RemotePackagesPage.java
//Synthetic comment -- index ac6c0c8..099826a 100755

//Synthetic comment -- @@ -66,6 +66,7 @@
private Button mRefreshButton;
private Button mInstallSelectedButton;
private Label mDescriptionLabel;
    private Label mSdkLocLabel;



//Synthetic comment -- @@ -86,6 +87,12 @@
private void createContents(Composite parent) {
parent.setLayout(new GridLayout(5, false));

        mSdkLocLabel = new Label(parent, SWT.NONE);
        mSdkLocLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false, 5, 1));
        mSdkLocLabel.setText("SDK Location: " +
                (mUpdaterData.getOsSdkRoot() != null ? mUpdaterData.getOsSdkRoot()
                                                     : "<unknown>"));

mTreeViewerSources = new CheckboxTreeViewer(parent, SWT.BORDER);
mTreeViewerSources.addCheckStateListener(new ICheckStateListener() {
public void checkStateChanged(CheckStateChangedEvent event) {
//Synthetic comment -- @@ -104,7 +111,7 @@

mColumnSource = new TreeColumn(mTreeSources, SWT.NONE);
mColumnSource.setWidth(289);
        mColumnSource.setText("Packages available for download");

mDescriptionContainer = new Group(parent, SWT.NONE);
mDescriptionContainer.setLayout(new GridLayout(1, false));








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index 8f649b5..25ed30f 100755

//Synthetic comment -- @@ -57,7 +57,6 @@

private final ISdkLog mSdkLog;
private ITaskFactory mTaskFactory;

private SdkManager mSdkManager;
private AvdManager mAvdManager;
//Synthetic comment -- @@ -107,14 +106,6 @@
return mTaskFactory;
}

public SdkSources getSources() {
return mSources;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java
//Synthetic comment -- index 574357f..730ee8f 100755

//Synthetic comment -- @@ -80,14 +80,10 @@
* @param parentShell Parent shell.
* @param sdkLog Logger. Cannot be null.
* @param osSdkRoot The OS path to the SDK root.
*/
    public UpdaterWindowImpl(Shell parentShell, ISdkLog sdkLog, String osSdkRoot) {
mParentShell = parentShell;
mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);
}

/**
//Synthetic comment -- @@ -265,9 +261,9 @@

setWindowImage(mAndroidSdkUpdater);

        addPage(mAvdManagerPage,     "Virtual devices");
        addPage(mLocalPackagePage,   "Installed packages");
        addPage(mRemotePackagesPage, "Available packages");
addExtraPages();

int pageIndex = 0;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 8dcb533..7962f04 100644

//Synthetic comment -- @@ -807,6 +807,7 @@
/**
* Updates the enable state of the Details, Start, Delete and Update buttons.
*/
    @SuppressWarnings("null")
private void enableActionButtons() {
if (mIsEnabled == false) {
mDetailsButton.setEnabled(false);
//Synthetic comment -- @@ -972,8 +973,7 @@
UpdaterWindow window = new UpdaterWindow(
mTable.getShell(),
log,
                mAvdManager.getSdkManager().getLocation());
window.open();
refresh(true /*reload*/); // UpdaterWindow uses its own AVD manager so this one must reload.









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java
//Synthetic comment -- index 0e20ded..4e6f548 100755

//Synthetic comment -- @@ -49,12 +49,9 @@
* @param parentShell Parent shell.
* @param sdkLog Logger. Cannot be null.
* @param osSdkRoot The OS path to the SDK root.
*/
    public UpdaterWindow(Shell parentShell, ISdkLog sdkLog, String osSdkRoot) {
        mWindow = new UpdaterWindowImpl(parentShell, sdkLog, osSdkRoot);
}

/**







