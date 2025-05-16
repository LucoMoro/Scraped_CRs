//<Beginning of snippet n. 0>
return mIntent;
}

boolean isInProximity(double latitude, double longitude) {
    if (mLocation == null) {
        return false; // Ensure mLocation is initialized
    }

    double radius = mLocation.distanceTo(new Location("") {{
        setLatitude(latitude);
        setLongitude(longitude);
    }});
    return radius >= 0 && radius <= mRadius; // Ensure radius is non-negative and within bounds
}

@Override
long now = System.currentTimeMillis();
if (loc != null) { // Ensure loc is not null
    double latitude = loc.getLatitude();
    double longitude = loc.getLongitude();
    synchronized (mProximityAlerts) { // Synchronize access to mProximityAlerts
        ArrayList<PendingIntent> intentsToRemove = null;

        for (ProximityAlert alert : mProximityAlerts.values()) {
            if ((expiration == -1) || (now <= expiration)) {
                boolean entered = mProximitiesEntered.contains(alert);
                boolean inProximity = alert.isInProximity(latitude, longitude);
                if (!entered && inProximity) {
                    if (LOCAL_LOGV) {
                        Log.v(TAG, "Entered alert");
                    }
                    // Additional logic for handling entered alert
                }
            }
        }
    }
}

```