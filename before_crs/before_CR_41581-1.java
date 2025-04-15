/*[BT]: fixes null pointer issue in BluetoothService

This null dereference issue was detected by Klockwork.
The fix consists in adding a sanity check.

Change-Id:Ia295892e373bae3c69a12386233ba5f71cd3bd4cAuthor: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Ovidiu Beldie <ovidiux.beldie@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 34725*/
//Synthetic comment -- diff --git a/core/java/android/server/BluetoothService.java b/core/java/android/server/BluetoothService.java
//Synthetic comment -- index 6296b11..c521e12 100755

//Synthetic comment -- @@ -645,13 +645,10 @@
/*package*/ void initBluetoothAfterTurningOn() {
String discoverable = getProperty("Discoverable", false);
String timeout = getProperty("DiscoverableTimeout", false);
        if (timeout == null) {
            Log.w(TAG, "Null DiscoverableTimeout property");
            // assign a number, anything not 0
            timeout = "1";
        }
        if (discoverable.equals("true") && Integer.valueOf(timeout) != 0) {
            setAdapterPropertyBooleanNative("Discoverable", 0);
}
mBondState.initBondState();
initProfileState();







