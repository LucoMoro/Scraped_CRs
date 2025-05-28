//<Beginning of snippet n. 0>
return mIntent;
}

boolean isInProximity(double latitude, double longitude) {
    Location loc = new Location("");
    loc.setLatitude(latitude);
    loc.setLongitude(longitude);

    if (mLocation == null) {
        return false;
    }

    double radius = loc.distanceTo(mLocation);
    return radius <= mRadius;
}

@Override
long now() {
    return System.currentTimeMillis();
}

for (ProximityAlert alert : mProximityAlerts.values()) {
    long expiration = alert.getExpiration();
    if ((expiration == -1) || (now() <= expiration)) {
        boolean entered = mProximitiesEntered.contains(alert);
        boolean inProximity = alert.isInProximity(lat, lon);
        if (!entered && inProximity) {
            if (LOCAL_LOGV) {
                Log.v(TAG, "Entered alert");
            }
            mProximitiesEntered.add(alert);
        }
    }
}
//<End of snippet n. 0>