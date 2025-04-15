/*working version after refactoring

Change-Id:I930be2bfcd60611ebdda52345303d5785203b678*/




//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/JythonUtils.java b/monkeyrunner/src/com/android/monkeyrunner/JythonUtils.java
//Synthetic comment -- index badddff..cf56eb2 100644

//Synthetic comment -- @@ -287,7 +287,6 @@

// And remove anything that starts with __, as those are pretty important to retain
functions = Collections2.filter(functions, new Predicate<String>() {
public boolean apply(String value) {
return !value.startsWith("__");
}
//Synthetic comment -- @@ -333,13 +332,11 @@
}

private static final Predicate<AccessibleObject> SHOULD_BE_DOCUMENTED = new Predicate<AccessibleObject>() {
public boolean apply(AccessibleObject ao) {
return ao.isAnnotationPresent(MonkeyRunnerExported.class);
}
};
private static final Predicate<Field> IS_FIELD_STATIC = new Predicate<Field>() {
public boolean apply(Field f) {
return (f.getModifiers() & Modifier.STATIC) != 0;
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java
//Synthetic comment -- index 37b1cda..654e49a 100644

//Synthetic comment -- @@ -20,8 +20,6 @@
import java.util.Map;
import java.util.Set;

import org.python.core.ArgParser;
import org.python.core.ClassDictInit;
import org.python.core.Py;
//Synthetic comment -- @@ -30,6 +28,8 @@
import org.python.core.PyObject;
import org.python.core.PyTuple;

import com.android.monkeyrunner.core.IMonkeyDevice;
import com.android.monkeyrunner.core.IMonkeyImage;
import com.android.monkeyrunner.doc.MonkeyRunnerExported;
import com.android.monkeyrunner.easy.HierarchyViewer;

//Synthetic comment -- @@ -45,7 +45,7 @@
* implementation of this class.
*/
@MonkeyRunnerExported(doc = "Represents a device attached to the system.")
public class MonkeyDevice extends PyObject implements ClassDictInit {
public static void classDictInit(PyObject dict) {
JythonUtils.convertDocAnnotationsForClass(MonkeyDevice.class, dict);
}
//Synthetic comment -- @@ -57,49 +57,41 @@
@MonkeyRunnerExported(doc = "Sends a DOWN event, immediately followed by an UP event when used with touch() or press()")
public static final String DOWN_AND_UP = "downAndUp";

    // TODO: is this really necessary; remove if not accessible from jython
    /*public enum TouchPressType {
DOWN, UP, DOWN_AND_UP,
    }*/

    public static final Map<String, IMonkeyDevice.TouchPressType> TOUCH_NAME_TO_ENUM =
        ImmutableMap.of(DOWN, IMonkeyDevice.TouchPressType.DOWN,
                UP, IMonkeyDevice.TouchPressType.UP,
                DOWN_AND_UP, IMonkeyDevice.TouchPressType.DOWN_AND_UP);

private static final Set<String> VALID_DOWN_UP_TYPES = TOUCH_NAME_TO_ENUM.keySet();

    private IMonkeyDevice impl;
    
    public MonkeyDevice(IMonkeyDevice impl) {
        this.impl = impl;
    }

    public IMonkeyDevice getImpl() {
        return impl;
    }

@MonkeyRunnerExported(doc = "Get the HierarchyViewer object for the device.",
returns = "A HierarchyViewer object")
public HierarchyViewer getHierarchyViewer(PyObject[] args, String[] kws) {
        return impl.getHierarchyViewer();
}

@MonkeyRunnerExported(doc =
"Gets the device's screen buffer, yielding a screen capture of the entire display.",
returns = "A MonkeyImage object (a bitmap wrapper)")
    public MonkeyImage takeSnapshot() {
        IMonkeyImage image = impl.takeSnapshot();
        return new MonkeyImage(image);
    }

@MonkeyRunnerExported(doc = "Given the name of a variable on the device, " +
"returns the variable's value",
//Synthetic comment -- @@ -111,7 +103,7 @@
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);

        return impl.getProperty(ap.getString(0));
}

@MonkeyRunnerExported(doc = "Synonym for getProperty()",
//Synthetic comment -- @@ -121,7 +113,8 @@
public String getSystemProperty(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);

        return impl.getSystemProperty(ap.getString(0));
}

@MonkeyRunnerExported(doc = "Sends a touch event at the specified location",
//Synthetic comment -- @@ -150,7 +143,7 @@
// bad stuff was passed in, just use the already specified default value
type = MonkeyDevice.DOWN_AND_UP;
}
        impl.touch(x, y, TOUCH_NAME_TO_ENUM.get(type));
}

@MonkeyRunnerExported(doc = "Simulates dragging (touch, hold, and move) on the device screen.",
//Synthetic comment -- @@ -185,7 +178,7 @@

int steps = ap.getInt(3, 10);

        impl.drag(startx, starty, endx, endy, steps, ms);
}

@MonkeyRunnerExported(doc = "Send a key event to the specified key",
//Synthetic comment -- @@ -213,7 +206,7 @@
// bad stuff was passed in, just use the already specified default value
type = MonkeyDevice.DOWN_AND_UP;
}
        impl.press(name, TOUCH_NAME_TO_ENUM.get(type));
}

@MonkeyRunnerExported(doc = "Types the specified string on the keyboard. This is " +
//Synthetic comment -- @@ -225,7 +218,7 @@
Preconditions.checkNotNull(ap);

String message = ap.getString(0);
        impl.type(message);
}

@MonkeyRunnerExported(doc = "Executes an adb shell command and returns the result, if any.",
//Synthetic comment -- @@ -237,7 +230,7 @@
Preconditions.checkNotNull(ap);

String cmd = ap.getString(0);
        return impl.shell(cmd);
}

@MonkeyRunnerExported(doc = "Reboots the specified device into a specified bootloader.",
//Synthetic comment -- @@ -249,7 +242,7 @@

String into = ap.getString(0, null);

        impl.reboot(into);
}

@MonkeyRunnerExported(doc = "Installs the specified Android package (.apk file) " +
//Synthetic comment -- @@ -262,7 +255,7 @@
Preconditions.checkNotNull(ap);

String path = ap.getString(0);
        return impl.installPackage(path);
}

@MonkeyRunnerExported(doc = "Deletes the specified package from the device, including its " +
//Synthetic comment -- @@ -275,7 +268,7 @@
Preconditions.checkNotNull(ap);

String packageName = ap.getString(0);
        return impl.removePackage(packageName);
}

@MonkeyRunnerExported(doc = "Starts an Activity on the device by sending an Intent " +
//Synthetic comment -- @@ -308,7 +301,7 @@
String component = ap.getString(6, null);
int flags = ap.getInt(7, 0);

        impl.startActivity(uri, action, data, mimetype, categories, extras, component, flags);
}

@MonkeyRunnerExported(doc = "Sends a broadcast intent to the device.",
//Synthetic comment -- @@ -340,7 +333,7 @@
String component = ap.getString(6, null);
int flags = ap.getInt(7, 0);

        impl.broadcastIntent(uri, action, data, mimetype, categories, extras, component, flags);
}

@MonkeyRunnerExported(doc = "Run the specified package with instrumentation and return " +
//Synthetic comment -- @@ -368,7 +361,7 @@
instrumentArgs = Collections.emptyMap();
}

        Map<String, Object> result = impl.instrument(packageName, instrumentArgs);
return JythonUtils.convertMapToDict(result);
}

