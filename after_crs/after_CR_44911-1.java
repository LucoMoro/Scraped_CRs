/*Telephony: Fix for dial request when there is already an active call.

When there is one active call and the user tries to place second
call,the active call should be put on hold first and after receiving
hold response, the dial request should be placed for second call.

Change-Id:I206c94da7564b2d61f5c30078c4a0a27b52f20fc*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmCallTracker.java b/src/java/com/android/internal/telephony/gsm/GsmCallTracker.java
//Synthetic comment -- index fc7fe8a..410935a 100644

//Synthetic comment -- @@ -175,6 +175,9 @@
// note that this triggers call state changed notif
clearDisconnected();

        // flag used to determine if cm.dial needs to be sent now or later
        boolean isDialRequestPending = false;

if (!canDial()) {
throw new CallStateException("cannot dial in current state");
}
//Synthetic comment -- @@ -187,13 +190,14 @@
// but the dial might fail before this happens
// and we need to make sure the foreground call is clear
// for the newly dialed connection
            switchWaitingOrHoldingAndActive(clirMode);

// Fake local state so that
// a) foregroundCall is empty for the newly dialed connection
// b) hasNonHangupStateChanged remains false in the
// next poll, so that we don't clear a failed dialing call
fakeHoldForegroundBeforeDial();
            isDialRequestPending = true;
}

if (foregroundCall.getState() != GsmCall.State.IDLE) {
//Synthetic comment -- @@ -215,10 +219,15 @@
// and will mark it as dropped.
pollCallsWhenSafe();
} else {
            // if isDialRequestPending is true, we would postpone the dial
            // request for the second call till we get the hold confirmation
            // for the first call.
            if (false == isDialRequestPending) {
                // Always unmute when initiating a new call
                setMute(false);

                cm.dial(pendingMO.address, clirMode, uusInfo, obtainCompleteMessage());
            }
}

updatePhoneState();
//Synthetic comment -- @@ -242,6 +251,27 @@
return dial(dialString, clirMode, null);
}


    void
    dialPendingCall (int clirMode) {
        if (pendingMO.address == null || pendingMO.address.length() == 0
            || pendingMO.address.indexOf(PhoneNumberUtils.WILD) >= 0) {
            // Phone number is invalid
            pendingMO.cause = Connection.DisconnectCause.INVALID_NUMBER;

            // handlePollCalls() will notice this call not present
            // and will mark it as dropped.
            pollCallsWhenSafe();
        } else {
            // Always unmute when initiating a new call
            setMute(false);

            cm.dial(pendingMO.address, clirMode, obtainCompleteMessage());
        }
        updatePhoneState();
        phone.notifyPreciseCallStateChanged();
    }

void
acceptCall () throws CallStateException {
// FIXME if SWITCH fails, should retry with ANSWER
//Synthetic comment -- @@ -284,6 +314,18 @@
}

void
    switchWaitingOrHoldingAndActive(int clirMode) throws CallStateException {
        if (ringingCall.getState() == GsmCall.State.INCOMING) {
            throw new CallStateException("cannot be in the incoming state");
        } else {
            cm.switchWaitingOrHoldingAndActive(
                    obtainCompleteMessage(EVENT_SWITCH_RESULT, clirMode));
            callSwitchPending = true;
        }
    }


    void
conference() throws CallStateException {
cm.conference(obtainCompleteMessage(EVENT_CONFERENCE_RESULT));
}
//Synthetic comment -- @@ -366,6 +408,20 @@
return obtainMessage(what);
}


    private Message
    obtainCompleteMessage(int what, int clirMode) {
        pendingOperations++;
        lastRelevantPoll = null;
        needsPoll = true;

        if (DBG_POLL) log("obtainCompleteMessage: pendingOperations=" +
                pendingOperations + ", needsPoll=" + needsPoll);

        return obtainMessage(what, clirMode);
    }


private void
operationComplete() {
pendingOperations--;
//Synthetic comment -- @@ -853,7 +909,24 @@
operationComplete();
break;

            // This event will also be called when the call is placed
            // on hold while there is another dialed call. If Hold succeeds,
            // dialPendingCall would be invoked.Else getCurrentCalls is anyways
            // invoked through operationComplete,which will get the new
            // call states depending on which UI would be updated.
case EVENT_SWITCH_RESULT:
                callSwitchPending = false;
                ar = (AsyncResult)msg.obj;
                if (ar.exception != null) {
                    phone.notifySuppServiceFailed(getFailedService(msg.what));
                } else {
                    if (ar.userObj != null) {
                        dialPendingCall((Integer) ar.userObj);
                    }
                }
                operationComplete();
            break;

case EVENT_CONFERENCE_RESULT:
case EVENT_SEPARATE_RESULT:
case EVENT_ECT_RESULT:







