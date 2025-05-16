
//<Beginning of snippet n. 0>



package com.android.server;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import android.util.Log;
import android.util.Slog;
import android.util.PrintWriterPrinter;

import com.android.internal.location.GeocoderProxy;
import com.android.internal.location.GpsLocationProvider;
import com.android.internal.location.LocationProviderProxy;
import com.android.internal.location.MockProvider;
import com.android.internal.location.PassiveProvider;

/**
* The service class that manages LocationProviders and issues location
private static final String INSTALL_LOCATION_PROVIDER =
android.Manifest.permission.INSTALL_LOCATION_PROVIDER;

// Set of providers that are explicitly enabled
private final Set<String> mEnabledProviders = new HashSet<String>();

private IGpsStatusProvider mGpsStatusProvider;
private INetInitiatedListener mNetInitiatedListener;
private LocationWorkerHandler mLocationHandler;

// Cache the real providers for use in addTestProvider() and removeTestProvider()
LocationProviderInterface mNetworkLocationProvider;

// Handler messages
private static final int MESSAGE_LOCATION_CHANGED = 1;

// wakelock variables
private final static String WAKELOCK_KEY = "LocationManagerService";
// Load providers
loadProviders();

// Register for Network (Wifi or Mobile) updates
IntentFilter intentFilter = new IntentFilter();
intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
if (listeners > 0) {
p.setMinTime(getMinTimeLocked(provider));
p.enableLocationTracking(true);
}
} else {
p.enableLocationTracking(false);
long minTimeForProvider = getMinTimeLocked(provider);
p.setMinTime(minTimeForProvider);
p.enableLocationTracking(true);
} else {
// Notify the listener that updates are currently disabled
receiver.callProviderEnabledLocked(provider, false);
lastLocation.set(location);
}

// Fetch latest status update time
long newStatusUpdateTime = p.getStatusUpdateTime();

handleLocationChangedLocked(location, passive);
}
}
}
} catch (Exception e) {
// Log, don't crash!
}
}
}
}

//<End of snippet n. 0>








