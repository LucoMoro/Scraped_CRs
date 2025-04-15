/*SDK Manager: fix SWTMenuBar crash on MacOS X Mountain Lion.

The bug happens when the SDK Manager is open using the embedded
class from within Eclipse 4.2 -- the SWT used is then the newer
3.7 which doesn't have the same cocoa interface for menus.

The fix is threefold:
- If the cocoa enhancer fails, revert to the generic one. This doesn't
  quite help here since the crash is actually when the native handler
  is invoked but the setup is fine.
- Implement a new enhancer on top of the newer SWT 3.7 APIs that lets
  us access the About and Preference menus on Mac. That was the whole
  point of the enhancer workaround since SWT 3.5 an 3.6 don't have
  access to these menus directly. This new enhancer is used if SWT
  version 3700+ is used on Mac.
- Finally the crash only happens when using the embedded version of the
  SDK Manager within ADT. When possible this uses the forked
  standalone version. It will still revert to the embedded one if ADT
  is started without any tools.

The new enhancer for 3.7 on Mac is sub-optimal: since it *adds*
a listener to the about/preference Mac menus, that means when it is
invoked from within Eclipse these menus will generate 2 events,
e.g. bringing both the Eclipse preferences and then the SDK Manager
preferences. To support this case properly, we should detect this
runs from within Eclipse, not change the about menu and instead
integrate the sdk manager pref as a panel in eclipse's prefs. A
cheaper workaround is to revert to the default generic enhancer
that will create an options menu. Maybe for a later CL.

SDK Bug: 38640

Change-Id:Ib1588e401616548a5dc9eb216d3c35b579d3950b*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index 9e17f0f..f7c0645 100644

//Synthetic comment -- @@ -80,6 +80,7 @@
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
//Synthetic comment -- @@ -1197,14 +1198,13 @@
}

private void openSdkManager() {
                // Open the standalone external SDK Manager since we know
// that ADT on Windows is bound to be locking some SDK folders.
                //
                // Also when this is invoked because SdkManagerAction.run() fails, this
// test will fail and we'll fallback on using the internal one.
                if (SdkManagerAction.openExternalSdkManager()) {
                    return;
}

// Otherwise open the regular SDK Manager bundled within ADT








//Synthetic comment -- diff --git a/swtmenubar/src-darwin/com/android/menubar/internal/MenuBarEnhancerCocoa.java b/swtmenubar/src-darwin/com/android/menubar/internal/MenuBarEnhancerCocoa.java
//Synthetic comment -- index 170603a..88d230f 100644

//Synthetic comment -- @@ -234,7 +234,7 @@
// invoked.
//
// We don't need to set the target here as the current target is the
        // SWTApplicationDelegate and we have registered the new selectors on
// it. So just set the new action to invoke the selector.
invoke(nsMenuitemCls, prefMenuItem, "setAction",
new Object[] {








//Synthetic comment -- diff --git a/swtmenubar/src/com/android/menubar/MenuBarEnhancer.java b/swtmenubar/src/com/android/menubar/MenuBarEnhancer.java
//Synthetic comment -- index 7ca6471..9f1e748 100644

//Synthetic comment -- @@ -26,6 +26,8 @@
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.android.menubar.IMenuBarEnhancer.MenuBarMode;


/**
* On Mac, {@link MenuBarEnhancer#setupMenu} plugs a listener on the About and the
//Synthetic comment -- @@ -55,7 +57,7 @@
*          called "Tools". Must not be null.
* @param callbacks Callbacks called when "About" and "Preferences" menu items are invoked.
*          Must not be null.
     * @return A actual {@link IMenuBarEnhancer} implementation. Can be null on failure.
*          This is currently not of any use for the caller but is left in case
*          we want to expand the functionality later.
*/
//Synthetic comment -- @@ -64,61 +66,80 @@
final Menu swtMenu,
IMenuBarCallback callbacks) {

        IMenuBarEnhancer enhancer = getEnhancer(callbacks, swtMenu.getDisplay());

// Default implementation for generic platforms
if (enhancer == null) {
            enhancer = getGenericEnhancer(swtMenu);
}

        try {
            enhancer.setupMenu(appName, swtMenu.getDisplay(), callbacks);
        } catch (Exception e) {
            // If the enhancer failed, try to fall back on the generic one
            if (enhancer.getMenuBarMode() != MenuBarMode.GENERIC) {
                enhancer = getGenericEnhancer(swtMenu);
                try {
                    enhancer.setupMenu(appName, swtMenu.getDisplay(), callbacks);
                } catch (Exception e2) {
                    callbacks.printError("SWTMenuBar failed: %s", e2.toString());
                    return null;
                }
            }
        }
        return enhancer;
    }

    private static IMenuBarEnhancer getGenericEnhancer(final Menu swtMenu) {
        IMenuBarEnhancer enhancer;
        enhancer = new IMenuBarEnhancer() {

            public MenuBarMode getMenuBarMode() {
                return MenuBarMode.GENERIC;
            }

            public void setupMenu(
                    String appName,
                    Display display,
                    final IMenuBarCallback callbacks) {
                if (swtMenu.getItemCount() > 0) {
                    new MenuItem(swtMenu, SWT.SEPARATOR);
                }

                // Note: we use "Preferences" on Mac and "Options" on Windows/Linux.
                final MenuItem pref = new MenuItem(swtMenu, SWT.NONE);
                pref.setText("&Options...");

                final MenuItem about = new MenuItem(swtMenu, SWT.NONE);
                about.setText("&About...");

                pref.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        try {
                            pref.setEnabled(false);
                            callbacks.onPreferencesMenuSelected();
                            super.widgetSelected(e);
                        } finally {
                            pref.setEnabled(true);
                        }
                    }
                });

                about.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        try {
                            about.setEnabled(false);
                            callbacks.onAboutMenuSelected();
                            super.widgetSelected(e);
                        } finally {
                            about.setEnabled(true);
                        }
                    }
                });
            }
        };
