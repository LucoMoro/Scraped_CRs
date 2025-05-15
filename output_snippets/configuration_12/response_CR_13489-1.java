//<Beginning of snippet n. 0>


intent.setClassName("com.android.providers.downloads",
DownloadReceiver.class.getName());
intent.setData(contentUri);
n.setLatestEventInfo(mContext, title, caption,
PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

intent.setData(contentUri);
n.deleteIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

long downloadTime = c.getLong(lastModColumnId);
n.when = downloadTime;

// Logging for debugging
Log.d("DownloadNotification", "Setting notification time to: " + downloadTime);

mNotificationMgr.notify(c.getInt(idColumn), n);
}
c.close();

//<End of snippet n. 0>