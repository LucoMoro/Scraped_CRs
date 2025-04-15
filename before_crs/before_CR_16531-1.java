/*LocationManagerService: Persist LastKnownLocations

Updated LocationManagerService to check every 30 seconds while there is a
listener registered for an active location provider.  If any
lastKnownLocations have changed it writes them out to an xml file.

On startup it initializes the last known locations for the
providers from the .xml file it has written.

Change-Id:I15a47201f6f87347350344c7e9b740c2a2489614*/
//Synthetic comment -- diff --git a/services/java/com/android/server/LocationManagerService.java b/services/java/com/android/server/LocationManagerService.java
//Synthetic comment -- index 65f4194..7ee2e9c 100644

//Synthetic comment -- @@ -16,7 +16,13 @@

package com.android.server;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
//Synthetic comment -- @@ -67,6 +73,7 @@
import android.util.Log;
import android.util.Slog;
import android.util.PrintWriterPrinter;

import com.android.internal.location.GeocoderProxy;
import com.android.internal.location.GpsLocationProvider;
//Synthetic comment -- @@ -74,6 +81,11 @@
import com.android.internal.location.LocationProviderProxy;
import com.android.internal.location.MockProvider;
import com.android.internal.location.PassiveProvider;

/**
* The service class that manages LocationProviders and issues location
//Synthetic comment -- @@ -99,6 +111,21 @@
private static final String INSTALL_LOCATION_PROVIDER =
android.Manifest.permission.INSTALL_LOCATION_PROVIDER;

// Set of providers that are explicitly enabled
private final Set<String> mEnabledProviders = new HashSet<String>();

//Synthetic comment -- @@ -115,6 +142,7 @@
private IGpsStatusProvider mGpsStatusProvider;
private INetInitiatedListener mNetInitiatedListener;
private LocationWorkerHandler mLocationHandler;

// Cache the real providers for use in addTestProvider() and removeTestProvider()
LocationProviderInterface mNetworkLocationProvider;
//Synthetic comment -- @@ -122,6 +150,7 @@

// Handler messages
private static final int MESSAGE_LOCATION_CHANGED = 1;

// wakelock variables
private final static String WAKELOCK_KEY = "LocationManagerService";
//Synthetic comment -- @@ -505,6 +534,11 @@
// Load providers
loadProviders();

// Register for Network (Wifi or Mobile) updates
IntentFilter intentFilter = new IntentFilter();
intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//Synthetic comment -- @@ -693,6 +727,9 @@
if (listeners > 0) {
p.setMinTime(getMinTimeLocked(provider));
p.enableLocationTracking(true);
}
} else {
p.enableLocationTracking(false);
//Synthetic comment -- @@ -882,6 +919,9 @@
long minTimeForProvider = getMinTimeLocked(provider);
p.setMinTime(minTimeForProvider);
p.enableLocationTracking(true);
} else {
// Notify the listener that updates are currently disabled
receiver.callProviderEnabledLocked(provider, false);
//Synthetic comment -- @@ -1471,6 +1511,8 @@
lastLocation.set(location);
}

// Fetch latest status update time
long newStatusUpdateTime = p.getStatusUpdateTime();

//Synthetic comment -- @@ -1554,6 +1596,16 @@
handleLocationChangedLocked(location, passive);
}
}
}
} catch (Exception e) {
// Log, don't crash!
//Synthetic comment -- @@ -1965,4 +2017,188 @@
}
}
}
}







