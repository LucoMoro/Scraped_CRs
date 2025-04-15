/*Setting time zone might cause processes to have different time zones

If you set the default time zone during heavy load of the system,
java processes listening to ACTION_TIMEZONE_CHANGED might not
get the new time zone value.

1. The time zone is stored in a static variable. That value is based
on a system property. Setting a new time zone triggers all
processess to reset the static variable by fetching from the system
property, but sometimes the system property has not been populated
yet, causing processes to reset the static variable with the old
value.

Solution: add param with the new time zone when updating
all processes.

2. Updating the process default time zone is posted to a Binder
Thread while the broadcast of ACTION_TIMEZONE_CHANGED is posted
to main. If the Binder Thread is busy, the broadcast might
get handled on the main thread before the time zone is updated.

Solution: Make sure that the time zone is updated before calling
onReceive

Change-Id:I0cb99e986b326aa5052a37b1b3e4a53102473008*/
//Synthetic comment -- diff --git a/core/java/android/app/ActivityThread.java b/core/java/android/app/ActivityThread.java
//Synthetic comment -- index 0c761fc..1e7a4c3 100644

//Synthetic comment -- @@ -713,8 +713,12 @@
queueOrSendMessage(H.CONFIGURATION_CHANGED, config);
}

        public void updateTimeZone() {
            TimeZone.setDefault(null);
}

public void clearDnsCache() {
//Synthetic comment -- @@ -2116,6 +2120,15 @@
ContextImpl context = (ContextImpl)app.getBaseContext();
sCurrentBroadcastIntent.set(data.intent);
receiver.setPendingResult(data);
receiver.onReceive(context.getReceiverRestrictedContext(),
data.intent);
} catch (Exception e) {








//Synthetic comment -- diff --git a/core/java/android/app/ApplicationThreadNative.java b/core/java/android/app/ApplicationThreadNative.java
//Synthetic comment -- index c4a4fea..48193bc 100644

//Synthetic comment -- @@ -312,7 +312,8 @@

case UPDATE_TIME_ZONE_TRANSACTION: {
data.enforceInterface(IApplicationThread.descriptor);
            updateTimeZone();
return true;
}

//Synthetic comment -- @@ -886,9 +887,10 @@
data.recycle();
}

    public void updateTimeZone() throws RemoteException {
Parcel data = Parcel.obtain();
data.writeInterfaceToken(IApplicationThread.descriptor);
mRemote.transact(UPDATE_TIME_ZONE_TRANSACTION, data, null,
IBinder.FLAG_ONEWAY);
data.recycle();








//Synthetic comment -- diff --git a/core/java/android/app/IApplicationThread.java b/core/java/android/app/IApplicationThread.java
//Synthetic comment -- index 1253fe7..cad520c 100644

//Synthetic comment -- @@ -96,7 +96,7 @@
void scheduleSuicide() throws RemoteException;
void requestThumbnail(IBinder token) throws RemoteException;
void scheduleConfigurationChanged(Configuration config) throws RemoteException;
    void updateTimeZone() throws RemoteException;
void clearDnsCache() throws RemoteException;
void setHttpProxy(String proxy, String port, String exclList) throws RemoteException;
void processInBackground() throws RemoteException;








//Synthetic comment -- diff --git a/services/java/com/android/server/AlarmManagerService.java b/services/java/com/android/server/AlarmManagerService.java
//Synthetic comment -- index b8c44d9..6fa6c30 100644

//Synthetic comment -- @@ -246,11 +246,14 @@
// the time zone property
boolean timeZoneWasChanged = false;
synchronized (this) {
            String current = SystemProperties.get(TIMEZONE_PROPERTY);
            if (current == null || !current.equals(zone.getID())) {
                if (localLOGV) Slog.v(TAG, "timezone changed: " + current + ", new=" + zone.getID());
                timeZoneWasChanged = true; 
SystemProperties.set(TIMEZONE_PROPERTY, zone.getID());
}

// Update the kernel timezone information
//Synthetic comment -- @@ -259,8 +262,7 @@
setKernelTimezone(mDescriptor, -(gmtOffset / 60000));
}

        TimeZone.setDefault(null);
        
if (timeZoneWasChanged) {
Intent intent = new Intent(Intent.ACTION_TIMEZONE_CHANGED);
intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);








//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index cffb391..97a3c94 100644

//Synthetic comment -- @@ -1013,7 +1013,7 @@
ProcessRecord r = mLruProcesses.get(i);
if (r.thread != null) {
try {
                                r.thread.updateTimeZone();
} catch (RemoteException ex) {
Slog.w(TAG, "Failed to update time zone for: " + r.info.processName);
}
//Synthetic comment -- @@ -12217,12 +12217,14 @@
}

/*
         * If this is the time zone changed action, queue up a message that will reset the timezone
* of all currently running processes. This message will get queued up before the broadcast
* happens.
*/
        if (intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())) {
            mHandler.sendEmptyMessage(UPDATE_TIME_ZONE);
}

if (intent.ACTION_CLEAR_DNS_CACHE.equals(intent.getAction())) {







