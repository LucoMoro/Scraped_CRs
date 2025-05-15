//<Beginning of snippet n. 0>
return mIntent;
}

boolean isInProximity(double latitude, double longitude) {
    if (mLocation == null) {
        return false;
    }
    if (mRadius <= 0) {
        throw new IllegalArgumentException("Radius must be positive.");
    }
    
    Location loc = new Location("");
    loc.setLatitude(latitude);
    loc.setLongitude(longitude);

    double radius = loc.distanceTo(mLocation);
    return radius <= mRadius;
}

@Override
long now = System.currentTimeMillis();
double latitude = loc.getLatitude();
double longitude = loc.getLongitude();
ArrayList<PendingIntent> intentsToRemove = new ArrayList<>();

for (ProximityAlert alert : mProximityAlerts.values()) {
    long expiration = alert.getExpiration(); // Assuming expiration needs to be fetched from alert
    if ((expiration == -1) || (now <= expiration)) {
        boolean entered = mProximitiesEntered.contains(alert);
        boolean inProximity = alert.isInProximity(latitude, longitude);
        if (!entered && inProximity) {
            if (LOCAL_LOGV) {
                Log.v(TAG, "Entered alert");
            }
            mProximitiesEntered.add(alert); // Synchronizing state management
        } else if (entered && !inProximity) {
            if (LOCAL_LOGV) {
                Log.v(TAG, "Exited alert");
            }
            mProximitiesEntered.remove(alert); // Synchronizing state management
        }
    }
}
//<End of snippet n. 0>