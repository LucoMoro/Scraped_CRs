//<Beginning of snippet n. 0>
intent.setClassName("com.android.providers.downloads",
DownloadReceiver.class.getName());
intent.setData(contentUri);
n.when = System.currentTimeMillis(); // Set download timestamp accurately
n.setLatestEventInfo(mContext, title, caption,
PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)); // Ensure unique pending intent for each download

intent.setData(contentUri);
n.deleteIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT); // Ensure unique pending intent for delete

mNotificationMgr.notify(c.getInt(idColumn), n);
}
c.close();
//<End of snippet n. 0>