
//<Beginning of snippet n. 0>



package com.android.server;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import android.util.Log;
import android.util.Slog;
import android.util.PrintWriterPrinter;
import android.util.Xml;

import com.android.internal.location.GeocoderProxy;
import com.android.internal.location.GpsLocationProvider;
import com.android.internal.location.LocationProviderProxy;
import com.android.internal.location.MockProvider;
import com.android.internal.location.PassiveProvider;
import com.android.internal.util.FastXmlSerializer;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

/**
* The service class that manages LocationProviders and issues location
private static final String INSTALL_LOCATION_PROVIDER =
android.Manifest.permission.INSTALL_LOCATION_PROVIDER;

    // LastKnownLocation Persist xml variables
    private static final String SETTINGS_FILENAME = "/data/system/LocationManagerService.xml";
    private static final String LOCATIONS_TAG = "locs";
    private static final String PROVIDER_TAG = "p";
    private static final String PROVIDER_ATTR = "pr";
    private static final String LAT_ATTR = "lt";
    private static final String LON_ATTR = "ln";
    private static final String TIM_ATTR = "tm";
    private static final String ACC_ATTR = "ac";
    private static final String ALT_ATTR = "al";
    private static final String BRG_ATTR = "br";
    private static final String SPD_ATTR = "sp";
    private static final String NO_DATA_ATTR_VALUE = "None";
    private static final int MS_BETWEEN_LOC_CACHES = (1000 * 30);

// Set of providers that are explicitly enabled
private final Set<String> mEnabledProviders = new HashSet<String>();

private IGpsStatusProvider mGpsStatusProvider;
private INetInitiatedListener mNetInitiatedListener;
private LocationWorkerHandler mLocationHandler;
    private boolean mLastLocationsChanged = false;

// Cache the real providers for use in addTestProvider() and removeTestProvider()
LocationProviderInterface mNetworkLocationProvider;

// Handler messages
private static final int MESSAGE_LOCATION_CHANGED = 1;
    private static final int MESSAGE_PERSIST_LOCATIONS = 2;

// wakelock variables
private final static String WAKELOCK_KEY = "LocationManagerService";
// Load providers
loadProviders();

        // Collect Persisted lastKnownLocations from the Shared Preferences
        synchronized (mLock) {
            collectPersistedLocationsLocked();
        }

// Register for Network (Wifi or Mobile) updates
IntentFilter intentFilter = new IntentFilter();
intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
if (listeners > 0) {
p.setMinTime(getMinTimeLocked(provider));
p.enableLocationTracking(true);
                if (!mLocationHandler.hasMessages(MESSAGE_PERSIST_LOCATIONS)) {
                    mLocationHandler.sendMessageDelayed(mLocationHandler.obtainMessage(MESSAGE_PERSIST_LOCATIONS), MS_BETWEEN_LOC_CACHES);
                }
}
} else {
p.enableLocationTracking(false);
long minTimeForProvider = getMinTimeLocked(provider);
p.setMinTime(minTimeForProvider);
p.enableLocationTracking(true);
                if (!mLocationHandler.hasMessages(MESSAGE_PERSIST_LOCATIONS)) {
                    mLocationHandler.sendMessageDelayed(mLocationHandler.obtainMessage(MESSAGE_PERSIST_LOCATIONS), MS_BETWEEN_LOC_CACHES);
                }
} else {
// Notify the listener that updates are currently disabled
receiver.callProviderEnabledLocked(provider, false);
lastLocation.set(location);
}

        mLastLocationsChanged = true;

// Fetch latest status update time
long newStatusUpdateTime = p.getStatusUpdateTime();

handleLocationChangedLocked(location, passive);
}
}
                } else if (msg.what == MESSAGE_PERSIST_LOCATIONS) {
                    synchronized (mLock) {
                        if (mLastLocationsChanged) {
                            persistLastKnownLocationsLocked();
                            mLastLocationsChanged = false;
                        }
                        if (hasLocationTrackingProviderLocked()) {
                            sendMessageDelayed(mLocationHandler.obtainMessage(MESSAGE_PERSIST_LOCATIONS), MS_BETWEEN_LOC_CACHES);
                        }
                    }
}
} catch (Exception e) {
// Log, don't crash!
}
}
}

    private boolean hasLocationTrackingProviderLocked() {
        for (final LocationProviderInterface provider : mProviders) {
            if (provider != null && provider.isEnabled()) {
                ArrayList<UpdateRecord> records = mRecordsByProvider.get(provider.getName());
                if (records != null && records.size() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void collectPersistedLocationsLocked() {
        File file = new File(SETTINGS_FILENAME);
        if (file.exists()) {
            readLocationsFromFileLocked(file);
        }
    }

    private void persistLastKnownLocationsLocked() {
        if (mLastKnownLocation.size() == 0) {
            return;
        }
        File file = new File(SETTINGS_FILENAME);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e(TAG, "Error creating persist file " + file, e);
            }
        }

        writeLocationsToFileLocked(file);
    }

    private void writeLocationsToFileLocked(File file) {
        FileOutputStream stream = null;

        try {
            stream = new FileOutputStream(file, false);
            XmlSerializer out = new FastXmlSerializer();
            out.setOutput(stream, "utf-8");
            out.startDocument(null, true);

            out.startTag(null, LOCATIONS_TAG);

            for (String key : mLastKnownLocation.keySet()) {
                Location location = mLastKnownLocation.get(key);
                Log.d(TAG, "WriteLocationToFile key: " + key + " Location: " + location);

                if ( location != null && location.getProvider() != null ) {
                    out.startTag(null, PROVIDER_TAG);

                    out.attribute(null, PROVIDER_ATTR, location.getProvider());
                    out.attribute(null, LAT_ATTR, Double.toString(location.getLatitude()));
                    out.attribute(null, LON_ATTR, Double.toString(location.getLongitude()));
                    out.attribute(null, TIM_ATTR, Long.toString(location.getTime()));

                    if( location.hasAccuracy() ) {
                        out.attribute(null, ACC_ATTR, Float.toString(location.getAccuracy()));
                    }
                    else {
                        out.attribute(null, ACC_ATTR, NO_DATA_ATTR_VALUE);
                    }

                    if( location.hasAltitude() ) {
                        out.attribute(null, ALT_ATTR, Double.toString(location.getAltitude()));
                    }
                    else {
                        out.attribute(null, ALT_ATTR, NO_DATA_ATTR_VALUE);
                    }

                    if( location.hasBearing() ) {
                        out.attribute(null, BRG_ATTR, Float.toString(location.getBearing()));
                    }
                    else {
                        out.attribute(null, BRG_ATTR, NO_DATA_ATTR_VALUE);
                    }

                    if( location.hasSpeed() ) {
                        out.attribute(null, SPD_ATTR, Float.toString(location.getSpeed()));
                    }
                    else {
                        out.attribute(null, SPD_ATTR, NO_DATA_ATTR_VALUE);
                    }

                    out.endTag(null, PROVIDER_TAG);
                }
            }
            out.endTag(null, LOCATIONS_TAG);
            out.endDocument();
        } catch (IOException e) {
            if (file.exists()) {
                file.delete();
            }
        } finally {
             try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException ex) {
                Log.e(TAG, "Error closing output stream when persisting locations.", ex);
            }
        }
    }

    private void readLocationsFromFileLocked(File file) {
        FileInputStream stream = null;

        boolean success = false;

        try {
            stream = new FileInputStream(file);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(stream, null);

            int type = parser.getEventType();
            int providerIndex = 0;
            while (type != XmlPullParser.END_DOCUMENT) {
                type = parser.next();
                if (type == XmlPullParser.START_TAG) {
                    String tag = parser.getName();
                    if (PROVIDER_TAG.equals(tag)) {
                        String sProvider = parser.getAttributeValue(null, PROVIDER_ATTR);
                        String sLat = parser.getAttributeValue(null, LAT_ATTR);
                        String sLon = parser.getAttributeValue(null, LON_ATTR);
                        String sTim = parser.getAttributeValue(null, TIM_ATTR);
                        String sAcc = parser.getAttributeValue(null, ACC_ATTR);
                        String sAlt = parser.getAttributeValue(null, ALT_ATTR);
                        String sBrg = parser.getAttributeValue(null, BRG_ATTR);
                        String sSpd = parser.getAttributeValue(null, SPD_ATTR);

                        if ((sLat!=null) && (sLon!=null) && (sTim!=null) && (sProvider != null)) {

                            Location location = new Location(sProvider);
                            location.setLatitude(Double.parseDouble(sLat));
                            location.setLongitude(Double.parseDouble(sLon));
                            location.setTime(Long.parseLong(sTim));

                            if( (sAcc!=null) && !sAcc.equals(NO_DATA_ATTR_VALUE) ) {
                                location.setAccuracy(Float.parseFloat(sAcc));
                            }

                            if( (sAlt!=null) && !sAlt.equals(NO_DATA_ATTR_VALUE) ) {
                                location.setAltitude(Double.parseDouble(sAlt));
                            }

                            if( (sBrg!=null) && !sBrg.equals(NO_DATA_ATTR_VALUE) ) {
                                location.setBearing(Float.parseFloat(sBrg));
                            }

                            if( (sSpd!=null) && !sSpd.equals(NO_DATA_ATTR_VALUE) ) {
                                location.setSpeed(Float.parseFloat(sSpd));
                            }

                            mLastKnownLocation.put(sProvider, location);
                        }
                    }
                }
                type = parser.getEventType();
            }
            success = true;
        } catch (NullPointerException e) {
            Log.w(TAG, "failed parsing " + file, e);
        } catch (NumberFormatException e) {
            Log.w(TAG, "failed parsing " + file, e);
        } catch (XmlPullParserException e) {
            Log.w(TAG, "failed parsing " + file, e);
        } catch (IOException e) {
            Log.w(TAG, "failed parsing " + file, e);
        } catch (IndexOutOfBoundsException e) {
            Log.w(TAG, "failed parsing " + file, e);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error closing input stream when reading persisted locations." , e);
            }
        }
    }
}

//<End of snippet n. 0>








