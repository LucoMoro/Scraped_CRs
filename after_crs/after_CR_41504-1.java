/*BatteryService: Shut down when battery is discharging till 0% with a cable connected.

If the platform consumes more power than the charger could supply and
if the capacity is zero, then shutdown the platform gracefully.
We do this by implementing a function in Battery service that checks
if the battery is discharging. This function will be called whenever the
battery driver sends uevent notification while capacity is zero. The
battery driver sends uevent notification periodically. For example, Intel
MSIC charger driver sends uevent notification every 60 seconds.

Change-Id:Ie217ed63445d6a47d7ace9e80e05463661594cf0Author: Pavan Kumar S <pavan.kumar.s@intel.com>
Signed-off-by: Pavan Kumar S <pavan.kumar.s@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 47466, 13795*/




//Synthetic comment -- diff --git a/services/java/com/android/server/BatteryService.java b/services/java/com/android/server/BatteryService.java
//Synthetic comment -- index ab9ae69..4387b16 100644

//Synthetic comment -- @@ -125,6 +125,11 @@

private boolean mSentLowBatteryBroadcast = false;

    // Variables used to check if battery is discharging, by taking voltage samples
    private static int dischargeCount = 0;
    private static int voltPrev = -1;
    private static final int MAX_DISCHARGE_COUNT = 3;

public BatteryService(Context context, LightsService lights) {
mContext = context;
mLed = new Led(context, lights);
//Synthetic comment -- @@ -148,6 +153,19 @@
update();
}

    private final boolean isDischarging() {
        // If the voltage is dropping during consecutive readings, report that battery is discharging.
        // Reset the discharge count if the current voltage is greater than the previous voltage
        if ((mBatteryVoltage < voltPrev) && (voltPrev != -1)) {
            dischargeCount++;
        } else if (mBatteryVoltage > voltPrev) {
            dischargeCount = 0;
        }

        voltPrev = mBatteryVoltage;
        return (dischargeCount >= MAX_DISCHARGE_COUNT);
    }

final boolean isPowered() {
// assume we are powered if battery state is unknown so the "stay on while plugged in" option will work.
return (mAcOnline || mUsbOnline || mBatteryStatus == BatteryManager.BATTERY_STATUS_UNKNOWN);
//Synthetic comment -- @@ -206,9 +224,10 @@
}

private final void shutdownIfNoPower() {
        // shut down gracefully if our battery is critically low and we are not powered, or
        // if platform consuming more than what is being supplied at 0% battery capacity.
// wait until the system has booted before attempting to display the shutdown dialog.
        if (mBatteryLevel == 0 && (!isPowered() || isDischarging()) && ActivityManagerNative.isSystemReady()) {
Intent intent = new Intent(Intent.ACTION_REQUEST_SHUTDOWN);
intent.putExtra(Intent.EXTRA_KEY_CONFIRM, false);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);







