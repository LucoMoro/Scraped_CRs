/*SDK Manager 2: Refactor window implementation.

Change-Id:Ic1eac19dcc7e91d6750b7462eb2d28b447acad3a*/




//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 3e50a90..bbefa99 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
import com.android.sdkuilib.internal.repository.PackagesPage;
import com.android.sdkuilib.internal.repository.UpdateNoWindow;
import com.android.sdkuilib.internal.widgets.MessageBoxLog;
import com.android.sdkuilib.repository.IUpdaterWindow;
import com.android.sdkuilib.repository.UpdaterWindow;

import org.eclipse.swt.widgets.Display;
//Synthetic comment -- @@ -299,7 +300,7 @@
Display.getCurrent(),
true /*logErrorsOnly*/);

            IUpdaterWindow window = new UpdaterWindow(
null /* parentShell */,
errorLogger,
mOsSdkFolder);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java
//Synthetic comment -- index c433f5a..7514dea 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressTaskFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.IUpdaterWindow;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
//Synthetic comment -- @@ -49,7 +50,7 @@
* (AVD list, settings, about, installed packages, available packages)
* and the corresponding page on the right.
*/
public class UpdaterWindowImpl implements IUpdaterWindow {

private final Shell mParentShell;
/** Internal data shared between the window and its pages. */
//Synthetic comment -- @@ -91,16 +92,8 @@
mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);
}

