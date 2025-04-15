/*New standalone AVD Manager window.

This is the the current "AVD Manager" page wrapped
as a window so that it can be used either in standalone
or from the new SDK Manager 2.

Change-Id:Id6272ca87481890809e483eee6d57e715d8ad517*/




//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index ccac9b7..dd9f6cd 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
import com.android.sdklib.xml.AndroidXPathFactory;
import com.android.sdkmanager.internal.repository.AboutPage;
import com.android.sdkmanager.internal.repository.SettingsPage;
import com.android.sdkuilib.internal.repository.AvdManagerWindowImpl1;
import com.android.sdkuilib.internal.repository.PackagesPage;
import com.android.sdkuilib.internal.repository.UpdateNoWindow;
import com.android.sdkuilib.internal.repository.UpdaterPage;
//Synthetic comment -- @@ -232,7 +233,7 @@
// We don't support a specific GUI for this.
// If the user forces a gui mode to see this list, simply launch the regular GUI.
if (!mSdkCommandLine.getFlagNoUI(verb)) {
                    showSdkManagerWindow(false /*autoUpdate*/);
} else {
displayRemoteSdkListNoUI();
}
//Synthetic comment -- @@ -279,13 +280,21 @@
if (mSdkCommandLine.getFlagNoUI(verb)) {
updateSdkNoUI();
} else {
                    showSdkManagerWindow(true /*autoUpdate*/);
}

} else if (SdkCommandLine.OBJECT_ADB.equals(directObject)) {
updateAdb();

}
        } else if (SdkCommandLine.VERB_DISPLAY.equals(verb)) {
            if (SdkCommandLine.OBJECT_AVD.equals(directObject)) {
                showAvdManagerWindow();

            } else if (SdkCommandLine.OBJECT_SDK.equals(directObject)) {
                showSdkManagerWindow(false /*autoUpdate*/);
            }

} else if (SdkCommandLine.VERB_DELETE.equals(verb) &&
SdkCommandLine.OBJECT_AVD.equals(directObject)) {
deleteAvd();
//Synthetic comment -- @@ -295,7 +304,7 @@
moveAvd();

} else if (verb == null && directObject == null) {
            showSdkManagerWindow(false /*autoUpdate*/);

} else {
mSdkCommandLine.printHelpAndExit(null);
//Synthetic comment -- @@ -303,9 +312,9 @@
}

