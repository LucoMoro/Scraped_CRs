/*Enables writing to USIM phonebook (fixes issue 8976).

Change-Id:I60c4e4fab58d13a83193492d828b0b519875c710*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/AdnRecordCache.java b/telephony/java/com/android/internal/telephony/AdnRecordCache.java
//Synthetic comment -- index c8c0658..a1a8457 100644

//Synthetic comment -- @@ -186,7 +186,12 @@
}

ArrayList<AdnRecord>  oldAdnList;
        oldAdnList = getRecordsIfLoaded(efid);

if (oldAdnList == null) {
sendErrorResponse(response, "Adn list not exist for EF:" + efid);
//Synthetic comment -- @@ -208,6 +213,37 @@
return;
}

Message pendingResponse = userWriteResponse.get(efid);

if (pendingResponse != null) {
//Synthetic comment -- @@ -331,6 +367,7 @@

if (ar.exception == null) {
adnLikeFiles.get(efid).set(index - 1, adn);
}

Message response = userWriteResponse.get(efid);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java b/telephony/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java
//Synthetic comment -- index 48257cc7..2f22d74 100644

//Synthetic comment -- @@ -144,6 +144,9 @@
if (DBG) logd("updateAdnRecordsInEfBySearch: efid=" + efid +
" ("+ oldTag + "," + oldPhoneNumber + ")"+ "==>" +
" ("+ newTag + "," + newPhoneNumber + ")"+ " pin2=" + pin2);
synchronized(mLock) {
checkThread();
success = false;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/UsimPhoneBookManager.java b/telephony/java/com/android/internal/telephony/gsm/UsimPhoneBookManager.java
old mode 100644
new mode 100755
//Synthetic comment -- index 41e527ce..b642541

//Synthetic comment -- @@ -53,6 +53,7 @@
private ArrayList<byte[]> mIapFileRecord;
private ArrayList<byte[]> mEmailFileRecord;
private Map<Integer, ArrayList<String>> mEmailsForAdnRec;

private static final int EVENT_PBR_LOAD_DONE = 1;
private static final int EVENT_USIM_ADN_LOAD_DONE = 2;
//Synthetic comment -- @@ -91,11 +92,19 @@
mEmailFileRecord = null;
mPbrFile = null;
mIsPbrPresent = true;
}

public ArrayList<AdnRecord> loadEfFilesFromUsim() {
synchronized (mLock) {
            if (!mPhoneBookRecords.isEmpty()) return mPhoneBookRecords;
if (!mIsPbrPresent) return null;

// Check if the PBR file is present in the cache, if not read it
//Synthetic comment -- @@ -116,6 +125,20 @@
return mPhoneBookRecords;
}

private void readPbrFileAndWait() {
mPhone.getIccFileHandler().loadEFLinearFixedAll(EF_PBR, obtainMessage(EVENT_PBR_LOAD_DONE));
try {







