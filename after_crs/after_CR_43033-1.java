/*Animated icons in statusbar doesn't animate.

When sending notifications to the statusbar
that contains animated icons they will not
animate. That happent because to start the
animation we checked if the icon was present
in the hierarchy view before it was actually
added to the parent view.

Change-Id:I6b3c57a2c59d34f4a942c39c255adebffc59048c*/




//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/statusbar/AnimatedImageView.java b/packages/SystemUI/src/com/android/systemui/statusbar/AnimatedImageView.java
//Synthetic comment -- index d4491d8..78226c5 100644

//Synthetic comment -- @@ -70,6 +70,7 @@
public void onAttachedToWindow() {
super.onAttachedToWindow();
mAttached = true;
        updateAnim();
}

@Override







