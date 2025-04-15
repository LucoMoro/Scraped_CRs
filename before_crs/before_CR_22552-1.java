/*Added missing icon support to SETUP_CALLs setup phase

Change-Id:I553fc4b8c8147b933e7253cd6eae30044bfb2a95Signed-off-by: christian bejram <christian.bejram@stericsson.com>*/
//Synthetic comment -- diff --git a/src/com/android/stk/StkAppService.java b/src/com/android/stk/StkAppService.java
//Synthetic comment -- index a21b240..580f655 100644

//Synthetic comment -- @@ -512,7 +512,7 @@
resMsg.setResultCode(ResultCode.OK);
resMsg.setConfirmation(confirmed);
if (confirmed) {
                    launchCallMsg();
}
break;
}
//Synthetic comment -- @@ -606,7 +606,10 @@
}

private void launchEventMessage() {
        TextMessage msg = mCurrentCmd.geTextMessage();
if (msg == null || msg.text == null) {
return;
}
//Synthetic comment -- @@ -683,19 +686,6 @@
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
if (msg.text == null) {







