
//<Beginning of snippet n. 0>


public abstract Phone getPhone();
public abstract boolean isMultiparty();
public abstract void hangup() throws CallStateException;
    public abstract void hangupAllCalls() throws CallStateException;

/**
* hasConnection

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


owner.hangup(this);
}

    public void
    hangupAllCalls() throws CallStateException {
        throw new CallStateException ("hangupAllCalls: Unsupported Action");
    }

public String
toString() {
return state.toString();

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


owner.hangup(this);
}

    public void
    hangupAllCalls() throws CallStateException {
        owner.hangupAllCalls();
    }

public String
toString() {
return state.toString();

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


phone.notifyPreciseCallStateChanged();
}

    void hangupAllCalls () throws CallStateException {
        boolean hungUp = false;
        if (!ringingCall.isIdle()) {
             // Do not hangup waiting call
             // as per 3GPP TS 22.030, 6.5.5.1.
             if (ringingCall.getState() != GsmCall.State.WAITING) {
                 log("hangupAllCalls: hang up ringing call");
                 cm.hangupWaitingOrBackground(obtainCompleteMessage());
                 ringingCall.onHangupLocal();
                 hungUp = true;
             }
        }
        if (!foregroundCall.isIdle()) {
            log("hangupAllCalls: hang up active call");
            hangupAllConnections(foregroundCall);
            foregroundCall.onHangupLocal();
            hungUp = true;
        }
        if (!backgroundCall.isIdle()) {
            log("hangupAllCalls: hang up held call");
            hangupAllConnections(backgroundCall);
            backgroundCall.onHangupLocal();
            hungUp = true;
        }
        if (hungUp) {
            phone.notifyPreciseCallStateChanged();
        } else {
            throw new CallStateException("no active connections to hangup");
        }
    }

/* package */
void hangupWaitingOrBackground() {
if (Phone.DEBUG_PHONE) log("hangupWaitingOrBackground");

//<End of snippet n. 3>








