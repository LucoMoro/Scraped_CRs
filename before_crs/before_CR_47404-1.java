/*Save the export progress when rotating the phone

When rotating the phone which is exporting movies,
the export progress will be lost. The progress bar
will appear to be 0 for a few seconds. In this patch,
The export progress is saved when the video editor
activity is destroyed.

Change-Id:I9f4aa56a43f90b6250cac953a579f1230bdb7cccAuthor: Wangyi Gu <wangyi.gu@intel.com>
Signed-off-by: Wangyi Gu <wangyi.gu@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 37621*/
//Synthetic comment -- diff --git a/src/com/android/videoeditor/VideoEditorActivity.java b/src/com/android/videoeditor/VideoEditorActivity.java
//Synthetic comment -- index ca0b770..2cbfbb5 100755

//Synthetic comment -- @@ -137,6 +137,10 @@
// Threshold in width dip for showing title in action bar.
private static final int SHOW_TITLE_THRESHOLD_WIDTH_DIP = 1000;

private final TimelineRelativeLayout.LayoutCallback mLayoutCallback =
new TimelineRelativeLayout.LayoutCallback() {

//Synthetic comment -- @@ -392,9 +396,11 @@
mRestartPreview = savedInstanceState.getBoolean(STATE_PLAYING);
mCaptureMediaUri = savedInstanceState.getParcelable(STATE_CAPTURE_URI);
mMediaLayoutSelectedPos = savedInstanceState.getInt(STATE_SELECTED_POS_ID, -1);
} else {
mRestartPreview = false;
mMediaLayoutSelectedPos = -1;
}

// Compute the activity width
//Synthetic comment -- @@ -492,6 +498,7 @@
outState.putBoolean(STATE_PLAYING, isPreviewPlaying() || mRestartPreview);
outState.putParcelable(STATE_CAPTURE_URI, mCaptureMediaUri);
outState.putInt(STATE_SELECTED_POS_ID, mMediaLayout.getSelectedViewPos());
}

@Override
//Synthetic comment -- @@ -637,6 +644,7 @@

case R.id.menu_item_export_movie: {
// Present the user with a dialog to choose export options
showDialog(DIALOG_EXPORT_OPTIONS_ID);
return true;
}
//Synthetic comment -- @@ -1444,6 +1452,7 @@
@Override
protected void onExportProgress(int progress) {
if (mExportProgressDialog != null) {
mExportProgressDialog.setProgress(progress);
}
}
//Synthetic comment -- @@ -1453,6 +1462,7 @@
if (mExportProgressDialog != null) {
mExportProgressDialog.dismiss();
mExportProgressDialog = null;
}
}

//Synthetic comment -- @@ -1660,11 +1670,15 @@
mExportProgressDialog.setCanceledOnTouchOutside(false);
mExportProgressDialog.show();
mExportProgressDialog.setProgressNumberFormat("");
}

private void cancelExport() {
ApiService.cancelExportVideoEditor(VideoEditorActivity.this, mProjectPath,
mPendingExportFilename);
mPendingExportFilename = null;
mExportProgressDialog = null;
}







