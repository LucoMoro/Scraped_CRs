/*LocationManagerService: Fix race when removing LocationListener

In LocationManagerService if a LocationListener is removed while it has
a pending broadcast the wake lock held while pending broadcasts are
outstanding do not get cleared properly.

There are 2 cases of this race that are fixed:

1. locationCallbackFinished was changed to check the mReceivers HashMap
directly instead of calling getReceiver.  getReceiver would add the
ILocationListener as a new Receiver if it did not exist which caused
a receiver that was removed when it still had a broadcast pending to
be added back in a bad state when the pending broadcast completed.

2. removeUpdatesLocked was changed to decrement the pending broadcasts
when a Receiver is removed that has pending broadcasts.*/




//Synthetic comment -- diff --git a/services/java/com/android/server/LocationManagerService.java b/services/java/com/android/server/LocationManagerService.java
//Synthetic comment -- index 9866cce..4a4d5ab 100644

//Synthetic comment -- @@ -394,7 +394,12 @@
}

public void locationCallbackFinished(ILocationListener listener) {
        //Do not use getReceiver here as that will add the ILocationListener to
        //the receiver list if it is not found.  If it is not found then the
        //LocationListener was removed when it had a pending broadcast and should
        //not be added back.
        IBinder binder = listener.asBinder();
        Receiver receiver = mReceivers.get(binder);
if (receiver != null) {
synchronized (receiver) {
// so wakelock calls will succeed
//Synthetic comment -- @@ -1030,6 +1035,12 @@
try {
if (mReceivers.remove(receiver.mKey) != null && receiver.isListener()) {
receiver.getListener().asBinder().unlinkToDeath(receiver, 0);
                synchronized(receiver) {
                    if(receiver.mPendingBroadcasts > 0) {
                        decrementPendingBroadcasts();
                        receiver.mPendingBroadcasts = 0;
                    }
                }
}

// Record which providers were associated with this listener







