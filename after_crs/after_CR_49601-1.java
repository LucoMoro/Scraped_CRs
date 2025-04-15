/*WIP: hv via DDM

Change-Id:Ia88d107811abd8e36a0f980938c584d79565ac42*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/AndroidDebugBridge.java b/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/AndroidDebugBridge.java
//Synthetic comment -- index 5407d7f..7cb53dd 100644

//Synthetic comment -- @@ -197,6 +197,7 @@
HandleWait.register(monitorThread);
HandleProfiling.register(monitorThread);
HandleNativeHeap.register(monitorThread);
        HandleViewDebug.register(monitorThread);
}

/**








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/ClientData.java b/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/ClientData.java
//Synthetic comment -- index f490c1a..e7a0c5c 100644

//Synthetic comment -- @@ -136,6 +136,12 @@
public final static String FEATURE_OPENGL_TRACING = "opengl-tracing"; //$NON-NLS-1$

/**
     * String for feature indicating support for providing view hierarchy.
     * @see #hasFeature(String)
     */
    public static final String FEATURE_VIEW_HIERARCHY = "view-hierarchy"; //$NON-NLS-1$

    /**
* String for feature allowing to dump hprof files
* @see #hasFeature(String)
*/
//Synthetic comment -- @@ -147,6 +153,7 @@
*/
public final static String FEATURE_HPROF_STREAMING = "hprof-heap-dump-streaming"; //$NON-NLS-1$


private static IHprofDumpHandler sHprofDumpHandler;
private static IMethodProfilingHandler sMethodProfilingHandler;









//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/HandleViewDebug.java b/ddms/libs/ddmlib/src/main/java/com/android/ddmlib/HandleViewDebug.java
new file mode 100644
//Synthetic comment -- index 0000000..51689a5

//Synthetic comment -- @@ -0,0 +1,251 @@
/*
 * Copyright (C) 2007 The Android Open Source Project
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

package com.android.ddmlib;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

final public class HandleViewDebug extends ChunkHandler {
    /** Enable/Disable tracing of OpenGL calls. */
    private static final int CHUNK_VUGL = type("VUGL");

    /** List {@link ViewRootImpl}'s of this process. */
    private static final int CHUNK_VULW = type("VULW");

    /** Dump view hierarchy. */
    private static final int CHUNK_VUDP = type("VUDP");

    /** Capture View Layers. */
    private static final int CHUNK_VUCL = type("VUCL");

    /** Capture View Layers. */
    private static final int CHUNK_VUCV = type("VUCV");

    /** Obtain the Display List corresponding to the view. */
    private static final int CHUNK_VUDL = type("VUDL");

    /** Invalidate View. */
    private static final int CHUNK_VUIV = type("VUIV");

    /** Re-layout given view. */
    private static final int CHUNK_VULT = type("VULT");

    /** Profile a view. */
    private static final int CHUNK_VUPR = type("VUPR");

    private static final HandleViewDebug sInst = new HandleViewDebug();

    private HandleViewDebug() {}

    public static void register(MonitorThread mt) {
        mt.registerChunkHandler(CHUNK_VUCL, sInst);
        mt.registerChunkHandler(CHUNK_VUDL, sInst);
        mt.registerChunkHandler(CHUNK_VUIV, sInst);
        mt.registerChunkHandler(CHUNK_VULT, sInst);
        mt.registerChunkHandler(CHUNK_VUPR, sInst);
    }

    @Override
    public void clientReady(Client client) throws IOException {}

    @Override
    public void clientDisconnected(Client client) {}

    public static void listViewRoots(Client client, ListViewRootsHandler replyHandler)
            throws IOException {
        ByteBuffer buf = allocBuffer(4);
        JdwpPacket packet = new JdwpPacket(buf);
        ByteBuffer chunkBuf = getChunkDataBuf(buf);
        chunkBuf.putInt(1);
        finishChunkPacket(packet, CHUNK_VULW, chunkBuf.position());
        client.sendAndConsume(packet, replyHandler);
    }

    private static abstract class ViewDumpHandler extends ChunkHandler {
        private final CountDownLatch mLatch = new CountDownLatch(1);

        @Override
        void clientReady(Client client) throws IOException {
        }

        @Override
        void clientDisconnected(Client client) {
        }

        protected void resultReceived() {
            mLatch.countDown();
        }

        protected void waitForResult(long timeout, TimeUnit unit) {
            try {
                mLatch.await(timeout, unit);
            } catch (InterruptedException e) {
                // pass
            }
        }
    }

    public static class ListViewRootsHandler extends ViewDumpHandler {
        private List<String> mViewRoots = Collections.synchronizedList(new ArrayList<String>(10));

        @Override
        void handleChunk(Client client, int type, ByteBuffer data, boolean isReply, int msgId) {
            if (type != CHUNK_VULW) {
                handleUnknownChunk(client, type, data, isReply, msgId);
                return;
            }

            int nWindows = data.getInt();

            for (int i = 0; i < nWindows; i++) {
                int len = data.getInt();
                mViewRoots.add(getString(data, len));
            }

            resultReceived();
        }

        public List<String> getViewRoots(long timeout, TimeUnit unit) {
            waitForResult(timeout, unit);
            return mViewRoots;
        }
    }

    public static void dumpViewHierarchy(Client client, String viewRoot,
            boolean skipChildren, boolean includeProperties, DumpViewHierarchyHandler handler)
                    throws IOException {
        ByteBuffer buf = allocBuffer(4      // view root length
                + viewRoot.length() * 2     // view root
                + 4                         // skip children
                + 4);                       // include view properties
        JdwpPacket packet = new JdwpPacket(buf);
        ByteBuffer chunkBuf = getChunkDataBuf(buf);

        chunkBuf.putInt(viewRoot.length());
        putString(chunkBuf, viewRoot);
        chunkBuf.putInt(skipChildren ? 1 : 0);
        chunkBuf.putInt(includeProperties ? 1 : 0);

        finishChunkPacket(packet, CHUNK_VUDP, chunkBuf.position());
        client.sendAndConsume(packet, handler);
    }

    public static class DumpViewHierarchyHandler extends ViewDumpHandler {
        private AtomicReference<String> mViewHierarchyRef = new AtomicReference<String>("");
        @Override
        void handleChunk(Client client, int type, ByteBuffer data, boolean isReply, int msgId) {
            if (type != CHUNK_VUDP) {
                handleUnknownChunk(client, type, data, isReply, msgId);
                return;
            }

            byte[] b = new byte[data.remaining()];
            data.get(b);
            String s = new String(b, Charset.forName("UTF-8"));
            mViewHierarchyRef.set(s);
            resultReceived();
        }

        public String getViewHierarchy(long timeout, TimeUnit unit) {
            waitForResult(timeout, unit);
            return mViewHierarchyRef.get();
        }
    }

    private static void sendJdwpPacket(Client client, int type, String viewRoot, String view,
            ViewDumpHandler handler) throws IOException {
        int bufLen = 4 + viewRoot.length() * 2;
        if (view != null) {
            bufLen = 4 + view.length() * 2;
        }

        ByteBuffer buf = allocBuffer(bufLen);
        JdwpPacket packet = new JdwpPacket(buf);
        ByteBuffer chunkBuf = getChunkDataBuf(buf);

        chunkBuf.putInt(viewRoot.length());
        putString(chunkBuf, viewRoot);

        if (view != null) {
            chunkBuf.putInt(view.length());
            putString(chunkBuf, view);
        }

        finishChunkPacket(packet, type, chunkBuf.position());
        if (handler != null) {
            client.sendAndConsume(packet, handler);
        } else {
            client.sendAndConsume(packet);
        }
    }

    public static void captureView(Client client, String viewRoot, String view,
            CaptureViewHandler handler) throws IOException {
        sendJdwpPacket(client, CHUNK_VUCV, viewRoot, view, handler);
    }

    public static class CaptureViewHandler extends ViewDumpHandler {
        private AtomicReference<byte[]> mData = new AtomicReference<byte[]>();

        @Override
        void handleChunk(Client client, int type, ByteBuffer data, boolean isReply, int msgId) {
            if (type != CHUNK_VUCV) {
                handleUnknownChunk(client, type, data, isReply, msgId);
                return;
            }

            byte[] b = new byte[data.remaining()];
            data.get(b);
            mData.set(b);
            resultReceived();
        }

        public byte[] getData(long timeout, TimeUnit unit) {
            waitForResult(timeout, unit);
            return mData.get();
        }
    }

    public static void invalidateView(Client client, String viewRoot, String view)
            throws IOException {
        sendJdwpPacket(client, CHUNK_VUIV, viewRoot, view, null);
    }

    public static void requestLayout(Client client, String viewRoot, String view)
            throws IOException {
        sendJdwpPacket(client, CHUNK_VULT, viewRoot, view, null);
    }

    public static void dumpDisplayList(Client client, String viewRoot)
            throws IOException {
        sendJdwpPacket(client, CHUNK_VULT, viewRoot, null, null);
    }

    @Override
    public void handleChunk(Client client, int type, ByteBuffer data,
            boolean isReply, int msgId) {
    }
}









