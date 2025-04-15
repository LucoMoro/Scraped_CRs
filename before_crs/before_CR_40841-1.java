/*framework: fix notification icons not animating till ticker is updated

Fixes Android issue #15657

Change-Id:If3bb8693d99e3415bcf0fa9adadee5c1bdbb990d*/
//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/statusbar/AnimatedImageView.java b/packages/SystemUI/src/com/android/systemui/statusbar/AnimatedImageView.java
//Synthetic comment -- index d4491d8..78226c5 100644

//Synthetic comment -- @@ -70,6 +70,7 @@
public void onAttachedToWindow() {
super.onAttachedToWindow();
mAttached = true;
}

@Override







