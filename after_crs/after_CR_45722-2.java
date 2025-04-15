/*Fix Wrong parameter in HashMap.remove

Change-Id:Ibf93833697c865904f29821e5778853127e5fb00Signed-off-by: You Kim <you.kim72@gmail.com>*/




//Synthetic comment -- diff --git a/services/java/com/android/server/LocationManagerService.java b/services/java/com/android/server/LocationManagerService.java
//Synthetic comment -- index 8c1581c..0063d30 100644

//Synthetic comment -- @@ -2246,13 +2246,12 @@
public void removeTestProvider(String provider) {
checkMockPermissionsSafe();
synchronized (mLock) {
            MockProvider mockProvider = mMockProviders.remove(provider);
if (mockProvider == null) {
throw new IllegalArgumentException("Provider \"" + provider + "\" unknown");
}
long identity = Binder.clearCallingIdentity();
removeProvider(mProvidersByName.get(provider));
// reinstall real provider if we were mocking GPS or network provider
if (LocationManager.GPS_PROVIDER.equals(provider) &&
mGpsLocationProvider != null) {







