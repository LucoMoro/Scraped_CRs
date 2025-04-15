/*Remove checking of telephony for Airplane mode

Airplane mode should not be disabled if telephony is not available.
Airplane mode should be available for turning on/off on devices
with any radio interface, not only mobile.

Change-Id:I434f7c3f93edd8598d134ce780e108fc80121af5Signed-off-by: Ruslan Shymkevych <x0153342@ti.com>*/
//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/GlobalActions.java b/policy/src/com/android/internal/policy/impl/GlobalActions.java
//Synthetic comment -- index d1f8ef1..c4d61c30 100644

//Synthetic comment -- @@ -209,8 +209,6 @@

@Override
protected void changeStateFromPress(boolean buttonOn) {
                if (!mHasTelephony) return;

// In ECM mode airplane state cannot be changed
if (!(Boolean.parseBoolean(
SystemProperties.get(TelephonyProperties.PROPERTY_INECM_MODE)))) {
//Synthetic comment -- @@ -835,7 +833,6 @@
PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
@Override
public void onServiceStateChanged(ServiceState serviceState) {
            if (!mHasTelephony) return;
final boolean inAirplaneMode = serviceState.getState() == ServiceState.STATE_POWER_OFF;
mAirplaneState = inAirplaneMode ? ToggleAction.State.On : ToggleAction.State.Off;
mAirplaneModeOn.updateState(mAirplaneState);
//Synthetic comment -- @@ -885,8 +882,6 @@

private void onAirplaneModeChanged() {
// Let the service state callbacks handle the state.
        if (mHasTelephony) return;

boolean airplaneModeOn = Settings.Global.getInt(
mContext.getContentResolver(),
Settings.Global.AIRPLANE_MODE_ON,
//Synthetic comment -- @@ -907,9 +902,7 @@
intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
intent.putExtra("state", on);
mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
        if (!mHasTelephony) {
            mAirplaneState = on ? ToggleAction.State.On : ToggleAction.State.Off;
        }
}

private static final class GlobalActionsDialog extends Dialog implements DialogInterface {







