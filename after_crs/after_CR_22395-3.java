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
//Synthetic comment -- index 7242029..d481f48 100644

//Synthetic comment -- @@ -732,8 +732,12 @@
queueOrSendMessage(H.CONFIGURATION_CHANGED, config);
}

        public void updateTimeZone(String timeZone) {
            if (timeZone != null) {
                TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
            } else {
                TimeZone.setDefault(null);
            }
}

public void clearDnsCache() {
//Synthetic comment -- @@ -2226,6 +2230,15 @@
ContextImpl context = (ContextImpl)app.getBaseContext();
sCurrentBroadcastIntent.set(data.intent);
receiver.setPendingResult(data);
            if (Intent.ACTION_TIMEZONE_CHANGED.equals(data.intent.getAction())) {
                // The default time zone is updated via IBinder but it might not have
                // been updated yet. Make sure the default time zone for this process
                // is set before we continue.
                final String timeZone = data.intent.getStringExtra("time-zone");
                if (timeZone != null) {
                    TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
                }
            }
receiver.onReceive(context.getReceiverRestrictedContext(),
data.intent);
} catch (Exception e) {








//Synthetic comment -- diff --git a/core/java/android/app/ApplicationThreadNative.java b/core/java/android/app/ApplicationThreadNative.java
//Synthetic comment -- index 8e6278d..56a5949 100644

//Synthetic comment -- @@ -313,7 +313,8 @@

case UPDATE_TIME_ZONE_TRANSACTION: {
data.enforceInterface(IApplicationThread.descriptor);
            String timeZone = data.readString();
            updateTimeZone(timeZone);
return true;
}

//Synthetic comment -- @@ -934,9 +935,10 @@
data.recycle();
}

    public void updateTimeZone(String timeZone) throws RemoteException {
Parcel data = Parcel.obtain();
data.writeInterfaceToken(IApplicationThread.descriptor);
        data.writeString(timeZone);
mRemote.transact(UPDATE_TIME_ZONE_TRANSACTION, data, null,
IBinder.FLAG_ONEWAY);
data.recycle();








//Synthetic comment -- diff --git a/core/java/android/app/IApplicationThread.java b/core/java/android/app/IApplicationThread.java
//Synthetic comment -- index f60cfd6..1b2c068 100644

//Synthetic comment -- @@ -96,7 +96,7 @@
void scheduleSuicide() throws RemoteException;
void requestThumbnail(IBinder token) throws RemoteException;
void scheduleConfigurationChanged(Configuration config) throws RemoteException;
    void updateTimeZone(String timeZone) throws RemoteException;
void clearDnsCache() throws RemoteException;
void setHttpProxy(String proxy, String port, String exclList) throws RemoteException;
void processInBackground() throws RemoteException;








//Synthetic comment -- diff --git a/services/java/com/android/server/AlarmManagerService.java b/services/java/com/android/server/AlarmManagerService.java
//Synthetic comment -- index 32ac8e1..d593d48 100644

//Synthetic comment -- @@ -248,11 +248,14 @@
// the time zone property
boolean timeZoneWasChanged = false;
synchronized (this) {
            TimeZone current = TimeZone.getDefault();
            if (current == null || !current.getID().equals(zone.getID())) {
                if (localLOGV) {
                    Slog.v(TAG, "timezone changed: " + current.getID() + ", new=" + zone.getID());
                }
                timeZoneWasChanged = true;
SystemProperties.set(TIMEZONE_PROPERTY, zone.getID());
                TimeZone.setDefault(zone);
}

// Update the kernel timezone information
//Synthetic comment -- @@ -261,8 +264,7 @@
setKernelTimezone(mDescriptor, -(gmtOffset / 60000));
}


if (timeZoneWasChanged) {
Intent intent = new Intent(Intent.ACTION_TIMEZONE_CHANGED);
intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);








//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 60085f4..84151ae 100644

//Synthetic comment -- @@ -1020,7 +1020,7 @@
ProcessRecord r = mLruProcesses.get(i);
if (r.thread != null) {
try {
                                r.thread.updateTimeZone((String)msg.obj);
} catch (RemoteException ex) {
Slog.w(TAG, "Failed to update time zone for: " + r.info.processName);
}
//Synthetic comment -- @@ -12909,12 +12909,14 @@
}

/*
         * If this is the time zone changed action, queue up a message that will set the timezone
* of all currently running processes. This message will get queued up before the broadcast
* happens.
*/
        if (Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())) {
            Message msg = mHandler.obtainMessage(UPDATE_TIME_ZONE);
            msg.obj = intent.getStringExtra("time-zone");
            mHandler.sendMessage(msg);
}

if (intent.ACTION_CLEAR_DNS_CACHE.equals(intent.getAction())) {







