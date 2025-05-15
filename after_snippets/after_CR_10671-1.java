
//<Beginning of snippet n. 0>


return mIntent;
}

        boolean isInProximity(double latitude, double longitude, float accuracy) {
Location loc = new Location("");
loc.setLatitude(latitude);
loc.setLongitude(longitude);

double radius = loc.distanceTo(mLocation);
            return radius <= Math.max(mRadius,accuracy);
}

@Override
long now = System.currentTimeMillis();
double latitude = loc.getLatitude();
double longitude = loc.getLongitude();
            float accuracy = loc.getAccuracy();
ArrayList<PendingIntent> intentsToRemove = null;

for (ProximityAlert alert : mProximityAlerts.values()) {
if ((expiration == -1) || (now <= expiration)) {
boolean entered = mProximitiesEntered.contains(alert);
boolean inProximity =
                        alert.isInProximity(latitude, longitude, accuracy);
if (!entered && inProximity) {
if (LOCAL_LOGV) {
Log.v(TAG, "Entered alert");

//<End of snippet n. 0>








