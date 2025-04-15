/*Fix build - update references to Window & View classes

Change-Id:I083cbd235960a51f6e336e9df0c8c6aafd1a035a*/
//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/hierarchyviewer/HierarchyViewer.java b/chimpchat/src/com/android/chimpchat/hierarchyviewer/HierarchyViewer.java
//Synthetic comment -- index 285d922..5714701 100644

//Synthetic comment -- @@ -19,8 +19,9 @@
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.hierarchyviewerlib.device.DeviceBridge;
import com.android.hierarchyviewerlib.device.ViewNode;
import com.android.hierarchyviewerlib.device.Window;

import org.eclipse.swt.graphics.Point;

//Synthetic comment -- @@ -70,7 +71,7 @@

public ViewNode findViewById(String id) {
ViewNode rootNode = DeviceBridge.loadWindowData(
                new Window(mDevice, "", 0xffffffff));
if (rootNode == null) {
throw new RuntimeException("Could not dump view");
}
//Synthetic comment -- @@ -105,7 +106,7 @@
*/
public String getFocusedWindowName() {
int id = DeviceBridge.getFocusedWindow(mDevice);
        Window[] windows = DeviceBridge.loadWindows(mDevice);
for (Window w : windows) {
if (w.getHashCode() == id)
return w.getTitle();