/**
     * Opens the window.
* @wbp.parser.entryPoint
*/
public void open() {
//Synthetic comment -- @@ -149,7 +142,7 @@
/**
* Create contents of the window.
*/
    private void createContents() {
SashForm sashForm = new SashForm(mShell, SWT.NONE);
sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

//Synthetic comment -- @@ -166,7 +159,7 @@
sashForm.setWeights(new int[] {150, 576});
}

    private void createPagesRoot(Composite parent) {
mPagesRootComposite = new Composite(parent, SWT.NONE);
mStackLayout = new StackLayout();
mPagesRootComposite.setLayout(mStackLayout);
//Synthetic comment -- @@ -190,7 +183,7 @@
* @param title The title of the page.
* @param pageClass The {@link Composite}-derived class that will implement the page.
*/
    public void registerPage(String title, Class<? extends Composite> pageClass) {
if (mExtraPages == null) {
mExtraPages = new ArrayList<Object[]>();
}
//Synthetic comment -- @@ -282,7 +275,7 @@
/**
* Called before the UI is created.
*/
    private void preCreateContent() {
mUpdaterData.setWindowShell(mShell);
mTaskFactory = new ProgressTaskFactory(mShell);
mUpdaterData.setTaskFactory(mTaskFactory);
//Synthetic comment -- @@ -295,7 +288,7 @@
*
* Returns true if we should show the window.
*/
    private boolean postCreateContent() {
setWindowImage(mShell);
createPages();









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java
//Synthetic comment -- index 8d44c1e..b31d3a8 100755

//Synthetic comment -- @@ -18,33 +18,58 @@


import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressTaskFactory;
import com.android.sdkuilib.internal.tasks.ProgressView;
import com.android.sdkuilib.internal.tasks.ProgressViewFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.IUpdaterWindow;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;

/**
* This is the private implementation of the UpdateWindow
* for the second version of the SDK Manager.
* <p/>
* This window features only one embedded page, the combined installed+available package list.
*/
public class UpdaterWindowImpl2 implements IUpdaterWindow {

    private final Shell mParentShell;
    /** Internal data shared between the window and its pages. */
    private final UpdaterData mUpdaterData;
    /** A list of extra pages to instantiate. Each entry is an object array with 2 elements:
     *  the string title and the Composite class to instantiate to create the page. */
    private ArrayList<Object[]> mExtraPages;
    /** A factory to create progress task dialogs. */
    private ProgressTaskFactory mTaskFactory;
    /** Sets whether the auto-update wizard will be shown when opening the window. */
    private boolean mRequestAutoUpdate;

    // --- UI members ---

    protected Shell mShell;
    private PackagesPage mPkgPage;
private ProgressBar mProgressBar;
private Label mStatusText;
private ImgDisabledButton mButtonStop;
//Synthetic comment -- @@ -58,29 +83,62 @@
* @param osSdkRoot The OS path to the SDK root.
*/
public UpdaterWindowImpl2(Shell parentShell, ISdkLog sdkLog, String osSdkRoot) {
        mParentShell = parentShell;
        mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);
}

/**
     * Opens the window.
* @wbp.parser.entryPoint
*/
public void open() {
        if (mParentShell == null) {
            Display.setAppName("Android"); //$hide$ (hide from SWT designer)
        }

        createShell();
        preCreateContent();
        createContents();
        mShell.open();
        mShell.layout();

        if (postCreateContent()) {    //$hide$ (hide from SWT designer)
            Display display = Display.getDefault();
            while (!mShell.isDisposed()) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
        }

        dispose();  //$hide$
}

    private void createShell() {
        mShell = new Shell(mParentShell, SWT.SHELL_TRIM);
        mShell.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                onAndroidSdkUpdaterDispose();    //$hide$ (hide from SWT designer)
            }
        });

        GridLayout glShell = new GridLayout(2, false);
        glShell.verticalSpacing = 0;
        glShell.horizontalSpacing = 0;
        glShell.marginWidth = 0;
        glShell.marginHeight = 0;
        mShell.setLayout(glShell);

        mShell.setMinimumSize(new Point(500, 300));
        mShell.setSize(700, 500);
        mShell.setText("Android SDK and AVD Manager");
    }

    private void createContents() {
mShell.setText("Android SDK Manager");

        mPkgPage = new PackagesPage(mShell, mUpdaterData);
        mPkgPage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

Composite composite1 = new Composite(mShell, SWT.NONE);
composite1.setLayout(new GridLayout(1, false));
//Synthetic comment -- @@ -116,9 +174,8 @@
}

private Image getImage(String filename) {
        if (mUpdaterData != null) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();
if (imgFactory != null) {
return imgFactory.getImageByName(filename);
}
//Synthetic comment -- @@ -133,25 +190,157 @@

// --- Public API -----------


    /**
     * Registers an extra page for the updater window.
     * <p/>
     * Pages must derive from {@link Composite} and implement a constructor that takes
     * a single parent {@link Composite} argument.
     * <p/>
     * All pages must be registered before the call to {@link #open()}.
     *
     * @param title The title of the page.
     * @param pageClass The {@link Composite}-derived class that will implement the page.
     */
    public void registerPage(String title, Class<? extends Composite> pageClass) {
        if (mExtraPages == null) {
            mExtraPages = new ArrayList<Object[]>();
        }
        mExtraPages.add(new Object[]{ title, pageClass });
    }

    /**
     * Indicate the initial page that should be selected when the window opens.
     * This must be called before the call to {@link #open()}.
     * If null or if the page class is not found, the first page will be selected.
     */
    public void setInitialPage(Class<? extends Composite> pageClass) {
        // Unused in this case. This window display only one page.
    }

    /**
     * Sets whether the auto-update wizard will be shown when opening the window.
     * <p/>
     * This must be called before the call to {@link #open()}.
     */
    public void setRequestAutoUpdate(boolean requestAutoUpdate) {
        mRequestAutoUpdate = requestAutoUpdate;
    }

    /**
     * Adds a new listener to be notified when a change is made to the content of the SDK.
     */
    public void addListener(ISdkChangeListener listener) {
        mUpdaterData.addListeners(listener);
    }

    /**
     * Removes a new listener to be notified anymore when a change is made to the content of
     * the SDK.
     */
    public void removeListener(ISdkChangeListener listener) {
        mUpdaterData.removeListener(listener);
    }

// --- Internals & UI Callbacks -----------

    /**
     * Called before the UI is created.
     */
    private void preCreateContent() {
        mUpdaterData.setWindowShell(mShell);
        mUpdaterData.setImageFactory(new ImageFactory(mShell.getDisplay()));
        // Note: we can't create the TaskFactory yet because we need the UI
        // to be created first. And the UI needs the ImageFactory to be set.
    }

    /**
     * Once the UI has been created, initializes the content.
     * This creates the pages, selects the first one, setup sources and scan for local folders.
     *
     * Returns true if we should show the window.
     */
    private boolean postCreateContent() {
ProgressViewFactory factory = new ProgressViewFactory();
factory.setProgressView(new ProgressView(
mStatusText, mProgressBar, mButtonStop));
        mUpdaterData.setTaskFactory(factory);

        setWindowImage(mShell);

        setupSources();
        initializeSettings();

        if (mUpdaterData.checkIfInitFailed()) {
            return false;
        }

        mUpdaterData.broadcastOnSdkLoaded();

        if (mRequestAutoUpdate) {
            mUpdaterData.updateOrInstallAll_WithGUI(
                    null /*selectedArchives*/,
                    false /* includeObsoletes */);
        }

        return true;
}

    /**
     * Creates the icon of the window shell.
     */
    private void setWindowImage(Shell androidSdkUpdater) {
        String imageName = "android_icon_16.png"; //$NON-NLS-1$
        if (SdkConstants.currentPlatform() == SdkConstants.PLATFORM_DARWIN) {
            imageName = "android_icon_128.png"; //$NON-NLS-1$
        }

        if (mUpdaterData != null) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();
            if (imgFactory != null) {
                mShell.setImage(imgFactory.getImageByName(imageName));
            }
        }
    }

    /**
     * Called by the main loop when the window has been disposed.
     */
    private void dispose() {
        mUpdaterData.getSources().saveUserAddons(mUpdaterData.getSdkLog());
    }

    /**
     * Callback called when the window shell is disposed.
     */
    private void onAndroidSdkUpdaterDispose() {
        if (mUpdaterData != null) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();
            if (imgFactory != null) {
                imgFactory.dispose();
            }
        }
    }

    /**
     * Used to initialize the sources.
     */
    private void setupSources() {
        mUpdaterData.setupDefaultSources();
    }

    /**
     * Initializes settings.
     * This must be called after addExtraPages(), which created a settings page.
     * Iterate through all the pages to find the first (and supposedly unique) setting page,
     * and use it to load and apply these settings.
     */
    private void initializeSettings() {
        SettingsController c = mUpdaterData.getSettingsController();
        c.loadSettings();
        c.applySettings();

        // TODO c.setSettingsPage(settingsPage);
}

private void onToggleDetails() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 6309d5d..de77848 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressTask;
import com.android.sdkuilib.repository.IUpdaterWindow;
import com.android.sdkuilib.repository.UpdaterWindow;

import org.eclipse.jface.dialogs.MessageDialog;
//Synthetic comment -- @@ -1008,7 +1009,7 @@
log = new MessageBoxLog("Result of SDK Manager", display, true /*logErrorsOnly*/);
}

        IUpdaterWindow window = new UpdaterWindow(
mTable.getShell(),
log,
mAvdManager.getSdkManager().getLocation());








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/IUpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/IUpdaterWindow.java
new file mode 100755
//Synthetic comment -- index 0000000..a771e53

