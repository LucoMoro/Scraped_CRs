/*Mechanism to disable SwtMenuBar in SdkMan2

The SDK Manager 2 should not try to change the
menu bar when invoked from Eclipse. Actually it
can't because the SwtMenuBar lib isn't even present
to it crashes with a NoClassDef exception.

This CL adds the notion of "invocation context" so
that we know what is invoking the manager and we can
change the UI accordingly.

Change-Id:I606850a20fbc5f9d2d1d4fd0e16aa0bd71ef41c7*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/actions/AvdManagerAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/actions/AvdManagerAction.java
//Synthetic comment -- index 42739e2..ded5919 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.UpdaterWindow;
import com.android.sdkuilib.repository.UpdaterWindow.InvocationContext;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
//Synthetic comment -- @@ -45,7 +46,7 @@
}

public void run(IAction action) {
        final Sdk sdk = Sdk.getCurrent();
if (sdk != null) {

// Runs the updater window, directing all logs to the ADT console.
//Synthetic comment -- @@ -53,7 +54,8 @@
UpdaterWindow window = new UpdaterWindow(
AdtPlugin.getDisplay().getActiveShell(),
new AdtConsoleSdkLog(),
                    sdk.getSdkLocation(),
                    InvocationContext.IDE);

ISdkChangeListener listener = new ISdkChangeListener() {
public void onSdkLoaded() {
//Synthetic comment -- @@ -88,7 +90,6 @@
// then we want to make sure we don't get any attempt to use the SDK
// before the postInstallHook is called.

if (sdk != null) {
DexWrapper dx = sdk.getDexWrapper();
dx.unload();








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 8dce9b1..ccac9b7 100644

//Synthetic comment -- @@ -44,6 +44,7 @@
import com.android.sdkuilib.internal.repository.UpdaterPage;
import com.android.sdkuilib.internal.widgets.MessageBoxLog;
import com.android.sdkuilib.repository.UpdaterWindow;
import com.android.sdkuilib.repository.UpdaterWindow.InvocationContext;
import com.android.util.Pair;

import org.eclipse.swt.widgets.Display;
//Synthetic comment -- @@ -314,7 +315,8 @@
UpdaterWindow window = new UpdaterWindow(
null /* parentShell */,
errorLogger,
                    mOsSdkFolder,
                    InvocationContext.STANDALONE);
window.registerPage(SettingsPage.class, UpdaterPage.Purpose.SETTINGS);
window.registerPage(AboutPage.class,    UpdaterPage.Purpose.ABOUT_BOX);
if (autoUpdate) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/MenuBarWrapper.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/MenuBarWrapper.java
new file mode 100755
//Synthetic comment -- index 0000000..eabe603

//Synthetic comment -- @@ -0,0 +1,56 @@
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


import com.android.menubar.IMenuBarCallback;
import com.android.menubar.MenuBarEnhancer;

import org.eclipse.swt.widgets.Menu;

/**
 * A simple wrapper/delegate around the {@link MenuBarEnhancer}.
 *
 * The {@link MenuBarEnhancer} and {@link IMenuBarCallback} classes are only
 * available when the SwtMenuBar library is available too. This wrapper helps
 * {@link UpdaterWindowImpl2} make the call conditional, otherwise the updater
 * window class would fail to load when the SwtMenuBar library isn't found.
 */
abstract class MenuBarWrapper {

    public MenuBarWrapper(String appName, Menu menu) {
        MenuBarEnhancer.setupMenu(appName, menu, new IMenuBarCallback() {
            public void onPreferencesMenuSelected() {
                MenuBarWrapper.this.onPreferencesMenuSelected();
            }

            public void onAboutMenuSelected() {
                MenuBarWrapper.this.onAboutMenuSelected();
            }

            public void printError(String format, Object... args) {
                MenuBarWrapper.this.printError(format, args);
            }
        });
    }

    abstract public void onPreferencesMenuSelected();

    abstract public void onAboutMenuSelected();

