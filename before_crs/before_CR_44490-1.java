/*WIP -- AVD: display devices in a tab+list.

Change-Id:I70c6e0fa0b9622e8050e5d949674377e5ac0ffad*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/DeviceMenuListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/DeviceMenuListener.java
//Synthetic comment -- index 32f8e9d..95aeb4c 100644

//Synthetic comment -- @@ -205,9 +205,9 @@
//   same space
// * Add in screen resolution and density
String name = d.getName();
        if (name.equals("3.7 FWVGA slider")) {
// Fix metadata: this one entry doesn't have "in" like the rest of them
            name = "3.7in FWVGA slider";
}

Matcher matcher = PATTERN.matcher(name);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AddonSitesDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AddonSitesDialog.java
//Synthetic comment -- index 2b9f072..77f82b1 100755

//Synthetic comment -- @@ -54,7 +54,6 @@
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
//Synthetic comment -- @@ -110,7 +109,6 @@
shell.setSize(600, 400);

TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
        tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
GridDataBuilder.create(tabFolder).fill().grab().hSpan(2);

TabItem sitesTabItem = new TabItem(tabFolder, SWT.NONE);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AvdManagerWindowImpl1.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AvdManagerWindowImpl1.java
//Synthetic comment -- index ae6ba1c..048ba58 100755

//Synthetic comment -- @@ -31,6 +31,8 @@
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.SdkUpdaterWindow;
import com.android.utils.ILogger;

import org.eclipse.jface.window.Window;
//Synthetic comment -- @@ -40,12 +42,14 @@
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;
import java.util.HashMap;
//Synthetic comment -- @@ -169,10 +173,6 @@
});

GridLayout glShell = new GridLayout(2, false);
        glShell.verticalSpacing = 0;
        glShell.horizontalSpacing = 0;
        glShell.marginWidth = 0;
        glShell.marginHeight = 0;
mShell.setLayout(glShell);

mShell.setMinimumSize(new Point(500, 300));
//Synthetic comment -- @@ -184,8 +184,36 @@

private void createContents() {

        mAvdPage = new AvdManagerPage(mShell, SWT.NONE, mUpdaterData, mDeviceManager);
        mAvdPage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
}

@SuppressWarnings("unused")








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java
new file mode 100755
//Synthetic comment -- index 0000000..447309e

//Synthetic comment -- @@ -0,0 +1,528 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index ab8e1c9..42d85eb 100644

//Synthetic comment -- @@ -438,6 +438,7 @@
public void setSettingsController(SettingsController controller) {
mController = controller;
}
/**
* Sets the table grid layout data.
*








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/ui/GridLayoutBuilder.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/ui/GridLayoutBuilder.java
//Synthetic comment -- index fbb31ce..7e8c161 100755

//Synthetic comment -- @@ -30,7 +30,7 @@
*/
public final class GridLayoutBuilder {

    private static GridLayout mGL;

private GridLayoutBuilder() {
mGL = new GridLayout();
//Synthetic comment -- @@ -41,7 +41,7 @@
*/
static public GridLayoutBuilder create(Composite parent) {
GridLayoutBuilder glh = new GridLayoutBuilder();
        parent.setLayout(GridLayoutBuilder.mGL);
return glh;
}








