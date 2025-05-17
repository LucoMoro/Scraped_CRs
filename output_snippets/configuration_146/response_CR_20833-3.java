//<Beginning of snippet n. 0>

protected boolean mStorageAvailable = true;
protected boolean mReportMemoryStatusPending = false;
private static int sConcatenatedRef = 0;

protected static synchronized int getNextConcatenatedRef() {
    return ++sConcatenatedRef;
}

protected void sendSmsIntent(Intent sentIntent, SmsResponse ar, Context mContext, int error, Bundle fillIn) {
    if (sentIntent != null) {
        try {
            sentIntent.send(Activity.RESULT_OK);
        } catch (CanceledException ex) {
            // Log the exception for debugging
        } catch (Exception ex) {
            // Handle other exceptions
        }
    } else {
        if (ar.result != null) {
            fillIn.putExtra("errorCode", ((SmsResponse) ar.result).errorCode);
        }
        tracker.mSentIntent.send(mContext, error, fillIn);
    }
} 

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

if (parts != null && !parts.isEmpty()) {
    int msgCount = parts.size();
    int encoding = android.telephony.SmsMessage.ENCODING_UNKNOWN;
    
    for (int i = 0; i < msgCount; i++) {
        TextEncodingDetails details = SmsMessage.calculateLength(parts.get(i), false);
        if (encoding != details.codeUnitSize) {
            encoding = details.codeUnitSize;
        }
        if (details.codeUnitSize > SmsMessage.getMaxLength()) {
            // Handle segment excess case
        }
    }
} else {
    // Handle empty parts case
}

//<End of snippet n. 1>