    abstract public void printError(String format, Object... args);
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java
//Synthetic comment -- index 90534e0..7a7cc6d 100755

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressTaskFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.UpdaterWindow.InvocationContext;
import com.android.util.Pair;

import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -86,8 +87,14 @@
* @param parentShell Parent shell.
* @param sdkLog Logger. Cannot be null.
* @param osSdkRoot The OS path to the SDK root.
     * @param context The {@link InvocationContext} to change the behavior depending on who's
     *  opening the SDK Manager. Unused for SdkMan1.
*/
    public UpdaterWindowImpl(
            Shell parentShell,
            ISdkLog sdkLog,
            String osSdkRoot,
            InvocationContext context/*unused*/) {
mParentShell = parentShell;
mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java
//Synthetic comment -- index abbb3ef..ee81897 100755

//Synthetic comment -- @@ -17,8 +17,6 @@
package com.android.sdkuilib.internal.repository;


import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdkuilib.internal.repository.PackagesPage.MenuAction;
//Synthetic comment -- @@ -27,6 +25,7 @@
import com.android.sdkuilib.internal.tasks.ProgressView;
import com.android.sdkuilib.internal.tasks.ProgressViewFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.UpdaterWindow.InvocationContext;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;
import com.android.sdkuilib.ui.SwtBaseDialog;
//Synthetic comment -- @@ -68,6 +67,7 @@

private static final String APP_NAME = "Android SDK Manager";
private final Shell mParentShell;
    private final InvocationContext mContext;
/** Internal data shared between the window and its pages. */
private final UpdaterData mUpdaterData;
/** A list of extra pages to instantiate. Each entry is an object array with 2 elements:
//Synthetic comment -- @@ -92,9 +92,16 @@
* @param parentShell Parent shell.
* @param sdkLog Logger. Cannot be null.
* @param osSdkRoot The OS path to the SDK root.
     * @param context The {@link InvocationContext} to change the behavior depending on who's
     *  opening the SDK Manager.
*/
    public UpdaterWindowImpl2(
            Shell parentShell,
            ISdkLog sdkLog,
            String osSdkRoot,
            InvocationContext context) {
mParentShell = parentShell;
        mContext = context;
mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);
}

//Synthetic comment -- @@ -254,8 +261,10 @@
Menu menuTools = new Menu(menuBarTools);
menuBarTools.setMenu(menuTools);

        if (mContext == InvocationContext.STANDALONE) {
            MenuItem manageAvds = new MenuItem(menuTools, SWT.NONE);
            manageAvds.setText("Manage AVDs...");
        }

MenuItem manageSources = new MenuItem(menuTools,
MenuAction.SHOW_ADDON_SITES.getMenuStyle());
//Synthetic comment -- @@ -264,24 +273,32 @@
mPkgPage.registerMenuAction(
MenuAction.SHOW_ADDON_SITES, manageSources);

        if (mContext == InvocationContext.STANDALONE) {
            // Note: when invoked from an IDE, the SwtMenuBar library isn't
            // available. This means this source should not directly import
            // any of SwtMenuBar classes, otherwise the whole window class
            // would fail to load. The MenuBarWrapper below helps to make
            // that indirection.

            new MenuBarWrapper(APP_NAME, menuTools) {
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








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 1f68471..f2cd67d 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressTask;
import com.android.sdkuilib.repository.UpdaterWindow;
import com.android.sdkuilib.repository.UpdaterWindow.InvocationContext;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
//Synthetic comment -- @@ -1011,7 +1012,8 @@
UpdaterWindow window = new UpdaterWindow(
mTable.getShell(),
log,
                mAvdManager.getSdkManager().getLocation(),
                InvocationContext.AVD_SELECTOR);
window.open();
refresh(true /*reload*/); // UpdaterWindow uses its own AVD manager so this one must reload.









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java
//Synthetic comment -- index 8121b5a..a8c1570 100755

//Synthetic comment -- @@ -32,22 +32,57 @@
*/
public class UpdaterWindow {

    /** The actual window implement to which this class delegates. */
private IUpdaterWindow mWindow;

/**
     * Enum giving some indication of what is invoking this window.
     * The behavior and UI will change slightly depending on the context.
     * <p/>
     * Note: if you add Android support to your specific IDE, you might want
     * to specialize this context enum.
     */
    public enum InvocationContext {
        /**
         * The SDK Manager is invoked from the stand-alone 'android' tool.
         * In this mode, we present an about box, a settings page.
         * For SdkMan2, we also have a menu bar and link to the AVD manager.
         */
        STANDALONE,
        /**
         * The SDK Manager is invoked from an IDE.
         * In this mode, we do not modify the menu bar. There is no about box
         * and no settings (e.g. HTTP proxy settings are inherited from Eclipse.)
         */
        IDE,
        /**
         * The SDK Manager is invoked from the AVD Selector.
         * For SdkMan1, this means the AVD page will be displayed first.
         * For SdkMan2, we won't be using this.
         */
        AVD_SELECTOR
    }

    /**
* Creates a new window. Caller must call open(), which will block.
*
* @param parentShell Parent shell.
* @param sdkLog Logger. Cannot be null.
* @param osSdkRoot The OS path to the SDK root.
     * @param context The {@link InvocationContext} to change the behavior depending on who's
     *  opening the SDK Manager.
*/
    public UpdaterWindow(
            Shell parentShell,
            ISdkLog sdkLog,
            String osSdkRoot,
            InvocationContext context) {

// TODO right now the new PackagesPage is experimental and not enabled by default
if (System.getenv("ANDROID_SDKMAN_EXP") != null) {  //$NON-NLS-1$
            mWindow = new UpdaterWindowImpl2(parentShell, sdkLog, osSdkRoot, context);
} else {
            mWindow = new UpdaterWindowImpl(parentShell, sdkLog, osSdkRoot, context);
}
}








