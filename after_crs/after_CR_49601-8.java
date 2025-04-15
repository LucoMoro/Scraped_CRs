/*Add support for hv protocol that works via DDM

Change-Id:Ia88d107811abd8e36a0f980938c584d79565ac42*/




//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java
//Synthetic comment -- index 3257e1c..4bc535d 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import com.android.hierarchyviewerlib.actions.LoadOverlayAction;
import com.android.hierarchyviewerlib.actions.LoadViewHierarchyAction;
import com.android.hierarchyviewerlib.actions.PixelPerfectAutoRefreshAction;
import com.android.hierarchyviewerlib.actions.ProfileNodesAction;
import com.android.hierarchyviewerlib.actions.RefreshPixelPerfectAction;
import com.android.hierarchyviewerlib.actions.RefreshPixelPerfectTreeAction;
import com.android.hierarchyviewerlib.actions.RefreshViewAction;
//Synthetic comment -- @@ -128,6 +129,7 @@
private Composite mTreeViewControls;

private ActionButton dumpDisplayList;
    private ActionButton mProfileNodes;

private HierarchyViewerDirector mDirector;

//Synthetic comment -- @@ -372,7 +374,7 @@

Composite innerButtonPanel = new Composite(buttonPanel, SWT.NONE);
innerButtonPanel.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        GridLayout innerButtonPanelLayout = new GridLayout(8, true);
innerButtonPanelLayout.marginWidth = innerButtonPanelLayout.marginHeight = 2;
innerButtonPanelLayout.horizontalSpacing = innerButtonPanelLayout.verticalSpacing = 2;
innerButtonPanel.setLayout(innerButtonPanelLayout);
//Synthetic comment -- @@ -404,6 +406,11 @@
new ActionButton(innerButtonPanel, DumpDisplayListAction.getAction());
dumpDisplayList.setLayoutData(new GridData(GridData.FILL_BOTH));

        ActionButton profileNodes =
                new ActionButton(innerButtonPanel, ProfileNodesAction.getAction());
        profileNodes.setLayoutData(new GridData(GridData.FILL_BOTH));


SashForm mainSash = new SashForm(mTreeViewPanel, SWT.HORIZONTAL | SWT.SMOOTH);
mainSash.setLayoutData(new GridData(GridData.FILL_BOTH));
Composite treeViewContainer = new Composite(mainSash, SWT.BORDER);








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java
//Synthetic comment -- index 867446d..0f0cf65 100644

