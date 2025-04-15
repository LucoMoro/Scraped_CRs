/*Prevent window leak from VideoView.

Make sure to always dismiss the error message dialog in
case the containing activity is finished. This can happen
for instance when resuming the task if clearTaskOnLaunch
is set further up the activity stack.

Change-Id:I1ce110ec8dd2244c3807e5db418ca9d135978594*/
//Synthetic comment -- diff --git a/core/java/android/widget/VideoView.java b/core/java/android/widget/VideoView.java
//Synthetic comment -- index b169c93..adc04fa 100644

//Synthetic comment -- @@ -90,6 +90,7 @@
private boolean     mCanSeekBack;
private boolean     mCanSeekForward;
private int         mStateWhenSuspended;  //state before calling suspend()

public VideoView(Context context) {
super(context);
//Synthetic comment -- @@ -383,7 +384,7 @@
messageId = com.android.internal.R.string.VideoView_error_text_unknown;
}

                new AlertDialog.Builder(mContext)
.setTitle(com.android.internal.R.string.VideoView_error_title)
.setMessage(messageId)
.setPositiveButton(com.android.internal.R.string.VideoView_error_button,
//Synthetic comment -- @@ -395,6 +396,7 @@
if (mOnCompletionListener != null) {
mOnCompletionListener.onCompletion(mMediaPlayer);
}
}
})
.setCancelable(false)
//Synthetic comment -- @@ -557,6 +559,16 @@
return super.onKeyDown(keyCode, event);
}

private void toggleMediaControlsVisiblity() {
if (mMediaController.isShowing()) {
mMediaController.hide();







