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
//Synthetic comment -- index 9e17f0f..e08d7ea 100644

//Synthetic comment -- @@ -1197,14 +1197,13 @@
}

private void openSdkManager() {
                // Windows only: open the standalone external SDK Manager since we know
// that ADT on Windows is bound to be locking some SDK folders.
                // Also when this is invoked becasue SdkManagerAction.run() fails, this
// test will fail and we'll fallback on using the internal one.
                if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS) {
                    if (SdkManagerAction.openExternalSdkManager()) {
                        return;
                    }
}

// Otherwise open the regular SDK Manager bundled within ADT








//Synthetic comment -- diff --git a/swtmenubar/src-darwin/com/android/menubar/internal/MenuBarEnhancerCocoa.java b/swtmenubar/src-darwin/com/android/menubar/internal/MenuBarEnhancerCocoa.java
//Synthetic comment -- index 170603a..88d230f 100644

//Synthetic comment -- @@ -234,7 +234,7 @@
// invoked.
//
// We don't need to set the target here as the current target is the
        // SWTApplicationDelegate and we have registerd the new selectors on
// it. So just set the new action to invoke the selector.
invoke(nsMenuitemCls, prefMenuItem, "setAction",
new Object[] {








//Synthetic comment -- diff --git a/swtmenubar/src/com/android/menubar/MenuBarEnhancer.java b/swtmenubar/src/com/android/menubar/MenuBarEnhancer.java
//Synthetic comment -- index 7ca6471..7575ecd 100644

//Synthetic comment -- @@ -26,6 +26,8 @@
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;


/**
* On Mac, {@link MenuBarEnhancer#setupMenu} plugs a listener on the About and the
//Synthetic comment -- @@ -55,7 +57,7 @@
*          called "Tools". Must not be null.
* @param callbacks Callbacks called when "About" and "Preferences" menu items are invoked.
*          Must not be null.
     * @return A actual {@link IMenuBarEnhancer} implementation. Never null.
*          This is currently not of any use for the caller but is left in case
*          we want to expand the functionality later.
*/
//Synthetic comment -- @@ -64,61 +66,80 @@
final Menu swtMenu,
IMenuBarCallback callbacks) {

        IMenuBarEnhancer enhancer = getEnhancer(callbacks);

// Default implementation for generic platforms
if (enhancer == null) {
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
}

        enhancer.setupMenu(appName, swtMenu.getDisplay(), callbacks);
return enhancer;
}

//Synthetic comment -- @@ -149,7 +170,7 @@
}
};

        IMenuBarEnhancer enhancer = getEnhancer(callbacks);

// Default implementation for generic platforms
if (enhancer == null) {
//Synthetic comment -- @@ -187,12 +208,16 @@
return enhancer;
}

    private static IMenuBarEnhancer getEnhancer(IMenuBarCallback callbacks) {
IMenuBarEnhancer enhancer = null;
String p = SWT.getPlatform();
String className = null;
if ("cocoa".equals(p)) {                                                  //$NON-NLS-1$
className = "com.android.menubar.internal.MenuBarEnhancerCocoa";      //$NON-NLS-1$
}

if (System.getenv("DEBUG_SWTMENUBAR") != null) {








//Synthetic comment -- diff --git a/swtmenubar/src/com/android/menubar/MenuBarEnhancer37.java b/swtmenubar/src/com/android/menubar/MenuBarEnhancer37.java
new file mode 100644
//Synthetic comment -- index 0000000..8560bfa

//Synthetic comment -- @@ -0,0 +1,153 @@







