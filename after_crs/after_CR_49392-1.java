/*Changed timeouts for Stk

The SIM Toolkit interface in Android uses one common
timeout for UI commands. This is not good practice,
as there is a very clear difference between use cases.

A DISPLAY TEXT command with the option “clear after delay”
and with option “Wait for user to clear” should not have
the same timeout, which is the case without a patch. The
timeouts have in the patch been set to 15 s for
“clear after delay” (i.e. automatically removed), 60 seconds
for “Wait for user to clear” and 30 s for other UI timeouts.

The times are both in accordance with ETSI 102.223 and
operator requirements.

Change-Id:I61262bf36a84f071ec4f223eb187f92e2026b68b*/




//Synthetic comment -- diff --git a/src/com/android/stk/StkApp.java b/src/com/android/stk/StkApp.java
//Synthetic comment -- index 0f0af52..4484493 100644

//Synthetic comment -- @@ -32,8 +32,12 @@
static final int MENU_ID_BACK = android.view.Menu.FIRST + 1;
static final int MENU_ID_HELP = android.view.Menu.FIRST + 2;

    // Display Text timeouts
    static final int DISP_TEXT_CLEAR_AFTER_DELAY_TIMEOUT = (15 * 1000);
    static final int DISP_TEXT_WAIT_FOR_USER_TIMEOUT = (60 * 1000);

    // UI timeout, 30 seconds - used for menues and input
    static final int UI_TIMEOUT = (30 * 1000);

// Tone default timeout - 2 seconds
static final int TONE_DFEAULT_TIMEOUT = (2 * 1000);








//Synthetic comment -- diff --git a/src/com/android/stk/StkDialogActivity.java b/src/com/android/stk/StkDialogActivity.java
//Synthetic comment -- index 3fd3ef7..127f51b 100644

//Synthetic comment -- @@ -126,7 +126,7 @@
@Override
public void onResume() {
super.onResume();
        startTimeOut(mTextMsg.userClear);
}

@Override
//Synthetic comment -- @@ -175,12 +175,18 @@
mTimeoutHandler.removeMessages(MSG_ID_TIMEOUT);
}

    private void startTimeOut(boolean waitForUserToClear) {
// Reset timeout.
cancelTimeOut();
int dialogDuration = StkApp.calculateDurationInMilis(mTextMsg.duration);
        // If duration is specified, this has priority. If not, set timeout
        // according to condition given by the card.
if (dialogDuration == 0) {
            if (waitForUserToClear) {
                dialogDuration = StkApp.DISP_TEXT_WAIT_FOR_USER_TIMEOUT;
            } else {
                dialogDuration = StkApp.DISP_TEXT_CLEAR_AFTER_DELAY_TIMEOUT;
            }
}
mTimeoutHandler.sendMessageDelayed(mTimeoutHandler
.obtainMessage(MSG_ID_TIMEOUT), dialogDuration);







