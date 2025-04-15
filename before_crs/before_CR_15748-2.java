/*Enables writing to USIM phonebook (fixes issue 8976).

Change-Id:I4d50b4b2c6821101c4afed91b6ac96ecefd2f5a8*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/AdnRecordCache.java b/telephony/java/com/android/internal/telephony/AdnRecordCache.java
//Synthetic comment -- index c8c0658..fbdf8fc 100644

//Synthetic comment -- @@ -40,6 +40,9 @@
SparseArray<ArrayList<AdnRecord>> adnLikeFiles
= new SparseArray<ArrayList<AdnRecord>>();

// People waiting for ADN-like files to be loaded
SparseArray<ArrayList<Message>> adnLikeWaiters
= new SparseArray<ArrayList<Message>>();
//Synthetic comment -- @@ -68,6 +71,7 @@
*/
public void reset() {
adnLikeFiles.clear();
mUsimPhoneBookManager.reset();

clearWaiters();
//Synthetic comment -- @@ -103,6 +107,20 @@
}

/**
* Returns extension ef associated with ADN-like EF or -1 if
* we don't know.
*
//Synthetic comment -- @@ -186,7 +204,7 @@
}

ArrayList<AdnRecord>  oldAdnList;
        oldAdnList = getRecordsIfLoaded(efid);

if (oldAdnList == null) {
sendErrorResponse(response, "Adn list not exist for EF:" + efid);
//Synthetic comment -- @@ -228,14 +246,17 @@
* record
*/
public void
    requestLoadAllAdnLike (int efid, int extensionEf, Message response) {
ArrayList<Message> waiters;
ArrayList<AdnRecord> result;

        if (efid == EF_PBR) {
result = mUsimPhoneBookManager.loadEfFilesFromUsim();
        } else {
            result = getRecordsIfLoaded(efid);
}

// Have we already loaded this efid?
//Synthetic comment -- @@ -330,7 +351,7 @@
AdnRecord adn = (AdnRecord) (ar.userObj);

if (ar.exception == null) {
                    adnLikeFiles.get(efid).set(index - 1, adn);
}

Message response = userWriteResponse.get(efid);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java b/telephony/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java
//Synthetic comment -- index 48257cc7..4370dce 100644

//Synthetic comment -- @@ -235,13 +235,15 @@
"Requires android.permission.READ_CONTACTS permission");
}

        efid = updateEfForIccType(efid);
        if (DBG) logd("getAdnRecordsInEF: efid=" + efid);

synchronized(mLock) {
checkThread();
Message response = mBaseHandler.obtainMessage(EVENT_LOAD_DONE);
            adnCache.requestLoadAllAdnLike(efid, adnCache.extensionEfForEf(efid), response);
try {
mLock.wait();
} catch (InterruptedException e) {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/UsimPhoneBookManager.java b/telephony/java/com/android/internal/telephony/gsm/UsimPhoneBookManager.java
old mode 100644
new mode 100755
//Synthetic comment -- index 41e527ce..064bf26

//Synthetic comment -- @@ -285,7 +285,7 @@
if (fileIds == null || fileIds.isEmpty()) return;

mAdnCache.requestLoadAllAdnLike(fileIds.get(USIM_EFADN_TAG),
            fileIds.get(USIM_EFEXT1_TAG), obtainMessage(EVENT_USIM_ADN_LOAD_DONE));
try {
mLock.wait();
} catch (InterruptedException e) {







