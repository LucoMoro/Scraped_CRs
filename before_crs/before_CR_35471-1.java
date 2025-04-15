/*Fix Force Close in PackageManagerService

There is a coding problem in PackageManagerService which removes
an element from a list while an iterator is accessing this list.

Change-Id:I1a649f2e636d059b88054c4f3822d964402e68e4*/
//Synthetic comment -- diff --git a/services/java/com/android/server/pm/PackageManagerService.java b/services/java/com/android/server/pm/PackageManagerService.java
//Synthetic comment -- index 6b61c47..c5b5582 100644

//Synthetic comment -- @@ -516,8 +516,10 @@
if (mContainerService == null) {
// Something seriously wrong. Bail out
Slog.e(TAG, "Cannot bind to media container service");
                        for (HandlerParams params : mPendingInstalls) {
                            mPendingInstalls.remove(0);
// Indicate service bind error
params.serviceError();
}
//Synthetic comment -- @@ -568,8 +570,10 @@
}
if (!connectToService()) {
Slog.e(TAG, "Failed to bind to media container service");
                            for (HandlerParams params : mPendingInstalls) {
                                mPendingInstalls.remove(0);
// Indicate service bind error
params.serviceError();
}







