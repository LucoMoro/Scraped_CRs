/*Make disabled TextViews uneditable

Fixes Issue 2771
From now on:
 disabling a TextView closes the associated on-screen keyboard
 selecting a disabled TextView does not open the on-screen keyboard
 can't edit contents if the disabled TextView focus is gained by the directional keys

Change-Id:I44e3c0aff2a0ce1e6426818bfe16c1d19c7c18ac*/
//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index e6ed70a..b51ae09 100644

//Synthetic comment -- @@ -944,6 +944,22 @@
setTypeface(tf, styleIndex);
}

/**
* Sets the typeface and style in which the text should be displayed,
* and turns on the fake bold and italic bits in the Paint if the
//Synthetic comment -- @@ -4436,7 +4452,7 @@
}

@Override public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        if (onCheckIsTextEditor()) {
if (mInputMethodState == null) {
mInputMethodState = new InputMethodState();
}
//Synthetic comment -- @@ -6575,7 +6591,8 @@
return superResult;
}

        if ((mMovement != null || onCheckIsTextEditor()) && mText instanceof Spannable && mLayout != null) {

boolean handled = false;








