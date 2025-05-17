//<Beginning of snippet n. 0>

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.DataCallState;
import com.android.internal.telephony.DataConnection.FailCause;
import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.DataConnectionAc;
import com.android.internal.telephony.DataConnectionTracker;

private DataCallState lastDataCallState = new DataCallState(); // Initialize to avoid null comparison

protected void onDataStateChanged(AsyncResult ar) {
    ArrayList<DataCallState> dataCallStates = (ArrayList<DataCallState>)(ar.result);
    
    if (ar.exception != null) {
        logException(ar.exception); // Log exception for better debugging
        return;
    }

    boolean isActiveOrDormantConnectionPresent = false;

    for (int index = 0; index < dataCallStates.size(); index++) {
        DataCallState currentDataCallState = dataCallStates.get(index);
        boolean connectionState = currentDataCallState.active;

        if (connectionState != DATA_CONNECTION_ACTIVE_PH_LINK_INACTIVE) {
            isActiveOrDormantConnectionPresent = true;
        }

        // Comparison mechanism
        if (lastDataCallState != null && !lastDataCallState.equals(currentDataCallState)) {
            // Trigger notifications for link property changes
            notifyLinkPropertyChange(lastDataCallState, currentDataCallState);
        }
        lastDataCallState = currentDataCallState;
    }
    
    // Ensure to notify link property changes if needed regardless of connection state
    for (DataCallState state : dataCallStates) {
        if (!state.equals(lastDataCallState)) {
            notifyLinkPropertyChange(lastDataCallState, state);
        }
    }
}

// Notification logic method to be defined
private void notifyLinkPropertyChange(DataCallState oldState, DataCallState newState) {
    // Implementation for notifying changes
}

private void logException(Throwable exception) {
    // Implementation for logging exceptions
}

//<End of snippet n. 0>