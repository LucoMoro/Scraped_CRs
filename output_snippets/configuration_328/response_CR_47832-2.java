//<Beginning of snippet n. 0>
new AsyncTask<Void, Void, Void>() {
    @Override
    protected Void doInBackground(Void... voids) {
        mContext.startService(intent);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mMessageItem.getMmsDownloadStatus() == DownloadManager.STATE_DOWNLOADING) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    showDownloadingAttachment();
                    mDateView.setText(buildTimestampLine(msgSizeText + " " + mMessageItem.mTimestamp));
                }
            });
        }
    }
}.execute();
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public static final int STATE_DOWNLOADING       = 0x81;
public static final int STATE_TRANSIENT_FAILURE = 0x82;
public static final int STATE_PERMANENT_FAILURE = 0x87;

private final Context mContext;
private final Handler mHandler;
//<End of snippet n. 1>