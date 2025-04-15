/*Follow Android-4.0-cdd Device Administration, if the device do not support ScreenLock, the device respond to the ACTION_ADD_DEVICE_ADMIN intent,but the resultCode is not RES so this test case should set PASSE button enable.*/




//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/admin/ScreenLockTestActivity.java b/apps/CtsVerifier/src/com/android/cts/verifier/admin/ScreenLockTestActivity.java
//Synthetic comment -- index 5520bb7..eb223c9 100644

//Synthetic comment -- @@ -89,7 +89,9 @@
private void handleAddDeviceAdminResult(int resultCode, Intent data) {
if (resultCode == RESULT_OK) {
mDevicePolicyManager.lockNow();
        }else{
	    getPassButton().setEnabled(true);
	}
}

@Override







