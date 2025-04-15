/*Cleanup IUpdaterWindow in SdkManager.

The interface was supposed to be for the internal
window implementation. The public UpdaterWindow
does not need this interface.

Change-Id:Iddeb46fd7ac6988e9b880fb9fadace4d4d8693a4*/




//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index d3e3235..8dce9b1 100644

//Synthetic comment -- @@ -43,7 +43,6 @@
import com.android.sdkuilib.internal.repository.UpdateNoWindow;
import com.android.sdkuilib.internal.repository.UpdaterPage;
import com.android.sdkuilib.internal.widgets.MessageBoxLog;
import com.android.sdkuilib.repository.UpdaterWindow;
import com.android.util.Pair;

//Synthetic comment -- @@ -312,7 +311,7 @@
Display.getCurrent(),
true /*logErrorsOnly*/);

            UpdaterWindow window = new UpdaterWindow(
null /* parentShell */,
errorLogger,
mOsSdkFolder);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/IUpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/IUpdaterWindow.java
similarity index 92%
rename from sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/IUpdaterWindow.java
rename to sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/IUpdaterWindow.java
//Synthetic comment -- index def5de0..2bfff2a 100755

//Synthetic comment -- @@ -14,12 +14,15 @@
* limitations under the License.
*/

package com.android.sdkuilib.internal.repository;

import com.android.sdkuilib.repository.ISdkChangeListener;

import org.eclipse.swt.widgets.Composite;

/**
 * Interface for the actual implementation of the Update Window.
 */
public interface IUpdaterWindow {

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl.java
//Synthetic comment -- index b148b4e..90534e0 100755

//Synthetic comment -- @@ -23,7 +23,6 @@
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressTaskFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.util.Pair;

import org.eclipse.swt.SWT;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java
//Synthetic comment -- index 43ec7cc..abbb3ef 100755

//Synthetic comment -- @@ -27,7 +27,6 @@
import com.android.sdkuilib.internal.tasks.ProgressView;
import com.android.sdkuilib.internal.tasks.ProgressViewFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;
import com.android.sdkuilib.ui.SwtBaseDialog;
//Synthetic comment -- @@ -265,6 +264,7 @@
mPkgPage.registerMenuAction(
MenuAction.SHOW_ADDON_SITES, manageSources);

        // TODO: When invoked from Eclipse, we actually don't want to change the menu bar.
MenuBarEnhancer.setupMenu(APP_NAME, menuTools, new IMenuBarCallback() {
public void onPreferencesMenuSelected() {
showRegisteredPage(Purpose.SETTINGS);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 56f2c7e..1f68471 100644

//Synthetic comment -- @@ -28,7 +28,6 @@
import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressTask;
import com.android.sdkuilib.repository.UpdaterWindow;

import org.eclipse.jface.dialogs.MessageDialog;
//Synthetic comment -- @@ -1009,7 +1008,7 @@
log = new MessageBoxLog("Result of SDK Manager", display, true /*logErrorsOnly*/);
}

        UpdaterWindow window = new UpdaterWindow(
mTable.getShell(),
log,
mAvdManager.getSdkManager().getLocation());








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/repository/UpdaterWindow.java
//Synthetic comment -- index 0fd0db2..8121b5a 100755

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdkuilib.repository;

import com.android.sdklib.ISdkLog;
import com.android.sdkuilib.internal.repository.IUpdaterWindow;
import com.android.sdkuilib.internal.repository.UpdaterPage;
import com.android.sdkuilib.internal.repository.UpdaterWindowImpl;
import com.android.sdkuilib.internal.repository.UpdaterWindowImpl2;
//Synthetic comment -- @@ -27,9 +28,9 @@
/**
* Opens an SDK Updater Window.
*
 * This is the public entry point for using the window.
*/
public class UpdaterWindow {

private IUpdaterWindow mWindow;

//Synthetic comment -- @@ -87,6 +88,7 @@

/**
* Adds a new listener to be notified when a change is made to the content of the SDK.
     * This should be called before {@link #open()}.
*/
public void addListener(ISdkChangeListener listener) {
mWindow.addListener(listener);







