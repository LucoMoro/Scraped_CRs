/*apps/Settings: Airplane mode.

Airplane mode depends on a cellular modem to return
a power off notification to the application to clear and
set the airplane mode database setting.

Systems without modems see that the intent return occurs before the
the setAirplane mode command is called thereby disabling the checkbox.

Only when the application looses focus and is re-entered is the checkbox
enabled and the command can be sent again.

This change will check to see if a cellular modem is available and if it
is send the intent to the modem.  If not the intent to the modem is not sent
but the intent is sent to the WiFi and Bluetooth.

This patch will be sent to Google for review and more than likely be rejected
but hopefully they can give perspective on what needs to be implemented for the
modem to return the correct status.

Signed-off-by: Dan Murphy <dmurphy@ti.com>*/
//Synthetic comment -- diff --git a/src/com/android/settings/AirplaneModeEnabler.java b/src/com/android/settings/AirplaneModeEnabler.java
//Synthetic comment -- index ff4b27d..861e774 100644

//Synthetic comment -- @@ -35,6 +35,8 @@
private final Context mContext;

private PhoneStateIntentReceiver mPhoneStateReceiver;

private final CheckBoxPreference mCheckBoxPref;

//Synthetic comment -- @@ -57,9 +59,12 @@
mCheckBoxPref = airplaneModeCheckBoxPreference;

airplaneModeCheckBoxPreference.setPersistent(false);
    
mPhoneStateReceiver = new PhoneStateIntentReceiver(mContext, mHandler);
mPhoneStateReceiver.notifyServiceState(EVENT_SERVICE_STATE_CHANGED);
}

public void resume() {
//Synthetic comment -- @@ -96,14 +101,27 @@
Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
intent.putExtra("state", enabling);
mContext.sendBroadcast(intent);
}

/**
* Called when we've received confirmation that the airplane mode was set.
*/
private void onAirplaneModeChanged() {
        ServiceState serviceState = mPhoneStateReceiver.getServiceState();
        boolean airplaneModeEnabled = serviceState.getState() == ServiceState.STATE_POWER_OFF;
mCheckBoxPref.setChecked(airplaneModeEnabled);
mCheckBoxPref.setSummary(airplaneModeEnabled ? null : 
mContext.getString(R.string.airplane_mode_summary));            







