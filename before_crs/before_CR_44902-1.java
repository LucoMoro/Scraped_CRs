/*Fix for dial request when there is already an active call.

When there is one active call and the user tries to place second
call,the active call should be put on hold first and after receiving
hold response, the dial request should be placed for second call.

Change-Id:I8bfffb7a0d10d759fe04c3bd4a8711f303e7b9ed*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmCallTracker.java b/src/java/com/android/internal/telephony/gsm/GsmCallTracker.java
//Synthetic comment -- index fc7fe8a..45b8a9a 100644

//Synthetic comment -- @@ -175,6 +175,9 @@
// note that this triggers call state changed notif
clearDisconnected();

if (!canDial()) {
throw new CallStateException("cannot dial in current state");
}
//Synthetic comment -- @@ -187,13 +190,14 @@
// but the dial might fail before this happens
// and we need to make sure the foreground call is clear
// for the newly dialed connection
            switchWaitingOrHoldingAndActive();

// Fake local state so that
// a) foregroundCall is empty for the newly dialed connection
// b) hasNonHangupStateChanged remains false in the
// next poll, so that we don't clear a failed dialing call
fakeHoldForegroundBeforeDial();
}

if (foregroundCall.getState() != GsmCall.State.IDLE) {
//Synthetic comment -- @@ -215,10 +219,15 @@
// and will mark it as dropped.
pollCallsWhenSafe();
} else {
            // Always unmute when initiating a new call
            setMute(false);

            cm.dial(pendingMO.address, clirMode, uusInfo, obtainCompleteMessage());
}

updatePhoneState();
//Synthetic comment -- @@ -242,6 +251,27 @@
return dial(dialString, clirMode, null);
}

void
acceptCall () throws CallStateException {
// FIXME if SWITCH fails, should retry with ANSWER
//Synthetic comment -- @@ -284,6 +314,17 @@
}

void
conference() throws CallStateException {
cm.conference(obtainCompleteMessage(EVENT_CONFERENCE_RESULT));
}
//Synthetic comment -- @@ -366,6 +407,20 @@
return obtainMessage(what);
}

private void
operationComplete() {
pendingOperations--;
//Synthetic comment -- @@ -853,7 +908,23 @@
operationComplete();
break;

case EVENT_SWITCH_RESULT:
case EVENT_CONFERENCE_RESULT:
case EVENT_SEPARATE_RESULT:
case EVENT_ECT_RESULT:







