/*Telephony: Check radio state before notify card absent

Check if radio is off before broadcasting that card is absent. If
radio is off then don't broadcast that card is absent, since the
card might have just been powered down and not removed.

Change-Id:I84ac988af60aeaacdd60f3043f833106bc0dcf8d*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCardProxy.java b/src/java/com/android/internal/telephony/IccCardProxy.java
//Synthetic comment -- index eef0c6f..2acda2b 100644

//Synthetic comment -- @@ -201,8 +201,10 @@
}
break;
case EVENT_ICC_ABSENT:
                mAbsentRegistrants.notifyRegistrants();
                setExternalState(State.ABSENT);
break;
case EVENT_ICC_LOCKED:
processLockedState();







