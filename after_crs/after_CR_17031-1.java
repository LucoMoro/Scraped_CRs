/*Fix wrong behavior when changing pin code in settings

In security setting, if user change orientation during entering
pin code, the change will not apply successfully, it is because
the entered pin codes has not been saved and restored when
application was re-create after changed orientation. So we save
entered pin codes and restore them in onCreate when application
is re-created.

Change-Id:Ia8a3fb3ee75185ce028104487377e47f272905ce*/




//Synthetic comment -- diff --git a/src/com/android/settings/IccLockSettings.java b/src/com/android/settings/IccLockSettings.java
//Synthetic comment -- index 6494567..0df69a0 100644

//Synthetic comment -- @@ -61,6 +61,11 @@
private static final String DIALOG_PIN = "dialogPin";
private static final String DIALOG_ERROR = "dialogError";
private static final String ENABLE_TO_STATE = "enableState";

    // Save and restore inputted PIN code when configuration changed
    // (ex. portrait<-->landscape) during change PIN code
    private static final String OLD_PINCODE = "oldPinCode";
    private static final String NEW_PINCODE = "newPinCode";

private static final int MIN_PIN_LENGTH = 4;
private static final int MAX_PIN_LENGTH = 8;
//Synthetic comment -- @@ -133,6 +138,23 @@
mPin = savedInstanceState.getString(DIALOG_PIN);
mError = savedInstanceState.getString(DIALOG_ERROR);
mToState = savedInstanceState.getBoolean(ENABLE_TO_STATE);

            // Restore inputted PIN code
            switch (mDialogState) {
                case ICC_NEW_MODE:
                    mOldPin = savedInstanceState.getString(OLD_PINCODE);
                    break;

                case ICC_REENTER_MODE:
                    mOldPin = savedInstanceState.getString(OLD_PINCODE);
                    mNewPin = savedInstanceState.getString(NEW_PINCODE);
                    break;

                case ICC_LOCK_MODE:
                case ICC_OLD_MODE:
                default:
                    break;
            }
}

mPinDialog.setOnPinEnteredListener(this);
//Synthetic comment -- @@ -170,6 +192,23 @@
out.putString(DIALOG_PIN, mPinDialog.getEditText().getText().toString());
out.putString(DIALOG_ERROR, mError);
out.putBoolean(ENABLE_TO_STATE, mToState);

            // Save inputted PIN code
            switch (mDialogState) {
                case ICC_NEW_MODE:
                    out.putString(OLD_PINCODE, mOldPin);
                    break;

                case ICC_REENTER_MODE:
                    out.putString(OLD_PINCODE, mOldPin);
                    out.putString(NEW_PINCODE, mNewPin);
                    break;

                case ICC_LOCK_MODE:
                case ICC_OLD_MODE:
                default:
                    break;
            }
} else {
super.onSaveInstanceState(out);
}







