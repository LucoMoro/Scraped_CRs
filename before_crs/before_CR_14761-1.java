/*Removed calls to deprecated APIs and unused imports

Change-Id:Id5e94947f58a1282857ec9f4239e85299b9d7d51*/
//Synthetic comment -- diff --git a/src/com/android/phone/EditPhoneNumberPreference.java b/src/com/android/phone/EditPhoneNumberPreference.java
//Synthetic comment -- index a6ddfbc..f6f2395 100644

//Synthetic comment -- @@ -187,7 +187,7 @@
@Override
protected void onBindDialogView(View view) {
// default the button clicked to be the cancel button.
        mButtonClicked = DialogInterface.BUTTON2;

super.onBindDialogView(view);

//Synthetic comment -- @@ -307,7 +307,7 @@
@Override
public void onClick(DialogInterface dialog, int which) {
// The neutral button (button3) is always the toggle.
        if ((mConfirmationMode == CM_ACTIVATION) && (which == DialogInterface.BUTTON3)) {
//flip the toggle if we are in the correct mode.
setToggled(!isToggled());
}
//Synthetic comment -- @@ -321,8 +321,8 @@
// phone numbers and calling the close action listener.
protected void onDialogClosed(boolean positiveResult) {
// A positive result is technically either button1 or button3.
        if ((mButtonClicked == DialogInterface.BUTTON1) ||
                (mButtonClicked == DialogInterface.BUTTON3)){
setPhoneNumber(getEditText().getText().toString());
super.onDialogClosed(positiveResult);
setText(getStringValue());








//Synthetic comment -- diff --git a/src/com/android/phone/PhoneUtils.java b/src/com/android/phone/PhoneUtils.java
//Synthetic comment -- index 1428d01..67a7b73 100755

//Synthetic comment -- @@ -1017,10 +1017,10 @@
new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int whichButton) {
switch (whichButton) {
                                case DialogInterface.BUTTON1:
phone.sendUssdResponse(inputText.getText().toString());
break;
                                case DialogInterface.BUTTON2:
if (mmiCode.isCancelable()) {
mmiCode.cancel();
}








//Synthetic comment -- diff --git a/src/com/android/phone/Settings.java b/src/com/android/phone/Settings.java
//Synthetic comment -- index 9a69458..38d45d1 100644

//Synthetic comment -- @@ -74,7 +74,7 @@
//This is a method implemented for DialogInterface.OnClickListener.
//  Used to dismiss the dialogs when they come up.
public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON1) {
mPhone.setDataRoamingEnabled(true);
mOkClicked = true;
} else {







