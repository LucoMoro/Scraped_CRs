/*Lid switch state is incorrect

InputManager#getSwitchState() returns OPEN(0) and CLOSE(1).
So PhoneWindowManager should handle the same as its value.

Change-Id:I40aa4e0defb95d82b6144ff6b7514f721bf9030f*/
//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/PhoneWindowManager.java b/policy/src/com/android/internal/policy/impl/PhoneWindowManager.java
//Synthetic comment -- index 0b223c1..84f3d65 100755

//Synthetic comment -- @@ -308,8 +308,8 @@
int mRecentAppsDialogHeldModifiers;

private static final int LID_ABSENT = -1;
    private static final int LID_CLOSED = 0;
    private static final int LID_OPEN = 1;

int mLidOpen = LID_ABSENT;

//Synthetic comment -- @@ -1083,9 +1083,9 @@
try {
int sw = mWindowManager.getSwitchState(SW_LID);
if (sw > 0) {
                mLidOpen = LID_OPEN;
            } else if (sw == 0) {
mLidOpen = LID_CLOSED;
} else {
mLidOpen = LID_ABSENT;
}







