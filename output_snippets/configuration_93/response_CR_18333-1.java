//<Beginning of snippet n. 0>
public abstract Phone getPhone();
public abstract boolean isMultiparty();
public abstract void hangup() throws CallStateException;

public void listenForEndKey() {
    // Logic to listen for the hardware end key
    // Assuming there is an input method to capture key events
    if (isEndKeyPressed()) {
        hangupWaitingOrBackground();
    }
}

/**
* hasConnection
*/
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

/* package */
void hangupWaitingOrBackground() {
    if (Phone.DEBUG_PHONE) log("hangupWaitingOrBackground");
    try {
        boolean callHungUp = false; // Track if any call was hung up
        for (Call call : getActiveCalls()) {
            if (call.getState() != Call.State.WAITING) {
                call.hangup();
                callHungUp = true;
            }
        }
        if (!callHungUp) {
            log("No calls were hung up, all active calls were in WAITING state.");
        }
    } catch (CallStateException e) {
        log("Error during hangup: " + e.getMessage());
    }
}
//<End of snippet n. 3>