//<Beginning of snippet n. 0>
public abstract Phone getPhone();
public abstract boolean isMultiparty();
public abstract void hangup() throws CallStateException;


/**
* hasConnection
*/
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public void onEndKeyPress() {
    try {
        Phone phone = getPhone();
        if (phone != null) {
            for (Call call : phone.getCalls()) {
                if (call.isActive()) {
                    call.hangup();
                }
            }
        }
    } catch (CallStateException e) {
        // Handle the CallStateException gracefully
        if (Phone.DEBUG_PHONE) log("Failed to hang up call: " + e.getMessage());
    }
}

public String toString() {
    return state.toString();
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
public void onEndKeyPress() {
    try {
        Phone phone = getPhone();
        if (phone != null) {
            for (Call call : phone.getCalls()) {
                if (call.isActive()) {
                    call.hangup();
                }
            }
        }
    } catch (CallStateException e) {
        // Handle the CallStateException gracefully
        if (Phone.DEBUG_PHONE) log("Failed to hang up call: " + e.getMessage());
    }
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
    // Logic to hang up waiting or background calls
}
//<End of snippet n. 3>