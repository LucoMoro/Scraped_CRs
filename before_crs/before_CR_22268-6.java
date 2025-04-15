/*Separate MonkeyRunner core logic

    Refactored the MonkeyRunner code to separate core logic
    from the jython wrapper. The core logic is now usable
    directly from Java w/o the pollution from jython.
    The existing MonkeyRunner classes are now just a thin
    and dumb wrapper atop the core.

Change-Id:I6ef18ea92e0e9284c1fde949b4efb0e2e7170e57*/
//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java
//Synthetic comment -- index 37b1cda..363b8f8 100644

//Synthetic comment -- @@ -20,8 +20,6 @@
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.python.core.ArgParser;
import org.python.core.ClassDictInit;
import org.python.core.Py;
//Synthetic comment -- @@ -30,6 +28,8 @@
import org.python.core.PyObject;
import org.python.core.PyTuple;

import com.android.monkeyrunner.doc.MonkeyRunnerExported;
import com.android.monkeyrunner.easy.HierarchyViewer;

//Synthetic comment -- @@ -45,7 +45,7 @@
* implementation of this class.
*/
@MonkeyRunnerExported(doc = "Represents a device attached to the system.")
public abstract class MonkeyDevice extends PyObject implements ClassDictInit {
public static void classDictInit(PyObject dict) {
JythonUtils.convertDocAnnotationsForClass(MonkeyDevice.class, dict);
}
//Synthetic comment -- @@ -57,49 +57,42 @@
@MonkeyRunnerExported(doc = "Sends a DOWN event, immediately followed by an UP event when used with touch() or press()")
public static final String DOWN_AND_UP = "downAndUp";

public enum TouchPressType {
DOWN, UP, DOWN_AND_UP,
}

    public static final Map<String, TouchPressType> TOUCH_NAME_TO_ENUM =
        ImmutableMap.of(MonkeyDevice.DOWN, TouchPressType.DOWN,
                MonkeyDevice.UP, TouchPressType.UP,
                MonkeyDevice.DOWN_AND_UP, TouchPressType.DOWN_AND_UP);

private static final Set<String> VALID_DOWN_UP_TYPES = TOUCH_NAME_TO_ENUM.keySet();

    /**
     * Create a MonkeyMananger for talking to this device.
     *
     * NOTE: This is not part of the jython API.
     *
     * @return the MonkeyManager
     */
    public abstract MonkeyManager getManager();

    /**
     * Dispose of any native resoureces this device may have taken hold of.
     *
     *  NOTE: This is not part of the jython API.
     */
    public abstract void dispose();

    /**
     * @return hierarchy viewer implementation for querying state of the view
     * hierarchy.
     */
    public abstract HierarchyViewer getHierarchyViewer();

@MonkeyRunnerExported(doc = "Get the HierarchyViewer object for the device.",
returns = "A HierarchyViewer object")
public HierarchyViewer getHierarchyViewer(PyObject[] args, String[] kws) {
        return getHierarchyViewer();
}

@MonkeyRunnerExported(doc =
"Gets the device's screen buffer, yielding a screen capture of the entire display.",
returns = "A MonkeyImage object (a bitmap wrapper)")
    public abstract MonkeyImage takeSnapshot();

@MonkeyRunnerExported(doc = "Given the name of a variable on the device, " +
"returns the variable's value",
//Synthetic comment -- @@ -111,7 +104,7 @@
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);

        return getProperty(ap.getString(0));
}

@MonkeyRunnerExported(doc = "Synonym for getProperty()",
//Synthetic comment -- @@ -121,7 +114,8 @@
public String getSystemProperty(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
        return getSystemProperty(ap.getString(0));
}

@MonkeyRunnerExported(doc = "Sends a touch event at the specified location",
//Synthetic comment -- @@ -150,7 +144,7 @@
// bad stuff was passed in, just use the already specified default value
type = MonkeyDevice.DOWN_AND_UP;
}
        touch(x, y, TOUCH_NAME_TO_ENUM.get(type));
}

@MonkeyRunnerExported(doc = "Simulates dragging (touch, hold, and move) on the device screen.",
//Synthetic comment -- @@ -185,7 +179,7 @@

int steps = ap.getInt(3, 10);

        drag(startx, starty, endx, endy, steps, ms);
}

@MonkeyRunnerExported(doc = "Send a key event to the specified key",
//Synthetic comment -- @@ -213,7 +207,7 @@
// bad stuff was passed in, just use the already specified default value
type = MonkeyDevice.DOWN_AND_UP;
}
        press(name, TOUCH_NAME_TO_ENUM.get(type));
}

