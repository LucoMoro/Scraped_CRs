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
    boolean hasChanged = previousState.active != newState.active ||
                         !previousState.linkProperties.equals(newState.linkProperties) ||
                         !previousState.otherRelevantProperty.equals(newState.otherRelevantProperty);
    if (hasChanged) {
        previousState = newState;
    }
    return hasChanged;
}

private void notifyLinkPropertyChange(DataCallState state) {
    for (LinkPropertyChangeListener listener : listeners) {
        listener.onLinkPropertyChanged(state);
    }
}

private void notifyListeners() {
    for (Listener listener : listeners) {
        listener.onDataStateChange();
    }
}

private void logError(String message) {
    String previousStateInfo = (previousState != null) ? previousState.toString() : "No previous state";
    System.err.println("Error: " + message + " | Previous State: " + previousStateInfo);
}

//<End of snippet n. 0>