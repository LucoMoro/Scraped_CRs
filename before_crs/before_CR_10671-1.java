/*Fixed Android issue #2002: "Proximity alert detection logic is broken"http://code.google.com/p/android/issues/detail?id=2002*/
//Synthetic comment -- diff --git a/services/java/com/android/server/LocationManagerService.java b/services/java/com/android/server/LocationManagerService.java
//Synthetic comment -- index 412dcf8..032efaa 100644

//Synthetic comment -- @@ -1236,13 +1236,13 @@
return mIntent;
}

        boolean isInProximity(double latitude, double longitude) {
Location loc = new Location("");
loc.setLatitude(latitude);
loc.setLongitude(longitude);

double radius = loc.distanceTo(mLocation);
            return radius <= mRadius;
}

@Override
//Synthetic comment -- @@ -1282,6 +1282,7 @@
long now = System.currentTimeMillis();
double latitude = loc.getLatitude();
double longitude = loc.getLongitude();
ArrayList<PendingIntent> intentsToRemove = null;

for (ProximityAlert alert : mProximityAlerts.values()) {
//Synthetic comment -- @@ -1291,7 +1292,7 @@
if ((expiration == -1) || (now <= expiration)) {
boolean entered = mProximitiesEntered.contains(alert);
boolean inProximity =
                        alert.isInProximity(latitude, longitude);
if (!entered && inProximity) {
if (LOCAL_LOGV) {
Log.v(TAG, "Entered alert");







