//<Beginning of snippet n. 0>
public abstract Phone getPhone();
public abstract boolean isMultiparty();
public abstract void hangup() throws CallStateException;

/**
* hasConnection

//<End of snippet n. 0>


//<Beginning of snippet n. 1>
public void onKeyPress(KeyEvent event) {
    if (event.getKeyCode() == KeyEvent.VK_END) {
        Phone phone = getPhone();
        if (phone != null) {
            try {
                if (isMultiparty()) {
                    // Logic to check active calls in multiparty scenario
                }
                // Add logic to terminate only active calls
                hangup();
            } catch (CallStateException e) {
                // Handle error gracefully
                System.err.println("Error while hanging up: " + e.getMessage());
            }
        } else {
            System.err.println("Invalid phone instance.");
        }
    }
}

public String toString() {
    return state.toString();
//<End of snippet n. 1>


//<Beginning of snippet n. 2>
public void onKeyPress(KeyEvent event) {
    if (event.getKeyCode() == KeyEvent.VK_END) {
        Phone phone = getPhone();
        if (phone != null) {
            try {
                if (isMultiparty()) {
                    // Logic to check active calls in multiparty scenario
                }
                // Add logic to terminate only active calls
                hangup();
            } catch (CallStateException e) {
                // Handle error gracefully
                System.err.println("Error while hanging up: " + e.getMessage());
            }
        } else {
            System.err.println("Invalid phone instance.");
        }
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
//<End of snippet n. 3>