
//<Beginning of snippet n. 0>

old mode 100644
new mode 100755

protected boolean mStorageAvailable = true;
protected boolean mReportMemoryStatusPending = false;

    protected static int mRemaingMessages = -1;

protected static int getNextConcatenatedRef() {
sConcatenatedRef += 1;
return sConcatenatedRef;

if (sentIntent != null) {
try {
                    if(mRemaingMessages > -1)
                        mRemaingMessages--;

                    if(mRemaingMessages == 0) {
                        Intent sendNext = new Intent();
                        sendNext.putExtra("SendNextMsg", true);
                        sentIntent.send(mContext, Activity.RESULT_OK, sendNext);
                    } else {
                        sentIntent.send(Activity.RESULT_OK);
                    }
} catch (CanceledException ex) {}
}
} else {
if (ar.result != null) {
fillIn.putExtra("errorCode", ((SmsResponse)ar.result).errorCode);
}
                    if(mRemaingMessages > -1)
                        mRemaingMessages--;

                    if(mRemaingMessages == 0)
                        fillIn.putExtra("SendNextMsg", true);

                    tracker.mSentIntent.send(mContext, RESULT_ERROR_GENERIC_FAILURE, fillIn);
} catch (CanceledException ex) {}
}
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


int msgCount = parts.size();
int encoding = android.telephony.SmsMessage.ENCODING_UNKNOWN;

        mRemaingMessages = msgCount;

for (int i = 0; i < msgCount; i++) {
TextEncodingDetails details = SmsMessage.calculateLength(parts.get(i), false);
if (encoding != details.codeUnitSize
int msgCount = parts.size();
int encoding = android.telephony.SmsMessage.ENCODING_UNKNOWN;

        mRemaingMessages = msgCount;

for (int i = 0; i < msgCount; i++) {
TextEncodingDetails details = SmsMessage.calculateLength(parts.get(i), false);
if (encoding != details.codeUnitSize

//<End of snippet n. 1>








