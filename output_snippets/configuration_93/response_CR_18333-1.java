//<Beginning of snippet n. 0>

public abstract Phone getPhone();
public abstract boolean isMultiparty();
public abstract void hangup() throws CallStateException;


/**
* hasConnection
*/
public void handleEndKeyPress() {
    if (hasActiveCalls() && !isWaiting()) {
        hangup();
    }
}

private boolean hasActiveCalls() {
    // Implement logic to check if there are active non-waiting calls.
    return true; // Placeholder
}

private boolean isWaiting() {
    // Implement logic to determine if the call is waiting.
    return false; // Placeholder
}

@Override
public void hangup() throws CallStateException {
    try {
        if (!hasActiveCalls()) {
            throw new CallStateException("No active calls to hang up.");
        }
        // Add state validation and hang up logic for non-waiting calls.
        // For example, iterate through active calls and hang up as necessary.
        log("Hanging up active calls.");
    } catch (Exception e) {
        log("Hangup failed: " + e.getMessage());
        throw e;
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
}

/* package */
void hangupWaitingOrBackground() {
    if (Phone.DEBUG_PHONE) log("hangupWaitingOrBackground");
    // Implement hangup logic for waiting or background calls if necessary.
}

//<End of snippet n. 3>