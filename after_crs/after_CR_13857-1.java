/*Remove the ProximityAlerts update Receiver when the last ProximityAlert expires.
Fixes issue 6900.*/




//Synthetic comment -- diff --git a/services/java/com/android/server/LocationManagerService.java b/services/java/com/android/server/LocationManagerService.java
//Synthetic comment -- index 024d8da..6e5802f 100644

//Synthetic comment -- @@ -1204,8 +1204,9 @@
// Remove expired alerts
if (intentsToRemove != null) {
for (PendingIntent i : intentsToRemove) {
                    ProximityAlert alert = mProximityAlerts.get(i);
mProximitiesEntered.remove(alert);
                    removeProximityAlertLocked(i);
}
}
}







