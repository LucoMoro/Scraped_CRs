/*Add polling mechanism to Battery service

VirtualBox ACPI implementation doesn't send ACPI events on battery
level changes and on charger plug/unplug.
That's why I propose to implement a polling mechanism which checks
battery informations every x seconds.
This patch uses 2 system properties (status.battery.polling and
status.battery.polling_freq) to enable polling and set polling freq.
I'm not sure I chose the right properties name - please let me know.

Change-Id:Ieec20f7f0b76122836d67343521c0759e4497dd9Signed-off-by: Daniel Fages <daniel@fages.com>*/
//Synthetic comment -- diff --git a/services/java/com/android/server/BatteryService.java b/services/java/com/android/server/BatteryService.java
//Synthetic comment -- index ab9ae69..2ade903 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UEventObserver;
import android.provider.Settings;
import android.util.EventLog;
import android.util.Slog;
//Synthetic comment -- @@ -138,6 +139,9 @@
com.android.internal.R.integer.config_lowBatteryCloseWarningLevel);

mPowerSupplyObserver.startObserving("SUBSYSTEM=power_supply");

// watch for invalid charger messages if the invalid_charger switch exists
if (new File("/sys/devices/virtual/switch/invalid_charger/state").exists()) {
//Synthetic comment -- @@ -194,6 +198,23 @@
}
};

// returns battery level as a percentage
final int getBatteryLevel() {
return mBatteryLevel;







