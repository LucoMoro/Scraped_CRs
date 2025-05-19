//<Beginning of snippet n. 0>
public abstract Phone getPhone();
public abstract boolean isMultiparty();
public abstract void hangup() throws CallStateException;

public void onEndKeyPressed() {
    if (isActiveCall()) {
        hangup();
    } else if (isWaitingCall() || isBackgroundCall()) {
        hangupWaitingOrBackground();
    }
}

private boolean isActiveCall() {
    for (Call call : getPhone().getAllCalls()) {
        if (call.getState() == Call.State.ACTIVE) {
            return true;
        }
    }
    return false;
}

private boolean isWaitingCall() {
    return getPhone().getBackgroundCall() != null && getPhone().getBackgroundCall().getState() == Call.State.WAITING;
}

private boolean isBackgroundCall() {
    return getPhone().getBackgroundCall() != null && getPhone().getBackgroundCall().getState() == Call.State.HOLDING;
}

public void hangup() throws CallStateException {
    boolean hasHungup = false;
    for (Call call : getPhone().getAllCalls()) {
        if (call.getState() == Call.State.ACTIVE) {
            call.hangup();
            hasHungup = true;
        }
    }
    if (!hasHungup) {
        throw new CallStateException("No active call to hang up.");
    }
}

void hangupWaitingOrBackground() throws CallStateException {
    if (Phone.DEBUG_PHONE) log("hangupWaitingOrBackground");
    if (!isWaitingCall() && !isBackgroundCall()) {
        throw new CallStateException("No waiting or background calls to hang up.");
    }
    if (isWaitingCall()) {
        getPhone().getBackgroundCall().hangup();
    }
    if (isBackgroundCall()) {
        getPhone().getBackgroundCall().hangup();
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
owner.hangup(this);
}

public String toString() {
    return state.toString();
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
owner.hangup(this);
}

public String toString() {
    return state.toString();
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
phone.notifyPreciseCallStateChanged();
//<End of snippet n. 3>