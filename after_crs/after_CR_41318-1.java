/*Movie stdio: fix the wrong title template display when switch phone in add title view

Root cause: when switch the phone, the overlay type is not saved by the app, so after
rotation, the default overlay type will be used to display the title template.

Change-Id:I7944b83e044d4e18022afb5dbe6c6729f4e69f05Author: wgu11 <wangyi.gu@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 37583*/




//Synthetic comment -- diff --git a/src/com/android/videoeditor/OverlayTitleEditor.java b/src/com/android/videoeditor/OverlayTitleEditor.java
//Synthetic comment -- index ad87e86..b4edf18 100644

//Synthetic comment -- @@ -50,6 +50,9 @@
private Bitmap mOverlayBitmap;
private int mPreviewWidth, mPreviewHeight;

    // The key for save instance state
    private static final String OVERLAY_KEY_TYPE = "type";

private final TextWatcher mTextWatcher = new TextWatcher() {
@Override
public void onTextChanged(CharSequence s, int start, int before, int count) {
//Synthetic comment -- @@ -107,12 +110,24 @@
mTitleView.setText(MovieOverlay.getTitle(attributes));
mSubtitleView.setText(MovieOverlay.getSubtitle(attributes));
} else {
            if (savedInstanceState != null) {
                // If overlay type has been saved, get it to update preview image.
                mOverlayType = savedInstanceState.getInt(OVERLAY_KEY_TYPE);
            } else {
                // Default overlay type that puts title at the bottom of the media item.
                mOverlayType = MovieOverlay.OVERLAY_TYPE_BOTTOM_1;
            }
}
updatePreviewImage();
}

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt(OVERLAY_KEY_TYPE, mOverlayType);
    }

private void launchOverlayTitleTemplatePicker() {
final Intent intent = new Intent(this, OverlayTitleTemplatePicker.class);
startActivityForResult(intent, REQUEST_CODE_PICK_TITLE_TEMPLATE);







