/*Unregister callback from binder when vibration is removed

When creating new vibration pattern, a callback is registered
in case binding to caller goes away, by linkToDeath().
Need to unregister this callback when we throw away the vibration.

Change-Id:Ibdf0bd415a539054ac7a66f49b33a864f729c546*/
//Synthetic comment -- diff --git a/services/java/com/android/server/VibratorService.java b/services/java/com/android/server/VibratorService.java
//Synthetic comment -- index f0b59555..86c30f8 100755

//Synthetic comment -- @@ -243,6 +243,7 @@
// Lock held on mVibrations
private void startNextVibrationLocked() {
if (mVibrations.size() <= 0) {
return;
}
mCurrentVibration = mVibrations.getFirst();
//Synthetic comment -- @@ -269,17 +270,27 @@
Vibration vib = iter.next();
if (vib.mToken == token) {
iter.remove();
return vib;
}
}
// We might be looking for a simple vibration which is only stored in
// mCurrentVibration.
if (mCurrentVibration != null && mCurrentVibration.mToken == token) {
return mCurrentVibration;
}
return null;
}

private class VibrateThread extends Thread {
final Vibration mVibration;
boolean mDone;
//Synthetic comment -- @@ -356,6 +367,7 @@
// If this vibration finished naturally, start the next
// vibration.
mVibrations.remove(mVibration);
startNextVibrationLocked();
}
}







