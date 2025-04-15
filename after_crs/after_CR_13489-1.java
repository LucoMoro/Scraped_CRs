/*Show correct date for downloads in the statusbar

The date was not set properly in notifications when the text for
expanded view was created.

This fix sets the displayed time to the download time for each
individual file. The DownloadProvider recreates the notifications
several times but don't set the time before the expanded message
is created. The expanded message will therefore display the time
the notification was created and not the time a file was downloaded.
The displayed time will in most case be the time when the last file
was downloaded. This fix sets the displayed time to the downloaded
time for each individual file.*/




//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadNotification.java b/src/com/android/providers/downloads/DownloadNotification.java
//Synthetic comment -- index 9f54190..f7c1007 100644

//Synthetic comment -- @@ -276,6 +276,7 @@
intent.setClassName("com.android.providers.downloads",
DownloadReceiver.class.getName());
intent.setData(contentUri);
            n.when = c.getLong(lastModColumnId);
n.setLatestEventInfo(mContext, title, caption,
PendingIntent.getBroadcast(mContext, 0, intent, 0));

//Synthetic comment -- @@ -285,8 +286,6 @@
intent.setData(contentUri);
n.deleteIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);

mNotificationMgr.notify(c.getInt(idColumn), n);
}
c.close();







