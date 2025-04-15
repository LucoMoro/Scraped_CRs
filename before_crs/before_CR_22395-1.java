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
//Synthetic comment -- index 162d9eb..74ce202 100644

//Synthetic comment -- @@ -580,8 +580,12 @@
queueOrSendMessage(H.CONFIGURATION_CHANGED, config);
}

        public void updateTimeZone() {
            TimeZone.setDefault(null);
}

public void processInBackground() {
//Synthetic comment -- @@ -1791,6 +1795,15 @@
receiver.setResult(data.resultCode, data.resultData,
data.resultExtras);
receiver.setOrderedHint(data.sync);
receiver.onReceive(context.getReceiverRestrictedContext(),
data.intent);
} catch (Exception e) {








//Synthetic comment -- diff --git a/core/java/android/app/ApplicationThreadNative.java b/core/java/android/app/ApplicationThreadNative.java
//Synthetic comment -- index 1c20062..982fbec 100644

//Synthetic comment -- @@ -287,7 +287,8 @@

case UPDATE_TIME_ZONE_TRANSACTION: {
data.enforceInterface(IApplicationThread.descriptor);
            updateTimeZone();
return true;
}

//Synthetic comment -- @@ -710,9 +711,10 @@
data.recycle();
}

    public void updateTimeZone() throws RemoteException {
Parcel data = Parcel.obtain();
data.writeInterfaceToken(IApplicationThread.descriptor);
mRemote.transact(UPDATE_TIME_ZONE_TRANSACTION, data, null,
IBinder.FLAG_ONEWAY);
data.recycle();








//Synthetic comment -- diff --git a/core/java/android/app/IApplicationThread.java b/core/java/android/app/IApplicationThread.java
//Synthetic comment -- index c8ef17f..7eace32 100644

//Synthetic comment -- @@ -86,7 +86,7 @@
void scheduleSuicide() throws RemoteException;
void requestThumbnail(IBinder token) throws RemoteException;
void scheduleConfigurationChanged(Configuration config) throws RemoteException;
    void updateTimeZone() throws RemoteException;
void processInBackground() throws RemoteException;
void dumpService(FileDescriptor fd, IBinder servicetoken, String[] args)
throws RemoteException;








//Synthetic comment -- diff --git a/services/java/com/android/server/AlarmManagerService.java b/services/java/com/android/server/AlarmManagerService.java
//Synthetic comment -- index 4931cc7..4c954d3 100644

//Synthetic comment -- @@ -272,13 +272,16 @@
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
// Kernel tracks time offsets as 'minutes west of GMT'
int gmtOffset = zone.getRawOffset();
//Synthetic comment -- @@ -288,8 +291,7 @@
setKernelTimezone(mDescriptor, -(gmtOffset / 60000));
}

        TimeZone.setDefault(null);
        
if (timeZoneWasChanged) {
Intent intent = new Intent(Intent.ACTION_TIMEZONE_CHANGED);
intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);








//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 0e38e10..622dea3 100755

//Synthetic comment -- @@ -1102,7 +1102,7 @@
ProcessRecord r = mLruProcesses.get(i);
if (r.thread != null) {
try {
                                r.thread.updateTimeZone();
} catch (RemoteException ex) {
Slog.w(TAG, "Failed to update time zone for: " + r.info.processName);
}
//Synthetic comment -- @@ -10222,12 +10222,14 @@
}

/*
         * If this is the time zone changed action, queue up a message that will reset the timezone
* of all currently running processes. This message will get queued up before the broadcast
* happens.
*/
        if (intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())) {
            mHandler.sendEmptyMessage(UPDATE_TIME_ZONE);
}

/*







