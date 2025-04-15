/*SDK Manager 2: Refactor window implementation.

The previous patch based the implementation of the
new window on top of the old UpdaterWindowImpl.
Although that works since there are many things in
common, in practice the goal is to get rid of the
old one eventually so it's easier to not add a
dependency that needs to be removed later.

Change-Id:Ic1eac19dcc7e91d6750b7462eb2d28b447acad3a*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 3e50a90..bbefa99 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
import com.android.sdkuilib.internal.repository.PackagesPage;
import com.android.sdkuilib.internal.repository.UpdateNoWindow;
import com.android.sdkuilib.internal.widgets.MessageBoxLog;
import com.android.sdkuilib.repository.UpdaterWindow;

import org.eclipse.swt.widgets.Display;
//Synthetic comment -- @@ -299,7 +300,7 @@
Display.getCurrent(),
true /*logErrorsOnly*/);

            UpdaterWindow window = new UpdaterWindow(
null /* parentShell */,
errorLogger,
mOsSdkFolder);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java
//Synthetic comment -- index c433f5a..7514dea 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressTaskFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
//Synthetic comment -- @@ -49,7 +50,7 @@
* (AVD list, settings, about, installed packages, available packages)
* and the corresponding page on the right.
*/
public class UpdaterWindowImpl {

private final Shell mParentShell;
/** Internal data shared between the window and its pages. */
//Synthetic comment -- @@ -91,16 +92,8 @@
mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);
}

    protected UpdaterData getUpdaterData() {
        return mUpdaterData;
    }

    protected Composite getPagesRootComposite() {
        return mPagesRootComposite;
    }

/**
     * Open the window.
* @wbp.parser.entryPoint
*/
public void open() {
//Synthetic comment -- @@ -149,7 +142,7 @@
/**
* Create contents of the window.
*/
    protected void createContents() {
SashForm sashForm = new SashForm(mShell, SWT.NONE);
sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

//Synthetic comment -- @@ -166,7 +159,7 @@
sashForm.setWeights(new int[] {150, 576});
}

    protected void createPagesRoot(Composite parent) {
mPagesRootComposite = new Composite(parent, SWT.NONE);
mStackLayout = new StackLayout();
mPagesRootComposite.setLayout(mStackLayout);
//Synthetic comment -- @@ -190,7 +183,7 @@
* @param title The title of the page.
* @param pageClass The {@link Composite}-derived class that will implement the page.
*/
    public void registerExtraPage(String title, Class<? extends Composite> pageClass) {
if (mExtraPages == null) {
mExtraPages = new ArrayList<Object[]>();
}
//Synthetic comment -- @@ -282,7 +275,7 @@
/**
* Called before the UI is created.
*/
    protected void preCreateContent() {
mUpdaterData.setWindowShell(mShell);
mTaskFactory = new ProgressTaskFactory(mShell);
mUpdaterData.setTaskFactory(mTaskFactory);
//Synthetic comment -- @@ -295,7 +288,7 @@
*
* Returns true if we should show the window.
*/
    protected boolean postCreateContent() {
setWindowImage(mShell);
createPages();









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java
//Synthetic comment -- index 8d44c1e..83a86b2 100755

//Synthetic comment -- @@ -18,33 +18,56 @@


import com.android.sdklib.ISdkLog;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressView;
import com.android.sdkuilib.internal.tasks.ProgressViewFactory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

/**
* This is the private implementation of the UpdateWindow
* for the second version of the SDK Manager.
* <p/>
* This window features only one embedded page, the combined installed+available package list.
*/
public class UpdaterWindowImpl2 extends UpdaterWindowImpl {

private ProgressBar mProgressBar;
private Label mStatusText;
private ImgDisabledButton mButtonStop;
//Synthetic comment -- @@ -58,29 +81,62 @@
* @param osSdkRoot The OS path to the SDK root.
*/
public UpdaterWindowImpl2(Shell parentShell, ISdkLog sdkLog, String osSdkRoot) {
        super(parentShell, sdkLog, osSdkRoot);
}

/**
* @wbp.parser.entryPoint
*/
    @Override
public void open() {
        super.open();
}

    @Override
    protected void createContents() {
mShell.setText("Android SDK Manager");

        Composite root = new Composite(mShell, SWT.NONE);
        GridLayout gl = new GridLayout(1, false);
        //gl.marginHeight = gl.marginRight = 0;
        root.setLayout(gl);
        root.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

        createPagesRoot(root);
        getPagesRootComposite().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

Composite composite1 = new Composite(mShell, SWT.NONE);
composite1.setLayout(new GridLayout(1, false));
//Synthetic comment -- @@ -116,9 +172,8 @@
}

private Image getImage(String filename) {
        UpdaterData updaterData = getUpdaterData();
        if (updaterData != null) {
            ImageFactory imgFactory = updaterData.getImageFactory();
if (imgFactory != null) {
return imgFactory.getImageByName(filename);
}
//Synthetic comment -- @@ -133,25 +188,161 @@

// --- Public API -----------

// --- Internals & UI Callbacks -----------

    @Override
    protected boolean postCreateContent() {
        // Override the base task factory with the new one
        UpdaterData updaterData = getUpdaterData();
ProgressViewFactory factory = new ProgressViewFactory();
factory.setProgressView(new ProgressView(
mStatusText, mProgressBar, mButtonStop));
        updaterData.setTaskFactory(factory);

        return super.postCreateContent();
}

    @Override
    protected void createPages() {
        PackagesPage pkgPage = new PackagesPage(getPagesRootComposite(), getUpdaterData());
        addPage(pkgPage, "Packages List");
        addExtraPages();
}

private void onToggleDetails() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 6309d5d..de77848 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressTask;
import com.android.sdkuilib.repository.UpdaterWindow;

import org.eclipse.jface.dialogs.MessageDialog;
//Synthetic comment -- @@ -1008,7 +1009,7 @@
log = new MessageBoxLog("Result of SDK Manager", display, true /*logErrorsOnly*/);
}

        UpdaterWindow window = new UpdaterWindow(
mTable.getShell(),
log,
mAvdManager.getSdkManager().getLocation());








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/IUpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/IUpdaterWindow.java
new file mode 100755
//Synthetic comment -- index 0000000..a771e53

//Synthetic comment -- @@ -0,0 +1,68 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java
//Synthetic comment -- index c1f51848..d49b072 100755

//Synthetic comment -- @@ -28,9 +28,9 @@
*
* This is the public interface for using the window.
*/
public class UpdaterWindow {

    private UpdaterWindowImpl mWindow;

/**
* Creates a new window. Caller must call open(), which will block.
//Synthetic comment -- @@ -61,7 +61,7 @@
* @param pageClass The {@link Composite}-derived class that will implement the page.
*/
public void registerPage(String title, Class<? extends Composite> pageClass) {
        mWindow.registerExtraPage(title, pageClass);
}

/**







