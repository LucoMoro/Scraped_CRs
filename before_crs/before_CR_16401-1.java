/*Increasing the changes of a device connecting properly and making sure things are disposed of properly.

Change-Id:Ib1190c65c8ac9795fc0b97beacd7fd102cb95abf*/
//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/UIThread.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/UIThread.java
//Synthetic comment -- index f1965b2..4dd990b 100644

//Synthetic comment -- @@ -88,7 +88,7 @@
TreeViewOverview treeViewOverview = new TreeViewOverview(shell3);
shell3.open();
// ComponentRegistry.getDirector().loadViewTreeData(null);
        while (!shell.isDisposed() && !shell2.isDisposed()) {
if (!display.readAndDispatch()) {
display.sleep();
}
//Synthetic comment -- @@ -99,6 +99,9 @@
if (!shell2.isDisposed()) {
shell2.dispose();
}

// NO LONGER TESTING STUFF.









//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java
//Synthetic comment -- index b63fba8..65cb24f 100644

//Synthetic comment -- @@ -77,27 +77,49 @@
DeviceBridge.setupDeviceForward(device);
if (!DeviceBridge.isViewServerRunning(device)) {
if (!DeviceBridge.startViewServer(device)) {
                    DeviceBridge.removeDeviceForward(device);
                    Log.e(TAG, "Unable to debug device " + device);
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

public void deviceDisconnected(IDevice device) {
ViewServerInfo viewServerInfo = DeviceBridge.getViewServerInfo(device);
if (viewServerInfo == null) {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index 34e7c03..c9244d2 100644

//Synthetic comment -- @@ -257,8 +257,8 @@
}

public static ViewServerInfo loadViewServerInfo(IDevice device) {
        int server = 2;
        int protocol = 2;
DeviceConnection connection = null;
try {
connection = new DeviceConnection(device);
//Synthetic comment -- @@ -289,6 +289,9 @@
connection.close();
}
}
ViewServerInfo returnValue = new ViewServerInfo(server, protocol);
synchronized (viewServerInfo) {
viewServerInfo.put(device, returnValue);








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/TreeViewModel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/TreeViewModel.java
//Synthetic comment -- index 890b88c..5c75d15 100644

//Synthetic comment -- @@ -130,6 +130,7 @@
listeners =
treeChangeListeners.toArray(new TreeChangeListener[treeChangeListeners.size()]);
}
return listeners;
}









//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/DeviceSelector.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/DeviceSelector.java
//Synthetic comment -- index 187a3fa..4add510 100644

//Synthetic comment -- @@ -31,6 +31,8 @@
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
//Synthetic comment -- @@ -158,6 +160,8 @@
tree.setLinesVisible(true);
tree.addSelectionListener(this);

loadResources();

model = ComponentRegistry.getDeviceSelectionModel();
//Synthetic comment -- @@ -192,12 +196,12 @@
.getSystemColor(SWT.COLOR_BLUE));
}

    @Override
    public void dispose() {
        super.dispose();
        model.removeWindowChangeListener(this);
        boldFont.dispose();
    }

@Override
public boolean setFocus() {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfect.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfect.java
//Synthetic comment -- index 3972830..5336ec3 100644

//Synthetic comment -- @@ -25,6 +25,8 @@

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
//Synthetic comment -- @@ -75,22 +77,25 @@
canvas.addMouseListener(mouseListener);
canvas.addMouseMoveListener(mouseMoveListener);

crosshairColor = new Color(Display.getDefault(), new RGB(0, 255, 255));
borderColor = new Color(Display.getDefault(), new RGB(255, 0, 0));
marginColor = new Color(Display.getDefault(), new RGB(0, 255, 0));
paddingColor = new Color(Display.getDefault(), new RGB(0, 0, 255));
}

    @Override
    public void dispose() {
        super.dispose();
        if (image != null) {
            image.dispose();
}
        crosshairColor.dispose();
        borderColor.dispose();
        paddingColor.dispose();
    }

@Override
public boolean setFocus() {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectLoupe.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectLoupe.java
//Synthetic comment -- index 62bb8c7..f0402f3 100644

//Synthetic comment -- @@ -23,6 +23,8 @@
import com.android.hierarchyviewerlib.models.PixelPerfectModel.Point;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseWheelListener;
//Synthetic comment -- @@ -71,24 +73,26 @@
addPaintListener(paintListener);
addMouseListener(mouseListener);
addMouseWheelListener(mouseWheelListener);

crosshairColor = new Color(Display.getDefault(), new RGB(255, 94, 254));

transform = new Transform(Display.getDefault());
}

    @Override
    public void dispose() {
        super.dispose();
        if (image != null) {
            image.dispose();
}
        crosshairColor.dispose();
        transform.dispose();
        if (grid != null) {
            grid.dispose();
        }
    }

private MouseListener mouseListener = new MouseListener() {









//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectTree.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectTree.java
//Synthetic comment -- index 9f25a33..30a7b9e 100644

//Synthetic comment -- @@ -29,6 +29,8 @@
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
//Synthetic comment -- @@ -142,6 +144,8 @@

loadResources();

model = ComponentRegistry.getPixelPerfectModel();
ContentProvider contentProvider = new ContentProvider();
treeViewer.setContentProvider(contentProvider);
//Synthetic comment -- @@ -158,12 +162,13 @@
folderImage = loader.loadImage("folder.png", Display.getDefault());
}

    @Override
    public void dispose() {
        super.dispose();
        fileImage.dispose();
        folderImage.dispose();
    }

@Override
public boolean setFocus() {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/TreeView.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/TreeView.java
//Synthetic comment -- index a29bdb3..2bf4012 100644

//Synthetic comment -- @@ -24,6 +24,8 @@
import com.android.hierarchyviewerlib.scene.DrawableViewNode.Rectangle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
//Synthetic comment -- @@ -67,17 +69,19 @@
addMouseMoveListener(mouseMoveListener);
addMouseWheelListener(mouseWheelListener);
addListener(SWT.Resize, resizeListener);

transform = new Transform(Display.getDefault());
inverse = new Transform(Display.getDefault());
}

    @Override
    public void dispose() {
        super.dispose();
        transform.dispose();
        inverse.dispose();
    }

private Listener resizeListener = new Listener() {
public void handleEvent(Event e) {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/TreeViewOverview.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/TreeViewOverview.java
//Synthetic comment -- index cfa9070..d299717 100644

//Synthetic comment -- @@ -24,6 +24,8 @@
import com.android.hierarchyviewerlib.scene.DrawableViewNode.Rectangle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
//Synthetic comment -- @@ -64,17 +66,19 @@
addMouseListener(mouseListener);
addMouseMoveListener(mouseMoveListener);
addListener(SWT.Resize, resizeListener);

transform = new Transform(Display.getDefault());
inverse = new Transform(Display.getDefault());
}

    @Override
    public void dispose() {
        super.dispose();
        transform.dispose();
        inverse.dispose();
    }

private MouseListener mouseListener = new MouseListener() {








