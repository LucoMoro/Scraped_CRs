//<Beginning of snippet n. 0>
return mIntent;
}

boolean isInProximity(double latitude, double longitude) {
    if (latitude < -90 || latitude > 90) {
        throw new IllegalArgumentException("Latitude must be in the range [-90, 90]");
    }
    if (longitude < -180 || longitude > 180) {
        throw new IllegalArgumentException("Longitude must be in the range [-180, 180]");
    }
    if (mLocation == null) {
        throw new IllegalStateException("Location not initialized");
    }
    
    double radius = mRadius;
    if (radius <= 0) {
        throw new IllegalArgumentException("Radius must be greater than zero");
    }

    Location loc = new Location("");
    loc.setLatitude(latitude);
    loc.setLongitude(longitude);
    
    double distance = loc.distanceTo(mLocation);
    return distance <= radius;
}

@Override
long now = System.currentTimeMillis();
double latitude = loc.getLatitude();
double longitude = loc.getLongitude();
ArrayList<PendingIntent> intentsToRemove = new ArrayList<>();

for (ProximityAlert alert : mProximityAlerts.values()) {
    long expiration = alert.getExpiration();
    if ((expiration == -1) || (now <= expiration)) {
        boolean entered = mProximitiesEntered.contains(alert);
        boolean inProximity = alert.isInProximity(latitude, longitude);
        if (!entered && inProximity) {
            if (LOCAL_LOGV) {
                Log.v(TAG, "Entered alert");
            }
            // Add additional logic to handle alert entry if necessary
        }
    }
}
//<End of snippet n. 0>