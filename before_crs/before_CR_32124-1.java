/*ConnectivityService: Fix to facilitate GC of FeatureUser

Change-Id:I72416864367469411b2aef21e362a6147d63a25c*/
//Synthetic comment -- diff --git a/services/java/com/android/server/ConnectivityService.java b/services/java/com/android/server/ConnectivityService.java
//Synthetic comment -- index 6e4aca7..7509ea5 100644

//Synthetic comment -- @@ -903,7 +903,7 @@
private class FeatureUser implements IBinder.DeathRecipient {
int mNetworkType;
String mFeature;
        IBinder mBinder;
int mPid;
int mUid;
long mCreateTime;







