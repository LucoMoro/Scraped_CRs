//<Beginning of snippet n. 0>
public abstract Phone getPhone();
public abstract boolean isMultiparty();
public abstract void hangup() throws CallStateException;

public void hangupCall() {
    if (isActiveCall()) {
        try {
            hangup();
        } catch (CallStateException e) {
            // Handle exception
        }
    }
}

private boolean isActiveCall() {
    // Insert logic to check if there are active calls
    return true; // Replace with actual condition
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public void onEndKeyPress() {
    if (isMultiparty()) {
        hangupCall();
    }
}

public String toString() {
    return state.toString();
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
public void onEndKeyPress() {
    if (isMultiparty()) {
        hangupCall();
    }
}

public String toString() {
    return state.toString();
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
phone.notifyPreciseCallStateChanged();
}

void hangupWaitingOrBackground() {
    if (Phone.DEBUG_PHONE) log("hangupWaitingOrBackground");
    // Logic to hangup waiting calls without affecting active ones
    // Add necessary validation for call states here
}

//<End of snippet n. 3>