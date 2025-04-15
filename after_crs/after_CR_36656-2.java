/*Added missing icon support to SETUP_CALLs setup phase

Change-Id:I973240f513e10da4b6a2693651ece7c19afe61beSigned-off-by: Christian Bejram <christian.bejram@stericsson.com>
Signed-off-by: Patrik Ryd <patrik.ryd@stericsson.com>*/




//Synthetic comment -- diff --git a/src/com/android/stk/StkAppService.java b/src/com/android/stk/StkAppService.java
//Synthetic comment -- index 9dcd25b..82e0257 100644

//Synthetic comment -- @@ -547,7 +547,7 @@
resMsg.setResultCode(ResultCode.OK);
resMsg.setConfirmation(confirmed);
if (confirmed) {
                    launchEventMessage(mCurrentCmd.getCallSettings().callMsg);
}
break;
}
//Synthetic comment -- @@ -653,7 +653,10 @@
}

private void launchEventMessage() {
        launchEventMessage(mCurrentCmd.geTextMessage());
    }

    private void launchEventMessage(TextMessage msg) {
if (msg == null || msg.text == null) {
return;
}
//Synthetic comment -- @@ -742,19 +745,6 @@
} catch (InterruptedException e) {}
}

private void launchIdleText() {
TextMessage msg = mCurrentCmd.geTextMessage();








