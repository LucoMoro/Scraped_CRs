/*Fix ddms filechooser behavior and Mac accelerator

Fix a couple of issues in ddms:

First, fix the code to remember the path you last opened the file
chooser with; if there is nothing in $lastImageSaveDir, it should look
at $imageSaveDir (which is the variable the default setting
($user.home) is initialized into).

Second, and this seems to be Mac specific, the
FileDialog.getFilterPath() call does not return the path you have
navigated to, which means that on the Mac it never sets
$lastImageSaveDir correctly - it always sets it to the original
suggestion. The fix is trivial - use File#getParent instead which does
the String manipulation to extract the parent portion of a string
which represents a path.

Finally, on Macs (only), make the keybindings use the Command key
instead of the Control key since that's the norm.

Change-Id:I1b0f381606f5373ddad973754e49ce07856a9bae*/
//Synthetic comment -- diff --git a/ddms/app/src/com/android/ddms/Main.java b/ddms/app/src/com/android/ddms/Main.java
//Synthetic comment -- index cd01fc9..bbc685a 100644

//Synthetic comment -- @@ -57,8 +57,7 @@
*/
public static void main(String[] args) {
// In order to have the AWT/SWT bridge work on Leopard, we do this little hack.
        String os = System.getProperty("os.name"); //$NON-NLS-1$
        if (os.startsWith("Mac OS")) { //$NON-NLS-1$
RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
System.setProperty(
"JAVA_STARTED_ON_FIRST_THREAD_" + (rt.getName().split("@"))[0], //$NON-NLS-1$
//Synthetic comment -- @@ -109,6 +108,14 @@
System.exit(0);
}

public static void ping(String ddmsParentLocation) {
Properties p = new Properties();
try{








//Synthetic comment -- diff --git a/ddms/app/src/com/android/ddms/UIThread.java b/ddms/app/src/com/android/ddms/UIThread.java
//Synthetic comment -- index d9ea3f1..4e2bb06 100644

//Synthetic comment -- @@ -718,7 +718,7 @@

item = new MenuItem(fileMenu, SWT.NONE);
item.setText("E&xit\tCtrl-Q");
        item.setAccelerator('Q' | SWT.CONTROL);
item.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
//Synthetic comment -- @@ -780,8 +780,13 @@

// create Device menu items
final MenuItem screenShotItem = new MenuItem(deviceMenu, SWT.NONE);
screenShotItem.setText("&Screen capture...\tCtrl-S");
        screenShotItem.setAccelerator('S' | SWT.CONTROL);
screenShotItem.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/ScreenShotDialog.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/ScreenShotDialog.java
//Synthetic comment -- index a0e9cbd..8e6a581 100644

//Synthetic comment -- @@ -41,6 +41,7 @@
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import java.io.IOException;


//Synthetic comment -- @@ -285,7 +286,12 @@

dlg.setText("Save image...");
dlg.setFileName("device.png");
        dlg.setFilterPath(DdmUiPreferences.getStore().getString("lastImageSaveDir"));
dlg.setFilterNames(new String[] {
"PNG Files (*.png)"
});
//Synthetic comment -- @@ -295,7 +301,16 @@

fileName = dlg.open();
if (fileName != null) {
            DdmUiPreferences.getStore().setValue("lastImageSaveDir", dlg.getFilterPath());

Log.d("ddms", "Saving image to " + fileName);
ImageData imageData = mImageLabel.getImage().getImageData();







