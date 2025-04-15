/*Doesn't display input PIN number after cancel the PIN screen

Set mEnteredDigits to zero when press cancel key.

Change-Id:Ib7c25e5f7f34dd9c280aad78da7cec707140c38fSigned-off-by: Charles Li<liyuchenlp@gmail.com>*/




//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/SimUnlockScreen.java b/policy/src/com/android/internal/policy/impl/SimUnlockScreen.java
//Synthetic comment -- index 9604cdc..e99e99a 100644

//Synthetic comment -- @@ -360,6 +360,7 @@
public void onClick(View v) {
if (v == mCancelButton) {
mPinText.setText(""); // clear the PIN entry field if the user cancels
                mEnteredDigits = 0;
mCallback.goToLockScreen();
return;
}







