/*These changes fix issues related to DISPLAY Text proactive command.

The following issues related to DISPLAY_TEXT are fixed in this change:
Ref: ETSI 102.223 Section: 6.4.1

1. Do not Send Terminal Response from STK_APP if ResponseNeeded qualifier is false.
Desc: If immediate response is set, then user should not send the terminal response
      from STK_APP. It is sent immediately from Stk framework.

2. Second Display text proactive command should replace first display text.
Desc: The dialog box is modified with the subsequent display text.
      (Refer to ETSI TS 102.384 section 27.22.4.1.4.4.2)

3. Proper handling of high priority display with user clear and immediateresponse set
Desc: When a high priority Display Text proactive command with user clear flag and
      immediate response set, then it should be displayed until user clears it.

Change-Id:Id042109d5081f31c6942ef64ef0d51db61cbae9b*/
//Synthetic comment -- diff --git a/src/com/android/stk/StkAppService.java b/src/com/android/stk/StkAppService.java
//Synthetic comment -- index 1007a4a..2375723 100644

//Synthetic comment -- @@ -644,7 +644,7 @@
private void launchTextDialog() {
Intent newIntent = new Intent(this, StkDialogActivity.class);
newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_MULTIPLE_TASK
| Intent.FLAG_ACTIVITY_NO_HISTORY
| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
| getFlagActivityNoUserAction(InitiatedByUserAction.unknown));








//Synthetic comment -- diff --git a/src/com/android/stk/StkDialogActivity.java b/src/com/android/stk/StkDialogActivity.java
//Synthetic comment -- index 3fd3ef7..382156c 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.stk;

import com.android.internal.telephony.cat.TextMessage;

import android.app.Activity;
import android.content.Intent;
//Synthetic comment -- @@ -64,18 +65,9 @@
protected void onCreate(Bundle icicle) {
super.onCreate(icicle);

        initFromIntent(getIntent());
        if (mTextMsg == null) {
            finish();
            return;
        }

requestWindowFeature(Window.FEATURE_LEFT_ICON);
        Window window = getWindow();

setContentView(R.layout.stk_msg_dialog);
        TextView mMessageView = (TextView) window
                .findViewById(R.id.dialog_message);

Button okButton = (Button) findViewById(R.id.button_ok);
Button cancelButton = (Button) findViewById(R.id.button_cancel);
//Synthetic comment -- @@ -83,33 +75,22 @@
okButton.setOnClickListener(this);
cancelButton.setOnClickListener(this);

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
}

public void onClick(View v) {
String input = null;

        switch (v.getId()) {
        case OK_BUTTON:
            sendResponse(StkAppService.RES_ID_CONFIRM, true);
            finish();
            break;
        case CANCEL_BUTTON:
            sendResponse(StkAppService.RES_ID_CONFIRM, false);
            finish();
            break;
}
}

@Override
//Synthetic comment -- @@ -126,6 +107,44 @@
@Override
public void onResume() {
super.onResume();
startTimeOut();
}

//Synthetic comment -- @@ -150,6 +169,12 @@
mTextMsg = savedInstanceState.getParcelable(TEXT);
}

private void sendResponse(int resId, boolean confirmed) {
Bundle args = new Bundle();
args.putInt(StkAppService.OPCODE, StkAppService.OP_RESPONSE);







