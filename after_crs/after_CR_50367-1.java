/*Fix bug #8054449 hierarchyviewer crashes on connection/disconnections

- fix various NPEs

Change-Id:I9157b9072c822146874d1c693a81c024fa5fe5f2*/




//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewServerDevice.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewServerDevice.java
//Synthetic comment -- index 4bd5d6b..0febcef 100644

//Synthetic comment -- @@ -135,14 +135,14 @@

@Override
public void addWindowChangeListener(IWindowChangeListener l) {
        if (mViewServerInfo != null && mViewServerInfo.protocolVersion >= 3) {
WindowUpdater.startListenForWindowChanges(l, mDevice);
}
}

@Override
public void removeWindowChangeListener(IWindowChangeListener l) {
        if (mViewServerInfo != null && mViewServerInfo.protocolVersion >= 3) {
WindowUpdater.stopListenForWindowChanges(l, mDevice);
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/WindowUpdater.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/WindowUpdater.java
//Synthetic comment -- index 9dbb989..a67d400 100644

//Synthetic comment -- @@ -72,6 +72,9 @@
public static void stopListenForWindowChanges(IWindowChangeListener listener, IDevice device) {
synchronized (sWindowChangeListeners) {
ArrayList<IWindowChangeListener> listeners = sWindowChangeListeners.get(device);
            if (listeners == null) {
                return;
            }
listeners.remove(listener);
// There are more listeners, so don't stop the listening thread.
if (listeners.size() != 0) {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/DeviceSelector.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/DeviceSelector.java
//Synthetic comment -- index c3154c8..ae8ad26 100644

//Synthetic comment -- @@ -330,7 +330,9 @@

@Override
public void widgetSelected(SelectionEvent e) {
        TreeItem item = (TreeItem) e.item;
        if (item == null) return;
        Object selection = item.getData();
if (selection instanceof IHvDevice) {
mModel.setSelection((IHvDevice) selection, null);
} else if (selection instanceof Window) {







