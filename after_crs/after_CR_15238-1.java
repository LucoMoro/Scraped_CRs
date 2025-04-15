/*Handle INTENT_VALUE_ICC_UNUSED value to update SIM status.

If NV is used in CDMA, ICC card is not used. In global mode phones,
when user enables and disables airplane mode, SIM card could be shutdown which
might cause "NO SIM/NO RUIM" message to pop up on the screen even though the
phone is in CDMA using NV. Hence SIM_STATE_CHANGED message should be ignored
when phone is using NV.

During ICC card status change, if the radio state is NV_READY then
ACTION_SIM_STATE_CHANGED intent will be broadcasted with INTENT_VALUE_ICC_UNUSED
value. If the intent value is INTENT_VALUE_ICC_UNUSED, KeyguardUpdateMonitor
will update the SIM state as "READY" and the lock screen will be updated with
normal status instead of sim missing.

Change-Id:I4e133b1dbcee6d0de744f87c9e3ee244220990e1*/




//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/KeyguardUpdateMonitor.java b/phone/com/android/internal/policy/impl/KeyguardUpdateMonitor.java
//Synthetic comment -- index 3483405..02a17e5 100644

//Synthetic comment -- @@ -118,6 +118,16 @@
this.simState = IccCard.State.ABSENT;
} else if (IccCard.INTENT_VALUE_ICC_READY.equals(stateExtra)) {
this.simState = IccCard.State.READY;
            } else if (IccCard.INTENT_VALUE_ICC_UNUSED.equals(stateExtra)) {
                /*
                 * During ICC card status change, if the radio state is NV_READY
                 * then ACTION_SIM_STATE_CHANGED intent will be broadcasted with
                 * INTENT_VALUE_ICC_UNUSED value. If the intent value is
                 * INTENT_VALUE_ICC_UNUSED, KeyguardUpdateMonitor will update
                 * the SIM state as "READY" and the lock screen will be updated
                 * with normal status instead of sim missing.
                 */
                this.simState = IccCard.State.READY;
} else if (IccCard.INTENT_VALUE_ICC_LOCKED.equals(stateExtra)) {
final String lockedReason = intent
.getStringExtra(IccCard.INTENT_KEY_LOCKED_REASON);







