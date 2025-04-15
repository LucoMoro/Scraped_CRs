/*Separate MonkeyRunner core logic

    Refactored the MonkeyRunner code to separate core logic
    from the jython wrapper. The core logic is now usable
    directly from Java w/o the pollution from jython.
    The existing MonkeyRunner classes are now just a thin
    and dumb wrapper atop the core.

Change-Id:I6ef18ea92e0e9284c1fde949b4efb0e2e7170e57*/




//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java
//Synthetic comment -- index 37b1cda..649e33c 100644

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

    // TODO: This may not be accessible from jython; if so, remove it.
public enum TouchPressType {
DOWN, UP, DOWN_AND_UP,
}

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
//Synthetic comment -- index 5f137cd..5529802 100644

//Synthetic comment -- @@ -19,6 +19,10 @@
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;

import com.android.monkeyrunner.core.IMonkeyBackend;
import com.android.monkeyrunner.core.IMonkeyDevice;
import com.android.monkeyrunner.core.IMonkeyImage;
import com.android.monkeyrunner.core.MonkeyImageBase;
import com.android.monkeyrunner.doc.MonkeyRunnerExported;

import org.python.core.ArgParser;
//Synthetic comment -- @@ -38,7 +42,7 @@
@MonkeyRunnerExported(doc = "Main entry point for MonkeyRunner")
public class MonkeyRunner extends PyObject implements ClassDictInit {
private static final Logger LOG = Logger.getLogger(MonkeyRunner.class.getCanonicalName());
    private static IMonkeyBackend backend;

public static void classDictInit(PyObject dict) {
JythonUtils.convertDocAnnotationsForClass(MonkeyRunner.class, dict);
//Synthetic comment -- @@ -49,7 +53,7 @@
*
* @param backend the backend to use.
*/
    /* package */ static void setBackend(IMonkeyBackend backend) {
MonkeyRunner.backend = backend;
}

//Synthetic comment -- @@ -71,8 +75,10 @@
timeoutMs = Long.MAX_VALUE;
}

        IMonkeyDevice device = backend.waitForConnection(timeoutMs,
ap.getString(1, ".*"));
        MonkeyDevice monkeyDevice = new MonkeyDevice(device);
        return monkeyDevice;
}

