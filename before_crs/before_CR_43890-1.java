/*SDK Manager: settings dialog not high enough on Mac/Linux.

SDK Bug: 38067

Change-Id:I92c1ca9f59d3dba29f955c47b062fde771c12ed7*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SettingsDialog.java
//Synthetic comment -- index 7f44b7c..f05be4c 100755

//Synthetic comment -- @@ -27,6 +27,7 @@
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
//Synthetic comment -- @@ -74,6 +75,14 @@
}

@Override
protected void createContents() {
super.createContents();
Shell shell = getShell();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterBaseDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterBaseDialog.java
//Synthetic comment -- index 7752838..02b39b4 100755

//Synthetic comment -- @@ -61,7 +61,6 @@
setWindowImage(shell);

GridLayoutBuilder.create(shell).columns(2);

}

protected void createCloseButton() {







