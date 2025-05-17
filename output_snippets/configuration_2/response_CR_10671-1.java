//<Beginning of snippet n. 0>
return mIntent;
}

boolean isInProximity(double latitude, double longitude) {
    if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
        throw new IllegalArgumentException("Latitude and longitude must be within valid ranges.");
    }
    
    Location loc = new Location("");
    loc.setLatitude(latitude);
    loc.setLongitude(longitude);

    double radius = loc.distanceTo(mLocation);
    return radius <= mRadius;
}

@Override
long now = System.currentTimeMillis();
double latitude = loc != null ? loc.getLatitude() : 0;
double longitude = loc != null ? loc.getLongitude() : 0;
ArrayList<PendingIntent> intentsToRemove = null;

synchronized (mProximitiesEntered) {
    for (ProximityAlert alert : mProximityAlerts.values()) {
        if ((expiration != null && expiration != -1 && now > expiration) || (expiration == null)) {
            continue;
        }
        boolean entered = mProximitiesEntered.contains(alert);
        boolean inProximity = alert.isInProximity(latitude, longitude);
        if (!entered && inProximity) {
            if (LOCAL_LOGV) {
                Log.v(TAG, "Entered alert with ID: " + alert.getId() + " at coordinates: (" + latitude + ", " + longitude + ")");
            }
            // Additional logic for adding intents or handling alerts can go here.
        }
    }
}
//<End of snippet n. 0>