/*Unregister callback from binder when vibration is removed

When creating new vibration pattern, a callback is registered
in case binding to caller goes away, by linkToDeath().
Need to unregister this callback when we throw away the vibration.

Change-Id:Ibdf0bd415a539054ac7a66f49b33a864f729c546*/




//Synthetic comment -- diff --git a/services/java/com/android/server/VibratorService.java b/services/java/com/android/server/VibratorService.java
//Synthetic comment -- index 2e7e3e1..8bfdcf7 100755

//Synthetic comment -- @@ -33,6 +33,7 @@

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class VibratorService extends IVibratorService.Stub {
private static final String TAG = "VibratorService";
//Synthetic comment -- @@ -263,17 +264,31 @@
Vibration vib = iter.next();
if (vib.mToken == token) {
iter.remove();
                unlinkToDeath(vib);
return vib;
}
}
// We might be looking for a simple vibration which is only stored in
// mCurrentVibration.
if (mCurrentVibration != null && mCurrentVibration.mToken == token) {
            unlinkToDeath(mCurrentVibration);
return mCurrentVibration;
}
return null;
}

    private void unlinkToDeath(Vibration vib) {
        if (vib.mPattern != null) {
            // If Vibration object has a pattern,
            // the Vibration object has also been linkedToDeath.
            try {
                vib.mToken.unlinkToDeath(vib, 0);
            } catch (NoSuchElementException e) {
                // Do nothing.
            }
        }
    }

private class VibrateThread extends Thread {
final Vibration mVibration;
boolean mDone;
//Synthetic comment -- @@ -348,6 +363,7 @@
// If this vibration finished naturally, start the next
// vibration.
mVibrations.remove(mVibration);
                    unlinkToDeath(mVibration);
startNextVibrationLocked();
}
}







