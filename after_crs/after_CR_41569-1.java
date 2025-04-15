/*Bluetooth: dicrease dbus load and fix discoverable mode display and timeout issue

Stress test hqve been done by sending
multiple files of BT OPP without any failure.

BT discoverable mode display issue was caused by the lack of a
mechanism to reset the discoverable flag in the BluetoothService
once the discoverable time window had elapsed.
Besides the display issue, this was also preventing the discoverabily
feature to be re-activated after the first time.

Fixed the initial value of the DiscoverableTimeout variable, that was set to 0.
As a consequence, if the first change after boot was to set the timeout to 0,
the comparison of the new value with the previous one resulted false and the
framework didn't send the new discoverable timeout property to the BT device,
that continued working with the old timeout instead.

Change-Id:Ic8d0b846d5e1e63aced7bd2115f05c08e1715641Author: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Christophe Bransiec<christophex.bransiec@intel.com>
Signed-off-by: Raffaele Aquilone<raffaelex.aquilone@intel.com>
Signed-off-by: Sebastien Cayetanot<sebastien.cayetanot@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 6106 17106, 10941, 12252 18620 17106, 21005*/




//Synthetic comment -- diff --git a/core/java/android/server/BluetoothEventLoop.java b/core/java/android/server/BluetoothEventLoop.java
//Synthetic comment -- index b758e7fa..dd8a49f 100644

//Synthetic comment -- @@ -327,6 +327,8 @@

if (name.equals("Discoverable")) {
mBluetoothState.sendMessage(BluetoothAdapterStateMachine.SCAN_MODE_CHANGED);
                boolean b = propValues[1].equals("true") ? true : false;
                mBluetoothService.setDiscoverable(b);
}

String pairable = name.equals("Pairable") ? propValues[1] :








//Synthetic comment -- diff --git a/core/java/android/server/BluetoothService.java b/core/java/android/server/BluetoothService.java
//Synthetic comment -- index 6296b11..c5bac88 100755

//Synthetic comment -- @@ -802,10 +802,23 @@
*
* @param timeout The discoverable timeout in seconds.
*/
    private int mTimeout = 1;
    private boolean mPairable = false;
    private boolean mDiscoverable = false;

    synchronized void setDiscoverable(boolean discoverable) {
        mDiscoverable = discoverable;
    }

public synchronized boolean setDiscoverableTimeout(int timeout) {
mContext.enforceCallingOrSelfPermission(BLUETOOTH_ADMIN_PERM,
"Need BLUETOOTH_ADMIN permission");
        if(mTimeout != timeout){
            mTimeout = timeout;
            return setPropertyInteger("DiscoverableTimeout", timeout);
        }
        else
            return true;
}

public synchronized boolean setScanMode(int mode, int duration) {
//Synthetic comment -- @@ -832,9 +845,16 @@
Log.w(TAG, "Requested invalid scan mode " + mode);
return false;
}
        if(mPairable != pairable){
            mPairable = pairable;
            setPropertyBoolean("Pairable", pairable);
        }
        synchronized(this) {
            if (mDiscoverable != discoverable) {
                mDiscoverable = discoverable;
                setPropertyBoolean("Discoverable", discoverable);
            }
        };
return true;
}