@MonkeyRunnerExported(doc = "Pause the currently running program for the specified " +
//Synthetic comment -- @@ -185,8 +191,8 @@
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
//Synthetic comment -- index 455d131..49cac08 100644

//Synthetic comment -- @@ -19,8 +19,8 @@

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.monkeyrunner.core.IMonkeyBackend;
import com.android.monkeyrunner.core.IMonkeyDevice;
import com.android.sdklib.SdkConstants;

import java.io.File;
//Synthetic comment -- @@ -32,12 +32,11 @@
/**
* Backend implementation that works over ADB to talk to the device.
*/
public class AdbBackend implements IMonkeyBackend {
private static Logger LOG = Logger.getLogger(AdbBackend.class.getCanonicalName());
// How long to wait each time we check for the device to be connected.
private static final int CONNECTION_ITERATION_TIMEOUT_MS = 200;
    private final List<IMonkeyDevice> devices = Lists.newArrayList();
private final AndroidDebugBridge bridge;

public AdbBackend() {
//Synthetic comment -- @@ -87,18 +86,20 @@
return null;
}

    @Override
    public IMonkeyDevice waitForConnection() {
return waitForConnection(Integer.MAX_VALUE, ".*");
}

    @Override
    public IMonkeyDevice waitForConnection(long timeoutMs, String deviceIdRegex) {
do {
IDevice device = findAttacedDevice(deviceIdRegex);
// Only return the device when it is online
if (device != null && device.getState() == IDevice.DeviceState.ONLINE) {
                IMonkeyDevice monkeyDevice = new AdbMonkeyDevice(device);
                devices.add(monkeyDevice);
                return monkeyDevice;
}

try {
//Synthetic comment -- @@ -113,8 +114,9 @@
return null;
}

    @Override
public void shutdown() {
        for (IMonkeyDevice device : devices) {
device.dispose();
}
AndroidDebugBridge.terminate();








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/adb/AdbMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/adb/AdbMonkeyDevice.java
//Synthetic comment -- index 614e656..60eaba9 100644

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
//Synthetic comment -- @@ -90,6 +90,7 @@
private void executeAsyncCommand(final String command,
final LoggingOutputReceiver logger) {
executor.submit(new Runnable() {
            @Override
public void run() {
try {
device.executeShellCommand(command, logger);
//Synthetic comment -- @@ -190,7 +191,7 @@
}

@Override
    public IMonkeyImage takeSnapshot() {
try {
return new AdbMonkeyImage(device.getScreenshot());
} catch (TimeoutException e) {
//Synthetic comment -- @@ -505,6 +506,7 @@
LinearInterpolator.Point start = new LinearInterpolator.Point(startx, starty);
LinearInterpolator.Point end = new LinearInterpolator.Point(endx, endy);
lerp.interpolate(start, end, new LinearInterpolator.Callback() {
            @Override
public void step(Point point) {
try {
manager.touchMove(point.getX(), point.getY());
//Synthetic comment -- @@ -519,6 +521,7 @@
}
}

            @Override
public void start(Point point) {
try {
manager.touchDown(point.getX(), point.getY());
//Synthetic comment -- @@ -534,6 +537,7 @@
}
}

            @Override
public void end(Point point) {
try {
manager.touchMove(point.getX(), point.getY());








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
//Synthetic comment -- index 7e31ea5..5a317f1 100644

//Synthetic comment -- @@ -16,9 +16,11 @@
package com.android.monkeyrunner.adb.image;

import com.android.ddmlib.RawImage;
import com.android.monkeyrunner.adb.AdbBackend;
import com.android.monkeyrunner.adb.AdbMonkeyImage;
import com.android.monkeyrunner.core.IMonkeyBackend;
import com.android.monkeyrunner.core.IMonkeyImage;
import com.android.monkeyrunner.core.IMonkeyDevice;

import java.io.FileOutputStream;
import java.io.IOException;
//Synthetic comment -- @@ -94,13 +96,13 @@
}

public static void main(String[] args) throws IOException {
        IMonkeyBackend backend = new AdbBackend();
        IMonkeyDevice device = backend.waitForConnection();
        IMonkeyImage snapshot = (IMonkeyImage) device.takeSnapshot();

// write out to a file
snapshot.writeToFile("output.png", "png");
        writeOutImage(((AdbMonkeyImage)snapshot).getRawImage(), "output.raw");
System.exit(0);
}
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/controller/MonkeyController.java b/monkeyrunner/src/com/android/monkeyrunner/controller/MonkeyController.java
//Synthetic comment -- index e199a75..ca3195c 100644

//Synthetic comment -- @@ -15,8 +15,9 @@
*/
package com.android.monkeyrunner.controller;

import com.android.monkeyrunner.adb.AdbBackend;
import com.android.monkeyrunner.core.IMonkeyBackend;
import com.android.monkeyrunner.core.IMonkeyDevice;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//Synthetic comment -- @@ -36,9 +37,10 @@

public static void main(String[] args) {
SwingUtilities.invokeLater(new Runnable() {
            @Override
public void run() {
                IMonkeyBackend adb = new AdbBackend();
                final IMonkeyDevice device = adb.waitForConnection();
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
similarity index 75%
rename from monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerBackend.java
rename to monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyBackend.java
//Synthetic comment -- index 216d214..3c1b943 100644

//Synthetic comment -- @@ -13,13 +13,22 @@
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
     * Wait for a default device to connect to the backend.
     *
     * @return the connected device (or null if timeout);
     */
    IMonkeyDevice waitForConnection();

/**
* Wait for a device to connect to the backend.
*
//Synthetic comment -- @@ -27,7 +36,7 @@
* @param deviceIdRegex the regular expression to specify which device to wait for.
* @return the connected device (or null if timeout);
*/
    IMonkeyDevice waitForConnection(long timeoutMs, String deviceIdRegex);

/**
* Shutdown the backend and cleanup any resources it was using.








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyDevice.java
new file mode 100644
//Synthetic comment -- index 0000000..c081a56

//Synthetic comment -- @@ -0,0 +1,200 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.monkeyrunner.core;

import com.android.monkeyrunner.MonkeyManager;
import com.android.monkeyrunner.easy.HierarchyViewer;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * MonkeyDevice interface.
 */
public interface IMonkeyDevice {
    enum TouchPressType {
        DOWN, UP, DOWN_AND_UP,
    }

    /**
     * Create a MonkeyMananger for talking to this device.
     *
     * @return the MonkeyManager
     */
    MonkeyManager getManager();

    /**
     * Dispose of any native resources this device may have taken hold of.
     */
    void dispose();

    /**
     * @return hierarchy viewer implementation for querying state of the view
     * hierarchy.
     */
    HierarchyViewer getHierarchyViewer();

    /**
     * Take the current screen's snapshot.
     * @return the snapshot image
     */
    IMonkeyImage takeSnapshot();

    /**
     * Reboot the device.
     *
     * @param into which bootloader to boot into.  Null means default reboot.
     */
    void reboot(@Nullable String into);

    /**
     * Get device's property.
     *
     * @param key the property name
     * @return the property value
     */
    String getProperty(String key);

    /**
     * Get system property.
     *
     * @param key the name of the system property
     * @return  the property value
     */
    String getSystemProperty(String key);

    /**
     * Perform a touch of the given type at (x,y).
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param type the touch type
     */
    void touch(int x, int y, TouchPressType type);

    /**
     * Perform a press of a given type using a given key.
     *
     * TODO: define standard key names in a separate class or enum
     *
     * @param keyName the name of the key to use
     * @param type the type of press to perform
     */
    void press(String keyName, TouchPressType type);

    /**
     * Perform a drag from one one location to another
     *
     * @param startx the x coordinate of the drag's starting point
     * @param starty the y coordinate of the drag's starting point
     * @param endx the x coordinate of the drag's end point
     * @param endy the y coordinate of the drag's end point
     * @param steps the number of steps to take when interpolating points
     * @param ms the duration of the drag
     */
    void drag(int startx, int starty, int endx, int endy, int steps, long ms);

    /**
     * Type a given string.
     *
     * @param string the string to type
     */
    void type(String string);

    /**
     * Execute a shell command.
     *
     * @param cmd the command to execute
     * @return the output of the command
     */
    String shell(String cmd);

    /**
     * Install a given package.
     *
     * @param path the path to the installation package
     * @return true if success
     */
    boolean installPackage(String path);

    /**
     * Uninstall a given package.
     *
     * @param packageName the name of the package
     * @return true if success
     */
    boolean removePackage(String packageName);

    /**
     * Start an activity.
     *
     * @param uri the URI for the Intent
     * @param action the action for the Intent
     * @param data the data URI for the Intent
     * @param mimeType the mime type for the Intent
     * @param categories the category names for the Intent
     * @param extras the extras to add to the Intent
     * @param component the component of the Intent
     * @param flags the flags for the Intent
     */
    void startActivity(@Nullable String uri, @Nullable String action,
            @Nullable String data, @Nullable String mimeType,
            Collection<String> categories, Map<String, Object> extras, @Nullable String component,
            int flags);

    /**
     * Send a broadcast intent to the device.
     *
     * @param uri the URI for the Intent
     * @param action the action for the Intent
     * @param data the data URI for the Intent
     * @param mimeType the mime type for the Intent
     * @param categories the category names for the Intent
     * @param extras the extras to add to the Intent
     * @param component the component of the Intent
     * @param flags the flags for the Intent
     */
    void broadcastIntent(@Nullable String uri, @Nullable String action,
            @Nullable String data, @Nullable String mimeType,
            Collection<String> categories, Map<String, Object> extras, @Nullable String component,
            int flags);

    /**
     * Run the specified package with instrumentation and return the output it
     * generates.
     *
     * Use this to run a test package using InstrumentationTestRunner.
     *
     * @param packageName The class to run with instrumentation. The format is
     * packageName/className. Use packageName to specify the Android package to
     * run, and className to specify the class to run within that package. For
     * test packages, this is usually testPackageName/InstrumentationTestRunner
     * @param args a map of strings to objects containing the arguments to pass
     * to this instrumentation.
     * @return A map of strings to objects for the output from the package.
     * For a test package, contains a single key-value pair: the key is 'stream'
     * and the value is a string containing the test output.
     */
    Map<String, Object> instrument(String packageName,
            Map<String, Object> args);

    /**
     * Wake up the screen on the device.
     */
    void wake();
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyImage.java b/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyImage.java
new file mode 100644
//Synthetic comment -- index 0000000..5a24fa7

//Synthetic comment -- @@ -0,0 +1,36 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.monkeyrunner.core;

import java.awt.image.BufferedImage;

/**
 * MonkeyImage interface.
 *
 * This interface defines an image representing a screen snapshot.
 */
public interface IMonkeyImage {
    // TODO: add java docs
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
//Synthetic comment -- index 0000000..04ccb93

//Synthetic comment -- @@ -0,0 +1,219 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.monkeyrunner.core;

import java.awt.Graphics;
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
 * Base class with basic functionality for MonkeyImage implementations.
 */
public abstract class MonkeyImageBase implements IMonkeyImage {
    private static Logger LOG = Logger.getLogger(MonkeyImageBase.class.getCanonicalName());

    /**
     * Convert the MonkeyImage to a BufferedImage.
     *
     * @return a BufferedImage for this MonkeyImage.
     */
    @Override
    public abstract BufferedImage createBufferedImage();

    // Cache the BufferedImage so we don't have to generate it every time.
    private WeakReference<BufferedImage> cachedBufferedImage = null;

    /**
     * Utility method to handle getting the BufferedImage and managing the cache.
     *
     * @return the BufferedImage for this image.
     */
    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    public static IMonkeyImage loadImageFromFile(String path) {
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

    @Override
    public IMonkeyImage getSubImage(int x, int y, int w, int h) {
        BufferedImage image = getBufferedImage();
        return new BufferedImageMonkeyImage(image.getSubimage(x, y, w, h));
    }
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/easy/By.java b/monkeyrunner/src/com/android/monkeyrunner/easy/By.java
//Synthetic comment -- index a8be6c0..1ed1c6f 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
* without notice.
*
* TODO: Implement other selectors, like classid, hash, and so on.
 * TODO: separate java-only core from jython wrapper
*/
public class By extends PyObject implements ClassDictInit {
public static void classDictInit(PyObject dict) {
//Synthetic comment -- @@ -62,6 +63,10 @@
return new By(id);
}

    public static By id(String id) {
        return new By(id);
    }

/**
* Find the selected view from the root view node.
*/








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/easy/EasyMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/easy/EasyMonkeyDevice.java
//Synthetic comment -- index 8e6ec0f..e72e462 100644

//Synthetic comment -- @@ -22,7 +22,7 @@
import com.android.hierarchyviewerlib.device.ViewNode.Property;
import com.android.monkeyrunner.JythonUtils;
import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.core.IMonkeyDevice.TouchPressType;
import com.android.monkeyrunner.doc.MonkeyRunnerExported;

import org.eclipse.swt.graphics.Point;
//Synthetic comment -- @@ -32,6 +32,7 @@
import org.python.core.PyException;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PyTuple;

import java.util.Set;
//Synthetic comment -- @@ -61,7 +62,7 @@
argDocs = { "MonkeyDevice to extend." })
public EasyMonkeyDevice(MonkeyDevice device) {
this.mDevice = device;
        this.mHierarchyViewer = device.getImpl().getHierarchyViewer();
}

@MonkeyRunnerExported(doc = "Sends a touch event to the selected object.",
//Synthetic comment -- @@ -74,19 +75,16 @@
Preconditions.checkNotNull(ap);

By selector = getSelector(ap, 0);
        String tmpType = ap.getString(1);
        TouchPressType type = MonkeyDevice.TOUCH_NAME_TO_ENUM.get(tmpType);
        if (type == null) type = TouchPressType.DOWN_AND_UP;
        // TODO: try catch rethrow PyExc
        touch(selector, type);
    }

    public void touch(By selector, TouchPressType type) {
        Point p = getElementCenter(selector);
        mDevice.getImpl().touch(p.x, p.y, type);
}

@MonkeyRunnerExported(doc = "Types a string into the specified object.",
//Synthetic comment -- @@ -100,16 +98,13 @@

By selector = getSelector(ap, 0);
String text = ap.getString(1);
        type(selector, text);
    }

    public void type(By selector, String text) {
        Point p = getElementCenter(selector);
        mDevice.getImpl().touch(p.x, p.y, TouchPressType.DOWN_AND_UP);
        mDevice.getImpl().type(text);
}

@MonkeyRunnerExported(doc = "Locates the coordinates of the selected object.",
//Synthetic comment -- @@ -141,7 +136,10 @@
Preconditions.checkNotNull(ap);

By selector = getSelector(ap, 0);
        return exists(selector);
    }

    public boolean exists(By selector) {
ViewNode node = mHierarchyViewer.findView(selector);
return node != null;
}
//Synthetic comment -- @@ -155,13 +153,11 @@
Preconditions.checkNotNull(ap);

By selector = getSelector(ap, 0);
        return visible(selector);
    }

    public boolean visible(By selector) {
        return mHierarchyViewer.visible(selector);
}

@MonkeyRunnerExported(doc = "Obtain the text in the selected input box.",
//Synthetic comment -- @@ -173,21 +169,20 @@
Preconditions.checkNotNull(ap);

By selector = getSelector(ap, 0);
        return getText(selector);
    }

    public String getText(By selector) {
        return mHierarchyViewer.getText(selector);
}

@MonkeyRunnerExported(doc = "Gets the id of the focused window.",
returns = "The symbolic id of the focused window or None.")
public String getFocusedWindowId(PyObject[] args, String[] kws) {
        return getFocusedWindowId();
    }

    public String getFocusedWindowId() {
return mHierarchyViewer.getFocusedWindowName();
}

//Synthetic comment -- @@ -210,6 +205,24 @@
* @return selector object.
*/
private By getSelector(ArgParser ap, int i) {
        return (By)ap.getPyObject(i).__tojava__(By.class);
}

    /**
     * Get the coordinates of the element's center.
     *
     * @param selector the element selector
     * @return the (x,y) coordinates of the center
     */
    private Point getElementCenter(By selector) {
        ViewNode node = mHierarchyViewer.findView(selector);
        if (node == null) {
            throw new PyException(Py.ValueError,
                    String.format("View not found: %s", selector));
        }

        Point p = HierarchyViewer.getAbsoluteCenterOfView(node);
        return p;
    }

}
\ No newline at end of file








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
//Synthetic comment -- index c1a8f7f..914a5b9 100644

//Synthetic comment -- @@ -15,8 +15,9 @@
*/
package com.android.monkeyrunner.recorder;

import com.android.monkeyrunner.adb.AdbBackend;
import com.android.monkeyrunner.core.IMonkeyBackend;
import com.android.monkeyrunner.core.IMonkeyDevice;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//Synthetic comment -- @@ -44,7 +45,7 @@
*
* @param device
*/
    public static void start(final IMonkeyDevice device) {
MonkeyRecorderFrame frame = new MonkeyRecorderFrame(device);
// TODO: this is a hack until the window listener works.
frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//Synthetic comment -- @@ -69,7 +70,7 @@
}

public static void main(String[] args) {
        IMonkeyBackend adb = new AdbBackend();
MonkeyRecorder.start(adb.waitForConnection());
}
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/MonkeyRecorderFrame.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/MonkeyRecorderFrame.java
//Synthetic comment -- index b6c1f78..88c1e16 100644

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
//Synthetic comment -- @@ -83,6 +84,7 @@
private ActionListModel actionListModel;

private final Timer refreshTimer = new Timer(1000, new ActionListener() {
        @Override
public void actionPerformed(ActionEvent e) {
refreshDisplay();  //  @jve:decl-index=0:
}
//Synthetic comment -- @@ -91,7 +93,7 @@
/**
* This is the default constructor
*/
    public MonkeyRecorderFrame(IMonkeyDevice device) {
this.device = device;
initialize();
}
//Synthetic comment -- @@ -110,7 +112,7 @@
}

private void refreshDisplay() {
        IMonkeyImage snapshot = device.takeSnapshot();
currentImage = snapshot.createBufferedImage();

Graphics2D g = scaledImage.createGraphics();
//Synthetic comment -- @@ -200,6 +202,7 @@
waitButton = new JButton();
waitButton.setText("Wait");
waitButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
public void actionPerformed(java.awt.event.ActionEvent e) {
String howLongStr = JOptionPane.showInputDialog("How many seconds to wait?");
if (howLongStr != null) {
//Synthetic comment -- @@ -222,6 +225,7 @@
pressButton = new JButton();
pressButton.setText("Press a Button");
pressButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
public void actionPerformed(java.awt.event.ActionEvent e) {
JPanel panel = new JPanel();
JLabel text = new JLabel("What button to press?");
//Synthetic comment -- @@ -255,6 +259,7 @@
typeButton = new JButton();
typeButton.setText("Type Something");
typeButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
public void actionPerformed(java.awt.event.ActionEvent e) {
String whatToType = JOptionPane.showInputDialog("What to type?");
if (whatToType != null) {
//Synthetic comment -- @@ -276,6 +281,7 @@
flingButton = new JButton();
flingButton.setText("Fling");
flingButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
public void actionPerformed(java.awt.event.ActionEvent e) {
JPanel panel = new JPanel();
panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//Synthetic comment -- @@ -358,6 +364,7 @@
exportActionButton = new JButton();
exportActionButton.setText("Export Actions");
exportActionButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
public void actionPerformed(java.awt.event.ActionEvent ev) {
JFileChooser fc = new JFileChooser();
if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
//Synthetic comment -- @@ -383,6 +390,7 @@
refreshButton = new JButton();
refreshButton.setText("Refresh Display");
refreshButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
public void actionPerformed(java.awt.event.ActionEvent e) {
refreshDisplay();
}








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
//Synthetic comment -- index 082bfe4..2461c0d 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.monkeyrunner.recorder.actions;

import com.android.monkeyrunner.core.IMonkeyDevice;

/**
* Action to drag the "finger" across the device.
//Synthetic comment -- @@ -77,7 +77,7 @@
}

@Override
    public void execute(IMonkeyDevice device) {
device.drag(startx, starty, endx, endy, steps, timeMs);
}
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/PressAction.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/PressAction.java
//Synthetic comment -- index a0d9e0e..66a933a 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.google.common.collect.ImmutableBiMap;

import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.core.IMonkeyDevice;

/**
* Action to press a certain button.
//Synthetic comment -- @@ -60,7 +61,7 @@
}

@Override
    public void execute(IMonkeyDevice device) {
device.press(key,
MonkeyDevice.TOUCH_NAME_TO_ENUM.get(downUpFlag));
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/TouchAction.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/TouchAction.java
//Synthetic comment -- index 4633edb..4e0ae2d 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.google.common.collect.ImmutableBiMap;

import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.core.IMonkeyDevice;

/**
* Action to touch the touchscreen at a certain location.
//Synthetic comment -- @@ -46,7 +47,7 @@
}

@Override
    public void execute(IMonkeyDevice device) throws Exception {
device.touch(x, y,
MonkeyDevice.TOUCH_NAME_TO_ENUM.get(direction));
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/TypeAction.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/TypeAction.java
//Synthetic comment -- index 1bfb9e9..78e90b0 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.monkeyrunner.recorder.actions;

import com.android.monkeyrunner.core.IMonkeyDevice;

/**
* Action to type in a string on the device.
//Synthetic comment -- @@ -34,12 +34,13 @@

@Override
public String serialize() {
        String pydict = PyDictUtilBuilder.newBuilder()
                .add("message", whatToType).build();
return "TYPE|" + pydict;
}

@Override
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
//Synthetic comment -- index c2fa5f7..b868bf1 100644

//Synthetic comment -- @@ -15,18 +15,22 @@
*/
package com.android.monkeyrunner.stub;

import com.android.monkeyrunner.MonkeyManager;
import com.android.monkeyrunner.core.IMonkeyBackend;
import com.android.monkeyrunner.core.IMonkeyDevice;

public class StubBackend implements IMonkeyBackend {
public MonkeyManager createManager(String address, int port) {
// TODO Auto-generated method stub
return null;
}

    public IMonkeyDevice waitForConnection() {
        // TODO Auto-generated method stub
        return null;
    }

    public IMonkeyDevice waitForConnection(long timeout, String deviceId) {
// TODO Auto-generated method stub
return null;
}







