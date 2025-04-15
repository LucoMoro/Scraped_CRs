/*Remove ProximityAlerts Receiver when last ProximityAlert expires.

Remove the ProximityAlerts update Receiver when the last ProximityAlert expires.
Fixes issue 6900.

Change-Id:Ida1970c084e71df47b204c64986a065cb75d0c13*/




//Synthetic comment -- diff --git a/services/java/com/android/server/LocationManagerService.java b/services/java/com/android/server/LocationManagerService.java
//Synthetic comment -- index e39da8c..d679a5e 100644

//Synthetic comment -- @@ -1202,8 +1202,9 @@
// Remove expired alerts
if (intentsToRemove != null) {
for (PendingIntent i : intentsToRemove) {
                    ProximityAlert alert = mProximityAlerts.get(i);
mProximitiesEntered.remove(alert);
                    removeProximityAlertLocked(i);
}
}
}







