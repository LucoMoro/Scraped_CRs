/*No response sent when backlight is OFF

When the LCD backlight is OFF while receiving a
PLAY TONE command, the onDestroy() method is called
immediately since the app is in the background.
In onDestroy() the STOP message is removed from the
handler queue which leads to no response is sent back to
StkAppService. This causes the RIL layer to send
TR 'ME currently unable to process command' back to the SIM.
Solution is to check if a response is sent before
removing any messages from the handler queue.

Change-Id:I7ac2e99b85b38576acfe72370aa55850cdc304a0*/
//Synthetic comment -- diff --git a/src/com/android/stk/ToneDialog.java b/src/com/android/stk/ToneDialog.java
//Synthetic comment -- index ba4a957..e4a4485 100644

//Synthetic comment -- @@ -38,6 +38,7 @@
TextMessage toneMsg = null;
ToneSettings settings = null;
TonePlayer player = null;

/**
* Handler used to stop tones from playing when the duration ends.
//Synthetic comment -- @@ -98,8 +99,9 @@
@Override
protected void onDestroy() {
super.onDestroy();

        mToneStopper.removeMessages(MSG_ID_STOP_TONE);
player.stop();
player.release();
mVibrator.cancel();
//Synthetic comment -- @@ -129,5 +131,6 @@
args.putInt(StkAppService.OPCODE, StkAppService.OP_RESPONSE);
args.putInt(StkAppService.RES_ID, resId);
startService(new Intent(this, StkAppService.class).putExtras(args));
}
}







