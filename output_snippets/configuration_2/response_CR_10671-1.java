//<Beginning of snippet n. 0>
return mIntent;
}

boolean isInProximity(double latitude, double longitude) {
    if (mLocation == null || mRadius <= 0) {
        return false;
    }
    Location loc = new Location(mLocation); 
    loc.setLatitude(latitude);
    loc.setLongitude(longitude);

    double radius = loc.distanceTo(mLocation);
    return radius <= mRadius;
}

@Override
public void someMethod() {
    long now = System.currentTimeMillis();
    if (mLocation == null) {
        return;
    }
    double latitude = mLocation.getLatitude();
    double longitude = mLocation.getLongitude();

    synchronized (mProximitiesEntered) {
        Iterator<ProximityAlert> iterator = mProximityAlerts.values().iterator();
        while (iterator.hasNext()) {
            ProximityAlert alert = iterator.next();
            long expiration = alert.getExpiration();
            if ((expiration == -1) || (now <= expiration)) {
                boolean entered = mProximitiesEntered.contains(alert);
                boolean inProximity = alert.isInProximity(latitude, longitude);

                if (!entered && inProximity) {
                    if (LOCAL_LOGV) {
                        Log.v(TAG, "Entered alert");
                    }
                    mProximitiesEntered.add(alert);
                    // Trigger the alert (e.g., send intent)
                } else if (entered && !inProximity) {
                    if (LOCAL_LOGV) {
                        Log.v(TAG, "Exited alert");
                    }
                    mProximitiesEntered.remove(alert);
                    // Handle the exit (e.g., send intent)
                }
            } else {
                iterator.remove(); // Remove expired alerts
            }
        }
    }
}
//<End of snippet n. 0>