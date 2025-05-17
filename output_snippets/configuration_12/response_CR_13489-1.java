//<Beginning of snippet n. 0>
intent.setClassName("com.android.providers.downloads", DownloadReceiver.class.getName());
intent.setData(contentUri);
long lastModificationTime = c.getLong(lastModColumnId);
n.when = lastModificationTime;
Log.d("Notification", "Setting notification time: " + lastModificationTime);
n.setLatestEventInfo(mContext, title, caption, PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

n.deleteIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
mNotificationMgr.notify(c.getInt(idColumn), n);
c.close();
//<End of snippet n. 0>