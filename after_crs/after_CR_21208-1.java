/*Added sleep within testGetAndSetMasterSyncAutomatically.

In this test case, after testGetAndSetMasterSyncAutomatically is done,
testGetAndSetSyncAutomatically is executed.

However, I think that doDatabaseCleanup within testGetAndSetMasterSyncAutomatically is sometimes called immediately after setSyncAutomatically(ACCOUNT, AUTHORITY, true) within testGetAndSetSyncAutomatically
(I understand that doDatabaseCleanup is triggered by setSyncAutomatically(ACCOUNT, AUTHORITY, true) within testGetAndSetMasterSyncAutomatically).

If this has occurred, account.name and account.authority are cleared, which cause getSyncAutomatically(ACCOUNT, AUTHORITY) to be false, resulting the test case to be failed.

The solution to this is to add sleep within testGetAndSetMasterSyncAutomatically so that doDatabaseCleanup is finished before calling testGetAndSetSyncAutomatically.*/




//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/ContentResolverSyncTestCase.java b/tests/tests/content/src/android/content/cts/ContentResolverSyncTestCase.java
//Synthetic comment -- index 032f10a..af3a54d 100644

//Synthetic comment -- @@ -217,6 +217,7 @@

ContentResolver.setMasterSyncAutomatically(false);
assertEquals(false, ContentResolver.getMasterSyncAutomatically());
        Thread.sleep(3000);
}

/**