//Synthetic comment -- @@ -0,0 +1,68 @@
/*
 * Copyright (C) 2009 The Android Open Source Project
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

package com.android.sdkuilib.repository;

import org.eclipse.swt.widgets.Composite;

public interface IUpdaterWindow {

    /**
     * Registers an extra page for the updater window.
     * <p/>
     * Pages must derive from {@link Composite} and implement a constructor that takes
     * a single parent {@link Composite} argument.
     * <p/>
     * All pages must be registered before the call to {@link #open()}.
     *
     * @param title The title of the page.
     * @param pageClass The {@link Composite}-derived class that will implement the page.
     */
    public abstract void registerPage(String title,
            Class<? extends Composite> pageClass);

    /**
     * Indicate the initial page that should be selected when the window opens.
     * <p/>
     * This must be called before the call to {@link #open()}.
     * If null or if the page class is not found, the first page will be selected.
     */
    public abstract void setInitialPage(Class<? extends Composite> pageClass);

    /**
     * Sets whether the auto-update wizard will be shown when opening the window.
     * <p/>
     * This must be called before the call to {@link #open()}.
     */
    public abstract void setRequestAutoUpdate(boolean requestAutoUpdate);

    /**
     * Adds a new listener to be notified when a change is made to the content of the SDK.
     */
    public abstract void addListener(ISdkChangeListener listener);

    /**
     * Removes a new listener to be notified anymore when a change is made to the content of
     * the SDK.
     */
    public abstract void removeListener(ISdkChangeListener listener);

    /**
     * Opens the window.
     */
    public abstract void open();

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java
//Synthetic comment -- index c1f51848..d49b072 100755

//Synthetic comment -- @@ -28,9 +28,9 @@
*
* This is the public interface for using the window.
*/
public class UpdaterWindow implements IUpdaterWindow {

    private IUpdaterWindow mWindow;

/**
* Creates a new window. Caller must call open(), which will block.
//Synthetic comment -- @@ -61,7 +61,7 @@
* @param pageClass The {@link Composite}-derived class that will implement the page.
*/
public void registerPage(String title, Class<? extends Composite> pageClass) {
        mWindow.registerPage(title, pageClass);
}

/**







