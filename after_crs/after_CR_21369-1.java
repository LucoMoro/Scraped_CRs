/*Fix to prevent Calendar Upgrade on each bootup

Moving the receiver to "finally" block to ensure that we
only ever get called once.
This will prevent this process from ever getting started
following any future reboots and thus further improving
the startup time of the device.

Change-Id:I58225c930a1f3027b8c4cf373a592f905abdd638*/




//Synthetic comment -- diff --git a/src/com/android/providers/calendar/CalendarUpgradeReceiver.java b/src/com/android/providers/calendar/CalendarUpgradeReceiver.java
//Synthetic comment -- index 30ac58c..f24cd51 100644

//Synthetic comment -- @@ -71,9 +71,11 @@
EventLogTags.writeCalendarUpgradeReceiver(System.currentTimeMillis() - startTime);
}
} catch (Throwable t) {
            // Something has gone terribly wrong.
            Log.wtf(TAG, "Error during upgrade attempt.", t);
        } finally {
            // Ensure that it only ever get called once on first bootup.
            Log.i(TAG, "Disable this receiver to avoid the upgrade at future reboots");
context.getPackageManager().setComponentEnabledSetting(
new ComponentName(context, getClass()),
PackageManager.COMPONENT_ENABLED_STATE_DISABLED,







