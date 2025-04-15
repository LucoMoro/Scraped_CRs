/*Change hide() to dismiss() to avoid memory leak.

Change-Id:I4a5acb123c673c75a48c3e77566cdca4760d8576*/
//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/SimUnlockScreen.java b/phone/com/android/internal/policy/impl/SimUnlockScreen.java
//Synthetic comment -- index 5518e11..486e7aa 100644

//Synthetic comment -- @@ -130,9 +130,10 @@

/** {@inheritDoc} */
public void cleanUp() {
        // hide the dialog.
if (mSimUnlockProgressDialog != null) {
            mSimUnlockProgressDialog.hide();
}
mUpdateMonitor.removeCallback(this);
}







