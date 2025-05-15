//<Beginning of snippet n. 0>
return mIntent;
}

boolean isInProximity(double latitude, double longitude) {
    if (mLocation == null) {
        Log.e(TAG, "Current location (mLocation) is not initialized.");
        return false;
    }
    Location loc = new Location("");
    loc.setLatitude(latitude);
    loc.setLongitude(longitude);

    double radius = loc.distanceTo(mLocation);
    return radius <= mRadius;
}

@Override
long now = System.currentTimeMillis();
double latitude = mLocation.getLatitude();
double longitude = mLocation.getLongitude();
ArrayList<PendingIntent> intentsToRemove = null;

for (ProximityAlert alert : mProximityAlerts.values()) {
    long expiration = alert.getExpiration();
    if ((expiration == -1) || (now <= expiration)) {
        boolean entered = mProximitiesEntered.contains(alert);
        boolean inProximity = alert.isInProximity(latitude, longitude);
        if (!entered && inProximity) {
            if (LOCAL_LOGV) {
                Log.v(TAG, "Entered alert");
            }
            mProximitiesEntered.add(alert);
            // Additional logic to handle alert entry
        }
    } else {
        if (mProximitiesEntered.contains(alert)) {
            if (LOCAL_LOGV) {
                Log.v(TAG, "Exited alert");
            }
            mProximitiesEntered.remove(alert);
            // Additional logic to handle alert exit
        }
    }
}
//<End of snippet n. 0>