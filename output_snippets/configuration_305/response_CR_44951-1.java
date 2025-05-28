//<Beginning of snippet n. 0>


import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.DataCallState;
import com.android.internal.telephony.DataConnection.FailCause;
import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.DataConnectionAc;
import com.android.internal.telephony.DataConnectionTracker;

private DataCallState previousState;

protected void onDataStateChanged(AsyncResult ar) {
    ArrayList<DataCallState> dataCallStates = (ArrayList<DataCallState>)(ar.result);

    if (ar.exception != null) {
        logError("AsyncResult exception occurred: " + ar.exception);
        return;
    }

    boolean isActiveOrDormantConnectionPresent = false;
    boolean hasChanges = false;

    for (DataCallState dataCallState : dataCallStates) {
        boolean connectionState = dataCallState.active;
        if (connectionState != DATA_CONNECTION_ACTIVE_PH_LINK_INACTIVE) {
            isActiveOrDormantConnectionPresent = true;
            if (hasLinkPropertyChanged(dataCallState)) {
                notifyLinkPropertyChange(dataCallState);
                hasChanges = true;
            }
        }
    }

    if (hasChanges) {
        notifyListeners();
    }
}

private boolean hasLinkPropertyChanged(DataCallState newState) {
    if (previousState == null) {
        previousState = newState;
        return true;
    }
    boolean hasChanged = !previousState.equals(newState);
    if (hasChanged) {
        previousState = newState;
    }
    return hasChanged;
}

private void notifyLinkPropertyChange(DataCallState state) {
    // Assuming a method to notify listeners exists
    for (LinkPropertyChangeListener listener : listeners) {
        listener.onLinkPropertyChanged(state);
    }
}

private void notifyListeners() {
    // Assuming a method to notify listeners exists
    for (Listener listener : listeners) {
        listener.onDataStateChange();
    }
}

private void logError(String message) {
    // Expanded logging with context
    System.err.println("Error: " + message);
    // Add more contextual information about the state if needed
}

//<End of snippet n. 0>