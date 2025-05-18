//<Beginning of snippet n. 0>

intent.setClassName("com.android.providers.downloads", DownloadReceiver.class.getName());
intent.setData(contentUri);

if (contentUri != null) {
    Cursor downloadCursor = null;
    try {
        downloadCursor = mContext.getContentResolver().query(contentUri, new String[]{Downloads.COLUMN_LAST_MODIFIED}, null, null, null);
        if (downloadCursor != null && downloadCursor.moveToFirst()) {
            n.when = downloadCursor.getLong(downloadCursor.getColumnIndex(Downloads.COLUMN_LAST_MODIFIED));
        } else {
            n.when = System.currentTimeMillis(); // Fallback to current time only if no data is found
        }
        n.setLatestEventInfo(mContext, title, caption, PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        n.deleteIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotificationMgr.notify(c.getInt(idColumn), n);
    } catch (Exception e) {
        // Handle cursor operation specific error
    } finally {
        if (downloadCursor != null) {
            downloadCursor.close();
        }
        c.close();
    }
}

//<End of snippet n. 0>