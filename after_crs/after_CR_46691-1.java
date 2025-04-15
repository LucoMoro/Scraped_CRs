/*Fix NPE when enable DEBUG which will cause system boot failed.
Signed-off-by: Jianzheng Zhou <jianzheng.zhou@freescale.com>

Change-Id:Iecd6c29fee790dbce9922609990b2f3b9f31abd7*/




//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/keyguard/KeyguardViewMediator.java b/policy/src/com/android/internal/policy/impl/keyguard/KeyguardViewMediator.java
//Synthetic comment -- index c227619..1f2fae1 100644

//Synthetic comment -- @@ -1321,11 +1321,14 @@
if (!isAssistantAvailable()) {
flags |= StatusBarManager.DISABLE_SEARCH;
}
                if (DEBUG) {
                    Log.d(TAG, "adjustStatusBarLocked:isSecure=" + isSecure());
                }
}

if (DEBUG) {
Log.d(TAG, "adjustStatusBarLocked: mShowing=" + mShowing + " mHidden=" + mHidden
                        + " --> flags=0x" + Integer.toHexString(flags));
}

if (!(mContext instanceof Activity)) {







