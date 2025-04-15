/*telephony: PIN/PUK MMI code support for CDMA

Added MMI support for change PIN1/PIN2 and unlocking PUK2.

Change-Id:If579a88643737df39588ece5a5304550282ea797*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 58e3e5f..9abb209 100755

//Synthetic comment -- @@ -523,7 +523,7 @@
if (mmi == null) {
Log.e(LOG_TAG, "Mmi is NULL!");
return false;
        } else if (mmi.isPukCommand()) {
mPendingMmis.add(mmi);
mMmiRegistrants.notifyRegistrants(new AsyncResult(null, mmi, null));
mmi.processCode();








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaMmiCode.java b/telephony/java/com/android/internal/telephony/cdma/CdmaMmiCode.java
//Synthetic comment -- index 8dd8c2e..ae283ac 100644

//Synthetic comment -- @@ -44,7 +44,10 @@
static final String ACTION_REGISTER = "**";

// Supp Service codes from TS 22.030 Annex B
static final String SC_PUK          = "05";

// Event Constant

//Synthetic comment -- @@ -174,8 +177,9 @@
/**
* @return true if the Service Code is PIN/PIN2/PUK/PUK2-related
*/
    boolean isPukCommand() {
        return sc != null && sc.equals(SC_PUK);
}

boolean isRegister() {
//Synthetic comment -- @@ -191,8 +195,8 @@
void
processCode () {
try {
            if (isPukCommand()) {
                // sia = old PUK
// sib = new PIN
// sic = new PIN
String oldPinOrPuk = sia;
//Synthetic comment -- @@ -206,8 +210,21 @@
// invalid length
handlePasswordError(com.android.internal.R.string.invalidPin);
} else {
                        phone.mCM.supplyIccPuk(oldPinOrPuk, newPin,
                                obtainMessage(EVENT_SET_COMPLETE, this));
}
} else {
throw new RuntimeException ("Invalid or Unsupported MMI Code");
//Synthetic comment -- @@ -246,7 +263,7 @@

private CharSequence getScString() {
if (sc != null) {
            if (isPukCommand()) {
return context.getText(com.android.internal.R.string.PinMmi);
}
}
//Synthetic comment -- @@ -264,7 +281,7 @@
if (ar.exception instanceof CommandException) {
CommandException.Error err = ((CommandException)(ar.exception)).getCommandError();
if (err == CommandException.Error.PASSWORD_INCORRECT) {
                    if (isPukCommand()) {
sb.append(context.getText(
com.android.internal.R.string.badPuk));
} else {







