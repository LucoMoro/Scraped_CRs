/*BT: Fix for SDP timeout error

This patch fixes a service discovery failure due to a timeout
that occurs after an unsuccessful channel search for a requested
service in cache, even though SDP is not finished yet.

Change-Id:I32ce5fb2071fed0e12cbe936c10f66f5d72fcd8aSigned-off-by: Christian Bejram <christian.bejram@stericsson.com>*/
//Synthetic comment -- diff --git a/core/java/android/server/BluetoothEventLoop.java b/core/java/android/server/BluetoothEventLoop.java
//Synthetic comment -- index a2038c9..69ef2e8 100644

//Synthetic comment -- @@ -446,6 +446,8 @@
mBluetoothService.updateDeviceServiceChannelCache(address);

mBluetoothService.sendUuidIntent(address);
} else if (name.equals("Paired")) {
if (propValues[1].equals("true")) {
// If locally initiated pairing, we will








//Synthetic comment -- diff --git a/core/java/android/server/BluetoothService.java b/core/java/android/server/BluetoothService.java
//Synthetic comment -- index 94fbbc8..1ccc4be 100755

//Synthetic comment -- @@ -511,15 +511,8 @@
@Override
public void handleMessage(Message msg) {
switch (msg.what) {
            case MESSAGE_UUID_INTENT:
                String address = (String)msg.obj;
                if (address != null) {
                    sendUuidIntent(address);
                    makeServiceChannelCallbacks(address);
                }
                break;
case MESSAGE_AUTO_PAIRING_FAILURE_ATTEMPT_DELAY:
                address = (String)msg.obj;
if (address == null) return;
int attempt = mBondState.getAttempt(address);

//Synthetic comment -- @@ -1254,9 +1247,6 @@
* The UUID's found are broadcast as intents.
* Optionally takes a uuid and callback to fetch the RFCOMM channel for the
* a given uuid.
     * TODO: Don't wait UUID_INTENT_DELAY to broadcast UUID intents on success
     * TODO: Don't wait UUID_INTENT_DELAY to handle the failure case for
     * callback and broadcast intents.
*/
public synchronized boolean fetchRemoteUuids(String address, ParcelUuid uuid,
IBluetoothCallback callback) {
//Synthetic comment -- @@ -1292,9 +1282,6 @@
mUuidCallbackTracker.put(new RemoteService(address, uuid), callback);
}

        Message message = mHandler.obtainMessage(MESSAGE_UUID_INTENT);
        message.obj = address;
        mHandler.sendMessageDelayed(message, UUID_INTENT_DELAY);
return ret;
}

//Synthetic comment -- @@ -1474,28 +1461,6 @@
}

synchronized (this) {
            // Make application callbacks
            for (Iterator<RemoteService> iter = mUuidCallbackTracker.keySet().iterator();
                    iter.hasNext();) {
                RemoteService service = iter.next();
                if (service.address.equals(address)) {
                    if (uuidToChannelMap.containsKey(service.uuid)) {
                        int channel = uuidToChannelMap.get(service.uuid);

                        if (DBG) Log.d(TAG, "Making callback for " + service.uuid +
                                    " with result " + channel);
                        IBluetoothCallback callback = mUuidCallbackTracker.get(service);
                        if (callback != null) {
                            try {
                                callback.onRfcommChannelFound(channel);
                            } catch (RemoteException e) {Log.e(TAG, "", e);}
                        }

                        iter.remove();
                    }
                }
            }

// Update cache
mDeviceServiceChannelCache.put(address, uuidToChannelMap);
}
//Synthetic comment -- @@ -1726,13 +1691,25 @@
iter.hasNext();) {
RemoteService service = iter.next();
if (service.address.equals(address)) {
                if (DBG) Log.d(TAG, "Cleaning up failed UUID channel lookup: "
                    + service.address + " " + service.uuid);
IBluetoothCallback callback = mUuidCallbackTracker.get(service);
if (callback != null) {
                    try {
                        callback.onRfcommChannelFound(-1);
                    } catch (RemoteException e) {Log.e(TAG, "", e);}
}

iter.remove();







