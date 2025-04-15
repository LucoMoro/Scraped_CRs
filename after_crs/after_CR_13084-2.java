/*Use isLocationProviderEnabled to test for enabled providers

LocationManagerService was just checking if the string of (comma-separated)
Location Providers contained the provider we were interested in.  This works
fine in normal cases, but breaks if we add a provider such as test_network.
Enabling test_network causes LocationManagerService to think that the network
provider is also enabled.

The code in Settings.Secure.isLocationProviderEnabled() checks for the commas
in the string as well, to make sure that a provider name which is a substring
of another provider name won't cause problems.  It also centralizes the code
which reads the string.

Signed-off-by: Brad Larson <brad.larson@garmin.com>

Change-Id:I00dfe7c2b09739ed4c8ed07c6167e409b0bf7d13*/




//Synthetic comment -- diff --git a/services/java/com/android/server/LocationManagerService.java b/services/java/com/android/server/LocationManagerService.java
//Synthetic comment -- index 024d8da..e39da8c 100644

//Synthetic comment -- @@ -552,10 +552,8 @@
}
// Use system settings
ContentResolver resolver = mContext.getContentResolver();

        return Settings.Secure.isLocationProviderEnabled(resolver, provider);
}

private void checkPermissionsSafe(String provider) {







