/*Use separate wake locks for each Receiver

If there was a problem with a Looper belonging to a LocationListener
the wake lock may never have been released. Also it was almost
impossible to know which application to blame because the wake lock
was held by the system.
The implementation is updated to use one wake lock for each Receiver
to make it possible to blame the correct application by the use of
work sources. The wake lock is also acquired separately for each
message to enable the possibility to use a timeout to release the
wake lock automatically in case the message is never received.

Change-Id:Ia8698d6c82c5861ee1413aa06de8261dcbb3f356*/
//Synthetic comment -- diff --git a/services/java/com/android/server/LocationManagerService.java b/services/java/com/android/server/LocationManagerService.java
//Synthetic comment -- index 2918dbc..0629308 100644

//Synthetic comment -- @@ -205,14 +205,25 @@
final PendingIntent mPendingIntent;
final Object mKey;
final HashMap<String,UpdateRecord> mUpdateRecords = new HashMap<String,UpdateRecord>();

        int mPendingBroadcasts;
String mRequiredPermissions;

Receiver(ILocationListener listener) {
mListener = listener;
mPendingIntent = null;
mKey = listener.asBinder();
}

Receiver(PendingIntent intent) {
//Synthetic comment -- @@ -221,6 +232,28 @@
mKey = intent;
}

@Override
public boolean equals(Object otherObj) {
if (otherObj instanceof Receiver) {
//Synthetic comment -- @@ -277,13 +310,13 @@
if (mListener != null) {
try {
synchronized (this) {
                        // synchronize to ensure incrementPendingBroadcastsLocked()
                        // is called before decrementPendingBroadcasts()
mListener.onStatusChanged(provider, status, extras);
if (mListener != mProximityListener) {
                            // call this after broadcasting so we do not increment
// if we throw an exeption.
                            incrementPendingBroadcastsLocked();
}
}
} catch (RemoteException e) {
//Synthetic comment -- @@ -295,13 +328,13 @@
statusChanged.putExtra(LocationManager.KEY_STATUS_CHANGED, status);
try {
synchronized (this) {
                        // synchronize to ensure incrementPendingBroadcastsLocked()
                        // is called before decrementPendingBroadcasts()
mPendingIntent.send(mContext, 0, statusChanged, this, mLocationHandler,
mRequiredPermissions);
// call this after broadcasting so we do not increment
// if we throw an exeption.
                        incrementPendingBroadcastsLocked();
}
} catch (PendingIntent.CanceledException e) {
return false;
//Synthetic comment -- @@ -314,13 +347,13 @@
if (mListener != null) {
try {
synchronized (this) {
                        // synchronize to ensure incrementPendingBroadcastsLocked()
                        // is called before decrementPendingBroadcasts()
mListener.onLocationChanged(location);
if (mListener != mProximityListener) {
                            // call this after broadcasting so we do not increment
// if we throw an exeption.
                            incrementPendingBroadcastsLocked();
}
}
} catch (RemoteException e) {
//Synthetic comment -- @@ -331,13 +364,13 @@
locationChanged.putExtra(LocationManager.KEY_LOCATION_CHANGED, location);
try {
synchronized (this) {
                        // synchronize to ensure incrementPendingBroadcastsLocked()
                        // is called before decrementPendingBroadcasts()
mPendingIntent.send(mContext, 0, locationChanged, this, mLocationHandler,
mRequiredPermissions);
// call this after broadcasting so we do not increment
// if we throw an exeption.
                        incrementPendingBroadcastsLocked();
}
} catch (PendingIntent.CanceledException e) {
return false;
//Synthetic comment -- @@ -350,17 +383,17 @@
if (mListener != null) {
try {
synchronized (this) {
                        // synchronize to ensure incrementPendingBroadcastsLocked()
                        // is called before decrementPendingBroadcasts()
if (enabled) {
mListener.onProviderEnabled(provider);
} else {
mListener.onProviderDisabled(provider);
}
if (mListener != mProximityListener) {
                            // call this after broadcasting so we do not increment
// if we throw an exeption.
                            incrementPendingBroadcastsLocked();
}
}
} catch (RemoteException e) {
//Synthetic comment -- @@ -371,13 +404,13 @@
providerIntent.putExtra(LocationManager.KEY_PROVIDER_ENABLED, enabled);
try {
synchronized (this) {
                        // synchronize to ensure incrementPendingBroadcastsLocked()
                        // is called before decrementPendingBroadcasts()
mPendingIntent.send(mContext, 0, providerIntent, this, mLocationHandler,
mRequiredPermissions);
// call this after broadcasting so we do not increment
// if we throw an exeption.
                        incrementPendingBroadcastsLocked();
}
} catch (PendingIntent.CanceledException e) {
return false;
//Synthetic comment -- @@ -395,31 +428,18 @@
removeUpdatesLocked(this);
}
synchronized (this) {
                if (mPendingBroadcasts > 0) {
                    LocationManagerService.this.decrementPendingBroadcasts();
                    mPendingBroadcasts = 0;
}
}
}

public void onSendFinished(PendingIntent pendingIntent, Intent intent,
int resultCode, String resultData, Bundle resultExtras) {
synchronized (this) {
                decrementPendingBroadcastsLocked();
            }
        }

        // this must be called while synchronized by caller in a synchronized block
        // containing the sending of the broadcaset
        private void incrementPendingBroadcastsLocked() {
            if (mPendingBroadcasts++ == 0) {
                LocationManagerService.this.incrementPendingBroadcasts();
            }
        }

        private void decrementPendingBroadcastsLocked() {
            if (--mPendingBroadcasts == 0) {
                LocationManagerService.this.decrementPendingBroadcasts();
}
}
}
//Synthetic comment -- @@ -435,7 +455,7 @@
synchronized (receiver) {
// so wakelock calls will succeed
long identity = Binder.clearCallingIdentity();
                receiver.decrementPendingBroadcastsLocked();
Binder.restoreCallingIdentity(identity);
}
}
//Synthetic comment -- @@ -1311,10 +1331,11 @@
if (mReceivers.remove(receiver.mKey) != null && receiver.isListener()) {
receiver.getListener().asBinder().unlinkToDeath(receiver, 0);
synchronized(receiver) {
                    if(receiver.mPendingBroadcasts > 0) {
                        decrementPendingBroadcasts();
                        receiver.mPendingBroadcasts = 0;
}
}
}








