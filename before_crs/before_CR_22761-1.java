/*Fix OBB tests to stop caring about implementation

The OBB tests were using the OBB API in a way that the documentation
doesn't recommend. Instead of assuming that we already have the
canonical path to an OBB, get the canonical path that is returned in the
observer instead.

Bug: 4371021
Bug: 4364321
Change-Id:Ic4ccd51cc0e173ccd07223508caafc45a9d13e37*/
//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/storage/cts/StorageManagerTest.java b/tests/tests/os/src/android/os/storage/cts/StorageManagerTest.java
//Synthetic comment -- index b229d03..e23c0b7 100644

//Synthetic comment -- @@ -51,11 +51,11 @@
public void testMountAndUnmountObbNormal() {
final File outFile = getFilePath("test1.obb");

        mountObb(R.raw.test1, outFile, OnObbStateChangeListener.MOUNTED);

mountObb(R.raw.test1, outFile, OnObbStateChangeListener.ERROR_ALREADY_MOUNTED);

        final String mountPath = checkMountedPath(outFile);
final File mountDir = new File(mountPath);

assertTrue("OBB mounted path should be a directory", mountDir.isDirectory());
//Synthetic comment -- @@ -96,15 +96,15 @@
ObbObserver oo2 = mountObbWithoutWait(R.raw.test1, file2);

Log.d(TAG, "Waiting for OBB #1 to complete mount");
        waitForObbActionCompletion(file1, oo1, OnObbStateChangeListener.MOUNTED, false);
Log.d(TAG, "Waiting for OBB #2 to complete mount");
        waitForObbActionCompletion(file2, oo2, OnObbStateChangeListener.MOUNTED, false);

        final String mountPath1 = checkMountedPath(file1);
final File mountDir1 = new File(mountPath1);
assertTrue("OBB mounted path should be a directory", mountDir1.isDirectory());

        final String mountPath2 = checkMountedPath(file2);
final File mountDir2 = new File(mountPath2);
assertTrue("OBB mounted path should be a directory", mountDir2.isDirectory());

//Synthetic comment -- @@ -187,7 +187,7 @@
| FileUtils.S_IRWXO);
}

    private void mountObb(final int resource, final File file, int expectedState) {
copyRawToFile(resource, file);

final ObbObserver observer = new ObbObserver();
//Synthetic comment -- @@ -198,13 +198,12 @@
observer.waitForCompletion());

if (expectedState == OnObbStateChangeListener.MOUNTED) {
            assertTrue("OBB should be mounted", mStorageManager.isObbMounted(file.getPath()));
}

        assertEquals("Actual file and resolved file should be the same",
                file.getPath(), observer.getPath());

assertEquals(expectedState, observer.getState());
}

private ObbObserver mountObbWithoutWait(final int resource, final File file) {
//Synthetic comment -- @@ -217,22 +216,17 @@
return observer;
}

    private void waitForObbActionCompletion(final File file,
            final ObbObserver observer, int expectedState, boolean checkPath) {
assertTrue("Mount should have completed", observer.waitForCompletion());

        assertTrue("OBB should be mounted", mStorageManager.isObbMounted(file.getPath()));

        if (checkPath) {
            assertEquals("Actual file and resolved file should be the same", file.getPath(),
                    observer.getPath());
        }

assertEquals(expectedState, observer.getState());
}

    private String checkMountedPath(final File file) {
        final String mountPath = mStorageManager.getMountedObbPath(file.getPath());
assertStartsWith("Path should be in " + OBB_MOUNT_PREFIX,
OBB_MOUNT_PREFIX,
mountPath);







