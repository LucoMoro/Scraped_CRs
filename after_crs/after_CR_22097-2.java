/*Converted large if-else blocks to switch statements, seems more readable and
compiler can now convert to a lookup table for speed. Also corrected
some small formatting issues

Removed trailing whitespace
Change-Id:I6ea32a2220c9a20b3b7ff175f8dec17e715f1b4b*/




//Synthetic comment -- diff --git a/src/com/android/settings/BatteryInfo.java b/src/com/android/settings/BatteryInfo.java
//Synthetic comment -- index 2f9d50e..488f76c 100644

//Synthetic comment -- @@ -26,7 +26,6 @@
import android.os.Handler;
import android.os.IPowerManager;
import android.os.Message;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.text.format.DateUtils;
//Synthetic comment -- @@ -93,22 +92,29 @@

int status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
String statusString;
                
                switch(status) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                	statusString = getString(R.string.battery_info_status_charging);
if (plugType > 0) {
statusString = statusString + " " + getString(
(plugType == BatteryManager.BATTERY_PLUGGED_AC)
? R.string.battery_info_status_charging_ac
: R.string.battery_info_status_charging_usb);
}
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                	statusString = getString(R.string.battery_info_status_discharging);
                	break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                	statusString = getString(R.string.battery_info_status_not_charging);
                	break;
                case BatteryManager.BATTERY_STATUS_FULL:
                	statusString = getString(R.string.battery_info_status_full);
                	break;
                default:
                	statusString = getString(R.string.battery_info_status_unknown);
                	break;
}
mStatus.setText(statusString);

//Synthetic comment -- @@ -132,20 +138,28 @@

int health = intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN);
String healthString;
                switch(health) {
	                case BatteryManager.BATTERY_HEALTH_GOOD:
	                	healthString = getString(R.string.battery_info_health_good);
	                	break;
	                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
	                	healthString = getString(R.string.battery_info_health_overheat);
	                	break;
	                case BatteryManager.BATTERY_HEALTH_DEAD:
	                	healthString = getString(R.string.battery_info_health_dead);
	                	break;
	                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
	                	healthString = getString(R.string.battery_info_health_over_voltage);
	                	break;
	                case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
	                	healthString = getString(R.string.battery_info_health_unspecified_failure);
	                	break;
	                case BatteryManager.BATTERY_HEALTH_COLD:
	                	healthString = getString(R.string.battery_info_health_cold);
	                	break;
	                default:
	                	healthString = getString(R.string.battery_info_health_unknown);
	                	break;
}
mHealth.setText(healthString);
}
//Synthetic comment -- @@ -198,7 +212,5 @@
private void updateBatteryStats() {
long uptime = SystemClock.elapsedRealtime();
mUptime.setText(DateUtils.formatElapsedTime(uptime / 1000));
}
}







