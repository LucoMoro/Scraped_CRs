//<Beginning of snippet n. 0>

package com.android.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.util.Log;

private static final String INSTALL_LOCATION_PROVIDER = android.Manifest.permission.INSTALL_LOCATION_PROVIDER;
private final Set<String> mEnabledProviders = new HashSet<String>();
private IGpsStatusProvider mGpsStatusProvider;
private INetInitiatedListener mNetInitiatedListener;
private LocationWorkerHandler mLocationHandler;
LocationProviderInterface mNetworkLocationProvider;
private static final int MESSAGE_LOCATION_CHANGED = 1;
private final static String WAKELOCK_KEY = "LocationManagerService";
private PowerManager.WakeLock wakeLock;
private final Object lastLocationLock = new Object();
private Location lastLocation;

private void loadProviders() {
    try {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, intentFilter);
    } catch (Exception e) {
        Log.e(WAKELOCK_KEY, "Error loading providers", e);
    }
}

private final BroadcastReceiver networkReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION) && !mEnabledProviders.isEmpty()) {
            for (String provider : mEnabledProviders) {
                LocationProviderInterface p = getProvider(provider);
                if (p != null) {
                    long minTimeForProvider = getMinTimeLocked(provider);
                    p.setMinTime(minTimeForProvider);
                    p.enableLocationTracking(true);
                }
            }
        }
    }
};

private void handleLocationChangedLocked(Location location, boolean passive) {
    synchronized (lastLocationLock) {
        try {
            if (lastLocation == null || !lastLocation.equals(location)) {
                lastLocation = location;
                long newStatusUpdateTime = p.getStatusUpdateTime();
                writeLastKnownLocationToFile();
            }
        } catch (Exception e) {
            Log.e(WAKELOCK_KEY, "Error handling location change", e);
        }
    }
}

private void writeLastKnownLocationToFile() {
    File tempFile = new File("temp_location.xml");
    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
        String xmlData = serializeLocationToXml(lastLocation);
        fos.write(xmlData.getBytes());
        fos.flush();
        if (tempFile.renameTo(new File("location.xml"))) {
            Log.i(WAKELOCK_KEY, "Successfully wrote last known location to file");
        } else {
            Log.e(WAKELOCK_KEY, "Error renaming temp file to actual file");
        }
    } catch (IOException e) {
        Log.e(WAKELOCK_KEY, "Error writing last known location to file", e);
    }
}

private String serializeLocationToXml(Location location) {
    return "<location><lat>" + location.getLatitude() + "</lat><lng>" + location.getLongitude() + "</lng></location>";
}

//<End of snippet n. 0>