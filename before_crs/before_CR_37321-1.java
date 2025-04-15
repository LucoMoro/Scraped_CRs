/*Phone: Use ICS alert icon

It's currently using the upscaled Gingerbread drawable.

Change-Id:I488f0097f0f61c1ba5f18759ea2a3dba5980109f*/
//Synthetic comment -- diff --git a/src/com/android/phone/InCallScreen.java b/src/com/android/phone/InCallScreen.java
//Synthetic comment -- index 26733ed..12929e2 100755

//Synthetic comment -- @@ -3074,7 +3074,7 @@

mCallLostDialog = new AlertDialog.Builder(this)
.setMessage(R.string.call_lost)
                .setIcon(android.R.drawable.ic_dialog_alert)
.create();
mCallLostDialog.show();
}








//Synthetic comment -- diff --git a/src/com/android/phone/OutgoingCallBroadcaster.java b/src/com/android/phone/OutgoingCallBroadcaster.java
//Synthetic comment -- index 0d7d1e6..0682407 100644

//Synthetic comment -- @@ -609,7 +609,7 @@
case DIALOG_NOT_VOICE_CAPABLE:
dialog = new AlertDialog.Builder(this)
.setTitle(R.string.not_voice_capable)
                        .setIcon(android.R.drawable.ic_dialog_alert)
.setPositiveButton(android.R.string.ok, this)
.setOnCancelListener(this)
.create();








//Synthetic comment -- diff --git a/src/com/android/phone/Settings.java b/src/com/android/phone/Settings.java
//Synthetic comment -- index ab928e0..45c7191 100644

//Synthetic comment -- @@ -161,7 +161,7 @@
new AlertDialog.Builder(this).setMessage(
getResources().getString(R.string.roaming_warning))
.setTitle(android.R.string.dialog_alert_title)
                        .setIcon(android.R.drawable.ic_dialog_alert)
.setPositiveButton(android.R.string.yes, this)
.setNegativeButton(android.R.string.no, this)
.show()








//Synthetic comment -- diff --git a/src/com/android/phone/SipCallOptionHandler.java b/src/com/android/phone/SipCallOptionHandler.java
//Synthetic comment -- index 0734cd7..64d18c5 100644

//Synthetic comment -- @@ -224,7 +224,7 @@
case DIALOG_SELECT_PHONE_TYPE:
dialog = new AlertDialog.Builder(this)
.setTitle(R.string.pick_outgoing_call_phone_type)
                    .setIcon(android.R.drawable.ic_dialog_alert)
.setSingleChoiceItems(R.array.phone_type_values, -1, this)
.setNegativeButton(android.R.string.cancel, this)
.setOnCancelListener(this)
//Synthetic comment -- @@ -233,7 +233,7 @@
case DIALOG_SELECT_OUTGOING_SIP_PHONE:
dialog = new AlertDialog.Builder(this)
.setTitle(R.string.pick_outgoing_sip_phone)
                    .setIcon(android.R.drawable.ic_dialog_alert)
.setSingleChoiceItems(getProfileNameArray(), -1, this)
.setNegativeButton(android.R.string.cancel, this)
.setOnCancelListener(this)
//Synthetic comment -- @@ -244,7 +244,7 @@
dialog = new AlertDialog.Builder(this)
.setTitle(R.string.no_sip_account_found_title)
.setMessage(R.string.no_sip_account_found)
                    .setIcon(android.R.drawable.ic_dialog_alert)
.setPositiveButton(R.string.sip_menu_add, this)
.setNegativeButton(android.R.string.cancel, this)
.setOnCancelListener(this)
//Synthetic comment -- @@ -257,7 +257,7 @@
: R.string.no_internet_available_title)
.setMessage(wifiOnly ? R.string.no_wifi_available
: R.string.no_internet_available)
                    .setIcon(android.R.drawable.ic_dialog_alert)
.setPositiveButton(android.R.string.ok, this)
.setOnCancelListener(this)
.create();
//Synthetic comment -- @@ -265,7 +265,7 @@
case DIALOG_NO_VOIP:
dialog = new AlertDialog.Builder(this)
.setTitle(R.string.no_voip)
                    .setIcon(android.R.drawable.ic_dialog_alert)
.setPositiveButton(android.R.string.ok, this)
.setOnCancelListener(this)
.create();








//Synthetic comment -- diff --git a/src/com/android/phone/sip/SipEditor.java b/src/com/android/phone/sip/SipEditor.java
//Synthetic comment -- index e1bfaff..7e58215 100644

//Synthetic comment -- @@ -301,7 +301,7 @@
public void run() {
new AlertDialog.Builder(SipEditor.this)
.setTitle(android.R.string.dialog_alert_title)
                        .setIcon(android.R.drawable.ic_dialog_alert)
.setMessage(message)
.setPositiveButton(R.string.alert_dialog_ok, null)
.show();








//Synthetic comment -- diff --git a/src/com/android/phone/sip/SipSettings.java b/src/com/android/phone/sip/SipSettings.java
//Synthetic comment -- index 226ee7a..bc2f44c 100644

//Synthetic comment -- @@ -360,7 +360,7 @@
}
new AlertDialog.Builder(this)
.setTitle(R.string.alert_dialog_close)
                .setIcon(android.R.drawable.ic_dialog_alert)
.setPositiveButton(R.string.close_profile,
new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int w) {







