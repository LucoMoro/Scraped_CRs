/*Fix mRequestsLists -> mRequestList

first of several fixes.

Change-Id:I362fbfd446aed6714e6c89204ee095d03474c26f*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index c3256df..dbfe5d9 100644

//Synthetic comment -- @@ -235,7 +235,7 @@
int mRequestMessagesWaiting;

//I'd rather this be LinkedList or something
    ArrayList<RILRequest> mRequestsList = new ArrayList<RILRequest>();

Object     mLastNITZTimeInfo;

//Synthetic comment -- @@ -320,8 +320,8 @@
return;
}

                        synchronized (mRequestsList) {
                            mRequestsList.add(rr);
mRequestMessagesWaiting++;
}

//Synthetic comment -- @@ -403,13 +403,13 @@
mRequestMessagesWaiting = 0;

if (RILJ_LOGD) {
                                    synchronized (mRequestsList) {
                                        int count = mRequestsList.size();
Rlog.d(LOG_TAG, "WAKE_LOCK_TIMEOUT " +
" mRequestList=" + count);

for (int i = 0; i < count; i++) {
                                            rr = mRequestsList.get(i);
Rlog.d(LOG_TAG, i + ": [" + rr.mSerial + "] "
+ requestToString(rr.mRequest));
}
//Synthetic comment -- @@ -598,7 +598,7 @@
RILRequest.resetSerial();

// Clear request list on close
                clearRequestsList(RADIO_NOT_AVAILABLE, false);
}} catch (Throwable tr) {
Rlog.e(LOG_TAG,"Uncaught exception", tr);
}
//Synthetic comment -- @@ -2145,14 +2145,14 @@
}

/**
     * Release each request in mReqeustsList then clear the list
* @param error is the RIL_Errno sent back
     * @param loggable true means to print all requests in mRequestslist
*/
    private void clearRequestsList(int error, boolean loggable) {
RILRequest rr;
        synchronized (mRequestsList) {
            int count = mRequestsList.size();
if (RILJ_LOGD && loggable) {
Rlog.d(LOG_TAG, "WAKE_LOCK_TIMEOUT " +
" mReqPending=" + mRequestMessagesPending +
//Synthetic comment -- @@ -2160,7 +2160,7 @@
}

for (int i = 0; i < count ; i++) {
                rr = mRequestsList.get(i);
if (RILJ_LOGD && loggable) {
Rlog.d(LOG_TAG, i + ": [" + rr.mSerial + "] " +
requestToString(rr.mRequest));
//Synthetic comment -- @@ -2168,18 +2168,18 @@
rr.onError(error, null);
rr.release();
}
            mRequestsList.clear();
mRequestMessagesWaiting = 0;
}
}

private RILRequest findAndRemoveRequestFromList(int serial) {
        synchronized (mRequestsList) {
            for (int i = 0, s = mRequestsList.size() ; i < s ; i++) {
                RILRequest rr = mRequestsList.get(i);

if (rr.mSerial == serial) {
                    mRequestsList.remove(i);
if (mRequestMessagesWaiting > 0)
mRequestMessagesWaiting--;
return rr;
//Synthetic comment -- @@ -3880,13 +3880,13 @@
pw.println(" mReceiver=" + mReceiver);
pw.println(" mWakeLock=" + mWakeLock);
pw.println(" mWakeLockTimeout=" + mWakeLockTimeout);
        synchronized (mRequestsList) {
pw.println(" mRequestMessagesPending=" + mRequestMessagesPending);
pw.println(" mRequestMessagesWaiting=" + mRequestMessagesWaiting);
            int count = mRequestsList.size();
pw.println(" mRequestList count=" + count);
for (int i = 0; i < count; i++) {
                RILRequest rr = mRequestsList.get(i);
pw.println("  [" + rr.mSerial + "] " + requestToString(rr.mRequest));
}
}







