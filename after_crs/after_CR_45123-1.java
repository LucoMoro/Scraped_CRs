/*Stk: These changes are related to DISPLAY Text proactive command.

The following issues related to DISPLAY_TEXT are fixed in this change:

1. Send the Terminal Response if ResponseNeeded qualifier is set, after the
default duration else display message for ever.

2. Currently the STK App is sending "screen busy" terminal response for
Subsequent display text proactive commands if a previous display text is
being displayed. Changes are made to update the dialog box with the
subsequent display text and not to send the screen busy.
(Refer to ETSI TS 102.384 section 27.22.4.1.4.4.2)

3. When a high priority Display Text proactive command with infinite time out is
sent,user has to clear this message. The Terminal Response for this command
will be sent from Stk framework itself. Stk app receives this message and
displays it until user clears it. In between other proactive commands like Play
Tone can be processed parallely. The Terminal Response for Play Tone command
is not sent if the Display Text message is not cleared because of improper
handling. This is corrected.

4. When a high priority, wait for user clear and no timeout Display Text
Proactive command is received UE should display the text until user clears
it. While the text is being displayed if a next Proactive command is received,
it should be acted upon immediately.

Change-Id:Ieabc764344b3fa8679672c9f7de35ea9b5849576*/




//Synthetic comment -- diff --git a/src/com/android/stk/StkAppService.java b/src/com/android/stk/StkAppService.java
//Synthetic comment -- index 9dcd25b..1770095 100644

//Synthetic comment -- @@ -221,6 +221,13 @@
}

/*
     * Package api used by StkDialogActivity to indicate if its on the foreground.
     */
    void indicateDisplayTextDlgVisibility(boolean visibility) {
        mDisplayTextDlgIsVisibile = visibility;
    }

    /*
* Package api used by StkMenuActivity to get its Menu parameter.
*/
Menu getMenu() {
//Synthetic comment -- @@ -396,7 +403,21 @@
// TODO: get the carrier name from the SIM
msg.title = "";
}

            //If the device is not in idlescreen and a low priority display
            //text message command arrives then send screen busy terminal
            //response with out displaying the message. Otherwise display the
            //message. The existing displayed message shall be updated with the
            //new display text proactive command (Refer to ETSI TS 102 384
            //section 27.22.4.1.4.4.2).
            if (!(msg.isHighPriority || mMenuIsVisibile || mDisplayTextDlgIsVisibile)) {
                Intent StkIntent = new Intent(AppInterface.CHECK_SCREEN_IDLE_ACTION);
                StkIntent.putExtra("SCREEN_STATUS_REQUEST",true);
                sendBroadcast(StkIntent);
                mDisplayText = true;
            } else {
                launchTextDialog();
            }
break;
case SELECT_ITEM:
mCurrentMenu = cmdMsg.getMenu();
//Synthetic comment -- @@ -644,7 +665,7 @@
private void launchTextDialog() {
Intent newIntent = new Intent(this, StkDialogActivity.class);
newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP
| Intent.FLAG_ACTIVITY_NO_HISTORY
| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
| getFlagActivityNoUserAction(InitiatedByUserAction.unknown));








//Synthetic comment -- diff --git a/src/com/android/stk/StkDialogActivity.java b/src/com/android/stk/StkDialogActivity.java
//Synthetic comment -- index 3fd3ef7..3cd71a8 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.stk;

import com.android.internal.telephony.cat.TextMessage;
import com.android.internal.telephony.cat.CatLog;

import android.app.Activity;
import android.content.Intent;
//Synthetic comment -- @@ -37,6 +38,9 @@
public class StkDialogActivity extends Activity implements View.OnClickListener {
// members
TextMessage mTextMsg;
    int  dialogDuration = 0;

    StkAppService appService = StkAppService.getInstance();

Handler mTimeoutHandler = new Handler() {
@Override
//Synthetic comment -- @@ -64,18 +68,9 @@
protected void onCreate(Bundle icicle) {
super.onCreate(icicle);

requestWindowFeature(Window.FEATURE_LEFT_ICON);

setContentView(R.layout.stk_msg_dialog);

Button okButton = (Button) findViewById(R.id.button_ok);
Button cancelButton = (Button) findViewById(R.id.button_cancel);
//Synthetic comment -- @@ -83,33 +78,21 @@
okButton.setOnClickListener(this);
cancelButton.setOnClickListener(this);

}

public void onClick(View v) {
String input = null;
        if (mTextMsg.responseNeeded) {
             switch (v.getId()) {
                 case OK_BUTTON:
                     sendResponse(StkAppService.RES_ID_CONFIRM, true);
                     break;
                 case CANCEL_BUTTON:
                     sendResponse(StkAppService.RES_ID_CONFIRM, false);
                     break;
             }
}
        finish();
}

@Override
//Synthetic comment -- @@ -126,6 +109,47 @@
@Override
public void onResume() {
super.onResume();

        appService.indicateDisplayTextDlgVisibility(true);

        initFromIntent(getIntent());
        if (mTextMsg == null) {
            finish();
            return;
        }

        Window window = getWindow();

        TextView mMessageView = (TextView) window
                .findViewById(R.id.dialog_message);

        setTitle(mTextMsg.title);

        if (!(mTextMsg.iconSelfExplanatory && mTextMsg.icon != null)) {
            mMessageView.setText(mTextMsg.text);
        }

        if (mTextMsg.icon == null) {
            window.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
                    com.android.internal.R.drawable.stat_notify_sim_toolkit);
        } else {
            window.setFeatureDrawable(Window.FEATURE_LEFT_ICON,
                    new BitmapDrawable(mTextMsg.icon));
        }


        /*
         * If userClear flag is set and dialogduration is set to 0, display Text
         * should be displayed to user for ever until some high priority event occurred
         * (incoming call, MMI code execution etc as mentioned under section
         * ETSI 102.223, 6.4.1)
         */
        dialogDuration = StkApp.calculateDurationInMilis(mTextMsg.duration);
        if (dialogDuration == 0 && !mTextMsg.responseNeeded && mTextMsg.userClear) {
            CatLog.d(this, "User should clear text..show message for ever");
            return;
        }

startTimeOut();
}

//Synthetic comment -- @@ -134,6 +158,7 @@
super.onPause();

cancelTimeOut();
        appService.indicateDisplayTextDlgVisibility(false);
}

@Override
//Synthetic comment -- @@ -150,6 +175,12 @@
mTextMsg = savedInstanceState.getParcelable(TEXT);
}

    @Override
    protected void onNewIntent(Intent intent) {
        CatLog.d(this, "onNewIntent - updating the same Dialog box");
        setIntent(intent);
    }

private void sendResponse(int resId, boolean confirmed) {
Bundle args = new Bundle();
args.putInt(StkAppService.OPCODE, StkAppService.OP_RESPONSE);
//Synthetic comment -- @@ -178,7 +209,7 @@
private void startTimeOut() {
// Reset timeout.
cancelTimeOut();
        dialogDuration = StkApp.calculateDurationInMilis(mTextMsg.duration);
if (dialogDuration == 0) {
dialogDuration = StkApp.UI_TIMEOUT;
}







