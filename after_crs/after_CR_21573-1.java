/*frameworks/base: unlink death notifications of Vibrate requests

Death notifications of Vibrate requests isn't needed when they
are canceled. Fix is to unlink death notifications.

Change-Id:Ia0fc5f98c6fdb4e782cd74c1d02682f30cb7e659*/




//Synthetic comment -- diff --git a/services/java/com/android/server/VibratorService.java b/services/java/com/android/server/VibratorService.java
//Synthetic comment -- index f0b59555..ff54939 100755

//Synthetic comment -- @@ -269,6 +269,9 @@
Vibration vib = iter.next();
if (vib.mToken == token) {
iter.remove();
                // Death notification requested only for vibrate request with
                // patterns.
                token.unlinkToDeath(vib, 0);
return vib;
}
}
//Synthetic comment -- @@ -355,6 +358,7 @@
if (!mDone) {
// If this vibration finished naturally, start the next
// vibration.
                    mVibration.mToken.unlinkToDeath(mVibration, 0);
mVibrations.remove(mVibration);
startNextVibrationLocked();
}
//Synthetic comment -- @@ -367,6 +371,13 @@
if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
synchronized (mVibrations) {
doCancelVibrateLocked();

                    int size = mVibrations.size();
                    for(int i = 0; i < size; i++) {
                        final Vibration vib = mVibrations.get(i);
                        vib.mToken.unlinkToDeath(vib, 0);
                    }

mVibrations.clear();
}
}







