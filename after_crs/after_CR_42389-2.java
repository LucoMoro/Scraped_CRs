/*Mount state of sd card not correct

If there are multiple mount events within a short time span (for
instance if auto mount is activated and the usb cable is inserted
and withdrawn in a quick succession) the sd card might not end up in
the correct mount state. This is due to MountService handling mount
events asynchronosly on multiple new threads.
Make MountService handle events in a controlled serial manner, in
one single background thread.

Change-Id:Iff158703112114fb98b1c960563a8aa68bede02d*/




//Synthetic comment -- diff --git a/services/java/com/android/server/MountService.java b/services/java/com/android/server/MountService.java
//Synthetic comment -- index 04267a3..a6c1e13 100644

//Synthetic comment -- @@ -211,6 +211,9 @@
final private Map<IBinder, List<ObbState>> mObbMounts = new HashMap<IBinder, List<ObbState>>();
final private Map<String, ObbState> mObbPathToStateMap = new HashMap<String, ObbState>();

    final private HandlerThread mWorkerThread;
    final private Handler mWorkerHandler;

class ObbState implements IBinder.DeathRecipient {
public ObbState(String filename, int callerUid, IObbActionListener token, int nonce)
throws RemoteException {
//Synthetic comment -- @@ -484,18 +487,17 @@
if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
mBooted = true;

new Thread() {
public void run() {
                        /*
                         * In the simulator, we need to broadcast a volume mounted event
                         * to make the media scanner run.
                         */
                        if ("simulator".equals(SystemProperties.get("ro.product.device"))) {
                            notifyVolumeStateChange(null, "/sdcard", VolumeState.NoMedia,
                                    VolumeState.Mounted);
                            return;
                        }
try {
// it is not safe to call vold with mVolumeStates locked
// so we make a copy of the paths and states and process them
//Synthetic comment -- @@ -643,9 +645,9 @@
public void onDaemonConnected() {
/*
* Since we'll be calling back into the NativeDaemonConnector,
         * we need to do our work in the worker thread.
*/
        mWorkerHandler.post(new Runnable() {
@Override
public void run() {
/**
//Synthetic comment -- @@ -700,104 +702,102 @@
mAsecsScanned.countDown();
mAsecsScanned = null;
}
        });
}

/**
* Callback from NativeDaemonConnector
*/
    public boolean onEvent(final int code, final String raw, final String[] cooked) {
        mWorkerHandler.post(new Runnable() {
            public void run() {
                if (DEBUG_EVENTS) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("onEvent::");
                    builder.append(" raw= " + raw);
                    if (cooked != null) {
                        builder.append(" cooked = " );
                        for (String str : cooked) {
                            builder.append(" " + str);
}
}
                    Slog.i(TAG, builder.toString());
}
                if (code == VoldResponseCode.VolumeStateChange) {
                    /*
                     * One of the volumes we're managing has changed state.
                     * Format: "NNN Volume <label> <path> state changed
                     * from <old_#> (<old_str>) to <new_#> (<new_str>)"
                     */
                    notifyVolumeStateChange(cooked[2], cooked[3], Integer.parseInt(cooked[7]),
                            Integer.parseInt(cooked[10]));
                } else if ((code == VoldResponseCode.VolumeDiskInserted) ||
                           (code == VoldResponseCode.VolumeDiskRemoved) ||
                           (code == VoldResponseCode.VolumeBadRemoval)) {
                    // FMT: NNN Volume <label> <mountpoint> disk inserted (<major>:<minor>)
                    // FMT: NNN Volume <label> <mountpoint> disk removed (<major>:<minor>)
                    // FMT: NNN Volume <label> <mountpoint> bad removal (<major>:<minor>)
                    String action = null;
                    final String label = cooked[2];
                    final String path = cooked[3];
                    int major = -1;
                    int minor = -1;

                    try {
                        String devComp = cooked[6].substring(1, cooked[6].length() - 1);
                        String[] devTok = devComp.split(":");
                        major = Integer.parseInt(devTok[0]);
                        minor = Integer.parseInt(devTok[1]);
                    } catch (Exception ex) {
                        Slog.e(TAG, "Failed to parse major/minor", ex);
                    }

                    if (code == VoldResponseCode.VolumeDiskInserted) {
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    int rc = doMountVolume(path);
                                    if (rc != StorageResultCode.OperationSucceeded) {
                                        Slog.w(TAG, "Insertion mount failed : " + rc);
                                    }
                                } catch (Exception ex) {
                                    Slog.w(TAG, "Failed to mount media on insertion", ex);
                                }
                            }
                        }.start();
                    } else if (code == VoldResponseCode.VolumeDiskRemoved) {
                        // This event gets trumped if we're already in BAD_REMOVAL state
                        if (getVolumeState(path).equals(Environment.MEDIA_BAD_REMOVAL)) {
                            return;
                        }
                        // Send the media unmounted event first
                        if (DEBUG_EVENTS) Slog.i(TAG, "Sending unmounted event first");
                        updatePublicVolumeState(path, Environment.MEDIA_UNMOUNTED);
                        sendStorageIntent(Environment.MEDIA_UNMOUNTED, path);

                        if (DEBUG_EVENTS) Slog.i(TAG, "Sending media removed");
                        updatePublicVolumeState(path, Environment.MEDIA_REMOVED);
                        action = Intent.ACTION_MEDIA_REMOVED;
                    } else if (code == VoldResponseCode.VolumeBadRemoval) {
                        if (DEBUG_EVENTS) Slog.i(TAG, "Sending unmounted event first");
                        // Send the media unmounted event first
                        updatePublicVolumeState(path, Environment.MEDIA_UNMOUNTED);
                        action = Intent.ACTION_MEDIA_UNMOUNTED;

                        if (DEBUG_EVENTS) Slog.i(TAG, "Sending media bad removal");
                        updatePublicVolumeState(path, Environment.MEDIA_BAD_REMOVAL);
                        action = Intent.ACTION_MEDIA_BAD_REMOVAL;
                    } else {
                        Slog.e(TAG, "Unknown code : " + code);
                    }

                    if (action != null) {
                        sendStorageIntent(action, path);
                    }
                }
}
        });
return true;
}

//Synthetic comment -- @@ -1022,23 +1022,18 @@
/*
* USB mass storage disconnected while enabled
*/
            try {
                int rc;
                Slog.w(TAG, "Disabling UMS after cable disconnect");
                doShareUnshareVolume(path, "ums", false);
                if ((rc = doMountVolume(path)) != StorageResultCode.OperationSucceeded) {
                    Slog.e(TAG, String.format(
                            "Failed to remount {%s} on UMS enabled-disconnect (%d)",
                                    path, rc));
}
            } catch (Exception ex) {
                Slog.w(TAG, "Failed to mount media on UMS enabled-disconnect", ex);
            }
}
}

//Synthetic comment -- @@ -1181,6 +1176,10 @@
mHandlerThread.start();
mHandler = new MountServiceHandler(mHandlerThread.getLooper());

        mWorkerThread = new HandlerThread("MountService-worker");
        mWorkerThread.start();
        mWorkerHandler = new Handler(mWorkerThread.getLooper());

// Add OBB Action Handler to MountService thread.
mObbActionHandler = new ObbActionHandler(mHandlerThread.getLooper());








