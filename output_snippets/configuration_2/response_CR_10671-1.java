//<Beginning of snippet n. 0>
return mIntent;
}

private final Location mLocation = new Location("");

boolean isInProximity(double latitude, double longitude) {
    mLocation.setLatitude(latitude);
    mLocation.setLongitude(longitude);

    if (mLocation == null) {
        return false;
    }

    double radius = mLocation.distanceTo(mLocation);
    return radius <= mRadius;
}

@Override
long now() {
    return System.currentTimeMillis();
}

synchronized void checkProximityAlerts() {
    for (ProximityAlert alert : mProximityAlerts.values()) {
        long expiration = alert.getExpiration();
        if ((expiration == -1) || (now() <= expiration)) {
            boolean entered;
            synchronized (mProximitiesEntered) {
                entered = mProximitiesEntered.contains(alert);
            }
            boolean inProximity = alert.isInProximity(lat, lon);
            if (!entered && inProximity) {
                if (LOCAL_LOGV) {
                    Log.v(TAG, "Entered alert");
                }
                synchronized (mProximitiesEntered) {
                    mProximitiesEntered.add(alert);
                }
            }
        }
    }
}
//<End of snippet n. 0>