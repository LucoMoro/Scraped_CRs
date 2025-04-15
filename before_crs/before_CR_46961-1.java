/*Make VideoView and MediaController listen to more key events

Make VideoView and MediaController listen to more key
events, i.e: "FastForward", "Rewind", "Next" and "Previous".

Change-Id:I522061a3fae2ab349d0ca8dbb82f9ba22e490cf7*/
//Synthetic comment -- diff --git a/core/java/android/widget/MediaController.java b/core/java/android/widget/MediaController.java
//Synthetic comment -- index f76ab2b..4182c9f 100644

//Synthetic comment -- @@ -97,6 +97,9 @@
private ImageButton         mNextButton;
private ImageButton         mPrevButton;

public MediaController(Context context, AttributeSet attrs) {
super(context, attrs);
mRoot = this;
//Synthetic comment -- @@ -486,6 +489,30 @@
hide();
}
return true;
}

show(sDefaultTimeout);








//Synthetic comment -- diff --git a/core/java/android/widget/VideoView.java b/core/java/android/widget/VideoView.java
//Synthetic comment -- index 7c8196d..fe9a2bf 100644

//Synthetic comment -- @@ -91,6 +91,9 @@
private boolean     mCanSeekBack;
private boolean     mCanSeekForward;

public VideoView(Context context) {
super(context);
initVideoView();
//Synthetic comment -- @@ -566,6 +569,22 @@
mMediaController.show();
}
return true;
} else {
toggleMediaControlsVisiblity();
}







