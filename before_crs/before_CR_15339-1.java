/*Replaced /sdcard with Environment.getExternalStorageDirectory()

Change-Id:I7eb4480771ed9f974d34ff05770502d313c47e58*/
//Synthetic comment -- diff --git a/src/com/android/contacts/ImportVCardActivity.java b/src/com/android/contacts/ImportVCardActivity.java
//Synthetic comment -- index 8fd9c0d..9730986 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.pim.vcard.EntryCommitter;
//Synthetic comment -- @@ -237,7 +238,7 @@
getString(R.string.reading_vcard_files));
mProgressDialogForReadVCard.setMax(mSelectedVCardFileList.size());
mProgressDialogForReadVCard.setProgress(0);
                    
for (VCardFile vcardFile : mSelectedVCardFileList) {
if (mCanceled) {
return;
//Synthetic comment -- @@ -277,7 +278,7 @@
}
builder.append(fileName);
}
                        
mHandler.post(new DialogDisplayer(
getString(R.string.fail_reason_failed_to_read_files,
builder.toString())));
//Synthetic comment -- @@ -406,7 +407,7 @@
public static final int IMPORT_MULTIPLE = 1;
public static final int IMPORT_ALL = 2;
public static final int IMPORT_TYPE_SIZE = 3;
        
private int mCurrentIndex;

public void onClick(DialogInterface dialog, int which) {
//Synthetic comment -- @@ -429,7 +430,7 @@
}
}
}
    
private class VCardSelectedListener implements
DialogInterface.OnClickListener, DialogInterface.OnMultiChoiceClickListener {
private int mCurrentIndex;
//Synthetic comment -- @@ -447,7 +448,7 @@
if (mSelectedIndexSet != null) {
List<VCardFile> selectedVCardFileList = new ArrayList<VCardFile>();
int size = mAllVCardFileList.size();
                    // We'd like to sort the files by its index, so we do not use Set iterator. 
for (int i = 0; i < size; i++) {
if (mSelectedIndexSet.contains(i)) {
selectedVCardFileList.add(mAllVCardFileList.get(i));
//Synthetic comment -- @@ -598,7 +599,7 @@
mHandler.post(new DialogDisplayer(R.id.dialog_select_one_vcard));
}
}
    
private void importMultipleVCardFromSDCard(final List<VCardFile> selectedVCardFileList) {
mHandler.post(new Runnable() {
public void run() {
//Synthetic comment -- @@ -816,12 +817,11 @@
* This method should be called from a thread with a looper (like Activity).
*/
public void startImportVCardFromSdCard() {
        File file = new File("/sdcard");
if (!file.exists() || !file.isDirectory() || !file.canRead()) {
showDialog(R.id.dialog_sdcard_not_found);
} else {
            File sdcardDirectory = new File("/sdcard");
            mVCardScanThread = new VCardScanThread(sdcardDirectory);
showDialog(R.id.dialog_searching_vcard);
}
}







