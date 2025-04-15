/*Process dialog does not disappear after setting call forwarding.

Dismiss the dialog not matter if it is on foreground.

Change-Id:I4f1191675afa1b26e182d92be2a57ce161521151*/




//Synthetic comment -- diff --git a/src/com/android/phone/TimeConsumingPreferenceActivity.java b/src/com/android/phone/TimeConsumingPreferenceActivity.java
//Synthetic comment -- index 807e21a..94568f2 100644

//Synthetic comment -- @@ -143,7 +143,7 @@
+ ", reading=" + reading);
mBusyList.remove(preference.getKey());

        if (mBusyList.isEmpty()) {
if (reading) {
dismissDialogSafely(BUSY_READING_DIALOG);
} else {







