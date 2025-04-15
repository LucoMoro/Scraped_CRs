/*Wrong parameter in LocationManagerService

Fix wrong parameter in removeTestProvider

Change-Id:Ibf93833697c865904f29821e5778853127e5fb00Signed-off-by: You Kim <you.kim72@gmail.com>*/




//Synthetic comment -- diff --git a/services/java/com/android/server/LocationManagerService.java b/services/java/com/android/server/LocationManagerService.java
//Synthetic comment -- index 89fa6d0..f497b23 100644

//Synthetic comment -- @@ -1884,13 +1884,12 @@
public void removeTestProvider(String provider) {
checkMockPermissionsSafe();
synchronized (mLock) {
            MockProvider mockProvider = mMockProviders.remove(provider);
if (mockProvider == null) {
throw new IllegalArgumentException("Provider \"" + provider + "\" unknown");
}
long identity = Binder.clearCallingIdentity();
removeProviderLocked(mProvidersByName.get(provider));

// reinstate real provider if available
LocationProviderInterface realProvider = mRealProviders.get(provider);







