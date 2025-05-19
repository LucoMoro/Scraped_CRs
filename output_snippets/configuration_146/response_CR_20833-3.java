//<Beginning of snippet n. 0>
protected boolean mStorageAvailable = true;
protected boolean mReportMemoryStatusPending = false;
private static int sConcatenatedRef = 0;

protected static int getNextConcatenatedRef() {
    if (sConcatenatedRef < 255) {
        sConcatenatedRef++;
    }
    return sConcatenatedRef;
}

try {
    if (sentIntent != null) {
        sentIntent.send(Activity.RESULT_OK);
        Log.i("SMS", "SMS sent successfully");
    }
} catch (CanceledException ex) {
    Log.e("SMS", "Sending SMS was canceled", ex);
} catch (PendingIntent.CanceledException ex) {
    Log.e("SMS", "PendingIntent was canceled", ex);
} catch (Exception ex) {
    Log.e("SMS", "Error sending SMS", ex);
}

if (ar.result != null) {
    fillIn.putExtra("errorCode", ((SmsResponse) ar.result).errorCode);
}
tracker.mSentIntent.send(mContext, error, fillIn);
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
for (int i = 0, msgCount = parts.size(), encoding = android.telephony.SmsMessage.ENCODING_UNKNOWN; i < msgCount; i++) {
    TextEncodingDetails details = SmsMessage.calculateLength(parts.get(i), false);
    if (encoding != details.codeUnitSize) {
        encoding = details.codeUnitSize;
    }
    if (msgCount > 1) {
        mStorageAvailable = false;
        mReportMemoryStatusPending = true;
    }
}
mStorageAvailable = true; // Reset after sending
//<End of snippet n. 1>