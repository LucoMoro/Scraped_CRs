/*removing ineffective code in WifiService.java

no need to use wrapper class Long.
I think this looks like typo.
However, this fix will give us more space.

WifiService.class 33646 -> 33541.

Change-Id:Ia3a57285170315a9563c6ccf687424fe95ccc2df*/




//Synthetic comment -- diff --git a/services/java/com/android/server/WifiService.java b/services/java/com/android/server/WifiService.java
//Synthetic comment -- index 1f03d17..5bb855d 100644

//Synthetic comment -- @@ -1534,7 +1534,7 @@
}

int uid = Binder.getCallingUid();
        final long ident = Binder.clearCallingIdentity();
try {
mBatteryStats.noteWifiMulticastEnabled(uid);
} catch (RemoteException e) {
//Synthetic comment -- @@ -1570,7 +1570,7 @@
mWifiStateMachine.startFilteringMulticastV4Packets();
}

        final long ident = Binder.clearCallingIdentity();
try {
mBatteryStats.noteWifiMulticastDisabled(uid);
} catch (RemoteException e) {







