/*Adding pixel perfect view, loupe and tree.

Change-Id:I9be3e9037dec5eeb240608ba8c6329fd77689bbe*/




//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java
//Synthetic comment -- index 427add4..6acbdff 100644

//Synthetic comment -- @@ -19,20 +19,22 @@
import com.android.hierarchyviewerlib.ComponentRegistry;
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;

public class HierarchyViewerApplication {
public static void main(String[] args) {
HierarchyViewerDirector director = new HierarchyViewerApplicationDirector();
ComponentRegistry.setDirector(director);
ComponentRegistry.setDeviceSelectionModel(new DeviceSelectionModel());
        ComponentRegistry.setPixelPerfectModel(new PixelPerfectModel());
        director.initDebugBridge();
director.startListenForDevices();
director.populateDeviceSelectionModel();

UIThread.runUI();
director.stopListenForDevices();
director.stopDebugBridge();
director.terminate();
        System.exit(0);
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/UIThread.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/UIThread.java
//Synthetic comment -- index f59f6c4..485fbb5 100644

//Synthetic comment -- @@ -18,24 +18,88 @@

import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyvieweruilib.DeviceSelector;
import com.android.hierarchyvieweruilib.PixelPerfect;
import com.android.hierarchyvieweruilib.PixelPerfectLoupe;
import com.android.hierarchyvieweruilib.PixelPerfectTree;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;

public class UIThread {
public static void runUI() {
Display display = new Display();

        // CODE BELOW IS FOR TESTING.
Shell shell = new Shell(display);
shell.setLayout(new FillLayout());
DeviceSelector deviceSelector = new DeviceSelector(shell);
shell.open();
        Shell shell2 = new Shell(display);
        shell2.setLayout(new FillLayout());
        PixelPerfect pixelPerfect = new PixelPerfect(shell2);
        shell2.open();
        Shell shell3 = new Shell(display);
        shell3.setLayout(new FillLayout());
        final PixelPerfectLoupe pixelPerfectLoupe = new PixelPerfectLoupe(shell3);
        shell3.open();
        Shell shell4 = new Shell(display);
        shell4.setLayout(new FillLayout());
        PixelPerfectTree pixelPerfectTree = new PixelPerfectTree(shell4);
        shell4.open();
        Shell shell5 = new Shell(display);
        shell5.setLayout(new FillLayout());
        final Slider slider = new Slider(shell5, SWT.HORIZONTAL);
        slider.setMinimum(2);
        slider.setMaximum(25);
        slider.setSelection(8);
        slider.setThumb(1);
        slider.addSelectionListener(new SelectionListener() {
            private int oldZoom = 8;

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // pass
            }

            public void widgetSelected(SelectionEvent arg0) {
                int newZoom = slider.getSelection();
                if (newZoom != oldZoom) {
                    pixelPerfectLoupe.setZoom(newZoom);
                    oldZoom = newZoom;
                }
            }

        });
        shell5.open();
        while (!shell.isDisposed() && !shell2.isDisposed() && !shell3.isDisposed()
                && !shell4.isDisposed()) {
if (!display.readAndDispatch()) {
display.sleep();
}
}
        if (!shell.isDisposed()) {
            shell.dispose();
        }
        if (!shell2.isDisposed()) {
            shell2.dispose();
        }
        if (!shell3.isDisposed()) {
            shell3.dispose();
        }
        if (!shell4.isDisposed()) {
            shell4.dispose();
        }

        // NO LONGER TESTING STUFF.

deviceSelector.terminate();
        pixelPerfect.terminate();
        pixelPerfectLoupe.terminate();
        pixelPerfectTree.terminate();
ImageLoader.dispose();
display.dispose();
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ComponentRegistry.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ComponentRegistry.java
//Synthetic comment -- index 0f463d9..528c35c 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.hierarchyviewerlib;

import com.android.hierarchyviewerlib.models.DeviceSelectionModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;

/**
* This is the central point for getting access to the various parts of the
//Synthetic comment -- @@ -29,6 +30,8 @@

private static DeviceSelectionModel deviceSelectionModel;

    private static PixelPerfectModel pixelPerfectModel;

public static HierarchyViewerDirector getDirector() {
return director;
}
//Synthetic comment -- @@ -44,4 +47,12 @@
public static void setDeviceSelectionModel(DeviceSelectionModel deviceSelectionModel) {
ComponentRegistry.deviceSelectionModel = deviceSelectionModel;
}

    public static void setPixelPerfectModel(PixelPerfectModel pixelPerfectModel) {
        ComponentRegistry.pixelPerfectModel = pixelPerfectModel;
    }

    public static PixelPerfectModel getPixelPerfectModel() {
        return pixelPerfectModel;
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java
//Synthetic comment -- index e60a6f2..c21da49 100644

//Synthetic comment -- @@ -16,15 +16,21 @@

package com.android.hierarchyviewerlib;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.TimeoutException;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.hierarchyviewerlib.device.DeviceBridge;
import com.android.hierarchyviewerlib.device.ViewNode;
import com.android.hierarchyviewerlib.device.Window;
import com.android.hierarchyviewerlib.device.WindowUpdater;
import com.android.hierarchyviewerlib.device.DeviceBridge.ViewServerInfo;
import com.android.hierarchyviewerlib.device.WindowUpdater.IWindowChangeListener;

import java.io.IOException;

/**
* This is the class where most of the logic resides.
*/
//Synthetic comment -- @@ -33,6 +39,8 @@

public static final String TAG = "hierarchyviewer";

    private int pixelPerfectRefreshesInProgress = 0;

public void terminate() {
WindowUpdater.terminate();
}
//Synthetic comment -- @@ -74,17 +82,19 @@
return;
}
}
executeInBackground(new Runnable() {
public void run() {
                    ViewServerInfo viewServerInfo = DeviceBridge.loadViewServerInfo(device);
Window[] windows = DeviceBridge.loadWindows(device);
ComponentRegistry.getDeviceSelectionModel().addDevice(device, windows);
                    if (viewServerInfo.protocolVersion >= 3) {
                        WindowUpdater.startListenForWindowChanges(HierarchyViewerDirector.this,
                                device);
                        focusChanged(device);
                    }
}
});

}
}

