/*Fix UnlockScreen can't show issue.

In the case UnlockScreen can't show because the running sequence:
1. keyguardDone(true)
2. onSimStateChanged: PIN_REQUIRED
3. resetStateLocked
4. handleHide
5. handleReset
So add an examination in reset() to doKeyguard if it's not showing.

Change-Id:I84cc4329d1b47f15b40697580035de29fd3bd1cf*/
//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/KeyguardViewMediator.java b/phone/com/android/internal/policy/impl/KeyguardViewMediator.java
//Synthetic comment -- index c255041..d9975eb 100644

//Synthetic comment -- @@ -1093,7 +1093,11 @@
private void handleReset() {
synchronized (KeyguardViewMediator.this) {
if (DEBUG) Log.d(TAG, "handleReset");
            mKeyguardViewManager.reset();
}
}