return enhancer;
}

//Synthetic comment -- @@ -149,7 +170,7 @@
}
};

        IMenuBarEnhancer enhancer = getEnhancer(callbacks, display);

// Default implementation for generic platforms
if (enhancer == null) {
//Synthetic comment -- @@ -187,12 +208,16 @@
return enhancer;
}

    private static IMenuBarEnhancer getEnhancer(IMenuBarCallback callbacks, Display display) {
IMenuBarEnhancer enhancer = null;
String p = SWT.getPlatform();
String className = null;
if ("cocoa".equals(p)) {                                                  //$NON-NLS-1$
className = "com.android.menubar.internal.MenuBarEnhancerCocoa";      //$NON-NLS-1$

            if (SWT.getVersion() >= 3700 && MenuBarEnhancer37.isSupported(display)) {
                className = MenuBarEnhancer37.class.getName();
            }
}

if (System.getenv("DEBUG_SWTMENUBAR") != null) {








//Synthetic comment -- diff --git a/swtmenubar/src/com/android/menubar/MenuBarEnhancer37.java b/swtmenubar/src/com/android/menubar/MenuBarEnhancer37.java
new file mode 100644
//Synthetic comment -- index 0000000..4b51296

//Synthetic comment -- @@ -0,0 +1,143 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * References:
 * Based on the SWT snippet example at
 * http://dev.eclipse.org/viewcvs/viewvc.cgi/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet354.java?view=co
 */

package com.android.menubar;


import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import java.lang.reflect.Method;

public class MenuBarEnhancer37 implements IMenuBarEnhancer {

    private static final int kAboutMenuItem = -1;           // SWT.ID_ABOUT       in SWT 3.7
    private static final int kPreferencesMenuItem = -2;     // SWT.ID_PREFERENCES in SWT 3.7
    private static final int kQuitMenuItem = -6;            // SWT.ID_QUIT        in SWT 3.7

    public MenuBarEnhancer37() {
    }

    public MenuBarMode getMenuBarMode() {
        return MenuBarMode.MAC_OS;
    }

    /**
     * Setup the About and Preferences native menut items with the
     * given application name and links them to the callback.
     *
     * @param appName The application name.
     * @param display The SWT display. Must not be null.
     * @param callbacks The callbacks invoked by the menus.
     */
    public void setupMenu(
            String appName,
            Display display,
            IMenuBarCallback callbacks) {

        try {
            // Initialize the menuItems.
            initialize(display, appName, callbacks);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        // Schedule disposal of callback object
        display.disposeExec(new Runnable() {
            public void run() {
            }
        });
    }

    public static boolean isSupported(Display display) {
        try {
            Object sysMenu = call0(display, "getSystemMenu");
            if (sysMenu instanceof Menu) {
                return findMenuById((Menu)sysMenu, kPreferencesMenuItem) != null &&
                       findMenuById((Menu)sysMenu, kAboutMenuItem) != null;
            }
        } catch (Exception ignore) {}
        return false;
    }

    private void initialize(
            Display display,
            String appName,
            final IMenuBarCallback callbacks)
                    throws Exception {
        Object sysMenu = call0(display, "getSystemMenu");
        if (sysMenu instanceof Menu) {
            MenuItem menu = findMenuById((Menu)sysMenu, kPreferencesMenuItem);
            if (menu != null) {
                menu.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent event) {
                        callbacks.onPreferencesMenuSelected();
                    }
                });
            }

            menu = findMenuById((Menu)sysMenu, kAboutMenuItem);
            if (menu != null) {
                menu.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent event) {
                        callbacks.onAboutMenuSelected();
                    }
                });
                menu.setText("About " + appName);
            }

            menu = findMenuById((Menu)sysMenu, kQuitMenuItem);
            if (menu != null) {
                // We already support the "quit" operation, no need for an extra handler here.
                menu.setText("Quit " + appName);
            }

        }
    }

    private static Object call0(Object obj, String method) {
        try {
            Method m = obj.getClass().getMethod(method, (Class<?>[])null);
            if (m != null) {
                return m.invoke(obj, (Object[])null);
            }
        } catch (Exception ignore) {}
        return null;
    }

    private static MenuItem findMenuById(Menu menu, int id) {
        MenuItem[] items = menu.getItems();
        for (int i = items.length - 1; i >= 0; i--) {
            MenuItem item = items[i];
            Object menuId = call0(item, "getID");
            if (menuId instanceof Integer) {
                if (((Integer) menuId).intValue() == id) {
                    return item;
                }
            }
        }
        return null;
    }
}







