/*Enables writing to USIM phonebook (fixes issue 8976).

Change-Id:I4d50b4b2c6821101c4afed91b6ac96ecefd2f5a8*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/AdnRecordCache.java b/telephony/java/com/android/internal/telephony/AdnRecordCache.java
//Synthetic comment -- index c8c0658..fbdf8fc 100644

//Synthetic comment -- @@ -40,6 +40,9 @@
SparseArray<ArrayList<AdnRecord>> adnLikeFiles
= new SparseArray<ArrayList<AdnRecord>>();

    // Indexed by EF ID. Stores the mapping between the EF ID asked for and the EF ID really read.
    SparseArray<Integer> efidAliases = new SparseArray<Integer>();

// People waiting for ADN-like files to be loaded
SparseArray<ArrayList<Message>> adnLikeWaiters
= new SparseArray<ArrayList<Message>>();
//Synthetic comment -- @@ -68,6 +71,7 @@
*/
public void reset() {
adnLikeFiles.clear();
        efidAliases.clear();
mUsimPhoneBookManager.reset();

clearWaiters();
//Synthetic comment -- @@ -103,6 +107,20 @@
}

/**
     * Looks for the EF ID under which the cache array has really been created.
     */
    private int getRealEfid(int efid) {
        Integer realEfid = efidAliases.get(efid);
        if (realEfid == null) return efid; // no indirection found

        efid = realEfid.intValue();
        realEfid = efidAliases.get(efid); // 2nd level of indirection?
        if (realEfid != null) efid = realEfid.intValue();

        return efid;
    }

    /**
* Returns extension ef associated with ADN-like EF or -1 if
* we don't know.
*
//Synthetic comment -- @@ -186,7 +204,7 @@
}

ArrayList<AdnRecord>  oldAdnList;
        oldAdnList = getRecordsIfLoaded(getRealEfid(efid));

if (oldAdnList == null) {
sendErrorResponse(response, "Adn list not exist for EF:" + efid);
//Synthetic comment -- @@ -228,14 +246,17 @@
* record
*/
public void
    requestLoadAllAdnLike (int efid, int extensionEf, Message response, int aliasEfid) {
ArrayList<Message> waiters;
ArrayList<AdnRecord> result;

        if (aliasEfid != efid) {
            efidAliases.put(aliasEfid, new Integer(efid));
        }

        result = getRecordsIfLoaded(getRealEfid(efid));
        if (result == null && efid == EF_PBR) {
result = mUsimPhoneBookManager.loadEfFilesFromUsim();
}

// Have we already loaded this efid?
//Synthetic comment -- @@ -330,7 +351,7 @@
AdnRecord adn = (AdnRecord) (ar.userObj);

if (ar.exception == null) {
                    adnLikeFiles.get(getRealEfid(efid)).set(index - 1, adn);
}

Message response = userWriteResponse.get(efid);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java b/telephony/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java
//Synthetic comment -- index 48257cc7..bb76be04 100644

//Synthetic comment -- @@ -235,13 +235,15 @@
"Requires android.permission.READ_CONTACTS permission");
}

        int updatedEfid = updateEfForIccType(efid);
        if (DBG) logd("getAdnRecordsInEF: efid=" + updatedEfid);

synchronized(mLock) {
checkThread();
Message response = mBaseHandler.obtainMessage(EVENT_LOAD_DONE);

            adnCache.requestLoadAllAdnLike(updatedEfid,
                adnCache.extensionEfForEf(updatedEfid), response, efid);
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
            fileIds.get(USIM_EFEXT1_TAG), obtainMessage(EVENT_USIM_ADN_LOAD_DONE), EF_PBR);
try {
mLock.wait();
} catch (InterruptedException e) {







