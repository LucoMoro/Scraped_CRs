/*Added missing icon support to SETUP_CALLs setup phase

Change-Id:I973240f513e10da4b6a2693651ece7c19afe61beSigned-off-by: Christian Bejram <christian.bejram@stericsson.com>*/
//Synthetic comment -- diff --git a/src/com/android/stk/StkAppService.java b/src/com/android/stk/StkAppService.java
//Synthetic comment -- index 1007a4a..bf5e614 100644

//Synthetic comment -- @@ -547,7 +547,7 @@
resMsg.setResultCode(ResultCode.OK);
resMsg.setConfirmation(confirmed);
if (confirmed) {
                    launchCallMsg();
}
break;
}
//Synthetic comment -- @@ -653,7 +653,10 @@
}

private void launchEventMessage() {
        TextMessage msg = mCurrentCmd.geTextMessage();
if (msg == null || msg.text == null) {
return;
}
//Synthetic comment -- @@ -742,19 +745,6 @@
} catch (InterruptedException e) {}
}

    private void launchCallMsg() {
        TextMessage msg = mCurrentCmd.getCallSettings().callMsg;
        if (msg.text == null || msg.text.length() == 0) {
            return;
        }
        msg.title = lastSelectedItem;

        Toast toast = Toast.makeText(mContext.getApplicationContext(), msg.text,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

private void launchIdleText() {
TextMessage msg = mCurrentCmd.geTextMessage();








