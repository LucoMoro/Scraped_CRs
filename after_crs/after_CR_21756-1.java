/*Fixed icon handling for proactive commands according to 3GPP 31.111

Change-Id:I9a0f908efd1a63ad8cefe2dc0576504b8648e716Signed-off-by: Christian Bejram <christian.bejram@stericsson.com>*/




//Synthetic comment -- diff --git a/src/com/android/stk/StkAppService.java b/src/com/android/stk/StkAppService.java
//Synthetic comment -- index a21b240..f76ad26 100644

//Synthetic comment -- @@ -623,7 +623,7 @@
} else {
iv.setVisibility(View.GONE);
}
        if (!msg.iconSelfExplanatory || msg.icon == null) {
tv.setText(msg.text);
}

//Synthetic comment -- @@ -709,7 +709,7 @@
notification.flags |= Notification.FLAG_NO_CLEAR;
notification.icon = com.android.internal.R.drawable.stat_notify_sim_toolkit;
// Set text and icon for the status bar and notification body.
            if (!msg.iconSelfExplanatory || msg.icon == null) {
notification.tickerText = msg.text;
contentView.setTextViewText(com.android.internal.R.id.text,
msg.text);








//Synthetic comment -- diff --git a/src/com/android/stk/StkInputActivity.java b/src/com/android/stk/StkInputActivity.java
//Synthetic comment -- index b6228fb..6ed8b81 100644

//Synthetic comment -- @@ -268,7 +268,9 @@
int inTypeId = R.string.alphabet;

// set the prompt.
        if (!mStkInput.iconSelfExplanatory || mStkInput.icon == null) {
            mPromptView.setText(mStkInput.text);
        }

// Set input type (alphabet/digit) info close to the InText form.
if (mStkInput.digitOnly) {








//Synthetic comment -- diff --git a/src/com/android/stk/StkMenuActivity.java b/src/com/android/stk/StkMenuActivity.java
//Synthetic comment -- index aac1a12..83992c3 100644

//Synthetic comment -- @@ -270,7 +270,8 @@
} else {
mTitleIconView.setVisibility(View.GONE);
}
            if (!mStkMenu.titleIconSelfExplanatory ||
                    mStkMenu.titleIcon == null) {
mTitleTextView.setVisibility(View.VISIBLE);
if (mStkMenu.title == null) {
mTitleTextView.setText(R.string.app_name);








//Synthetic comment -- diff --git a/src/com/android/stk/ToneDialog.java b/src/com/android/stk/ToneDialog.java
//Synthetic comment -- index ba4a957..d4b17bf 100644

//Synthetic comment -- @@ -75,7 +75,10 @@
ImageView iv = (ImageView) findViewById(R.id.icon);

// set text and icon
        if (!toneMsg.iconSelfExplanatory || toneMsg.icon == null) {
            tv.setText(toneMsg.text);
        }

if (toneMsg.icon == null) {
iv.setImageResource(com.android.internal.R.drawable.ic_volume);
} else {







