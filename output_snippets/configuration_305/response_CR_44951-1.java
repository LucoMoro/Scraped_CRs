//<Beginning of snippet n. 0>

import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.DataCallState;
import com.android.internal.telephony.DataConnection.FailCause;
import com.android.internal.telephony.DataConnection;
import com.android.internal.telephony.DataConnectionAc;
import com.android.internal.telephony.DataConnectionTracker;
import java.util.ArrayList;

protected void onDataStateChanged(AsyncResult ar) {
    ArrayList<DataCallState> dataCallStates = (ArrayList<DataCallState>)(ar.result);
    if (ar.exception != null) {
        // Error handling logic to capture link property changes
        notifyLinkPropertyChange(ar.exception);
        return;
    }

    boolean isActiveOrDormantConnectionPresent = false;
    boolean propertiesChanged = false;

    for (int index = 0; index < dataCallStates.size(); index++) {
        DataCallState currentState = dataCallStates.get(index);
        boolean connectionState = currentState.active;

        if (connectionState != DATA_CONNECTION_ACTIVE_PH_LINK_INACTIVE) {
            isActiveOrDormantConnectionPresent = true;
        }

        // Logic to check for changes in link properties
        if (hasLinkPropertiesChanged(previousDataState, currentState)) {
            propertiesChanged = true;
            notifyLinkPropertyChange(currentState);
        }
    }

    if (isActiveOrDormantConnectionPresent && propertiesChanged) {
        // Handle case where active connection remains but link properties have changed
        handleActiveConnectionChange();
    }
    
    // Store current state for future comparisons
    previousDataState = dataCallStates;
}

private void notifyLinkPropertyChange(Object state) {
    // Implementation for notifying changes
}

private boolean hasLinkPropertiesChanged(ArrayList<DataCallState> previousStates, DataCallState currentState) {
    // Compare with the previous states to detect changes
    // Placeholder for actual comparison logic
    return true; // Modify as per actual comparison logic
}

private void handleActiveConnectionChange() {
    // Logic to handle when an active connection detects changes in link properties
}

//<End of snippet n. 0>