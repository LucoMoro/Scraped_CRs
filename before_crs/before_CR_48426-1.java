/*Stk: Display Large Text notifications in Status bar

SETUP IDLE MODE TEXT - Large Text String can't be
displayed. Text is 274 characters long and only part
of the text is displayed.

With this patch, this is fixed by using the helper
class(Notification.BigTextStyle) for generating
large-format notifications that include a lot of text.

Change-Id:Iefdd95b791e5733037f04dbece6539d9c42a149dAuthor: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 61215*/
//Synthetic comment -- diff --git a/src/com/android/stk/StkAppService.java b/src/com/android/stk/StkAppService.java
//Synthetic comment -- index 82e0257..ea08b8b 100644

//Synthetic comment -- @@ -747,42 +747,51 @@

private void launchIdleText() {
TextMessage msg = mCurrentCmd.geTextMessage();

        if (msg == null) {
            CatLog.d(this, "mCurrent.getTextMessage is NULL");
            mNotificationManager.cancel(STK_NOTIFICATION_ID);
            return;
        }
        if (msg.text == null) {
mNotificationManager.cancel(STK_NOTIFICATION_ID);
} else {
            Notification notification = new Notification();
            RemoteViews contentView = new RemoteViews(
                    PACKAGE_NAME,
                    com.android.internal.R.layout.status_bar_latest_event_content);

            notification.flags |= Notification.FLAG_NO_CLEAR;
            notification.icon = com.android.internal.R.drawable.stat_notify_sim_toolkit;
            // Set text and icon for the status bar and notification body.
            if (!msg.iconSelfExplanatory) {
                notification.tickerText = msg.text;
                contentView.setTextViewText(com.android.internal.R.id.text,
                        msg.text);
            }
            if (msg.icon != null) {
                contentView.setImageViewBitmap(com.android.internal.R.id.icon,
                        msg.icon);
            } else {
                contentView
                        .setImageViewResource(
com.android.internal.R.id.icon,
com.android.internal.R.drawable.stat_notify_sim_toolkit);
            }
            notification.contentView = contentView;
            notification.contentIntent = PendingIntent.getService(mContext, 0,
                    new Intent(mContext, StkAppService.class), 0);

            mNotificationManager.notify(STK_NOTIFICATION_ID, notification);
}
}








