/*Fix for dial request when there is already an active call.

When there is one active call and the user tries to place second
call,the active call should be put on hold first and after receiving
hold response, the dial request should be placed for second call.

Change-Id:I68d3e25494f67ef561e12a96982a91b11eb3caf3*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmCallTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmCallTracker.java
//Synthetic comment -- index 06f310c..8cccc27 100644

//Synthetic comment -- @@ -172,6 +172,9 @@
// note that this triggers call state changed notif
clearDisconnected();

        // flag used to determine if dial request need to be sent now or later
        boolean isDialRequestPending = false;

if (!canDial()) {
throw new CallStateException("cannot dial in current state");
}
//Synthetic comment -- @@ -184,13 +187,14 @@
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
//Synthetic comment -- @@ -211,10 +215,14 @@
// and will mark it as dropped.
pollCallsWhenSafe();
} else {
            // if isDialRequestPending is true, we would postpone the dial
            // request for the second call till we get the hold confirmation
            // for the first call.
            if (false == isDialRequestPending) {
                // Always unmute when initiating a new call
                setMute(false);
                cm.dial(pendingMO.address, clirMode, obtainCompleteMessage());
            }
}

updatePhoneState();
//Synthetic comment -- @@ -228,6 +236,26 @@
return dial(dialString, CommandsInterface.CLIR_DEFAULT, null);
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

Connection
dial(String dialString, UUSInfo uusInfo) throws CallStateException {
return dial(dialString, CommandsInterface.CLIR_DEFAULT, uusInfo);
//Synthetic comment -- @@ -280,6 +308,17 @@
}

void
    switchWaitingOrHoldingAndActive(int clirMode) throws CallStateException {
        // Should we bother with this check?
        if (ringingCall.getState() == GsmCall.State.INCOMING) {
            throw new CallStateException("cannot be in the incoming state");
        } else {
            cm.switchWaitingOrHoldingAndActive(
                    obtainCompleteMessage(EVENT_SWITCH_RESULT, clirMode));
        }
    }

    void
conference() throws CallStateException {
cm.conference(obtainCompleteMessage(EVENT_CONFERENCE_RESULT));
}
//Synthetic comment -- @@ -362,6 +401,18 @@
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
//Synthetic comment -- @@ -850,6 +901,17 @@
break;

case EVENT_SWITCH_RESULT:
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