//Synthetic comment -- @@ -135,6 +135,10 @@
executeInBackground("Connecting device", new Runnable() {
@Override
public void run() {
                if (!device.isOnline()) {
                    return;
                }

IHvDevice hvDevice;
synchronized (mDevicesLock) {
hvDevice = mDevices.get(device);
//Synthetic comment -- @@ -304,7 +308,6 @@
IHvDevice hvDevice = window.getHvDevice();
ViewNode viewNode = hvDevice.loadWindowData(window);
if (viewNode != null) {
viewNode.setViewCount();
TreeViewModel.getModel().setData(window, viewNode);
}
//Synthetic comment -- @@ -597,6 +600,21 @@
}
}

    public void profileCurrentNode() {
        final DrawableViewNode selectedNode = TreeViewModel.getModel().getSelection();
        if (selectedNode != null) {
            executeInBackground("Profile Node", new Runnable() {
                @Override
                public void run() {
                    IHvDevice hvDevice = getHvDevice(selectedNode.viewNode.window.getDevice());
                    hvDevice.loadProfileData(selectedNode.viewNode.window, selectedNode.viewNode);
                    // Force the layout viewer to redraw.
                    TreeViewModel.getModel().notifySelectionChanged();
                }
            });
        }
    }

public void loadAllViews() {
executeInBackground("Loading all views", new Runnable() {
@Override








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/ProfileNodesAction.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/actions/ProfileNodesAction.java
new file mode 100644
//Synthetic comment -- index 0000000..4bf93e8

//Synthetic comment -- @@ -0,0 +1,55 @@
/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.android.hierarchyviewerlib.actions;

import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyviewerlib.HierarchyViewerDirector;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ProfileNodesAction extends SelectedNodeEnabledAction implements ImageAction {
    private static ProfileNodesAction sAction;

    private Image mImage;

    public ProfileNodesAction() {
        super("Profile Node");
        ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        mImage = imageLoader.loadImage("profile.png", Display.getDefault()); //$NON-NLS-1$
        setImageDescriptor(ImageDescriptor.createFromImage(mImage));
        setToolTipText("Obtain layout times for tree rooted at selected node");
    }

    public static ProfileNodesAction getAction() {
        if (sAction == null) {
            sAction = new ProfileNodesAction();
        }
        return sAction;
    }

    @Override
    public void run() {
        HierarchyViewerDirector.getDirector().profileCurrentNode();
    }

    @Override
    public Image getImage() {
        return mImage;
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DdmViewDebugDevice.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DdmViewDebugDevice.java
new file mode 100644
//Synthetic comment -- index 0000000..b572557

//Synthetic comment -- @@ -0,0 +1,373 @@
/*
 * Copyright (C) 2013 The Android Open Source Project
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

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.Client;
import com.android.ddmlib.ClientData;
import com.android.ddmlib.HandleViewDebug;
import com.android.ddmlib.HandleViewDebug.ViewDumpHandler;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.hierarchyviewerlib.device.WindowUpdater.IWindowChangeListener;
import com.android.hierarchyviewerlib.models.ViewNode;
import com.android.hierarchyviewerlib.models.Window;
import com.android.hierarchyviewerlib.ui.util.PsdFile;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class DdmViewDebugDevice extends AbstractHvDevice implements IDeviceChangeListener {
    private static final String TAG = "DdmViewDebugDevice";

    private final IDevice mDevice;
    private Map<Client, List<String>> mViewRootsPerClient = new HashMap<Client, List<String>>(40);

    public DdmViewDebugDevice(IDevice device) {
        mDevice = device;
    }

    @Override
    public boolean initializeViewDebug() {
        AndroidDebugBridge.addDeviceChangeListener(this);
        return reloadWindows();
    }

    private static class ListViewRootsHandler extends ViewDumpHandler {
        private List<String> mViewRoots = Collections.synchronizedList(new ArrayList<String>(10));

        public ListViewRootsHandler() {
            super(HandleViewDebug.CHUNK_VULW);
        }

        @Override
        protected void handleViewDebugResult(ByteBuffer data) {
            int nWindows = data.getInt();

            for (int i = 0; i < nWindows; i++) {
                int len = data.getInt();
                mViewRoots.add(getString(data, len));
            }
        }

        public List<String> getViewRoots(long timeout, TimeUnit unit) {
            waitForResult(timeout, unit);
            return mViewRoots;
        }
    }

    private static class CaptureByteArrayHandler extends ViewDumpHandler {
        public CaptureByteArrayHandler(int type) {
            super(type);
        }

        private AtomicReference<byte[]> mData = new AtomicReference<byte[]>();

        @Override
        protected void handleViewDebugResult(ByteBuffer data) {
            byte[] b = new byte[data.remaining()];
            data.get(b);
            mData.set(b);

        }

        public byte[] getData(long timeout, TimeUnit unit) {
            waitForResult(timeout, unit);
            return mData.get();
        }
    }

    private static class CaptureLayersHandler extends ViewDumpHandler {
        private AtomicReference<PsdFile> mPsd = new AtomicReference<PsdFile>();

        public CaptureLayersHandler() {
            super(HandleViewDebug.CHUNK_VURT);
        }

        @Override
        protected void handleViewDebugResult(ByteBuffer data) {
            byte[] b = new byte[data.remaining()];
            data.get(b);
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(b));
            try {
                mPsd.set(DeviceBridge.parsePsd(dis));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public PsdFile getPsdFile(long timeout, TimeUnit unit) {
            waitForResult(timeout, unit);
            return mPsd.get();
        }
    }

    @Override
    public boolean reloadWindows() {
        mViewRootsPerClient = new HashMap<Client, List<String>>(40);

        for (Client c : mDevice.getClients()) {
            ClientData cd = c.getClientData();
            if (cd != null && cd.hasFeature(ClientData.FEATURE_VIEW_HIERARCHY)) {
                ListViewRootsHandler handler = new ListViewRootsHandler();

                try {
                    HandleViewDebug.listViewRoots(c, handler);
                } catch (IOException e) {
                    Log.e(TAG, e);
                }

                List<String> viewRoots = new ArrayList<String>(
                        handler.getViewRoots(200, TimeUnit.MILLISECONDS));
                mViewRootsPerClient.put(c, viewRoots);
            }
        }

        return true;
    }

    @Override
    public void terminateViewDebug() {
        // nothing to terminate
    }

    @Override
    public boolean isViewDebugEnabled() {
        return true;
    }

    @Override
    public boolean supportsDisplayListDump() {
        return true;
    }

    @Override
    public Window[] getWindows() {
        List<Window> windows = new ArrayList<Window>(10);

        for (Client c: mViewRootsPerClient.keySet()) {
            for (String viewRoot: mViewRootsPerClient.get(c)) {
                windows.add(new Window(this, viewRoot, c));
            }
        }

        return windows.toArray(new Window[windows.size()]);
    }

    @Override
    public int getFocusedWindow() {
        // TODO: add support for identifying view in focus
        return -1;
    }

    @Override
    public IDevice getDevice() {
        return mDevice;
    }

    @Override
    public ViewNode loadWindowData(Window window) {
        Client c = window.getClient();
        if (c == null) {
            return null;
        }

        String viewRoot = window.getTitle();
        CaptureByteArrayHandler handler = new CaptureByteArrayHandler(HandleViewDebug.CHUNK_VURT);
        try {
            HandleViewDebug.dumpViewHierarchy(c, viewRoot,
                    false /* skipChildren */,
                    true  /* includeProperties */,
                    handler);
        } catch (IOException e) {
            Log.e(TAG, e);
            return null;
        }

        byte[] data = handler.getData(10, TimeUnit.SECONDS);
        String viewHierarchy = new String(data, Charset.forName("UTF-8"));
        return DeviceBridge.parseViewHierarchy(new BufferedReader(new StringReader(viewHierarchy)),
                window);
    }

    @Override
    public void loadProfileData(Window window, ViewNode viewNode) {
        Client c = window.getClient();
        if (c == null) {
            return;
        }

        String viewRoot = window.getTitle();
        CaptureByteArrayHandler handler = new CaptureByteArrayHandler(HandleViewDebug.CHUNK_VUOP);
        try {
            HandleViewDebug.profileView(c, viewRoot, viewNode.toString(), handler);
        } catch (IOException e) {
            Log.e(TAG, e);
            return;
        }

        byte[] data = handler.getData(30, TimeUnit.SECONDS);
        if (data == null) {
            Log.e(TAG, "Timed out waiting for profile data");
            return;
        }

        try {
            boolean success = DeviceBridge.loadProfileDataRecursive(viewNode,
                    new BufferedReader(new StringReader(new String(data))));
            if (success) {
                viewNode.setProfileRatings();
            }
        } catch (IOException e) {
            Log.e(TAG, e);
            return;
        }
    }

    @Override
    public Image loadCapture(Window window, ViewNode viewNode) {
        Client c = window.getClient();
        if (c == null) {
            return null;
        }

        String viewRoot = window.getTitle();
        CaptureByteArrayHandler handler = new CaptureByteArrayHandler(HandleViewDebug.CHUNK_VUOP);

        try {
            HandleViewDebug.captureView(c, viewRoot, viewNode.toString(), handler);
        } catch (IOException e) {
            Log.e(TAG, e);
            return null;
        }

        byte[] data = handler.getData(10, TimeUnit.SECONDS);
        return (data == null) ? null :
            new Image(Display.getDefault(), new ByteArrayInputStream(data));
    }

    @Override
    public PsdFile captureLayers(Window window) {
        Client c = window.getClient();
        if (c == null) {
            return null;
        }

        String viewRoot = window.getTitle();
        CaptureLayersHandler handler = new CaptureLayersHandler();
        try {
            HandleViewDebug.captureLayers(c, viewRoot, handler);
        } catch (IOException e) {
            Log.e(TAG, e);
            return null;
        }

        return handler.getPsdFile(20, TimeUnit.SECONDS);
    }

    @Override
    public void invalidateView(ViewNode viewNode) {
        Window window = viewNode.window;
        Client c = window.getClient();
        if (c == null) {
            return;
        }

        String viewRoot = window.getTitle();
        try {
            HandleViewDebug.invalidateView(c, viewRoot, viewNode.toString());
        } catch (IOException e) {
            Log.e(TAG, e);
        }
    }

    @Override
    public void requestLayout(ViewNode viewNode) {
        Window window = viewNode.window;
        Client c = window.getClient();
        if (c == null) {
            return;
        }

        String viewRoot = window.getTitle();
        try {
            HandleViewDebug.requestLayout(c, viewRoot, viewNode.toString());
        } catch (IOException e) {
            Log.e(TAG, e);
        }
    }

    @Override
    public void outputDisplayList(ViewNode viewNode) {
        Window window = viewNode.window;
        Client c = window.getClient();
        if (c == null) {
            return;
        }

        String viewRoot = window.getTitle();
        try {
            HandleViewDebug.dumpDisplayList(c, viewRoot, viewNode.toString());
        } catch (IOException e) {
            Log.e(TAG, e);
        }
    }

    @Override
    public void addWindowChangeListener(IWindowChangeListener l) {
        // TODO: add support for listening to view root changes
    }

    @Override
    public void removeWindowChangeListener(IWindowChangeListener l) {
        // TODO: add support for listening to view root changes
    }

    @Override
    public void deviceConnected(IDevice device) {
        // pass
    }

    @Override
    public void deviceDisconnected(IDevice device) {
        // pass
    }

    @Override
    public void deviceChanged(IDevice device, int changeMask) {
        if ((changeMask & IDevice.CHANGE_CLIENT_LIST) != 0) {
            reloadWindows();
        }
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index da29703..30fa6f6 100644

//Synthetic comment -- @@ -99,7 +99,7 @@
*/
public static void initDebugBridge(String adbLocation) {
if (sBridge == null) {
            AndroidDebugBridge.init(true /* debugger support */);
}
if (sBridge == null || !sBridge.isConnected()) {
sBridge = AndroidDebugBridge.createBridge(adbLocation, true);
//Synthetic comment -- @@ -438,9 +438,29 @@
connection = new DeviceConnection(window.getDevice());
connection.sendCommand("DUMP " + window.encode()); //$NON-NLS-1$
BufferedReader in = connection.getInputStream();
            ViewNode currentNode = parseViewHierarchy(in, window);
            ViewServerInfo serverInfo = getViewServerInfo(window.getDevice());
            if (serverInfo != null) {
                currentNode.protocolVersion = serverInfo.protocolVersion;
            }
            return currentNode;
        } catch (Exception e) {
            Log.e(TAG, "Unable to load window data for window " + window.getTitle() + " on device "
                    + window.getDevice());
            Log.e(TAG, e.getMessage());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return null;
    }

    public static ViewNode parseViewHierarchy(BufferedReader in, Window window) {
        ViewNode currentNode = null;
        int currentDepth = -1;
        String line;
        try {
while ((line = in.readLine()) != null) {
if ("DONE.".equalsIgnoreCase(line)) {
break;
//Synthetic comment -- @@ -458,27 +478,18 @@
currentNode = new ViewNode(window, currentNode, line.substring(depth));
currentDepth = depth;
}
        } catch (IOException e) {
            Log.e(TAG, "Error reading view hierarchy stream: " + e.getMessage());
            return null;
}
        if (currentNode == null) {
            return null;
        }
        while (currentNode.parent != null) {
            currentNode = currentNode.parent;
        }

        return currentNode;
}

public static boolean loadProfileData(Window window, ViewNode viewNode) {
//Synthetic comment -- @@ -524,7 +535,7 @@
return true;
}

    public static boolean loadProfileDataRecursive(ViewNode node, BufferedReader in)
throws IOException {
if (!loadProfileData(node, in)) {
return false;
//Synthetic comment -- @@ -567,16 +578,8 @@
new DataInputStream(new BufferedInputStream(connection.getSocket()
.getInputStream()));

            return parsePsd(in);
        } catch (IOException e) {
Log.e(TAG, "Unable to capture layers for window " + window.getTitle() + " on device "
+ window.getDevice());
} finally {
//Synthetic comment -- @@ -595,6 +598,18 @@
return null;
}

    public static PsdFile parsePsd(DataInputStream in) throws IOException {
        int width = in.readInt();
        int height = in.readInt();

        PsdFile psd = new PsdFile(width, height);

        while (readLayer(in, psd)) {
        }

        return psd;
    }

private static boolean readLayer(DataInputStream in, PsdFile psd) {
try {
if (in.read() == 2) {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/HvDeviceFactory.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/HvDeviceFactory.java
//Synthetic comment -- index e1fdab3..c38a9cc 100644

//Synthetic comment -- @@ -16,10 +16,29 @@

package com.android.hierarchyviewerlib.device;

import com.android.ddmlib.Client;
import com.android.ddmlib.ClientData;
import com.android.ddmlib.IDevice;

public class HvDeviceFactory {
    private static final boolean ALWAYS_USE_VIEWSERVER = false; // for debugging

public static IHvDevice create(IDevice device) {
        if (ALWAYS_USE_VIEWSERVER) {
            return new ViewServerDevice(device);
        }

        boolean ddmViewHierarchy = false;

        // see if any of the clients on the device support view hierarchy via DDMS
        for (Client c : device.getClients()) {
            ClientData cd = c.getClientData();
            if (cd != null && cd.hasFeature(ClientData.FEATURE_VIEW_HIERARCHY)) {
                ddmViewHierarchy = true;
                break;
            }
        }

        return ddmViewHierarchy ? new DdmViewDebugDevice(device) : new ViewServerDevice(device);
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/ViewNode.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/ViewNode.java
//Synthetic comment -- index 49bdf8f..e38da00 100644

//Synthetic comment -- @@ -132,7 +132,14 @@
data = data.substring(delimIndex + 1);
delimIndex = data.indexOf(' ');
hashCode = data.substring(0, delimIndex);

        if (data.length() > delimIndex + 1) {
            loadProperties(data.substring(delimIndex + 1).trim());
        } else {
            // defaults in case properties are not available
            id = "unknown";
            width = height = 10;
        }

measureTime = -1;
layoutTime = -1;








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/Window.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/Window.java
//Synthetic comment -- index 7298ab2..cdabd3f 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.hierarchyviewerlib.models;

import com.android.ddmlib.Client;
import com.android.ddmlib.IDevice;
import com.android.hierarchyviewerlib.device.IHvDevice;

//Synthetic comment -- @@ -27,11 +28,20 @@
private final String mTitle;
private final int mHashCode;
private final IHvDevice mHvDevice;
    private final Client mClient;

public Window(IHvDevice device, String title, int hashCode) {
this.mHvDevice = device;
this.mTitle = title;
this.mHashCode = hashCode;
        mClient = null;
    }

    public Window(IHvDevice device, String title, Client c) {
        this.mHvDevice = device;
        mTitle = title;
        mClient = c;
        mHashCode = c.hashCode();
}

public String getTitle() {
//Synthetic comment -- @@ -59,6 +69,10 @@
return mHvDevice.getDevice();
}

    public Client getClient() {
        return mClient;
    }

public static Window getFocusedWindow(IHvDevice device) {
return new Window(device, "<Focused Window>", -1);
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeViewOverview.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeViewOverview.java
//Synthetic comment -- index bbff48c..3352df0 100644

//Synthetic comment -- @@ -234,8 +234,7 @@
.ceil(mViewport.width), (int) Math.ceil(mViewport.height));

e.gc.setAlpha(255);
                        e.gc.setForeground(Display.getDefault().getSystemColor(
SWT.COLOR_DARK_GRAY));
e.gc.setLineWidth((int) Math.ceil(2 / mScale));
e.gc.drawRectangle((int) mViewport.x, (int) mViewport.y, (int) Math







