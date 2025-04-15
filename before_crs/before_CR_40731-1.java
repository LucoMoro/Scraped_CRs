/*bluetooth: add illegal exception handler in BluetoothOppTransferHistory

In transfercomplete, the query record generate an illegal exception due to URL not found.
Add the exception catch in case of URL not found

Change-Id:I2d59033f8eefe9ae9a47a64f1daa37021feb3401Author: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 8948 17106*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppTransferHistory.java b/src/com/android/bluetooth/opp/BluetoothOppTransferHistory.java
//Synthetic comment -- index 697e3cb..1f6d56e 100644

//Synthetic comment -- @@ -275,9 +275,15 @@
private void openCompleteTransfer() {
int sessionId = mTransferCursor.getInt(mIdColumnId);
Uri contentUri = Uri.parse(BluetoothShare.CONTENT_URI + "/" + sessionId);
        BluetoothOppTransferInfo transInfo = BluetoothOppUtility.queryRecord(this, contentUri);
        if (transInfo == null) {
            Log.e(TAG, "Error: Can not get data from db");
return;
}
if (transInfo.mDirection == BluetoothShare.DIRECTION_INBOUND







