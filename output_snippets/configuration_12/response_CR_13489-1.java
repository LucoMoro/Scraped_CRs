//<Beginning of snippet n. 0>

intent.setClassName("com.android.providers.downloads",
DownloadReceiver.class.getName());
intent.setData(contentUri);
n.setLatestEventInfo(mContext, title, caption,
PendingIntent.getBroadcast(mContext, 0, intent, 0));

intent.setData(contentUri);
n.deleteIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);

// Set the correct download time for the notification
long downloadTime = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP));
n.when = downloadTime;

mNotificationMgr.notify(c.getInt(idColumn), n);
}
c.close();

//<End of snippet n. 0>