/*Fix Wrong parameter in HashMap.remove

Change-Id:Ibf93833697c865904f29821e5778853127e5fb00Signed-off-by: You Kim <you.kim72@gmail.com>*/




//Synthetic comment -- diff --git a/services/java/com/android/server/LocationManagerService.java b/services/java/com/android/server/LocationManagerService.java
//Synthetic comment -- index 8c1581c..d3f2612 100644

//Synthetic comment -- @@ -2252,7 +2252,7 @@
}
long identity = Binder.clearCallingIdentity();
removeProvider(mProvidersByName.get(provider));
            mMockProviders.remove(provider);
// reinstall real provider if we were mocking GPS or network provider
if (LocationManager.GPS_PROVIDER.equals(provider) &&
mGpsLocationProvider != null) {