/**
     * Display the main SDK Manager app window
*/
    private void showSdkManagerWindow(boolean autoUpdate) {
try {
MessageBoxLog errorLogger = new MessageBoxLog(
"SDK Manager",
//Synthetic comment -- @@ -332,6 +341,34 @@
}
}

    /**
     * Display the main AVD Manager app window
     */
    private void showAvdManagerWindow() {
        try {
            MessageBoxLog errorLogger = new MessageBoxLog(
                    "AVD Manager",
                    Display.getCurrent(),
                    true /*logErrorsOnly*/);

            AvdManagerWindowImpl1 window = new AvdManagerWindowImpl1(
                    null /* parentShell */,
                    errorLogger,
                    mOsSdkFolder,
                    AvdManagerWindowImpl1.InvocationContext.STANDALONE);

            window.registerPage(SettingsPage.class, UpdaterPage.Purpose.SETTINGS);
            window.registerPage(AboutPage.class,    UpdaterPage.Purpose.ABOUT_BOX);

            window.open();

            errorLogger.displayResult(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

private void displayRemoteSdkListNoUI() {
boolean force = mSdkCommandLine.getFlagForce();
boolean useHttp = mSdkCommandLine.getFlagNoHttps();








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java b/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java
//Synthetic comment -- index fb15cb5..7b73f65 100644

//Synthetic comment -- @@ -38,11 +38,12 @@
*   or optional) for the given action.
*/

    public final static String VERB_LIST    = "list";                               //$NON-NLS-1$
    public final static String VERB_CREATE  = "create";                             //$NON-NLS-1$
    public final static String VERB_MOVE    = "move";                               //$NON-NLS-1$
    public final static String VERB_DELETE  = "delete";                             //$NON-NLS-1$
    public final static String VERB_UPDATE  = "update";                             //$NON-NLS-1$
    public final static String VERB_DISPLAY = "display";                            //$NON-NLS-1$

public static final String OBJECT_SDK            = "sdk";                       //$NON-NLS-1$
public static final String OBJECT_AVD            = "avd";                       //$NON-NLS-1$
//Synthetic comment -- @@ -143,8 +144,14 @@
"Updates adb to support the USB devices declared in the SDK add-ons." },

{ VERB_UPDATE, OBJECT_SDK,
                "Updates the SDK by suggesting new platforms to install if available." },

            { VERB_DISPLAY, OBJECT_SDK,
                "Displays the SDK Manager window." },
            { VERB_DISPLAY, OBJECT_AVD,
                "Displays the AVD Manager window.",
                OBJECT_AVDS },
    };

public SdkCommandLine(ISdkLog logger) {
super(logger, ACTIONS);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AvdManagerWindowImpl1.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AvdManagerWindowImpl1.java
new file mode 100755
//Synthetic comment -- index 0000000..719de4c

//Synthetic comment -- @@ -0,0 +1,539 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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

package com.android.sdkuilib.internal.repository;


import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdkuilib.internal.repository.UpdaterPage.Purpose;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.UpdaterWindow;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;
import com.android.sdkuilib.ui.SwtBaseDialog;
import com.android.util.Pair;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;

/**
 * This is an intermediate version of the {@link AvdManagerPage}
 * wrapped in its own standalone window for use from the SDK Manager 2.
 */
public class AvdManagerWindowImpl1 implements IUpdaterWindow {

    private static final String APP_NAME = "Android Virtual Device Manager";
    private static final String APP_NAME_MAC_MENU = "AVD Manager";

    /**
     * Enum giving some indication of what is invoking this window.
     * The behavior and UI will change slightly depending on the context.
     * <p/>
     * Note: if you add Android support to your specific IDE, you might want
     * to specialize this context enum.
     */
    public enum InvocationContext {
        /**
         * The AVD Manager is invoked from the stand-alone 'android' tool.
         * In this mode, we present an about box, a settings page.
         * For SdkMan2, we also have a menu bar and link to the SDK Manager 2.
         */
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
         * In this mode, we do not modify the menu bar.
         * There is no about box and no settings.
         */
        IDE,
    }


    private final Shell mParentShell;
    private final InvocationContext mContext;
    /** Internal data shared between the window and its pages. */
    private final UpdaterData mUpdaterData;
    /** A list of extra pages to instantiate. Each entry is an object array with 2 elements:
     *  the string title and the Composite class to instantiate to create the page. */
    private ArrayList<Pair<Class<? extends UpdaterPage>, Purpose>> mExtraPages;
    /** Sets whether the auto-update wizard will be shown when opening the window. */
    private boolean mRequestAutoUpdate;

    // --- UI members ---

    protected Shell mShell;
    private AvdManagerPage mAvdPage;
    private SettingsController mSettingsController;

    /**
     * Creates a new window. Caller must call open(), which will block.
     *
     * @param parentShell Parent shell.
     * @param sdkLog Logger. Cannot be null.
     * @param osSdkRoot The OS path to the SDK root.
     * @param context The {@link InvocationContext} to change the behavior depending on who's
     *  opening the SDK Manager.
     */
    public AvdManagerWindowImpl1(
            Shell parentShell,
            ISdkLog sdkLog,
            String osSdkRoot,
            InvocationContext context) {
        mParentShell = parentShell;
        mContext = context;
        mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);
    }

    /**
     * Creates a new window. Caller must call open(), which will block.
     * <p/>
     * This is to be used when the window is opened from {@link UpdaterWindowImpl2}
     * to share the same {@link UpdaterData} structure.
     *
     * @param parentShell Parent shell.
     * @param updaterData The parent's updater data.
     * @param context The {@link InvocationContext} to change the behavior depending on who's
     *  opening the SDK Manager.
     */
    public AvdManagerWindowImpl1(
            Shell parentShell,
            UpdaterData updaterData,
            InvocationContext context) {
        mParentShell = parentShell;
        mContext = context;
        mUpdaterData = updaterData;
    }

    /**
     * Opens the window.
     * @wbp.parser.entryPoint
     */
    public void open() {
        if (mParentShell == null) {
            Display.setAppName(APP_NAME); //$hide$ (hide from SWT designer)
        }

        createShell();
        preCreateContent();
        createContents();
        createMenuBar();
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
        mShell.setText(APP_NAME);
    }

    private void createContents() {

        mAvdPage = new AvdManagerPage(mShell, SWT.NONE, mUpdaterData);
        mAvdPage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
    }

    private void createMenuBar() {

        if (mContext != InvocationContext.STANDALONE) {
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

        if (mContext != InvocationContext.IDE) {
            // Note: when invoked from an IDE, the SwtMenuBar library isn't
            // available. This means this source should not directly import
            // any of SwtMenuBar classes, otherwise the whole window class
            // would fail to load. The MenuBarWrapper below helps to make
            // that indirection.

            new MenuBarWrapper(APP_NAME_MAC_MENU, menuTools) {
                @Override
                public void onPreferencesMenuSelected() {
                    showRegisteredPage(Purpose.SETTINGS);
                }

                @Override
                public void onAboutMenuSelected() {
                    showRegisteredPage(Purpose.ABOUT_BOX);
                }

                @Override
                public void printError(String format, Object... args) {
                    if (mUpdaterData != null) {
                        mUpdaterData.getSdkLog().error(null, format, args);
                    }
                }
            };
        }
    }

    private Image getImage(String filename) {
        if (mUpdaterData != null) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();
            if (imgFactory != null) {
                return imgFactory.getImageByName(filename);
            }
        }
        return null;
    }


    // -- Start of internal part ----------
    // Hide everything down-below from SWT designer
    //$hide>>$

    // --- Public API -----------


    /**
     * Registers an extra page for the updater window.
     * <p/>
     * Pages must derive from {@link Composite} and implement a constructor that takes
     * a single parent {@link Composite} argument.
     * <p/>
     * All pages must be registered before the call to {@link #open()}.
     *
     * @param pageClass The {@link Composite}-derived class that will implement the page.
     * @param purpose The purpose of this page, e.g. an about box, settings page or generic.
     */
    @SuppressWarnings("unchecked")
    public void registerPage(Class<? extends UpdaterPage> pageClass,
            Purpose purpose) {
        if (mExtraPages == null) {
            mExtraPages = new ArrayList<Pair<Class<? extends UpdaterPage>, Purpose>>();
        }
        Pair<?, Purpose> value = Pair.of(pageClass, purpose);
        mExtraPages.add((Pair<Class<? extends UpdaterPage>, Purpose>) value);
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
        // We need the UI factory to create the UI
        mUpdaterData.setImageFactory(new ImageFactory(mShell.getDisplay()));
        // Note: we can't create the TaskFactory yet because we need the UI
        // to be created first, so this is done in postCreateContent().
    }

    /**
     * Once the UI has been created, initializes the content.
     * This creates the pages, selects the first one, setup sources and scan for local folders.
     *
     * Returns true if we should show the window.
     */
    private boolean postCreateContent() {
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
     *
     * @param shell The shell on which to put the icon
     */
    private void setWindowImage(Shell shell) {
        String imageName = "android_icon_16.png"; //$NON-NLS-1$
        if (SdkConstants.currentPlatform() == SdkConstants.PLATFORM_DARWIN) {
            imageName = "android_icon_128.png";
        }

        if (mUpdaterData != null) {
            ImageFactory imgFactory = mUpdaterData.getImageFactory();
            if (imgFactory != null) {
                shell.setImage(imgFactory.getImageByName(imageName));
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
        mSettingsController = mUpdaterData.getSettingsController();
        mSettingsController.loadSettings();
        mSettingsController.applySettings();
    }

    private void showRegisteredPage(Purpose purpose) {
        if (mExtraPages == null) {
            return;
        }

        Class<? extends UpdaterPage> clazz = null;

        for (Pair<Class<? extends UpdaterPage>, Purpose> extraPage : mExtraPages) {
            if (extraPage.getSecond() == purpose) {
                clazz = extraPage.getFirst();
                break;
            }
        }

        if (clazz != null) {
            PageDialog d = new PageDialog(mShell, clazz, purpose == Purpose.SETTINGS);
            d.open();
        }
    }

    private void onSdkManager() {
        ITaskFactory oldFactory = mUpdaterData.getTaskFactory();

        try {
            UpdaterWindowImpl2 win = new UpdaterWindowImpl2(
                    mShell,
                    mUpdaterData,
                    UpdaterWindow.InvocationContext.AVD_MANAGER);

            for (Pair<Class<? extends UpdaterPage>, Purpose> page : mExtraPages) {
                win.registerPage(page.getFirst(), page.getSecond());
            }

            win.open();
        } catch (Exception e) {
            mUpdaterData.getSdkLog().error(e, "SDK Manager window error");
        } finally {
            mUpdaterData.setTaskFactory(oldFactory);
        }
    }

    // End of hiding from SWT Designer
    //$hide<<$

    // -----

    /**
     * Dialog used to display either the About page or the Settings (aka Options) page
     * with a "close" button.
     */
    private class PageDialog extends SwtBaseDialog {

        private final Class<? extends UpdaterPage> mPageClass;
        private final boolean mIsSettingsPage;

        protected PageDialog(
                Shell parentShell,
                Class<? extends UpdaterPage> pageClass,
                boolean isSettingsPage) {
            super(parentShell, SWT.APPLICATION_MODAL, null /*title*/);
            mPageClass = pageClass;
            mIsSettingsPage = isSettingsPage;
        }

        @Override
        protected void createContents() {
            Shell shell = getShell();
            setWindowImage(shell);

            GridLayoutBuilder.create(shell).columns(2);

            UpdaterPage content = UpdaterPage.newInstance(
                    mPageClass,
                    shell,
                    SWT.NONE,
                    mUpdaterData.getSdkLog());
            GridDataBuilder.create(content).fill().grab().hSpan(2);
            if (content.getLayout() instanceof GridLayout) {
                GridLayout gl = (GridLayout) content.getLayout();
                gl.marginHeight = gl.marginWidth = 0;
            }

            if (mIsSettingsPage && content instanceof ISettingsPage) {
                mSettingsController.setSettingsPage((ISettingsPage) content);
            }

            getShell().setText(
                    String.format("%1$s - %2$s", APP_NAME, content.getPageTitle()));

            Label filler = new Label(shell, SWT.NONE);
            GridDataBuilder.create(filler).hFill().hGrab();

            Button close = new Button(shell, SWT.PUSH);
            close.setText("Close");
            GridDataBuilder.create(close);
            close.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    close();
                }
            });
        }

        @Override
        protected void postCreate() {
            // pass
        }

        @Override
        protected void close() {
            if (mIsSettingsPage) {
                mSettingsController.setSettingsPage(null);
            }
            super.close();
        }
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java
//Synthetic comment -- index dda5b36..d70df85 100755

//Synthetic comment -- @@ -19,6 +19,7 @@

import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdkuilib.internal.repository.PackagesPage.MenuAction;
import com.android.sdkuilib.internal.repository.UpdaterPage.Purpose;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
//Synthetic comment -- @@ -104,6 +105,26 @@
}

/**
     * Creates a new window. Caller must call open(), which will block.
     * <p/>
     * This is to be used when the window is opened from {@link AvdManagerWindowImpl1}
     * to share the same {@link UpdaterData} structure.
     *
     * @param parentShell Parent shell.
     * @param updaterData The parent's updater data.
     * @param context The {@link InvocationContext} to change the behavior depending on who's
     *  opening the SDK Manager.
     */
    public UpdaterWindowImpl2(
            Shell parentShell,
            UpdaterData updaterData,
            InvocationContext context) {
        mParentShell = parentShell;
        mContext = context;
        mUpdaterData = updaterData;
    }

    /**
* Opens the window.
* @wbp.parser.entryPoint
*/
//Synthetic comment -- @@ -262,6 +283,12 @@
if (mContext == InvocationContext.STANDALONE) {
MenuItem manageAvds = new MenuItem(menuTools, SWT.NONE);
manageAvds.setText("Manage AVDs...");
            manageAvds.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent event) {
                    onAvdManager();
                }
            });
}

