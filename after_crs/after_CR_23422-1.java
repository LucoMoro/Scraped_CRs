/*dont check the gps if device do not have

Change-Id:I7f423e6d0f73874764b0c4d66105f1e8b6b0fac2*/




//Synthetic comment -- diff --git a/tests/tests/location/src/android/location/cts/LocationManagerTest.java b/tests/tests/location/src/android/location/cts/LocationManagerTest.java
//Synthetic comment -- index 48cdade..0048619 100755

//Synthetic comment -- @@ -186,7 +186,7 @@
assertTrue(providers.size() >= 2);
assertTrue(hasTestProvider(providers));

        //assertTrue(hasGpsProvider(providers));

int oldSizeAllProviders = providers.size();

//Synthetic comment -- @@ -242,8 +242,10 @@
assertEquals(TEST_MOCK_PROVIDER_NAME, p.getName());

p = mManager.getProvider(LocationManager.GPS_PROVIDER);
        //assertNotNull(p);
        if(p!=null){
            assertEquals(LocationManager.GPS_PROVIDER, p.getName());
        }

p = mManager.getProvider(UNKNOWN_PROVIDER_NAME);
assertNull(p);








//Synthetic comment -- diff --git a/tests/tests/location/src/android/location/cts/LocationProviderTest.java b/tests/tests/location/src/android/location/cts/LocationProviderTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 3625684..8dd34b2

//Synthetic comment -- @@ -45,7 +45,10 @@
public void testGetName() {
String name = "gps";
LocationProvider locationProvider = mLocationManager.getProvider(name);
        
        if(locationProvider!=null){
            assertEquals(name, locationProvider.getName());
        }
}

@TestTargetNew(
//Synthetic comment -- @@ -56,9 +59,11 @@
public void testMeetsCriteria() {
LocationProvider locationProvider = mLocationManager.getProvider("gps");

        if(locationProvider!=null){
            Criteria criteria = new Criteria();
            criteria.setAltitudeRequired(true);
            criteria.setBearingRequired(true);
            assertTrue(locationProvider.meetsCriteria(criteria));
        }
}
}







