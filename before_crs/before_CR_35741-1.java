/*Backup and restore original timeout

After this test case, the display timeout will be changed to 5s.
It would cause some unpredictable failures. So let's backup the original
timeout value first, then restore it after the test cases completes.

Change-Id:I331828055554d764e27e0f14b7eabead3a814d30*/
//Synthetic comment -- diff --git a/tests/tests/admin/src/android/admin/cts/DevicePolicyManagerTest.java b/tests/tests/admin/src/android/admin/cts/DevicePolicyManagerTest.java
//Synthetic comment -- index af47299..e5df794 100644

//Synthetic comment -- @@ -65,11 +65,15 @@
}

public void testGetMaximumTimeToLock() {
mDevicePolicyManager.setMaximumTimeToLock(mComponent, 3000);
assertEquals(3000, mDevicePolicyManager.getMaximumTimeToLock(mComponent));

mDevicePolicyManager.setMaximumTimeToLock(mComponent, 5000);
assertEquals(5000, mDevicePolicyManager.getMaximumTimeToLock(mComponent));
}

public void testGetMaximumFailedPasswordsForWipe() {







