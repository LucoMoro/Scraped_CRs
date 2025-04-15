/*Telephony: Fix error in importing SIM contacts

Number of ADN records is not matching with the
number of IAP records resulting in the error log
"Error: Improper ICC card: No IAP record for ADN,
continuing".

EFiap, EFemail are Type 1 files that contain as
many records as the reference/master file(EFADN,
EFADN1). Currently, mIapFileRecord and
mEmailFileRecord are equated to the single EFiap
and EFemail content. Since there can be more than
1 EFiap, EFemail file, 1st file content will be
last when the 2nd file is loaded.

On loading of EFiap, EFemail, instead of equating
the contents, append the contents to the respective
list.

Change-Id:Ie8ad459c1140135b701ea9f171e4e8fba380f58aAuthor: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 64280*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/UsimPhoneBookManager.java b/src/java/com/android/internal/telephony/gsm/UsimPhoneBookManager.java
//Synthetic comment -- index f46b461..3747877 100755

//Synthetic comment -- @@ -78,6 +78,9 @@
public UsimPhoneBookManager(IccFileHandler fh, AdnRecordCache cache) {
mFh = fh;
mPhoneBookRecords = new ArrayList<AdnRecord>();
mPbrFile = null;
// We assume its present, after the first read this is updated.
// So we don't have to read from UICC if its not present on subsequent reads.
//Synthetic comment -- @@ -87,8 +90,8 @@

public void reset() {
mPhoneBookRecords.clear();
        mIapFileRecord = null;
        mEmailFileRecord = null;
mPbrFile = null;
mIsPbrPresent = true;
mRefreshCache = false;
//Synthetic comment -- @@ -359,7 +362,7 @@
log("Loading USIM IAP records done");
ar = (AsyncResult) msg.obj;
if (ar.exception == null) {
                mIapFileRecord = ((ArrayList<byte[]>)ar.result);
}
synchronized (mLock) {
mLock.notify();
//Synthetic comment -- @@ -369,7 +372,7 @@
log("Loading USIM Email records done");
ar = (AsyncResult) msg.obj;
if (ar.exception == null) {
                mEmailFileRecord = ((ArrayList<byte[]>)ar.result);
}

synchronized (mLock) {







