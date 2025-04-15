/*Lid switch state is incorrect

InputManager#getSwitchState() returns AKEY_STATE_UP(0=OPEN)
and AKEY_STATE_DOWN(1=CLOSE).
mLidOpen is opposite from a real lid status.

Change-Id:I40aa4e0defb95d82b6144ff6b7514f721bf9030f*/




//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/PhoneWindowManager.java b/policy/src/com/android/internal/policy/impl/PhoneWindowManager.java
//Synthetic comment -- index 0de76a7..7923709 100755

//Synthetic comment -- @@ -1083,9 +1083,9 @@
try {
int sw = mWindowManager.getSwitchState(SW_LID);
if (sw > 0) {
mLidOpen = LID_CLOSED;
            } else if (sw == 0) {
                mLidOpen = LID_OPEN;
} else {
mLidOpen = LID_ABSENT;
}