@MonkeyRunnerExported(doc = "Types the specified string on the keyboard. This is " +
//Synthetic comment -- @@ -225,7 +219,7 @@
Preconditions.checkNotNull(ap);

String message = ap.getString(0);
        type(message);
}

@MonkeyRunnerExported(doc = "Executes an adb shell command and returns the result, if any.",
//Synthetic comment -- @@ -237,7 +231,7 @@
Preconditions.checkNotNull(ap);

String cmd = ap.getString(0);
        return shell(cmd);
}

@MonkeyRunnerExported(doc = "Reboots the specified device into a specified bootloader.",
//Synthetic comment -- @@ -249,7 +243,7 @@

String into = ap.getString(0, null);

        reboot(into);
}

@MonkeyRunnerExported(doc = "Installs the specified Android package (.apk file) " +
//Synthetic comment -- @@ -262,7 +256,7 @@
Preconditions.checkNotNull(ap);

String path = ap.getString(0);
        return installPackage(path);
}

@MonkeyRunnerExported(doc = "Deletes the specified package from the device, including its " +
//Synthetic comment -- @@ -275,7 +269,7 @@
Preconditions.checkNotNull(ap);

String packageName = ap.getString(0);
        return removePackage(packageName);
}

@MonkeyRunnerExported(doc = "Starts an Activity on the device by sending an Intent " +
//Synthetic comment -- @@ -308,7 +302,7 @@
String component = ap.getString(6, null);
int flags = ap.getInt(7, 0);

        startActivity(uri, action, data, mimetype, categories, extras, component, flags);
}

@MonkeyRunnerExported(doc = "Sends a broadcast intent to the device.",
//Synthetic comment -- @@ -340,7 +334,7 @@
String component = ap.getString(6, null);
int flags = ap.getInt(7, 0);

        broadcastIntent(uri, action, data, mimetype, categories, extras, component, flags);
}

@MonkeyRunnerExported(doc = "Run the specified package with instrumentation and return " +
//Synthetic comment -- @@ -368,7 +362,7 @@
instrumentArgs = Collections.emptyMap();
}

        Map<String, Object> result = instrument(packageName, instrumentArgs);
return JythonUtils.convertMapToDict(result);
}

//Synthetic comment -- @@ -377,34 +371,6 @@
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);

        wake();
}

    /**
     * Reboot the device.
     *
     * @param into which bootloader to boot into.  Null means default reboot.
     */
    public abstract void reboot(@Nullable String into);

    public abstract String getProperty(String key);
    public abstract String getSystemProperty(String key);
    public abstract void touch(int x, int y, TouchPressType type);
    public abstract void press(String keyName, TouchPressType type);
    public abstract void drag(int startx, int starty, int endx, int endy, int steps, long ms);
    public abstract void type(String string);
    public abstract String shell(String cmd);
    public abstract boolean installPackage(String path);
    public abstract boolean removePackage(String packageName);
    public abstract void startActivity(@Nullable String uri, @Nullable String action,
            @Nullable String data, @Nullable String mimetype,
            Collection<String> categories, Map<String, Object> extras, @Nullable String component,
            int flags);
    public abstract void broadcastIntent(@Nullable String uri, @Nullable String action,
            @Nullable String data, @Nullable String mimetype,
            Collection<String> categories, Map<String, Object> extras, @Nullable String component,
            int flags);
    public abstract Map<String, Object> instrument(String packageName,
            Map<String, Object> args);
    public abstract void wake();
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyImage.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyImage.java
//Synthetic comment -- index 70201ee..b55b4f3 100644

//Synthetic comment -- @@ -17,6 +17,7 @@

import com.google.common.base.Preconditions;

import com.android.monkeyrunner.doc.MonkeyRunnerExported;

import org.python.core.ArgParser;
//Synthetic comment -- @@ -25,61 +26,31 @@
import org.python.core.PyObject;
import org.python.core.PyTuple;

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
* Jython object to encapsulate images that have been taken.
*/
@MonkeyRunnerExported(doc = "An image")
public abstract class MonkeyImage extends PyObject implements ClassDictInit {
private static Logger LOG = Logger.getLogger(MonkeyImage.class.getCanonicalName());

public static void classDictInit(PyObject dict) {
JythonUtils.convertDocAnnotationsForClass(MonkeyImage.class, dict);
}

