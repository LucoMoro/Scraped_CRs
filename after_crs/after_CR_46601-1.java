/*Fixed a leaked partial wakelock in AbstractThreadedSyncAdapter.

This fix ensures that onFinished() is always called in a syncClient
even if the application lacks the READ_SYNC_SETTINGS permission.

Change-Id:I944717e71ceae06a665f8a3b1199b41d73e12da4*/




//Synthetic comment -- diff --git a/core/java/android/content/AbstractThreadedSyncAdapter.java b/core/java/android/content/AbstractThreadedSyncAdapter.java
//Synthetic comment -- index bafe67d..898cc4e 100644

//Synthetic comment -- @@ -160,10 +160,13 @@
if (mAutoInitialize
&& extras != null
&& extras.getBoolean(ContentResolver.SYNC_EXTRAS_INITIALIZE, false)) {
                        try {
                            if (ContentResolver.getIsSyncable(account, authority) < 0) {
                                ContentResolver.setIsSyncable(account, authority, 1);
                            }
                        } finally {
                            syncContextClient.onFinished(new SyncResult());
}
return;
}
SyncThread syncThread = new SyncThread(







