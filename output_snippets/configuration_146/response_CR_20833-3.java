//<Beginning of snippet n. 0>
protected boolean mStorageAvailable = true;
protected boolean mReportMemoryStatusPending = false;
private static int sConcatenatedRef = 0;

protected static synchronized int getNextConcatenatedRef() {
    sConcatenatedRef += 1;
    return sConcatenatedRef;
}

if (sentIntent != null) {
    try {
        sentIntent.send(Activity.RESULT_OK);
    } catch (CanceledException ex) {
        // Handle exception appropriately
    }
} else {
    if (ar.result != null) {
        fillIn.putExtra("errorCode", ((SmsResponse) ar.result).errorCode);
    }
    try {
        tracker.mSentIntent.send(mContext, error, fillIn);
    } catch (CanceledException ex) {
        // Handle exception appropriately
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
int encoding = android.telephony.SmsMessage.ENCODING_UNKNOWN;
int msgCount = parts.size();

if (mStorageAvailable) {
    for (int i = 0; i < msgCount; i++) {
        TextEncodingDetails details = SmsMessage.calculateLength(parts.get(i), false);
        if (encoding != details.codeUnitSize) {
            encoding = details.codeUnitSize;
        }
    }
}
//<End of snippet n. 1>