/*Fix mRequestsLists -> mRequestList

first of several fixes.

Change-Id:I362fbfd446aed6714e6c89204ee095d03474c26f*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index c3256df..dbfe5d9 100644

//Synthetic comment -- @@ -235,7 +235,7 @@
int mRequestMessagesWaiting;

//I'd rather this be LinkedList or something
    ArrayList<RILRequest> mRequestList = new ArrayList<RILRequest>();

Object     mLastNITZTimeInfo;

//Synthetic comment -- @@ -320,8 +320,8 @@
return;
}

                        synchronized (mRequestList) {
                            mRequestList.add(rr);
mRequestMessagesWaiting++;
}

//Synthetic comment -- @@ -403,13 +403,13 @@
mRequestMessagesWaiting = 0;

if (RILJ_LOGD) {
                                    synchronized (mRequestList) {
                                        int count = mRequestList.size();
Rlog.d(LOG_TAG, "WAKE_LOCK_TIMEOUT " +
" mRequestList=" + count);

for (int i = 0; i < count; i++) {
                                            rr = mRequestList.get(i);
Rlog.d(LOG_TAG, i + ": [" + rr.mSerial + "] "
+ requestToString(rr.mRequest));
}
//Synthetic comment -- @@ -598,7 +598,7 @@
RILRequest.resetSerial();

// Clear request list on close
                clearRequestList(RADIO_NOT_AVAILABLE, false);
}} catch (Throwable tr) {
Rlog.e(LOG_TAG,"Uncaught exception", tr);
}
//Synthetic comment -- @@ -2145,14 +2145,14 @@
}

/**
     * Release each request in mRequestList then clear the list
* @param error is the RIL_Errno sent back
     * @param loggable true means to print all requests in mRequestList
*/
    private void clearRequestList(int error, boolean loggable) {
RILRequest rr;
        synchronized (mRequestList) {
            int count = mRequestList.size();
if (RILJ_LOGD && loggable) {
Rlog.d(LOG_TAG, "WAKE_LOCK_TIMEOUT " +
" mReqPending=" + mRequestMessagesPending +
//Synthetic comment -- @@ -2160,7 +2160,7 @@
}

for (int i = 0; i < count ; i++) {
                rr = mRequestList.get(i);
if (RILJ_LOGD && loggable) {
Rlog.d(LOG_TAG, i + ": [" + rr.mSerial + "] " +
requestToString(rr.mRequest));
//Synthetic comment -- @@ -2168,18 +2168,18 @@
rr.onError(error, null);
rr.release();
}
            mRequestList.clear();
mRequestMessagesWaiting = 0;
}
}

private RILRequest findAndRemoveRequestFromList(int serial) {
        synchronized (mRequestList) {
            for (int i = 0, s = mRequestList.size() ; i < s ; i++) {
                RILRequest rr = mRequestList.get(i);

if (rr.mSerial == serial) {
                    mRequestList.remove(i);
if (mRequestMessagesWaiting > 0)
mRequestMessagesWaiting--;
return rr;
//Synthetic comment -- @@ -3880,13 +3880,13 @@
pw.println(" mReceiver=" + mReceiver);
pw.println(" mWakeLock=" + mWakeLock);
pw.println(" mWakeLockTimeout=" + mWakeLockTimeout);
        synchronized (mRequestList) {
pw.println(" mRequestMessagesPending=" + mRequestMessagesPending);
pw.println(" mRequestMessagesWaiting=" + mRequestMessagesWaiting);
            int count = mRequestList.size();
pw.println(" mRequestList count=" + count);
for (int i = 0; i < count; i++) {
                RILRequest rr = mRequestList.get(i);
pw.println("  [" + rr.mSerial + "] " + requestToString(rr.mRequest));
}
}







