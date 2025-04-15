/*close bluetooth headset proxy when bluetooth is turned off

BluetoothService and BatteryStatsService get BluetoothHeadset profile proxy
when bluetooth is turned on. Profile proxy is not closed when bluetooth is
turned off, which is causing resource leak.
So close BluetoothHeadset profile proxy from BluetoothService and
BatteryStatsService when bluetooth is turned off

bug 27307

Signed-off-by: Nagarjuna Kristam <nkristam@nvidia.com>*/




//Synthetic comment -- diff --git a/core/java/android/server/BluetoothService.java b/core/java/android/server/BluetoothService.java
old mode 100755
new mode 100644
//Synthetic comment -- index 6296b11..0f37622

//Synthetic comment -- @@ -426,6 +426,7 @@
* Local clean up after broadcasting STATE_OFF intent
*/
synchronized void cleanupAfterFinishDisable() {
        mAdapter.closeProfileProxy(BluetoothProfile.HEADSET,mHeadsetProxy);
mAdapterProperties.clear();

for (Integer srHandle : mServiceRecordToPid.keySet()) {








//Synthetic comment -- diff --git a/services/java/com/android/server/am/BatteryStatsService.java b/services/java/com/android/server/am/BatteryStatsService.java
//Synthetic comment -- index 8f797ec..6fd8e667 100644

//Synthetic comment -- @@ -327,6 +327,11 @@

public void noteBluetoothOff() {
enforceCallingPermission();
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            adapter.closeProfileProxy(BluetoothProfile.HEADSET,
                                      mBluetoothHeadset);
        }
synchronized (mStats) {
mBluetoothPendingStats = false;
mStats.noteBluetoothOffLocked();