    /**
     * Convert the MonkeyImage into a BufferedImage.
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
    private BufferedImage getBufferedImage() {
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

@MonkeyRunnerExported(doc = "Converts the MonkeyImage into a particular format and returns " +
"the result as a String. Use this to get access to the raw" +
"pixels in a particular format. String output is for better " +
//Synthetic comment -- @@ -93,16 +64,7 @@
Preconditions.checkNotNull(ap);

String format = ap.getString(0, "png");

      BufferedImage argb = convertSnapshot();

      ByteArrayOutputStream os = new ByteArrayOutputStream();
      try {
          ImageIO.write(argb, format, os);
      } catch (IOException e) {
          return new byte[0];
      }
      return os.toByteArray();
}

@MonkeyRunnerExported(doc = "Write the MonkeyImage to a file.  If no " +
//Synthetic comment -- @@ -120,38 +82,7 @@

String path = ap.getString(0);
String format = ap.getString(1, null);

        if (format != null) {
            return writeToFile(path, format);
        }
        int offset = path.lastIndexOf('.');
        if (offset < 0) {
            return writeToFile(path, "png");
        }
        String ext = path.substring(offset + 1);
        Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix(ext);
        if (!writers.hasNext()) {
            return writeToFile(path, "png");
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

@MonkeyRunnerExported(doc = "Get a single ARGB (alpha, red, green, blue) pixel at location " +
//Synthetic comment -- @@ -167,7 +98,7 @@

int x = ap.getInt(0);
int y = ap.getInt(1);
        int pixel = getPixel(x, y);
PyInteger a = new PyInteger((pixel & 0xFF000000) >> 24);
PyInteger r = new PyInteger((pixel & 0x00FF0000) >> 16);
PyInteger g = new PyInteger((pixel & 0x0000FF00) >> 8);
//Synthetic comment -- @@ -188,35 +119,7 @@

int x = ap.getInt(0);
int y = ap.getInt(1);
        return getPixel(x, y);
    }

    private int getPixel(int x, int y) {
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

    public boolean writeToFile(String path, String format) {
        BufferedImage argb = convertSnapshot();

        try {
            ImageIO.write(argb, format, new File(path));
        } catch (IOException e) {
            return false;
        }
        return true;
}

@MonkeyRunnerExported(doc = "Compare this MonkeyImage object to aother MonkeyImage object.",
//Synthetic comment -- @@ -231,72 +134,13 @@
Preconditions.checkNotNull(ap);

PyObject otherObject = ap.getPyObject(0);
        MonkeyImage other = (MonkeyImage) otherObject.__tojava__(MonkeyImage.class);

double percent = JythonUtils.getFloat(ap, 1, 1.0);

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

    private static class BufferedImageMonkeyImage extends MonkeyImage {
        private final BufferedImage image;

        public BufferedImageMonkeyImage(BufferedImage image) {
            this.image = image;
        }

        @Override
        public BufferedImage createBufferedImage() {
            return image;
        }
    }

    /* package */ static MonkeyImage loadImageFromFile(String path) {
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

@MonkeyRunnerExported(doc = "Copy a rectangular region of the image.",
//Synthetic comment -- @@ -315,7 +159,7 @@
int w = rect.__getitem__(2).asInt();
int h = rect.__getitem__(3).asInt();

        BufferedImage image = getBufferedImage();
        return new BufferedImageMonkeyImage(image.getSubimage(x, y, w, h));
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunner.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunner.java
//Synthetic comment -- index 5f137cd..5529802 100644

//Synthetic comment -- @@ -19,6 +19,10 @@
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;

import com.android.monkeyrunner.doc.MonkeyRunnerExported;

import org.python.core.ArgParser;
//Synthetic comment -- @@ -38,7 +42,7 @@
@MonkeyRunnerExported(doc = "Main entry point for MonkeyRunner")
public class MonkeyRunner extends PyObject implements ClassDictInit {
private static final Logger LOG = Logger.getLogger(MonkeyRunner.class.getCanonicalName());
    private static MonkeyRunnerBackend backend;

public static void classDictInit(PyObject dict) {
JythonUtils.convertDocAnnotationsForClass(MonkeyRunner.class, dict);
//Synthetic comment -- @@ -49,7 +53,7 @@
*
* @param backend the backend to use.
*/
    /* package */ static void setBackend(MonkeyRunnerBackend backend) {
MonkeyRunner.backend = backend;
}

//Synthetic comment -- @@ -71,8 +75,10 @@
timeoutMs = Long.MAX_VALUE;
}

        return backend.waitForConnection(timeoutMs,
ap.getString(1, ".*"));
}

@MonkeyRunnerExported(doc = "Pause the currently running program for the specified " +
//Synthetic comment -- @@ -185,8 +191,8 @@
Preconditions.checkNotNull(ap);

String path = ap.getString(0);

        return MonkeyImage.loadImageFromFile(path);
}

/**








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerStarter.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerStarter.java
//Synthetic comment -- index 5244dbc..8c9942c 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.google.common.collect.ImmutableMap;

import com.android.monkeyrunner.adb.AdbBackend;
import com.android.monkeyrunner.stub.StubBackend;

import org.python.util.PythonInterpreter;
//Synthetic comment -- @@ -50,7 +51,7 @@
private static final Logger LOG = Logger.getLogger(MonkeyRunnerStarter.class.getName());
private static final String MONKEY_RUNNER_MAIN_MANIFEST_NAME = "MonkeyRunnerStartupRunner";

    private final MonkeyRunnerBackend backend;
private final MonkeyRunnerOptions options;

public MonkeyRunnerStarter(MonkeyRunnerOptions options) {
//Synthetic comment -- @@ -68,7 +69,7 @@
* @param backendName the name of the backend to create
* @return the new backend, or null if none were found.
*/
    public static MonkeyRunnerBackend createBackendByName(String backendName) {
if ("adb".equals(backendName)) {
return new AdbBackend();
} else if ("stub".equals(backendName)) {








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/adb/AdbBackend.java b/monkeyrunner/src/com/android/monkeyrunner/adb/AdbBackend.java
//Synthetic comment -- index 455d131..49cac08 100644

//Synthetic comment -- @@ -19,8 +19,8 @@

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.MonkeyRunnerBackend;
import com.android.sdklib.SdkConstants;

import java.io.File;
//Synthetic comment -- @@ -32,12 +32,11 @@
/**
* Backend implementation that works over ADB to talk to the device.
*/
public class AdbBackend implements MonkeyRunnerBackend {
private static Logger LOG = Logger.getLogger(AdbBackend.class.getCanonicalName());
// How long to wait each time we check for the device to be connected.
private static final int CONNECTION_ITERATION_TIMEOUT_MS = 200;
    private final List<AdbMonkeyDevice> devices = Lists.newArrayList();

private final AndroidDebugBridge bridge;

public AdbBackend() {
//Synthetic comment -- @@ -87,18 +86,20 @@
return null;
}

    public MonkeyDevice waitForConnection() {
return waitForConnection(Integer.MAX_VALUE, ".*");
}

    public MonkeyDevice waitForConnection(long timeoutMs, String deviceIdRegex) {
do {
IDevice device = findAttacedDevice(deviceIdRegex);
// Only return the device when it is online
if (device != null && device.getState() == IDevice.DeviceState.ONLINE) {
                AdbMonkeyDevice amd = new AdbMonkeyDevice(device);
                devices.add(amd);
                return amd;
}

try {
//Synthetic comment -- @@ -113,8 +114,9 @@
return null;
}

public void shutdown() {
        for (AdbMonkeyDevice device : devices) {
device.dispose();
}
AndroidDebugBridge.terminate();








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/adb/AdbMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/adb/AdbMonkeyDevice.java
//Synthetic comment -- index 614e656..60eaba9 100644

//Synthetic comment -- @@ -25,10 +25,10 @@
import com.android.ddmlib.InstallException;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;
import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.MonkeyImage;
import com.android.monkeyrunner.MonkeyManager;
import com.android.monkeyrunner.adb.LinearInterpolator.Point;
import com.android.monkeyrunner.easy.HierarchyViewer;

import java.io.IOException;
//Synthetic comment -- @@ -48,7 +48,7 @@

import javax.annotation.Nullable;

public class AdbMonkeyDevice extends MonkeyDevice {
private static final Logger LOG = Logger.getLogger(AdbMonkeyDevice.class.getName());

private static final String[] ZERO_LENGTH_STRING_ARRAY = new String[0];
//Synthetic comment -- @@ -90,6 +90,7 @@
private void executeAsyncCommand(final String command,
final LoggingOutputReceiver logger) {
executor.submit(new Runnable() {
public void run() {
try {
device.executeShellCommand(command, logger);
//Synthetic comment -- @@ -190,7 +191,7 @@
}

@Override
    public MonkeyImage takeSnapshot() {
try {
return new AdbMonkeyImage(device.getScreenshot());
} catch (TimeoutException e) {
//Synthetic comment -- @@ -505,6 +506,7 @@
LinearInterpolator.Point start = new LinearInterpolator.Point(startx, starty);
LinearInterpolator.Point end = new LinearInterpolator.Point(endx, endy);
lerp.interpolate(start, end, new LinearInterpolator.Callback() {
public void step(Point point) {
try {
manager.touchMove(point.getX(), point.getY());
//Synthetic comment -- @@ -519,6 +521,7 @@
}
}

public void start(Point point) {
try {
manager.touchDown(point.getX(), point.getY());
//Synthetic comment -- @@ -534,6 +537,7 @@
}
}

public void end(Point point) {
try {
manager.touchMove(point.getX(), point.getY());








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/adb/AdbMonkeyImage.java b/monkeyrunner/src/com/android/monkeyrunner/adb/AdbMonkeyImage.java
//Synthetic comment -- index fc32600..e2bd86e 100644

//Synthetic comment -- @@ -16,15 +16,15 @@
package com.android.monkeyrunner.adb;

import com.android.ddmlib.RawImage;
import com.android.monkeyrunner.MonkeyImage;
import com.android.monkeyrunner.adb.image.ImageUtils;

import java.awt.image.BufferedImage;

/**
* ADB implementation of the MonkeyImage class.
*/
public class AdbMonkeyImage extends MonkeyImage {
private final RawImage image;

/**








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/adb/image/CaptureRawAndConvertedImage.java b/monkeyrunner/src/com/android/monkeyrunner/adb/image/CaptureRawAndConvertedImage.java
//Synthetic comment -- index 7e31ea5..5a317f1 100644

//Synthetic comment -- @@ -16,9 +16,11 @@
package com.android.monkeyrunner.adb.image;

import com.android.ddmlib.RawImage;
import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.adb.AdbBackend;
import com.android.monkeyrunner.adb.AdbMonkeyImage;

import java.io.FileOutputStream;
import java.io.IOException;
//Synthetic comment -- @@ -94,13 +96,13 @@
}

public static void main(String[] args) throws IOException {
        AdbBackend backend = new AdbBackend();
        MonkeyDevice device = backend.waitForConnection();
        AdbMonkeyImage snapshot = (AdbMonkeyImage) device.takeSnapshot();

// write out to a file
snapshot.writeToFile("output.png", "png");
        writeOutImage(snapshot.getRawImage(), "output.raw");
System.exit(0);
}
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/controller/MonkeyController.java b/monkeyrunner/src/com/android/monkeyrunner/controller/MonkeyController.java
//Synthetic comment -- index e199a75..ca3195c 100644

//Synthetic comment -- @@ -15,8 +15,9 @@
*/
package com.android.monkeyrunner.controller;

import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.adb.AdbBackend;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//Synthetic comment -- @@ -36,9 +37,10 @@

public static void main(String[] args) {
SwingUtilities.invokeLater(new Runnable() {
public void run() {
                AdbBackend adb = new AdbBackend();
                final MonkeyDevice device = adb.waitForConnection();
MonkeyControllerFrame mf = new MonkeyControllerFrame(device);
mf.setVisible(true);
mf.addWindowListener(new WindowAdapter() {








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/controller/MonkeyControllerFrame.java b/monkeyrunner/src/com/android/monkeyrunner/controller/MonkeyControllerFrame.java
//Synthetic comment -- index 7f5a7d8..7750936 100644

//Synthetic comment -- @@ -15,10 +15,10 @@
*/
package com.android.monkeyrunner.controller;

import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.MonkeyImage;
import com.android.monkeyrunner.MonkeyManager;
import com.android.monkeyrunner.PhysicalButton;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
//Synthetic comment -- @@ -61,7 +61,7 @@
}
});

    private final MonkeyDevice device;

private class PressAction extends AbstractAction {
private final PhysicalButton button;
//Synthetic comment -- @@ -85,7 +85,7 @@
return button;
}

    public MonkeyControllerFrame(MonkeyDevice device) {
super("MonkeyController");
this.device = device;

//Synthetic comment -- @@ -155,7 +155,7 @@
}

private void updateScreen() {
        MonkeyImage snapshot = device.takeSnapshot();
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
package com.android.monkeyrunner;

/**
* Interface between MonkeyRunner common code and the MonkeyRunner backend.  The backend is
* responsible for communicating between the host and the device.
*/
public interface MonkeyRunnerBackend {
/**
* Wait for a device to connect to the backend.
*
//Synthetic comment -- @@ -27,7 +36,7 @@
* @param deviceIdRegex the regular expression to specify which device to wait for.
* @return the connected device (or null if timeout);
*/
    MonkeyDevice waitForConnection(long timeoutMs, String deviceIdRegex);

/**
* Shutdown the backend and cleanup any resources it was using.








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyDevice.java
new file mode 100644
//Synthetic comment -- index 0000000..68d0267

//Synthetic comment -- @@ -0,0 +1,86 @@








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyImage.java b/monkeyrunner/src/com/android/monkeyrunner/core/IMonkeyImage.java
new file mode 100644
//Synthetic comment -- index 0000000..5a24fa7

//Synthetic comment -- @@ -0,0 +1,36 @@








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/core/MonkeyImageBase.java b/monkeyrunner/src/com/android/monkeyrunner/core/MonkeyImageBase.java
new file mode 100644
//Synthetic comment -- index 0000000..b57d78f

//Synthetic comment -- @@ -0,0 +1,205 @@








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/easy/By.java b/monkeyrunner/src/com/android/monkeyrunner/easy/By.java
//Synthetic comment -- index a8be6c0..1ed1c6f 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
* without notice.
*
* TODO: Implement other selectors, like classid, hash, and so on.
*/
public class By extends PyObject implements ClassDictInit {
public static void classDictInit(PyObject dict) {
//Synthetic comment -- @@ -62,6 +63,10 @@
return new By(id);
}

/**
* Find the selected view from the root view node.
*/








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/easy/EasyMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/easy/EasyMonkeyDevice.java
//Synthetic comment -- index 8e6ec0f..cc08702 100644

//Synthetic comment -- @@ -22,7 +22,7 @@
import com.android.hierarchyviewerlib.device.ViewNode.Property;
import com.android.monkeyrunner.JythonUtils;
import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.MonkeyDevice.TouchPressType;
import com.android.monkeyrunner.doc.MonkeyRunnerExported;

import org.eclipse.swt.graphics.Point;
//Synthetic comment -- @@ -32,6 +32,7 @@
import org.python.core.PyException;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyTuple;

import java.util.Set;
//Synthetic comment -- @@ -61,7 +62,7 @@
argDocs = { "MonkeyDevice to extend." })
public EasyMonkeyDevice(MonkeyDevice device) {
this.mDevice = device;
        this.mHierarchyViewer = device.getHierarchyViewer();
}

@MonkeyRunnerExported(doc = "Sends a touch event to the selected object.",
//Synthetic comment -- @@ -74,19 +75,14 @@
Preconditions.checkNotNull(ap);

By selector = getSelector(ap, 0);
        ViewNode node = mHierarchyViewer.findView(selector);
        if (node == null) {
            throw new PyException(Py.ValueError,
                    String.format("View not found: %s", selector));
        }
        Point p = HierarchyViewer.getAbsoluteCenterOfView(node);

        PyObject[] otherArgs = new PyObject[3];
        otherArgs[0] = new PyInteger(p.x);
        otherArgs[1] = new PyInteger(p.y);
        otherArgs[2] = args[1];

        mDevice.touch(otherArgs, kws);
}

@MonkeyRunnerExported(doc = "Types a string into the specified object.",
//Synthetic comment -- @@ -100,16 +96,13 @@

By selector = getSelector(ap, 0);
String text = ap.getString(1);

        ViewNode node = mHierarchyViewer.findView(selector);
        if (node == null) {
            throw new PyException(Py.ValueError,
                    String.format("View not found: %s", selector));
        }

        Point p = HierarchyViewer.getAbsoluteCenterOfView(node);
        mDevice.touch(p.x, p.y, TouchPressType.DOWN_AND_UP);
        mDevice.type(text);
}

@MonkeyRunnerExported(doc = "Locates the coordinates of the selected object.",
//Synthetic comment -- @@ -141,7 +134,10 @@
Preconditions.checkNotNull(ap);

By selector = getSelector(ap, 0);

ViewNode node = mHierarchyViewer.findView(selector);
return node != null;
}
//Synthetic comment -- @@ -155,13 +151,11 @@
Preconditions.checkNotNull(ap);

By selector = getSelector(ap, 0);

        ViewNode node = mHierarchyViewer.findView(selector);
        boolean ret = (node != null)
                && node.namedProperties.containsKey("getVisibility()")
                && "VISIBLE".equalsIgnoreCase(
                        node.namedProperties.get("getVisibility()").value);
        return ret;
}

@MonkeyRunnerExported(doc = "Obtain the text in the selected input box.",
//Synthetic comment -- @@ -173,21 +167,20 @@
Preconditions.checkNotNull(ap);

By selector = getSelector(ap, 0);

        ViewNode node = mHierarchyViewer.findView(selector);
        if (node == null) {
            throw new RuntimeException("Node not found");
        }
        Property textProperty = node.namedProperties.get("text:mText");
        if (textProperty == null) {
            throw new RuntimeException("No text property on node");
        }
        return textProperty.value;
}

@MonkeyRunnerExported(doc = "Gets the id of the focused window.",
returns = "The symbolic id of the focused window or None.")
public String getFocusedWindowId(PyObject[] args, String[] kws) {
return mHierarchyViewer.getFocusedWindowName();
}

//Synthetic comment -- @@ -210,6 +203,24 @@
* @return selector object.
*/
private By getSelector(ArgParser ap, int i) {
        return (By)ap.getPyObject(0).__tojava__(By.class);
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/easy/HierarchyViewer.java b/monkeyrunner/src/com/android/monkeyrunner/easy/HierarchyViewer.java
//Synthetic comment -- index 5d6911e..450571c 100644

//Synthetic comment -- @@ -121,4 +121,38 @@
return new Point(
point.x + (node.width / 2), point.y + (node.height / 2));
}
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/MonkeyRecorder.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/MonkeyRecorder.java
//Synthetic comment -- index c1a8f7f..914a5b9 100644

//Synthetic comment -- @@ -15,8 +15,9 @@
*/
package com.android.monkeyrunner.recorder;

import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.adb.AdbBackend;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//Synthetic comment -- @@ -44,7 +45,7 @@
*
* @param device
*/
    public static void start(final MonkeyDevice device) {
MonkeyRecorderFrame frame = new MonkeyRecorderFrame(device);
// TODO: this is a hack until the window listener works.
frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//Synthetic comment -- @@ -69,7 +70,7 @@
}

public static void main(String[] args) {
        AdbBackend adb = new AdbBackend();
MonkeyRecorder.start(adb.waitForConnection());
}
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/MonkeyRecorderFrame.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/MonkeyRecorderFrame.java
//Synthetic comment -- index b6c1f78..73ede62 100644

//Synthetic comment -- @@ -16,7 +16,8 @@
package com.android.monkeyrunner.recorder;

import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.MonkeyImage;
import com.android.monkeyrunner.recorder.actions.Action;
import com.android.monkeyrunner.recorder.actions.DragAction;
import com.android.monkeyrunner.recorder.actions.DragAction.Direction;
//Synthetic comment -- @@ -60,7 +61,7 @@
private static final Logger LOG =
Logger.getLogger(MonkeyRecorderFrame.class.getName());

    private final MonkeyDevice device;

private static final long serialVersionUID = 1L;
private JPanel jContentPane = null;
//Synthetic comment -- @@ -83,6 +84,7 @@
private ActionListModel actionListModel;

private final Timer refreshTimer = new Timer(1000, new ActionListener() {
public void actionPerformed(ActionEvent e) {
refreshDisplay();  //  @jve:decl-index=0:
}
//Synthetic comment -- @@ -91,7 +93,7 @@
/**
* This is the default constructor
*/
    public MonkeyRecorderFrame(MonkeyDevice device) {
this.device = device;
initialize();
}
//Synthetic comment -- @@ -102,7 +104,6 @@
this.setTitle("MonkeyRecorder");

SwingUtilities.invokeLater(new Runnable() {
            @Override
public void run() {
refreshDisplay();
}});
//Synthetic comment -- @@ -110,7 +111,7 @@
}

private void refreshDisplay() {
        MonkeyImage snapshot = device.takeSnapshot();
currentImage = snapshot.createBufferedImage();

Graphics2D g = scaledImage.createGraphics();
//Synthetic comment -- @@ -200,6 +201,7 @@
waitButton = new JButton();
waitButton.setText("Wait");
waitButton.addActionListener(new java.awt.event.ActionListener() {
public void actionPerformed(java.awt.event.ActionEvent e) {
String howLongStr = JOptionPane.showInputDialog("How many seconds to wait?");
if (howLongStr != null) {
//Synthetic comment -- @@ -222,6 +224,7 @@
pressButton = new JButton();
pressButton.setText("Press a Button");
pressButton.addActionListener(new java.awt.event.ActionListener() {
public void actionPerformed(java.awt.event.ActionEvent e) {
JPanel panel = new JPanel();
JLabel text = new JLabel("What button to press?");
//Synthetic comment -- @@ -255,6 +258,7 @@
typeButton = new JButton();
typeButton.setText("Type Something");
typeButton.addActionListener(new java.awt.event.ActionListener() {
public void actionPerformed(java.awt.event.ActionEvent e) {
String whatToType = JOptionPane.showInputDialog("What to type?");
if (whatToType != null) {
//Synthetic comment -- @@ -276,6 +280,7 @@
flingButton = new JButton();
flingButton.setText("Fling");
flingButton.addActionListener(new java.awt.event.ActionListener() {
public void actionPerformed(java.awt.event.ActionEvent e) {
JPanel panel = new JPanel();
panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//Synthetic comment -- @@ -358,6 +363,7 @@
exportActionButton = new JButton();
exportActionButton.setText("Export Actions");
exportActionButton.addActionListener(new java.awt.event.ActionListener() {
public void actionPerformed(java.awt.event.ActionEvent ev) {
JFileChooser fc = new JFileChooser();
if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
//Synthetic comment -- @@ -383,6 +389,7 @@
refreshButton = new JButton();
refreshButton.setText("Refresh Display");
refreshButton.addActionListener(new java.awt.event.ActionListener() {
public void actionPerformed(java.awt.event.ActionEvent e) {
refreshDisplay();
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/Action.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/Action.java
//Synthetic comment -- index d582aa4..6fa91ab 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.monkeyrunner.recorder.actions;

import com.android.monkeyrunner.MonkeyDevice;

/**
* All actions that can be recorded must implement this interface.
//Synthetic comment -- @@ -41,5 +41,5 @@
*
* @param device the device to execute the action on.
*/
    void execute(MonkeyDevice device) throws Exception;
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/DragAction.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/DragAction.java
//Synthetic comment -- index 082bfe4..2461c0d 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.monkeyrunner.recorder.actions;

import com.android.monkeyrunner.MonkeyDevice;

/**
* Action to drag the "finger" across the device.
//Synthetic comment -- @@ -77,7 +77,7 @@
}

@Override
    public void execute(MonkeyDevice device) {
device.drag(startx, starty, endx, endy, steps, timeMs);
}
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/PressAction.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/PressAction.java
//Synthetic comment -- index a0d9e0e..66a933a 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.google.common.collect.ImmutableBiMap;

import com.android.monkeyrunner.MonkeyDevice;

/**
* Action to press a certain button.
//Synthetic comment -- @@ -60,7 +61,7 @@
}

@Override
    public void execute(MonkeyDevice device) {
device.press(key,
MonkeyDevice.TOUCH_NAME_TO_ENUM.get(downUpFlag));
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/TouchAction.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/TouchAction.java
//Synthetic comment -- index 4633edb..4e0ae2d 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.google.common.collect.ImmutableBiMap;

import com.android.monkeyrunner.MonkeyDevice;

/**
* Action to touch the touchscreen at a certain location.
//Synthetic comment -- @@ -46,7 +47,7 @@
}

@Override
    public void execute(MonkeyDevice device) throws Exception {
device.touch(x, y,
MonkeyDevice.TOUCH_NAME_TO_ENUM.get(direction));
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/TypeAction.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/TypeAction.java
//Synthetic comment -- index 1bfb9e9..45e1470 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.monkeyrunner.recorder.actions;

import com.android.monkeyrunner.MonkeyDevice;

/**
* Action to type in a string on the device.
//Synthetic comment -- @@ -27,19 +27,16 @@
this.whatToType = whatToType;
}

    @Override
public String getDisplayName() {
return String.format("Type \"%s\"", whatToType);
}

    @Override
public String serialize() {
String pydict = PyDictUtilBuilder.newBuilder().add("message", whatToType).build();
return "TYPE|" + pydict;
}

    @Override
    public void execute(MonkeyDevice device) {
device.type(whatToType);
}
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/WaitAction.java b/monkeyrunner/src/com/android/monkeyrunner/recorder/actions/WaitAction.java
//Synthetic comment -- index 9115f9a..bd2d421 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.monkeyrunner.recorder.actions;

import com.android.monkeyrunner.MonkeyDevice;

/**
* Action that specifies to wait for a certain amount of time.
//Synthetic comment -- @@ -27,19 +27,16 @@
this.howLongSeconds = howLongSeconds;
}

    @Override
public String getDisplayName() {
return String.format("Wait for %g seconds", this.howLongSeconds);
}

    @Override
public String serialize() {
String pydict = PyDictUtilBuilder.newBuilder().add("seconds", howLongSeconds).build();
return "WAIT|" + pydict;
}

    @Override
    public void execute(MonkeyDevice device) throws Exception {
long ms = (long) (1000.0f * howLongSeconds);
Thread.sleep(ms);
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/stub/StubBackend.java b/monkeyrunner/src/com/android/monkeyrunner/stub/StubBackend.java
//Synthetic comment -- index c2fa5f7..b868bf1 100644

//Synthetic comment -- @@ -15,18 +15,22 @@
*/
package com.android.monkeyrunner.stub;

import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.MonkeyManager;
import com.android.monkeyrunner.MonkeyRunnerBackend;

public class StubBackend implements MonkeyRunnerBackend {

public MonkeyManager createManager(String address, int port) {
// TODO Auto-generated method stub
return null;
}

    public MonkeyDevice waitForConnection(long timeout, String deviceId) {
// TODO Auto-generated method stub
return null;
}







