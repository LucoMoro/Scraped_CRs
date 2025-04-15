/*Fix NPE when enable DEBUG which will cause system boot failed.

Change-Id:I057e5d837517c7f6a1f9e1c796d99b99d063e6bdSigned-off-by: Jianzheng Zhou <jianzheng.zhou@freescale.com>*/
//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/keyguard/KeyguardViewMediator.java b/policy/src/com/android/internal/policy/impl/keyguard/KeyguardViewMediator.java
//Synthetic comment -- index c227619..3b053ba 100644

//Synthetic comment -- @@ -1321,11 +1321,14 @@
if (!isAssistantAvailable()) {
flags |= StatusBarManager.DISABLE_SEARCH;
}
}

if (DEBUG) {
Log.d(TAG, "adjustStatusBarLocked: mShowing=" + mShowing + " mHidden=" + mHidden
                        + " isSecure=" + isSecure() + " --> flags=0x" + Integer.toHexString(flags));
}

if (!(mContext instanceof Activity)) {







