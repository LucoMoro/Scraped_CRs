/*avoid crashing system serer process

Context#unregisterReceiver will throw IllegalArgumentException
if the receiver has already unregistered.

In GlobalAction class, when SilentModeAction is changed, it will
close the dialog with some delay. So there is a possibility
that it would unregistere again for the same receiver if a user
changes slient mode againand again. System must not crash in such
a case.

Change-Id:I33662feb48a770d39a8413901ec77ac1acd16bc7*/




//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/GlobalActions.java b/policy/src/com/android/internal/policy/impl/GlobalActions.java
old mode 100644
new mode 100755
//Synthetic comment -- index fc187ce..6f8382c

//Synthetic comment -- @@ -320,7 +320,12 @@
/** {@inheritDoc} */
public void onDismiss(DialogInterface dialog) {
if (SHOW_SILENT_TOGGLE) {
            try {
                mContext.unregisterReceiver(mRingerModeReceiver);
            } catch (IllegalArgumentException ie) {
                // ignore this
                Log.w(TAG, ie);
            }
}
}