//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java
//Synthetic comment -- index 867446d..b08adc3 100644

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








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DdmViewDebugDevice.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DdmViewDebugDevice.java
new file mode 100644
//Synthetic comment -- index 0000000..5bdc612

//Synthetic comment -- @@ -0,0 +1,214 @@
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
import com.android.ddmlib.HandleViewDebug.CaptureViewHandler;
import com.android.ddmlib.HandleViewDebug.DumpViewHierarchyHandler;
import com.android.ddmlib.HandleViewDebug.ListViewRootsHandler;
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
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
        DumpViewHierarchyHandler handler = new DumpViewHierarchyHandler();
        try {
            HandleViewDebug.dumpViewHierarchy(c, viewRoot,
                    false /* skipChildren */,
                    true /* includeProperties */,
                    handler);
        } catch (IOException e) {
            Log.e(TAG, e);
            return null;
        }

        String viewHierarchy = handler.getViewHierarchy(10, TimeUnit.SECONDS);
        return DeviceBridge.parseViewHierarchy(new BufferedReader(new StringReader(viewHierarchy)),
                window);
    }

    @Override
    public void loadProfileData(Window window, ViewNode viewNode) {
    }

    @Override
    public Image loadCapture(Window window, ViewNode viewNode) {
        Client c = window.getClient();
        if (c == null) {
            return null;
        }

        String viewRoot = window.getTitle();
        CaptureViewHandler handler = new CaptureViewHandler();

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
        return null;
    }

    @Override
    public void invalidateView(ViewNode viewNode) {
    }

    @Override
    public void requestLayout(ViewNode viewNode) {
    }

    @Override
    public void outputDisplayList(ViewNode viewNode) {
    }

    @Override
    public void addWindowChangeListener(IWindowChangeListener l) {
    }

    @Override
    public void removeWindowChangeListener(IWindowChangeListener l) {
    }

    @Override
    public void deviceConnected(IDevice device) {
    }

    @Override
    public void deviceDisconnected(IDevice device) {
    }

    @Override
    public void deviceChanged(IDevice device, int changeMask) {
        if ((changeMask & IDevice.CHANGE_CLIENT_LIST) != 0) {
            reloadWindows();
        }
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index da29703..9da88cc 100644

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







