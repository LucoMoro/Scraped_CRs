/*bluetooth/opp: fixed crash when file was deleted during batch sending

When sending multiple files via opp in a single batch, if a file was
deleted before being sent a bluetooth crash occured because of a non
caught IllegalStateException.

This patch catches this exception within generateFileInfo method
and cleanly returns an BluetoothOppSendFileInfo object with status
set to BluetoothShare.STATUS_FILE_ERROR.

Change-Id:Ideed7ea8a81ce5c638247c3714752810b775673eAuthor: Fabien Peix <fabienx.peix@intel.com>
Signed-off-by: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 10653 17106*/




//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppSendFileInfo.java b/src/com/android/bluetooth/opp/BluetoothOppSendFileInfo.java
//Synthetic comment -- index a9d0578..3fa6075 100644

//Synthetic comment -- @@ -108,10 +108,19 @@
// This will allow more 3rd party applications to share files via
// bluetooth
if (scheme.equals("content")) {
            Cursor metadataCursor = null;
            try {
                contentType = contentResolver.getType(u);
                metadataCursor = contentResolver.query(u, new String[] {
                        OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE
                }, null, null, null);
            }
            catch (IllegalStateException e) {
                // Manages cases where file was deleted in the mean time
                Log.e(TAG, "Cannot access file, IllegalStateException : ", e);
                return new BluetoothOppSendFileInfo(null, null, 0, null,
                        BluetoothShare.STATUS_FILE_ERROR, dest);
            }
if (metadataCursor != null) {
try {
if (metadataCursor.moveToFirst()) {







