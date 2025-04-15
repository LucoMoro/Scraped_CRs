/*Don't get a location provider if the device doesn't support GPS

If the device doesn't support GPS, it is impossible to get a location
provider. So before getting location provider, confirm whether the
device support GPS or not.

Change-Id:Ie7f23fd299779bda0002861268411cc79cc42b7e*/




//Synthetic comment -- diff --git a/tests/tests/location/src/android/location/cts/LocationManagerTest.java b/tests/tests/location/src/android/location/cts/LocationManagerTest.java
//Synthetic comment -- index 48cdade..c976610 100755

//Synthetic comment -- @@ -27,6 +27,8 @@
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
//Synthetic comment -- @@ -40,6 +42,7 @@
import android.provider.Settings;
import android.test.InstrumentationTestCase;

import java.util.HashSet;
import java.util.List;

/**
//Synthetic comment -- @@ -65,10 +68,30 @@

private TestIntentReceiver mIntentReceiver;

    private PackageManager mPackageManager;

    private HashSet<String> mAvailableFeatures;

    private boolean isSupport;

@Override
protected void setUp() throws Exception {
super.setUp();
mContext = getInstrumentation().getTargetContext();
        mPackageManager = mContext.getPackageManager();
        mAvailableFeatures = new HashSet<String>();
        
        if (mPackageManager.getSystemAvailableFeatures() != null) {
            for (FeatureInfo feature : mPackageManager.getSystemAvailableFeatures()) {
                mAvailableFeatures.add(feature.name);
            }
        }
        if (mPackageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS) &&
            mAvailableFeatures.contains(PackageManager.FEATURE_LOCATION_GPS)) {
                isSupport = true;
        } else {
            isSupport = false;
        }

mManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

//Synthetic comment -- @@ -183,10 +206,16 @@
})
public void testGetProviders() {
List<String> providers = mManager.getAllProviders();
        if (isSupport) {
            assertTrue(providers.size() >= 2);
        } else {
            assertTrue(providers.size() >= 1);
        }
assertTrue(hasTestProvider(providers));

        if (isSupport) {
            assertTrue(hasGpsProvider(providers));
        }

int oldSizeAllProviders = providers.size();

//Synthetic comment -- @@ -241,9 +270,11 @@
assertNotNull(p);
assertEquals(TEST_MOCK_PROVIDER_NAME, p.getName());

        if (isSupport) {
            p = mManager.getProvider(LocationManager.GPS_PROVIDER);
            assertNotNull(p);
            assertEquals(LocationManager.GPS_PROVIDER, p.getName());
        }

p = mManager.getProvider(UNKNOWN_PROVIDER_NAME);
assertNull(p);








//Synthetic comment -- diff --git a/tests/tests/location/src/android/location/cts/LocationProviderTest.java b/tests/tests/location/src/android/location/cts/LocationProviderTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 3625684..f91dea8

//Synthetic comment -- @@ -21,20 +21,42 @@
import dalvik.annotation.TestTargetNew;

import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.test.AndroidTestCase;

import java.util.HashSet;

@TestTargetClass(LocationProvider.class)
public class LocationProviderTest extends AndroidTestCase {
private LocationManager mLocationManager;
    private PackageManager mPackageManager;
    private HashSet<String> mAvailableFeatures;

    private boolean isSupport;

@Override
protected void setUp() throws Exception {
super.setUp();
mLocationManager =
(LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        mPackageManager = mContext.getPackageManager();
        mAvailableFeatures = new HashSet<String>();

        if (mPackageManager.getSystemAvailableFeatures() != null) {
            for (FeatureInfo feature : mPackageManager.getSystemAvailableFeatures()) {
                mAvailableFeatures.add(feature.name);
            }
        }
        if (mPackageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS) &&
            mAvailableFeatures.contains(PackageManager.FEATURE_LOCATION_GPS)) {
                isSupport = true;
        } else {
            isSupport = false;
        }
}

@TestTargetNew(
//Synthetic comment -- @@ -44,8 +66,10 @@
)
public void testGetName() {
String name = "gps";
        if (isSupport) {
            LocationProvider locationProvider = mLocationManager.getProvider(name);
            assertEquals(name, locationProvider.getName());
        }
}

@TestTargetNew(
//Synthetic comment -- @@ -54,11 +78,13 @@
args = {android.location.Criteria.class}
)
public void testMeetsCriteria() {
        if (isSupport) {
            LocationProvider locationProvider = mLocationManager.getProvider("gps");

            Criteria criteria = new Criteria();
            criteria.setAltitudeRequired(true);
            criteria.setBearingRequired(true);
            assertTrue(locationProvider.meetsCriteria(criteria));
        }
}
}







