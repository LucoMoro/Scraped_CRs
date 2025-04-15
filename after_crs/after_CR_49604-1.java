/*Fix build - update references to Window & View classes

Change-Id:I35ae4e9d75a8a121fb94b6f56d0ffd0f06cc7468*/




//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/hierarchyviewer/HierarchyViewer.java b/chimpchat/src/com/android/chimpchat/hierarchyviewer/HierarchyViewer.java
//Synthetic comment -- index 285d922..5714701 100644

//Synthetic comment -- @@ -19,8 +19,9 @@
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.hierarchyviewerlib.device.DeviceBridge;
import com.android.hierarchyviewerlib.device.ViewServerDevice;
import com.android.hierarchyviewerlib.models.ViewNode;
import com.android.hierarchyviewerlib.models.Window;

import org.eclipse.swt.graphics.Point;

//Synthetic comment -- @@ -70,7 +71,7 @@

public ViewNode findViewById(String id) {
ViewNode rootNode = DeviceBridge.loadWindowData(
                new Window(new ViewServerDevice(mDevice), "", 0xffffffff));
if (rootNode == null) {
throw new RuntimeException("Could not dump view");
}
//Synthetic comment -- @@ -105,7 +106,7 @@
*/
public String getFocusedWindowName() {
int id = DeviceBridge.getFocusedWindow(mDevice);
        Window[] windows = DeviceBridge.loadWindows(new ViewServerDevice(mDevice), mDevice);
for (Window w : windows) {
if (w.getHashCode() == id)
return w.getTitle();







