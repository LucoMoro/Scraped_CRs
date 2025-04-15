/*Fixes for the location manager cts tests

If the gps feature isn't declared on the system, it should not be required to return a gps provider

Change-Id:I1eda27e133d78a384f77b7b2bf09863631479313*/




//Synthetic comment -- diff --git a/tests/tests/location/src/android/location/cts/LocationManagerTest.java b/tests/tests/location/src/android/location/cts/LocationManagerTest.java
//Synthetic comment -- index 48cdade..51623d0 100755

//Synthetic comment -- @@ -27,6 +27,7 @@
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
//Synthetic comment -- @@ -186,7 +187,7 @@
assertTrue(providers.size() >= 2);
assertTrue(hasTestProvider(providers));

        assertEquals(hasGpsFeature(), hasGpsProvider(providers));

int oldSizeAllProviders = providers.size();

//Synthetic comment -- @@ -222,6 +223,11 @@
return hasProvider(providers, LocationManager.GPS_PROVIDER);
}

    private boolean hasGpsFeature() {
        return mContext.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_LOCATION_GPS);
    }

private boolean hasProvider(List<String> providers, String providerName) {
for (String provider : providers) {
if (provider != null && provider.equals(providerName)) {
//Synthetic comment -- @@ -242,8 +248,12 @@
assertEquals(TEST_MOCK_PROVIDER_NAME, p.getName());

p = mManager.getProvider(LocationManager.GPS_PROVIDER);
        if (hasGpsFeature()) {
            assertNotNull(p);
            assertEquals(LocationManager.GPS_PROVIDER, p.getName());
        } else {
            assertNull(p);
        }

p = mManager.getProvider(UNKNOWN_PROVIDER_NAME);
assertNull(p);