MenuItem manageSources = new MenuItem(menuTools,
//Synthetic comment -- @@ -505,6 +532,27 @@
}
}

    private void onAvdManager() {
        ITaskFactory oldFactory = mUpdaterData.getTaskFactory();

        try {
            AvdManagerWindowImpl1 win = new AvdManagerWindowImpl1(
                    mShell,
                    mUpdaterData,
                    AvdManagerWindowImpl1.InvocationContext.SDK_MANAGER);

            for (Pair<Class<? extends UpdaterPage>, Purpose> page : mExtraPages) {
                win.registerPage(page.getFirst(), page.getSecond());
            }

            win.open();
        } catch (Exception e) {
            mUpdaterData.getSdkLog().error(e, "AVD Manager window error");
        } finally {
            mUpdaterData.setTaskFactory(oldFactory);
        }
    }

// End of hiding from SWT Designer
//$hide<<$









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java
//Synthetic comment -- index a8c1570..83803b7 100755

//Synthetic comment -- @@ -26,7 +26,7 @@
import org.eclipse.swt.widgets.Shell;

/**
 * Opens an SDK Manager Window.
*
* This is the public entry point for using the window.
*/
//Synthetic comment -- @@ -49,12 +49,21 @@
* For SdkMan2, we also have a menu bar and link to the AVD manager.
*/
STANDALONE,

        /**
         * The SDK Manager is invoked from the standalone AVD Manager.
         * This is similar to the standalone mode except that in this case we
         * don't display a menu item linking to the AVD Manager.
         */
        AVD_MANAGER,

/**
* The SDK Manager is invoked from an IDE.
* In this mode, we do not modify the menu bar. There is no about box
* and no settings (e.g. HTTP proxy settings are inherited from Eclipse.)
*/
IDE,

/**
* The SDK Manager is invoked from the AVD Selector.
* For SdkMan1, this means the AVD page will be displayed first.







