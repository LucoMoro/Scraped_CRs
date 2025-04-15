/*Switch widget: Fix ON/OFF option update error

When calling setChecked() from a listener which is invoked by
the Switch toggle, setChecked() will be called recursively,
causing the value of mThumbPosition overridden by the outer
setChecked(). This make the nested setChecked() unable to update
the "Thumb" position correctly.

Set mThumbPosition to sync up with the exact checked state of
the Switch to avoid update error.

Issue 34748

Change-Id:Id4aa389114dcc590430b11626c80d29c9c6016a0Signed-off-by: Yu Haonong <yuhaonong@gmail.com>*/




//Synthetic comment -- diff --git a/core/java/android/widget/Switch.java b/core/java/android/widget/Switch.java
//Synthetic comment -- index 471f259..9ea1372 100644

//Synthetic comment -- @@ -675,7 +675,7 @@
@Override
public void setChecked(boolean checked) {
super.setChecked(checked);
        mThumbPosition = isChecked() ? getThumbScrollRange() : 0;
invalidate();
}








