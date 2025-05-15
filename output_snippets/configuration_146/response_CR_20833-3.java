//<Beginning of snippet n. 0>
protected boolean mStorageAvailable = true;
protected boolean mReportMemoryStatusPending = false;
private static int sConcatenatedRef = 0;

protected static int getNextConcatenatedRef() {
    return ++sConcatenatedRef;
}

public void sendMessage(Intent sentIntent, SmsResponse ar) {
    if (sentIntent != null) {
        try {
            sentIntent.send(Activity.RESULT_OK);
        } catch (CanceledException ex) {
            // Log or handle the CanceledException accordingly
        }
    } else {
        if (ar != null && ar.result != null) {
            fillIn.putExtra("errorCode", ((SmsResponse) ar.result).errorCode);
        }
        tracker.mSentIntent.send(mContext, error, fillIn);
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
int msgCount = parts.size();
int encoding = android.telephony.SmsMessage.ENCODING_UNKNOWN;

for (int i = 0; i < msgCount; i++) {
    TextEncodingDetails details = SmsMessage.calculateLength(parts.get(i), false);
    if (encoding != details.codeUnitSize) {
        encoding = details.codeUnitSize;
    }
    // Validate message and handle message parts appropriately
}
//<End of snippet n. 1>