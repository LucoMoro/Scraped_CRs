/*Handle hotplug events as described instead of rescanning

Hotplug events say which display they're for and whether the display
was connected or disconnected. Before, this info was ignored, and the
event just triggered a rescan of all displays. If a display was
disconnected and then reconnected quickly, the rescan would treat this
as a no-op or a device property change and wouldn't turn the display
on.

Now the display manager attempts to update its state with the change
the event describes. So a quick disconnect/connect cycle will cause
the display to be turned on since the display manager will have
updated its internal state to reflect the disconnect event, and will
treat the connect event as a new display rather than a device property
change.

Bug: 7491120
Change-Id:Ia83f7c96b7f16f4c1bed2a73e9b56b0bf7ee3984*/
//Synthetic comment -- diff --git a/services/java/com/android/server/display/LocalDisplayAdapter.java b/services/java/com/android/server/display/LocalDisplayAdapter.java
//Synthetic comment -- index b37d57f..ee2d617 100644

//Synthetic comment -- @@ -60,31 +60,38 @@
super.registerLocked();

mHotplugReceiver = new HotplugDisplayEventReceiver(getHandler().getLooper());
        scanDisplaysLocked();
}

    private void scanDisplaysLocked() {
        for (int builtInDisplayId : BUILT_IN_DISPLAY_IDS_TO_SCAN) {
            IBinder displayToken = Surface.getBuiltInDisplay(builtInDisplayId);
            if (displayToken != null && Surface.getDisplayInfo(displayToken, mTempPhys)) {
                LocalDisplayDevice device = mDevices.get(builtInDisplayId);
                if (device == null) {
                    // Display was added.
                    device = new LocalDisplayDevice(displayToken, builtInDisplayId, mTempPhys);
                    mDevices.put(builtInDisplayId, device);
                    sendDisplayDeviceEventLocked(device, DISPLAY_DEVICE_EVENT_ADDED);
                } else if (device.updatePhysicalDisplayInfoLocked(mTempPhys)) {
                    // Display properties changed.
                    sendDisplayDeviceEventLocked(device, DISPLAY_DEVICE_EVENT_CHANGED);
                }
            } else {
                LocalDisplayDevice device = mDevices.get(builtInDisplayId);
                if (device != null) {
                    // Display was removed.
                    mDevices.remove(builtInDisplayId);
                    sendDisplayDeviceEventLocked(device, DISPLAY_DEVICE_EVENT_REMOVED);
                }
}
}
}

//Synthetic comment -- @@ -191,7 +198,11 @@
@Override
public void onHotplug(long timestampNanos, int builtInDisplayId, boolean connected) {
synchronized (getSyncRoot()) {
                scanDisplaysLocked();
}
}
}







