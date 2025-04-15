/*Mms: Need to type twice on the back key

Display MMS edit, touch "Edit" item twice. We Need to touch back
key twice if we want to return to the MMS edit.

Disable the button when click the edit button.
And reset to enable the button when the activity onresume.

Change-Id:Icba0621d020cdd3dba14b86d2e51261022984874Author: Lei Wu <b497@borqs.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 20739*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/AttachmentEditor.java b/src/com/android/mms/ui/AttachmentEditor.java
//Synthetic comment -- index 8854c4c..7d5670b 100644

//Synthetic comment -- @@ -58,7 +58,9 @@
private SlideshowModel mSlideshow;
private Presenter mPresenter;
private boolean mCanSend;
private Button mSendButton;

public AttachmentEditor(Context context, AttributeSet attr) {
super(context, attr);
//Synthetic comment -- @@ -111,6 +113,19 @@
}
}

public void hideView() {
if (mView != null) {
((View)mView).setVisibility(View.GONE);
//Synthetic comment -- @@ -202,13 +217,15 @@
R.id.slideshow_attachment_view);
view.setVisibility(View.VISIBLE);

        Button editBtn = (Button) view.findViewById(R.id.edit_slideshow_button);
mSendButton = (Button) view.findViewById(R.id.send_slideshow_button);
updateSendButton();
final ImageButton playBtn = (ImageButton) view.findViewById(
R.id.play_slideshow_button);

        editBtn.setOnClickListener(new MessageOnClick(MSG_EDIT_SLIDESHOW));
mSendButton.setOnClickListener(new MessageOnClick(MSG_SEND_SLIDESHOW));
playBtn.setOnClickListener(new MessageOnClick(MSG_PLAY_SLIDESHOW));









//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 72df986..6d05e32 100644

//Synthetic comment -- @@ -344,6 +344,7 @@
if (mTempMmsUri == null) {
return;
}
Intent intent = new Intent(ComposeMessageActivity.this,
SlideshowEditActivity.class);
intent.setData(mTempMmsUri);
//Synthetic comment -- @@ -3178,6 +3179,7 @@
if (mWorkingMessage.hasSlideshow()) {
mBottomPanel.setVisibility(View.GONE);
mAttachmentEditor.requestFocus();
return;
}








