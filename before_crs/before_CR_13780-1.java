/*Remove the ProximityAlerts update Receiver when the last ProximityAlert expires.*/
//Synthetic comment -- diff --git a/services/java/com/android/server/LocationManagerService.java b/services/java/com/android/server/LocationManagerService.java
//Synthetic comment -- index bbb43d7..9a3b7ce 100644

//Synthetic comment -- @@ -1204,9 +1204,9 @@
// Remove expired alerts
if (intentsToRemove != null) {
for (PendingIntent i : intentsToRemove) {
                    mProximityAlerts.remove(i);
ProximityAlert alert = mProximityAlerts.get(i);
mProximitiesEntered.remove(alert);
}
}








