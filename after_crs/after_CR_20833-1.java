/*Fix the Multi-page SMS sending error to several receipents

Change-Id:Iefde94b638413e3c1761f17c3065b20a044e5958*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SMSDispatcher.java b/telephony/java/com/android/internal/telephony/SMSDispatcher.java
old mode 100644
new mode 100755
//Synthetic comment -- index 917e1d8..52032b4

//Synthetic comment -- @@ -153,6 +153,8 @@
protected boolean mStorageAvailable = true;
protected boolean mReportMemoryStatusPending = false;

    protected static int remainMsg = -1;

protected static int getNextConcatenatedRef() {
sConcatenatedRef += 1;
return sConcatenatedRef;
//Synthetic comment -- @@ -463,7 +465,18 @@

if (sentIntent != null) {
try {
                            if(remainMsg > -1)
                                remainMsg--;
                            if(remainMsg == 0)
                                {
                                    Intent sendNext = new Intent();
                                    sendNext.putExtra("SendNextMsg", true);                                                                        
                                    sentIntent.send(mContext, Activity.RESULT_OK, sendNext);
                                }
                            else
                                {
                                    sentIntent.send(Activity.RESULT_OK);                                
                                }
} catch (CanceledException ex) {}
}
} else {
//Synthetic comment -- @@ -502,8 +515,13 @@
if (ar.result != null) {
fillIn.putExtra("errorCode", ((SmsResponse)ar.result).errorCode);
}
			if(remainMsg > -1)
				remainMsg--;
			if(remainMsg == 0)
			{
				fillIn.putExtra("SendNextMsg", true);                            
			}                        
			tracker.mSentIntent.send(mContext, RESULT_ERROR_GENERIC_FAILURE, fillIn);
} catch (CanceledException ex) {}
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index 49fa5da..0344f2e 100755

//Synthetic comment -- @@ -180,6 +180,8 @@
int msgCount = parts.size();
int encoding = android.telephony.SmsMessage.ENCODING_UNKNOWN;

        remainMsg = msgCount;	

for (int i = 0; i < msgCount; i++) {
TextEncodingDetails details = SmsMessage.calculateLength(parts.get(i), false);
if (encoding != details.codeUnitSize
//Synthetic comment -- @@ -269,6 +271,8 @@
int msgCount = parts.size();
int encoding = android.telephony.SmsMessage.ENCODING_UNKNOWN;

        remainMsg = msgCount;	

for (int i = 0; i < msgCount; i++) {
TextEncodingDetails details = SmsMessage.calculateLength(parts.get(i), false);
if (encoding != details.codeUnitSize