//Synthetic comment -- @@ -124,5 +134,62 @@
focusedWindow);
}
});

        // Some interesting logic here. We don't want to refresh the pixel
        // perfect view 1000 times in a row if the focus keeps changing. We just
        // want it to refresh following the last focus change.
        boolean proceed = false;
        synchronized (this) {
            if (pixelPerfectRefreshesInProgress <= 1) {
                proceed = true;
                pixelPerfectRefreshesInProgress++;
            }
        }
        if (proceed) {
            executeInBackground(new Runnable() {
                public void run() {
                    if (ComponentRegistry.getDeviceSelectionModel().getFocusedWindow(device) != -1
                            && device == ComponentRegistry.getPixelPerfectModel().getDevice()) {
                        try {
                            ViewNode viewNode =
                                    DeviceBridge.loadWindowData(Window.getFocusedWindow(device));
                            RawImage screenshot = device.getScreenshot();
                            ComponentRegistry.getPixelPerfectModel().setFocusData(screenshot,
                                    viewNode);
                        } catch (IOException e) {
                            Log.e(TAG, "Unable to load screenshot from device " + device);
                        } catch (TimeoutException e) {
                            Log.e(TAG, "Timeout loading screenshot from device " + device);
                        } catch (AdbCommandRejectedException e) {
                            Log.e(TAG, "Adb rejected command to load screenshot from device "
                                    + device);
                        }
                    }
                    synchronized (HierarchyViewerDirector.this) {
                        pixelPerfectRefreshesInProgress--;
                    }
                }

            });
        }
    }

    public void loadPixelPerfectData(final IDevice device) {
        executeInBackground(new Runnable() {
            public void run() {
                try {
                    RawImage screenshot = device.getScreenshot();
                    ViewNode viewNode =
                            DeviceBridge.loadWindowData(Window.getFocusedWindow(device));
                    ComponentRegistry.getPixelPerfectModel().setData(device, screenshot, viewNode);
                } catch (IOException e) {
                    Log.e(TAG, "Unable to load screenshot from device " + device);
                } catch (TimeoutException e) {
                    Log.e(TAG, "Timeout loading screenshot from device " + device);
                } catch (AdbCommandRejectedException e) {
                    Log.e(TAG, "Adb rejected command to load screenshot from device " + device);
                }
            }
        });
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index 7a5a6f7..001f98b 100644

//Synthetic comment -- @@ -263,8 +263,11 @@
try {
connection = new DeviceConnection(device);
connection.sendCommand("SERVER");
            String line = connection.getInputStream().readLine();
            if (line != null) {
                server = Integer.parseInt(line);
            }
        } catch (Exception e) {
Log.e(TAG, "Unable to get view server version from device " + device);
} finally {
if (connection != null) {
//Synthetic comment -- @@ -275,8 +278,11 @@
try {
connection = new DeviceConnection(device);
connection.sendCommand("PROTOCOL");
            String line = connection.getInputStream().readLine();
            if (line != null) {
                protocol = Integer.parseInt(line);
            }
        } catch (Exception e) {
Log.e(TAG, "Unable to get view server protocol version from device " + device);
} finally {
if (connection != null) {
//Synthetic comment -- @@ -369,7 +375,7 @@
connection = new DeviceConnection(device);
connection.sendCommand("GET_FOCUS");
String line = connection.getInputStream().readLine();
            if (line == null || line.length() == 0) {
return -1;
}
return (int) Long.parseLong(line.substring(0, line.indexOf(' ')), 16);
//Synthetic comment -- @@ -382,4 +388,46 @@
}
return -1;
}

    public static ViewNode loadWindowData(Window window) {
        DeviceConnection connection = null;
        try {
            connection = new DeviceConnection(window.getDevice());
            connection.sendCommand("DUMP " + window.encode());
            BufferedReader in = connection.getInputStream();
            ViewNode currentNode = null;
            int currentDepth = -1;
            String line;
            while ((line = in.readLine()) != null) {
                if ("DONE.".equalsIgnoreCase(line)) {
                    break;
                }
                int depth = 0;
                while (line.charAt(depth) == ' ') {
                    depth++;
                }
                while (depth <= currentDepth) {
                    currentNode = currentNode.parent;
                    currentDepth--;
                }
                currentNode = new ViewNode(currentNode, line.substring(depth));
                currentDepth = depth;
            }
            if (currentNode == null) {
                return null;
            }
            while (currentNode.parent != null) {
                currentNode = currentNode.parent;
            }
            return currentNode;
        } catch (IOException e) {
            Log.e(TAG, "Unable to load window data for window " + window.getTitle() + " on device "
                    + window.getDevice());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return null;
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewNode.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewNode.java
new file mode 100644
//Synthetic comment -- index 0000000..00453ee

//Synthetic comment -- @@ -0,0 +1,186 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
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

package com.android.hierarchyviewerlib.device;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewNode {
    public String id;

    public String name;

    public String hashCode;

    public List<Property> properties = new ArrayList<Property>();

    public Map<String, Property> namedProperties = new HashMap<String, Property>();

    public ViewNode parent;

    public List<ViewNode> children = new ArrayList<ViewNode>();

    public int left;

    public int top;

    public int width;

    public int height;

    public int scrollX;

    public int scrollY;

    public int paddingLeft;

    public int paddingRight;

    public int paddingTop;

    public int paddingBottom;

    public int marginLeft;

    public int marginRight;

    public int marginTop;

    public int marginBottom;

    public int baseline;

    public boolean willNotDraw;

    public boolean hasMargins;

    public boolean hasFocus;

    public int index;

    public ViewNode(ViewNode parent, String data) {
        this.parent = parent;
        index = this.parent == null ? 0 : this.parent.children.size();
        if (this.parent != null) {
            this.parent.children.add(this);
        }
        int delimIndex = data.indexOf('@');
        name = data.substring(0, delimIndex);
        data = data.substring(delimIndex + 1);
        delimIndex = data.indexOf(' ');
        hashCode = data.substring(0, delimIndex);
        loadProperties(data.substring(delimIndex + 1).trim());
    }

    private void loadProperties(String data) {
        int start = 0;
        boolean stop;
        do {
            int index = data.indexOf('=', start);
            ViewNode.Property property = new ViewNode.Property();
            property.name = data.substring(start, index);

            int index2 = data.indexOf(',', index + 1);
            int length = Integer.parseInt(data.substring(index + 1, index2));
            start = index2 + 1 + length;
            property.value = data.substring(index2 + 1, index2 + 1 + length);

            properties.add(property);
            namedProperties.put(property.name, property);

            stop = start >= data.length();
            if (!stop) {
                start += 1;
            }
        } while (!stop);

        Collections.sort(properties, new Comparator<ViewNode.Property>() {
            public int compare(ViewNode.Property source, ViewNode.Property destination) {
                return source.name.compareTo(destination.name);
            }
        });

        id = namedProperties.get("mID").value;

        left = getInt("mLeft", 0);
        top = getInt("mTop", 0);
        width = getInt("getWidth()", 0);
        height = getInt("getHeight()", 0);
        scrollX = getInt("mScrollX", 0);
        scrollY = getInt("mScrollY", 0);
        paddingLeft = getInt("mPaddingLeft", 0);
        paddingRight = getInt("mPaddingRight", 0);
        paddingTop = getInt("mPaddingTop", 0);
        paddingBottom = getInt("mPaddingBottom", 0);
        marginLeft = getInt("layout_leftMargin", Integer.MIN_VALUE);
        marginRight = getInt("layout_rightMargin", Integer.MIN_VALUE);
        marginTop = getInt("layout_topMargin", Integer.MIN_VALUE);
        marginBottom = getInt("layout_bottomMargin", Integer.MIN_VALUE);
        baseline = getInt("getBaseline()", 0);
        willNotDraw = getBoolean("willNotDraw()", false);
        hasFocus = getBoolean("hasFocus()", false);

        hasMargins =
                marginLeft != Integer.MIN_VALUE && marginRight != Integer.MIN_VALUE
                        && marginTop != Integer.MIN_VALUE && marginBottom != Integer.MIN_VALUE;

    }

    private boolean getBoolean(String name, boolean defaultValue) {
        Property p = namedProperties.get(name);
        if (p != null) {
            try {
                return Boolean.parseBoolean(p.value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    private int getInt(String name, int defaultValue) {
        Property p = namedProperties.get(name);
        if (p != null) {
            try {
                return Integer.parseInt(p.value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    @Override
    public String toString() {
        return name + "@" + hashCode;
    }

    public static class Property {
        public String name;

        public String value;

        @Override
        public String toString() {
            return name + '=' + value;
        }
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/PixelPerfectModel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/PixelPerfectModel.java
new file mode 100644
//Synthetic comment -- index 0000000..6963b25

//Synthetic comment -- @@ -0,0 +1,196 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.android.hierarchyviewerlib.models;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.RawImage;
import com.android.hierarchyviewerlib.device.ViewNode;

import java.util.ArrayList;

public class PixelPerfectModel {

    public static class Point {
        public int x;

        public int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private IDevice device;

    private RawImage image;

    private Point crosshairLocation;

    private ViewNode viewNode;

    private ViewNode selected;

    private final ArrayList<ImageChangeListener> imageChangeListeners =
            new ArrayList<ImageChangeListener>();

    public void setData(IDevice device, RawImage image, ViewNode viewNode) {
        synchronized (this) {
            this.device = device;
            this.image = image;
            this.viewNode = viewNode;
            this.crosshairLocation = new Point(image.width / 2, image.height / 2);
            this.selected = null;
        }
        notifyImageLoaded();
    }

    public void setCrosshairLocation(int x, int y) {
        synchronized (this) {
            crosshairLocation = new Point(x, y);
        }
        notifyCrosshairMoved();
    }

    public void setSelected(ViewNode selected) {
        synchronized (this) {
            this.selected = selected;
        }
        notifySelectionChanged();
    }

    public void setFocusData(RawImage image, ViewNode viewNode) {
        synchronized (this) {
            this.image = image;
            this.viewNode = viewNode;
            this.selected = null;
        }
        notifyFocusChanged();
    }

    public ViewNode getViewNode() {
        synchronized (this) {
            return viewNode;
        }
    }

    public Point getCrosshairLocation() {
        synchronized (this) {
            return crosshairLocation;
        }
    }

    public RawImage getImage() {
        synchronized (this) {
            return image;
        }
    }

    public ViewNode getSelected() {
        synchronized (this) {
            return selected;
        }
    }

    public IDevice getDevice() {
        synchronized (this) {
            return device;
        }
    }

    public static interface ImageChangeListener {
        public void imageLoaded();

        public void imageChanged();

        public void crosshairMoved();

        public void selectionChanged();

        public void focusChanged();
    }

    private ImageChangeListener[] getImageChangeListenerList() {
        ImageChangeListener[] listeners = null;
        synchronized (imageChangeListeners) {
            if (imageChangeListeners.size() == 0) {
                return null;
            }
            listeners =
                    imageChangeListeners.toArray(new ImageChangeListener[imageChangeListeners
                            .size()]);
        }
        return listeners;
    }

    public void notifyImageLoaded() {
        ImageChangeListener[] listeners = getImageChangeListenerList();
        if (listeners != null) {
            for (int i = 0; i < listeners.length; i++) {
                listeners[i].imageLoaded();
            }
        }
    }

    public void notifyImageChanged() {
        ImageChangeListener[] listeners = getImageChangeListenerList();
        if (listeners != null) {
            for (int i = 0; i < listeners.length; i++) {
                listeners[i].imageChanged();
            }
        }
    }

    public void notifyCrosshairMoved() {
        ImageChangeListener[] listeners = getImageChangeListenerList();
        if (listeners != null) {
            for (int i = 0; i < listeners.length; i++) {
                listeners[i].crosshairMoved();
            }
        }
    }

    public void notifySelectionChanged() {
        ImageChangeListener[] listeners = getImageChangeListenerList();
        if (listeners != null) {
            for (int i = 0; i < listeners.length; i++) {
                listeners[i].selectionChanged();
            }
        }
    }

    public void notifyFocusChanged() {
        ImageChangeListener[] listeners = getImageChangeListenerList();
        if (listeners != null) {
            for (int i = 0; i < listeners.length; i++) {
                listeners[i].focusChanged();
            }
        }
    }

    public void addImageChangeListener(ImageChangeListener listener) {
        synchronized (imageChangeListeners) {
            imageChangeListeners.add(listener);
        }
    }

    public void removeImageChangeListener(ImageChangeListener listener) {
        synchronized (imageChangeListeners) {
            imageChangeListeners.remove(listener);
        }
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/DeviceSelector.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/DeviceSelector.java
//Synthetic comment -- index a948d12..60d8e6d 100644

//Synthetic comment -- @@ -243,7 +243,10 @@

public void widgetDefaultSelected(SelectionEvent e) {
// TODO: Double click to open view hierarchy
        Object selection = ((TreeItem) e.item).getData();
        if (selection instanceof IDevice) {
            ComponentRegistry.getDirector().loadPixelPerfectData((IDevice) selection);
        }
}

public void widgetSelected(SelectionEvent e) {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfect.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfect.java
new file mode 100644
//Synthetic comment -- index 0000000..e4345e1

//Synthetic comment -- @@ -0,0 +1,297 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.android.hierarchyvieweruilib;

import com.android.ddmlib.RawImage;
import com.android.hierarchyviewerlib.ComponentRegistry;
import com.android.hierarchyviewerlib.device.ViewNode;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.ImageChangeListener;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.Point;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class PixelPerfect implements ImageChangeListener {
    private Canvas canvas;

    private PixelPerfectModel model;

    private ScrolledComposite scrolledComposite;

    private Image image;

    private Color crosshairColor;

    private Color marginColor;

    private Color borderColor;

    private Color paddingColor;

    private int width;

    private int height;

    private Point crosshairLocation;

    private ViewNode selectedNode;

    public PixelPerfect(Composite parent) {
        scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        canvas = new Canvas(scrolledComposite, SWT.NONE);
        scrolledComposite.setContent(canvas);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        model = ComponentRegistry.getPixelPerfectModel();
        model.addImageChangeListener(this);

        canvas.addPaintListener(paintListener);
        canvas.addMouseListener(mouseListener);
        canvas.addMouseMoveListener(mouseMoveListener);

        crosshairColor = new Color(Display.getDefault(), new RGB(0, 255, 255));
        borderColor = new Color(Display.getDefault(), new RGB(255, 0, 0));
        marginColor = new Color(Display.getDefault(), new RGB(0, 255, 0));
        paddingColor = new Color(Display.getDefault(), new RGB(0, 0, 255));
    }

    public void terminate() {
        if (image != null) {
            image.dispose();
        }
        crosshairColor.dispose();
        borderColor.dispose();
        paddingColor.dispose();
    }

    public void setFocus() {
        canvas.setFocus();
    }

    private MouseListener mouseListener = new MouseListener() {

        public void mouseDoubleClick(MouseEvent e) {
            // pass
        }

        public void mouseDown(MouseEvent e) {
            handleMouseEvent(e);
        }

        public void mouseUp(MouseEvent e) {
            handleMouseEvent(e);
        }

    };

    private MouseMoveListener mouseMoveListener = new MouseMoveListener() {
        public void mouseMove(MouseEvent e) {
            if (e.stateMask != 0) {
                handleMouseEvent(e);
            }
        }
    };

    private void handleMouseEvent(MouseEvent e) {
        synchronized (this) {
            if (image == null) {
                return;
            }
            int leftOffset = canvas.getSize().x / 2 - width / 2;
            int topOffset = canvas.getSize().y / 2 - height / 2;
            e.x -= leftOffset;
            e.y -= topOffset;
            e.x = Math.max(e.x, 0);
            e.x = Math.min(e.x, width - 1);
            e.y = Math.max(e.y, 0);
            e.y = Math.min(e.y, height - 1);
        }
        model.setCrosshairLocation(e.x, e.y);
    }

    private PaintListener paintListener = new PaintListener() {
        public void paintControl(PaintEvent e) {
            synchronized (this) {
                e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
                e.gc.fillRectangle(0, 0, canvas.getSize().x, canvas.getSize().y);
                if (image != null) {
                    // Let's be cool and put it in the center...
                    int leftOffset = canvas.getSize().x / 2 - width / 2;
                    int topOffset = canvas.getSize().y / 2 - height / 2;
                    e.gc.drawImage(image, leftOffset, topOffset);
                    if (selectedNode != null) {
                        // There are a few quirks here. First of all, margins
                        // are sometimes negative or positive numbers... Yet,
                        // they are always treated as positive.
                        // Secondly, if the screen is in landscape mode, the
                        // coordinates are backwards.
                        int leftShift = 0;
                        int topShift = 0;
                        int nodeLeft = selectedNode.left;
                        int nodeTop = selectedNode.top;
                        int nodeWidth = selectedNode.width;
                        int nodeHeight = selectedNode.height;
                        int nodeMarginLeft = Math.abs(selectedNode.marginLeft);
                        int nodeMarginTop = Math.abs(selectedNode.marginTop);
                        int nodeMarginRight = Math.abs(selectedNode.marginRight);
                        int nodeMarginBottom = Math.abs(selectedNode.marginBottom);
                        int nodePadLeft = selectedNode.paddingLeft;
                        int nodePadTop = selectedNode.paddingTop;
                        int nodePadRight = selectedNode.paddingRight;
                        int nodePadBottom = selectedNode.paddingBottom;
                        ViewNode cur = selectedNode;
                        while (cur.parent != null) {
                            leftShift += cur.parent.left;
                            topShift += cur.parent.top;
                            cur = cur.parent;
                        }

                        // Everything is sideways.
                        if (cur.width > cur.height) {
                            e.gc.setForeground(paddingColor);
                            e.gc.drawRectangle(leftOffset + width - nodeTop - topShift - nodeHeight
                                    + nodePadBottom,
                                    topOffset + leftShift + nodeLeft + nodePadLeft, nodeHeight
                                            - nodePadBottom - nodePadTop, nodeWidth - nodePadRight
                                            - nodePadLeft);
                            e.gc.setForeground(marginColor);
                            e.gc.drawRectangle(leftOffset + width - nodeTop - topShift - nodeHeight
                                    - nodeMarginBottom, topOffset + leftShift + nodeLeft
                                    - nodeMarginLeft,
                                    nodeHeight + nodeMarginBottom + nodeMarginTop, nodeWidth
                                            + nodeMarginRight + nodeMarginLeft);
                            e.gc.setForeground(borderColor);
                            e.gc.drawRectangle(
                                    leftOffset + width - nodeTop - topShift - nodeHeight, topOffset
                                            + leftShift + nodeLeft, nodeHeight, nodeWidth);
                        } else {
                            e.gc.setForeground(paddingColor);
                            e.gc.drawRectangle(leftOffset + leftShift + nodeLeft + nodePadLeft,
                                    topOffset + topShift + nodeTop + nodePadTop, nodeWidth
                                            - nodePadRight - nodePadLeft, nodeHeight
                                            - nodePadBottom - nodePadTop);
                            e.gc.setForeground(marginColor);
                            e.gc.drawRectangle(leftOffset + leftShift + nodeLeft - nodeMarginLeft,
                                    topOffset + topShift + nodeTop - nodeMarginTop, nodeWidth
                                            + nodeMarginRight + nodeMarginLeft, nodeHeight
                                            + nodeMarginBottom + nodeMarginTop);
                            e.gc.setForeground(borderColor);
                            e.gc.drawRectangle(leftOffset + leftShift + nodeLeft, topOffset
                                    + topShift + nodeTop, nodeWidth, nodeHeight);
                        }
                    }
                    if (crosshairLocation != null) {
                        e.gc.setForeground(crosshairColor);
                        e.gc.drawLine(leftOffset, topOffset + crosshairLocation.y, leftOffset
                                + width - 1, topOffset + crosshairLocation.y);
                        e.gc.drawLine(leftOffset + crosshairLocation.x, topOffset, leftOffset
                                + crosshairLocation.x, topOffset + height - 1);
                    }
                }
            }
        }
    };

    private void redraw() {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                canvas.redraw();
            }
        });
    }

    private void loadImage() {
        final RawImage rawImage = model.getImage();
        if (rawImage != null) {
            ImageData imageData =
                    new ImageData(rawImage.width, rawImage.height, rawImage.bpp,
                            new PaletteData(rawImage.getRedMask(), rawImage.getGreenMask(),
                                    rawImage.getBlueMask()), 1, rawImage.data);
            if (image != null) {
                image.dispose();
            }
            image = new Image(Display.getDefault(), imageData);
            width = rawImage.width;
            height = rawImage.height;

        } else {
            if (image != null) {
                image.dispose();
                image = null;
            }
            width = 0;
            height = 0;
        }
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                scrolledComposite.setMinSize(width, height);
            }
        });
    }

    public void imageLoaded() {
        synchronized (this) {
            loadImage();
            crosshairLocation = model.getCrosshairLocation();
            selectedNode = model.getSelected();
        }
        redraw();
    }

    public void imageChanged() {
        synchronized (this) {
            loadImage();
        }
        redraw();
    }

    public void crosshairMoved() {
        synchronized (this) {
            crosshairLocation = model.getCrosshairLocation();
        }
        redraw();
    }

    public void selectionChanged() {
        synchronized (this) {
            selectedNode = model.getSelected();
        }
        redraw();
    }

    public void focusChanged() {
        synchronized (this) {
            loadImage();
            selectedNode = model.getSelected();
        }
        redraw();
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectLoupe.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectLoupe.java
new file mode 100644
//Synthetic comment -- index 0000000..eb1df88

//Synthetic comment -- @@ -0,0 +1,271 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.android.hierarchyvieweruilib;

import com.android.ddmlib.RawImage;
import com.android.hierarchyviewerlib.ComponentRegistry;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.ImageChangeListener;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.Point;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class PixelPerfectLoupe implements ImageChangeListener {
    private Canvas canvas;

    private PixelPerfectModel model;

    private Image image;

    private Image grid;

    private Color crosshairColor;

    private int width;

    private int height;

    private Point crosshairLocation;

    private int zoom;

    private Transform transform;

    private int canvasWidth;

    private int canvasHeight;

    public PixelPerfectLoupe(Composite parent) {
        canvas = new Canvas(parent, SWT.NONE);
        model = ComponentRegistry.getPixelPerfectModel();
        model.addImageChangeListener(this);

        canvas.addPaintListener(paintListener);
        canvas.addMouseListener(mouseListener);

        crosshairColor = new Color(Display.getDefault(), new RGB(255, 94, 254));

        transform = new Transform(Display.getDefault());
        zoom = 8;
    }

    public void terminate() {
        if (image != null) {
            image.dispose();
        }
        crosshairColor.dispose();
        transform.dispose();
        if (grid != null) {
            grid.dispose();
        }
    }

    public void setFocus() {
        canvas.setFocus();
    }

    public void setZoom(int value) {
        synchronized (this) {
            if (grid != null) {
                // To notify that the zoom level has changed, we get rid of the
                // grid.
                grid.dispose();
                grid = null;
                zoom = value;
            }
        }
        redraw();
    }

    private MouseListener mouseListener = new MouseListener() {

        public void mouseDoubleClick(MouseEvent e) {
            // pass
        }

        public void mouseDown(MouseEvent e) {
            // pass
        }

        public void mouseUp(MouseEvent e) {
            handleMouseEvent(e);
        }

    };

    private void handleMouseEvent(MouseEvent e) {
        int newX = -1;
        int newY = -1;
        synchronized (this) {
            if (image == null) {
                return;
            }
            int zoomedX = -crosshairLocation.x * zoom - zoom / 2 + canvas.getBounds().width / 2;
            int zoomedY = -crosshairLocation.y * zoom - zoom / 2 + canvas.getBounds().height / 2;
            int x = (e.x - zoomedX) / zoom;
            int y = (e.y - zoomedY) / zoom;
            if (x >= 0 && x < width && y >= 0 && y < height) {
                newX = x;
                newY = y;
            }
        }
        if (newX != -1) {
            model.setCrosshairLocation(newX, newY);
        }
    }

    private PaintListener paintListener = new PaintListener() {
        public void paintControl(PaintEvent e) {
            synchronized (this) {
                e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
                e.gc.fillRectangle(0, 0, canvas.getSize().x, canvas.getSize().y);
                if (image != null && crosshairLocation != null) {
                    int zoomedX =
                            -crosshairLocation.x * zoom - zoom / 2 + canvas.getBounds().width / 2;
                    int zoomedY =
                            -crosshairLocation.y * zoom - zoom / 2 + canvas.getBounds().height / 2;
                    transform.translate(zoomedX, zoomedY);
                    transform.scale(zoom, zoom);
                    e.gc.setInterpolation(SWT.NONE);
                    e.gc.setTransform(transform);
                    e.gc.drawImage(image, 0, 0);
                    transform.identity();
                    e.gc.setTransform(transform);

                    // If the size of the canvas has changed, we need to make
                    // another grid.
                    if (grid != null
                            && (canvasWidth != canvas.getBounds().width || canvasHeight != canvas
                                    .getBounds().height)) {
                        grid.dispose();
                        grid = null;
                    }
                    canvasWidth = canvas.getBounds().width;
                    canvasHeight = canvas.getBounds().height;
                    if (grid == null) {
                        // Make a transparent image;
                        ImageData imageData =
                                new ImageData(canvasWidth + zoom + 1, canvasHeight + zoom + 1, 1,
                                        new PaletteData(new RGB[] {
                                            new RGB(0, 0, 0)
                                        }));
                        imageData.transparentPixel = 0;

                        // Draw the grid.
                        grid = new Image(Display.getDefault(), imageData);
                        GC gc = new GC(grid);
                        gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
                        for (int x = 0; x <= canvasWidth + zoom; x += zoom) {
                            gc.drawLine(x, 0, x, canvasHeight + zoom);
                        }
                        for (int y = 0; y <= canvasHeight + zoom; y += zoom) {
                            gc.drawLine(0, y, canvasWidth + zoom, y);
                        }
                        gc.dispose();
                    }

                    e.gc.setClipping(new Rectangle(zoomedX, zoomedY, width * zoom + 1, height
                            * zoom + 1));
                    e.gc.setAlpha(76);
                    e.gc.drawImage(grid, (canvasWidth / 2 - zoom / 2) % zoom - zoom,
                            (canvasHeight / 2 - zoom / 2) % zoom - zoom);
                    e.gc.setAlpha(255);

                    e.gc.setForeground(crosshairColor);
                    e.gc.drawLine(0, canvasHeight / 2, canvasWidth - 1, canvasHeight / 2);
                    e.gc.drawLine(canvasWidth / 2, 0, canvasWidth / 2, canvasHeight - 1);
                }
            }
        }
    };

    private void redraw() {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                canvas.redraw();
            }
        });
    }

    private void loadImage() {
        final RawImage rawImage = model.getImage();
        if (rawImage != null) {
            ImageData imageData =
                    new ImageData(rawImage.width, rawImage.height, rawImage.bpp,
                            new PaletteData(rawImage.getRedMask(), rawImage.getGreenMask(),
                                    rawImage.getBlueMask()), 1, rawImage.data);
            if (image != null) {
                image.dispose();
            }
            image = new Image(Display.getDefault(), imageData);
            width = rawImage.width;
            height = rawImage.height;
        } else {
            if (image != null) {
                image.dispose();
                image = null;
            }
            width = 0;
            height = 0;
        }
    }

    public void imageLoaded() {
        synchronized (this) {
            loadImage();
            crosshairLocation = model.getCrosshairLocation();
        }
        redraw();
    }

    public void imageChanged() {
        synchronized (this) {
            loadImage();
        }
        redraw();
    }

    public void crosshairMoved() {
        synchronized (this) {
            crosshairLocation = model.getCrosshairLocation();
        }
        redraw();
    }

    public void selectionChanged() {
        // pass
    }

    public void focusChanged() {
        imageChanged();
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectTree.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectTree.java
new file mode 100644
//Synthetic comment -- index 0000000..a85c8eb

//Synthetic comment -- @@ -0,0 +1,199 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.android.hierarchyvieweruilib;

import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyviewerlib.ComponentRegistry;
import com.android.hierarchyviewerlib.device.ViewNode;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel.ImageChangeListener;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import java.util.List;

public class PixelPerfectTree implements ImageChangeListener, SelectionListener {

    private TreeViewer treeViewer;

    private Tree tree;

    private PixelPerfectModel model;

    private Image folderImage;

    private Image fileImage;

    private class ContentProvider implements ITreeContentProvider, ILabelProvider {
        public Object[] getChildren(Object element) {
            if (element instanceof ViewNode) {
                List<ViewNode> children = ((ViewNode) element).children;
                return children.toArray(new ViewNode[children.size()]);
            }
            return null;
        }

        public Object getParent(Object element) {
            if (element instanceof ViewNode) {
                return ((ViewNode) element).parent;
            }
            return null;
        }

        public boolean hasChildren(Object element) {
            if (element instanceof ViewNode) {
                return ((ViewNode) element).children.size() != 0;
            }
            return false;
        }

        public Object[] getElements(Object element) {
            if (element instanceof PixelPerfectModel) {
                ViewNode viewNode = ((PixelPerfectModel) element).getViewNode();
                if (viewNode == null) {
                    return new Object[0];
                }
                return new Object[] {
                    viewNode
                };
            }
            return new Object[0];
        }

        public void dispose() {
            // pass
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            // pass
        }

        public Image getImage(Object element) {
            if (element instanceof ViewNode) {
                if (hasChildren(element)) {
                    return folderImage;
                }
                return fileImage;
            }
            return null;
        }

        public String getText(Object element) {
            if (element instanceof ViewNode) {
                return ((ViewNode) element).name;
            }
            return null;
        }

        public void addListener(ILabelProviderListener listener) {
            // pass
        }

        public boolean isLabelProperty(Object element, String property) {
            // pass
            return false;
        }

        public void removeListener(ILabelProviderListener listener) {
            // pass
        }
    }

    public PixelPerfectTree(Composite parent) {
        treeViewer = new TreeViewer(parent, SWT.SINGLE);
        treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

        tree = treeViewer.getTree();
        TreeColumn col = new TreeColumn(tree, SWT.LEFT);
        col.setText("Name");
        col.pack();
        tree.setHeaderVisible(true);
        tree.addSelectionListener(this);

        loadResources();

        model = ComponentRegistry.getPixelPerfectModel();
        ContentProvider contentProvider = new ContentProvider();
        treeViewer.setContentProvider(contentProvider);
        treeViewer.setLabelProvider(contentProvider);
        treeViewer.setInput(model);
        model.addImageChangeListener(this);

    }

    public void loadResources() {
        ImageLoader loader = ImageLoader.getDdmUiLibLoader();
        fileImage = loader.loadImage("file.png", Display.getDefault());

        folderImage = loader.loadImage("folder.png", Display.getDefault());
    }

    public void terminate() {
        fileImage.dispose();
        folderImage.dispose();
    }

    public void setFocus() {
        tree.setFocus();
    }

    public void imageLoaded() {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                treeViewer.refresh();
                treeViewer.expandAll();
            }
        });
    }

    public void imageChanged() {
        // pass
    }

    public void crosshairMoved() {
        // pass
    }

    public void selectionChanged() {
        // pass
    }

    public void focusChanged() {
        imageLoaded();
    }

    public void widgetDefaultSelected(SelectionEvent e) {
        // pass
    }

    public void widgetSelected(SelectionEvent e) {
        model.setSelected((ViewNode) e.item.getData());
    }

}







