/*Enables writing to USIM phonebook (fixes issue 8976).

Change-Id:I60c4e4fab58d13a83193492d828b0b519875c710*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/AdnRecordCache.java b/telephony/java/com/android/internal/telephony/AdnRecordCache.java
//Synthetic comment -- index c8c0658..50bc70d 100644

//Synthetic comment -- @@ -186,7 +186,12 @@
}

ArrayList<AdnRecord>  oldAdnList;

        if (efid == EF_PBR) {
            oldAdnList = mUsimPhoneBookManager.loadEfFilesFromUsim();
        } else {
            oldAdnList = getRecordsIfLoaded(efid);
        }

if (oldAdnList == null) {
sendErrorResponse(response, "Adn list not exist for EF:" + efid);
//Synthetic comment -- @@ -208,6 +213,37 @@
return;
}

        if (efid == EF_PBR) {
            AdnRecord foundAdn = oldAdnList.get(index-1);
            efid = foundAdn.efid;
            extensionEF = foundAdn.extRecord;

            oldAdnList = getRecordsIfLoaded(efid);
            if (oldAdnList == null) {
                sendErrorResponse(response, "Adn list not exist for EF:" + efid);
                return;
            }

            index = -1;
            count = 1;
            for (Iterator<AdnRecord> it = oldAdnList.iterator(); it.hasNext(); ) {
                if (oldAdn.isEqual(it.next())) {
                    index = count;
                    break;
                }
                count++;
            }

            if (index == -1) {
                sendErrorResponse(response, "Adn record don't exist for " + oldAdn);
                return;
            }

            newAdn.efid = efid;
            newAdn.extRecord = extensionEF;
            newAdn.recordNumber = foundAdn.recordNumber;
	}

Message pendingResponse = userWriteResponse.get(efid);

if (pendingResponse != null) {
//Synthetic comment -- @@ -331,6 +367,7 @@

if (ar.exception == null) {
adnLikeFiles.get(efid).set(index - 1, adn);
                    mUsimPhoneBookManager.invalidateCache();
}

Message response = userWriteResponse.get(efid);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java b/telephony/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java
//Synthetic comment -- index 48257cc7..c10858c 100644

//Synthetic comment -- @@ -144,6 +144,9 @@
if (DBG) logd("updateAdnRecordsInEfBySearch: efid=" + efid +
" ("+ oldTag + "," + oldPhoneNumber + ")"+ "==>" +
" ("+ newTag + "," + newPhoneNumber + ")"+ " pin2=" + pin2);

        efid = updateEfForIccType(efid);

synchronized(mLock) {
checkThread();
success = false;
//Synthetic comment -- @@ -241,6 +244,7 @@
synchronized(mLock) {
checkThread();
Message response = mBaseHandler.obtainMessage(EVENT_LOAD_DONE);

adnCache.requestLoadAllAdnLike(efid, adnCache.extensionEfForEf(efid), response);
try {
mLock.wait();








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/UsimPhoneBookManager.java b/telephony/java/com/android/internal/telephony/gsm/UsimPhoneBookManager.java
old mode 100644
new mode 100755
//Synthetic comment -- index 41e527ce..b642541

//Synthetic comment -- @@ -53,6 +53,7 @@
private ArrayList<byte[]> mIapFileRecord;
private ArrayList<byte[]> mEmailFileRecord;
private Map<Integer, ArrayList<String>> mEmailsForAdnRec;
    private boolean mRefreshCache = false;

private static final int EVENT_PBR_LOAD_DONE = 1;
private static final int EVENT_USIM_ADN_LOAD_DONE = 2;
//Synthetic comment -- @@ -91,11 +92,19 @@
mEmailFileRecord = null;
mPbrFile = null;
mIsPbrPresent = true;
        mRefreshCache = false;
}

public ArrayList<AdnRecord> loadEfFilesFromUsim() {
synchronized (mLock) {
            if (!mPhoneBookRecords.isEmpty()) {
                if (mRefreshCache) {
                    mRefreshCache = false;
                    refreshCache();
                }
                return mPhoneBookRecords;
            }

if (!mIsPbrPresent) return null;

// Check if the PBR file is present in the cache, if not read it
//Synthetic comment -- @@ -116,6 +125,20 @@
return mPhoneBookRecords;
}

    private void refreshCache() {
        if (mPbrFile == null) return;
        mPhoneBookRecords.clear();

        int numRecs = mPbrFile.mFileIds.size();
        for (int i = 0; i < numRecs; i++) {
            readAdnFileAndWait(i);
        }
    }

    public void invalidateCache() {
        mRefreshCache = true;
    }

private void readPbrFileAndWait() {
mPhone.getIccFileHandler().loadEFLinearFixedAll(EF_PBR, obtainMessage(EVENT_PBR_LOAD_DONE));
try {







