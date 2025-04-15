/*LocationManagerService: Fix bug removing proximity alerts.

Alerts were not being removed from the mProximitiesEntered array.

Signed-off-by: Mike Lockwood <lockwood@android.com>*/
//Synthetic comment -- diff --git a/services/java/com/android/server/LocationManagerService.java b/services/java/com/android/server/LocationManagerService.java
//Synthetic comment -- index 4c3893c..024d8da 100644

//Synthetic comment -- @@ -1204,12 +1204,10 @@
// Remove expired alerts
if (intentsToRemove != null) {
for (PendingIntent i : intentsToRemove) {
                    mProximityAlerts.remove(i);
                    ProximityAlert alert = mProximityAlerts.get(i);
mProximitiesEntered.remove(alert);
}
}

}

// Note: this is called with the lock held.







