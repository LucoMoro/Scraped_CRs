/*Fix UnlockScreen can't show isse.

In the case UnlockScreen can't show because the running sequence:
1. keyguardDone(true)
2. onSimStateChanged: PIN_REQUIRED
3. resetStateLocked
4. handleHide
5. handleReset
So add an examination in reset() to doKeyguard if it's not showing.

Change-Id:I6df25107ce008a44e6619edecbae45f38adb1df7*/




//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/KeyguardViewMediator.java b/phone/com/android/internal/policy/impl/KeyguardViewMediator.java
//Synthetic comment -- index c255041..d9975eb 100644

//Synthetic comment -- @@ -1093,7 +1093,11 @@
private void handleReset() {
synchronized (KeyguardViewMediator.this) {
if (DEBUG) Log.d(TAG, "handleReset");
            if (!isShowing()) {
                doKeyguard();
            } else {
                mKeyguardViewManager.reset();
            }
}
}