//Synthetic comment -- @@ -377,34 +370,6 @@
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);

        impl.wake();
}
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyImage.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyImage.java
//Synthetic comment -- index 70201ee..b55b4f3 100644

//Synthetic comment -- @@ -17,6 +17,7 @@

import com.google.common.base.Preconditions;

import com.android.monkeyrunner.core.IMonkeyImage;
import com.android.monkeyrunner.doc.MonkeyRunnerExported;

import org.python.core.ArgParser;
//Synthetic comment -- @@ -25,61 +26,31 @@
import org.python.core.PyObject;
import org.python.core.PyTuple;

import java.awt.image.BufferedImage;
import java.util.logging.Logger;

/**
* Jython object to encapsulate images that have been taken.
*/
@MonkeyRunnerExported(doc = "An image")
public class MonkeyImage extends PyObject implements ClassDictInit {
private static Logger LOG = Logger.getLogger(MonkeyImage.class.getCanonicalName());

public static void classDictInit(PyObject dict) {
JythonUtils.convertDocAnnotationsForClass(MonkeyImage.class, dict);
}

    private IMonkeyImage impl;

    public MonkeyImage(IMonkeyImage impl) {
        this.impl = impl;
}

    public IMonkeyImage getImpl() {
        return impl;
    }


@MonkeyRunnerExported(doc = "Converts the MonkeyImage into a particular format and returns " +
"the result as a String. Use this to get access to the raw" +
"pixels in a particular format. String output is for better " +
//Synthetic comment -- @@ -93,16 +64,7 @@
Preconditions.checkNotNull(ap);

String format = ap.getString(0, "png");
      return impl.convertToBytes(format);
}

@MonkeyRunnerExported(doc = "Write the MonkeyImage to a file.  If no " +
//Synthetic comment -- @@ -120,38 +82,7 @@

String path = ap.getString(0);
String format = ap.getString(1, null);
        return impl.writeToFile(path, format);
}

@MonkeyRunnerExported(doc = "Get a single ARGB (alpha, red, green, blue) pixel at location " +
//Synthetic comment -- @@ -167,7 +98,7 @@

int x = ap.getInt(0);
int y = ap.getInt(1);
        int pixel = impl.getPixel(x, y);
PyInteger a = new PyInteger((pixel & 0xFF000000) >> 24);
PyInteger r = new PyInteger((pixel & 0x00FF0000) >> 16);
PyInteger g = new PyInteger((pixel & 0x0000FF00) >> 8);
//Synthetic comment -- @@ -188,35 +119,7 @@

int x = ap.getInt(0);
int y = ap.getInt(1);
        return impl.getPixel(x, y);
}

@MonkeyRunnerExported(doc = "Compare this MonkeyImage object to aother MonkeyImage object.",
//Synthetic comment -- @@ -231,72 +134,13 @@
Preconditions.checkNotNull(ap);

PyObject otherObject = ap.getPyObject(0);
        // TODO: check if this conversion wortks
        IMonkeyImage other = (IMonkeyImage) otherObject.__tojava__(
                IMonkeyImage.class);

double percent = JythonUtils.getFloat(ap, 1, 1.0);

        return impl.sameAs(other, percent);
}

@MonkeyRunnerExported(doc = "Copy a rectangular region of the image.",
//Synthetic comment -- @@ -315,7 +159,7 @@
int w = rect.__getitem__(2).asInt();
int h = rect.__getitem__(3).asInt();

        IMonkeyImage image = impl.getSubImage(x, y, w, h);
        return new MonkeyImage(image);
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunner.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunner.java
//Synthetic comment -- index 5f137cd..4e3e7fc 100644

//Synthetic comment -- @@ -19,6 +19,9 @@
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;

import com.android.monkeyrunner.core.IMonkeyBackend;
import com.android.monkeyrunner.core.IMonkeyImage;
import com.android.monkeyrunner.core.MonkeyImageBase;
import com.android.monkeyrunner.doc.MonkeyRunnerExported;

import org.python.core.ArgParser;
//Synthetic comment -- @@ -38,7 +41,7 @@
@MonkeyRunnerExported(doc = "Main entry point for MonkeyRunner")
public class MonkeyRunner extends PyObject implements ClassDictInit {
private static final Logger LOG = Logger.getLogger(MonkeyRunner.class.getCanonicalName());
    private static IMonkeyBackend backend;

public static void classDictInit(PyObject dict) {
JythonUtils.convertDocAnnotationsForClass(MonkeyRunner.class, dict);
//Synthetic comment -- @@ -49,7 +52,7 @@
*
* @param backend the backend to use.
*/
    /* package */ static void setBackend(IMonkeyBackend backend) {
MonkeyRunner.backend = backend;
}

//Synthetic comment -- @@ -185,8 +188,8 @@
Preconditions.checkNotNull(ap);

String path = ap.getString(0);
        IMonkeyImage image = MonkeyImageBase.loadImageFromFile(path);
        return new MonkeyImage(image);
}

/**








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerStarter.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerStarter.java
//Synthetic comment -- index 5244dbc..8c9942c 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.google.common.collect.ImmutableMap;

import com.android.monkeyrunner.adb.AdbBackend;
import com.android.monkeyrunner.core.IMonkeyBackend;
import com.android.monkeyrunner.stub.StubBackend;

import org.python.util.PythonInterpreter;
//Synthetic comment -- @@ -50,7 +51,7 @@
private static final Logger LOG = Logger.getLogger(MonkeyRunnerStarter.class.getName());
private static final String MONKEY_RUNNER_MAIN_MANIFEST_NAME = "MonkeyRunnerStartupRunner";

    private final IMonkeyBackend backend;
private final MonkeyRunnerOptions options;

public MonkeyRunnerStarter(MonkeyRunnerOptions options) {
//Synthetic comment -- @@ -68,7 +69,7 @@
* @param backendName the name of the backend to create
* @return the new backend, or null if none were found.
*/
    public static IMonkeyBackend createBackendByName(String backendName) {
if ("adb".equals(backendName)) {
return new AdbBackend();
} else if ("stub".equals(backendName)) {








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/adb/AdbBackend.java b/monkeyrunner/src/com/android/monkeyrunner/adb/AdbBackend.java
//Synthetic comment -- index 455d131..708507f 100644

//Synthetic comment -- @@ -19,8 +19,8 @@

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.monkeyrunner.core.IMonkeyBackend;
import com.android.monkeyrunner.MonkeyDevice;
import com.android.sdklib.SdkConstants;

import java.io.File;
//Synthetic comment -- @@ -32,7 +32,7 @@
/**
* Backend implementation that works over ADB to talk to the device.
*/
public class AdbBackend implements IMonkeyBackend {
private static Logger LOG = Logger.getLogger(AdbBackend.class.getCanonicalName());
// How long to wait each time we check for the device to be connected.
private static final int CONNECTION_ITERATION_TIMEOUT_MS = 200;
//Synthetic comment -- @@ -97,8 +97,9 @@
// Only return the device when it is online
if (device != null && device.getState() == IDevice.DeviceState.ONLINE) {
AdbMonkeyDevice amd = new AdbMonkeyDevice(device);
                MonkeyDevice monkeyDevice = new MonkeyDevice(amd);
devices.add(amd);
                return monkeyDevice;
}

try {








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/adb/AdbMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/adb/AdbMonkeyDevice.java
//Synthetic comment -- index 614e656..68e42ea 100644

//Synthetic comment -- @@ -25,10 +25,10 @@
import com.android.ddmlib.InstallException;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;
import com.android.monkeyrunner.MonkeyManager;
import com.android.monkeyrunner.adb.LinearInterpolator.Point;
import com.android.monkeyrunner.core.IMonkeyImage;
import com.android.monkeyrunner.core.IMonkeyDevice;
import com.android.monkeyrunner.easy.HierarchyViewer;

import java.io.IOException;
//Synthetic comment -- @@ -48,7 +48,7 @@

import javax.annotation.Nullable;

public class AdbMonkeyDevice implements IMonkeyDevice {
private static final Logger LOG = Logger.getLogger(AdbMonkeyDevice.class.getName());

private static final String[] ZERO_LENGTH_STRING_ARRAY = new String[0];
//Synthetic comment -- @@ -67,12 +67,10 @@
Preconditions.checkNotNull(this.manager);
}

public MonkeyManager getManager() {
return manager;
}

public void dispose() {
try {
manager.quit();
//Synthetic comment -- @@ -82,7 +80,6 @@
manager = null;
}

public HierarchyViewer getHierarchyViewer() {
return new HierarchyViewer(device);
}
//Synthetic comment -- @@ -189,8 +186,7 @@
return mm;
}

    public IMonkeyImage takeSnapshot() {
try {
return new AdbMonkeyImage(device.getScreenshot());
} catch (TimeoutException e) {
//Synthetic comment -- @@ -205,12 +201,10 @@
}
}

public String getSystemProperty(String key) {
return device.getProperty(key);
}

public String getProperty(String key) {
try {
return manager.getVariable(key);
//Synthetic comment -- @@ -220,7 +214,6 @@
}
}

public void wake() {
try {
manager.wake();
//Synthetic comment -- @@ -237,7 +230,6 @@
return shell(cmd.toString());
}

public String shell(String cmd) {
CommandOutputCapture capture = new CommandOutputCapture();
try {
//Synthetic comment -- @@ -258,7 +250,6 @@
return capture.toString();
}

public boolean installPackage(String path) {
try {
String result = device.installPackage(path, true);
//Synthetic comment -- @@ -273,7 +264,6 @@
}
}

public boolean removePackage(String packageName) {
try {
String result = device.uninstallPackage(packageName);
//Synthetic comment -- @@ -289,7 +279,6 @@
}
}

public void press(String keyName, TouchPressType type) {
try {
switch (type) {
//Synthetic comment -- @@ -308,7 +297,6 @@
}
}

public void type(String string) {
try {
manager.type(string);
//Synthetic comment -- @@ -317,7 +305,6 @@
}
}

public void touch(int x, int y, TouchPressType type) {
try {
switch (type) {
//Synthetic comment -- @@ -336,7 +323,6 @@
}
}

public void reboot(String into) {
try {
device.reboot(into);
//Synthetic comment -- @@ -349,7 +335,6 @@
}
}

public void startActivity(String uri, String action, String data, String mimetype,
Collection<String> categories, Map<String, Object> extras, String component,
int flags) {
//Synthetic comment -- @@ -359,7 +344,6 @@
intentArgs.toArray(ZERO_LENGTH_STRING_ARRAY)).toArray(ZERO_LENGTH_STRING_ARRAY));
}

public void broadcastIntent(String uri, String action, String data, String mimetype,
Collection<String> categories, Map<String, Object> extras, String component,
int flags) {
//Synthetic comment -- @@ -449,7 +433,6 @@
return parts;
}

public Map<String, Object> instrument(String packageName, Map<String, Object> args) {
List<String> shellCmd = Lists.newArrayList("am", "instrument", "-w", "-r", packageName);
String result = shell(shellCmd.toArray(ZERO_LENGTH_STRING_ARRAY));
//Synthetic comment -- @@ -497,7 +480,6 @@
return map;
}

public void drag(int startx, int starty, int endx, int endy, int steps, long ms) {
final long iterationTime = ms / steps;









//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/adb/AdbMonkeyImage.java b/monkeyrunner/src/com/android/monkeyrunner/adb/AdbMonkeyImage.java
//Synthetic comment -- index fc32600..e2bd86e 100644

//Synthetic comment -- @@ -16,15 +16,15 @@
package com.android.monkeyrunner.adb;

import com.android.ddmlib.RawImage;
import com.android.monkeyrunner.adb.image.ImageUtils;
import com.android.monkeyrunner.core.MonkeyImageBase;

import java.awt.image.BufferedImage;

/**
* ADB implementation of the MonkeyImage class.
*/
public class AdbMonkeyImage extends MonkeyImageBase {
private final RawImage image;

/**








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/adb/image/CaptureRawAndConvertedImage.java b/monkeyrunner/src/com/android/monkeyrunner/adb/image/CaptureRawAndConvertedImage.java
//Synthetic comment -- index 7e31ea5..8766316 100644

//Synthetic comment -- @@ -16,9 +16,10 @@
package com.android.monkeyrunner.adb.image;

import com.android.ddmlib.RawImage;
import com.android.monkeyrunner.adb.AdbBackend;
import com.android.monkeyrunner.adb.AdbMonkeyImage;
import com.android.monkeyrunner.core.IMonkeyImage;
import com.android.monkeyrunner.core.IMonkeyDevice;

import java.io.FileOutputStream;
import java.io.IOException;
//Synthetic comment -- @@ -95,12 +96,12 @@

public static void main(String[] args) throws IOException {
AdbBackend backend = new AdbBackend();
        IMonkeyDevice device = backend.waitForConnection().getImpl();
        IMonkeyImage snapshot = (IMonkeyImage) device.takeSnapshot();

// write out to a file
snapshot.writeToFile("output.png", "png");
        writeOutImage(((AdbMonkeyImage)snapshot).getRawImage(), "output.raw");
System.exit(0);
}
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/controller/MonkeyController.java b/monkeyrunner/src/com/android/monkeyrunner/controller/MonkeyController.java
//Synthetic comment -- index e199a75..cdd61bd 100644

//Synthetic comment -- @@ -15,8 +15,8 @@
*/
package com.android.monkeyrunner.controller;

import com.android.monkeyrunner.adb.AdbBackend;
import com.android.monkeyrunner.core.IMonkeyDevice;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//Synthetic comment -- @@ -38,7 +38,7 @@
SwingUtilities.invokeLater(new Runnable() {
public void run() {
AdbBackend adb = new AdbBackend();
                final IMonkeyDevice device = adb.waitForConnection().getImpl();
MonkeyControllerFrame mf = new MonkeyControllerFrame(device);
mf.setVisible(true);
mf.addWindowListener(new WindowAdapter() {








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/controller/MonkeyControllerFrame.java b/monkeyrunner/src/com/android/monkeyrunner/controller/MonkeyControllerFrame.java
//Synthetic comment -- index 7f5a7d8..7750936 100644

//Synthetic comment -- @@ -15,10 +15,10 @@
*/
package com.android.monkeyrunner.controller;

import com.android.monkeyrunner.MonkeyManager;
import com.android.monkeyrunner.PhysicalButton;
import com.android.monkeyrunner.core.IMonkeyImage;
import com.android.monkeyrunner.core.IMonkeyDevice;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
//Synthetic comment -- @@ -61,7 +61,7 @@
}
});

    private final IMonkeyDevice device;

private class PressAction extends AbstractAction {
private final PhysicalButton button;
//Synthetic comment -- @@ -85,7 +85,7 @@
return button;
}

    public MonkeyControllerFrame(IMonkeyDevice device) {
super("MonkeyController");
this.device = device;

//Synthetic comment -- @@ -155,7 +155,7 @@
}

private void updateScreen() {
        IMonkeyImage snapshot = device.takeSnapshot();
currentImage = snapshot.createBufferedImage();
imageLabel.setIcon(new ImageIcon(currentImage));









//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerBackend.java b/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyBackend.java
similarity index 90%
rename from monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerBackend.java
rename to monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyBackend.java
//Synthetic comment -- index 216d214..1f18f4b 100644

//Synthetic comment -- @@ -13,13 +13,15 @@
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.android.monkeyrunner.core;

import com.android.monkeyrunner.MonkeyDevice;

/**
* Interface between MonkeyRunner common code and the MonkeyRunner backend.  The backend is
* responsible for communicating between the host and the device.
*/
public interface IMonkeyBackend {
/**
* Wait for a device to connect to the backend.
*








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyDevice.java
new file mode 100644
//Synthetic comment -- index 0000000..6850953

//Synthetic comment -- @@ -0,0 +1,73 @@
// Copyright 2011 Google Inc. All Rights Reserved.
package com.android.monkeyrunner.core;

import com.android.monkeyrunner.MonkeyManager;
import com.android.monkeyrunner.easy.HierarchyViewer;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * @author adrianz@google.com (Adrian Zakrzewski)
 */
public interface IMonkeyDevice {
    enum TouchPressType {
        DOWN, UP, DOWN_AND_UP,
    }


    /**
     * Create a MonkeyMananger for talking to this device.
     *
     * NOTE: This is not part of the jython API.
     *
     * @return the MonkeyManager
     */
    MonkeyManager getManager();

    /**
     * Dispose of any native resoureces this device may have taken hold of.
     *
     *  NOTE: This is not part of the jython API.
     */
    void dispose();

    /**
     * @return hierarchy viewer implementation for querying state of the view
     * hierarchy.
     */
    HierarchyViewer getHierarchyViewer();

    IMonkeyImage takeSnapshot();


    /**
     * Reboot the device.
     *
     * @param into which bootloader to boot into.  Null means default reboot.
     */
    void reboot(@Nullable String into);

    String getProperty(String key);
    String getSystemProperty(String key);
    void touch(int x, int y, TouchPressType type);
    void press(String keyName, TouchPressType type);
    void drag(int startx, int starty, int endx, int endy, int steps, long ms);
    void type(String string);
    String shell(String cmd);
    boolean installPackage(String path);
    boolean removePackage(String packageName);
    void startActivity(@Nullable String uri, @Nullable String action,
            @Nullable String data, @Nullable String mimetype,
            Collection<String> categories, Map<String, Object> extras, @Nullable String component,
            int flags);
    void broadcastIntent(@Nullable String uri, @Nullable String action,
            @Nullable String data, @Nullable String mimetype,
            Collection<String> categories, Map<String, Object> extras, @Nullable String component,
            int flags);
    Map<String, Object> instrument(String packageName,
            Map<String, Object> args);
    void wake();
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyImage.java b/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyImage.java
new file mode 100644
//Synthetic comment -- index 0000000..312ae51

//Synthetic comment -- @@ -0,0 +1,30 @@
// Copyright 2011 Google Inc. All Rights Reserved.
package com.android.monkeyrunner.core;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

/**
 * @author adrianz@google.com (Adrian Zakrzewski)
 */
public interface IMonkeyImage {
    BufferedImage createBufferedImage();
    BufferedImage getBufferedImage();

    IMonkeyImage getSubImage(int x, int y, int w, int h);

    byte[] convertToBytes(String format);
    boolean writeToFile(String path, String format);
    int getPixel(int x, int y);
    boolean sameAs(IMonkeyImage other, double percent);

}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/core/MonkeyImageBase.java b/monkeyrunner/src/com/android/monkeyrunner/core/MonkeyImageBase.java
new file mode 100644
//Synthetic comment -- index 0000000..c8b348f

//Synthetic comment -- @@ -0,0 +1,198 @@
// Copyright 2011 Google Inc. All Rights Reserved.
package com.android.monkeyrunner.core;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

/**
 * Interface defining a snapshot image.
 */
public abstract class MonkeyImageBase implements IMonkeyImage {
    private static Logger LOG = Logger.getLogger(MonkeyImageBase.class.getCanonicalName());
    
    /**
     * Convert the MonkeyImage to a BufferedImage.
     *
     * @return a BufferedImage for this MonkeyImage.
     */
    public abstract BufferedImage createBufferedImage();

    // Cache the BufferedImage so we don't have to generate it every time.
    private WeakReference<BufferedImage> cachedBufferedImage = null;

    /**
     * Utility method to handle getting the BufferedImage and managing the cache.
     *
     * @return the BufferedImage for this image.
     */
    public BufferedImage getBufferedImage() {
        // Check the cache first
        if (cachedBufferedImage != null) {
            BufferedImage img = cachedBufferedImage.get();
            if (img != null) {
                return img;
            }
        }

        // Not in the cache, so create it and cache it.
        BufferedImage img = createBufferedImage();
        cachedBufferedImage = new WeakReference<BufferedImage>(img);
        return img;
    }

    public byte[] convertToBytes(String format) {
      BufferedImage argb = convertSnapshot();

      ByteArrayOutputStream os = new ByteArrayOutputStream();
      try {
          ImageIO.write(argb, format, os);
      } catch (IOException e) {
          return new byte[0];
      }
      return os.toByteArray();
    }

    public boolean writeToFile(String path, String format) {
        if (format != null) {
            return writeToFileHelper(path, format);
        }
        int offset = path.lastIndexOf('.');
        if (offset < 0) {
            return writeToFileHelper(path, "png");
        }
        String ext = path.substring(offset + 1);
        Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix(ext);
        if (!writers.hasNext()) {
            return writeToFileHelper(path, "png");
        }
        ImageWriter writer = writers.next();
        BufferedImage image = convertSnapshot();
        try {
            File f = new File(path);
            f.delete();

            ImageOutputStream outputStream = ImageIO.createImageOutputStream(f);
            writer.setOutput(outputStream);

            try {
                writer.write(image);
            } finally {
                writer.dispose();
                outputStream.flush();
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public int getPixel(int x, int y) {
        BufferedImage image = getBufferedImage();
        return image.getRGB(x, y);
    }

    private BufferedImage convertSnapshot() {
        BufferedImage image = getBufferedImage();

        // Convert the image to ARGB so ImageIO writes it out nicely
        BufferedImage argb = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = argb.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return argb;
    }

    private boolean writeToFileHelper(String path, String format) {
        BufferedImage argb = convertSnapshot();

        try {
            ImageIO.write(argb, format, new File(path));
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean sameAs(IMonkeyImage other, double percent) {
        BufferedImage otherImage = other.getBufferedImage();
        BufferedImage myImage = getBufferedImage();

        // Easy size check
        if (otherImage.getWidth() != myImage.getWidth()) {
            return false;
        }
        if (otherImage.getHeight() != myImage.getHeight()) {
            return false;
        }

        int[] otherPixel = new int[1];
        int[] myPixel = new int[1];

        int width = myImage.getWidth();
        int height = myImage.getHeight();

        int numDiffPixels = 0;
        // Now, go through pixel-by-pixel and check that the images are the same;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (myImage.getRGB(x, y) != otherImage.getRGB(x, y)) {
                    numDiffPixels++;
                }
            }
        }
        double numberPixels = (height * width);
        double diffPercent = numDiffPixels / numberPixels;
        return percent <= 1.0 - diffPercent;
    }

    // TODO: figure out the location of this class and is superclasses
    private static class BufferedImageMonkeyImage extends MonkeyImageBase {
        private final BufferedImage image;

        public BufferedImageMonkeyImage(BufferedImage image) {
            this.image = image;
        }

        @Override
        public BufferedImage createBufferedImage() {
            return image;
        }
    }

    /* package */ public static IMonkeyImage loadImageFromFile(String path) {
        File f = new File(path);
        if (f.exists() && f.canRead()) {
            try {
                BufferedImage bufferedImage = ImageIO.read(new File(path));
                if (bufferedImage == null) {
                    LOG.log(Level.WARNING, "Cannot decode file %s", path);
                    return null;
                }
                return new BufferedImageMonkeyImage(bufferedImage);
            } catch (IOException e) {
                LOG.log(Level.WARNING, "Exception trying to decode image", e);
                return null;
            }
        } else {
            LOG.log(Level.WARNING, "Cannot read file %s", path);
            return null;
        }
    }

    public IMonkeyImage getSubImage(int x, int y, int w, int h) {
        BufferedImage image = getBufferedImage();
        return new BufferedImageMonkeyImage(image.getSubimage(x, y, w, h));
    }
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/easy/EasyMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/easy/EasyMonkeyDevice.java
//Synthetic comment -- index 727bb83..3915b4d 100644

//Synthetic comment -- @@ -22,7 +22,6 @@
import com.android.hierarchyviewerlib.device.ViewNode.Property;
import com.android.monkeyrunner.JythonUtils;
import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.doc.MonkeyRunnerExported;

import org.eclipse.swt.graphics.Point;
//Synthetic comment -- @@ -32,6 +31,7 @@
import org.python.core.PyException;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PyTuple;

import java.util.Set;
//Synthetic comment -- @@ -60,7 +60,7 @@
argDocs = { "MonkeyDevice to extend." })
public EasyMonkeyDevice(MonkeyDevice device) {
this.mDevice = device;
        this.mHierarchyViewer = device.getImpl().getHierarchyViewer();
}

@MonkeyRunnerExported(doc = "Sends a touch event to the selected object.",
//Synthetic comment -- @@ -85,7 +85,7 @@
otherArgs[1] = new PyInteger(p.y);
otherArgs[2] = args[1];

        mDevice.touch(otherArgs, new String[]{});
}

@MonkeyRunnerExported(doc = "Types a string into the specified object.",
//Synthetic comment -- @@ -107,8 +107,16 @@
}

Point p = HierarchyViewer.getAbsoluteCenterOfView(node);

        PyObject[] otherArgs = new PyObject[3];
        otherArgs[0] = new PyInteger(p.x);
        otherArgs[1] = new PyInteger(p.y);
        otherArgs[2] = args[1];

        mDevice.touch(otherArgs, new String[]{});
        otherArgs = new PyObject[1];
        otherArgs[0] = new PyString(text);
        mDevice.type(otherArgs, new String[]{});
}

@MonkeyRunnerExported(doc = "Locates the coordinates of the selected object.",
//Synthetic comment -- @@ -154,13 +162,7 @@
Preconditions.checkNotNull(ap);

By selector = getSelector(ap, 0);
        return mHierarchyViewer.visible(selector);
}

@MonkeyRunnerExported(doc = "Obtain the text in the selected input box.",
//Synthetic comment -- @@ -172,16 +174,7 @@
Preconditions.checkNotNull(ap);

By selector = getSelector(ap, 0);
        return mHierarchyViewer.getText(selector);
}

@MonkeyRunnerExported(doc = "Gets the id of the focused window.",








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/easy/HierarchyViewer.java b/monkeyrunner/src/com/android/monkeyrunner/easy/HierarchyViewer.java
//Synthetic comment -- index 5d6911e..450571c 100644

//Synthetic comment -- @@ -121,4 +121,38 @@
return new Point(
point.x + (node.width / 2), point.y + (node.height / 2));
}

    /**
     * Gets the visibility of a given element.
     *
     * @param selector selector for the view.
     * @return True if the element is visible.
     */
    public boolean visible(By selector) {
        ViewNode node = findView(selector);
        boolean ret = (node != null)
                && node.namedProperties.containsKey("getVisibility()")
                && "VISIBLE".equalsIgnoreCase(
                        node.namedProperties.get("getVisibility()").value);
        return ret;

    }

    /**
     * Gets the text of a given element.
     *
     * @param selector selector for the view.
     * @return the text of the given element.
     */
    public String getText(By selector) {
        ViewNode node = findView(selector);
        if (node == null) {
            throw new RuntimeException("Node not found");
        }
        ViewNode.Property textProperty = node.namedProperties.get("text:mText");
        if (textProperty == null) {
            throw new RuntimeException("No text property on node");
        }
        return textProperty.value;
    }
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/MonkeyRecorder.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/MonkeyRecorder.java
//Synthetic comment -- index c1a8f7f..1268c75 100644

//Synthetic comment -- @@ -15,8 +15,8 @@
*/
package com.android.monkeyrunner.recorder;

import com.android.monkeyrunner.adb.AdbBackend;
import com.android.monkeyrunner.core.IMonkeyDevice;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//Synthetic comment -- @@ -44,7 +44,7 @@
*
* @param device
*/
    public static void start(final IMonkeyDevice device) {
MonkeyRecorderFrame frame = new MonkeyRecorderFrame(device);
// TODO: this is a hack until the window listener works.
frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//Synthetic comment -- @@ -70,6 +70,6 @@

public static void main(String[] args) {
AdbBackend adb = new AdbBackend();
        MonkeyRecorder.start(adb.waitForConnection().getImpl());
}
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/MonkeyRecorderFrame.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/MonkeyRecorderFrame.java
//Synthetic comment -- index b6c1f78..41a5e66 100644

//Synthetic comment -- @@ -16,7 +16,8 @@
package com.android.monkeyrunner.recorder;

import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.core.IMonkeyImage;
import com.android.monkeyrunner.core.IMonkeyDevice;
import com.android.monkeyrunner.recorder.actions.Action;
import com.android.monkeyrunner.recorder.actions.DragAction;
import com.android.monkeyrunner.recorder.actions.DragAction.Direction;
//Synthetic comment -- @@ -60,7 +61,7 @@
private static final Logger LOG =
Logger.getLogger(MonkeyRecorderFrame.class.getName());

    private final IMonkeyDevice device;

private static final long serialVersionUID = 1L;
private JPanel jContentPane = null;
//Synthetic comment -- @@ -91,7 +92,7 @@
/**
* This is the default constructor
*/
    public MonkeyRecorderFrame(IMonkeyDevice device) {
this.device = device;
initialize();
}
//Synthetic comment -- @@ -102,7 +103,6 @@
this.setTitle("MonkeyRecorder");

SwingUtilities.invokeLater(new Runnable() {
public void run() {
refreshDisplay();
}});
//Synthetic comment -- @@ -110,7 +110,7 @@
}

private void refreshDisplay() {
        IMonkeyImage snapshot = device.takeSnapshot();
currentImage = snapshot.createBufferedImage();

Graphics2D g = scaledImage.createGraphics();








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/Action.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/Action.java
//Synthetic comment -- index d582aa4..6fa91ab 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.monkeyrunner.recorder.actions;

import com.android.monkeyrunner.core.IMonkeyDevice;

/**
* All actions that can be recorded must implement this interface.
//Synthetic comment -- @@ -41,5 +41,5 @@
*
* @param device the device to execute the action on.
*/
    void execute(IMonkeyDevice device) throws Exception;
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/DragAction.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/DragAction.java
//Synthetic comment -- index 082bfe4..6afcd43 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.monkeyrunner.recorder.actions;

import com.android.monkeyrunner.core.IMonkeyDevice;

/**
* Action to drag the "finger" across the device.
//Synthetic comment -- @@ -58,12 +58,10 @@
timeMs = millis;
}

public String getDisplayName() {
return String.format("Fling %s", dir.name().toLowerCase());
}

public String serialize() {
float duration = timeMs / 1000.0f;

//Synthetic comment -- @@ -76,8 +74,7 @@
return "DRAG|" + pydict;
}

    public void execute(IMonkeyDevice device) {
device.drag(startx, starty, endx, endy, steps, timeMs);
}
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/PressAction.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/PressAction.java
//Synthetic comment -- index a0d9e0e..190e20c 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.google.common.collect.ImmutableBiMap;

import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.core.IMonkeyDevice;

/**
* Action to press a certain button.
//Synthetic comment -- @@ -45,13 +46,11 @@
this(key, MonkeyDevice.DOWN_AND_UP);
}

public String getDisplayName() {
return String.format("%s button %s",
DOWNUP_FLAG_MAP.get(downUpFlag), key);
}

public String serialize() {
String pydict = PyDictUtilBuilder.newBuilder().
add("name", key).
//Synthetic comment -- @@ -59,8 +58,7 @@
return "PRESS|" + pydict;
}

    public void execute(IMonkeyDevice device) {
device.press(key,
MonkeyDevice.TOUCH_NAME_TO_ENUM.get(downUpFlag));
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/TouchAction.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/TouchAction.java
//Synthetic comment -- index 4633edb..b0dd3e4 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.google.common.collect.ImmutableBiMap;

import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.core.IMonkeyDevice;

/**
* Action to touch the touchscreen at a certain location.
//Synthetic comment -- @@ -39,19 +40,16 @@
this.direction = direction;
}

public String getDisplayName() {
return String.format("%s touchscreen at (%d, %d)",
DOWNUP_FLAG_MAP.get(direction), x, y);
}

    public void execute(IMonkeyDevice device) throws Exception {
device.touch(x, y,
MonkeyDevice.TOUCH_NAME_TO_ENUM.get(direction));
}

public String serialize() {
String pydict = PyDictUtilBuilder.newBuilder().
add("x", x).








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/TypeAction.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/TypeAction.java
//Synthetic comment -- index 1bfb9e9..45e1470 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.monkeyrunner.recorder.actions;

import com.android.monkeyrunner.core.IMonkeyDevice;

/**
* Action to type in a string on the device.
//Synthetic comment -- @@ -27,19 +27,16 @@
this.whatToType = whatToType;
}

public String getDisplayName() {
return String.format("Type \"%s\"", whatToType);
}

public String serialize() {
String pydict = PyDictUtilBuilder.newBuilder().add("message", whatToType).build();
return "TYPE|" + pydict;
}

    public void execute(IMonkeyDevice device) {
device.type(whatToType);
}
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/WaitAction.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/WaitAction.java
//Synthetic comment -- index 9115f9a..bd2d421 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.monkeyrunner.recorder.actions;

import com.android.monkeyrunner.core.IMonkeyDevice;

/**
* Action that specifies to wait for a certain amount of time.
//Synthetic comment -- @@ -27,19 +27,16 @@
this.howLongSeconds = howLongSeconds;
}

public String getDisplayName() {
return String.format("Wait for %g seconds", this.howLongSeconds);
}

public String serialize() {
String pydict = PyDictUtilBuilder.newBuilder().add("seconds", howLongSeconds).build();
return "WAIT|" + pydict;
}

    public void execute(IMonkeyDevice device) throws Exception {
long ms = (long) (1000.0f * howLongSeconds);
Thread.sleep(ms);
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/stub/StubBackend.java b/monkeyrunner/src/com/android/monkeyrunner/stub/StubBackend.java
//Synthetic comment -- index c2fa5f7..4a65c98 100644

//Synthetic comment -- @@ -17,9 +17,9 @@

import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.MonkeyManager;
import com.android.monkeyrunner.core.IMonkeyBackend;

public class StubBackend implements IMonkeyBackend {

public MonkeyManager createManager(String address, int port) {
// TODO Auto-generated method stub







