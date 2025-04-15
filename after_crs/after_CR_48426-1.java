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
        if (msg == null || msg.text == null) {
mNotificationManager.cancel(STK_NOTIFICATION_ID);
} else {
            /*
             * For showing large-format notifications that include a lot of
             * text, Notification.BigTextStyle can be used. This is a helper
             * class and is a "rebuilder": It consumes a Builder object
             * and modifies its behavior.
             */
            Notification.Builder notificationBuilder = new Notification.Builder(mContext);

            if (notificationBuilder != null) {
                notificationBuilder.setSmallIcon(
                        com.android.internal.R.drawable.stat_notify_sim_toolkit);
                notificationBuilder.setAutoCancel(false);

                RemoteViews contentView = new RemoteViews(PACKAGE_NAME,
                        com.android.internal.R.layout.status_bar_latest_event_content);

                if (contentView != null) {
                    // Set text and icon for the status bar and notification body.
                    if (!msg.iconSelfExplanatory) {
                        notificationBuilder.setTicker(msg.text);
                        contentView.setTextViewText(com.android.internal.R.id.text, msg.text);
                    }
                    if (msg.icon != null) {
                        contentView.setImageViewBitmap(com.android.internal.R.id.icon, msg.icon);
                    } else {
                        contentView.setImageViewResource(
com.android.internal.R.id.icon,
com.android.internal.R.drawable.stat_notify_sim_toolkit);
                    }

                    notificationBuilder.setContent(contentView);
                    notificationBuilder.setContentIntent(PendingIntent.getService(mContext, 0,
                            new Intent(mContext, StkAppService.class), 0));

                    Notification.BigTextStyle notifBigTextStyle =
                            new Notification.BigTextStyle(notificationBuilder);
                    if (notifBigTextStyle != null) {
                        notifBigTextStyle.bigText(msg.text);
                        mNotificationManager.notify(STK_NOTIFICATION_ID, notifBigTextStyle.build());
                    }
                }
            }
}
}








