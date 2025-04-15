/*Prevent TONE_CDMA_SIGNAL_OFF to be played for non-CDMA phones

When a non-CDMA phone is used, there is no need to start TONE_CDMA_SIGNAL_OFF
after disconnection. Additionnaly, by being started after the BUSY call
notification, it prevents the busy tones to be properly played on BT HFP device.

Change-Id:Iaa004452105b0b115d2fdedff49bd723e143f637Signed-off-by: Hugo Dupras <h-dupras@ti.com>
Signed-off-by: Patrick Combes <p-combes@ti.com>*/




//Synthetic comment -- diff --git a/src/com/android/phone/CallNotifier.java b/src/com/android/phone/CallNotifier.java
//Synthetic comment -- index 2e82543..ea48333 100755

//Synthetic comment -- @@ -1002,12 +1002,10 @@
if ((c != null) && (c.getCall().getPhone().getPhoneType() == Phone.PHONE_TYPE_CDMA)) {
autoretrySetting = android.provider.Settings.System.getInt(mApplication.
getContentResolver(),android.provider.Settings.System.CALL_AUTO_RETRY, 0);

            // Stop any signalInfo tone being played when a call gets ended
            stopSignalInfoTone();

// Resetting the CdmaPhoneCallState members
mApplication.cdmaPhoneCallState.resetCdmaPhoneCallState();








