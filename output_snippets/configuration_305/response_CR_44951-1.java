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
        // Handle exception
        return;
    }

    boolean isActiveOrDormantConnectionPresent = false;

    for (int index = 0; index < dataCallStates.size(); index++) {
        boolean connectionState = dataCallStates.get(index).active;
        
        if (connectionState != DATA_CONNECTION_ACTIVE_PH_LINK_INACTIVE) {
            isActiveOrDormantConnectionPresent = true;
            break;
        }
    }

    if (isActiveOrDormantConnectionPresent) {
        notifyActiveConnectionChange(dataCallStates);
    }

    compareAndNotifyLinkProperties(dataCallStates);
}

private void notifyActiveConnectionChange(ArrayList<DataCallState> dataCallStates) {
    // Logic to notify about active connection change
}

private void compareAndNotifyLinkProperties(ArrayList<DataCallState> dataCallStates) {
    // Logic to compare current link properties with previous ones
    // Trigger notifications if discrepancies are detected
}

//<End of snippet n. 0>