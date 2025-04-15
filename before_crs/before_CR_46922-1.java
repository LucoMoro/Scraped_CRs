/*UI Automator Viewer improvements

* Non-root device needs to do dexopt into alternative dalvik
  cache when uiautomator is called for the very first time; This
  may incidentally lead to long response time from XML dump via
  UI Automator Viewer; The 40s timeout is long enough but it was
  actually not passed into IDevice#executeShellCommand()

* Dumped XML has "rotation" attribute on root node since API
  Level 17, we now make use of this to rotate captured screenshot
  image

* Fixed an Eclipse warning on unclosed InputStream

Bug: 7564716
Bug: 7564146
Change-Id:Ie46968aff323ed392990febfd89e18f2dbd099de*/
//Synthetic comment -- diff --git a/uiautomatorviewer/src/com/android/uiautomator/UiAutomatorHelper.java b/uiautomatorviewer/src/com/android/uiautomator/UiAutomatorHelper.java
//Synthetic comment -- index 6011fed..8ead9de 100644

//Synthetic comment -- @@ -20,6 +20,8 @@
import com.android.ddmlib.IDevice;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.SyncService;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
//Synthetic comment -- @@ -41,6 +43,7 @@
private static final String UIAUTOMATOR = "/system/bin/uiautomator";    //$NON-NLS-1$
private static final String UIAUTOMATOR_DUMP_COMMAND = "dump";          //$NON-NLS-1$
private static final String UIDUMP_DEVICE_PATH = "/data/local/tmp/uidump.xml";  //$NON-NLS-1$

private static boolean supportsUiAutomator(IDevice device) {
String apiLevelString = device.getProperty(IDevice.PROP_BUILD_API_LEVEL);
//Synthetic comment -- @@ -78,9 +81,11 @@
CountDownLatch commandCompleteLatch = new CountDownLatch(1);

try {
            device.executeShellCommand(command,
                    new CollectingOutputReceiver(commandCompleteLatch));
            commandCompleteLatch.await(40, TimeUnit.SECONDS);

monitor.subTask("Pull UI XML snapshot from device...");
device.getSyncService().pullFile(UIDUMP_DEVICE_PATH,
//Synthetic comment -- @@ -149,6 +154,13 @@
throw new UiAutomatorException(msg, e);
}

PaletteData palette = new PaletteData(
rawImage.getRedMask(),
rawImage.getGreenMask(),








//Synthetic comment -- diff --git a/uiautomatorviewer/src/com/android/uiautomator/actions/ImageHelper.java b/uiautomatorviewer/src/com/android/uiautomator/actions/ImageHelper.java
//Synthetic comment -- index c22f1fd..603b226 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

import java.io.InputStream;

public class ImageHelper {
//Synthetic comment -- @@ -32,6 +33,11 @@
try {
data = new ImageLoader().load(is);
} catch (SWTException e) {
}
if (data != null && data.length > 0) {
return ImageDescriptor.createFromImageData(data[0]);








//Synthetic comment -- diff --git a/uiautomatorviewer/src/com/android/uiautomator/tree/RootWindowNode.java b/uiautomatorviewer/src/com/android/uiautomator/tree/RootWindowNode.java
//Synthetic comment -- index 27a21e4..d0e27c9 100644

//Synthetic comment -- @@ -22,9 +22,15 @@

private final String mWindowName;
private Object[] mCachedAttributesArray;

public RootWindowNode(String windowName) {
mWindowName = windowName;
}

@Override
//Synthetic comment -- @@ -39,4 +45,8 @@
}
return mCachedAttributesArray;
}
}








//Synthetic comment -- diff --git a/uiautomatorviewer/src/com/android/uiautomator/tree/UiHierarchyXmlLoader.java b/uiautomatorviewer/src/com/android/uiautomator/tree/UiHierarchyXmlLoader.java
//Synthetic comment -- index f2339d1..2e897d9 100644

//Synthetic comment -- @@ -73,7 +73,17 @@
// will be the parent of the element that is being started here
mParentNode = mWorkingNode;
if ("hierarchy".equals(qName)) {
                    mWorkingNode = new RootWindowNode(attributes.getValue("windowName"));
nodeCreated = true;
} else if ("node".equals(qName)) {
UiNode tmpNode = new UiNode();







