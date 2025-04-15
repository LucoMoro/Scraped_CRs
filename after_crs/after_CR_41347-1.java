/*Save the export progress when rotating the phone

When rotating the phone which is exporting movies,
the export progress will be lost. The progress bar
will appear to be 0 for a few seconds. In this patch,
The export progress is saved when the video editor
activity is destroyed.

Change-Id:I1a317197ab4c9ea8292c42eca66d04044894ec89Author: Wangyi Gu <wangyi.gu@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 37621*/




//Synthetic comment -- diff --git a/src/com/android/videoeditor/VideoEditorActivity.java b/src/com/android/videoeditor/VideoEditorActivity.java
//Synthetic comment -- index 193bae3..f9ca5e6 100755

//Synthetic comment -- @@ -136,6 +136,10 @@
// Threshold in width dip for showing title in action bar.
private static final int SHOW_TITLE_THRESHOLD_WIDTH_DIP = 1000;

    // To store the export progress when the activity is destroyed
    private static final String EXPORT_PROGRESS  = "export_progress";
    private int mExportProgress;

private final TimelineRelativeLayout.LayoutCallback mLayoutCallback =
new TimelineRelativeLayout.LayoutCallback() {

//Synthetic comment -- @@ -391,9 +395,11 @@
mRestartPreview = savedInstanceState.getBoolean(STATE_PLAYING);
mCaptureMediaUri = savedInstanceState.getParcelable(STATE_CAPTURE_URI);
mMediaLayoutSelectedPos = savedInstanceState.getInt(STATE_SELECTED_POS_ID, -1);
            mExportProgress = savedInstanceState.getInt(EXPORT_PROGRESS);
} else {
mRestartPreview = false;
mMediaLayoutSelectedPos = -1;
            mExportProgress = 0;
}

// Compute the activity width
//Synthetic comment -- @@ -491,6 +497,7 @@
outState.putBoolean(STATE_PLAYING, isPreviewPlaying() || mRestartPreview);
outState.putParcelable(STATE_CAPTURE_URI, mCaptureMediaUri);
outState.putInt(STATE_SELECTED_POS_ID, mMediaLayout.getSelectedViewPos());
        outState.putInt(EXPORT_PROGRESS,mExportProgress);
}

@Override
//Synthetic comment -- @@ -636,6 +643,7 @@

case R.id.menu_item_export_movie: {
// Present the user with a dialog to choose export options
                mExportProgress = 0;
showDialog(DIALOG_EXPORT_OPTIONS_ID);
return true;
}
//Synthetic comment -- @@ -1443,6 +1451,7 @@
@Override
protected void onExportProgress(int progress) {
if (mExportProgressDialog != null) {
            mExportProgress = progress;
mExportProgressDialog.setProgress(progress);
}
}
//Synthetic comment -- @@ -1452,6 +1461,7 @@
if (mExportProgressDialog != null) {
mExportProgressDialog.dismiss();
mExportProgressDialog = null;
            mExportProgress = 0;
}
}

//Synthetic comment -- @@ -1659,11 +1669,15 @@
mExportProgressDialog.setCanceledOnTouchOutside(false);
mExportProgressDialog.show();
mExportProgressDialog.setProgressNumberFormat("");
        if (mExportProgress >= 0 && mExportProgress <= 100) {
            mExportProgressDialog.setProgress(mExportProgress);
        }
}

private void cancelExport() {
ApiService.cancelExportVideoEditor(VideoEditorActivity.this, mProjectPath,
mPendingExportFilename);
        mExportProgress = 0;
mPendingExportFilename = null;
mExportProgressDialog = null;
}







