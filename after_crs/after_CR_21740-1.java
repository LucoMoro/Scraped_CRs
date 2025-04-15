/*StkApp, Fix null pointer crash when receiving REFRESH

Add an additional check to fix null pointer crash.

Change-Id:Ida3229d596cb3c5baff10714e651a6cb1c769b8fSigned-off-by: Christian Bejram <christian.bejram@stericsson.com>*/




//Synthetic comment -- diff --git a/src/com/android/stk/StkAppService.java b/src/com/android/stk/StkAppService.java
//Synthetic comment -- index a21b240..cdd257d 100644

//Synthetic comment -- @@ -698,7 +698,7 @@

private void launchIdleText() {
TextMessage msg = mCurrentCmd.geTextMessage();
        if (msg == null || msg.text == null) {
mNotificationManager.cancel(STK_NOTIFICATION_ID);
} else {
Notification notification = new Notification();







