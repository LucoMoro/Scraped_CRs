/*[BT] : fixed crash when renaming a remote (paired) device

The crashed occured because of a segmentation fault due to a NULL
pointer passed as an argument to a DBUS call. The fix consists in
preventing the call if the argument is NULL.

Change-Id:I8c8af4baeac2af927a38644c9da63cea395f304eAuthor: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Ovidiu Beldie <ovidiux.beldie@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 30119*/




//Synthetic comment -- diff --git a/core/java/android/server/BluetoothService.java b/core/java/android/server/BluetoothService.java
//Synthetic comment -- index 6296b11..d3c1122 100755

//Synthetic comment -- @@ -971,8 +971,12 @@
if (!BluetoothAdapter.checkBluetoothAddress(address)) {
return false;
}
        String objPath = getObjectPathFromAddress(address);
        if (objPath == null) {
            return false;
        }

        return setDevicePropertyStringNative(objPath,
"Alias", alias);
}








