/*Settings: disable Lock SIM card till response is received

Lock SIM card checkbox is enabled always which results in
allowing the user to change the state even before the
previous change has been completed successfully. Due to
this option, UI ends up in state where it can send
disable Lock SIM card twice resulting in operation
not allowed error from modem.

Change-Id:I0f4a344a8d76720e75accf3a763c3d0e940a0dcaAuthor: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 9954*/
//Synthetic comment -- diff --git a/src/com/android/settings/IccLockSettings.java b/src/com/android/settings/IccLockSettings.java
//Synthetic comment -- index 755be83..ab12587 100644

//Synthetic comment -- @@ -348,7 +348,8 @@
// reset dialog state. Else inject error message and show dialog again.
Message callback = Message.obtain(mHandler, MSG_ENABLE_ICC_PIN_COMPLETE);
mPhone.getIccCard().setIccLockEnabled(mToState, mPin, callback);

}

private void iccLockChanged(boolean success) {
//Synthetic comment -- @@ -358,6 +359,7 @@
Toast.makeText(this, mRes.getString(R.string.sim_lock_failed), Toast.LENGTH_SHORT)
.show();
}
resetDialogState();
}








