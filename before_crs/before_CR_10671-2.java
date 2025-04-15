/*Fixed Android issue #2002: "Proximity alert detection logic is broken"http://code.google.com/p/android/issues/detail?id=2002*/
//Synthetic comment -- diff --git a/services/java/com/android/server/LocationManagerService.java b/services/java/com/android/server/LocationManagerService.java
//Synthetic comment -- index 3f268c9..9866cce 100644

//Synthetic comment -- @@ -1157,13 +1157,13 @@
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
//Synthetic comment -- @@ -1203,6 +1203,7 @@
long now = System.currentTimeMillis();
double latitude = loc.getLatitude();
double longitude = loc.getLongitude();
ArrayList<PendingIntent> intentsToRemove = null;

for (ProximityAlert alert : mProximityAlerts.values()) {
//Synthetic comment -- @@ -1212,7 +1213,7 @@
if ((expiration == -1) || (now <= expiration)) {
boolean entered = mProximitiesEntered.contains(alert);
boolean inProximity =
                        alert.isInProximity(latitude, longitude);
if (!entered && inProximity) {
if (LOCAL_LOGV) {
Log.v(TAG, "Entered alert");







