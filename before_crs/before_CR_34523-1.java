/*Updates Emergency call button in PUK lock screen

Updates to support Emergency call button in PUK lock screen

Change-Id:I6b47c1d461d081006fa41b83bf4e4b74a632b785Signed-off-by: Christian Bejram <christian.bejram@stericsson.com>*/
//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/KeyguardStatusViewManager.java b/policy/src/com/android/internal/policy/impl/KeyguardStatusViewManager.java
//Synthetic comment -- index ee54de1..c15edbe 100644

//Synthetic comment -- @@ -514,10 +514,7 @@
case SimPukLocked:
carrierText = makeCarierString(mPlmn,
getContext().getText(R.string.lockscreen_sim_puk_locked_message));
                if (!mLockPatternUtils.isPukUnlockScreenEnable()) {
                    // This means we're showing the PUK unlock screen
                    mEmergencyButtonEnabledBecauseSimLocked = true;
                }
break;
}








