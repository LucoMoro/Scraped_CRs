/*Fix unable to show no media notification on status bar

Vold notify mount service before system boot completed and will
not notify again. Therefore, there will be no missing SD card
indication on the status bar. The solution is to check the media
status when mount service receive the boot complete intent and
update the status bar

Change-Id:I626e9e2833aa1561a51390308477e041007dd60d*/
//Synthetic comment -- diff --git a/services/java/com/android/server/MountService.java b/services/java/com/android/server/MountService.java
//Synthetic comment -- index 6ceeb95..b395a03 100644

//Synthetic comment -- @@ -121,6 +121,7 @@
final private ArrayList<MountServiceBinderListener> mListeners =
new ArrayList<MountServiceBinderListener>();
private boolean                               mBooted = false;
private boolean                               mReady = false;
private boolean                               mSendUmsConnectedOnBoot = false;

//Synthetic comment -- @@ -320,6 +321,12 @@
* the volume is shared (runtime restart while ums enabled)
*/
notifyVolumeStateChange(null, path, VolumeState.NoMedia, VolumeState.Shared);
}

/*
//Synthetic comment -- @@ -332,6 +339,8 @@
}
} catch (Exception ex) {
Slog.e(TAG, "Boot-time mount exception", ex);
}
}
}.start();
//Synthetic comment -- @@ -376,7 +385,7 @@
return;
}

        if (mLegacyState.equals(state)) {
Slog.w(TAG, String.format("Duplicate state transition (%s -> %s)", mLegacyState, state));
return;
}







